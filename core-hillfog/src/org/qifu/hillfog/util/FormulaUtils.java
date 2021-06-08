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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.model.FormulaMode;
import org.qifu.hillfog.model.FormulaVariable;
import org.qifu.hillfog.service.IFormulaService;
import org.qifu.util.ScriptExpressionUtils;

public class FormulaUtils {
	private static final String DEFAULT_RETURN_MODE_VAR = "ans_" + System.currentTimeMillis();
	
	private static IFormulaService<HfFormula, String> formulaService;
	
	static {
		formulaService = (IFormulaService<HfFormula, String>) AppContext.context.getBean(IFormulaService.class);
	}
	
	public static HfFormula getFormulaById(String forId) throws ServiceException, Exception {
		if (StringUtils.isBlank(forId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		HfFormula formula = new HfFormula();
		formula.setForId( forId );
		formula = formulaService.selectByUniqueKey(formula).getValueEmptyThrowMessage();
		return formula;
	}
	
	public static Map<String, Object> getParameter(HfKpi kpi, HfMeasureData measureData) {
		if (null == kpi || null == measureData) {
			return null;
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		if (measureData.getTarget() != null) {
			parameter.put(FormulaVariable.PARAM_MEASURE_DATA_TARGET, measureData.getTarget());
		}
		if (measureData.getActual() != null) {
			parameter.put(FormulaVariable.PARAM_MEASURE_DATA_ACTUAL, measureData.getActual());
		}	
		if (kpi != null) {
			parameter.put(FormulaVariable.PARAM_KPI_OBJECT, kpi);
			parameter.put(FormulaVariable.PARAM_KPI_MAX, kpi.getMax());
			parameter.put(FormulaVariable.PARAM_KPI_MIN, kpi.getMin());
			parameter.put(FormulaVariable.PARAM_KPI_TARGET, kpi.getTarget());
			/**
			 * FIXME : 改為填入 HfSoOwnerKpis.cardWeight
			 */
			parameter.put(FormulaVariable.PARAM_KPI_WEIGHT, kpi.getWeight());
		}
		return parameter;
	}	
	
	public static Map<String, Object> parse(String type, String returnMode, String returnVar, String expression, Map<String, Object> parameter) throws Exception {
		Map<String, Object> results = new HashMap<String, Object>();
		if (FormulaMode.MODE_CUSTOM.equals(returnMode)) {
			if (StringUtils.isBlank(returnVar)) {
				throw new java.lang.IllegalArgumentException("returnVar cannot blank!");
			}
			results.put(returnVar, null);
		} else {
			results.put(DEFAULT_RETURN_MODE_VAR, null);
		}
		ScriptExpressionUtils.execute(type, handlerExpression(type, returnMode, expression), results, parameter);
		return results;
	}
	
	public static BigDecimal parse(HfKpi kpi, HfFormula formula, HfMeasureData data) throws Exception {
		if (formula == null || StringUtils.isBlank(formula.getType()) || StringUtils.isBlank(formula.getExpression()) ) {
			throw new java.lang.IllegalArgumentException("formula data cannot blank!");
		}
		Object resultObj = null;
		Map<String, Object> results = parse(formula.getType(), formula.getReturnMode(), formula.getReturnVar(), 
				formula.getExpression(), getParameter(kpi, data) );
		if (FormulaMode.MODE_CUSTOM.equals(formula.getReturnMode())) {
			resultObj = results.get(formula.getReturnVar());
		} else {
			resultObj = results.get(DEFAULT_RETURN_MODE_VAR);
		}
		return (BigDecimal) resultObj;
	}
	
	private static String handlerExpression(String type, String returnMode, String expression) throws Exception {
		if (StringUtils.isBlank(expression)) {
			return expression;
		}
		String currentExpression = expression;
		if (FormulaMode.MODE_DEFAULT.equals(returnMode)) {
			currentExpression = DEFAULT_RETURN_MODE_VAR + "=" + currentExpression;
		}
		return replaceFormulaExpression(type, currentExpression);
	}	
	
	private static String replaceFormulaExpression(String type, String expression) throws Exception {
		if (StringUtils.isBlank(expression)) {
			return expression;
		}
		String expr = expression;
		expr = StringUtils.replace(expr, "÷", "/");
		expr = StringUtils.replace(expr, "×", "*");
		expr = StringUtils.replace(expr, "−", "-");
		expr = StringUtils.replace(expr, "+", "+");
		/*
		if (!ScriptTypeCode.IS_PYTHON.equals(type)) {
			expr = StringUtils.replace(expr, "abs(", "Math.abs(");
			expr = StringUtils.replace(expr, "sqrt(", "Math.sqrt(");					
		}		
		*/
		expr = StringUtils.replace(expr, FormulaVariable.PAGE_MEASURE_DATA_TARGET, FormulaVariable.PARAM_MEASURE_DATA_TARGET);
		expr = StringUtils.replace(expr, FormulaVariable.PAGE_MEASURE_DATA_ACTUAL, FormulaVariable.PARAM_MEASURE_DATA_ACTUAL);
		expr = StringUtils.replace(expr, FormulaVariable.PAGE_KPI_MAX, FormulaVariable.PARAM_KPI_MAX);
		expr = StringUtils.replace(expr, FormulaVariable.PAGE_KPI_MIN, FormulaVariable.PARAM_KPI_MIN);
		expr = StringUtils.replace(expr, FormulaVariable.PAGE_KPI_TARGET, FormulaVariable.PARAM_KPI_TARGET);
		expr = StringUtils.replace(expr, FormulaVariable.PAGE_KPI_WEIGHT, FormulaVariable.PARAM_KPI_WEIGHT);
		return expr;		
	}
	
}
