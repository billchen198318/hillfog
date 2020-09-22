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
package org.qifu.core.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.Constants;
import org.qifu.base.CoreAppConstants;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.core.model.User;
import org.qifu.core.support.SysEventLogSupport;
import org.qifu.core.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class ControllerAuthorityCheckInterceptor implements HandlerInterceptor {
	protected Logger logger = LogManager.getLogger(ControllerAuthorityCheckInterceptor.class);
	
	@Autowired
	BaseInfoConfigProperties baseInfoConfigProperties;
	
	private void log(String userId, String sysId, String url, boolean permit) throws Exception {
		if (!YesNo.YES.equals(baseInfoConfigProperties.getEnableControllerAuthCheckLog())) {
			return;
		}
		if (this.isEventLogPage(url)) {
			return;
		}
		boolean log = true;
		String[] excludeLogUrls = CoreAppConstants.getWebConfiginterceptorExcludePathPatterns();
		for (int i = 0; log && excludeLogUrls != null && i < excludeLogUrls.length; i++) {
			if ( excludeLogUrls[i].indexOf(url) > -1 ) {
				log = false;
			}
		}
		if (!log) {
			logger.info("exclude log url: " + url);
			return;
		}
		SysEventLogSupport.log(userId, sysId, url, permit);
	}
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getServletPath();
		String qifuPageTab = request.getParameter(Constants.QIFU_PAGE_IN_TAB_IFRAME);
		User user = UserUtils.getCurrentUser();
		if (user == null) {
			if (YesNo.YES.equals(qifuPageTab)) {
				response.sendRedirect( CoreAppConstants.SYS_PAGE_TAB_LOGIN_AGAIN );
				return false;
			}		
			String header = request.getHeader("X-Requested-With");
			if ("XMLHttpRequest".equalsIgnoreCase(header)) {
				response.getWriter().print(Constants.NO_AUTHZ_JSON_DATA);
				response.getWriter().flush();
				response.getWriter().close();
				return false;
			}
			response.sendRedirect( CoreAppConstants.SYS_PAGE_LOGIN );
			return false;
		}
		if (UserUtils.isAdmin()) {
			this.log( user.getUsername(), baseInfoConfigProperties.getSystem(), url, true );
			return true;
		}
		if (!(handler instanceof HandlerMethod)) { // 非 HandlerMethod 不檢查
			return true;
		}
		Method method = ((HandlerMethod) handler).getMethod();
		Annotation[] actionMethodAnnotations = method.getAnnotations();
		if (this.isControllerAuthority(actionMethodAnnotations)) {
			this.log( user.getUsername(), baseInfoConfigProperties.getSystem(), url, true );
			return true;
		}		
		if (UserUtils.isPermitted(url) || UserUtils.isPermitted("/"+url)) {
			this.log( user.getUsername(), baseInfoConfigProperties.getSystem(), url, true );
			return true;
		}		
		logger.warn("[decline] user=" + user.getUsername() + " url=" + url);
		if (YesNo.YES.equals(qifuPageTab)) {
			this.log( user.getUsername(), baseInfoConfigProperties.getSystem(), url, false );
			response.sendRedirect( CoreAppConstants.SYS_PAGE_NO_AUTH );
			return false;
		}		
		String header = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equalsIgnoreCase(header)) {
			response.getWriter().print(Constants.NO_AUTHZ_JSON_DATA);
			response.getWriter().flush();
			response.getWriter().close();
			this.log( user.getUsername(), baseInfoConfigProperties.getSystem(), url, false );
			return false;
		}
		this.log( user.getUsername(), baseInfoConfigProperties.getSystem(), url, false );
		response.sendRedirect( CoreAppConstants.SYS_PAGE_NO_AUTH );
		return false;
	}
	
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	
    }
    
	private boolean isControllerAuthority(Annotation[] actionMethodAnnotations) {
		if (actionMethodAnnotations==null || actionMethodAnnotations.length == 0) { // 沒有 ControllerMethodAuthority 不需要check
			return true;
		}
		boolean foundControllerMethodAuthority = false;
		for (Annotation anno : actionMethodAnnotations) {
			if (anno instanceof ControllerMethodAuthority) {
				foundControllerMethodAuthority = true;
			}
		}
		if (!foundControllerMethodAuthority) { // 沒有 ControllerMethodAuthority 不需要check
			return true;
		}
		for (Annotation anno : actionMethodAnnotations) {
			if (anno instanceof ControllerMethodAuthority) {
				if (!((ControllerMethodAuthority)anno).check()) { // check=false , 表示不要檢查權限
					return true;
				}
				String progId = ((ControllerMethodAuthority)anno).programId();
				if (StringUtils.isBlank(progId)) {
					return false;	
				}
				if (UserUtils.isPermitted(progId)) {
					return true;
				}
			}
		}
		return false;
	}	    
	
	/*
	 * Event log 查詢的頁面
	 */
	private boolean isEventLogPage(String url) {
		if (StringUtils.defaultString(url).startsWith("/sysEventLog")) {
			return true;
		}
		return false;
	}
	
}
