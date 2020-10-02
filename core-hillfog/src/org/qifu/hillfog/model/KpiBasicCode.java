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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.qifu.base.Constants;
import org.qifu.base.model.PleaseSelect;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KpiBasicCode {
	
	private static final String _CONFIG = "org/qifu/hillfog/model/KpiBasicCode.json";
	
	private static Map<String, Object> srcMap = null;
	
	private static String _srcDatas = " { } ";	
	
	private static final String[][] QUASI_RANGE_TABLE = new String[][] {
		{ "0", 		"0%" },
		{ "1", 		"1%" },
		{ "3", 		"3%" },
		{ "5", 		"5%" },
		{ "7", 		"7%" },
		{ "9", 		"9%" },
		{ "10", 	"10%" },
		{ "15", 	"15%" },
		{ "20", 	"20%" },
		{ "25", 	"25%" },
		{ "30", 	"30%" },
		{ "35", 	"35%" },
		{ "40", 	"40%" },
		{ "45", 	"45%" },
		{ "50", 	"50%" },
	};
	
	private static Map<String, String> managementMap = new LinkedHashMap<String, String>();
	private static Map<String, String> compareTypeMap = new LinkedHashMap<String, String>();
	private static Map<String, String> dataTypeMap = new LinkedHashMap<String, String>();
	private static Map<String, String> quasiRangeMap = new LinkedHashMap<String, String>();	
	
	static {
		try {
			InputStream is = KpiBasicCode.class.getClassLoader().getResource( _CONFIG ).openStream();
			_srcDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
			is = null;
			srcMap = loadDatas();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==srcMap) {
				srcMap = new HashMap<String, Object>();
			}
		}		
		for (int i=0; i<QUASI_RANGE_TABLE.length; i++) {
			quasiRangeMap.put(QUASI_RANGE_TABLE[i][0], QUASI_RANGE_TABLE[i][1]);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> loadDatas() {
		Map<String, Object> datas = null;
		try {
			datas = (Map<String, Object>)new ObjectMapper().readValue( _srcDatas, LinkedHashMap.class );
			if (datas.get("management") != null) {
				managementMap.putAll( (Map<? extends String, ? extends String>) datas.get("management") );
			}
			if (datas.get("compareType") != null) {
				compareTypeMap.putAll( (Map<? extends String, ? extends String>) datas.get("compareType") );
			}
			if (datas.get("dataType") != null) {
				dataTypeMap.putAll( (Map<? extends String, ? extends String>) datas.get("dataType") );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkDataMap();
		}
		return datas;
	}
	
	private static void checkDataMap() {
		if (managementMap.size() != 3) {
			managementMap.clear();
			managementMap.put(MANAGEMENT_BIGGER_IS_BETTER, MANAGEMENT_BIGGER_IS_BETTER);
			managementMap.put(MANAGEMENT_SMALLER_IS_BETTER, MANAGEMENT_SMALLER_IS_BETTER);
			managementMap.put(MANAGEMENT_QUASI_IS_BETTER, MANAGEMENT_QUASI_IS_BETTER);
		}		
		if (compareTypeMap.size() != 2) {
			compareTypeMap.clear();
			compareTypeMap.put(COMPARE_TYPE_TARGET, COMPARE_TYPE_TARGET);
			compareTypeMap.put(COMPARE_TYPE_MIN, COMPARE_TYPE_MIN);
		}
		if (dataTypeMap.size() != 3) {
			dataTypeMap.clear();
			dataTypeMap.put(DATA_TYPE_DEPARTMENT, DATA_TYPE_DEPARTMENT);
			dataTypeMap.put(DATA_TYPE_PERSONAL, DATA_TYPE_PERSONAL);
			dataTypeMap.put(DATA_TYPE_BOTH, DATA_TYPE_BOTH);
		}		
	}
	
	public static Map<String, String> getManagementMap(boolean pleaseSelect) {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		dataMap.putAll(managementMap);
		return dataMap;
	}	
	
	public static Map<String, String> getCompareTypeMap(boolean pleaseSelect) {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		dataMap.putAll(compareTypeMap);
		return dataMap;
	}		
	
	public static Map<String, String> getDataTypeMap(boolean pleaseSelect) {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		dataMap.putAll(dataTypeMap);
		return dataMap;
	}		
	
	public static Map<String, String> getQuasiRangeMap() {
		return quasiRangeMap;
	}
	
	// -------------------------------------------------------------------------------------------
	
	/**
	 * 越大越好
	 */
	public static final String MANAGEMENT_BIGGER_IS_BETTER = "1";
	
	/**
	 * 越小越好
	 */
	public static final String MANAGEMENT_SMALLER_IS_BETTER = "2";	
	
	/**
	 * 越準越好
	 */
	public static final String MANAGEMENT_QUASI_IS_BETTER = "3";		
	
	// -------------------------------------------------------------------------------------------	
	
	/**
	 * 1.與TARGET比較
	 */
	public static final String COMPARE_TYPE_TARGET = "1";
	
	/**
	 * 2.與MIN比較
	 */
	public static final String COMPARE_TYPE_MIN = "2";
	
	// -------------------------------------------------------------------------------------------
	
	/**
	 * 部門 KPI
	 */
	public static final String DATA_TYPE_DEPARTMENT = "1";
	
	/**
	 * 個人 KPI
	 */
	public static final String DATA_TYPE_PERSONAL = "2";
	
	/**
	 * 部門/個人 KPI
	 */
	public static final String DATA_TYPE_BOTH = "3";
	
}
