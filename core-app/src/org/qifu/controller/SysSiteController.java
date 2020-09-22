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
import java.util.Map;

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
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysIcon;
import org.qifu.core.logic.IApplicationSystemLogicService;
import org.qifu.core.service.ISysIconService;
import org.qifu.core.service.ISysService;
import org.qifu.core.util.IconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysSiteController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysIconService<TbSysIcon, String> sysIconService;
	
	@Autowired
	IApplicationSystemLogicService applicationSystemLogicService;
	
	public SysSiteController() {
		super();
	}
	
	@Override
	public String viewPageNamespace() {
		return "sys_site";
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			Map<String, String> iconDataMap = IconUtils.getIconsSelectData();
			mm.put("iconDataMap", iconDataMap);
			String firstIconKey = "";
			for (Map.Entry<String, String> entry : iconDataMap.entrySet()) {
				if ("".equals(firstIconKey)) {
					firstIconKey = entry.getKey();
				}
			}
			mm.put("firstIconKey", firstIconKey);
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		TbSys sys = new TbSys();
		sys.setOid(oid);
		DefaultResult<TbSys> sysResult = this.sysService.selectByEntityPrimaryKey(sys);
		if (sysResult.getValue() == null) {
			throw new ControllerException(sysResult.getMessage());
		}
		sys = sysResult.getValue();
		mm.put("sys", sys);
		
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setIconId(sys.getIcon());
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.selectByUniqueKey(sysIcon);
		if (iconResult.getValue() == null) {
			throw new ControllerException( iconResult.getMessage() );
		}
		sysIcon = iconResult.getValue();
		mm.put("firstIconKey", sysIcon.getOid());
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001Q")
	@RequestMapping("/sysSitePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001A")
	@RequestMapping("/sysSiteCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001E")
	@RequestMapping("/sysSiteEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001Q")
	@RequestMapping(value = "/sysSiteQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<TbSys>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<TbSys>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<TbSys>> queryResult = this.sysService.findPage(
					this.queryParameter(searchValue).fullEquals("sysId").fullLink("name").value(),
					pageOf.orderBy("NAME").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSys sys) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> sysResult = this.applicationSystemLogicService.delete(sys);
		if (sysResult.getValue() != null) {
			result.setSuccess( YES );
		}
		result.setMessage( sysResult.getMessage() );		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001D")
	@RequestMapping(value = "/sysSiteDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSys sys) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sys);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	private void checkFields(DefaultControllerJsonResultObj<TbSys> result, TbSys sys) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("systemId", sys, "@org.apache.commons.lang3.StringUtils@isBlank(sysId)", "Id is blank!")
		.testField("systemId", sys, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09(sysId)", "Id only normal character!")
		.testField("systemId", ( PleaseSelect.noSelect(sys.getSysId()) ), "Please change Id value!") // Id 不能用  "all" 這個下拉值
		.testField("systemName", sys, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("systemHost", sys, "@org.apache.commons.lang3.StringUtils@isBlank(host)", "Host is blank!")
		.testField("systemContextPath", sys, "@org.apache.commons.lang3.StringUtils@isBlank(contextPath)", "Context path is blank!")
		.throwMessage();		
	}
	
	private void save(DefaultControllerJsonResultObj<TbSys> result, TbSys sys) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sys);
		DefaultResult<TbSys> sysResult = this.applicationSystemLogicService.create(sys, sys.getIcon());
		if ( sysResult.getValue() != null ) {
			result.setValue( sysResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( sysResult.getMessage() );		
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001A")
	@RequestMapping(value = "/sysSiteSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSys> doSave(TbSys sys) {
		DefaultControllerJsonResultObj<TbSys> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, sys);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void update(DefaultControllerJsonResultObj<TbSys> result, TbSys sys) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sys);
		DefaultResult<TbSys> sysResult = this.applicationSystemLogicService.update(sys, sys.getIcon());
		if ( sysResult.getValue() != null ) {
			result.setValue( sysResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( sysResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0001E")
	@RequestMapping(value = "/sysSiteUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSys> doUpdate(TbSys sys) {
		DefaultControllerJsonResultObj<TbSys> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, sys);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
