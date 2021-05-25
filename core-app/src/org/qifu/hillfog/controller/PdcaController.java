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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.util.UploadSupportUtils;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.logic.IPdcaLogicService;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.model.PDCABase;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IPdcaService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PdcaController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;	
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;	
	
	@Autowired
	IPdcaService<HfPdca, String> pdcaService;
	
	@Autowired
	IPdcaLogicService pdcaLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_pdca";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type)) {
			if (mm.get("masterType") != null) {
				this.generatePdcaNumber(mm);
			}
			String startDate = this.getNowDate2();
			DateTime dateTime = new DateTime(new Date());
			dateTime = dateTime.plusDays(30);
			String endDate = SimpleUtils.getStrYMD(dateTime.toDate(), "-");
			mm.put("startDate", startDate);
			mm.put("endDate", endDate);
		}
		if ("createPage".equals(type) || "editPage".equals(type)) {
			List<String> empList = this.employeeService.findInputAutocomplete();
			mm.put("empList", empList);
			mm.put("empInputAutocomplete", pageAutocompleteContent(empList));	
			mm.put("frequencyMap", MeasureDataCode.getFrequencyMap(true));
		}		
	}
	
	private void generatePdcaNumber(ModelMap mm) throws ServiceException, Exception {
		String head = "OKR";
		if (mm.get("kpi") != null) {
			HfKpi kpi = (HfKpi) mm.get("kpi");
			head = kpi.getId();
		}
		mm.put("pdcaNum", this.pdcaService.selectMaxPdcaNum(head));
	}
	
	private void fetchForCreatePage(ModelMap mm, String oid, String masterType) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("masterType", masterType);
		mm.put("oid", oid);
		if (PDCABase.SOURCE_MASTER_KPI_TYPE.equals(masterType)) {
			HfKpi kpi = kpiService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
			mm.put("kpi", kpi);
		} else if (PDCABase.SOURCE_MASTER_OBJECTIVE_TYPE.equals(masterType)) {
			HfObjective objective = objectiveService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
			mm.put("objective", objective);			
		} else {
			throw new ControllerException("args type error!");
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001Q")
	@RequestMapping("/hfPdcaPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001Q")
	@RequestMapping(value = "/hfPdcaLoadUploadFileShowName", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})		
	public @ResponseBody DefaultControllerJsonResultObj<String> loadUploadFileShowName(HttpServletRequest request, @RequestParam(name="oid") String uploadOid) {
		DefaultControllerJsonResultObj<String> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			TbSysUpload upload = UploadSupportUtils.findUploadNoByteContent(uploadOid);
			result.setValue( upload.getShowName() );
			result.setSuccess( YES );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001A")
	@RequestMapping("/hfPdcaCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String sourceMasterOid, @RequestParam(name="masterType") String masterType) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetchForCreatePage(mm, sourceMasterOid, masterType);
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
	
	private void checkFields(DefaultControllerJsonResultObj<HfPdca> result, HttpServletRequest request, HfPdca pdca) throws ControllerException, ServiceException, Exception {
		String masterType = request.getParameter("masterType");
		CheckControllerFieldHandler<HfPdca> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("name", pdca, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("startDate", !SimpleUtils.isDate(request.getParameter("startDate")), "Please input start date!")
		.testField("endDate", !SimpleUtils.isDate(request.getParameter("endDate")), "Please input end date!");
		checkHandler.throwMessage();
		if (SimpleUtils.getDaysBetween(request.getParameter("startDate"), request.getParameter("endDate")) < 1) {
			checkHandler.throwMessage("startDate", "Please adjust the start and end date fields!");
		}
		if (PDCABase.SOURCE_MASTER_KPI_TYPE.equals(masterType)) {
			checkHandler
			.testField("kpiFrequency", PleaseSelect.noSelect(pdca.getKpiFrequency()), "Please select frequency!")
			.testField("kpiMeasureDate1", !SimpleUtils.isDate(request.getParameter("kpiMeasureDate1")), "Please input KPI measure-data start date!")
			.testField("kpiMeasureDate2", !SimpleUtils.isDate(request.getParameter("kpiMeasureDate2")), "Please input KPI measure-data end date!")
			.throwMessage();
			if (SimpleUtils.getDaysBetween(request.getParameter("kpiMeasureDate1"), request.getParameter("kpiMeasureDate2")) < 1) {
				checkHandler.throwMessage("startDate", "Please adjust the KPI masure-data start and KPI masure-data end date fields!");
			}			
		}
		
		String owner = StringUtils.defaultString( request.getParameter("owner") );
		Map<String, List<Map<String, Object>>> ownerJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( owner, LinkedHashMap.class );
		List ownerList = ownerJsonData.get("items");
		if (ownerList.size() < 1) {
			checkHandler.throwMessage("owner", "Please add owner employee!");
		}		
		
		String planStr = StringUtils.defaultString( request.getParameter("planList") );
		Map<String, List<Map<String, Object>>> planJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( planStr, LinkedHashMap.class );
		List planMapList = planJsonData.get("items");		
		if (planMapList.size() < 1) {
			checkHandler.throwMessage("plan", "At least one Plan item is required!");
		}
		
		String doStr = StringUtils.defaultString( request.getParameter("doList") );
		Map<String, List<Map<String, Object>>> doJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( doStr, LinkedHashMap.class );
		List doMapList = doJsonData.get("items");			
		
		String checkStr = StringUtils.defaultString( request.getParameter("checkList") );
		Map<String, List<Map<String, Object>>> checkJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( checkStr, LinkedHashMap.class );
		List checkMapList = checkJsonData.get("items");			
		
		String actStr = StringUtils.defaultString( request.getParameter("actList") );
		Map<String, List<Map<String, Object>>> actJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( actStr, LinkedHashMap.class );
		List actMapList = actJsonData.get("items");			
		
		// check item name, startDate, endDate, owner, parent
		this.checkPdcaItem(checkHandler, PDCABase.TYPE_P, request.getParameter("startDate"), request.getParameter("endDate"), planMapList);
		this.checkPdcaItem(checkHandler, PDCABase.TYPE_D, request.getParameter("startDate"), request.getParameter("endDate"), doMapList);
		this.checkPdcaItem(checkHandler, PDCABase.TYPE_C, request.getParameter("startDate"), request.getParameter("endDate"), checkMapList);
		this.checkPdcaItem(checkHandler, PDCABase.TYPE_A, request.getParameter("startDate"), request.getParameter("endDate"), actMapList);
	}
	
	private void checkPdcaItem(CheckControllerFieldHandler<HfPdca> checkHandler, String itemType, String projectStartDate, String projectEndDate, List<Map<String, Object>> itemMapList) throws ControllerException, Exception {
		for (Map<String, Object> itemDataMap : itemMapList) {
			if (!PDCABase.TYPE_P.equals(itemType)) {
				checkHandler.testField("pdcaItemParent", PleaseSelect.noSelect((String)itemDataMap.get("parentOid")), "[" + itemType + "] Please select PDCA item parent!").throwMessage();
			}
			checkHandler.testField("pdcaItemName", itemDataMap, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "[" + itemType + "] Name is blank!").throwMessage();
			checkHandler.testField("pdcaItemStartDate", !SimpleUtils.isDate((String)itemDataMap.get("startDate")), "[" + itemType + "] Please input PDCA item start date!").throwMessage();
			checkHandler.testField("pdcaItemEndDate", !SimpleUtils.isDate((String)itemDataMap.get("endDate")), "[" + itemType + "] Please input PDCA item end date!").throwMessage();
			String itemStartDate = (String)itemDataMap.get("startDate");
			String itemEndDate = (String)itemDataMap.get("endDate");
			if (SimpleUtils.getDaysBetween(itemStartDate, itemEndDate) < 1) {
				checkHandler.throwMessage("itemStartDate", "[" + itemType + "] Please adjust the PDCA item start and end date fields!");
			}			
			if (SimpleUtils.getDaysBetween(projectStartDate, itemStartDate) < 0) {
				checkHandler.throwMessage("itemStartDate", "[" + itemType + "] The starting date of PDCA Item shall not be less than the starting date of the project!");
			}
			if (SimpleUtils.getDaysBetween(itemEndDate, projectEndDate) < 0) {
				checkHandler.throwMessage("itemStartDate", "[" + itemType + "] The end date of PDCA Item cannot be greater than the end date of the project!");
			}			
			List<String> ownerList = (List<String>) itemDataMap.get("ownerList");
			checkHandler.testField("pdcaItemOwner", null == ownerList || ownerList.size() < 1, "[" + itemType + "] Please input PDCA item owner!").throwMessage();
		}
	}
	
	private void save(DefaultControllerJsonResultObj<HfPdca> result, HttpServletRequest request, HfPdca pdca) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, request, pdca);
		
		String owner = StringUtils.defaultString( request.getParameter("owner") );
		Map<String, List<Map<String, Object>>> ownerJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( owner, LinkedHashMap.class );
		List ownerList = ownerJsonData.get("items");
		
		String uploadAttc = StringUtils.defaultString( request.getParameter("uploadAttc") );
		Map<String, List<Map<String, Object>>> uploadAttcJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( uploadAttc, LinkedHashMap.class );
		List uploadAttcJsonList = uploadAttcJsonData.get("items");		
		List<String> uploadOidsList = new ArrayList<String>();
		for (int i = 0 ; uploadAttcJsonList != null && i < uploadAttcJsonList.size(); i++) {
			Map<String, Object> dataMap = (Map<String, Object>) uploadAttcJsonList.get(i);
			uploadOidsList.add( (String) dataMap.get("oid") );
		}
		
		String plan = StringUtils.defaultString( request.getParameter("planList") );
		Map<String, List<Map<String, Object>>> planJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( plan, LinkedHashMap.class );
		List planMapList = planJsonData.get("items");	
		
		String doStr = StringUtils.defaultString( request.getParameter("doList") );
		Map<String, List<Map<String, Object>>> doJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( doStr, LinkedHashMap.class );
		List doMapList = doJsonData.get("items");			
		
		String check = StringUtils.defaultString( request.getParameter("checkList") );
		Map<String, List<Map<String, Object>>> checkJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( check, LinkedHashMap.class );
		List checkMapList = checkJsonData.get("items");			
		
		String act = StringUtils.defaultString( request.getParameter("actList") );
		Map<String, List<Map<String, Object>>> actJsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( act, LinkedHashMap.class );
		List actMapList = actJsonData.get("items");	
		
		DefaultResult<HfPdca> iResult = this.pdcaLogicService.create(pdca, request.getParameter("masterType"), request.getParameter("masterOid"), ownerList, uploadOidsList, 
				planMapList, doMapList, checkMapList, actMapList);
		this.setDefaultResponseJsonResult(result, iResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001A")
	@RequestMapping(value = "/hfPdcaSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfPdca> doSave(HttpServletRequest request, HfPdca pdca) {
		DefaultControllerJsonResultObj<HfPdca> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, request, pdca);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001E")
	@RequestMapping("/hfPdcaEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001V")
	@RequestMapping("/hfPdcaViewDetailPage")
	public String detailPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("detail-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetch(mm, oid);
			this.init("detailPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}
	
}
