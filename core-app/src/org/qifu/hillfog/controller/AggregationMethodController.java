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
import org.qifu.base.model.ScriptTypeCode;
import org.qifu.base.model.SearchValue;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.logic.IAggregationMethodLogicService;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AggregationMethodController extends BaseControllerSupport implements IPageNamespaceProvide {
	private static final int MAX_LENGTH = 500;
	
	@Autowired
	IAggregationMethodService<HfAggregationMethod, String> aggregationMethodService;
	
	@Autowired
	IAggregationMethodLogicService aggregationMethodLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_aggr";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			mm.put("typeMap", ScriptTypeCode.getTypeMap(true));
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfAggregationMethod aggrMethod = this.aggregationMethodService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("aggrMethod", aggrMethod);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004Q")
	@RequestMapping("/hfAggregationMethodPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004A")
	@RequestMapping("/hfAggregationMethodCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004E")
	@RequestMapping("/hfAggregationMethodEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004Q")
	@RequestMapping(value = "/hfAggregationMethodQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<HfAggregationMethod>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<HfAggregationMethod>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<HfAggregationMethod>> queryResult = this.aggregationMethodService.findPage(
					this.queryParameter(searchValue).fullEquals("aggrId").fullLink("nameLike").value(), 
					pageOf.orderBy("AGGR_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void checkFields(DefaultControllerJsonResultObj<HfAggregationMethod> result, HfAggregationMethod aggrMethod) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("aggrId", aggrMethod, "@org.apache.commons.lang3.StringUtils@isBlank(aggrId)", "Id is blank!")
		.testField("aggrId", ( PleaseSelect.noSelect(aggrMethod.getAggrId()) ), "Please change Id value!") // FOR_ID 不能用  "all" 這個下拉值
		.testField("aggrId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(aggrMethod.getAggrId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("name", aggrMethod, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("type", ( PleaseSelect.noSelect(aggrMethod.getType()) ), "Please select type!")
		.testField("expression1", aggrMethod, "@org.apache.commons.lang3.StringUtils@isBlank(expression1)", "Expression is blank!")
		.testField("expression2", aggrMethod, "@org.apache.commons.lang3.StringUtils@isBlank(expression2)", "Expression (date range) is blank!")
		.throwMessage();
	}		
	
	private void save(DefaultControllerJsonResultObj<HfAggregationMethod> result, HfAggregationMethod aggrMethod) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, aggrMethod);
		if (StringUtils.defaultString(aggrMethod.getDescription()).length() > MAX_LENGTH) {
			aggrMethod.setDescription( aggrMethod.getDescription().substring(0, MAX_LENGTH) );
		}
		DefaultResult<HfAggregationMethod> iResult = this.aggregationMethodService.insert(aggrMethod);
		if (iResult.getValue() != null) {
			result.setValue( iResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( iResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004A")
	@RequestMapping(value = "/hfAggregationMethodSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfAggregationMethod> doSave(HttpServletRequest request, HfAggregationMethod aggrMethod) {
		DefaultControllerJsonResultObj<HfAggregationMethod> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, aggrMethod);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}
	
	private void update(DefaultControllerJsonResultObj<HfAggregationMethod> result, HfAggregationMethod aggrMethod) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, aggrMethod);
		if (StringUtils.defaultString(aggrMethod.getDescription()).length() > MAX_LENGTH) {
			aggrMethod.setDescription( aggrMethod.getDescription().substring(0, MAX_LENGTH) );
		}
		DefaultResult<HfAggregationMethod> uResult = this.aggregationMethodService.update(aggrMethod);
		if ( uResult.getValue() != null ) {
			result.setValue( uResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( uResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004E")
	@RequestMapping(value = "/hfAggregationMethodUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfAggregationMethod> doUpdate(HttpServletRequest request, HfAggregationMethod aggrMethod) {
		DefaultControllerJsonResultObj<HfAggregationMethod> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, aggrMethod);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfAggregationMethod aggrMethod) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.aggregationMethodLogicService.delete(aggrMethod);
		if (dResult.getValue() != null) {
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0004D")
	@RequestMapping(value = "/hfAggregationMethodDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfAggregationMethod aggrMethod) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, aggrMethod);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
