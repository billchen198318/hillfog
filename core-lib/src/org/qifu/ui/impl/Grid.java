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

import org.apache.commons.lang3.StringUtils;
import org.qifu.ui.ComponentResourceUtils;
import org.qifu.ui.UIComponent;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Grid implements UIComponent {
	private static final String _HTML_RES = "META-INF/resource/grid/ui.grid.htm.ftl";
	private static final String _JS_RES = "META-INF/resource/grid/ui.grid.js.ftl";
	private ServletRequestAttributes servletRequestAttributes = null;
	private String id="";
	private String xhrUrl = "";
	private String xhrParameter = "";
	private String gridFieldStructure = "";
	private String queryFunction = "";
	private String clearFunction = "";
	private String selfPleaseWaitShow = "";
	private StringBuilder htmlOut=new StringBuilder();
	private StringBuilder jsOut=new StringBuilder();
	
	private Map<String, Object> getParameters(String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", this.id);
		if (IS_SCRIPT.equals(type)) {
			
			this.queryFunction = StringUtils.defaultString(this.queryFunction).replaceAll("[(]", "").replaceAll("[)]", "").replaceAll(";", "");
			this.clearFunction = StringUtils.defaultString(this.clearFunction).replaceAll("[(]", "").replaceAll("[)]", "").replaceAll(";", "");
			
			paramMap.put("xhrUrl", this.xhrUrl);
			paramMap.put("xhrParameter", this.xhrParameter);
			paramMap.put("gridFieldStructure", this.gridFieldStructure);
			paramMap.put("queryFunction", this.queryFunction);
			paramMap.put("clearFunction", this.clearFunction);
			paramMap.put("selfPleaseWaitShow", this.selfPleaseWaitShow);
		}
		return paramMap;
	}
	
	private void generateHtml() {
		try {
			htmlOut.append( ComponentResourceUtils.generatorResource(Grid.class, IS_HTML, _HTML_RES, this.getParameters(IS_HTML)) );
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
	
	private void generateJavascript() {
		try {
			jsOut.append( ComponentResourceUtils.generatorResource(Grid.class, IS_SCRIPT, _JS_RES, this.getParameters(IS_SCRIPT)) );
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
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getScript() throws Exception {
		this.generateJavascript();
		return this.jsOut.toString();
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
	
	public String getXhrUrl() {
		return xhrUrl;
	}

	public void setXhrUrl(String xhrUrl) {
		this.xhrUrl = xhrUrl;
	}

	public String getXhrParameter() {
		return xhrParameter;
	}

	public void setXhrParameter(String xhrParameter) {
		this.xhrParameter = xhrParameter;
	}

	public String getGridFieldStructure() {
		return gridFieldStructure;
	}

	public void setGridFieldStructure(String gridFieldStructure) {
		this.gridFieldStructure = gridFieldStructure;
	}

	public String getQueryFunction() {
		return queryFunction;
	}

	public void setQueryFunction(String queryFunction) {
		this.queryFunction = queryFunction;
	}

	public String getClearFunction() {
		return clearFunction;
	}

	public void setClearFunction(String clearFunction) {
		this.clearFunction = clearFunction;
	}

	public String getSelfPleaseWaitShow() {
		return selfPleaseWaitShow;
	}

	public void setSelfPleaseWaitShow(String selfPleaseWaitShow) {
		this.selfPleaseWaitShow = selfPleaseWaitShow;
	}

}
