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
import org.qifu.core.ui.HasPermission;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class HasPermissionDirective implements TemplateDirectiveModel {
	private String check = "";
	
	private HasPermission handler() {
		HasPermission hasPermissionTest = new HasPermission();
		hasPermissionTest.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		hasPermissionTest.setCheck( this.check );
		return hasPermissionTest;
	}	
	
    @Override
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	HasPermission hasPermission = this.handler();
        if (paramMap != null) {
            try {
                BeanUtils.populate(hasPermission, paramMap);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }    	
    	if (hasPermission.getTestResult() && body != null) {
    		body.render(env.getOut());
    	}
    	hasPermission = null;
    }
    
}
