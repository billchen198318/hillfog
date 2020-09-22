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
import org.qifu.base.model.YesNo;
import org.qifu.ui.ComponentResourceUtils;
import org.qifu.ui.UIComponent;
import org.qifu.ui.UIComponentValueUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CheckBox implements UIComponent {
	private static final String _HTML_RES = "META-INF/resource/checkbox/ui.checkbox.htm.ftl";
	
	private ServletRequestAttributes servletRequestAttributes = null;
	private String id = "";
	private String name = "";
	private String nbspFirst = "";
	private String cssClass = "";
	private String checked = "";
	private String checkedTest = "";
	private String disabled = "";
	private String label = "";
	private String onchange = "";
	private String onclick = "";
	private StringBuilder htmlOut = new StringBuilder();
	
	private Map<String, Object> getParameters() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", this.id);
		paramMap.put("name", this.name);
		paramMap.put("nbspFirst", this.nbspFirst);
		paramMap.put("cssClass", this.cssClass);
		paramMap.put("disabled", this.disabled);
		paramMap.put("label", this.label);
		paramMap.put("onchange", this.onchange);
		paramMap.put("onclick", this.onclick);
		if (!StringUtils.isBlank(this.checkedTest)) {
			Object objVal = UIComponentValueUtils.getOgnlProcessObjectFromPageContextOrRequest(this.servletRequestAttributes, this.checkedTest);
			if (objVal instanceof Boolean && (boolean)objVal) {
				this.checked = YesNo.YES;
			}
		}
		paramMap.put("checked", this.checked);
		return paramMap;
	}
	
	private void generateHtml() {
		try {
			htmlOut.append( ComponentResourceUtils.generatorResource(CheckBox.class, IS_HTML, _HTML_RES, this.getParameters()) );
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
		return htmlOut.toString();
	}
	
	@Override
	public void setServletRequestAttributes(ServletRequestAttributes servletRequestAttributes) {
		this.servletRequestAttributes = servletRequestAttributes;
	}
	
	public String getNbspFirst() {
		return nbspFirst;
	}

	public void setNbspFirst(String nbspFirst) {
		this.nbspFirst = nbspFirst;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getCheckedTest() {
		return checkedTest;
	}

	public void setCheckedTest(String checkedTest) {
		this.checkedTest = checkedTest;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

}
