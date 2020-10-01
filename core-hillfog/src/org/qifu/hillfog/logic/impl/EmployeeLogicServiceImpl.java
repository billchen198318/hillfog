/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.hillfog.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.entity.TbRole;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IAccountService;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfEmployeeOrg;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IEmployeeLogicService;
import org.qifu.hillfog.service.IEmployeeOrgService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class EmployeeLogicServiceImpl extends BaseLogicService implements IEmployeeLogicService {
	protected Logger logger=LogManager.getLogger(EmployeeLogicServiceImpl.class);
	
	private static final int MAX_LENGTH = 500;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IEmployeeOrgService<HfEmployeeOrg, String> employeeOrgService;
	
	@Autowired
	IAccountService<TbAccount, String> accountService;
	
    @Autowired
    PasswordEncoder passwordEncoder;
	
    @Autowired
    IRoleLogicService roleLogicService;
    
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )    
	@Override
	public DefaultResult<HfEmployee> create(HfEmployee employee, String password, List<String> orgInputAutocompleteList) throws ServiceException, Exception {
		if (null == employee || StringUtils.isBlank(password) || null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		TbAccount account = new TbAccount();
		account.setAccount(employee.getAccount());
		account.setOnJob(YesNo.YES);
		account.setPassword( this.passwordEncoder.encode(password) );
		account = this.accountService.insert(account).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("empId", employee.getEmpId());
		if (this.employeeService.count(paramMap) > 0) {
			throw new ServiceException( "Please change Id, " + BaseSystemMessage.dataIsExist() );
		}
		this.setStringValueMaxLength(employee, "description", MAX_LENGTH);
		DefaultResult<HfEmployee> result = this.employeeService.insert(employee);
		if ( result.getValue() == null ) {
			throw new ServiceException(result.getMessage());
		}
		employee = result.getValue();
		this.createEmployeeOrganization(employee, orgInputAutocompleteList);
		TbRole roleEntity = this.roleLogicService.getDefaultUserRoleEntity();
		List<String> roles = new ArrayList<String>();
		roles.add(roleEntity.getOid());
		this.roleLogicService.updateUserRole(account.getOid(), roles);
		return result;
	}
	
	private void createEmployeeOrganization(HfEmployee employee, List<String> orgInputAutocompleteList) throws ServiceException, Exception {
		int size = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (String empOrgStr : orgInputAutocompleteList) {
			String orgId = StringUtils.deleteWhitespace(empOrgStr.split("/")[0]);
			if (this.isBlank(orgId)) {
				continue;
			}
			paramMap.put("orgId", orgId);
			if (this.orgDeptService.count(paramMap) < 1) {
				throw new ServiceException( BaseSystemMessage.dataErrors() );
			}
			HfEmployeeOrg employeeOrg = new HfEmployeeOrg();
			employeeOrg.setAccount(employee.getAccount());
			employeeOrg.setOrgId(orgId);
			this.employeeOrgService.insert(employeeOrg);
			size++;
		}
		if (size < 1) {
			throw new ServiceException( "No create employee's organization, " + BaseSystemMessage.dataErrors() );
		}
		paramMap.clear();
		paramMap = null;
	}
	
	private void deleteEmployeeOrganization(HfEmployee employee) throws ServiceException, Exception {
		if (null == employee || this.isBlank(employee.getAccount())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("account", employee.getAccount());
		List<HfEmployeeOrg> employeeOrgList = this.employeeOrgService.selectListByParams(paramMap).getValueEmptyThrowMessage();
		for (HfEmployeeOrg empOrg : employeeOrgList) {
			this.employeeOrgService.delete(empOrg);
		}
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<HfEmployee> update(HfEmployee employee, String password, List<String> orgInputAutocompleteList) throws ServiceException, Exception {
		if (null == employee || null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		HfEmployee oldEmployee = this.employeeService.selectByEntityPrimaryKey(employee).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!oldEmployee.getEmpId().equals(employee.getEmpId())) {
			paramMap.put("empId", employee.getEmpId());
			if (this.employeeService.count(paramMap) > 0) {
				throw new ServiceException( "Please change Id, " + BaseSystemMessage.dataIsExist() );
			}			
		}
		paramMap.clear();
		DefaultResult<HfEmployee> result = this.employeeService.update(employee);
		this.deleteEmployeeOrganization(employee);
		this.createEmployeeOrganization(oldEmployee, orgInputAutocompleteList);
		if (!StringUtils.isBlank(password)) {
			TbAccount account = new TbAccount();
			account.setAccount(employee.getAccount());
			account = this.accountService.selectByUniqueKey(account).getValueEmptyThrowMessage();
			account.setPassword(  this.passwordEncoder.encode(password) );
			this.accountService.update(account);
		}
		return result;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<Boolean> delete(HfEmployee employee) throws ServiceException, Exception {
		if (null == employee || this.isBlank(employee.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		employee = this.employeeService.selectByEntityPrimaryKey(employee).getValueEmptyThrowMessage();
		TbAccount account = new TbAccount();
		account.setAccount(employee.getAccount());
		account = this.accountService.selectByUniqueKey(account).getValueEmptyThrowMessage();
		this.deleteEmployeeOrganization(employee);
		this.employeeService.delete(employee);
		return this.accountService.delete(account);
	}
	
}
