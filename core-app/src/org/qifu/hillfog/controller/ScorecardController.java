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
package org.qifu.hillfog.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfScorecard;
import org.qifu.hillfog.logic.IScorecardLogicService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IScorecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ScorecardController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IScorecardService<HfScorecard, String> scorecardService;
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;
	
	@Autowired
	IScorecardLogicService scorecardLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_sc";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			Map<String, String> kpiMap = this.kpiService.findMap(false);
			Map<String, String> okrMap = this.objectiveService.findMap(false);
			mm.put("kpiMap", kpiMap);
			mm.put("okrMap", okrMap);
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfScorecard scorecard = this.scorecardService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("scorecard", scorecard);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008Q")
	@RequestMapping("/hfScorecardPage")
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("mainPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008Q")
	@RequestMapping(value = "/hfScorecardQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<HfScorecard>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<HfScorecard>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<HfScorecard>> queryResult = this.scorecardService.findPage(
					"count", "findPageNoContent", this.queryParameter(searchValue).fullLink("nameLike").value(), pageOf.orderBy("NAME").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008A")
	@RequestMapping("/hfScorecardCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("createPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008E")
	@RequestMapping("/hfScorecardEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetch(mm, oid);
			this.init("editPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}
	
	private void queryPerspectivesAndDetailForEdit(DefaultControllerJsonResultObj<List<Map<String, Object>>> result, HfScorecard scorecard) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<List<Map<String, Object>>> qResult = this.scorecardLogicService.findPerspectivesItemForEditPage(scorecard.getOid());
		this.setDefaultResponseJsonResult(result, qResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008E")
	@RequestMapping(value = "/hfScorecardQueryEditDataJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<List<Map<String, Object>>> doQueryEditData(HttpServletRequest request, HfScorecard scorecard) {
		DefaultControllerJsonResultObj<List<Map<String, Object>>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.queryPerspectivesAndDetailForEdit(result, scorecard);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void checkFields(DefaultControllerJsonResultObj<HfScorecard> result, HttpServletRequest request, HfScorecard scorecard) throws ControllerException, ServiceException, Exception {
		CheckControllerFieldHandler<HfScorecard> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("name", scorecard, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("content", scorecard, "@org.apache.commons.lang3.StringUtils@isBlank(content)", "Content is blank!")
		.testField("mission", scorecard, "@org.apache.commons.lang3.StringUtils@isBlank(mission)", "Mission is blank!");
		checkHandler.throwMessage();
		
		String perspectivesItemJsonStr = StringUtils.defaultString( request.getParameter("perspectives") );
		Map<String, List<Map<String, Object>>> perspectivesJsonData = 
				(Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( perspectivesItemJsonStr, LinkedHashMap.class );
		List<Map<String, Object>> perspectivesDataMapList = perspectivesJsonData.get("items");
		if (null == perspectivesDataMapList || perspectivesDataMapList.size() < 1) {
			checkHandler.throwMessage("perspective", "At least one Perspective item is required!");
		}
		for (Map<String, Object> perDataMap : perspectivesDataMapList) {
			String perName = this.defaultString( (String)perDataMap.get("name") );
			if (StringUtils.isBlank(perName)) {
				checkHandler.throwMessage("perspective", "Perspective item name is blank!");
			}
			List<Map<String, Object>> strategyObjectivesDataMapList = (List<Map<String, Object>>) perDataMap.get("strategyObjectives");
			if (null == strategyObjectivesDataMapList || strategyObjectivesDataMapList.size() < 1) {
				checkHandler.throwMessage("strategyObjective", "Perspective (" + perName + ") At least one Strategy-objective item is required!");
			}
			for (Map<String, Object> soDataMap : strategyObjectivesDataMapList) {
				String soName = this.defaultString( (String)soDataMap.get("name") );
				if (StringUtils.isBlank(soName)) {
					checkHandler.throwMessage("strategyObjective", "Perspective (" + perName + ") 's Strategy-objective item name is blank!");
				}
				List<Map<String, Object>> kpisDataMapList = (List<Map<String, Object>>) soDataMap.get("kpis");
				if (null == kpisDataMapList || kpisDataMapList.size() < 1) {
					checkHandler.throwMessage("strategyObjective", "Perspective (" + perName + ") 's Strategy-objective (" + soName + ") At least one KPI item is required!");
				}
			}
		}		
	}
	
	private void save(DefaultControllerJsonResultObj<HfScorecard> result, HttpServletRequest request, HfScorecard scorecard) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, request, scorecard);
		String perspectivesItemJsonStr = StringUtils.defaultString( request.getParameter("perspectives") );
		Map<String, List<Map<String, Object>>> perspectivesJsonData = 
				(Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( perspectivesItemJsonStr, LinkedHashMap.class );
		List<Map<String, Object>> perspectivesDataMapList = perspectivesJsonData.get("items");
		DefaultResult<HfScorecard> iResult = this.scorecardLogicService.create(scorecard, perspectivesDataMapList);
		this.setDefaultResponseJsonResult(result, iResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008A")
	@RequestMapping(value = "/hfScorecardSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfScorecard> doSave(HttpServletRequest request, HfScorecard scorecard) {
		DefaultControllerJsonResultObj<HfScorecard> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, request, scorecard);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void update(DefaultControllerJsonResultObj<HfScorecard> result, HttpServletRequest request, HfScorecard scorecard) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, request, scorecard);
		String perspectivesItemJsonStr = StringUtils.defaultString( request.getParameter("perspectives") );
		Map<String, List<Map<String, Object>>> perspectivesJsonData = 
				(Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( perspectivesItemJsonStr, LinkedHashMap.class );
		List<Map<String, Object>> perspectivesDataMapList = perspectivesJsonData.get("items");
		DefaultResult<HfScorecard> uResult = this.scorecardLogicService.update(scorecard, perspectivesDataMapList);
		this.setDefaultResponseJsonResult(result, uResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008E")
	@RequestMapping(value = "/hfScorecardUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfScorecard> doUpdate(HttpServletRequest request, HfScorecard scorecard) {
		DefaultControllerJsonResultObj<HfScorecard> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, request, scorecard);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfScorecard scorecard) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.scorecardLogicService.delete(scorecard);
		this.setDefaultResponseJsonResult(result, dResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0008D")
	@RequestMapping(value = "/hfScorecardDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfScorecard scorecard) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, scorecard);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
