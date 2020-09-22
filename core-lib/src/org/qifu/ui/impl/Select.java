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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.ui.ComponentResourceUtils;
import org.qifu.ui.UIComponent;
import org.qifu.ui.UIComponentValueUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Select implements UIComponent {
	private static final String _HTML_RES = "META-INF/resource/select/ui.select.htm.ftl";
	private ServletRequestAttributes servletRequestAttributes = null;
	private String id = "";
	private String name = "";
	private String value  = "";
	private String label = "";
	private String cssClass = "";
	private String requiredFlag = "";
	private String dataSource = "";
	private String onchange = "";
	private String disabled = "";
	private StringBuilder htmlOut = new StringBuilder();
	private Map<String, String> dataMap = null;	
	
	@SuppressWarnings("unchecked")
	private void handlerDataSource() throws JsonParseException, JsonMappingException, IOException {
		if (this.dataMap!=null) {
			return;
		}
		this.dataMap = new LinkedHashMap<String, String>();
		if (StringUtils.isBlank(this.dataSource)) {
			return;
		}
		HttpServletRequest request = this.servletRequestAttributes.getRequest();
		Object dataSourceObj = ( request.getParameter(this.dataSource) != null ? request.getParameter(this.dataSource) : request.getAttribute(this.dataSource) );
		if (dataSourceObj == null) { // tag 傳過來的資料
			this.dataMap = (Map<String, String>)new ObjectMapper().readValue(this.dataSource, LinkedHashMap.class);
			return;
		}
		if (dataSourceObj instanceof java.lang.String) { // action 傳過來的資料
			this.dataMap = (Map<String, String>)new ObjectMapper().readValue((String)dataSourceObj, LinkedHashMap.class);
		}
		if (dataSourceObj instanceof Map) { // action 傳過來的資料
			this.dataMap = ((Map<String, String>)dataSourceObj);
		}
	}	
	
	private Map<String, Object> getParameters(String type) {
		try {
			this.handlerDataSource();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", this.id);
		params.put("name", this.name);
		params.put("dataSource", this.dataMap);
		params.put("label", this.label);
		params.put("cssClass", this.cssClass);
		params.put("requiredFlag", this.requiredFlag);
		params.put("onchange", this.onchange);
		params.put("disabled", this.disabled);
		UIComponentValueUtils.setValue(this.servletRequestAttributes, params, "value", this.value, false, false, SCOPE_REQUEST);	
		return params;
	}
	
	private void generateHtml() {
		try {
			htmlOut.append( ComponentResourceUtils.generatorResource(Select.class, IS_HTML, _HTML_RES, this.getParameters(IS_HTML)) );
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

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	
}
