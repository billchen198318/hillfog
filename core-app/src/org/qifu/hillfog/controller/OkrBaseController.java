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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.SortType;
import org.qifu.core.util.TemplateUtils;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfInitiatives;
import org.qifu.hillfog.entity.HfKeyRes;
import org.qifu.hillfog.entity.HfKeyResVal;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IOkrBaseLogicService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IInitiativesService;
import org.qifu.hillfog.service.IKeyResService;
import org.qifu.hillfog.service.IKeyResValService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.vo.MeasureDataInputBody;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class OkrBaseController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	private static final String TEMPLATE_RESOURCE = "META-INF/resource/key-res-measure-data-day.ftl";
	
	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Autowired
	IOkrBaseLogicService okrBaseLogicService;
	
	@Autowired
	IKeyResService<HfKeyRes, String> keyResService;
	
	@Autowired
	IKeyResValService<HfKeyResVal, String> keyResValService;
	
	@Autowired
	IInitiativesService<HfInitiatives, String> initiativesService;	
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_okrb";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("mainPage".equals(type) || "createPage".equals(type) || "editPage".equals(type)) {
			mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocomplete()));
			mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocomplete()));			
		}		
		if ("editPage".equals(type)) {
			// 取出 objective - owner & department
			HfObjective objective = (HfObjective) mm.get("objective");
			mm.put("selOrgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocompleteByObjectiveOid(objective.getOid())));
			mm.put("selEmpInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocompleteByObjectiveOid(objective.getOid())));			
		}
		if ("enterMeasureDataPage".equals(type)) {
			HfObjective objective = (HfObjective) mm.get("objective");
			mm.put("systemDate", this.getNowDate2());
			mm.put("keyResOptionsMap", this.keyResService.findSelectOptionsMapByObjectiveOid(true, objective.getOid()));
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfObjective objective = this.objectiveService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("objective", objective);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006Q")
	@RequestMapping("/hfOkrBasePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006A")
	@RequestMapping("/hfOkrBaseAddPage")
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
	
	private void checkFields(DefaultControllerJsonResultObj<HfObjective> result, HttpServletRequest request, HfObjective objective) throws ControllerException, ServiceException, Exception {
		String objDept = request.getParameter("objDept");
		String objOwner = request.getParameter("objOwner");
		CheckControllerFieldHandler<HfObjective> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("name", objective, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("date1", !SimpleUtils.isDate(request.getParameter("date1")), "Please input start date!")
		.testField("date2", !SimpleUtils.isDate(request.getParameter("date2")), "Please input end date!")
		.testField("objOrg", StringUtils.isBlank(objDept) && StringUtils.isBlank(objOwner), "Please select organization or employee!");
		checkHandler.throwMessage();
		
		Map<String, List<Map<String, Object>>> objDeptJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( objDept, LinkedHashMap.class );
		List objDeptList = objDeptJsonData.get("items");
		Map<String, List<Map<String, Object>>> objOwnerJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( objOwner, LinkedHashMap.class );
		List objOwnerList = objOwnerJsonData.get("items");
		if (objDeptList.size() < 1 &&  objOwnerList.size() < 1) {
			checkHandler.throwMessage("objOrg", "Please select organization or employee!");
		}
		
		String keyResults = request.getParameter("keyResults");
		Map<String, List<Map<String, Object>>> keyResultsJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( keyResults, LinkedHashMap.class );
		List keyResultsList = keyResultsJsonData.get("items");
		if (keyResultsList == null || keyResultsList.size() < 1) {
			checkHandler.throwMessage("", "Please input any one key result!");
		}
		for (int i = 0; i < keyResultsList.size(); i++) {
			Map<String, Object> dataMap = (Map<String, Object>) keyResultsList.get(i);
			if (StringUtils.isBlank((String)dataMap.get("name"))) {
				checkHandler.throwMessage("", "Please input position " + (i+1) + " key result name!");
			}
			if (!NumberUtils.isCreatable(String.valueOf(dataMap.get("target")))) {
				checkHandler.throwMessage("", "Please input position " + (i+1) + " key result target number!");
			}
		}
		objective.setStartDate(request.getParameter("date1"));
		objective.setEndDate(request.getParameter("date2"));
	}
	
	private void save(DefaultControllerJsonResultObj<HfObjective> result, HttpServletRequest request, HfObjective objective) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, request, objective);
		String objDept = request.getParameter("objDept");
		String objOwner = request.getParameter("objOwner");
		Map<String, List<Map<String, Object>>> objDeptJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( objDept, LinkedHashMap.class );
		List objDeptList = objDeptJsonData.get("items");
		Map<String, List<Map<String, Object>>> objOwnerJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( objOwner, LinkedHashMap.class );
		List objOwnerList = objOwnerJsonData.get("items");
		
		String keyResults = request.getParameter("keyResults");
		String initiatives = request.getParameter("initiatives");
		Map<String, List<Map<String, Object>>> keyResultsJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( keyResults, LinkedHashMap.class );
		List keyResultsMapList = keyResultsJsonData.get("items");
		Map<String, List<Map<String, Object>>> initiativesJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( initiatives, LinkedHashMap.class );
		List initiativesMapList = initiativesJsonData.get("items");
		
		DefaultResult<HfObjective> iResult = this.okrBaseLogicService.create(objective, objDeptList, objOwnerList, keyResultsMapList, initiativesMapList);
		this.setDefaultResponseJsonResult(result, iResult);
	}
	
	private void update(DefaultControllerJsonResultObj<HfObjective> result, HttpServletRequest request, HfObjective objective) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, request, objective);
		String objDept = request.getParameter("objDept");
		String objOwner = request.getParameter("objOwner");
		Map<String, List<Map<String, Object>>> objDeptJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( objDept, LinkedHashMap.class );
		List objDeptList = objDeptJsonData.get("items");
		Map<String, List<Map<String, Object>>> objOwnerJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( objOwner, LinkedHashMap.class );
		List objOwnerList = objOwnerJsonData.get("items");
		
		String keyResults = request.getParameter("keyResults");
		String initiatives = request.getParameter("initiatives");
		Map<String, List<Map<String, Object>>> keyResultsJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( keyResults, LinkedHashMap.class );
		List keyResultsMapList = keyResultsJsonData.get("items");
		Map<String, List<Map<String, Object>>> initiativesJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( initiatives, LinkedHashMap.class );
		List initiativesMapList = initiativesJsonData.get("items");
		
		DefaultResult<HfObjective> uResult = this.okrBaseLogicService.update(objective, objDeptList, objOwnerList, keyResultsMapList, initiativesMapList);
		this.setDefaultResponseJsonResult(result, uResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006A")
	@RequestMapping(value = "/hfOkrBaseSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfObjective> doSave(HttpServletRequest request, HfObjective objective) {
		DefaultControllerJsonResultObj<HfObjective> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, request, objective);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}
	
	private void queryObjectives(DefaultControllerJsonResultObj<List<HfObjective>> result, HttpServletRequest request) throws ServiceException, Exception {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String departmentId = StringUtils.defaultString(request.getParameter("departmentId"));
		String accountId = StringUtils.defaultString(request.getParameter("ownerAccount"));
		if (!SimpleUtils.isDate(startDate) || !SimpleUtils.isDate(endDate)) {
			startDate = "";
			endDate = "";
		}
		startDate = startDate.replaceAll("-", "").replaceAll("/", "");
		endDate = endDate.replaceAll("-", "").replaceAll("/", "");
		departmentId = StringUtils.deleteWhitespace(departmentId.split("/")[0]);
		String tmp[] = accountId.split("/");
		if (tmp != null && tmp.length >= 3) {
			accountId = StringUtils.deleteWhitespace(tmp[1]);
		}
		String name = StringUtils.defaultString(request.getParameter("name"));
		DefaultResult<List<HfObjective>> qResult = this.objectiveService.selectQueryObjectiveList(accountId, departmentId, startDate, endDate, name);
		this.setDefaultResponseJsonResult(result, qResult);
		result.setSuccess(YES);
	}
	
	private void queryAllData(DefaultControllerJsonResultObj<HfObjective> result, HfObjective objective) throws ServiceException, Exception {
		objective = this.objectiveService.selectByEntityPrimaryKey(objective).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objective.getOid());
		objective.setInitiativeList( this.initiativesService.selectListByParams(paramMap).getValue() );
		objective.setKeyResList( this.keyResService.selectListByParams(paramMap, "NAME", SortType.ASC).getValue() );
		if (objective.getInitiativeList() == null) {
			objective.setInitiativeList(new ArrayList<HfInitiatives>());
		}
		if (objective.getKeyResList() == null) {
			objective.setKeyResList(new ArrayList<HfKeyRes>());
		}
		result.setValue( objective );
		result.setSuccess( YES );
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006Q")
	@RequestMapping(value = "/hfOkrBaseQueryJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<List<HfObjective>> doQuery(HttpServletRequest request) {
		DefaultControllerJsonResultObj<List<HfObjective>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.queryObjectives(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006E")
	@RequestMapping("/hfOkrBaseEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006M")
	@RequestMapping("/hfOkrBaseEnterMasureDataPage")
	public String enterMeasureDataPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("masure-data-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetch(mm, oid);
			this.init("enterMeasureDataPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	private List<HfKeyResVal> findMeasureData(String objectiveOid, String keyResOid, String date) throws ServiceException, Exception {
		List<HfKeyResVal> searchList = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objectiveOid);
		paramMap.put("resOid", keyResOid);
		paramMap.put("dateLike", date.substring(0, 6)+"%");
		searchList = this.keyResValService.selectListByParams(paramMap).getValue();
		if (null == searchList) {
			searchList = new ArrayList<HfKeyResVal>();
		}
		return searchList;
	}
	
	private Map<String, Object> getParameter(String objectiveOid, String keyResOid, String date) throws ServiceException, Exception {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("date", date);
		parameter.put("masureDatas", findMeasureData(objectiveOid, keyResOid, date));
		return parameter;
	}		
	
	private void fillParameterForDay(Map<String, Object> parameter) throws Exception {
		String tmp[] = ( (String)parameter.get("date") ).split("-");
		String yyyy = tmp[0];
		String mm = tmp[1];
		String yyyyMM = yyyy+mm;
		int maxday = SimpleUtils.getMaxDayOfMonth(SimpleUtils.getInt(yyyy, 1990), SimpleUtils.getInt(mm, 1) );
		int dayOfWeek = SimpleUtils.getDayOfWeek(SimpleUtils.getInt(yyyy, 1990), SimpleUtils.getInt(mm, 1) );
		int showLen = (maxday+dayOfWeek) / 7;
		if ( (maxday + dayOfWeek) % 7 > 1 ) {
			showLen = showLen + 1;		
		}		
		int previousMonthMaxDay = 0;
		int previousMonth = SimpleUtils.getInt(mm, 1)-1;
		int previousYear = SimpleUtils.getInt(yyyy, 1990);
		if (previousMonth < 1 ) {
			previousYear = previousYear - 1;
			previousMonth = 12;
		}
		previousMonthMaxDay = SimpleUtils.getMaxDayOfMonth(previousYear, previousMonth);
		
		parameter.put("yyyy", yyyy);
		parameter.put("mm", mm);
		parameter.put("yyyyMM", yyyyMM);
		parameter.put("maxday", maxday);
		parameter.put("dayOfWeek", dayOfWeek);
		parameter.put("showLen", showLen);
		parameter.put("previousMonthMaxDay", previousMonthMaxDay);
		parameter.put("previousMonth", previousMonth);
		parameter.put("previousYear", previousYear);
	}	
	
	private String render(Map<String, Object> parameter, String templateResource) throws Exception {
		return TemplateUtils.processTemplate(
				"resourceTemplate", 
				OkrBaseController.class.getClassLoader(), 
				templateResource, 
				parameter);
	}	
	
	private String renderBody(String objectiveOid, String keyResOid, String date) throws ServiceException, Exception {
		if (StringUtils.isBlank(objectiveOid) || StringUtils.isBlank(keyResOid) || !SimpleUtils.isDate(date)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> parameter = getParameter(objectiveOid, keyResOid, date);
		fillParameterForDay(parameter);
		return render(parameter, TEMPLATE_RESOURCE);
	}	
	
	private void contentBody(DefaultControllerJsonResultObj<MeasureDataInputBody> result, HttpServletRequest request) throws AuthorityException, ControllerException, ServiceException, Exception {
		MeasureDataInputBody inputBody = new MeasureDataInputBody();
		result.setValue( inputBody );
		String dateStatus = StringUtils.defaultString(request.getParameter("dateStatus"));
		String dateStr = StringUtils.defaultString(request.getParameter("date"));
		if (!StringUtils.isBlank(dateStatus) && SimpleUtils.isDate(dateStr) && ("1".equals(dateStatus) || "-1".equals(dateStatus)) ) {
			DateTime dateTime = new DateTime(dateStr);
			if ("1".equals(dateStatus)) { // date +1
				dateTime = dateTime.plusMonths(+1);	
			}
			if ("-1".equals(dateStatus)) { // date -1
				dateTime = dateTime.plusMonths(-1);
			}
			dateStr = dateTime.toString("yyyy-MM-dd");
		}		
		String content = this.renderBody(
				request.getParameter("objectiveOid"),
				request.getParameter("keyResOid"), 
				dateStr);
		inputBody.setContent(content);
		inputBody.setDate(dateStr);
		if (!StringUtils.isBlank(content)) {
			result.setSuccess(YES);
		}		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006M")
	@RequestMapping(value = "/hfOkrBaseEnterMasureDataBodyJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<MeasureDataInputBody> doContentBody(HttpServletRequest request) {
		DefaultControllerJsonResultObj<MeasureDataInputBody> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.contentBody(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006E")
	@RequestMapping(value = "/hfOkrBaseQueryAllDataJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfObjective> doQueryAllData(HttpServletRequest request, HfObjective objective) {
		DefaultControllerJsonResultObj<HfObjective> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.queryAllData(result, objective);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006E")
	@RequestMapping(value = "/hfOkrBaseUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfObjective> doUpdate(HttpServletRequest request, HfObjective objective) {
		DefaultControllerJsonResultObj<HfObjective> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, request, objective);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfObjective objective) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.okrBaseLogicService.delete(objective);
		this.setDefaultResponseJsonResult(result, dResult);
	}		
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0006D")
	@RequestMapping(value = "/hfOkrBaseDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfObjective objective) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, objective);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
