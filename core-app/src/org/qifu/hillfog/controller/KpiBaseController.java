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
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IKpiLogicService;
import org.qifu.hillfog.model.KpiBasicCode;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IFormulaService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IOrgDeptService;
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
public class KpiBaseController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IKpiLogicService kpiLogicService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Autowired
	IFormulaService<HfFormula, String> formulaService;	
	
	@Autowired
	IAggregationMethodService<HfAggregationMethod, String> aggregationMethodService;	
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_kpib";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			mm.put("compareTypeMap", KpiBasicCode.getCompareTypeMap(true));
			mm.put("dataTypeMap", KpiBasicCode.getDataTypeMap(true));
			mm.put("managementMap", KpiBasicCode.getManagementMap(true));
			mm.put("quasiRangeMap", KpiBasicCode.getQuasiRangeMap());
			mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocomplete()));
			mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocomplete()));
			mm.put("formulaMap", this.formulaService.findMap(true));
			mm.put("aggrMethodMap", this.aggregationMethodService.findMap(true));
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfKpi kpi = this.kpiService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("kpi", kpi);
		HfFormula formula = new HfFormula();
		formula.setForId( kpi.getForId() );
		formula = this.formulaService.selectByUniqueKey(formula).getValueEmptyThrowMessage();
		mm.put("formula", formula);
		HfAggregationMethod aggrMethod = new HfAggregationMethod();
		aggrMethod.setAggrId( kpi.getAggrId() );
		aggrMethod = this.aggregationMethodService.selectByUniqueKey(aggrMethod).getValueEmptyThrowMessage();
		mm.put("aggrMethod", aggrMethod);
		mm.put("selOrgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocompleteByKpiId(kpi.getId())));
		mm.put("selEmpInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocompleteByKpiId(kpi.getId())));
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005Q")
	@RequestMapping("/hfKpiBasePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005A")
	@RequestMapping("/hfKpiBaseCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005E")
	@RequestMapping("/hfKpiBaseEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005Q")
	@RequestMapping(value = "/hfKpiBaseQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<HfKpi>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<HfKpi>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<HfKpi>> queryResult = this.kpiService.findPage(
					this.queryParameter(searchValue).fullEquals("id").fullLink("nameLike").value(), 
					pageOf.orderBy("ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void checkFields(DefaultControllerJsonResultObj<HfKpi> result, HfKpi kpi, String forOid, String aggrOid, String selKpiDept, String selKpiEmp) throws ControllerException, Exception {
		CheckControllerFieldHandler<HfKpi> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("id", kpi, "@org.apache.commons.lang3.StringUtils@isBlank(id)", "Id is blank!")
		.testField("id", ( PleaseSelect.noSelect(kpi.getId()) ), "Please change Id value!") // ID 不能用  "all" 這個下拉值
		.testField("id", ( !SimpleUtils.checkBeTrueOf_azAZ09Id(super.defaultString(kpi.getId())) ), "Id only normal character!")
		.testField("forOid", PleaseSelect.noSelect(forOid), "Please select formula!")
		.testField("aggrOid", PleaseSelect.noSelect(aggrOid), "Please select aggregation method!")
		.testField("name", kpi, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("weight", kpi, "weight == null", "weight only key-in numbers!")
		.testField("max", kpi, "max == null", "Maximum only key-in numbers!")
		.testField("target", kpi, "target == null", "Target only key-in numbers!")
		.testField("min", kpi, "min == null", "Minimum only key-in numbers!")
		.testField("unit", kpi, "@org.apache.commons.lang3.StringUtils@isBlank(unit)", "Unit is blank!")
		.testField("management", PleaseSelect.noSelect(kpi.getManagement()), "Please select management!")
		.testField("compareType", PleaseSelect.noSelect(kpi.getCompareType()), "Please select compare type!")
		.testField("quasiRange", kpi, "quasiRange == null", "Quasi range only key-in numbers!")
		.testField("dataType", PleaseSelect.noSelect(kpi.getDataType()), "Please select data type!");
		checkHandler.throwMessage();
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(kpi.getDataType()) || KpiBasicCode.DATA_TYPE_DEPARTMENT.equals(kpi.getDataType())) {
			checkHandler.testField("kpiOrga", StringUtils.isBlank(selKpiDept), "Please add organization!").throwMessage();
			Map<String, List<Map<String, Object>>> jsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( selKpiDept, LinkedHashMap.class );
			List orgInputAutocompleteList = jsonData.get("items");
			if (null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
				checkHandler.throwMessage("kpiOrga", "Please add organization!");
			}			
		}
		if (KpiBasicCode.DATA_TYPE_BOTH.equals(kpi.getDataType()) || KpiBasicCode.DATA_TYPE_PERSONAL.equals(kpi.getDataType())) {
			checkHandler.testField("kpiEmpl", StringUtils.isBlank(selKpiDept), "Please add employee!").throwMessage();
			Map<String, List<Map<String, Object>>> jsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( selKpiEmp, LinkedHashMap.class );
			List orgInputAutocompleteList = jsonData.get("items");
			if (null == orgInputAutocompleteList || orgInputAutocompleteList.size() < 1) {
				checkHandler.throwMessage("kpiEmpl", "Please add employee!");
			}			
		}
	}
	
	private void save(DefaultControllerJsonResultObj<HfKpi> result, HfKpi kpi, String forOid, String aggrOid, String selKpiDept, String selKpiEmp) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, kpi, forOid, aggrOid, selKpiDept, selKpiEmp);
		Map<String, List<Map<String, Object>>> jsonData1 = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( selKpiDept, LinkedHashMap.class );
		List orgInputAutocompleteList = jsonData1.get("items");
		Map<String, List<Map<String, Object>>> jsonData2 = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( selKpiEmp, LinkedHashMap.class );
		List empInputAutocompleteList = jsonData2.get("items");
		DefaultResult<HfKpi> iResult = this.kpiLogicService.create(kpi, forOid, aggrOid, orgInputAutocompleteList, empInputAutocompleteList);
		this.setDefaultResponseJsonResult(result, iResult);	
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005A")
	@RequestMapping(value = "/hfKpiBaseSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfKpi> doSave(HttpServletRequest request, HfKpi kpi) {
		DefaultControllerJsonResultObj<HfKpi> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(
					result, 
					kpi, 
					request.getParameter("forOid"), 
					request.getParameter("aggrOid"), 
					request.getParameter("selKpiDept"), 
					request.getParameter("selKpiEmp"));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	private void update(DefaultControllerJsonResultObj<HfKpi> result, HfKpi kpi, String forOid, String aggrOid, String selKpiDept, String selKpiEmp) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, kpi, forOid, aggrOid, selKpiDept, selKpiEmp);
		Map<String, List<Map<String, Object>>> jsonData1 = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( selKpiDept, LinkedHashMap.class );
		List orgInputAutocompleteList = jsonData1.get("items");
		Map<String, List<Map<String, Object>>> jsonData2 = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( selKpiEmp, LinkedHashMap.class );
		List empInputAutocompleteList = jsonData2.get("items");
		DefaultResult<HfKpi> uResult = this.kpiLogicService.update(kpi, forOid, aggrOid, orgInputAutocompleteList, empInputAutocompleteList);
		this.setDefaultResponseJsonResult(result, uResult);		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005E")
	@RequestMapping(value = "/hfKpiBaseUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfKpi> doUpdate(HttpServletRequest request, HfKpi kpi) {
		DefaultControllerJsonResultObj<HfKpi> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(
					result, 
					kpi, 
					request.getParameter("forOid"), 
					request.getParameter("aggrOid"), 
					request.getParameter("selKpiDept"), 
					request.getParameter("selKpiEmp"));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}			
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfKpi kpi) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.kpiLogicService.delete(kpi);
		this.setDefaultResponseJsonResult(result, dResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005D")
	@RequestMapping(value = "/hfKpiBaseDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfKpi kpi) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, kpi);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
