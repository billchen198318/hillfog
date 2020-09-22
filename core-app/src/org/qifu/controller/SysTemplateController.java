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
import org.qifu.base.model.SearchValue;
import org.qifu.core.entity.TbSysTemplate;
import org.qifu.core.entity.TbSysTemplateParam;
import org.qifu.core.logic.ISystemTemplateLogicService;
import org.qifu.core.service.ISysTemplateParamService;
import org.qifu.core.service.ISysTemplateService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysTemplateController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysTemplateService<TbSysTemplate, String> sysTemplateService;
	
	@Autowired
	ISysTemplateParamService<TbSysTemplateParam, String> sysTemplateParamService;
	
	@Autowired
	ISystemTemplateLogicService systemTemplateLogicService;	
	
	@Override
	public String viewPageNamespace() {
		return "sys_temp";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	private void fetch(String oid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		DefaultResult<TbSysTemplate> result = this.sysTemplateService.selectByPrimaryKey(oid);
		if ( result.getValue() == null ) {
			throw new ControllerException( result.getMessage() );
		}
		TbSysTemplate template = result.getValue();
		mm.put("template", template);
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004Q")
	@RequestMapping(value = "/sysTemplatePage")	
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004A")
	@RequestMapping(value = "/sysTemplateCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004E")
	@RequestMapping("/sysTemplateEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004S01Q")
	@RequestMapping(value = "/sysTemplateParam")
	public String paramPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("param-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("editParamPage", mm);
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004Q")
	@RequestMapping(value = "/sysTemplateQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysTemplate> >  queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysTemplate> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysTemplate> > queryResult = this.sysTemplateService.findPage(
					this.queryParameter(searchValue).fullEquals("templateId").fullLink("title").value(), 
					pageOf.orderBy("TEMPLATE_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
		
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004S01Q")
	@RequestMapping(value = "/sysTemplateParamQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysTemplateParam> >  paramQueryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysTemplateParam> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysTemplateParam> > queryResult = this.sysTemplateParamService.findPage(
					this.queryParameter(searchValue).fullEquals("templateOid").value(), 
					pageOf.orderBy("TEMPLATE_ID, IS_TITLE, TEMPLATE_VAR").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}		
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysTemplate> result, TbSysTemplate template) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("templateId", template, "@org.apache.commons.lang3.StringUtils@isBlank(templateId)", "Id is blank!")
		.testField("templateId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(template.getTemplateId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("templateId", ( PleaseSelect.noSelect(template.getTemplateId()) ), "Please change Id value!") // Role 不能用  "all" 這個下拉值
		.testField("title", template, "@org.apache.commons.lang3.StringUtils@isBlank(title)", "Title is blank!")
		.testField("message", template, "@org.apache.commons.lang3.StringUtils@isBlank(message)", "Message is blank!")
		.throwMessage();		
	}
	
	private void checkFieldsForParam(DefaultControllerJsonResultObj<TbSysTemplateParam> result, TbSysTemplateParam templateParam) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("templateVar", templateParam, "@org.apache.commons.lang3.StringUtils@isBlank(templateVar)", "Template variable is blank!")
		.testField("objectVar", templateParam, "@org.apache.commons.lang3.StringUtils@isBlank(objectVar)", "Object variable is blank!")
		.testField("templateVar", templateParam, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09(templateVar)", "Template variable only normal character!")
		.testField("objectVar", templateParam, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09(objectVar)", "Object variable only normal character!")
		.throwMessage();
	}	
	
	private void save(DefaultControllerJsonResultObj<TbSysTemplate> result, TbSysTemplate template) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, template);
		DefaultResult<TbSysTemplate> tResult = this.systemTemplateLogicService.create(template);
		if ( tResult.getValue() != null ) {
			result.setValue( tResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getMessage() );
	}
	
	private void update(DefaultControllerJsonResultObj<TbSysTemplate> result, TbSysTemplate template) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, template);
		DefaultResult<TbSysTemplate> tResult = this.systemTemplateLogicService.update(template);
		if ( tResult.getValue() != null ) {
			result.setValue( tResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getMessage() );		
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysTemplate template) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> tResult = this.systemTemplateLogicService.delete(template);
		if ( tResult.getValue() != null && tResult.getValue() ) {
			result.setValue( Boolean.TRUE );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getMessage() );
	}
	
	private void saveParam(DefaultControllerJsonResultObj<TbSysTemplateParam> result, String templateOid, TbSysTemplateParam templateParam) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFieldsForParam(result, templateParam);
		DefaultResult<TbSysTemplateParam> tResult = this.systemTemplateLogicService.createParam(templateParam, templateOid);
		if ( tResult.getValue() != null ) {
			result.setValue( tResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getMessage() );
	}	
	
	private void deleteParam(DefaultControllerJsonResultObj<Boolean> result, TbSysTemplateParam templateParam) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> tResult = this.systemTemplateLogicService.deleteParam(templateParam);
		if ( tResult.getValue() != null && tResult.getValue() ) {
			result.setValue( Boolean.TRUE );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getMessage() );
	}		
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004A")
	@RequestMapping(value = "/sysTemplateSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysTemplate> doSave(TbSysTemplate template) {
		DefaultControllerJsonResultObj<TbSysTemplate> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, template);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004E")
	@RequestMapping(value = "/sysTemplateUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysTemplate> doUpdate(TbSysTemplate template) {
		DefaultControllerJsonResultObj<TbSysTemplate> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, template);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004D")
	@RequestMapping(value = "/sysTemplateDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysTemplate template) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, template);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004S01A")
	@RequestMapping(value = "/sysTemplateParamSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysTemplateParam> doParamSave(@RequestParam("templateOid") String templateOid, TbSysTemplateParam templateParam) {
		DefaultControllerJsonResultObj<TbSysTemplateParam> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.saveParam(result, templateOid, templateParam);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0004S01D")
	@RequestMapping(value = "/sysTemplateParamDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDeleteParam(TbSysTemplateParam templateParam) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.deleteParam(result, templateParam);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
