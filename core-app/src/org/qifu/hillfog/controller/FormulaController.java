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
package org.qifu.hillfog.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.ScriptTypeCode;
import org.qifu.base.model.SearchValue;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.logic.IFormulaLogicService;
import org.qifu.hillfog.model.FormulaMode;
import org.qifu.hillfog.service.IFormulaService;
import org.qifu.hillfog.util.FormulaUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FormulaController extends BaseControllerSupport implements IPageNamespaceProvide {
	private static final int MAX_LENGTH = 500;
	
	@Autowired
	IFormulaService<HfFormula, String> formulaService;
	
	@Autowired
	IFormulaLogicService formulaLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_for";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			mm.put("typeMap", ScriptTypeCode.getTypeMap(true));
			mm.put("returnModeMap", FormulaMode.getReturnModeMap(false));
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfFormula formula = this.formulaService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("formula", formula);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003Q")
	@RequestMapping("/hfFormulaPage")
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("mainPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003A")
	@RequestMapping("/hfFormulaCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("createPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003E")
	@RequestMapping("/hfFormulaEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetch(mm, oid);
			this.init("editPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003Q")
	@RequestMapping(value = "/hfFormulaQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<HfFormula>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<HfFormula>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<HfFormula>> queryResult = this.formulaService.findPage(
					this.queryParameter(searchValue).fullEquals("forId").fullLink("nameLike").value(), 
					pageOf.orderBy("FOR_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003A")
	@RequestMapping(value = "/hfFormulaTestJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Object> doTest(HttpServletRequest request, HfFormula formula) {
		DefaultControllerJsonResultObj<Object> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.testFormula(result, request, formula);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	private void checkFields(DefaultControllerJsonResultObj<HfFormula> result, HfFormula formula) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("forId", formula, "@org.apache.commons.lang3.StringUtils@isBlank(forId)", "Id is blank!")
		.testField("forId", ( PleaseSelect.noSelect(formula.getForId()) ), "Please change Id value!") // FOR_ID 不能用  "all" 這個下拉值
		.testField("forId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(formula.getForId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("name", formula, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.throwMessage();
	}		
	
	private void testFormula(DefaultControllerJsonResultObj<Object> result, HttpServletRequest request, HfFormula formula) throws ControllerException, AuthorityException, ServiceException, Exception {
		CheckControllerFieldHandler<Object> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("type", PleaseSelect.noSelect(formula.getType()), "Please select type!" )
		.testField("returnMode", PleaseSelect.noSelect(formula.getReturnMode()), "Please select return mode!" )
		.testField("returnVar", ( FormulaMode.MODE_CUSTOM.equals(formula.getReturnMode()) && StringUtils.isBlank(formula.getReturnVar()) ), "Return variable is blank!" )
		.testField("expression", formula, "@org.apache.commons.lang3.StringUtils@isBlank(expression)", "Formula content is blank!");
		
		String testMeasureActual = request.getParameter("testMeasureActual");
		String testMeasureTarget = request.getParameter("testMeasureTarget");
		if (!NumberUtils.isCreatable(testMeasureActual)) {
			checkHandler.throwMessage("testMeasureActual", "Test actual value error!");
		}
		if (!NumberUtils.isCreatable(testMeasureTarget)) {
			checkHandler.throwMessage("testMeasureTarget", "Test target value error!");
		}
		
		checkHandler.throwMessage();
		
		HfKpi kpi = new HfKpi();
		kpi.setId("T"+System.currentTimeMillis());
		kpi.setMax(BigDecimal.valueOf(100));
		kpi.setMin(BigDecimal.valueOf(-100));
		kpi.setTarget(BigDecimal.valueOf(80));
		kpi.setWeight(BigDecimal.valueOf(50));
		
		HfMeasureData measureData = new HfMeasureData();
		measureData.setActual( new BigDecimal(testMeasureActual) );
		measureData.setTarget( new BigDecimal(testMeasureTarget) );
		
		Object retValue = FormulaUtils.parse(formula, kpi, measureData);
		if (null != retValue) {
			result.setMessage( "Calculation success: " + retValue );
			result.setValue(retValue);
			result.setSuccess(YES);
		} else {
			result.setMessage( "Calculation fail!" );
		}
	}
	
	private void save(DefaultControllerJsonResultObj<HfFormula> result, HttpServletRequest request, HfFormula formula) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, formula);
		this.testFormula((DefaultControllerJsonResultObj)result, request, formula);
		if (StringUtils.defaultString(formula.getDescription()).length() > MAX_LENGTH) {
			formula.setDescription( formula.getDescription().substring(0, MAX_LENGTH) );
		}
		DefaultResult<HfFormula> iResult = this.formulaService.insert(formula);
		if (iResult.getValue() != null) {
			result.setValue( iResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( iResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003A")
	@RequestMapping(value = "/hfFormulaSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfFormula> doSave(HttpServletRequest request, HfFormula formula) {
		DefaultControllerJsonResultObj<HfFormula> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, request, formula);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private void update(DefaultControllerJsonResultObj<HfFormula> result, HttpServletRequest request, HfFormula formula) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, formula);
		this.testFormula((DefaultControllerJsonResultObj)result, request, formula);
		if (StringUtils.defaultString(formula.getDescription()).length() > MAX_LENGTH) {
			formula.setDescription( formula.getDescription().substring(0, MAX_LENGTH) );
		}
		DefaultResult<HfFormula> uResult = this.formulaService.update(formula);
		if ( uResult.getValue() != null ) {
			result.setValue( uResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( uResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003E")
	@RequestMapping(value = "/hfFormulaUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<HfFormula> doUpdate(HttpServletRequest request, HfFormula formula) {
		DefaultControllerJsonResultObj<HfFormula> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, request, formula);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, HfFormula formula) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.formulaLogicService.delete(formula);
		if (dResult.getValue() != null) {
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0003D")
	@RequestMapping(value = "/hfFormulaDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)			
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(HfFormula formula) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, formula);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
