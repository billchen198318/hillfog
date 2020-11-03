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
package org.qifu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.logic.ISystemExpressionLogicService;
import org.qifu.core.service.ISysExpressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysExpressionController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysExpressionService<TbSysExpression, String> sysExpressionService;
	
	@Autowired
	ISystemExpressionLogicService systemExpressionLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "sys_expr";
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("typeMap", ScriptTypeCode.getTypeMap(true));
	}
	
	private void fetch(String sysExpressionOid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		TbSysExpression sysExpression = this.sysExpressionService.selectByPrimaryKey(sysExpressionOid).getValueEmptyThrowMessage();
		mm.put("sysExpression", sysExpression);
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002Q")
	@RequestMapping(value = "/sysExpressionPage")	
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002Q")
	@RequestMapping(value = "/sysExpressionQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysExpression> > queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysExpression> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysExpression> > queryResult = this.sysExpressionService.findPage(
					"countPageSimple",
					"findPageSimple",
					this.queryParameter(searchValue).fullEquals("exprId").fullLink("name").selectOption("type").value(), 
					pageOf.orderBy("EXPR_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002A")
	@RequestMapping(value = "/sysExpressionCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002E")
	@RequestMapping(value = "/sysExpressionEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("editPage", mm);
			this.fetch(oid, mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}	
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysExpression> result, TbSysExpression sysExpression) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("exprId", sysExpression, "@org.apache.commons.lang3.StringUtils@isBlank( exprId )", "Id is required!")
		.testField("exprId", sysExpression, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( exprId.replaceAll(\"-\", \"\").replaceAll(\"_\", \"\") )", "Id only normal character!")
		.testField("exprId", PleaseSelect.noSelect(sysExpression.getExprId()), "Please change Id value!") // Id 不能使用 all
		.testField("name", sysExpression, "@org.apache.commons.lang3.StringUtils@isBlank( name )", "Name is required!")
		.testField("type", ( !ScriptTypeCode.isTypeCode(sysExpression.getType()) ), "Please select type!")
		.testField("content", sysExpression, "@org.apache.commons.lang3.StringUtils@isBlank( content )", "Expression content is required!")
		.throwMessage();
	}
	
	private void save(DefaultControllerJsonResultObj<TbSysExpression> result, TbSysExpression sysExpression) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysExpression);
		DefaultResult<TbSysExpression> cResult = this.systemExpressionLogicService.create(sysExpression);
		this.setDefaultResponseJsonResult(result, cResult);
		if (result.getValue() != null) {
			result.getValue().setContent(""); // 不要回填 content
		}	
	}
	
	private void update(DefaultControllerJsonResultObj<TbSysExpression> result, TbSysExpression sysExpression) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysExpression);
		DefaultResult<TbSysExpression> uResult = this.systemExpressionLogicService.update(sysExpression);
		this.setDefaultResponseJsonResult(result, uResult);
		if (result.getValue() != null) {
			result.getValue().setContent(""); // 不要回填 content
		}		
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysExpression sysExpression) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.systemExpressionLogicService.delete(sysExpression);
		this.setDefaultResponseJsonResult(result, dResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002A")
	@RequestMapping(value = "/sysExpressionSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysExpression> doSave(TbSysExpression sysExpression) {
		DefaultControllerJsonResultObj<TbSysExpression> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, sysExpression);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002E")
	@RequestMapping(value = "/sysExpressionUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysExpression> doUpdate(TbSysExpression sysExpression) {
		DefaultControllerJsonResultObj<TbSysExpression> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, sysExpression);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0002D")
	@RequestMapping(value = "/sysExpressionDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysExpression sysExpression) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysExpression);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
