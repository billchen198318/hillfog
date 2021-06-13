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
package org.qifu.hillfog.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.util.SimpleUtils;

public class KpiScoreDataDateRangeInitUtils {
	
	public static DefaultResult<Boolean> checkDateRangeSize(String frequency, String date1, String date2) throws ServiceException, Exception {
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		result.setMessage( "success!" );
		boolean checkSuccess = true;
		List<String> dateRange = getDateRange(frequency, date1, date2);
		if (MeasureDataCode.FREQUENCY_YEAR.equals(frequency)) {
			if (dateRange.size() > 6) {
				checkSuccess = false;
				result.setMessage( "The query interval cannot exceed 6 years!" );
			}
		}
		if (MeasureDataCode.FREQUENCY_HALF_OF_YEAR.equals(frequency)) {
			if (dateRange.size() > 12) {
				checkSuccess = false;
				result.setMessage( "The query interval cannot exceed 6 years!" );				
			}
		}
		if (MeasureDataCode.FREQUENCY_QUARTER.equals(frequency)) {
			if (dateRange.size() > 12) {
				checkSuccess = false;
				result.setMessage( "The query interval cannot exceed 3 years!" );				
			}			
		}
		if (MeasureDataCode.FREQUENCY_DAY.equals(frequency) || MeasureDataCode.FREQUENCY_WEEK.equals(frequency) || MeasureDataCode.FREQUENCY_MONTH.equals(frequency) ) {
			if (dateRange.size() > 12) {
				checkSuccess = false;
				result.setMessage( "The query interval cannot exceed 1 years!" );					
			}
		}
		result.setValue(checkSuccess);
		return result;
	}
	
	public static List<String> getDateRange(String frequency, String date1, String date2) throws Exception {
		List<String> dateRange = new LinkedList<String>();
		if (MeasureDataCode.FREQUENCY_DAY.equals(frequency) || MeasureDataCode.FREQUENCY_WEEK.equals(frequency) || MeasureDataCode.FREQUENCY_MONTH.equals(frequency) ) {
			DateTime dt1 = new DateTime( date1.replaceAll("/", "-").substring( 0, date1.length()-2 ) + "01" ); 
			DateTime dt2 = new DateTime( date2.replaceAll("/", "-").substring( 0, date2.length()-2 ) + "01" );
			int betweenMonths = Months.monthsBetween(dt1, dt2).getMonths();
			Date nowDate = dt1.toDate();
			for (int i=0; i<=betweenMonths; i++) {
				dateRange.add( SimpleUtils.getStrYMD(nowDate, "/").substring(0, 7) );
				nowDate = DateUtils.addMonths(nowDate, 1);
			}			
			return dateRange;
		} 
		if (MeasureDataCode.FREQUENCY_QUARTER.equals(frequency)) {
			DateTime dt1 = new DateTime( date1.substring(0, 4) + "-01-01" ); 
			DateTime dt2 = new DateTime( date2.substring(0, 4) + "-01-01" );
			int betweenYears = Years.yearsBetween(dt1, dt2).getYears();
			Date nowDate = dt1.toDate();
			for (int i=0; i<=betweenYears; i++) {
				String yyyy = SimpleUtils.getStrYMD(nowDate, "/").substring(0, 4);				
				dateRange.add( yyyy + "/" + AggregationMethod.QUARTER_1 );
				dateRange.add( yyyy + "/" + AggregationMethod.QUARTER_2 );
				dateRange.add( yyyy + "/" + AggregationMethod.QUARTER_3 );
				dateRange.add( yyyy + "/" + AggregationMethod.QUARTER_4 );
				nowDate = DateUtils.addYears(nowDate, 1);
			}
			return dateRange;
		}
		if (MeasureDataCode.FREQUENCY_HALF_OF_YEAR.equals(frequency)) {
			DateTime dt1 = new DateTime( date1.substring(0, 4) + "-01-01" ); 
			DateTime dt2 = new DateTime( date2.substring(0, 4) + "-01-01" );
			int betweenYears = Years.yearsBetween(dt1, dt2).getYears();
			Date nowDate = dt1.toDate();
			for (int i=0; i<=betweenYears; i++) {
				String yyyy = SimpleUtils.getStrYMD(nowDate, "/").substring(0, 4);				
				dateRange.add( yyyy + "/" + AggregationMethod.HALF_YEAR_FIRST );
				dateRange.add( yyyy + "/" + AggregationMethod.HALF_YEAR_LAST );
				nowDate = DateUtils.addYears(nowDate, 1);
			}
			return dateRange;
		}		
		int begYear = Integer.parseInt( date1.substring(0, 4) );
		int endYear = Integer.parseInt( date2.substring(0, 4) );
		for (int i=begYear; i<=endYear; i++) {
			dateRange.add( String.valueOf(i) );
		}
		return dateRange;		
	}	
	
}
