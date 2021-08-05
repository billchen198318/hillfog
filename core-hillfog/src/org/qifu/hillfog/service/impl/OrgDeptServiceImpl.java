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
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.mapper.HfOrgDeptMapper;
import org.qifu.hillfog.service.IOrgDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class OrgDeptServiceImpl extends BaseService<HfOrgDept, String> implements IOrgDeptService<HfOrgDept, String> {
	
	@Autowired
	HfOrgDeptMapper orgDeptMapper;
	
	@Override
	protected IBaseMapper<HfOrgDept, String> getBaseMapper() {
		return this.orgDeptMapper;
	}

	@Override
	public List<String> findInputAutocomplete() throws ServiceException, Exception {
		List<String> dataList = new ArrayList<String>();
		List<HfOrgDept> orgDeptList = this.selectList("ORG_ID", SortType.ASC).getValue();
		if (null == orgDeptList) {
			return dataList;
		}
		for (HfOrgDept orgDept : orgDeptList) {
			dataList.add( this.getPagefieldValue(orgDept) );
		}
		return dataList;
	}

	@Override
	public List<String> findInputAutocompleteByAccount(String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(account)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		List<String> dataList = new ArrayList<String>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("account", account);
		List<HfOrgDept> orgDeptList = this.orgDeptMapper.findListByAccount(paramMap);
		for (HfOrgDept orgDept : orgDeptList) {
			dataList.add( this.getPagefieldValue(orgDept) );
		}
		return dataList;		
	}
	
	private String getPagefieldValue(HfOrgDept orgDept) {
		return orgDept.getOrgId() + " / " + this.replaceAllContent(orgDept.getName());
	}
	
	private String replaceAllContent(String srcStr) {
		return StringUtils.defaultString(srcStr).trim()
				.replaceAll("/", "").replaceAll(",", "").replaceAll("\"", "")
				.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("<", "")
				.replaceAll(">", "").replaceAll(";", "").replaceAll("'", "");
	}

	/**
	 * KPI的負責部門
	 * 
	 * @param kpiId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public DefaultResult<List<HfOrgDept>> findKpiDepartment(String kpiId) throws ServiceException, Exception {
		if (StringUtils.isBlank(kpiId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<List<HfOrgDept>> result = new DefaultResult<List<HfOrgDept>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpiId);
		List<HfOrgDept> dataList = this.orgDeptMapper.findKpiDepartment(paramMap);
		if (dataList != null && dataList.size() > 0) {
			result.setValue(dataList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	/**
	 * KPI的負責部門
	 * 
	 * @param kpiId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByKpiId(String kpiId) throws ServiceException, Exception {
		DefaultResult<List<HfOrgDept>> result = this.findKpiDepartment(kpiId);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(result.getValue())) {
			return dataList;
		}		
		for (HfOrgDept orgDept : result.getValue()) {
			dataList.add( this.getPagefieldValue(orgDept) );
		}
		return dataList;
	}

	/**
	 * Objective的負責部門
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
		List<HfOrgDept> orgList = this.orgDeptMapper.findObjectiveDepartment(paramMap);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(orgList)) {
			return dataList;
		}		
		for (HfOrgDept orgDept : orgList) {
			dataList.add( this.getPagefieldValue(orgDept) );
		}
		return dataList;
	}

	/**
	 * Scorecard擁有的KPI的負責部門
	 * 
	 * @param scorecardOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	@Override
	public DefaultResult<List<HfOrgDept>> findScorecardDepartment(String scorecardOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(scorecardOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<List<HfOrgDept>> result = new DefaultResult<List<HfOrgDept>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("oid", scorecardOid);
		List<HfOrgDept> dataList = this.orgDeptMapper.findScorecardKpisDepartment(paramMap);
		if (dataList != null && dataList.size() > 0) {
			result.setValue(dataList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	/**
	 * Scorecard擁有的KPI的負責部門
	 * 
	 * @param scorecardOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<String> findInputAutocompleteByScorecard(String scorecardOid) throws ServiceException, Exception {
		DefaultResult<List<HfOrgDept>> result = this.findScorecardDepartment(scorecardOid);
		List<String> dataList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(result.getValue())) {
			return dataList;
		}		
		for (HfOrgDept orgDept : result.getValue()) {
			dataList.add( this.getPagefieldValue(orgDept) );
		}
		return dataList;
	}	
	
}
