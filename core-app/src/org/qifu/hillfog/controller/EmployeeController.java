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

import org.apache.commons.collections.CollectionUtils;
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
import org.qifu.core.util.LocaleMessageSourceUtils;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfEmployeeHier;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IEmployeeLogicService;
import org.qifu.hillfog.service.IEmployeeService;
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
public class EmployeeController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IEmployeeLogicService employeeLogicService;
	
	@Autowired
	LocaleMessageSourceUtils localeMessageSourceUtils;	
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_emp";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			mm.put("orgInputAutocomplete", this.pageAutocompleteContent(this.orgDeptService.findInputAutocomplete()));
		}
		if ("editPage".equals(type)) {
			HfEmployee employee = (HfEmployee) mm.get("employee");
			mm.put("orgInputAutocompleteValue", this.pageAutocompleteContent(this.orgDeptService.findInputAutocompleteByAccount(employee.getAccount())));
		}
		if ("hierarchyPage".equals(type)) {
			mm.put("ztreeData", this.employeeLogicService.findZtreeData());
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfEmployee employee = this.employeeService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("employee", employee);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002Q")
	@RequestMapping("/hfEmployeePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002A")
	@RequestMapping("/hfEmployeeCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002E")
	@RequestMapping("/hfEmployeeEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002Q")
	@RequestMapping(value = "/hfEmployeeQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<HfEmployee>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<HfEmployee>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<HfEmployee>> queryResult = this.employeeService.findPage(
					this.queryParameter(searchValue).fullEquals("account").beginningLike("empIdLike").fullLink("nameLike").fullLink("jobTitleLike").value(), 
					pageOf.orderBy("EMP_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	private void checkFields(DefaultControllerJsonResultObj<HfEmployee> result, HfEmployee employee, String password1, String password2, String orgInputAutocompleteJsonStr, boolean createMode) throws ControllerException, Exception {
		CheckControllerFieldHandler<HfEmployee> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("account", employee, "@org.apache.commons.lang3.StringUtils@isBlank(account)", localeMessageSourceUtils.getMessage("page.employee.message01"))
		.testField("account", ( PleaseSelect.noSelect(employee.getAccount()) ), localeMessageSourceUtils.getMessage("page.employee.message02")) // ORG_ID 不能用  "all" 這個下拉值
		.testField("account", ( !SimpleUtils.checkBeTrueOf_azAZ09Id(super.defaultString(employee.getAccount())) ), localeMessageSourceUtils.getMessage("page.employee.message03"))
		.testField("empId", employee, "@org.apache.commons.lang3.StringUtils@isBlank(empId)", localeMessageSourceUtils.getMessage("page.employee.message04"))
		.testField("empId", ( PleaseSelect.noSelect(employee.getEmpId()) ), localeMessageSourceUtils.getMessage("page.employee.message05")) // ORG_ID 不能用  "all" 這個下拉值
		.testField("empId", ( !SimpleUtils.checkBeTrueOf_azAZ09Id(super.defaultString(employee.getEmpId())) ), localeMessageSourceUtils.getMessage("page.employee.message06"))
		.testField("name", employee, "@org.apache.commons.lang3.StringUtils@isBlank(name)", localeMessageSourceUtils.getMessage("page.employee.message07"))
		.testField("employeeOrganization", StringUtils.isBlank(orgInputAutocompleteJsonStr), localeMessageSourceUtils.getMessage("page.employee.message08"));
		checkHandler.throwMessage();
		if (createMode) {
			checkHandler.testField("password1", StringUtils.isBlank(password1), localeMessageSourceUtils.getMessage("page.employee.message09")).throwMessage();
			checkHandler.testField("password2", StringUtils.isBlank(password2), localeMessageSourceUtils.getMessage("page.employee.message09")).throwMessage();
			this.checkFieldsForPassword(checkHandler, password1, password2);
		}
		if (!createMode && !StringUtils.isBlank(password1) && !StringUtils.isBlank(password2)) {
			this.checkFieldsForPassword(checkHandler, password1, password2);
		}
		Map<String, List<Map<String, Object>>> jsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( orgInputAutocompleteJsonStr, LinkedHashMap.class );
		List orgInputAutocompleteList = jsonData.get("items");
		if (CollectionUtils.isEmpty(orgInputAutocompleteList)) {
			checkHandler.throwMessage("employeeOrganization", localeMessageSourceUtils.getMessage("page.employee.message08"));
		}
	}
	
	private void checkFieldsForPassword(CheckControllerFieldHandler<HfEmployee> checkHandler, String password1, String password2) throws ControllerException, Exception {
		checkHandler.testField("password1", !password1.equals(password2), localeMessageSourceUtils.getMessage("page.employee.message10"));
		checkHandler.testField("password1", !SimpleUtils.checkBeTrueOf_azAZ09(4, 15, password1), localeMessageSourceUtils.getMessage("page.employee.message10"));
		checkHandler.throwMessage();
	}
	
	private void save(DefaultControllerJsonResultObj<HfEmployee> result, HfEmployee employee, String password1, String password2, String orgInputAutocompleteJsonStr) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, employee, password1, password2, orgInputAutocompleteJsonStr, true);
		Map<String, List<Map<String, Object>>> jsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( orgInputAutocompleteJsonStr, LinkedHashMap.class );
		List orgInputAutocompleteList = jsonData.get("items");
		DefaultResult<HfEmployee> iResult = this.employeeLogicService.create(employee, password1, orgInputAutocompleteList);
		this.setDefaultResponseJsonResult(result, iResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002A")
	@RequestMapping(value = "/hfEmployeeSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfEmployee> doSave(HttpServletRequest request, HfEmployee employee) {
		DefaultControllerJsonResultObj<HfEmployee> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(
					result, 
					employee, 
					request.getParameter("password1"), 
					request.getParameter("password2"), 
					request.getParameter("employeeOrganization"));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void update(DefaultControllerJsonResultObj<HfEmployee> result, HfEmployee employee, String password1, String password2, String orgInputAutocompleteJsonStr) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, employee, password1, password2, orgInputAutocompleteJsonStr, false);
		Map<String, List<Map<String, Object>>> jsonData = (Map<String, List<Map<String, Object>>>) new ObjectMapper().readValue( orgInputAutocompleteJsonStr, LinkedHashMap.class );
		List orgInputAutocompleteList = jsonData.get("items");		
		DefaultResult<HfEmployee> uResult = this.employeeLogicService.update(employee, password1, orgInputAutocompleteList);
		this.setDefaultResponseJsonResult(result, uResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002E")
	@RequestMapping(value = "/hfEmployeeUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfEmployee> doUpdate(HttpServletRequest request, HfEmployee employee) {
		DefaultControllerJsonResultObj<HfEmployee> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {			
			this.update(
					result, 
					employee, 
					request.getParameter("password1"), 
					request.getParameter("password2"), 
					request.getParameter("employeeOrganization"));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfEmployee employee) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> sysResult = this.employeeLogicService.delete(employee);
		this.setDefaultResponseJsonResult(result, sysResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002D")
	@RequestMapping(value = "/hfEmployeeDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfEmployee employee) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, employee);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002S")
	@RequestMapping("/hfEmployeeHierarchyPage")
	public String hierarchyPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPageWithNamespace("hierarchy-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("hierarchyPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0002S")
	@RequestMapping(value = "/hfEmployeeHierarchyUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfEmployeeHier> doHierarchyUpdate(HttpServletRequest request, @RequestParam(name="empOid") String empOid, @RequestParam(name="parentOid") String parentOid) {
		DefaultControllerJsonResultObj<HfEmployeeHier> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.setDefaultResponseJsonResult(result, this.employeeLogicService.updateHierarchy(empOid, parentOid));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
}
