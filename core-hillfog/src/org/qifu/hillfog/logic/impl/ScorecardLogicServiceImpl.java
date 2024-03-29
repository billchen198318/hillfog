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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseLogicService;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfPerspective;
import org.qifu.hillfog.entity.HfScColor;
import org.qifu.hillfog.entity.HfScorecard;
import org.qifu.hillfog.entity.HfSoOwnerKpis;
import org.qifu.hillfog.entity.HfSoOwnerOkrs;
import org.qifu.hillfog.entity.HfStrategyObjective;
import org.qifu.hillfog.logic.IScorecardLogicService;
import org.qifu.hillfog.model.ScoreColor;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IPerspectiveService;
import org.qifu.hillfog.service.IScColorService;
import org.qifu.hillfog.service.IScorecardService;
import org.qifu.hillfog.service.ISoOwnerKpisService;
import org.qifu.hillfog.service.ISoOwnerOkrsService;
import org.qifu.hillfog.service.IStrategyObjectiveService;
import org.qifu.hillfog.util.ScoreColorUtils;
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
	
	@Autowired
	IScColorService<HfScColor, String> scColorService;
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<HfScorecard> create(HfScorecard scorecard, List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception {
		if (null == scorecard || CollectionUtils.isEmpty(perspectivesDataMapList)) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		this.checkPerspectivesDataParam(perspectivesDataMapList);
		this.setStringValueMaxLength(scorecard, "content", MAX_CONTENT_LENGTH);
		this.setStringValueMaxLength(scorecard, "mission", MAX_CONTENT_LENGTH);
		DefaultResult<HfScorecard> result = this.scorecardService.insert(scorecard);
		scorecard = result.getValueEmptyThrowMessage();
		this.createPerspectives(scorecard, perspectivesDataMapList);
		this.resetUpdateTabNum(scorecard);
		this.createScoreColor(scorecard);
		return result;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<HfScorecard> update(HfScorecard scorecard, List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception {
		if (null == scorecard || this.isBlank(scorecard.getOid()) || CollectionUtils.isEmpty(perspectivesDataMapList)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		this.checkPerspectivesDataParam(perspectivesDataMapList);
		HfScorecard checkUkScorecard = this.scorecardService.selectByUniqueKey(scorecard).getValue();
		if (checkUkScorecard != null) {
			if (!checkUkScorecard.getOid().equals(scorecard.getOid())) {
				throw new ServiceException("Please change name, has other Scorecard use.");
			}
		}
		this.setStringValueMaxLength(scorecard, "content", MAX_CONTENT_LENGTH);
		this.setStringValueMaxLength(scorecard, "mission", MAX_CONTENT_LENGTH);
		DefaultResult<HfScorecard> mResult = this.scorecardService.update(scorecard);
		scorecard = mResult.getValueEmptyThrowMessage();
		this.deletePerspectiveAndAllDetail(scorecard);
		this.createPerspectives(scorecard, perspectivesDataMapList);
		this.resetUpdateTabNum(scorecard);		
		return mResult;
	}	
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> delete(HfScorecard scorecard) throws ServiceException, Exception {
		if (null == scorecard || this.isBlank(scorecard.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		this.deletePerspectiveAndAllDetail(scorecard);
		this.deleteScoreColor(scorecard);
		return this.scorecardService.delete(scorecard);
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
	
	private void createScoreColor(HfScorecard scorecard) throws ServiceException, Exception {
		ScoreColor sc = ScoreColorUtils.getUnknown();
		HfScColor c = new HfScColor();
		c.setScOid(scorecard.getOid());
		c.setType(ScoreColor.TYPE_DEFAULT);
		c.setRange1(0);
		c.setRange2(0);
		c.setFontColor( sc.getFontColor() );
		c.setBgColor( sc.getBackgroundColor() );
		this.scColorService.insert(c);
		Map<String, String> scoreMap = (Map<String, String>) ScoreColorUtils.getSrcMap().get("range");
		for (Map.Entry<String, String> entry : scoreMap.entrySet()) {
			String scoreVal[] = entry.getKey().split(Constants.DEFAULT_SPLIT_DELIMITER);
			String colorVal[] = entry.getValue().split(Constants.DEFAULT_SPLIT_DELIMITER);
			if (scoreVal.length != 2 || colorVal.length != 2) {
				continue;
			}
			c = new HfScColor();
			c.setScOid(scorecard.getOid());
			c.setType(ScoreColor.TYPE_CUSTOM);
			c.setRange1( NumberUtils.toInt(StringUtils.deleteWhitespace(scoreVal[0])) );
			c.setRange2( NumberUtils.toInt(StringUtils.deleteWhitespace(scoreVal[1])) );
			c.setFontColor( StringUtils.deleteWhitespace(colorVal[1]) );
			c.setBgColor( StringUtils.deleteWhitespace(colorVal[0]) );
			this.scColorService.insert(c);
		}
	}
	
	private void deletePerspectiveAndAllDetail(HfScorecard scorecard) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecard.getOid());
		List<HfPerspective> perspectivesList = this.perspectiveService.selectListByParams(paramMap, "TAB_NUM", SortType.ASC).getValueEmptyThrowMessage();
		for (HfPerspective perspective : perspectivesList) {
			this.perspectiveService.delete(perspective);
			this.deleteStrategyObjective(perspective);
		}
		paramMap.clear();
	}
	
	private void deleteStrategyObjective(HfPerspective perspective) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("perOid", perspective.getOid());
		List<HfStrategyObjective> strategyObjectivesList = this.strategyObjectiveService.selectListByParams(paramMap).getValueEmptyThrowMessage();
		for (HfStrategyObjective so : strategyObjectivesList) {
			this.strategyObjectiveService.delete(so);
			this.deleteOwnerKpis(so);
			this.deleteOwnerOkrs(so);
		}
		paramMap.clear();
	}
	
	private void deleteOwnerKpis(HfStrategyObjective so) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("soOid", so.getOid());
		List<HfSoOwnerKpis> ownerKpisList = this.soOwnerKpisService.selectListByParams(paramMap).getValueEmptyThrowMessage();
		for (HfSoOwnerKpis ownerKpi : ownerKpisList) {
			this.soOwnerKpisService.delete(ownerKpi);
		}
		paramMap.clear();
	}
	
	private void deleteOwnerOkrs(HfStrategyObjective so) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("soOid", so.getOid());
		List<HfSoOwnerOkrs> ownerOkrsList = this.soOwnerOkrsService.selectListByParams(paramMap).getValue();
		for (HfSoOwnerOkrs ownerOkr : ownerOkrsList) {
			this.soOwnerOkrsService.delete(ownerOkr);
		}
		paramMap.clear();
	}
	
	private void deleteScoreColor(HfScorecard scorecard) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecard.getOid());
		List<HfScColor> scoreColorList = this.scColorService.selectListByParams(paramMap).getValue();
		for (HfScColor scc : scoreColorList) {
			this.scColorService.delete(scc);
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
			if (CollectionUtils.isEmpty(strategyObjectivesDataMapList)) {
				throw new ServiceException( "Perspective (" + perName + ") At least one Strategy-objective item is required!" );
			}
			for (Map<String, Object> soDataMap : strategyObjectivesDataMapList) {
				String soName = this.defaultString( (String)soDataMap.get("name") );
				if (StringUtils.isBlank(soName)) {
					throw new ServiceException( "Perspective (" + perName + ") 's Strategy-objective item name is blank!" );
				}
				List<Map<String, Object>> kpisDataMapList = (List<Map<String, Object>>) soDataMap.get("kpis");
				if (CollectionUtils.isEmpty(kpisDataMapList)) {
					throw new ServiceException( "Perspective (" + perName + ") 's Strategy-objective (" + soName + ") At least one KPI item is required!" );
				}
			}
		}		
	}

	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<List<Map<String, Object>>> findPerspectivesItemForEditPage(String scorecardOid) throws ServiceException, Exception {
		if (this.isBlank(scorecardOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<List<Map<String, Object>>> result = new DefaultResult<List<Map<String, Object>>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecardOid);
		List<HfPerspective> perspectivesList = this.perspectiveService.selectListByParams(paramMap, "TAB_NUM", SortType.ASC).getValue();
		List<Map<String, Object>> perspectivesDataMapList = new LinkedList<Map<String, Object>>();
		for (HfPerspective perspective : perspectivesList) {
			paramMap.clear();
			paramMap.put("perOid", perspective.getOid());
			List<HfStrategyObjective> strategyObjectiveList = this.strategyObjectiveService.selectListByParams(paramMap).getValue();
			
			Map<String, Object> perspectiveDataMap = new HashMap<String, Object>();
			List<Map<String, Object>> strategyObjectiveDataMapList = new ArrayList<Map<String, Object>>();
			perspectiveDataMap.put("strategyObjectives", strategyObjectiveDataMapList);
			perspectiveDataMap.put("oid", perspective.getOid());
			perspectiveDataMap.put("name", perspective.getName());
			perspectiveDataMap.put("weight", perspective.getWeight().floatValue());
			perspectiveDataMap.put("tabNum", perspective.getTabNum());
			perspectivesDataMapList.add(perspectiveDataMap);
			
			for (HfStrategyObjective strategyObjective : strategyObjectiveList) {
				Map<String, Object> strategyObjectiveDataMap = new HashMap<String, Object>();
				List<Map<String, Object>> kpis = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> okrs = new ArrayList<Map<String, Object>>();
				strategyObjectiveDataMap.put("kpis", kpis);
				strategyObjectiveDataMap.put("okrs", okrs);
				strategyObjectiveDataMap.put("currentSelect1", Constants.HTML_SELECT_NO_SELECT_ID);
				strategyObjectiveDataMap.put("currentSelect2", Constants.HTML_SELECT_NO_SELECT_ID);
				strategyObjectiveDataMap.put("oid", strategyObjective.getOid());
				strategyObjectiveDataMap.put("name", strategyObjective.getName());
				strategyObjectiveDataMap.put("weight", strategyObjective.getWeight().floatValue());
				strategyObjectiveDataMapList.add(strategyObjectiveDataMap);
				
				paramMap.clear();
				paramMap.put("soOid", strategyObjective.getOid());
				List<HfSoOwnerKpis> ownerKpisList = this.soOwnerKpisService.selectListByParams(paramMap).getValue();
				for (HfSoOwnerKpis ownerKpi : ownerKpisList) {
					HfKpi kpi = this.kpiService.selectByPrimaryKey(ownerKpi.getKpiOid()).getValueEmptyThrowMessage();
					Map<String, Object> kpiData = new HashMap<String, Object>();
					kpiData.put("oid", kpi.getOid());
					kpiData.put("name", kpi.getName());
					kpiData.put("weight", ownerKpi.getCardWeight().floatValue());
					kpis.add(kpiData);
				}
				
				List<HfSoOwnerOkrs> ownerOkrsList = this.soOwnerOkrsService.selectListByParams(paramMap).getValue();
				for (HfSoOwnerOkrs ownerOkr : ownerOkrsList) {
					HfObjective objective = this.objectiveService.selectByPrimaryKey(ownerOkr.getOkrOid()).getValueEmptyThrowMessage();
					Map<String, Object> okrData = new HashMap<String, Object>();
					okrData.put("oid", objective.getOid());
					okrData.put("name", objective.getName());
					okrs.add(okrData);
				}
				
			}
			
		}
		paramMap.clear();
		result.setValue(perspectivesDataMapList);
		result.setSuccess( YesNo.YES );
		result.setMessage( "success!" );
		return result;
	}

	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> updateColor(
			HfScorecard scorecard, List<Map<String, Object>> defaultScoreColorDataMapList, List<Map<String, Object>> customScoreColorDataMapList) throws ServiceException, Exception {
		if (null == scorecard || this.isBlank(scorecard.getOid()) || CollectionUtils.isEmpty(defaultScoreColorDataMapList) || CollectionUtils.isEmpty(customScoreColorDataMapList)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		scorecard = this.scorecardService.selectByEntityPrimaryKey(scorecard).getValueEmptyThrowMessage();
		this.deleteScoreColor(scorecard);
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		Map<String, Object> defaultColorDataMap = defaultScoreColorDataMapList.get(0);
		defaultColorDataMap.remove("cdate");
		defaultColorDataMap.remove("udate");
		HfScColor defaultScColor = new HfScColor();
		BeanUtils.populate(defaultScColor, defaultColorDataMap);
		defaultScColor.setType(ScoreColor.TYPE_DEFAULT);
		this.scColorService.insert(defaultScColor);
		for (Map<String, Object> customColorDataMap : customScoreColorDataMapList) {
			customColorDataMap.remove("cdate");
			customColorDataMap.remove("udate");
			HfScColor customScColor = new HfScColor();
			BeanUtils.populate(customScColor, customColorDataMap);
			customScColor.setType(ScoreColor.TYPE_CUSTOM);
			this.scColorService.insert(customScColor);
		}
		result.setValue( true );
		result.setMessage( BaseSystemMessage.updateSuccess() );
		return result;
	}
	
}
