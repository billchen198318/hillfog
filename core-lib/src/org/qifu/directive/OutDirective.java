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
import org.qifu.ui.UIComponent;
import org.qifu.ui.impl.Out;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class OutDirective implements TemplateDirectiveModel {
	private String scope = UIComponent.SCOPE_REQUEST;
	private String value = "";
	private String escapeHtml = YesNo.NO;
	private String escapeJavaScript = YesNo.NO;	
	
	private Out handler() {
		Out out = new Out();
		out.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		out.setScope(this.scope);
		out.setValue(this.value);
		out.setEscapeHtml(this.escapeHtml);
		out.setEscapeJavaScript(this.escapeJavaScript);
		return out;
	}
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	Out comp = this.handler();
    	PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;    	
    }	
	
}
