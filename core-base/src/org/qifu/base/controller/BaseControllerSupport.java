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
package org.qifu.base.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.BaseSysException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryParamBuilder;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchBody;
import org.qifu.base.model.SearchValue;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.base.properties.PageVariableConfigProperties;
import org.qifu.core.model.User;
import org.qifu.core.util.ApplicationSiteUtils;
import org.qifu.core.util.MenuSupportUtils;
import org.qifu.core.util.UserUtils;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ognl.Ognl;
import ognl.OgnlException;

public abstract class BaseControllerSupport {
	protected static final String VIEW_PAGE_PARENT_FOLDER = "view";
	protected static final String PAGE_SYS_LOGIN = VIEW_PAGE_PARENT_FOLDER + "/login";
	protected static final String PAGE_SYS_SEARCH_NO_DATA = VIEW_PAGE_PARENT_FOLDER + "/search_no_data";
	protected static final String PAGE_SYS_LOGIN_AGAIN = VIEW_PAGE_PARENT_FOLDER + "/login_again";
	protected static final String PAGE_SYS_NO_AUTH = VIEW_PAGE_PARENT_FOLDER + "/auth1";
	protected static final String PAGE_SYS_ERROR = VIEW_PAGE_PARENT_FOLDER + "/error";
	protected static final String PAGE_SYS_WARNING = VIEW_PAGE_PARENT_FOLDER + "/warning";
	
	protected static final String REDIRECT_INDEX = "index";
	
	protected static final String YES = YesNo.YES;
	protected static final String NO = YesNo.NO;
	protected static final String EXCEPTION = ControllerException.PAGE_EXCEPTION_CODE;
	
	protected static final String CONTENT_MAIN_PAGE = "main-page";
	protected static final String CONTENT_CREATE_PAGE = "create-page";
	protected static final String CONTENT_EDIT_PAGE = "edit-page";
	
	@Autowired
	BaseInfoConfigProperties baseInfoConfigProperties;
	
	@Autowired
	PageVariableConfigProperties pageVariableConfigProperties;
	
	public String getPageRedirect(String url) {
		return "redirect:/" + url;
	}	
	
	public String viewPage(String page) {
		return VIEW_PAGE_PARENT_FOLDER + "/" + page;
	}
	
	public String viewPageWithNamespace(String page) {
		if ( this instanceof IPageNamespaceProvide ) {
			return VIEW_PAGE_PARENT_FOLDER + "/" + ((IPageNamespaceProvide)this).viewPageNamespace() + "/" + page;
		}
		return VIEW_PAGE_PARENT_FOLDER + "/" + page;
	}	
	
	public String viewCreatePage(String packageName) {
		if (StringUtils.isBlank(packageName)) {
			return this.viewPage( CONTENT_CREATE_PAGE );
		}
		return this.viewPage( packageName + "/" + CONTENT_CREATE_PAGE );
	}
	
	public String viewEditPage(String packageName) {
		if (StringUtils.isBlank(packageName)) {
			return this.viewPage( CONTENT_EDIT_PAGE );
		}
		return this.viewPage( packageName + "/" + CONTENT_EDIT_PAGE );
	}
	
	public String viewMainPage(String packageName) {
		if (StringUtils.isBlank(packageName)) {
			return this.viewPage( CONTENT_MAIN_PAGE );
		}
		return this.viewPage( packageName + "/" + CONTENT_MAIN_PAGE );
	}		
	
	public String viewCreatePage() {
		if ( this instanceof IPageNamespaceProvide ) {
			return this.viewCreatePage( ((IPageNamespaceProvide)this).viewPageNamespace() );
		}
		return this.viewPage( CONTENT_CREATE_PAGE );
	}
	
	public String viewEditPage() {
		if ( this instanceof IPageNamespaceProvide ) {
			return this.viewEditPage( ((IPageNamespaceProvide)this).viewPageNamespace() );
		}
		return this.viewPage( CONTENT_EDIT_PAGE );		
	}
	
	public String viewMainPage() {
		if ( this instanceof IPageNamespaceProvide ) {
			return this.viewMainPage( ((IPageNamespaceProvide)this).viewPageNamespace() );
		}
		return this.viewPage( CONTENT_MAIN_PAGE );			
	}
	
	public ModelMap getDefaultModelMap(ModelMap mm) {
		User user = UserUtils.getCurrentUser();
		if (user != null) {
			mm.put("qifu_user", user);
		}
	    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = servletRequestAttributes.getRequest();
		String basePath = this.getBasePath(request);
		mm.addAttribute("qifu_basePath", basePath);
		mm.addAttribute("qifu_errorContact", baseInfoConfigProperties.getErrorContact());
		mm.addAttribute("qifu_verMsg", baseInfoConfigProperties.getVerMsg());
		mm.addAttribute("qifu_jsVerBuild", baseInfoConfigProperties.getJsVerBuild());
		mm.addAttribute("qifu_loginCaptchaCodeEnable", baseInfoConfigProperties.getLoginCaptchaCodeEnable());
		mm.addAttribute("qifu_mainSystem", baseInfoConfigProperties.getMainSystem());
		mm.addAttribute("qifu_system", baseInfoConfigProperties.getSystem());
		mm.addAttribute("jqXhrType", this.getJqXhrType());
		mm.addAttribute("jqXhrTimeout", this.getJqXhrTimeout());
		mm.addAttribute("jqXhrCache", this.getJqXhrCache());
		mm.addAttribute("jqXhrAsync", this.getJqXhrAsync());
		mm.addAttribute("qifu_maxUploadSize", this.getMaxUploadSize());
		int mbSize = NumberUtils.toInt(this.getMaxUploadSize(), 1048576)/1048576;
		if (mbSize < 1) {
			mbSize = 1;
		}
		mm.addAttribute("qifu_maxUploadSizeMb", mbSize+"");
		mm.addAttribute("qifu_mainBasePath", basePath);
		if (!baseInfoConfigProperties.getSystem().equals(baseInfoConfigProperties.getMainSystem())) {
			mm.addAttribute("qifu_mainBasePath", ApplicationSiteUtils.getBasePath(baseInfoConfigProperties.getMainSystem(), request));
		}
		return mm;
	}
	
	public ModelMap getDefaultModelMap(ModelMap mm, String progId) {
		this.getDefaultModelMap(mm);
		if (StringUtils.isBlank(progId)) {
			return mm;
		}
		mm.addAttribute("programId", progId);
		mm.addAttribute("programName", MenuSupportUtils.getProgramName(progId));
		return mm;
	}	
	
	public ModelMap getDefaultModelMap(ModelMap mm, ControllerMethodAuthority currentAnno) {
		if (null == currentAnno) {
			return this.getDefaultModelMap(mm, "");
		}
		return this.getDefaultModelMap(mm, currentAnno.programId());
	}
	
	public String getErrorContact() {
		return String.valueOf( baseInfoConfigProperties.getErrorContact() );
	}
	
	public String getVerMsg() {
		return String.valueOf( baseInfoConfigProperties.getVerMsg() );
	}
	
	public String getJsVerBuild() {
		return String.valueOf( baseInfoConfigProperties.getJsVerBuild() );
	}
	
	public String getLoginCaptchaCodeEnable() {
		return baseInfoConfigProperties.getLoginCaptchaCodeEnable();
	}
	
	public String getSystem() {
		return baseInfoConfigProperties.getSystem();
	}
	
	public String getMainSystem() {
		return baseInfoConfigProperties.getMainSystem();
	}
	
	public String getJqXhrType() {
		return pageVariableConfigProperties.getJqXhrType();
	}

	public String getJqXhrTimeout() {
		return pageVariableConfigProperties.getJqXhrTimeout();
	}

	public String getJqXhrCache() {
		return pageVariableConfigProperties.getJqXhrCache();
	}

	public String getJqXhrAsync() {
		return pageVariableConfigProperties.getJqXhrAsync();
	}

	public String getMaxUploadSize() {
		return pageVariableConfigProperties.getMaxUploadSize();
	}	
	
	public String generateOid() {
		return SimpleUtils.getUUIDStr();
	}	
	
	public String getBasePath(HttpServletRequest request) {
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	}
	
	protected String defaultString(String str) {
		return StringUtils.defaultString(str);
	}
	
	/*
	protected void setPageErrorContact(HttpServletRequest request) {
		request.setAttribute("errorContact", this.getErrorContact());
	}
	*/
	
	protected void setPageMessage(ModelMap mm, String pageMessage) {
		if (null!=pageMessage && pageMessage.length()>=Constants.MAX_SYS_DESCRIPTION_LENGTH) {
			pageMessage=pageMessage.substring(0, Constants.MAX_SYS_DESCRIPTION_LENGTH);
		}
		mm.addAttribute(Constants.PAGE_MESSAGE, pageMessage);
	}
	
//	protected void setPageErrorContact(ModelMap mm) {
//		mv.addObject("errorContact", this.getErrorContact());
//	}	
	
	protected String getNowDate() {
		return SimpleUtils.getStrYMD("/");
	}
	
	protected String getNowDate2() {
		return SimpleUtils.getStrYMD("-");
	}		
	
	protected ControllerMethodAuthority currentMethodAuthority() {
		ControllerMethodAuthority currAnnotation = null;
		String currThreadMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		Method method = null;
		for(Method m : this.getClass().getMethods()) {
			if(m.getName().equals(currThreadMethodName)) {
                method = m;
            }
        }
		if (method != null) {
			Annotation actionMethodAnnotations[] = method.getAnnotations();
			for (int i = 0; actionMethodAnnotations != null && null == currAnnotation && i < actionMethodAnnotations.length; i++) {
				if (actionMethodAnnotations[i] instanceof ControllerMethodAuthority) {
					currAnnotation = (ControllerMethodAuthority)actionMethodAnnotations[i];
				}
			}
		}
		return currAnnotation;
	}
	
	protected <T> DefaultControllerJsonResultObj<T> getDefaultJsonResult(String progId, boolean checkPermitted) {
		DefaultControllerJsonResultObj<T> result = DefaultControllerJsonResultObj.build();
		this.setResultDefaultValue(result, progId, checkPermitted);
		return result;
	}
	
	protected <T> QueryControllerJsonResultObj<T> getQueryJsonResult(String progId, boolean checkPermitted) {
		QueryControllerJsonResultObj<T> result = QueryControllerJsonResultObj.build();
		this.setResultDefaultValue(result, progId, checkPermitted);
		return result;
	}		
	
	protected <T> DefaultControllerJsonResultObj<T> getDefaultJsonResult(ControllerMethodAuthority currentAnno) {
		DefaultControllerJsonResultObj<T> result = DefaultControllerJsonResultObj.build();
		if (null != currentAnno) {
			this.setResultDefaultValue(result, currentAnno.programId(), currentAnno.check());
		} else {
			this.setResultDefaultValue(result, "", false);
		}
		return result;
	}
	
	protected <T> QueryControllerJsonResultObj<T> getQueryJsonResult(ControllerMethodAuthority currentAnno) {
		QueryControllerJsonResultObj<T> result = QueryControllerJsonResultObj.build();
		if (null != currentAnno) {
			this.setResultDefaultValue(result, currentAnno.programId(), currentAnno.check());
		} else {
			this.setResultDefaultValue(result, "", false);
		}
		return result;
	}	
	
	private void setResultDefaultValue(DefaultControllerJsonResultObj<?> result, String progId, boolean checkPermitted) {
		User user = UserUtils.getCurrentUser();
		if (user == null) {
			result.setMessage( BaseSystemMessage.noLoginAccessDenied() );
			return;
		}				
		result.setLogin( YES );
		if (UserUtils.isAdmin()) {
			result.setIsAuthorize( YES );
		}
		if (!checkPermitted) {
			result.setIsAuthorize( YES );
		} else {
			if (UserUtils.isPermitted(progId)) {
				result.setIsAuthorize( YES );
			}
		}
		if (!YES.equals(result.getIsAuthorize())) {
			result.setMessage( BaseSystemMessage.noPermission() );
		}	
	}
	
	protected boolean isAuthorizeAndLoginFromControllerJsonResult(DefaultControllerJsonResultObj<?> result) {
		if (YES.equals(result.getIsAuthorize()) && YES.equals(result.getLogin())) {
			return true;
		}
		return false;
	}
	
	protected void baseExceptionResult(DefaultControllerJsonResultObj<?> result, BaseSysException e) {
		if (e == null || e.getMessage() == null) {
			result.setMessage( BaseSystemMessage.objectNull() );
		} else {
			result.setMessage( e.getMessage().toString() );
		}
		result.setSuccess( NO );		
	}
	
	protected void exceptionResult(DefaultControllerJsonResultObj<?> result, Exception e) {
		e.printStackTrace();
		if (e == null || e.getMessage() == null) {
			result.setMessage( BaseSystemMessage.objectNull() );
		} else {
			result.setMessage( e.getMessage().toString() );
		}
		result.setSuccess( EXCEPTION );
	}
	
	protected String getExceptionMessage(Exception e) {
		String str = "";
		if (e != null && e.getMessage() != null) {
			str = e.getMessage();
		}
		if (e != null && e.getMessage() == null) {
			str = e.toString();
		}
		if (e == null) {
			str = "null undefined";
		}
		return str;
	}
	
	protected String getAuthorityExceptionPage(BaseSysException e, ModelMap mm) {
		this.setPageMessage(mm, this.getExceptionMessage(e));
		return PAGE_SYS_NO_AUTH;
	}	
	
	protected String getServiceOrControllerExceptionPage(BaseSysException e, ModelMap mm) {
		this.setPageMessage(mm, this.getExceptionMessage(e));
		return PAGE_SYS_SEARCH_NO_DATA;
	}	
	
	protected String getWarningPage(String message, ModelMap mm) {
		this.setPageMessage(mm, message);
		return PAGE_SYS_WARNING;
	}	
	
	protected String getExceptionPage(Exception e, ModelMap mm) {
		e.printStackTrace();
		this.setPageMessage(mm, this.getExceptionMessage(e));
		return PAGE_SYS_ERROR;
	}
	
	protected void fillObjectFromRequest(HttpServletRequest request, Object root) {
		Enumeration<String> pNames = request.getParameterNames();
		while (pNames.hasMoreElements()) {
			String key = pNames.nextElement();
			Object value = request.getParameter(key);
			try {
				Ognl.setValue(key, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), root, value);
			} catch (OgnlException e) {
				//e.printStackTrace();
			}
		}
	}
	
	protected PageOf getPageOf(HttpServletRequest request) {
		PageOf pageOf = new PageOf();
		fillObjectFromRequest(request, pageOf);
		return pageOf;
	}
	
	protected SearchValue getSearchValue(HttpServletRequest request) {
		SearchValue searchValue = new SearchValue();
		fillObjectFromRequest(request, searchValue);
		return searchValue;
	}	
	
	protected <T> void setQueryGridJsonResult(QueryControllerJsonResultObj<T> jsonResult, QueryResult<T> queryResult, PageOf pageOf) {
		if (queryResult.getValue() != null) {
			jsonResult.setValue( queryResult.getValue() );
			jsonResult.setPageOfCountSize( NumberUtils.toInt(pageOf.getCountSize(), 0) ); // queryResult.getRowCount()
			jsonResult.setPageOfSelect( NumberUtils.toInt(pageOf.getSelect(), 1) );
			jsonResult.setPageOfShowRow( NumberUtils.toInt(pageOf.getShowRow(), PageOf.Rows[0]) );
			jsonResult.setPageOfSize( NumberUtils.toInt(pageOf.getSize(), 1) );
			jsonResult.setSuccess(YesNo.YES);
		} else {
			jsonResult.setMessage( queryResult.getMessage() );
		}		
	}
	
	protected <T> CheckControllerFieldHandler<T> getCheckControllerFieldHandler(DefaultControllerJsonResultObj<T> result) {
		return CheckControllerFieldHandler.build(result);
	}
	
	protected List<String> transformAppendKeyStringToList(String appendOid) {
		List<String> list = new LinkedList<String>();
		if (StringUtils.isBlank(appendOid)) {
			return list;
		}
		String tmp[] = appendOid.split(Constants.ID_DELIMITER);
		for (int i=0; tmp != null && i < tmp.length; i++) {
			if (StringUtils.isBlank(tmp[i])) {
				continue;
			}
			if (list.contains(tmp[i])) {
				continue;
			}
			list.add(tmp[i]);
		}
		return list;
	}	
	
	protected QueryParamBuilder queryParameter() {
		return QueryParamBuilder.build();
	}
	
	protected QueryParamBuilder queryParameter(SearchBody searchBody) {
		return QueryParamBuilder.build(searchBody);
	}		
	
	protected QueryParamBuilder queryParameter(SearchValue searchValue) {
		SearchBody searchBody = new SearchBody(searchValue.getParameter());
		return QueryParamBuilder.build(searchBody);
	}
	
	protected QueryParamBuilder queryParameter(SearchValue searchValue, PageOf pageOf) {
		SearchBody searchBody = new SearchBody(pageOf, searchValue.getParameter());
		return QueryParamBuilder.build(searchBody);
	}	
	
	protected String getAccountId() {
		User user = UserUtils.getCurrentUser();
		if (user == null) {
			return null;
		}
		return user.getUsername();
	}
	
}
