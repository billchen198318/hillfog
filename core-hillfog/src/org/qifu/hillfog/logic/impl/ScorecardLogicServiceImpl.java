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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.SortType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfPerspective;
import org.qifu.hillfog.entity.HfScorecard;
import org.qifu.hillfog.entity.HfSoOwnerKpis;
import org.qifu.hillfog.entity.HfSoOwnerOkrs;
import org.qifu.hillfog.entity.HfStrategyObjective;
import org.qifu.hillfog.logic.IScorecardLogicService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IPerspectiveService;
import org.qifu.hillfog.service.IScorecardService;
import org.qifu.hillfog.service.ISoOwnerKpisService;
import org.qifu.hillfog.service.ISoOwnerOkrsService;
import org.qifu.hillfog.service.IStrategyObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class ScorecardLogicServiceImpl extends BaseLogicService implements IScorecardLogicService {
	protected Logger logger = LogManager.getLogger(ScorecardLogicServiceImpl.class);
	
	private static final int MAX_CONTENT_LENGTH = 4000; // for scorecard vision's content & mission field
	
	@Autowired
	IScorecardService<HfScorecard, String> scorecardService;
	
	@Autowired
	IPerspectiveService<HfPerspective, String> perspectiveService;
	
	@Autowired
	IStrategyObjectiveService<HfStrategyObjective, String> strategyObjectiveService;
	
	@Autowired
	ISoOwnerKpisService<HfSoOwnerKpis, String> soOwnerKpisService;
	
	@Autowired
	ISoOwnerOkrsService<HfSoOwnerOkrs, String> soOwnerOkrsService;
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;	
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<HfScorecard> create(HfScorecard scorecard, List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception {
		if (null == scorecard || null == perspectivesDataMapList || perspectivesDataMapList.size() < 1) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		this.checkPerspectivesDataParam(perspectivesDataMapList);
		this.setStringValueMaxLength(scorecard, "content", MAX_CONTENT_LENGTH);
		this.setStringValueMaxLength(scorecard, "mission", MAX_CONTENT_LENGTH);
		DefaultResult<HfScorecard> result = this.scorecardService.insert(scorecard);
		scorecard = result.getValueEmptyThrowMessage();
		this.createPerspectives(scorecard, perspectivesDataMapList);
		this.resetUpdateTabNum(scorecard);
		return result;
	}
	
	private void resetUpdateTabNum(HfScorecard scorecard) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecard.getOid());
		List<HfPerspective> perspectivesList = this.perspectiveService.selectListByParams(paramMap, "TAB_NUM", SortType.ASC).getValueEmptyThrowMessage();
		for (int i = 0, n = 1; i < perspectivesList.size(); i++, n++) {
			String tabNum = StringUtils.leftPad(n+"", 5, "0");
			HfPerspective perspective = perspectivesList.get(i);
			perspective.setTabNum(tabNum);
			this.perspectiveService.update(perspective);
		}
	}
	
	private void createPerspectives(HfScorecard scorecard, List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception {
		for (Map<String, Object> perDataMap : perspectivesDataMapList) {
			HfPerspective perspective = new HfPerspective();
			BeanUtils.populate(perspective, perDataMap);
			perspective.setScOid( scorecard.getOid() );
			String tabNum = StringUtils.leftPad(perspective.getTabNum(), 5, "0");
			perspective.setTabNum(tabNum);
			perspective = this.perspectiveService.insert(perspective).getValueEmptyThrowMessage();
			this.createStrategyObjective(perspective, (List<Map<String, Object>>) perDataMap.get("strategyObjectives"));
		}
	}
	
	private void createStrategyObjective(HfPerspective perspective, List<Map<String, Object>> strategyObjectivesDataMapList) throws ServiceException, Exception {
		for (Map<String, Object> soDataMap : strategyObjectivesDataMapList) {
			HfStrategyObjective so = new HfStrategyObjective();
			BeanUtils.populate(so, soDataMap);
			so.setPerOid( perspective.getOid() );
			so = this.strategyObjectiveService.insert(so).getValueEmptyThrowMessage();
			List<Map<String, Object>> kpisDataMapList = (List<Map<String, Object>>) soDataMap.get("kpis");
			List<Map<String, Object>> okrsDataMapList = (List<Map<String, Object>>) soDataMap.get("okrs");
			this.createOwnerKpis(so, kpisDataMapList);
			this.createOwnerOkrs(so, okrsDataMapList);
		}
	}
	
	private void createOwnerKpis(HfStrategyObjective so, List<Map<String, Object>> kpisDataMapList) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (Map<String, Object> kpiDataMap : kpisDataMapList) {
			String oid = (String) kpiDataMap.get("oid");
			paramMap.put("oid", oid);
			if (this.kpiService.count(paramMap) < 1) {
				throw new ServiceException( BaseSystemMessage.dataErrors() );
			}
			HfSoOwnerKpis ownerKpi = new HfSoOwnerKpis();
			ownerKpi.setSoOid(so.getOid());
			ownerKpi.setKpiOid(oid);
			ownerKpi.setCardWeight( new BigDecimal(String.valueOf(kpiDataMap.get("weight"))) );
			this.soOwnerKpisService.insert(ownerKpi);
		}
		paramMap.clear();
	}
	
	private void createOwnerOkrs(HfStrategyObjective so, List<Map<String, Object>> okrsDataMapList) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (Map<String, Object> kpiDataMap : okrsDataMapList) {
			String oid = (String) kpiDataMap.get("oid");
			paramMap.put("oid", oid);
			if (this.objectiveService.count(paramMap) < 1) {
				throw new ServiceException( BaseSystemMessage.dataErrors() );
			}
			HfSoOwnerOkrs ownerOkr = new HfSoOwnerOkrs();
			ownerOkr.setSoOid(so.getOid());
			ownerOkr.setOkrOid(oid);
			this.soOwnerOkrsService.insert(ownerOkr);
		}
		paramMap.clear();
	}
	
	private void checkPerspectivesDataParam(List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception {
		for (Map<String, Object> perDataMap : perspectivesDataMapList) {
			String perName = this.defaultString( (String)perDataMap.get("name") );
			if (StringUtils.isBlank(perName)) {
				throw new ServiceException( "Perspective item name is blank!" );
			}
			List<Map<String, Object>> strategyObjectivesDataMapList = (List<Map<String, Object>>) perDataMap.get("strategyObjectives");
			if (null == strategyObjectivesDataMapList || strategyObjectivesDataMapList.size() < 1) {
				throw new ServiceException( "Perspective (" + perName + ") At least one Strategy-objective item is required!" );
			}
			for (Map<String, Object> soDataMap : strategyObjectivesDataMapList) {
				String soName = this.defaultString( (String)soDataMap.get("name") );
				if (StringUtils.isBlank(soName)) {
					throw new ServiceException( "Perspective (" + perName + ") 's Strategy-objective item name is blank!" );
				}
				List<Map<String, Object>> kpisDataMapList = (List<Map<String, Object>>) soDataMap.get("kpis");
				if (null == kpisDataMapList || kpisDataMapList.size() < 1) {
					throw new ServiceException( "Perspective (" + perName + ") 's Strategy-objective (" + soName + ") At least one KPI item is required!" );
				}
			}
		}		
	}
	
}
