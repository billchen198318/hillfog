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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.PleaseSelect;
import org.qifu.hillfog.vo.MeasureDataQueryParam;
import org.qifu.util.SimpleUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MeasureDataCode {
	
	private static final String _CONFIG = "org/qifu/hillfog/model/MeasureDataFrequency.json";
	
	private static Map<String, String> srcMap = null;
	
	private static String _srcDatas = " { } ";
	
	static {
		try {
			InputStream is = MeasureDataCode.class.getClassLoader().getResource( _CONFIG ).openStream();
			_srcDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
			is = null;
			srcMap = loadDatas();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==srcMap) {
				srcMap = new HashMap<String, String>();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, String> loadDatas() {
		Map<String, String> datas = null;
		try {
			datas = (Map<String, String>)new ObjectMapper().readValue( _srcDatas, LinkedHashMap.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}		
	
	/**
	 * 不區分員工/部門的衡量資料
	 */
	public final static String MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL = "*";
	
	/**
	 * 衡量數據(TARGET)輸入欄位的開頭
	 */
	public final static String MEASURE_DATA_TARGET_ID = "MEASURE_DATA_TARGET:";
	
	/**
	 * 衡量數據(ACTUAL)輸入欄位的開頭
	 */
	public final static String MEASURE_DATA_ACTUAL_ID = "MEASURE_DATA_ACTUAL:";	
	
	/**
	 * 日
	 */
	public static final String FREQUENCY_DAY = "1";
	
	
	/**
	 * 周
	 */
	public static final String FREQUENCY_WEEK = "2";
	
	/**
	 * 月
	 */
	public static final String FREQUENCY_MONTH = "3";
	
	/**
	 * 季
	 */
	public static final String FREQUENCY_QUARTER = "4";
	
	/**
	 * 半年
	 */
	public static final String FREQUENCY_HALF_OF_YEAR = "5";
	
	/**
	 * 年
	 */
	public static final String FREQUENCY_YEAR = "6";
	
	public static Map<String, String> getFrequencyMap(boolean pleaseSelect) {
		if (!pleaseSelect) {
			return srcMap;
		}
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		dataMap.putAll( srcMap );
		return dataMap;
	}	
	
	public static String getQueryDate(String date, String frequency) {
		String queryDate = date.replaceAll("-", "").replaceAll("/", "");		
		if (FREQUENCY_DAY.equals(frequency) || FREQUENCY_WEEK.equals(frequency) ) {
			queryDate = queryDate.substring(0, 6);
		}
		if (FREQUENCY_MONTH.equals(frequency) || FREQUENCY_QUARTER.equals(frequency) 
				|| FREQUENCY_HALF_OF_YEAR.equals(frequency) || FREQUENCY_YEAR.equals(frequency) ) {
			queryDate = queryDate.substring(0, 4);
		}		
		return queryDate;
	}	
	
	/**
	 * 給報表查詢時, 在 bb_measure_data 正確的 "月", "周" 的日期起迄
	 * yyyyMMdd 
	 * 2013/01/01
	 * 20130101
	 * 
	 * @param yyyyMMdd
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getWeekOrMonthStartEnd(String frequency, String startDate, String endDate) throws Exception {
		if (!FREQUENCY_WEEK.equals(frequency) && !FREQUENCY_MONTH.equals(frequency)) {
			throw new java.lang.IllegalArgumentException("frequency error.");
		}
		Map<String, String> dateMap=new HashMap<String, String>();
		if (!SimpleUtils.isDate(startDate) || !SimpleUtils.isDate(endDate)) {
			throw new java.lang.IllegalArgumentException("startDate/endDate error.");
		}
		if (FREQUENCY_WEEK.equals(frequency)) {
			int firstDay = Integer.parseInt( startDate.substring(startDate.length()-2, startDate.length()) );
			int endDay = Integer.parseInt( endDate.substring(endDate.length()-2, endDate.length()) );
			if (firstDay>=1 && firstDay<8) {
				firstDay = 1;
			}
			if (firstDay>=8 && firstDay<15) {
				firstDay = 8;
			}
			if (firstDay>=15 && firstDay<22) {
				firstDay = 15;
			}
			if (firstDay>=22) { 
				firstDay = 22;
			}
			if (endDay>=1 && endDay<8) {
				endDay = 7;
			}
			if (endDay>=8 && endDay<15) {
				endDay = 14;
			}
			if (endDay>=15 && endDay<22) {
				endDay = 21;
			}
			if (endDay>=22) { 
				endDay = SimpleUtils.getMaxDayOfMonth( Integer.parseInt(endDate.substring(0, 4)), Integer.parseInt(endDate.substring(5, 7)) );
			}
			String newStartDate = startDate.substring(0, startDate.length()-2) + StringUtils.leftPad(String.valueOf(firstDay), 2, "0");
			String newEndDate = endDate.substring(0, endDate.length()-2) + StringUtils.leftPad(String.valueOf(endDay), 2, "0");
			dateMap.put("startDate", newStartDate);
			dateMap.put("endDate", newEndDate);
		}
		if (FREQUENCY_MONTH.equals(frequency)) {
			int endDay = SimpleUtils.getMaxDayOfMonth( Integer.parseInt(endDate.substring(0, 4)), Integer.parseInt(endDate.substring(5, 7)) );
			String newStartDate = startDate.substring(0, startDate.length()-2) + "01";
			String newEndDate = endDate.substring(0, endDate.length()-2) + StringUtils.leftPad(String.valueOf(endDay), 2, "0");			
			dateMap.put("startDate", newStartDate);
			dateMap.put("endDate", newEndDate);	
		}
		return dateMap;		
	}	
	
	public static MeasureDataQueryParam queryParam(HttpServletRequest request) throws ServiceException, Exception {
		String accountId = MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
		String orgId = MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
		if (!StringUtils.isBlank(request.getParameter("kpiEmpl"))) {
			accountId = request.getParameter("kpiEmpl");
			String tmp[] = accountId.split("/");
			if (tmp == null || tmp.length != 3) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
			accountId = StringUtils.deleteWhitespace(tmp[1]);
			if (StringUtils.isBlank(accountId)) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
		}
		if (!StringUtils.isBlank(request.getParameter("kpiOrga"))) {
			orgId = request.getParameter("kpiOrga");
			orgId = StringUtils.deleteWhitespace(orgId.split("/")[0]);
		}		
		return new MeasureDataQueryParam(accountId, orgId);
	}
	
}
