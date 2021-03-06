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

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.YesNo;
import org.qifu.hillfog.callable.ScoreCalculationCallable;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.vo.DateRangeScore;
import org.qifu.hillfog.vo.ScoreCalculationData;
import org.qifu.util.SimpleUtils;

public class KpiScore {
	
	private List<ScoreCalculationData> scoreDatas = new LinkedList<ScoreCalculationData>();
	
	public KpiScore() {
		
	}
	
	public static KpiScore build() {
		try {
			return KpiScore.class.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public KpiScore add(List<HfKpi> kpis, String frequency, String date1, String date2, String measureDataAccount, String measureDataOrgId) {
		for (HfKpi kpi : kpis) {
			this.add(kpi, frequency, date1, date2, measureDataAccount, measureDataOrgId);
		}
		return this;
	}
	
	public KpiScore add(HfKpi kpi, String frequency, String date1, String date2, String measureDataAccount, String measureDataOrgId) {
		boolean found = false;
		for (int i = 0; !found && this.scoreDatas != null && i < this.scoreDatas.size(); i++) {
			if (this.scoreDatas.get(i).getKpi().getOid().equals(kpi.getOid())) {
				found = true;
			}
		}
		if (found) {
			return this;
		}
		ScoreCalculationData scd = new ScoreCalculationData();
		scd.setKpi(kpi);
		scd.setFrequency(frequency);
		scd.setDate1(date1);
		scd.setDate2(date2);
		scd.setMeasureDataAccount(measureDataAccount);
		scd.setMeasureDataOrgId(measureDataOrgId);
		this.scoreDatas.add(scd);
		return this;
	}
	
	public KpiScore remove(HfKpi kpi) {
		for (int i = 0; this.scoreDatas != null && i < this.scoreDatas.size(); i++) {
			if (this.scoreDatas.get(i).getKpi().getOid().equals(kpi.getOid())) {
				this.scoreDatas.remove(i);
				i = this.scoreDatas.size();
			}
		}
		return this;
	}
	
	public KpiScore clear() {
		this.scoreDatas.clear();
		return this;
	}
	
	private int getAvailableProcessors() {
		int ftps = SimpleUtils.getAvailableProcessors(this.scoreDatas.size());
		if (ftps > 8) {
			ftps = 8;
		}
		return ftps;
	}
	
	private void process(String processDateRangeFlag) {	
		ExecutorService kpiCalculationPool = Executors.newFixedThreadPool( this.getAvailableProcessors() );
		for (ScoreCalculationData data : this.scoreDatas) {
			data.setProcessDateRange( processDateRangeFlag );
			try {
				data = kpiCalculationPool.submit( new ScoreCalculationCallable(data) ).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
				data.setErrorMessage( (e == null || e.getMessage() == null) ? BaseSystemMessage.objectNull() : e.getMessage() );
			} catch (ExecutionException e) {
				e.printStackTrace();
				data.setErrorMessage( (e == null || e.getMessage() == null) ? BaseSystemMessage.objectNull() : e.getMessage() );
			}
		}
		kpiCalculationPool.shutdown();
	}
	
	public KpiScore processDefault() {
		this.process( YesNo.NO );
		return this;
	}
	
	public KpiScore processDateRange() {
		this.process( YesNo.YES );
		return this;
	}
	
	public KpiScore reduce() {
		for (ScoreCalculationData data : this.scoreDatas) {
			if (data.getFormula() != null) {
				data.getFormula().setExpression("");
				data.getFormula().setDescription("");
				data.getFormula().setCdate(null);
				data.getFormula().setCuserid("");
				data.getFormula().setUdate(null);
				data.getFormula().setUuserid("");
			}
			if (data.getKpi() != null) {
				data.getKpi().setCdate(null);
				data.getKpi().setCuserid("");
				data.getKpi().setUdate(null);
				data.getKpi().setUuserid("");
			}
			for (HfMeasureData md : data.getMeasureDatas()) {
				md.setCdate(null);
				md.setCuserid("");
				md.setUdate(null);
				md.setUuserid("");
			}
			for (DateRangeScore drs : data.getDataRangeScores()) {
				for (HfMeasureData md : drs.getSourceMeasureDatas()) {
					md.setCdate(null);
					md.setCuserid("");
					md.setUdate(null);
					md.setUuserid("");
				}
			}
		}
		return this;
	}
	
	public List<ScoreCalculationData> value() {
		return this.scoreDatas;
	}
	
	public List<ScoreCalculationData> valueThrowMessage() throws Exception {
		for (ScoreCalculationData scd : this.scoreDatas) {
			if (!StringUtils.isBlank(scd.getErrorMessage())) {
				throw new Exception( scd.getErrorMessage() );
			}
		}
		return this.scoreDatas;
	}	
	
}
