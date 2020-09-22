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
package org.qifu.directive;

import java.io.IOException;
import java.util.Map;

import org.qifu.base.model.YesNo;
import org.qifu.ui.PageUiDirectiveUtils;
import org.qifu.ui.impl.TextArea;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TextAreaDirective implements TemplateDirectiveModel {
	private String id = "";
	private String name = "";
	private String value = "";
	private String readonly = YesNo.NO;
	private String label = "";
	private String cssClass = "form-control mb-2 mr-sm-2 mb-sm-0";
	private String requiredFlag = YesNo.NO;
	private String rows = "3";
	private String placeholder = "";
	private String escapeHtml = YesNo.YES;
	private String escapeJavaScript = YesNo.NO;	
	
	private TextArea handler() {
		TextArea textArea = new TextArea();
		textArea.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		textArea.setId(this.id);
		textArea.setName(this.name);
		textArea.setValue(this.value);
		textArea.setReadonly(this.readonly);
		textArea.setLabel(this.label);
		textArea.setCssClass(this.cssClass);
		textArea.setRequiredFlag(this.requiredFlag);
		textArea.setRows(this.rows);
		textArea.setPlaceholder(this.placeholder);
		textArea.setEscapeHtml(this.escapeHtml);
		textArea.setEscapeJavaScript(this.escapeJavaScript);
		return textArea;
	}	
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	TextArea comp = this.handler();
    	PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;    	
    }	
	
}
