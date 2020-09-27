/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.logic.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.entity.TbRole;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.entity.TbSysMenuRole;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.entity.TbUserRole;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IAccountService;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.IRoleService;
import org.qifu.core.service.ISysCodeService;
import org.qifu.core.service.ISysMenuRoleService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class RoleLogicServiceImpl extends BaseLogicService implements IRoleLogicService {
	protected Logger logger=LogManager.getLogger(RoleLogicServiceImpl.class);
	
	private final static String DEFAULT_ROLE_CODE = "CMM_CONF001"; // 預設要套用role的 TB_SYS_CODE.CODE = 'BSC_CONF001' and TYPE='BSC'
	private final static String DEFAULT_ROLE_CODE_TYPE = "CMM"; // 預設要套用role的 TB_SYS_CODE.CODE = 'BSC_CONF001' and TYPE='BSC'	
	private static final int MAX_DESCRIPTION_LENGTH = 500;
	
	@Autowired
	ISysCodeService<TbSysCode, String> sysCodeService;
	
	@Autowired
	IRoleService<TbRole, String> roleService;
	
	@Autowired
	IRolePermissionService<TbRolePermission, String> rolePermissionService;
	
	@Autowired
	IUserRoleService<TbUserRole, String> userRoleService;
	
	@Autowired
	IAccountService<TbAccount, String> accountService;
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISysMenuRoleService<TbSysMenuRole, String> sysMenuRoleService; 
	
	public RoleLogicServiceImpl() {
		super();
	}
	
	/**
	 * 建立 TB_ROLE 資料
	 * 
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbRole> create(TbRole role) throws ServiceException, Exception {
		if (role==null || super.isBlank(role.getRole())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		role.setDescription( super.defaultString(role.getDescription()) );
		this.setStringValueMaxLength(role, "description", MAX_DESCRIPTION_LENGTH);
		return this.roleService.insert(role);
	}

	/**
	 * 更新 TB_ROLE 資料
	 * 
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<TbRole> update(TbRole role) throws ServiceException, Exception {
		
		if (role==null || super.isBlank(role.getRole())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		role.setDescription( super.defaultString(role.getDescription()) );
		this.setStringValueMaxLength(role, "description", MAX_DESCRIPTION_LENGTH);
		return this.roleService.update(role);
	}

	/**
	 * 刪除 TB_ROLE, TB_ROLE_PERMISSION, TB_USER_ROLE 資料
	 * 
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> delete(TbRole role) throws ServiceException, Exception {
		if (role==null || super.isBlank(role.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbRole> rResult = this.roleService.selectByEntityPrimaryKey(role);
		if (rResult.getValue()==null) {
			throw new ServiceException(rResult.getMessage());
		}
		role = rResult.getValue();
		if (Constants.SUPER_ROLE_ADMIN.equals(role.getRole()) || Constants.SUPER_ROLE_ALL.equals(role.getRole())) {			
			throw new ServiceException("Administrator or super role cannot delete!");
		}		
		String defaultUserRole = this.getDefaultUserRole();
		if (role.getRole().equals(defaultUserRole)) {
			throw new ServiceException("Default user role: " + defaultUserRole + " cannot delete!");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role", role.getRole());		
		this.deleteRolePermission(params);
		this.deleteUserRole(params);
		this.deleteSysMenuRole(params);
		return roleService.delete(role);
	}
	
	private void deleteRolePermission(Map<String, Object> params) throws ServiceException, Exception {
		DefaultResult<List<TbRolePermission>> permListResult = this.rolePermissionService.selectListByParams(params);
		if (null == permListResult.getValue() || permListResult.getValue().size() < 1) {
			return;
		}
		for (TbRolePermission rolePerm : permListResult.getValue()) {
			this.rolePermissionService.delete(rolePerm);
		}
	}
	
	private void deleteUserRole(Map<String, Object> params) throws ServiceException, Exception {
		DefaultResult<List<TbUserRole>> userRoleListResult = this.userRoleService.selectListByParams(params);
		if (null == userRoleListResult.getValue() || userRoleListResult.getValue().size() < 1) {
			return;
		}
		for (TbUserRole userRole : userRoleListResult.getValue()) {
			this.userRoleService.delete(userRole);
		}
	}
	
	private void deleteSysMenuRole(Map<String, Object> params) throws ServiceException, Exception {
		DefaultResult<List<TbSysMenuRole>> menuRoleListResult = this.sysMenuRoleService.selectListByParams(params);
		if (menuRoleListResult.getValue() == null || menuRoleListResult.getValue().size() < 1) {
			return;
		}
		for (TbSysMenuRole sysMenuRole : menuRoleListResult.getValue()) {
			this.sysMenuRoleService.delete(sysMenuRole);
		}
	}

	/**
	 * 產生 TB_ROLE_PERMISSION 資料
	 * 
	 * @param permission
	 * @param roleOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbRolePermission> createPermission(TbRolePermission permission, String roleOid) throws ServiceException, Exception {
		if ( super.isBlank(roleOid) || permission==null || super.isBlank(permission.getPermission()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbRole> rResult = this.roleService.selectByPrimaryKey(roleOid);
		if (rResult.getValue()==null) {
			throw new ServiceException(rResult.getMessage());
		}
		TbRole role = rResult.getValue();
		if (Constants.SUPER_ROLE_ADMIN.equals(role.getRole()) || Constants.SUPER_ROLE_ALL.equals(role.getRole())) {			
			throw new ServiceException("Administrator or super role no need to set permission!");
		}
		permission.setRole(role.getRole());
		if ( super.defaultString(permission.getDescription()).length()>MAX_DESCRIPTION_LENGTH ) {
			permission.setDescription( permission.getDescription().substring(0, MAX_DESCRIPTION_LENGTH) );
		}
		return this.rolePermissionService.insert(permission);
	}

	/**
	 * 刪除 TB_ROLE_PERMISSION 資料
	 * 
	 * @param permission
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> deletePermission(TbRolePermission permission) throws ServiceException, Exception {
		if ( null==permission || super.isBlank(permission.getOid()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		return rolePermissionService.delete(permission);
	}

	/**
	 * 找出全部的role與某帳戶下的role
	 * 
	 * map 中的  key 
	 * enable	- 帳戶下的role
	 * all	- 所有role
	 * 
	 * @param accountOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Override
	public Map<String, List<TbRole>> findForAccountRoleEnableAndAll(String accountOid) throws ServiceException, Exception {
		if (super.isBlank(accountOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbAccount> aResult = this.accountService.selectByPrimaryKey(accountOid);
		if (aResult.getValue()==null) {
			throw new ServiceException(aResult.getMessage());
		}
		TbAccount account = aResult.getValue();
		Map<String, List<TbRole>> roleMap = new HashMap<String, List<TbRole>>();
		List<TbRole> enableRole = this.roleService.findForAccount(account.getAccount());
		List<TbRole> allRole = this.roleService.selectList().getValue();
		roleMap.put("enable", enableRole);
		roleMap.put("all", allRole);
		return roleMap;
	}

	/**
	 * 更新帳戶的role
	 * 
	 * @param accountOid
	 * @param roles
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> updateUserRole(String accountOid, List<String> roles) throws ServiceException, Exception {
		if (super.isBlank(accountOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbAccount> aResult = this.accountService.selectByPrimaryKey(accountOid);
		if (aResult.getValue()==null) {
			throw new ServiceException(aResult.getMessage());
		}
		TbAccount account = aResult.getValue();
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		result.setValue(false);
		result.setMessage(BaseSystemMessage.updateFail());		
		this.deleteUserRoleByAccount(account);
		for (int i=0; roles!=null && i<roles.size(); i++) {
			String roleOid = roles.get(i).trim();
			if (super.isBlank(roleOid)) {
				continue;
			}
			DefaultResult<TbRole> rResult = this.roleService.selectByPrimaryKey(roleOid);
			if (rResult.getValue()==null) {
				throw new ServiceException(rResult.getMessage());
			}
			TbRole role = rResult.getValue();
			TbUserRole userRole = new TbUserRole();
			userRole.setAccount(account.getAccount());
			userRole.setRole(role.getRole());
			userRole.setDescription("");
			DefaultResult<TbUserRole> urResult = this.userRoleService.insert(userRole);
			if (urResult.getValue()==null) {
				throw new ServiceException(urResult.getMessage());
			}
		}
		result.setValue(true);
		result.setMessage(BaseSystemMessage.updateSuccess());
		return result;
	}
	
	private void deleteUserRoleByAccount(TbAccount account) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("account", account.getAccount());
		DefaultResult<List<TbUserRole>> userRoleListResult = this.userRoleService.selectListByParams(paramMap);
		if (userRoleListResult.getValue() == null || userRoleListResult.getValue().size() < 1) {
			return;
		}
		for (TbUserRole userRole : userRoleListResult.getValue()) {
			this.userRoleService.delete(userRole);
		}
	}

	/**
	 * 找出全部的role與某程式menu所屬的role
	 * 
	 * map 中的  key 
	 * enable	- 程式menu的role
	 * all	- 所有role
	 * 
	 * @param programOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Override
	public Map<String, List<TbRole>> findForProgramRoleEnableAndAll(String programOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(programOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysProg> spResult = this.sysProgService.selectByPrimaryKey(programOid);
		if (spResult.getValue()==null) {
			throw new ServiceException(spResult.getMessage());
		}
		TbSysProg sysProg = spResult.getValue();
		Map<String, List<TbRole>> roleMap = new HashMap<String, List<TbRole>>();
		List<TbRole> enableRole = this.roleService.findForProgram(sysProg.getProgId());
		List<TbRole> allRole = this.roleService.selectList().getValue();
		roleMap.put("enable", enableRole);
		roleMap.put("all", allRole);				
		return roleMap;
	}
	
	/**
	 * 更新存在選單中程式的選單所屬 role
	 * 
	 * @param progOid
	 * @param roles
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> updateMenuRole(String progOid, List<String> roles) throws ServiceException, Exception {
		if (super.isBlank(progOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysProg> spResult = this.sysProgService.selectByPrimaryKey(progOid);
		if (spResult.getValue()==null) {
			throw new ServiceException(spResult.getMessage());
		}
		TbSysProg sysProg = spResult.getValue();
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		result.setValue(false);
		result.setMessage(BaseSystemMessage.updateSuccess());			
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progId", sysProg.getProgId());
		DefaultResult<List<TbSysMenuRole>> sysMenuRoleListResult = this.sysMenuRoleService.selectListByParams(paramMap);
		for (int i=0; sysMenuRoleListResult.getValue() != null && i < sysMenuRoleListResult.getValue().size(); i++) {
			TbSysMenuRole sysMenuRole = sysMenuRoleListResult.getValue().get(i);
			this.sysMenuRoleService.delete(sysMenuRole);
		}
		for (int i=0; roles != null && i < roles.size(); i++) {
			String roleOid = roles.get(i).trim();
			if (super.isBlank(roleOid)) {
				continue;
			}
			DefaultResult<TbRole> rResult = this.roleService.selectByPrimaryKey(roleOid);
			if (rResult.getValue()==null) {
				throw new ServiceException(rResult.getMessage());
			}
			TbRole role = rResult.getValue();
			TbSysMenuRole sysMenuRole = new TbSysMenuRole();
			sysMenuRole.setProgId(sysProg.getProgId());
			sysMenuRole.setRole(role.getRole());
			DefaultResult<TbSysMenuRole> smrResult = this.sysMenuRoleService.insert(sysMenuRole);
			if (smrResult.getValue()==null) {
				throw new ServiceException(smrResult.getMessage());
			}			
		}
		result.setValue(true);
		result.setMessage(BaseSystemMessage.updateSuccess());
		return result;
	}
	
	/**
	 * 拷備一份role
	 * 
	 * @param fromRoleOid
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbRole> copyAsNew(String fromRoleOid, TbRole role) throws ServiceException, Exception {
		if (role==null || super.isBlank(role.getRole()) || super.isBlank(fromRoleOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		super.setStringValueMaxLength(role, "description", MAX_DESCRIPTION_LENGTH);
		DefaultResult<TbRole> result = this.roleService.insert(role);
		if (result.getValue() == null) {
			throw new ServiceException(result.getMessage());
		}
		DefaultResult<TbRole> fromResult = this.roleService.selectByPrimaryKey(fromRoleOid);
		if ( fromResult.getValue() == null ) {
			throw new ServiceException( fromResult.getMessage() );
		}
		TbRole oldRole = fromResult.getValue();		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("role", oldRole.getRole());
		DefaultResult<List<TbRolePermission>> permListResult = this.rolePermissionService.selectListByParams(paramMap);
		for (int i=0; permListResult.getValue() != null && i < permListResult.getValue().size(); i++) {
			TbRolePermission perm = permListResult.getValue().get(i);
			perm.setOid(null);
			perm.setRole( result.getValue().getRole() );
			perm.setCuserid(null);
			perm.setCdate(null);
			this.rolePermissionService.insert(perm);
		}
		// 選單menu role 也copy一份
		DefaultResult<List<TbSysMenuRole>> menuRolesResult = this.sysMenuRoleService.selectListByParams(paramMap);
		for (int i=0; menuRolesResult.getValue() != null && i < menuRolesResult.getValue().size(); i++) {
			TbSysMenuRole menuRole = menuRolesResult.getValue().get(i);
			menuRole.setOid(null);
			menuRole.setRole( result.getValue().getRole() );
			menuRole.setCuserid(null);
			menuRole.setCdate(null);
			this.sysMenuRoleService.insert(menuRole);
		}
		return result;
	}
	
	/**
	 * 使用者設的role-id(角色), 它設定在 tb_sys_code中
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	public String getDefaultUserRole() throws ServiceException, Exception {
		String role = "";
		TbSysCode sysCode = new TbSysCode();
		sysCode.setType(DEFAULT_ROLE_CODE_TYPE);
		sysCode.setCode(DEFAULT_ROLE_CODE);
		sysCode = this.sysCodeService.selectByUniqueKey(sysCode).getValueEmptyThrowMessage();
		role = sysCode.getParam1();
		if (super.isBlank(role)) {
			throw new ServiceException(BaseSystemMessage.dataErrors());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("role", role);
		if ( this.roleService.count(paramMap) != 1 ) {
			throw new ServiceException(BaseSystemMessage.dataErrors());
		}
		return role;
	}	
	
	/**
	 * 使用者設的role(角色), 它設定在 tb_sys_code中
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	public TbRole getDefaultUserRoleEntity() throws ServiceException, Exception {
		TbSysCode sysCode = new TbSysCode();
		sysCode.setType(DEFAULT_ROLE_CODE_TYPE);
		sysCode.setCode(DEFAULT_ROLE_CODE);
		sysCode = this.sysCodeService.selectByUniqueKey(sysCode).getValueEmptyThrowMessage();
		if (super.isBlank(sysCode.getParam1())) {
			throw new ServiceException(BaseSystemMessage.dataErrors());
		}
		TbRole role = new TbRole();
		role.setRole( sysCode.getParam1() );
		role = this.roleService.selectByUniqueKey(role).getValueEmptyThrowMessage();
		return role;
	}
	
}
