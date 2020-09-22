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

import org.qifu.base.model.YesNo;
import org.qifu.ui.ComponentResourceUtils;
import org.qifu.ui.UIComponent;
import org.qifu.ui.UIComponentValueUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

public class TextBox implements UIComponent {
	private static final String _HTML_RES = "META-INF/resource/textbox/ui.textbox.htm.ftl";
	private ServletRequestAttributes servletRequestAttributes = null;
	private String id = "";
	private String name = "";
	private String value  = "";
	private String readonly = "";
	private String placeholder = "";
	private String label = "";
	private String cssClass = "";
	private String requiredFlag = "";
	private String maxlength = "";
	private String escapeHtml = "";
	private String escapeJavaScript = "";
	private StringBuilder htmlOut=new StringBuilder();	
	
	private Map<String, Object> getParameters(String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", this.id);
		paramMap.put("name", this.name);
		paramMap.put("readonly", this.readonly);
		paramMap.put("placeholder", this.placeholder);
		paramMap.put("label", this.label);
		paramMap.put("cssClass", this.cssClass);
		paramMap.put("requiredFlag", this.requiredFlag);
		paramMap.put("maxlength", this.maxlength);
		UIComponentValueUtils.setValue(this.servletRequestAttributes, paramMap, "value", this.value, ( YesNo.YES.equals(escapeHtml) ? true : false ), ( YesNo.YES.equals(escapeJavaScript) ? true : false ), SCOPE_REQUEST);
		return paramMap;
	}
	
	private void generateHtml() {
		try {
			htmlOut.append( ComponentResourceUtils.generatorResource(TextBox.class, IS_HTML, _HTML_RES, this.getParameters(IS_HTML)) );
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getScript() throws Exception {
		return "";
	}

	@Override
	public String getHtml() throws Exception {
		this.generateHtml();
		return this.htmlOut.toString();
	}

	@Override
	public void setServletRequestAttributes(ServletRequestAttributes servletRequestAttributes) {
		this.servletRequestAttributes = servletRequestAttributes;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(String requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public String getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
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
