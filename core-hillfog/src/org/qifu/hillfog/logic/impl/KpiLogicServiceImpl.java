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
import org.qifu.base.service.BaseLogicService;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfKpiEmpl;
import org.qifu.hillfog.entity.HfKpiOrga;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.logic.IKpiLogicService;
import org.qifu.hillfog.model.KpiBasicCode;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.hillfog.service.IFormulaService;
import org.qifu.hillfog.service.IKpiEmplService;
import org.qifu.hillfog.service.IKpiOrgaService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IMeasureDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class KpiLogicServiceImpl extends BaseLogicService implements IKpiLogicService {
	protected Logger logger = LogManager.getLogger(KpiLogicServiceImpl.class);
	
	private static final int MAX_LENGTH = 500;
	
	@Autowired
	IFormulaService<HfFormula, String> formulaService;
	
	@Autowired
	IAggregationMethodService<HfAggregationMethod, String> aggregationMethodService;
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IKpiEmplService<HfKpiEmpl, String> kpiEmplService;
	
	@Autowired
	IKpiOrgaService<HfKpiOrga, String> kpiOrgaService;
	
	@Autowired
	IMeasureDataService<HfMeasureData, String> measureDataService;
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<HfKpi> create(HfKpi kpi, String forOid, String aggrOid, List<String> orgInputAutocompleteList, List<String> empInputAutocompleteList) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(forOid) || this.isBlank(aggrOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(kpi.getDataType()) || KpiBasicCode.DATA_TYPE_DEPARTMENT.equals(kpi.getDataType())) {
			if (null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
				throw new ServiceException( BaseSystemMessage.parameterBlank() );
			}
		}
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(kpi.getDataType()) || KpiBasicCode.DATA_TYPE_PERSONAL.equals(kpi.getDataType())) {
			if (null == empInputAutocompleteList || empInputAutocompleteList.size() < 1) {
				throw new ServiceException( BaseSystemMessage.parameterBlank() ); 
			}
		}		
		HfFormula formula = this.formulaService.selectByPrimaryKey(forOid).getValueEmptyThrowMessage();
		HfAggregationMethod aggrMethod = this.aggregationMethodService.selectByPrimaryKey(aggrOid).getValueEmptyThrowMessage();
		kpi.setForId(formula.getForId());
		kpi.setAggrId(aggrMethod.getAggrId());
		this.setStringValueMaxLength(kpi, "description", MAX_LENGTH);
		DefaultResult<HfKpi> result = this.kpiService.insert(kpi);
		kpi = result.getValue();
		this.createKpiDepartment(kpi, orgInputAutocompleteList);
		this.createKpiOwner(kpi, empInputAutocompleteList);
		return result;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<HfKpi> update(HfKpi kpi, String forOid, String aggrOid, List<String> orgInputAutocompleteList, List<String> empInputAutocompleteList) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(forOid) || this.isBlank(aggrOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		if (this.isBlank(kpi.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterIncorrect() );
		}
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(kpi.getDataType()) || KpiBasicCode.DATA_TYPE_DEPARTMENT.equals(kpi.getDataType())) {
			if (null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
				throw new ServiceException( BaseSystemMessage.parameterBlank() );
			}
		}
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(kpi.getDataType()) || KpiBasicCode.DATA_TYPE_PERSONAL.equals(kpi.getDataType())) {
			if (null == empInputAutocompleteList || empInputAutocompleteList.size() < 1) {
				throw new ServiceException( BaseSystemMessage.parameterBlank() ); 
			}
		}		
		HfKpi oldKpi = this.kpiService.selectByEntityPrimaryKey(kpi).getValueEmptyThrowMessage();
		HfFormula formula = this.formulaService.selectByPrimaryKey(forOid).getValueEmptyThrowMessage();
		HfAggregationMethod aggrMethod = this.aggregationMethodService.selectByPrimaryKey(aggrOid).getValueEmptyThrowMessage();
		kpi.setForId(formula.getForId());
		kpi.setAggrId(aggrMethod.getAggrId());
		kpi.setId( oldKpi.getId() );
		this.setStringValueMaxLength(kpi, "description", MAX_LENGTH);
		DefaultResult<HfKpi> result = this.kpiService.update(kpi);
		kpi = result.getValue();
		this.deleteKpiDepartment(kpi);
		this.deleteKpiOwner(kpi);
		this.createKpiDepartment(kpi, orgInputAutocompleteList);
		this.createKpiOwner(kpi, empInputAutocompleteList);		
		return result;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<Boolean> delete(HfKpi kpi) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(kpi.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		kpi = this.kpiService.selectByEntityPrimaryKey(kpi).getValueEmptyThrowMessage();
		this.deleteKpiDepartment(kpi);
		this.deleteKpiOwner(kpi);
		this.measureDataService.deleteByKpiId(kpi.getId());
		return this.kpiService.delete(kpi);
	}
	
	private void deleteKpiOwner(HfKpi kpi) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(kpi.getId())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpi.getId());
		List<HfKpiEmpl> kpiEmplList = this.kpiEmplService.selectListByParams(paramMap).getValue();
		if (null == kpiEmplList) {
			return;
		}
		for (HfKpiEmpl kpiEmpl : kpiEmplList) {
			this.kpiEmplService.delete(kpiEmpl);
		}
	}
	
	private void deleteKpiDepartment(HfKpi kpi) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(kpi.getId())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpi.getId());
		List<HfKpiOrga> kpiOrgaList = this.kpiOrgaService.selectListByParams(paramMap).getValue();
		if (null == kpiOrgaList) {
			return;
		}
		for (HfKpiOrga kpiOrga : kpiOrgaList) {
			this.kpiOrgaService.delete(kpiOrga);
		}
	}
	
	private void createKpiOwner(HfKpi kpi, List<String> empInputAutocompleteList) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(kpi.getId())) {
			return;
		}
		if (null == empInputAutocompleteList || empInputAutocompleteList.size() < 1) {
			return;
		}
		for (String str : empInputAutocompleteList) {
			String account = StringUtils.deleteWhitespace(str.split("/")[1]);
			if (this.isBlank(account)) {
				continue;
			}
			HfKpiEmpl kpiEmpl = new HfKpiEmpl();
			kpiEmpl.setAccount(account);
			kpiEmpl.setKpiId(kpi.getId());
			this.kpiEmplService.insert(kpiEmpl);
		}
	}
	
	private void createKpiDepartment(HfKpi kpi, List<String> orgInputAutocompleteList) throws ServiceException, Exception {
		if (null == kpi || this.isBlank(kpi.getId())) {
			return;
		}
		if (null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
			return;
		}		
		for (String str : orgInputAutocompleteList) {
			String orgId = StringUtils.deleteWhitespace(str.split("/")[0]);
			if (this.isBlank(orgId)) {
				continue;
			}			
			HfKpiOrga kpiOrga = new HfKpiOrga();
			kpiOrga.setOrgId(orgId);
			kpiOrga.setKpiId(kpi.getId());
			this.kpiOrgaService.insert(kpiOrga);
		}
	}
	
}
