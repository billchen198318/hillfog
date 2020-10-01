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

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.ScriptTypeCode;
import org.qifu.base.model.SearchValue;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.logic.IAggregationMethodLogicService;
import org.qifu.hillfog.service.IAggregationMethodService;
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
	
}
