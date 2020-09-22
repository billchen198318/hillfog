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
package org.qifu.core.directive;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.qifu.core.ui.HasRole;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class HasRoleDirective implements TemplateDirectiveModel {
	private String check = "";
	
	private HasRole handler() {
		HasRole hasRoleTest = new HasRole();
		hasRoleTest.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		hasRoleTest.setCheck( this.check );
		return hasRoleTest;
	}	
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	HasRole hasRole = this.handler();
        if (paramMap != null) {
            try {
                BeanUtils.populate(hasRole, paramMap);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }    	
    	if (hasRole.getTestResult() && body != null) {
    		body.render(env.getOut());
    	}
    	hasRole = null;
    }
    
}
