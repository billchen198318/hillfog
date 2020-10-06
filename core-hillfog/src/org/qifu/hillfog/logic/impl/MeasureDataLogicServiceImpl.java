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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.logic.IMeasureDataLogicService;
import org.qifu.hillfog.model.KpiBasicCode;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IMeasureDataService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class MeasureDataLogicServiceImpl extends BaseLogicService implements IMeasureDataLogicService {
	protected Logger logger = LogManager.getLogger(MeasureDataLogicServiceImpl.class);
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;	
	
	@Autowired
	IMeasureDataService<HfMeasureData, String> measureDataService;	
	
	@ServiceMethodAuthority(type = {ServiceMethodType.INSERT, ServiceMethodType.UPDATE})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<Boolean> createOrUpdate(String kpiOid, String frequency, String date, String dataFor, String account, String orgId, List<Map<String, String>> fieldDataList) throws ServiceException, Exception {
		if (this.isBlank(kpiOid) || PleaseSelect.noSelect(frequency) || !SimpleUtils.isDate(date) || !KpiBasicCode.isDataFor(dataFor)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		if (this.isBlank(account) && this.isBlank(orgId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(dataFor)) {
			account = MeasureDataCode.MEASURE_DATA_EMPLOYEE_FULL;
			orgId = MeasureDataCode.MEASURE_DATA_ORGANIZATION_FULL;
		}
		if (KpiBasicCode.DATA_TYPE_DEPARTMENT.equals(dataFor)) {
			account = MeasureDataCode.MEASURE_DATA_EMPLOYEE_FULL;
		}
		if (KpiBasicCode.DATA_TYPE_PERSONAL.equals(dataFor)) {
			orgId = MeasureDataCode.MEASURE_DATA_ORGANIZATION_FULL;
		}
		HfKpi kpi = this.kpiService.selectByPrimaryKey(kpiOid).getValueEmptyThrowMessage();
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		for (Map<String, String> fieldMap : fieldDataList) {
			for (Entry<String, String> entry : fieldMap.entrySet()) {
				if (entry.getKey().startsWith(MeasureDataCode.MEASURE_DATA_TARGET_ID)) {
					String fieldDate = entry.getKey().split(Constants.INPUT_NAME_DELIMITER)[1];
					this.deleteMeasureData(kpi, frequency, fieldDate, account, orgId);
				}
			}
			for (Entry<String, String> entry : fieldMap.entrySet()) {
				if (entry.getKey().startsWith(MeasureDataCode.MEASURE_DATA_TARGET_ID)) {
					String fieldDate = entry.getKey().split(Constants.INPUT_NAME_DELIMITER)[1];
					String actual = fieldMap.get(MeasureDataCode.MEASURE_DATA_ACTUAL_ID + fieldDate);
					String target = fieldMap.get(MeasureDataCode.MEASURE_DATA_TARGET_ID + fieldDate);
					this.deleteMeasureData(kpi, frequency, fieldDate, account, orgId);
					if (!(NumberUtils.isCreatable(actual) && NumberUtils.isCreatable(target))) {
						continue;
					}
					this.createMeasureData(kpi, frequency, fieldDate, account, orgId, new BigDecimal(target), new BigDecimal(actual));
				}
			}
		}
		result.setValue(true);
		result.setMessage( BaseSystemMessage.updateSuccess() );
		return result;
	}
	
	private void createMeasureData(HfKpi kpi, String frequency, String date, String account, String orgId, BigDecimal target, BigDecimal actual) throws ServiceException, Exception {
		HfMeasureData measureData = new HfMeasureData();
		measureData.setKpiId(kpi.getId());
		measureData.setFrequency(frequency);
		measureData.setDate(date);
		measureData.setOrgId(orgId);
		measureData.setAccount(account);
		measureData.setActual(actual);
		measureData.setTarget(target);
		this.measureDataService.insert(measureData);
	}
	
	private void deleteMeasureData(HfKpi kpi, String frequency, String date, String account, String orgId) throws ServiceException, Exception {
		HfMeasureData measureData = new HfMeasureData();
		measureData.setKpiId(kpi.getId());
		measureData.setFrequency(frequency);
		measureData.setDate(date);
		measureData.setOrgId(orgId);
		measureData.setAccount(account);
		measureData = this.measureDataService.selectByUniqueKey(measureData).getValue();
		if (null == measureData || this.isBlank(measureData.getOid())) {
			return;
		}
		this.measureDataService.delete(measureData);
	}
	
}
