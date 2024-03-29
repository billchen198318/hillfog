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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
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
	
	public String getPagefieldValue(HfEmployee employee) {
		return employee.getEmpId() + " / " + employee.getAccount() + " / " + this.replaceAllContent(employee.getName());
	}	
	
	private String replaceAllContent(String srcStr) {
		return StringUtils.defaultString(srcStr).trim()
				.replaceAll("/", "").replaceAll(",", "").replaceAll("\"", "")
				.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "")
				.replaceAll(">", "").replaceAll(";", "").replaceAll("'", "");
	}

	/**
	 * KPI的負責人
	 * 
	 * @param kpiId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public DefaultResult<List<HfEmployee>> findKpiOwner(String kpiId) throws ServiceException, Exception {
		if (StringUtils.isBlank(kpiId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<List<HfEmployee>> result = new DefaultResult<List<HfEmployee>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpiId);
		List<HfEmployee> dataList = this.employeeMapper.findKpiOwner(paramMap);
		if (dataList != null && dataList.size() > 0) {
			result.setValue(dataList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	/**
	 * KPI的負責人
	 * 
	 * @param kpiId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByKpiId(String kpiId) throws ServiceException, Exception {
		DefaultResult<List<HfEmployee>> result = this.findKpiOwner(kpiId);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(result.getValue())) {
			return dataList;
		}
		for (HfEmployee employee : result.getValue()) {
			dataList.add( this.getPagefieldValue(employee) );
		}		
		return dataList;
	}

	/**
	 * Objective的負責人
	 * 
	 * @param oid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByObjectiveOid(String oid) throws ServiceException, Exception {
		if (StringUtils.isBlank(oid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", oid);
		List<HfEmployee> empList = this.employeeMapper.findObjectiveOwner(paramMap);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(empList)) {
			return dataList;
		}
		for (HfEmployee employee : empList) {
			dataList.add( this.getPagefieldValue(employee) );
		}		
		return dataList;
	}

	/**
	 * PDCA 負責人
	 * 
	 * @param pdcaOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public DefaultResult<List<HfEmployee>> findPdcaOwner(String pdcaOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(pdcaOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}		
		DefaultResult<List<HfEmployee>> result = new DefaultResult<List<HfEmployee>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdcaOid);
		List<HfEmployee> dataList = this.employeeMapper.findPdcaOwner(paramMap);
		if (dataList != null && dataList.size() > 0) {
			result.setValue(dataList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	/**
	 * PDCA-item 負責人
	 * 
	 * @param pdcaOid
	 * @param itemOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public DefaultResult<List<HfEmployee>> findPdcaItemOwner(String pdcaOid, String itemOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(pdcaOid) || StringUtils.isBlank(itemOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}		
		DefaultResult<List<HfEmployee>> result = new DefaultResult<List<HfEmployee>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdcaOid);
		paramMap.put("itemOid", itemOid);
		List<HfEmployee> dataList = this.employeeMapper.findPdcaItemOwner(paramMap);
		if (dataList != null && dataList.size() > 0) {
			result.setValue(dataList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	/**
	 * PDCA 負責人
	 * 
	 * @param pdcaOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByPdcaOid(String pdcaOid) throws ServiceException, Exception {
		DefaultResult<List<HfEmployee>> result = this.findPdcaOwner(pdcaOid);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(result.getValue())) {
			return dataList;
		}
		for (HfEmployee employee : result.getValue()) {
			dataList.add( this.getPagefieldValue(employee) );
		}		
		return dataList;
	}

	/**
	 * PDCA-item 負責人
	 * 
	 * @param pdcaOid
	 * @param itemOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByPdcaItemOid(String pdcaOid, String itemOid) throws ServiceException, Exception {
		DefaultResult<List<HfEmployee>> result = this.findPdcaItemOwner(pdcaOid, itemOid);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(result.getValue())) {
			return dataList;
		}
		for (HfEmployee employee : result.getValue()) {
			dataList.add( this.getPagefieldValue(employee) );
		}		
		return dataList;
	}

	/**
	 * Scorecard擁有的KPI的負責人
	 * 
	 * @param scorecardOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public DefaultResult<List<HfEmployee>> findScorecardKpisOwner(String scorecardOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(scorecardOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<List<HfEmployee>> result = new DefaultResult<List<HfEmployee>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("oid", scorecardOid);
		List<HfEmployee> dataList = this.employeeMapper.findScorecardKpisOwner(paramMap);
		if (dataList != null && dataList.size() > 0) {
			result.setValue(dataList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	/**
	 * Scorecard擁有的KPI的負責人
	 * 
	 * @param scorecardOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByScorecard(String scorecardOid) throws ServiceException, Exception {
		DefaultResult<List<HfEmployee>> result = this.findScorecardKpisOwner(scorecardOid);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(result.getValue())) {
			return dataList;
		}
		for (HfEmployee employee : result.getValue()) {
			dataList.add( this.getPagefieldValue(employee) );
		}
		return dataList;
	}	
	
}
