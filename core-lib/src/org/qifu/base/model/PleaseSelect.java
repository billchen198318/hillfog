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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PleaseSelect {
	private static final String _CONFIG = "META-INF/resource/please-select-label-name.json";
	private static String _pleaseSelectDatas = " { } ";
	private static Map<String, Object> _pleaseSelectMap;
	
	static {
		try {
			InputStream is = Constants.class.getClassLoader().getResource( _CONFIG ).openStream();
			_pleaseSelectDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
			is = null;
			_pleaseSelectMap = loadDatas();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==_pleaseSelectMap) {
				_pleaseSelectMap = new HashMap<String, Object>();
			}
		}		
	}
	
	public static boolean isAllOption(String value) {
		if (Constants.HTML_SELECT_NO_SELECT_ID.equals(value)) {
			return true;
		}
		return false;
	}
	
	public static boolean noSelect(String value) {
		if (StringUtils.isBlank(value) || Constants.HTML_SELECT_NO_SELECT_ID.equals(value)) {
			return true;
		}
		return false;
	}	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadDatas() {
		Map<String, Object> datas = null;
		try {
			datas = (Map<String, Object>)new ObjectMapper().readValue( _pleaseSelectDatas, LinkedHashMap.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	public static Map<String, Object> getDataMap() {
		return _pleaseSelectMap;
	}		

	public static String getLabel(String localeLang) {
		String label = (String) _pleaseSelectMap.get(localeLang);
		return (StringUtils.isBlank(label)) ? Constants.HTML_SELECT_NO_SELECT_NAME : label;
	}
	
	public static Map<String, String> pageSelectMap(boolean pleaseSelect) {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		if (pleaseSelect) {
			dataMap.put(Constants.HTML_SELECT_NO_SELECT_ID, Constants.HTML_SELECT_NO_SELECT_NAME);
		}
		return dataMap;
	}
	
}
