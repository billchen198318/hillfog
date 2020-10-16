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
package org.qifu.hillfog.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.hillfog.vo.DateRangeScore;
import org.qifu.util.ScriptExpressionUtils;

public class AggregationMethodUtils {
	/**
	 * kpi變數
	 */
	public static final String VAR_KPI = "kpi";
	
	/**
	 * 分數變數
	 */
	public static final String VAR_SCORE = "score";
	
	/**
	 * 日期區間頻率
	 */
	public static final String VAR_FREQUENCY = "frequency";
	
	/**
	 * 衡量數據資料list
	 */
	public static final String VAR_MEASURE_DATAS = "measureDatas";
	
	/**
	 * 日期區間分數list
	 */
	public static final String VAR_DATE_RANGE_SCORES = "dateRangeScores";
	
	/**
	 * 公式
	 */
	public static final String VAR_FORMULA = "formula";
	
	private static IAggregationMethodService<HfAggregationMethod, String> aggregationMethodService;
	private static ThreadLocal<Map<String, HfAggregationMethod>> aggrMethodThreadLocal = new ThreadLocal<Map<String, HfAggregationMethod>>();
	
	static {
		aggregationMethodService = AppContext.context.getBean(IAggregationMethodService.class);	
	}
	
	public static void clearThreadLocal() {
		aggrMethodThreadLocal.remove();
	}
	
	private static HfAggregationMethod findAggregationMethodById(String id) throws ServiceException, Exception {
		Map<String, HfAggregationMethod> datas = aggrMethodThreadLocal.get();
		if (datas!=null) {
			if ( datas.get(id) != null ) {
				return datas.get(id);
			}
		}
		HfAggregationMethod aggr = new HfAggregationMethod();
		aggr.setAggrId( id );
		aggr = aggregationMethodService.selectByUniqueKey(aggr).getValueEmptyThrowMessage();
		if (null == aggrMethodThreadLocal.get()) {
			aggrMethodThreadLocal.set( new HashMap<String, HfAggregationMethod>() );	
		}
		aggrMethodThreadLocal.get().put(id, aggr);
		return aggr;
	}		
	
	private static Map<String, Object> getParameter(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(VAR_KPI, kpi);
		parameter.put(VAR_FREQUENCY, frequency);
		parameter.put(VAR_DATE_RANGE_SCORES, dateRangeScores);
		parameter.put(VAR_MEASURE_DATAS, measureDatas);
		parameter.put(VAR_FORMULA, formula);
		parameter.put(VAR_SCORE, null);
		return parameter;
	}
	
	private static Map<String, Object> getScore(String type, String expression, HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		if ( kpi == null || measureDatas == null ) {
			throw new Exception("no measure data!");
		}
		if ( formula == null ) {
			throw new Exception("no formula data!");
		}
		Map<String, Object> results = new HashMap<String, Object>();
		results.put(VAR_SCORE, null);
		results = ScriptExpressionUtils.execute(type, expression, results, getParameter(kpi, formula, measureDatas, frequency, dateRangeScores));
		// System.out.println("results=" +  results );
		return results;		
	}
	
	public static BigDecimal processDefaultMode(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency) throws Exception {
		HfAggregationMethod aggrMethod = findAggregationMethodById(kpi.getAggrId());
		Map<String, Object> results = getScore(aggrMethod.getType(), aggrMethod.getExpression1(), kpi, formula, measureDatas, frequency, null);
		Object value = results.get( VAR_SCORE );
		if ( null == value || !(value instanceof BigDecimal) ) {
			return BigDecimal.ZERO;
		}
		return (BigDecimal)value;
	}	
	
	public static List<DateRangeScore> processDateRangeMode(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		HfAggregationMethod aggrMethod = findAggregationMethodById(kpi.getAggrId());
		Map<String, Object> results = getScore(aggrMethod.getType(), aggrMethod.getExpression2(), kpi, formula, measureDatas, frequency, dateRangeScores);
		Object value = results.get( VAR_DATE_RANGE_SCORES );
		if ( null == value || !(value instanceof List) ) {
			return dateRangeScores;
		}
		return (List<DateRangeScore>) value;
	}
	
}
