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
package org.qifu.hillfog.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.SortType;
import org.qifu.hillfog.entity.HfInitiatives;
import org.qifu.hillfog.entity.HfKeyRes;
import org.qifu.hillfog.entity.HfKeyResVal;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.model.OkrProgressRateData;
import org.qifu.hillfog.service.IInitiativesService;
import org.qifu.hillfog.service.IKeyResService;
import org.qifu.hillfog.service.IKeyResValService;
import org.qifu.hillfog.service.IObjectiveService;

public class OkrProgressRateUtils {
	
	IObjectiveService<HfObjective, String> objectiveService;
	IKeyResService<HfKeyRes, String> keyResService;
	IKeyResValService<HfKeyResVal, String> keyResValService;
	IInitiativesService<HfInitiatives, String> initiativesService;
	
	private OkrProgressRateData progressRateData = new OkrProgressRateData();
	
	public OkrProgressRateUtils() {
		try {
			objectiveService = (IObjectiveService<HfObjective, String>) AppContext.getBean(IObjectiveService.class);
			keyResService = (IKeyResService<HfKeyRes, String>) AppContext.getBean(IKeyResService.class);
			keyResValService = (IKeyResValService<HfKeyResVal, String>) AppContext.getBean(IKeyResValService.class);
			initiativesService = (IInitiativesService<HfInitiatives, String>) AppContext.getBean(IInitiativesService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static OkrProgressRateUtils build() {
		try {
			return OkrProgressRateUtils.class.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException 
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OkrProgressRateData value() {
		return this.progressRateData;
	}
	
	public OkrProgressRateUtils fromObjective(String objectiveOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(objectiveOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		progressRateData.setObjective( this.objectiveService.selectByPrimaryKey(objectiveOid).getValueEmptyThrowMessage() );
		this.fillKeyResAllData(objectiveOid);
		return this;
	}
	
	public OkrProgressRateUtils fromObjective(HfObjective objective) throws ServiceException, Exception {
		if (null == objective || StringUtils.isBlank(objective.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		progressRateData.setObjective(objective);
		this.fillKeyResAllData(objective.getOid());
		return this;
	}
	
	public OkrProgressRateUtils loadInitiatives() throws ServiceException, Exception {
		if (this.progressRateData.getObjective() == null) {
			return this;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", this.progressRateData.getObjective().getOid());
		this.progressRateData.getObjective().setInitiativeList( this.initiativesService.selectListByParams(paramMap).getValue() );
		return this;
	}
	
	private void fillKeyResAllData(String objectiveOid) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objectiveOid);
		progressRateData.getObjective().setKeyResList( this.keyResService.selectListByParams(paramMap).getValue() );
		if (progressRateData.getObjective().getKeyResList() == null || progressRateData.getObjective().getKeyResList().size() < 1) {
			throw new ServiceException( BaseSystemMessage.dataErrors() );
		}
		String startDate = progressRateData.getObjective().getStartDate();
		String endDate = progressRateData.getObjective().getEndDate();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		for (HfKeyRes keyRes : progressRateData.getObjective().getKeyResList()) {
			paramMap.put("resOid", keyRes.getOid());
			keyRes.setKeyResValList( this.keyResValService.selectListByParams(paramMap, "DATE", SortType.ASC).getValue() );
			if (keyRes.getKeyResValList() == null) {
				keyRes.setKeyResValList(new ArrayList<HfKeyResVal>());
			}
		}		
	}
	
	/**
	 * GP_TYPE
	 * Get period measure data value
	 * 
	 * 1-SUM
	 * 2-AVG
	 * 3-MAX
	 * 4-MIN
	 */
	private void setPeriodMeasureDataValue(HfObjective objective) {
		for (HfKeyRes keyRes : objective.getKeyResList()) {
			if (keyRes.getKeyResValList() == null || keyRes.getKeyResValList().size() < 1) {
				continue;
			}
			for (HfKeyResVal resVal : keyRes.getKeyResValList()) {
				if ("1".equals(keyRes.getGpType()) || "2".equals(keyRes.getGpType())) { // 1-SUM and 2-AVG
					keyRes.setMeasureValue( keyRes.getMeasureValue().add(resVal.getValue()) ); 
				}
				if ("3".equals(keyRes.getGpType())) { // 3-MAX
					if (resVal.getValue().compareTo(keyRes.getMeasureValue()) == 1) {
						keyRes.setMeasureValue( resVal.getValue() );
					}
				}
				if ("4".equals(keyRes.getGpType())) { // 4-MIN
					if (resVal.getValue().compareTo(keyRes.getMeasureValue()) == -1) {
						keyRes.setMeasureValue( resVal.getValue() );
					}					
				}				
			}	
			if ("2".equals(keyRes.getGpType()) && keyRes.getMeasureValue() != BigDecimal.ZERO && keyRes.getKeyResValList().size() > 0) { // 2-AVG
				if (keyRes.getKeyResValList().size() > 0) {
					keyRes.setMeasureValue( keyRes.getMeasureValue().divide(new BigDecimal(keyRes.getKeyResValList().size()), 2, RoundingMode.HALF_UP) );
				}
			}
		}
	}
	
	/**
	 * OP_TARGET
	 * 
	 * 1.	>   (value > target)
	 * 2.	<   (value < target)
	 * 3.	=   (value = target)
	 * 4.	>= 	(value >= target)
	 * 5.	<= 	(value <= target)
	 * 
	 */
	
	public OkrProgressRateUtils process() throws ServiceException, Exception {
		if (this.progressRateData.getObjective() == null) {
			return this;
		}
		this.setPeriodMeasureDataValue(this.progressRateData.getObjective());
		BigDecimal fullPercentage = new BigDecimal("100");
		BigDecimal sumKeyResProgress = BigDecimal.ZERO;
		for (HfKeyRes keyRes : this.progressRateData.getObjective().getKeyResList()) {
			if (keyRes.getKeyResValList() == null || keyRes.getKeyResValList().size() < 1) {
				continue;
			}			
			if ("1".equals(keyRes.getOpTarget())) { // >
				if (keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == 1) {
					keyRes.setProgressPercentage( fullPercentage );
				} else {
					if (keyRes.getTarget() != BigDecimal.ZERO) {
						keyRes.setProgressPercentage( keyRes.getMeasureValue().divide(keyRes.getTarget(), 2, RoundingMode.HALF_UP).multiply(fullPercentage) );
					}
				}
			}
			if ("2".equals(keyRes.getOpTarget())) { // <
				if (keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == -1) {
					keyRes.setProgressPercentage( fullPercentage );
				} else {
					if (keyRes.getMeasureValue() != BigDecimal.ZERO) {
						keyRes.setProgressPercentage( keyRes.getTarget().divide(keyRes.getMeasureValue(), 2, RoundingMode.HALF_UP).multiply(fullPercentage) );
					}
				}
			}
			if ("3".equals(keyRes.getOpTarget())) { // =
				if (keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == 0) {
					keyRes.setProgressPercentage( fullPercentage );
				} else {
					if (keyRes.getMeasureValue().compareTo(keyRes.getTarget()) >= 0) { // 數據大
						if (keyRes.getMeasureValue() != BigDecimal.ZERO) {
							keyRes.setProgressPercentage( keyRes.getTarget().divide(keyRes.getMeasureValue(), 2, RoundingMode.HALF_UP).multiply(fullPercentage) );
						}					
					} else { // target大
						if (keyRes.getTarget() != BigDecimal.ZERO) {
							keyRes.setProgressPercentage( keyRes.getMeasureValue().divide(keyRes.getTarget(), 2, RoundingMode.HALF_UP).multiply(fullPercentage) );
						}
					}
				}
			}
			if ("4".equals(keyRes.getOpTarget())) { // >=
				if (keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == 1 || keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == 0) {
					keyRes.setProgressPercentage( fullPercentage );
				} else {
					if (keyRes.getTarget() != BigDecimal.ZERO) {
						keyRes.setProgressPercentage( keyRes.getMeasureValue().divide(keyRes.getTarget(), 2, RoundingMode.HALF_UP).multiply(fullPercentage) );
					}
				}				
			}
			if ("5".equals(keyRes.getOpTarget())) { // <=
				if (keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == -1 || keyRes.getMeasureValue().compareTo(keyRes.getTarget()) == 0) {
					keyRes.setProgressPercentage( fullPercentage );
				} else {
					if (keyRes.getMeasureValue() != BigDecimal.ZERO) {
						keyRes.setProgressPercentage( keyRes.getTarget().divide(keyRes.getMeasureValue(), 2, RoundingMode.HALF_UP).multiply(fullPercentage) );
					}
				}				
			}
			sumKeyResProgress = sumKeyResProgress.add(keyRes.getProgressPercentage());
		}
		this.progressRateData.getObjective().setProgressPercentage( sumKeyResProgress.divide(new BigDecimal(this.progressRateData.getObjective().getKeyResList().size()), 2, RoundingMode.HALF_UP) );
		if (this.progressRateData.getObjective().getProgressPercentage().compareTo(fullPercentage) > 0) {
			this.progressRateData.getObjective().setProgressPercentage( fullPercentage );
		}
		return this;
	}
	
}
