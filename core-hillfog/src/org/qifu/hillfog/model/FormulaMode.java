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
package org.qifu.hillfog.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.qifu.base.model.PleaseSelect;
import org.qifu.util.LoadResources;

public class FormulaMode {
	
	/**
	 * 預設的模式
	 */
	public final static String MODE_DEFAULT = "D";
	
	/**
	 * 預設的模式的TB_SYS_CODE.CODE
	 */
	public final static String MODE_DEFAULT_NAME = "Default";
	
	/**
	 * 自訂回傳變數的模式
	 */
	public final static String MODE_CUSTOM = "C";
	
	/**
	 * 自訂回傳變數的模式的TB_SYS_CODE.CODE
	 */
	public final static String MODE_CUSTOM_NAME = "Customize";
	
	private static final String _CONFIG = "org/qifu/hillfog/model/FormulaMode.json";
	
	private static Map<String, String> srcMap = null;
	
	static {
		try {
			srcMap = LoadResources.objectMapperReadValue(_CONFIG, LinkedHashMap.class, FormulaMode.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null==srcMap) {
				srcMap = new HashMap<String, String>();
			}
		}
	}	
	
	public static Map<String, String> getReturnModeMap(boolean pleaseSelect) {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		if (srcMap != null && srcMap.get(MODE_DEFAULT) != null && srcMap.get(MODE_CUSTOM) != null) {
			dataMap.putAll(srcMap);
		} else {
			dataMap.put(MODE_DEFAULT, MODE_DEFAULT_NAME);
			dataMap.put(MODE_CUSTOM, MODE_CUSTOM_NAME);			
		}
		return dataMap;
	}	
	
}
