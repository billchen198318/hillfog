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
package org.qifu.base.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultControllerJsonResultObj<T> implements java.io.Serializable {
	private static final long serialVersionUID = 3754125684960027639L;
	
	private T value = null;
	private String success = YesNo.NO;
	private String message = "";
	private String login = YesNo.NO;
	private String isAuthorize = YesNo.NO;
	private Map<String, String> checkFields = new LinkedHashMap<String, String>(); // 不符合條件的輸入欄位
	
	public static <T> DefaultControllerJsonResultObj<T> build() {
		DefaultControllerJsonResultObj<T> obj = new DefaultControllerJsonResultObj<T>();
		return obj;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public String getSuccess() {
		return success;
	}
	
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getIsAuthorize() {
		return isAuthorize;
	}
	
	public void setIsAuthorize(String isAuthorize) {
		this.isAuthorize = isAuthorize;
	}

	public Map<String, String> getCheckFields() {
		return checkFields;
	}

	public void setCheckFields(Map<String, String> checkFields) {
		this.checkFields = checkFields;
	}
	
}
