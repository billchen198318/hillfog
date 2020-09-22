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
import org.qifu.ui.impl.TextBox;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TextBoxDirective implements TemplateDirectiveModel {
	private String id = "";
	private String name = "";
	private String value  = "";
	private String readonly = YesNo.NO;
	private String placeholder = "";
	private String label = "";
	private String cssClass = "form-control mb-2 mr-sm-2 mb-sm-0";
	private String requiredFlag = YesNo.NO;
	private String maxlength = "";
	private String escapeHtml = YesNo.YES;
	private String escapeJavaScript = YesNo.NO;	

	private TextBox handler() {
		TextBox textBox = new TextBox();
		textBox.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		textBox.setId(this.id);
		textBox.setName(this.name);
		textBox.setValue(this.value);
		textBox.setReadonly(this.readonly);
		textBox.setPlaceholder(this.placeholder);
		textBox.setLabel(this.label);
		textBox.setCssClass(this.cssClass);
		textBox.setRequiredFlag(this.requiredFlag);
		textBox.setMaxlength(this.maxlength);
		textBox.setEscapeHtml(this.escapeHtml);
		textBox.setEscapeJavaScript(this.escapeJavaScript);
		return textBox;
	}
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	TextBox comp = this.handler();
    	PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;    	
    }	
    
}
