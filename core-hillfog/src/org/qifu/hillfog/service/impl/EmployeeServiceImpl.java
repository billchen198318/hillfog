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
package org.qifu.hillfog.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.model.SortType;
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.mapper.HfEmployeeMapper;
import org.qifu.hillfog.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class EmployeeServiceImpl extends BaseService<HfEmployee, String> implements IEmployeeService<HfEmployee, String> {
	
	@Autowired
	HfEmployeeMapper employeeMapper;
	
	@Override
	protected IBaseMapper<HfEmployee, String> getBaseMapper() {
		return this.employeeMapper;
	}

	@Override
	public List<String> findInputAutocomplete() throws ServiceException, Exception {
		List<String> dataList = new ArrayList<String>();
		List<HfEmployee> empList = this.selectList("EMP_ID", SortType.ASC).getValue();
		if (null == empList) {
			return dataList;
		}
		for (HfEmployee employee : empList) {
			dataList.add( this.getPagefieldValue(employee) );
		}
		return dataList;
	}
	
	private String getPagefieldValue(HfEmployee employee) {
		return employee.getEmpId() + " / " + employee.getAccount() + " / " + this.replaceAllContent(employee.getName());
	}	
	
	private String replaceAllContent(String srcStr) {
		return StringUtils.defaultString(srcStr).trim()
				.replaceAll("/", "").replaceAll(",", "").replaceAll("\"", "")
				.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "")
				.replaceAll(">", "").replaceAll(";", "").replaceAll("'", "");
	}	
	
}
