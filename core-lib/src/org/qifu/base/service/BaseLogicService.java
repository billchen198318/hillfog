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
package org.qifu.base.service;

import java.util.Map;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.util.UserLocalUtils;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ognl.Ognl;
import ognl.OgnlException;

public abstract class BaseLogicService {
	
	public String getAccountId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && (auth.getPrincipal() instanceof UserDetails)) {
			return ( (UserDetails) auth.getPrincipal() ).getUsername();
		}
		if (UserLocalUtils.getUserInfo() != null) { // for JOB service
			return UserLocalUtils.getUserInfo().getUserId();
		}		
		return null;
	}
	
	public String generateOid() {
		return SimpleUtils.getUUIDStr();
	}	
	
	public String defaultString(String source) {
		return SimpleUtils.getStr(source, "");
	}	
	
	public boolean isBlank(String source) {
		return StringUtils.isBlank(source);
	}
	
	protected Map<String, String> providedSelectZeroDataMap(boolean pleaseSelectItem) {
		return PleaseSelect.pageSelectMap(pleaseSelectItem);
	}	
	
	public <T> T setStringValueMaxLength(T obj, String fieldName, int maxLength) {
		if (obj == null) {
			return obj;
		}
		try {
			Object value = Ognl.getValue(fieldName, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), obj);
			if ( !(value instanceof String) ) {
				return obj;
			}
			if ( this.isBlank((String)value) || ((String)value).length() <= maxLength ) {
				return obj;
			}
			value = ((String)value).substring(0, maxLength);
			Ognl.setValue(fieldName, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), obj, value);
		} catch (OgnlException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public <T> T replaceSplit2Blank(T obj, String fieldName, String split) {
		if (obj == null) {
			return obj;
		}
		try {
			Object value = Ognl.getValue(fieldName, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), obj);
			if ( !(value instanceof String) ) {
				return obj;
			}
			if ( this.isBlank((String)value) ) {
				return obj;
			}
			value = ((String)value).replaceAll(split, "");
			Ognl.setValue(fieldName, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), obj, value);
		} catch (OgnlException e) {
			e.printStackTrace();
		}
		return obj;
	}	
	
	protected void replaceAll(Object obj, String variableName, String regex, String replacement) {
		try {
			Object val = Ognl.getValue(variableName, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), obj);
			if (val == null) {
				return;
			}
			String  str = (String) val;
			str = RegExUtils.replaceAll(str, regex, replacement);
			Ognl.setValue(variableName, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), obj, str);
		} catch (OgnlException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
