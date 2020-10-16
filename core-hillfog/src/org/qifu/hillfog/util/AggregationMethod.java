/* 
 * Copyright 2012-2017 bambooCORE, greenstep of copyright Chen Xin Nien
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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.model.ScoreColor;
import org.qifu.hillfog.vo.DateRangeScore;

public class AggregationMethod {
	public static final String QUARTER_1 = "Q1";
	public static final String QUARTER_2 = "Q2";
	public static final String QUARTER_3 = "Q3";
	public static final String QUARTER_4 = "Q4";
	public static final String HALF_YEAR_FIRST = "first";
	public static final String HALF_YEAR_LAST = "last";
	
	public AggregationMethod() {
		
	}
	
	public static AggregationMethod build() {
		try {
			return AggregationMethod.class.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean isDateRange(String date, String frequency, HfMeasureData measureData) {
		if (MeasureDataCode.FREQUENCY_QUARTER.equals(frequency)) { // Q1, Q2, Q3, Q4
			String yyyy = date.substring(0, 4);
			if (date.endsWith(QUARTER_1)) {
				if (!(yyyy+"0101").equals(measureData.getDate())) { // Q1
					return false;
				}
			}
			if (date.endsWith(QUARTER_2)) {
				if (!(yyyy+"0401").equals(measureData.getDate())) { // Q2
					return false;
				}						
			}
			if (date.endsWith(QUARTER_3)) {
				if (!(yyyy+"0701").equals(measureData.getDate())) { // Q3
					return false;
				}							
			}
			if (date.endsWith(QUARTER_4)) {
				if (!(yyyy+"1001").equals(measureData.getDate())) { // Q4
					return false;
				}						
			}					
		} else if (MeasureDataCode.FREQUENCY_HALF_OF_YEAR.equals(frequency)) { // first, last
			String yyyy = date.substring(0, 4);
			if (date.endsWith(HALF_YEAR_FIRST)) {
				if (!(yyyy+"0101").equals(measureData.getDate())) { // first-half
					return false;
				}						
			}
			if (date.endsWith(HALF_YEAR_LAST)) {
				if (!(yyyy+"0701").equals(measureData.getDate())) { // last-half
					return false;
				}							
			}										
		} else { // DAY, WEEK, MONTH, YEAR
			if (!measureData.getDate().startsWith(date)) {
				return false;
			}					
		}			
		return true;
	}
	
	public BigDecimal average(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		BigDecimal score = BigDecimal.ZERO; // init zero
		BigDecimal size = BigDecimal.ZERO;
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
				score = score.add( value );
				size = size.add( BigDecimal.ONE );
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		if ( score.floatValue() != 0.0f && size.longValue() > 0 ) {
			score = score.divide(size, 2, RoundingMode.HALF_UP);
		}
		return score;
	}
	
	public List<DateRangeScore> averageDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			BigDecimal score = BigDecimal.ZERO;
			BigDecimal size = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
					score = score.add( value );
					size = size.add( BigDecimal.ONE );					
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			if ( score.floatValue() != 0.0f && size.longValue() > 0 ) {
				score = score.divide(size, 2, RoundingMode.HALF_UP);
			}
			this.fillDateRangeScore(dateScore, score);
		}
		return dateRangeScores;
	}
	
	public BigDecimal averageDistinct(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		List<BigDecimal> scores = new ArrayList<BigDecimal>();
		BigDecimal score = BigDecimal.ZERO; // init zero
		BigDecimal size = BigDecimal.ZERO;
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
		      	if ( !scores.contains(value) ) {
		      		scores.add( value );
		      		score = score.add( value );
		      		size = size.add( BigDecimal.ONE );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ( score.floatValue() != 0.0f && size.longValue() > 0 ) {
			score = score.divide(size, 2, RoundingMode.HALF_UP);
		}		
		return score;
	}
	
	public List<DateRangeScore> averageDistinctDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			List<BigDecimal> scores = new ArrayList<BigDecimal>();
			BigDecimal score = BigDecimal.ZERO;
			BigDecimal size = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
			      	if ( !scores.contains(value) ) {
						scores.add( value );
						score = score.add( value );
						size = size.add( BigDecimal.ONE );
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if ( score.floatValue() != 0.0f && size.longValue() > 0 ) {
				score = score.divide(size, 2, RoundingMode.HALF_UP);
			}
			this.fillDateRangeScore(dateScore, score);
		}
		return dateRangeScores;
	}
	
	public BigDecimal count(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		if (null == measureDatas) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal( measureDatas.size() );
	}
	
	public List<DateRangeScore> countDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			BigDecimal score = BigDecimal.ZERO;
			BigDecimal size = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				dateScore.getSourceMeasureDatas().add(measureData);
				size = size.add( BigDecimal.ONE );
			}
		    score = score.add(size);
		    this.fillDateRangeScore(dateScore, score);
		}
		return dateRangeScores;
	}
	
	public BigDecimal countDistinct(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		List<BigDecimal> scores = new ArrayList<BigDecimal>();
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
		      	if ( !scores.contains(value) ) {
					scores.add( value );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return new BigDecimal( scores.size() );
	}
	
	public List<DateRangeScore> countDistinctDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			List<BigDecimal> scores = new ArrayList<BigDecimal>();
			BigDecimal score = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}	
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
		      		if ( !scores.contains(value) ) {
						scores.add( value );
					}
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		    score = new BigDecimal( scores.size() );
		    this.fillDateRangeScore(dateScore, score);
		}		
		return dateRangeScores;
	}
	
	public BigDecimal max(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		BigDecimal score = BigDecimal.ZERO; // init
		BigDecimal size = BigDecimal.ZERO;
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
				if ( size.longValue() < 1 ) {
					score = value;
				} else { // Max
					if ( score.compareTo(value) < 0 ) {
						score = value;
					}
				}
				size = size.add( BigDecimal.ONE );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return score;
	}
	
	public List<DateRangeScore> maxDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			BigDecimal score = BigDecimal.ZERO;
			BigDecimal size = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
					if ( size.longValue() < 1 ) {
						score = value;
					} else { // Max
						if ( score.compareTo(value) < 0 ) {
							score = value;
						}
					}
					size = size.add( BigDecimal.ONE );			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.fillDateRangeScore(dateScore, score);
		}	
		return dateRangeScores;
	}	
	
	public BigDecimal min(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		BigDecimal score = BigDecimal.ZERO; // init
		BigDecimal size = BigDecimal.ZERO;
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
				if ( size.longValue() < 1 ) {
					score = value;
				} else { // Min
					if ( score.compareTo(value) > 0 ) {
						score = value;
					}
				}
				size = size.add( BigDecimal.ONE );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return score;
	}
	
	public List<DateRangeScore> minDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			BigDecimal score = BigDecimal.ZERO;
			BigDecimal size = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
					if ( size.longValue() < 1 ) {
						score = value;
					} else { // Min
						if ( score.compareTo(value) > 0 ) {
							score = value;
						}
					}
					size = size.add( BigDecimal.ONE );				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.fillDateRangeScore(dateScore, score);
		}	
		return dateRangeScores;
	}	
	
	public BigDecimal sum(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		BigDecimal score = BigDecimal.ZERO; // init
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
				score = score.add(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return score;
	}
	
	public List<DateRangeScore> sumDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			BigDecimal score = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
					score = score.add( value );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.fillDateRangeScore(dateScore, score);
		}
		return dateRangeScores;
	}	
	
	public BigDecimal sumDistinct(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas) throws Exception {
		List<BigDecimal> scores = new ArrayList<BigDecimal>();
		BigDecimal score = BigDecimal.ZERO; // init
		for (HfMeasureData measureData : measureDatas) {
			try {
				BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
				if (value == null) {
					continue;
				}
				if ( !scores.contains(value) ) {
					scores.add( value );
					score = score.add( value );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return score;
	}
	
	public List<DateRangeScore> sumDistinctDateRange(HfKpi kpi, HfFormula formula, List<HfMeasureData> measureDatas, String frequency, List<DateRangeScore> dateRangeScores) throws Exception {
		for (DateRangeScore dateScore : dateRangeScores) {
			List<BigDecimal> scores = new ArrayList<BigDecimal>();
			BigDecimal score = BigDecimal.ZERO;
			for (HfMeasureData measureData : measureDatas) {
				String date = dateScore.getDate().replaceAll("/", "");
				if (!this.isDateRange(date, frequency, measureData)) {
					continue;
				}
				try {
					BigDecimal value = FormulaUtils.parse(kpi, formula, measureData);
					if (value == null) {
						continue;
					}
					dateScore.getSourceMeasureDatas().add(measureData);
					if ( !scores.contains(value) ) {
						scores.add( value );
						score = score.add(value);
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.fillDateRangeScore(dateScore, score);
		}
		return dateRangeScores;
	}	
	
	private void fillDateRangeScore(DateRangeScore dateScore, BigDecimal score) {
		ScoreColor sc = ScoreColorUtils.get(score);
		dateScore.setScore(score);
		dateScore.setFontColor( sc.getFontColor() );
		dateScore.setBgColor( sc.getBackgroundColor() );
		//dateScore.setImgIcon( BscReportSupportUtils.getHtmlIcon(kpi, score) );
	}
	
}
