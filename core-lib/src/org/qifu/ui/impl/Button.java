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
import org.springframework.web.context.request.ServletRequestAttributes;

public class Button implements UIComponent {
	private static final String _HTML_RES = "META-INF/resource/button/ui.button.htm.ftl";
	private static final String _JS_RES = "META-INF/resource/button/ui.button.js.ftl";	
	
	private ServletRequestAttributes servletRequestAttributes = null;
	private String id = "";
	private String xhrUrl = "";
	private String xhrParameter = "";
	private String formId = "";
	private String onclick = "";
	private String cssClass = "";
	private String label = "";
	private String errorFunction = "";
	private String loadFunction = "";
	private String disabled = "";
	private String xhrSendNoPleaseWait = "";
	private String selfPleaseWaitShow = "";
	private String bootboxConfirm = "";
	private String bootboxConfirmTitle = "";
	private StringBuilder htmlOut = new StringBuilder();
	private StringBuilder jsOut = new StringBuilder();	

	private Map<String, Object> getParameters(String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (IS_SCRIPT.equals(type)) {
			if (!StringUtils.isBlank(this.xhrParameter) && !StringUtils.isBlank(this.formId)) { // 以 xhrParameter 為主, 不能同時存在 xhrParameter 與 formId
				this.formId = "";
			}
			if (YesNo.YES.equals(this.bootboxConfirm) && StringUtils.isBlank(this.bootboxConfirmTitle)) {
				this.bootboxConfirmTitle = "undefined";
			}
			paramMap.put("xhrUrl", this.xhrUrl);
			paramMap.put("xhrParameter", StringUtils.defaultString(this.xhrParameter).trim());
			paramMap.put("formId", StringUtils.defaultString(this.formId).trim());
			paramMap.put("errorFunction", this.errorFunction);
			paramMap.put("loadFunction", this.loadFunction);
			paramMap.put("onclick", this.onclick.replaceAll("[(]", "").replaceAll("[)]", "").replaceAll(";", ""));
			paramMap.put("xhrSendNoPleaseWait", this.xhrSendNoPleaseWait);
			paramMap.put("selfPleaseWaitShow", this.selfPleaseWaitShow);
			paramMap.put("bootboxConfirm", this.bootboxConfirm);
			paramMap.put("bootboxConfirmTitle", this.bootboxConfirmTitle);
		} else {
			paramMap.put("id", this.id);
			paramMap.put("cssClass", this.cssClass);
			paramMap.put("label", this.label);
			paramMap.put("onclick", this.onclick);
			paramMap.put("disabled", this.disabled);
		}
		return paramMap;
	}
	
	private void generateHtml() {
		try {
			htmlOut.append( ComponentResourceUtils.generatorResource(Button.class, IS_HTML, _HTML_RES, this.getParameters(IS_HTML)) );
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
	
	private void generateJavascript() {
		try {
			jsOut.append( ComponentResourceUtils.generatorResource(Button.class, IS_SCRIPT, _JS_RES, this.getParameters(IS_SCRIPT)) );
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

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getErrorFunction() {
		return errorFunction;
	}

	public void setErrorFunction(String errorFunction) {
		this.errorFunction = errorFunction;
	}

	public String getLoadFunction() {
		return loadFunction;
	}

	public void setLoadFunction(String loadFunction) {
		this.loadFunction = loadFunction;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getXhrSendNoPleaseWait() {
		return xhrSendNoPleaseWait;
	}

	public void setXhrSendNoPleaseWait(String xhrSendNoPleaseWait) {
		this.xhrSendNoPleaseWait = xhrSendNoPleaseWait;
	}

	public String getSelfPleaseWaitShow() {
		return selfPleaseWaitShow;
	}

	public void setSelfPleaseWaitShow(String selfPleaseWaitShow) {
		this.selfPleaseWaitShow = selfPleaseWaitShow;
	}

	public String getBootboxConfirm() {
		return bootboxConfirm;
	}

	public void setBootboxConfirm(String bootboxConfirm) {
		this.bootboxConfirm = bootboxConfirm;
	}

	public String getBootboxConfirmTitle() {
		return bootboxConfirmTitle;
	}

	public void setBootboxConfirmTitle(String bootboxConfirmTitle) {
		this.bootboxConfirmTitle = bootboxConfirmTitle;
	}
	
}
