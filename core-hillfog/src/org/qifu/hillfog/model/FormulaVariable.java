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

public class FormulaVariable {

	/**
	 * 數據-目標值
	 */
	public static final String PAGE_MEASURE_DATA_TARGET = "$P{target}";
	public static final String PARAM_MEASURE_DATA_TARGET = "$target";
	
	/**
	 * 數據-實際值
	 */
	public static final String PAGE_MEASURE_DATA_ACTUAL = "$P{actual}";
	public static final String PARAM_MEASURE_DATA_ACTUAL = "$actual";
	
	/**
	 * KPI Entity物件
	 */
	public static final String PARAM_KPI_OBJECT = "$kpi";
	
	/**
	 * KPI最大值(分數or儀表板)
	 */
	public static final String PAGE_KPI_MAX = "$P{kpi.max}";
	public static final String PARAM_KPI_MAX = "$kpiMax";
	
	/**
	 * KPI最小值(分數or儀表板)
	 */
	public static final String PAGE_KPI_MIN = "$P{kpi.min}";
	public static final String PARAM_KPI_MIN = "$kpiMin";
	
	/**
	 * KPI目標(分數or儀表板)
	 */
	public static final String PAGE_KPI_TARGET = "$P{kpi.target}";
	public static final String PARAM_KPI_TARGET = "$kpiTarget";
	
	/**
	 * 權重
	 */
	public static final String PAGE_KPI_WEIGHT = "$P{kpi.weight}";
	public static final String PARAM_KPI_WEIGHT = "$kpiWeight";
	
}
