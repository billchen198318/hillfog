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
package org.qifu.hillfog.vo;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.qifu.base.model.YesNo;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.model.MeasureDataCode;

public class ScoreCalculationData implements java.io.Serializable {
	private static final long serialVersionUID = 4895615892970938138L;
	private String processDateRange = YesNo.NO;
	private HfKpi kpi;
	private HfFormula formula; // 之後由ScoreCalculationCallable填入
	private List<HfMeasureData> measureDatas; // 之後由ScoreCalculationCallable填入
	private String frequency;
	private String date1;
	private String date2;
	private BigDecimal score; // 之後由ScoreCalculationCallable填入
	private List<DateRangeScore> dataRangeScores; // 之後由ScoreCalculationCallable填入
	private String fontColor; // 之後由ScoreCalculationCallable填入
	private String bgColor; // 之後由ScoreCalculationCallable填入
	private String measureDataAccount = MeasureDataCode.MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
	private String measureDataOrgId = MeasureDataCode.MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
	private String aggregationMethodName = "";
	private String errorMessage = "";
	private List<PdcaItems> pdcaItems = new LinkedList<PdcaItems>(); // 顯示PDCA用
	
	public ScoreCalculationData() {
		super();
	}
	
	public ScoreCalculationData(HfKpi kpi, String frequency, String date1, String date2, String measureDataAccount, String measureDataOrgId) {
		super();
		this.kpi = kpi;
		this.frequency = frequency;
		this.date1 = date1;
		this.date2 = date2;
		this.measureDataAccount = measureDataAccount;
		this.measureDataOrgId = measureDataOrgId;
	}
	
	public String getProcessDateRange() {
		return processDateRange;
	}
	
	public void setProcessDateRange(String processDateRange) {
		this.processDateRange = processDateRange;
	}
	
	public HfKpi getKpi() {
		return kpi;
	}
	
	public void setKpi(HfKpi kpi) {
		this.kpi = kpi;
	}
	
	public HfFormula getFormula() {
		return formula;
	}
	
	public void setFormula(HfFormula formula) {
		this.formula = formula;
	}
	
	public List<HfMeasureData> getMeasureDatas() {
		return measureDatas;
	}
	
	public void setMeasureDatas(List<HfMeasureData> measureDatas) {
		this.measureDatas = measureDatas;
	}
	
	public String getFrequency() {
		return frequency;
	}
	
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	public String getDate1() {
		return date1;
	}
	
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	
	public String getDate2() {
		return date2;
	}
	
	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public List<DateRangeScore> getDataRangeScores() {
		return dataRangeScores;
	}

	public void setDataRangeScores(List<DateRangeScore> dataRangeScores) {
		this.dataRangeScores = dataRangeScores;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getMeasureDataAccount() {
		return measureDataAccount;
	}

	public void setMeasureDataAccount(String measureDataAccount) {
		this.measureDataAccount = measureDataAccount;
	}

	public String getMeasureDataOrgId() {
		return measureDataOrgId;
	}

	public void setMeasureDataOrgId(String measureDataOrgId) {
		this.measureDataOrgId = measureDataOrgId;
	}
	
	public String getAggregationMethodName() {
		return aggregationMethodName;
	}

	public void setAggregationMethodName(String aggregationMethodName) {
		this.aggregationMethodName = aggregationMethodName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<PdcaItems> getPdcaItems() {
		return pdcaItems;
	}

	public void setPdcaItems(List<PdcaItems> pdcaItems) {
		this.pdcaItems = pdcaItems;
	}
	
}
