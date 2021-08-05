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
package org.qifu.hillfog.callable;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.qifu.base.model.YesNo;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.model.ScoreColor;
import org.qifu.hillfog.util.AggregationMethodUtils;
import org.qifu.hillfog.util.FormulaUtils;
import org.qifu.hillfog.util.KpiScoreDataDateRangeInitUtils;
import org.qifu.hillfog.util.QueryMeasureDataUtils;
import org.qifu.hillfog.util.ScoreColorUtils;
import org.qifu.hillfog.vo.DateRangeScore;
import org.qifu.hillfog.vo.ScoreCalculationData;

public class ScoreCalculationCallable implements Callable<ScoreCalculationData> {
	private ScoreCalculationData data = null;
	
	public ScoreCalculationCallable(ScoreCalculationData data) {
		super();
		this.data = data;
	}	
	
	@Override
	public ScoreCalculationData call() throws Exception {
		if (this.data.getFormula() == null) {
			this.data.setFormula( FormulaUtils.getFormulaById(this.data.getKpi().getForId()) );
		}
		if (CollectionUtils.isEmpty(this.data.getMeasureDatas())) {
			this.data.setMeasureDatas( QueryMeasureDataUtils.queryForScoreCalculationData(this.data) );
		}
		this.data.setAggregationMethodName( AggregationMethodUtils.findAggregationMethodNameById( this.data.getKpi().getAggrId()) );
		if ( !YesNo.YES.equals(this.data.getProcessDateRange()) ) { // KPI分數
			BigDecimal score = AggregationMethodUtils.processDefaultMode(this.data.getKpi(), this.data.getFormula(), this.data.getMeasureDatas(), this.data.getFrequency());
			this.data.setScore(score);
			ScoreColor sc = ScoreColorUtils.get(score);
			this.data.setBgColor( sc.getBackgroundColor() );
			this.data.setFontColor( sc.getFontColor() );	
		} else { // KPI 日期區間分數
			List<String> dateRange = KpiScoreDataDateRangeInitUtils.getDateRange(this.data.getFrequency(), this.data.getDate1(), this.data.getDate2());
			this.data.setDataRangeScores( this.initKpiDateRangeScores(this.data.getKpi(), dateRange) );			
			this.data.setDataRangeScores( AggregationMethodUtils.processDateRangeMode(this.data.getKpi(), this.data.getFormula(), this.data.getMeasureDatas(), this.data.getFrequency(), this.data.getDataRangeScores()) );
		}
		return this.data;
	}
	
	public ScoreCalculationData getData() {
		return data;
	}

	public void setData(ScoreCalculationData data) {
		this.data = data;
	}	
	
	private List<DateRangeScore> initKpiDateRangeScores(HfKpi kpi, List<String> dateRange) throws Exception {
		List<DateRangeScore> dataRangeScores = new LinkedList<DateRangeScore>();
		ScoreColor sc = ScoreColorUtils.getUnknown();
		for (String date : dateRange) {
			DateRangeScore dateScore = new DateRangeScore();
			dateScore.setTarget(kpi.getTarget());
			dateScore.setMin(kpi.getMin());
			dateScore.setDate(date);
			dateScore.setScore( BigDecimal.ZERO );
			dateScore.setFontColor( sc.getFontColor() );
			dateScore.setBgColor( sc.getBackgroundColor() );
			dateScore.setImgIcon("");
			dataRangeScores.add(dateScore);			
		}
		return dataRangeScores;
	}	
	
}
