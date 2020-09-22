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
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.core.entity.TbSysEventLog;
import org.qifu.core.service.ISysEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysEventLogViewAction extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysEventLogService<TbSysEventLog, String> sysEventLogService;
	
	@Override
	public String viewPageNamespace() {
		return "sys_log";
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG004D0001Q")
	@RequestMapping(value = "/sysEventLogPage")	
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPageWithNamespace("event-log-page");
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG004D0001Q")
	@RequestMapping(value = "/sysEventLogQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysEventLog> > queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysEventLog> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysEventLog> > queryResult = this.sysEventLogService.findPage(
					this.queryParameter(searchValue).fullEquals("user").fullEquals("sysId").value(), 
					pageOf.orderBy("CDATE").sortTypeDesc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysEventLog sysEventLog) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.sysEventLogService.delete(sysEventLog);
		if ( dResult.getValue() != null && dResult.getValue() ) {
			result.setValue( dResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );			
	}
	
	private void deleteAll(DefaultControllerJsonResultObj<Boolean> result) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.sysEventLogService.deleteAll();
		result.setSuccess( YES );
		result.setValue( true );
		result.setMessage( BaseSystemMessage.deleteSuccess() );
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG004D0001D")
	@RequestMapping(value = "/sysEventLogDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysEventLog sysEventLog) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysEventLog);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG004D0001D")
	@RequestMapping(value = "/sysEventLogDeleteAllJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDeleteAll() {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.deleteAll(result);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
}
