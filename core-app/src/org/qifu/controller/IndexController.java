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

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.exception.ServiceException;
import org.qifu.core.model.MenuResult;
import org.qifu.core.util.IconUtils;
import org.qifu.core.util.MenuSupportUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseControllerSupport {
	
	// for session timeout sendRedirect url
	@RequestMapping({"/loginAgainPage"})
	public String loginAgain(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPage( "login_again" );
		this.getDefaultModelMap(mm);
		return viewName;
	}
	
	// for ControllerAuthorityCheckInterceptor sendRedirect url
	@RequestMapping({"/noAuthPage"})
	public String noAuth(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPage( "auth1" );
		this.getDefaultModelMap(mm);
		this.setPageMessage(mm, "no permission!");
		return viewName;
	}	
	
	@RequestMapping({"/index"}) // "/", "/index"
	public String index(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPage( "index" );
		this.getDefaultModelMap(mm);
		MenuResult menuResult = null;
		String iconJavascriptData = "";
		String firstLoadJavascript = "";
		try {
			menuResult = MenuSupportUtils.getMenuData( this.getBasePath(request) );
			firstLoadJavascript = MenuSupportUtils.getFirstLoadJavascript();
			iconJavascriptData = IconUtils.getJsData();
		} catch (ServiceException se) {
			viewName = this.getServiceOrControllerExceptionPage(se, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		if (menuResult == null) {
			menuResult = new MenuResult();
		}
		mm.put("menuResult", menuResult);
		mm.put("firstLoadJavascript", firstLoadJavascript);
		mm.put("iconJavascriptData", iconJavascriptData);
		return viewName;
	}
	
}
