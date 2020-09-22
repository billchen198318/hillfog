/* 
 * Copyright 2012-2020 qifu of copyright Chen Xin Nien
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
package org.qifu.core.ui;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.core.util.UserUtils;
import org.qifu.ui.UIComponent;
import org.qifu.ui.UIComponentValueUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HasPermission implements UIComponent {
	private ServletRequestAttributes servletRequestAttributes = null;	
	private String check = "";
	
	@Override
	public void setId(String id) {
		
	}
	
	@Override
	public String getId() {
		return "";
	}
	
	@Override
	public void setName(String name) {
		
	}
	
	@Override
	public String getName() {
		return "";
	}
	
	@Override
	public String getScript() throws Exception {
		return "";
	}

	@Override
	public String getHtml() throws Exception {
		return "";
	}

	@Override
	public void setServletRequestAttributes(ServletRequestAttributes servletRequestAttributes) {
		this.servletRequestAttributes = servletRequestAttributes;
	}
	
	public void setCheck(String check) {
		this.check = check;
	}

	public Boolean getTestResult() {
		String permIdArr[] = StringUtils.defaultString( this.check ).split(Constants.DEFAULT_SPLIT_DELIMITER);
		boolean flag = false;
		for (int i = 0; permIdArr != null && i < permIdArr.length && !flag; i++) {
			flag = UserUtils.isPermitted( permIdArr[i].replaceAll(" ", "") );
		}
		UIComponentValueUtils.putHasPermissionResult(servletRequestAttributes, flag);
		return flag;
	}	
	
}
