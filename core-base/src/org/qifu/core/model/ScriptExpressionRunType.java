/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.model;

import java.util.Map;

import org.qifu.base.model.PleaseSelect;

public class ScriptExpressionRunType {
	public static final String IS_AFTER = "AFTER";
	public static final String IS_BEFORE = "BEFORE";
	
	public static boolean isCode(String runType) {
		if (IS_AFTER.equals(runType) || IS_BEFORE.equals(runType)) {
			return true;
		}
		return false;
	}
	
	public static Map<String, String> getRunTypeMap(boolean pleaseSelect) {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		dataMap.put(IS_BEFORE, IS_BEFORE);
		dataMap.put(IS_AFTER, IS_AFTER);
		return dataMap;
	}
	
}
