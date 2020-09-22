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
package org.qifu.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import freemarker.core.Environment;

public class PageUiDirectiveUtils {
	
	public static void write(Environment env, Map paramMap, UIComponent comp) {
        if (paramMap != null) {
            try {
                BeanUtils.populate(comp, paramMap);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        try {        	
            env.getOut().write(comp.getHtml());
            env.getOut().write(comp.getScript());
        } catch (Exception e) {
            e.printStackTrace();
        }		
	}
	
}
