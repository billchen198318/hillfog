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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.SortType;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.entity.HfPerspective;
import org.qifu.hillfog.entity.HfScorecard;
import org.qifu.hillfog.entity.HfSoOwnerKpis;
import org.qifu.hillfog.entity.HfSoOwnerOkrs;
import org.qifu.hillfog.entity.HfStrategyObjective;
import org.qifu.hillfog.model.OkrProgressRateData;
import org.qifu.hillfog.model.ScoreColor;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.service.IPerspectiveService;
import org.qifu.hillfog.service.IScorecardService;
import org.qifu.hillfog.service.ISoOwnerKpisService;
import org.qifu.hillfog.service.ISoOwnerOkrsService;
import org.qifu.hillfog.service.IStrategyObjectiveService;
import org.qifu.hillfog.vo.BscKpi;
import org.qifu.hillfog.vo.BscOkr;
import org.qifu.hillfog.vo.BscPerspective;
import org.qifu.hillfog.vo.BscStrategyObjective;
import org.qifu.hillfog.vo.BscVision;
import org.qifu.hillfog.vo.ScoreCalculationData;

public class BalancedScorecard {
	protected static Logger log = LogManager.getLogger(BalancedScorecard.class);
	
	private static int SCALE = 2;
	private static RoundingMode ROUND_MODE = RoundingMode.HALF_UP;
	
	private IScorecardService<HfScorecard, String> scorecardService = null;
	private IPerspectiveService<HfPerspective, String> perspectiveService = null;
	private IStrategyObjectiveService<HfStrategyObjective, String> strategyObjectiveService = null;
	private IKpiService<HfKpi, String> kpiService = null;
	private IObjectiveService<HfObjective, String> objectiveService = null;
	private ISoOwnerKpisService<HfSoOwnerKpis, String> soOwnerKpisService = null;
	private ISoOwnerOkrsService<HfSoOwnerOkrs, String> soOwnerOkrsService = null;
	private IEmployeeService<HfEmployee, String> employeeService = null;
	private IOrgDeptService<HfOrgDept, String> orgDeptService = null;	
	
	private BscVision vision = null;
	private String frequency;
	private String date1;
	private String date2;
	private String measureDataAccount;
	private String measureDataOrgId;
	
	public BalancedScorecard() {
		
	}
	
	public static BalancedScorecard build() {
		try {
			return BalancedScorecard.class.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BalancedScorecard from(String scorecardOid, String frequency, String date1, String date2, 
			String measureDataAccount, String measureDataOrgId) throws ServiceException, Exception {
		if (StringUtils.isBlank(scorecardOid) || StringUtils.isBlank(frequency) || StringUtils.isBlank(date1) || StringUtils.isBlank(date2)
				|| StringUtils.isBlank(measureDataAccount) || StringUtils.isBlank(measureDataOrgId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		this.frequency = frequency;
		this.date1 = date1;
		this.date2 = date2;
		this.measureDataAccount = measureDataAccount;
		this.measureDataOrgId = measureDataOrgId;
		try {
			scorecardService = (IScorecardService<HfScorecard, String>) AppContext.getBean(IScorecardService.class);
			perspectiveService = (IPerspectiveService<HfPerspective, String>) AppContext.getBean(IPerspectiveService.class);
			strategyObjectiveService = (IStrategyObjectiveService<HfStrategyObjective, String>) AppContext.getBean(IStrategyObjectiveService.class);
			kpiService = (IKpiService<HfKpi, String>) AppContext.getBean(IKpiService.class);
			objectiveService = (IObjectiveService<HfObjective, String>) AppContext.getBean(IObjectiveService.class);
			soOwnerKpisService = (ISoOwnerKpisService<HfSoOwnerKpis, String>) AppContext.getBean(ISoOwnerKpisService.class);
			soOwnerOkrsService = (ISoOwnerOkrsService<HfSoOwnerOkrs, String>) AppContext.getBean(ISoOwnerOkrsService.class);
			employeeService = (IEmployeeService<HfEmployee, String>) AppContext.getBean(IEmployeeService.class);
			orgDeptService = (IOrgDeptService<HfOrgDept, String>) AppContext.getBean(IOrgDeptService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == scorecardService || null == perspectiveService || null == strategyObjectiveService
				|| null == kpiService || null == objectiveService) {
			return this;
		}
		HfScorecard scorecard = scorecardService.selectByPrimaryKey(scorecardOid).getValueEmptyThrowMessage();
		this.vision = new BscVision();
		BeanUtils.copyProperties(this.vision, scorecard);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecardOid);
		List<HfPerspective> perspectiveList = perspectiveService.selectListByParams(paramMap, "TAB_NUM", SortType.ASC).getValueEmptyThrowMessage();
		for (HfPerspective perspective : perspectiveList) {
			BscPerspective bscPerspective = new BscPerspective();
			BeanUtils.copyProperties(bscPerspective, perspective);
			this.vision.getPerspectives().add(bscPerspective);
			paramMap.clear();
			paramMap.put("perOid", perspective.getOid());
			List<HfStrategyObjective> strategyObjectiveList = strategyObjectiveService.selectListByParams(paramMap).getValueEmptyThrowMessage();
			for (HfStrategyObjective strategyObjective : strategyObjectiveList) {
				BscStrategyObjective bscStrategyObjective = new BscStrategyObjective();
				BeanUtils.copyProperties(bscStrategyObjective, strategyObjective);
				bscPerspective.getStrategyObjectives().add(bscStrategyObjective);
				paramMap.clear();
				
				paramMap.put("soOid", strategyObjective.getOid());
				
				// KPI
				List<HfSoOwnerKpis> kpisList = soOwnerKpisService.selectListByParams(paramMap).getValueEmptyThrowMessage();
				for (HfSoOwnerKpis ownerKpi : kpisList) {
					HfKpi kpi = kpiService.selectByPrimaryKey(ownerKpi.getKpiOid()).getValueEmptyThrowMessage();
					BscKpi bscKpi = new BscKpi();
					BeanUtils.copyProperties(bscKpi, kpi);
					bscKpi.setWeight( ownerKpi.getCardWeight() );
					bscKpi.setSource(kpi);
					bscStrategyObjective.getKpis().add(bscKpi);
					this.fillKpiEmployeesAndDepartments(bscKpi);
				}
				
				// OKR
				List<HfSoOwnerOkrs> okrsList = soOwnerOkrsService.selectListByParams(paramMap).getValue();
				for (HfSoOwnerOkrs ownerOkr : okrsList) {
					HfObjective objective = objectiveService.selectByPrimaryKey(ownerOkr.getOkrOid()).getValueEmptyThrowMessage();
					BscOkr bscOkr = new BscOkr();
					BeanUtils.copyProperties(bscOkr, objective);
					bscOkr.setSource(objective);
					bscStrategyObjective.getOkrs().add(bscOkr);
				}
				
			}
		}
		if (this.vision != null) {
			this.setRowSize();
		}
		AggregationMethodUtils.clearThreadLocal();
		return this;
	}
	
	public BalancedScorecard process() throws ServiceException, Exception {
		if (null == this.vision) {
			return this;
		}
		BigDecimal visScore = BigDecimal.ZERO;
		for (BscPerspective perspective : this.vision.getPerspectives()) {
			BigDecimal perScore = BigDecimal.ZERO;
			for (BscStrategyObjective so : perspective.getStrategyObjectives()) {
				BigDecimal soScore = BigDecimal.ZERO;
				KpiScore kpiScore = KpiScore.build();
				for (BscKpi kpi : so.getKpis()) {
					BeanUtils.copyProperties(kpi, kpi.getSource());
					ScoreCalculationData scd = kpiScore.add(kpi.getSource(), frequency, date1, date2, measureDataAccount, measureDataOrgId)
							.processDefault().processDateRange().reduce().valueThrowMessage().get(0);
					kpi.setScore( scd.getScore() );
					kpi.setFontColor( scd.getFontColor() );
					kpi.setBgColor( scd.getBgColor() );
					soScore = soScore.add( kpi.getScore().multiply(this.getWeightPercentage(kpi.getWeight())) );
				}
				soScore = soScore.setScale(SCALE, ROUND_MODE);
				
				for (BscOkr okr : so.getOkrs()) {
					OkrProgressRateData oprd = OkrProgressRateUtils.build().fromObjective(okr.getSource()).process().value();
					okr.setProgressPercentage( oprd.getValue() );
					ScoreColor sc = ScoreColorUtils.get(okr.getProgressPercentage());
					okr.setBgColor( sc.getBackgroundColor() );
					okr.setFontColor( sc.getFontColor() );
				}
				
				so.setScore(soScore);
				ScoreColor sc = ScoreColorUtils.get(soScore);
				so.setBgColor( sc.getBackgroundColor() );
				sc.setFontColor( sc.getFontColor() );
				
				perScore = perScore.add( soScore.multiply(this.getWeightPercentage(so.getWeight())) );
				
			}
			perScore = perScore.setScale(SCALE, ROUND_MODE);
			perspective.setScore(perScore);
			ScoreColor sc = ScoreColorUtils.get(perScore);
			perspective.setBgColor( sc.getBackgroundColor() );
			perspective.setFontColor( sc.getFontColor() );
			
			visScore = visScore.add( perScore.multiply(this.getWeightPercentage(perspective.getWeight())) );
		}
		visScore = visScore.setScale(SCALE, ROUND_MODE);
		this.vision.setScore(visScore);
		ScoreColor sc = ScoreColorUtils.get(visScore);
		this.vision.setBgColor( sc.getBackgroundColor() );
		this.vision.setFontColor( sc.getFontColor() );		
		
		return this;
	}
	
	private void fillKpiEmployeesAndDepartments(BscKpi kpi) throws ServiceException, Exception {
		kpi.setEmployees( this.employeeService.findInputAutocompleteByKpiId(kpi.getId()) );
		kpi.setOrganizations( this.orgDeptService.findInputAutocompleteByKpiId(kpi.getId()) );
	}
	
	private void setRowSize() {
		int vRow = 0;
		for (BscPerspective perspective : this.vision.getPerspectives()) {
			int pRow = 0;
			for (BscStrategyObjective so : perspective.getStrategyObjectives()) {
				/*
				vRow += so.getKpis().size() + so.getOkrs().size();
				pRow += so.getKpis().size() + so.getOkrs().size();
				so.setRow( so.getKpis().size() + so.getOkrs().size() );
				*/
				vRow += so.getKpis().size() ;
				pRow += so.getKpis().size() ;
				so.setRow( so.getKpis().size() );				
			}
			perspective.setRow(pRow);
		}
		vision.setRow(vRow);
	}
	
	private BigDecimal getWeightPercentage(BigDecimal weight) {
		if (weight==null) {
			return BigDecimal.ZERO;
		}
		if (weight.floatValue() == 0.0f ) {
			return BigDecimal.ZERO;
		}
		BigDecimal w = weight.divide(new BigDecimal("100.0"));
		w = w.setScale(SCALE, ROUND_MODE);
		return w;
	}		
	
	public BscVision value() {
		return this.vision;
	}
	
	public BscVision valueCleanOkrSource() {
		for (BscPerspective perspective : this.vision.getPerspectives()) {
			for (BscStrategyObjective so : perspective.getStrategyObjectives()) {
				for (BscOkr okr : so.getOkrs()) {
					okr.setSource(null);
				}
			}
		}
		return this.vision;
	}	
	
}
