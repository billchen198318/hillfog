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
import org.qifu.ui.impl.Grid;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class GridDirective implements TemplateDirectiveModel {
	private String id = "";
	private String xhrUrl = "";
	private String xhrParameter = "";
	private String gridFieldStructure = "";
	private String queryFunction = "";
	private String clearFunction = "";
	private String selfPleaseWaitShow = YesNo.NO;
	
	private Grid handler() {
		Grid grid = new Grid();
		grid.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		grid.setId(this.id);
		grid.setXhrUrl(this.xhrUrl);
		grid.setXhrParameter(this.xhrParameter);
		grid.setGridFieldStructure(this.gridFieldStructure);
		grid.setQueryFunction(this.queryFunction);
		grid.setClearFunction(this.clearFunction);
		grid.setSelfPleaseWaitShow(this.selfPleaseWaitShow);
		return grid;
	}
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	Grid comp = this.handler();
    	PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;    	
    }
    
}
