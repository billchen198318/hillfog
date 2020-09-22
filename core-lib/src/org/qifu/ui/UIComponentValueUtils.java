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
package org.qifu.ui;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import ognl.Ognl;
import ognl.OgnlException;

public class UIComponentValueUtils {
	
	public static boolean foundIfResult(ServletRequestAttributes servletRequestAttributes) {
		if ( servletRequestAttributes.getAttribute(UIComponent.IfResultVariableName, RequestAttributes.SCOPE_REQUEST) != null ) {
			return true;
		}
		return false;
	}
	
	public static void removeIfResult(ServletRequestAttributes servletRequestAttributes) {
		servletRequestAttributes.removeAttribute(UIComponent.IfResultVariableName, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static void putIfResult(ServletRequestAttributes servletRequestAttributes, boolean result) {
		servletRequestAttributes.setAttribute(UIComponent.IfResultVariableName, result, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static boolean getIfResult(ServletRequestAttributes servletRequestAttributes) {
		if ( servletRequestAttributes.getAttribute( UIComponent.IfResultVariableName, RequestAttributes.SCOPE_REQUEST ) != null ) {
			return (Boolean) servletRequestAttributes.getAttribute( UIComponent.IfResultVariableName, RequestAttributes.SCOPE_REQUEST );
		}
		return false;
	}
	
	public static boolean foundHasRoleResult(ServletRequestAttributes servletRequestAttributes) {
		if ( servletRequestAttributes.getAttribute( UIComponent.HasRoleResultVariableName, RequestAttributes.SCOPE_REQUEST) != null ) {
			return true;
		}
		return false;
	}	
	
	public static void removeHasRoleResult(ServletRequestAttributes servletRequestAttributes) {
		servletRequestAttributes.removeAttribute(UIComponent.HasRoleResultVariableName, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static void putHasRoleResult(ServletRequestAttributes servletRequestAttributes, boolean result) {
		servletRequestAttributes.setAttribute(UIComponent.HasRoleResultVariableName, result, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static boolean getHasRoleResult(ServletRequestAttributes servletRequestAttributes) {
		if ( servletRequestAttributes.getAttribute( UIComponent.HasRoleResultVariableName, RequestAttributes.SCOPE_REQUEST ) != null ) {
			return (Boolean) servletRequestAttributes.getAttribute( UIComponent.HasRoleResultVariableName, RequestAttributes.SCOPE_REQUEST );
		}
		return false;
	}		
	
	public static boolean foundHasPermissionResult(ServletRequestAttributes servletRequestAttributes) {
		if ( servletRequestAttributes.getAttribute( UIComponent.HasPermissionResultVariableName, RequestAttributes.SCOPE_REQUEST) != null ) {
			return true;
		}
		return false;
	}	
	
	public static void removeHasPermissionResult(ServletRequestAttributes servletRequestAttributes) {
		servletRequestAttributes.removeAttribute(UIComponent.HasPermissionResultVariableName, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static void putHasPermissionResult(ServletRequestAttributes servletRequestAttributes, boolean result) {
		servletRequestAttributes.setAttribute(UIComponent.HasPermissionResultVariableName, result, RequestAttributes.SCOPE_REQUEST);
	}
	
	public static boolean getHasPermissionResult(ServletRequestAttributes servletRequestAttributes) {
		if ( servletRequestAttributes.getAttribute( UIComponent.HasPermissionResultVariableName, RequestAttributes.SCOPE_REQUEST ) != null ) {
			return (Boolean) servletRequestAttributes.getAttribute( UIComponent.HasPermissionResultVariableName, RequestAttributes.SCOPE_REQUEST );
		}
		return false;
	}			
	
	public static Object getObjectFromPageContextOrRequest(ServletRequestAttributes servletRequestAttributes, String paramName) {
		HttpServletRequest request = servletRequestAttributes.getRequest();
		/**
		 * 優先順序: request.getParameter > request.getAttribute
		 */
		return ( request.getParameter(paramName) != null ? request.getParameter(paramName) : request.getAttribute(paramName) );
	}
	
	public static Object getObjectFromSession(ServletRequestAttributes servletRequestAttributes, String paramName) {
		HttpServletRequest request = servletRequestAttributes.getRequest();
		return request.getSession().getAttribute(paramName);
	}
	
	public static Object getOgnlProcessObjectFromPageContextOrRequest(ServletRequestAttributes servletRequestAttributes, String expression) {
		Map<String, Object> ognlRoot = new HashMap<String, Object>();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		Enumeration<String> pNames = request.getParameterNames();
		Enumeration<String> aNames = request.getAttributeNames();
		/**
		 * ognlRoot 放入變數, 優先順序:  request.getParameter > request.getAttribute
		 */
		while (pNames.hasMoreElements()) {
			String key = pNames.nextElement();
			if (ognlRoot.get(key) == null) {
				ognlRoot.put(key, request.getParameter(key));
			}
		}
		while (aNames.hasMoreElements()) {
			String key = aNames.nextElement();
			if (ognlRoot.get(key) == null) {
				ognlRoot.put(key, request.getAttribute(key));
			}
		}
		if (ognlRoot.size() == 0) {
			ognlRoot = null;
			return null;
		}
		Object val = null;
		try {
			val = Ognl.getValue(expression, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), ognlRoot);
		} catch (OgnlException e) {
			//e.printStackTrace();
		}
		ognlRoot.clear();
		ognlRoot = null;
		return val;
	}
	
	public static Object getOgnlProcessObjectFromHttpSession(ServletRequestAttributes servletRequestAttributes, String expression) {
		Map<String, Object> ognlRoot = new HashMap<String, Object>();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		Enumeration<String> aNames = request.getSession().getAttributeNames();
		while (aNames.hasMoreElements()) {
			String key = aNames.nextElement();
			ognlRoot.put(key, request.getSession().getAttribute(key));
		}
		if (ognlRoot.size() == 0) {
			ognlRoot = null;
			return null;
		}
		Object val = null;
		try {
			val = Ognl.getValue(expression, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), ognlRoot);
		} catch (OgnlException e) {
			//e.printStackTrace();
		}
		ognlRoot.clear();
		ognlRoot = null;		
		return val;		
	}
	
	public static void setValue(ServletRequestAttributes servletRequestAttributes, Map<String, Object> paramMap, String paramMapKey, String value, boolean escapeHtml, boolean ecmaScript, String scope) {
		if (!StringUtils.isBlank(value)) {
			if ( UIComponent.SCOPE_SESSION.equals(scope) ) {
				Object valObj = null;
				if ( (valObj = getObjectFromSession(servletRequestAttributes, value)) != null ) {
					putValue(paramMap, paramMapKey, valObj, escapeHtml, ecmaScript);
					return;
				}
			} else {
				Object valObj = null;
				if ( (valObj = getObjectFromPageContextOrRequest(servletRequestAttributes, value)) != null ) {
					putValue(paramMap, paramMapKey, valObj, escapeHtml, ecmaScript);
					return;
				}
			}
		}		
		/**
		 * (暫時不需要)不要處理 "@" 與 "new" 的 Ognl expression 如: @java.lang.Runtime@getRuntime().exec("exec /usr/local/bin/firefox"), new java.lang...
		 * 處理 如: policy.no , policy.amount
		 */
		//if (!StringUtils.isBlank(value) && value.indexOf("@") == -1 && value.indexOf("new ") == -1 && value.indexOf(".") >= 1) { // 暫時不需要
		if (!StringUtils.isBlank(value) && !NumberUtils.isParsable(value)) { // TextBox, TextArea, Select, Out tag 會用到 , 2020-08-08 add number value no need
			Object val = null;
			if ( UIComponent.SCOPE_SESSION.equals(scope) ) {
				val = getOgnlProcessObjectFromHttpSession(servletRequestAttributes, value);
			} else {
				val = getOgnlProcessObjectFromPageContextOrRequest(servletRequestAttributes, value);
			}
			if (null != val) {
				putValue(paramMap, paramMapKey, val, escapeHtml, ecmaScript);
				return;
			}
		}
		if (paramMap.get(paramMapKey) == null) {
			paramMap.put(paramMapKey, StringUtils.defaultString(value));
		}
	}
	
	private static void putValue(Map<String, Object> params, String paramMapKey, Object val, boolean escapeHtml, boolean ecmaScript) {
		if (val == null) {
			return;
		}
		if (val instanceof java.lang.String) {
			params.put(paramMapKey, String.valueOf(val) );
			if (ecmaScript) {
				params.put(paramMapKey, StringEscapeUtils.escapeEcmaScript( (String)val ) );
			}
			if (escapeHtml) {
				params.put(paramMapKey, StringEscapeUtils.escapeHtml4( (String)val ) );
			}
			if (ecmaScript && escapeHtml) {
				params.put(paramMapKey, StringEscapeUtils.escapeHtml4( StringEscapeUtils.escapeEcmaScript( (String)val ) ) );
			}
			return;
		}		
		if (val instanceof java.lang.Integer) {
			params.put(paramMapKey, String.valueOf( (Integer)val ) );
			return;			
		}
		if (val instanceof java.lang.Long) {
			params.put(paramMapKey, String.valueOf( (Long)val ) );
			return;						
		}
		if (val instanceof java.math.BigDecimal) {
			params.put(paramMapKey, ((java.math.BigDecimal)val).toString() );
			return;					
		}
		if (val instanceof java.math.BigInteger) {
			params.put(paramMapKey, ((java.math.BigInteger)val).toString() );
			return;							
		}
		if (val instanceof java.lang.Float) {
			params.put(paramMapKey, String.valueOf( (Float)val ) );
			return;						
		}
		if (val instanceof java.lang.Double) {
			params.put(paramMapKey, String.valueOf( (Double)val ) );
			return;						
		}	
		params.put(paramMapKey, String.valueOf(val));
	}	
	
}
