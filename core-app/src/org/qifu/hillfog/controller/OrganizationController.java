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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IOrganizationLogicService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrganizationController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	private static final int MAX_LENGTH = 500;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Autowired
	IOrganizationLogicService organizationLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_org";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfOrgDept orgDept = this.orgDeptService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("orgDept", orgDept);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001Q")
	@RequestMapping("/hfOrgDeptPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001A")
	@RequestMapping("/hfOrgDeptCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001E")
	@RequestMapping("/hfOrgDeptEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("editPage", mm);
			this.fetch(mm, oid);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001Q")
	@RequestMapping(value = "/hfOrgDeptQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<HfOrgDept>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<HfOrgDept>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<HfOrgDept>> queryResult = this.orgDeptService.findPage(
					this.queryParameter(searchValue).fullEquals("orgId").fullLink("nameLike").value(), 
					pageOf.orderBy("ORG_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	private void checkFields(DefaultControllerJsonResultObj<HfOrgDept> result, HfOrgDept orgDept) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("orgId", orgDept, "@org.apache.commons.lang3.StringUtils@isBlank(orgId)", "Id is blank!")
		.testField("orgId", ( PleaseSelect.noSelect(orgDept.getOrgId()) ), "Please change Id value!") // ORG_ID 不能用  "all" 這個下拉值
		.testField("orgId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(orgDept.getOrgId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("name", orgDept, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.throwMessage();		
		orgDept.setName( orgDept.getName().replaceAll("/", "") );
	}	
	
	private void save(DefaultControllerJsonResultObj<HfOrgDept> result, HfOrgDept orgDept) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, orgDept);
		if (StringUtils.defaultString(orgDept.getDescription()).length() > MAX_LENGTH) {
			orgDept.setDescription( orgDept.getDescription().substring(0, MAX_LENGTH) );
		}
		DefaultResult<HfOrgDept> iResult = this.orgDeptService.insert(orgDept);
		if (iResult.getValue() != null) {
			result.setValue( iResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( iResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001A")
	@RequestMapping(value = "/hfOrgDeptSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfOrgDept> doSave(HttpServletRequest request, HfOrgDept orgDept) {
		DefaultControllerJsonResultObj<HfOrgDept> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, orgDept);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void update(DefaultControllerJsonResultObj<HfOrgDept> result, HfOrgDept orgDept) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, orgDept);
		DefaultResult<HfOrgDept> sysResult = this.orgDeptService.update(orgDept);
		if ( sysResult.getValue() != null ) {
			result.setValue( sysResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( sysResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001E")
	@RequestMapping(value = "/hfOrgDeptUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfOrgDept> doUpdate(HfOrgDept orgDept) {
		DefaultControllerJsonResultObj<HfOrgDept> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, orgDept);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfOrgDept orgDept) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> sysResult = this.organizationLogicService.delete(orgDept);
		if (sysResult.getValue() != null) {
			result.setSuccess( YES );
		}
		result.setMessage( sysResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0001D")
	@RequestMapping(value = "/hfOrgDeptDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfOrgDept orgDept) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, orgDept);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
