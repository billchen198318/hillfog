/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.core.util.SystemSettingConfigureUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysSettingController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Override
	public String viewPageNamespace() {
		return "sys_conf";
	}		
	
	private void init(String type, ModelMap mm) throws ServiceException, ControllerException, Exception {
		mm.put("mailFrom", SystemSettingConfigureUtils.getMailDefaultFromValue().trim());
		mm.put("mailEnable", SystemSettingConfigureUtils.getMailEnableValue().trim());
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0007Q")
	@RequestMapping(value = "/sysSettingPage")	
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
	
	private void update(DefaultControllerJsonResultObj<Boolean> result, HttpServletRequest request) throws AuthorityException, ControllerException, ServiceException, Exception {
		String mailFrom = request.getParameter("mailFrom");
		String mailEnable = request.getParameter("mailEnable");
		if (StringUtils.isBlank(mailFrom)) {
			mailFrom = SystemSettingConfigureUtils.getMailDefaultFromValue().trim();
		}
		SystemSettingConfigureUtils.updateMailDefaultFromValue(mailFrom);
		SystemSettingConfigureUtils.updateMailEnableValue( (YES.equals(mailEnable) ? YES : NO) );
		result.setSuccess( YES );
		result.setValue( true );
		result.setMessage( BaseSystemMessage.updateSuccess() );				
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0007Q")
	@RequestMapping(value = "/sysSettingUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doUpdate(HttpServletRequest request) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
