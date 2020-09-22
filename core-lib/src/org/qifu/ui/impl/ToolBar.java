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

import org.qifu.ui.ComponentResourceUtils;
import org.qifu.ui.UIComponent;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ToolBar implements UIComponent {
	private static final String _HTML_RES = "META-INF/resource/toolbar/ui.toolbar.htm.ftl";
	private ServletRequestAttributes servletRequestAttributes = null;
	private String id="";
	private String createNewEnable="";
	private String saveEnabel="";
	private String refreshEnable="";
	private String cancelEnable="";
	private String exportEnable="";
	private String importEnable="";	
	private String createNewJsMethod="";
	private String saveJsMethod="";
	private String refreshJsMethod="";
	private String cancelJsMethod="";
	private String exportJsMethod="";
	private String importJsMethod="";	
	private String programName="";
	private String programId="";
	private String description="";
	private StringBuilder htmlOut=new StringBuilder();	
	
	private Map<String, Object> getParameters(String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", this.id);
		paramMap.put("createNewEnable", this.createNewEnable);		
		paramMap.put("saveEnabel", this.saveEnabel);
		paramMap.put("refreshEnable", this.refreshEnable);		
		paramMap.put("cancelEnable", this.cancelEnable);		
		paramMap.put("exportEnable", this.exportEnable);		
		paramMap.put("importEnable", this.importEnable);		
		paramMap.put("createNewJsMethod", this.createNewJsMethod);		
		paramMap.put("saveJsMethod", this.saveJsMethod);		
		paramMap.put("refreshJsMethod", this.refreshJsMethod);		
		paramMap.put("cancelJsMethod", this.cancelJsMethod);		
		paramMap.put("exportJsMethod", this.exportJsMethod);
		paramMap.put("importJsMethod", this.importJsMethod);
		paramMap.put("programName", this.programName);
		paramMap.put("programId", this.programId);
		paramMap.put("description", this.description);
		return paramMap;
	}
	
	private void generateHtml() {
		try {
			htmlOut.append( ComponentResourceUtils.generatorResource(ToolBar.class, IS_HTML, _HTML_RES, this.getParameters(IS_HTML)) );
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
	
	public String getCreateNewEnable() {
		return createNewEnable;
	}

	public void setCreateNewEnable(String createNewEnable) {
		this.createNewEnable = createNewEnable;
	}

	public String getSaveEnabel() {
		return saveEnabel;
	}

	public void setSaveEnabel(String saveEnabel) {
		this.saveEnabel = saveEnabel;
	}

	public String getRefreshEnable() {
		return refreshEnable;
	}

	public void setRefreshEnable(String refreshEnable) {
		this.refreshEnable = refreshEnable;
	}

	public String getCancelEnable() {
		return cancelEnable;
	}

	public void setCancelEnable(String cancelEnable) {
		this.cancelEnable = cancelEnable;
	}

	public String getExportEnable() {
		return exportEnable;
	}

	public void setExportEnable(String exportEnable) {
		this.exportEnable = exportEnable;
	}

	public String getImportEnable() {
		return importEnable;
	}

	public void setImportEnable(String importEnable) {
		this.importEnable = importEnable;
	}

	public String getCreateNewJsMethod() {
		return createNewJsMethod;
	}

	public void setCreateNewJsMethod(String createNewJsMethod) {
		this.createNewJsMethod = createNewJsMethod;
	}

	public String getSaveJsMethod() {
		return saveJsMethod;
	}

	public void setSaveJsMethod(String saveJsMethod) {
		this.saveJsMethod = saveJsMethod;
	}

	public String getRefreshJsMethod() {
		return refreshJsMethod;
	}

	public void setRefreshJsMethod(String refreshJsMethod) {
		this.refreshJsMethod = refreshJsMethod;
	}

	public String getCancelJsMethod() {
		return cancelJsMethod;
	}

	public void setCancelJsMethod(String cancelJsMethod) {
		this.cancelJsMethod = cancelJsMethod;
	}

	public String getExportJsMethod() {
		return exportJsMethod;
	}

	public void setExportJsMethod(String exportJsMethod) {
		this.exportJsMethod = exportJsMethod;
	}

	public String getImportJsMethod() {
		return importJsMethod;
	}

	public void setImportJsMethod(String importJsMethod) {
		this.importJsMethod = importJsMethod;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}			
	
}
