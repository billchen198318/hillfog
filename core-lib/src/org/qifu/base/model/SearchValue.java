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

import java.util.HashMap;
import java.util.Map;

public class SearchValue implements java.io.Serializable {
	private static final long serialVersionUID = 8848320628029045810L;
	private Map<String, String> parameter=new HashMap<String, String>();
	
	public SearchValue() {
		
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
	
}
