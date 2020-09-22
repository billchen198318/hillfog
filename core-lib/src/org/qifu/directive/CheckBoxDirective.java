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
import org.qifu.ui.impl.CheckBox;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class CheckBoxDirective implements TemplateDirectiveModel {
	private String id = "";
	private String name = "";
	private String nbspFirst = YesNo.YES;
	private String cssClass = "custom-control-input";
	private String checked = YesNo.NO;
	private String checkedTest = "";
	private String disabled = YesNo.NO;
	private String label = "";
	private String onchange = "";
	private String onclick = "";	

	private CheckBox handler() {
		CheckBox checkBox = new CheckBox();
		checkBox.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		checkBox.setId(this.id);
		checkBox.setName(this.name);
		checkBox.setNbspFirst(this.nbspFirst);
		checkBox.setCssClass(this.cssClass);
		checkBox.setChecked(this.checked);
		checkBox.setCheckedTest(this.checkedTest);
		checkBox.setDisabled(this.disabled);
		checkBox.setLabel(this.label);
		checkBox.setLabel(this.label);
		checkBox.setOnchange(this.onchange);
		checkBox.setOnclick(this.onclick);
		return checkBox;
	}	
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	CheckBox comp = this.handler();
    	PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;    	
    }
    
}
