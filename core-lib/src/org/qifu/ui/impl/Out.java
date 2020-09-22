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
package org.qifu.ui.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.qifu.base.model.YesNo;
import org.qifu.ui.UIComponent;
import org.qifu.ui.UIComponentValueUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Out implements UIComponent {
	private ServletRequestAttributes servletRequestAttributes = null;
	private String scope = "";
	private String value = "";
	private String escapeHtml = "";
	private String escapeJavaScript = "";	
	
	@Override
	public void setId(String id) {
		
	}
	
	@Override
	public String getId() {
		return "";
	}
	
	@Override
	public void setName(String name) {
		
	}
	
	@Override
	public String getName() {
		return "";
	}
	
	@Override
	public String getScript() throws Exception {
		return "";
	}
	
	@Override
	public String getHtml() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		UIComponentValueUtils.setValue( this.servletRequestAttributes, paramMap, "objVal", this.value, ( YesNo.YES.equals(escapeHtml) ? true : false ), ( YesNo.YES.equals(escapeJavaScript) ? true : false ), this.scope );
		Object objVal = paramMap.get("objVal");
		paramMap.clear();
		paramMap = null;
		return StringUtils.defaultString( String.valueOf( objVal ) );
	}
	
	@Override
	public void setServletRequestAttributes(ServletRequestAttributes servletRequestAttributes) {
		this.servletRequestAttributes = servletRequestAttributes;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getEscapeHtml() {
		return escapeHtml;
	}
	
	public void setEscapeHtml(String escapeHtml) {
		this.escapeHtml = escapeHtml;
	}
	
	public String getEscapeJavaScript() {
		return escapeJavaScript;
	}
	
	public void setEscapeJavaScript(String escapeJavaScript) {
		this.escapeJavaScript = escapeJavaScript;
	}

}
