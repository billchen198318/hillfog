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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IFormulaService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IMeasureDataService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MeasureDataController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Autowired
	IKpiService<HfKpi, String> kpiService;	
	
	@Autowired
	IMeasureDataService<HfMeasureData, String> measureDataService;	
	
	@Autowired
	IFormulaService<HfFormula, String> formulaService;	
	
	@Autowired
	IAggregationMethodService<HfAggregationMethod, String> aggregationMethodService;		
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;	
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_md";
	}
	
	private String pageAutocompleteContent(List<String> orgList) {
		if (null == orgList || orgList.size() < 1) {
			return "";
		}
		StringBuilder orgListJsStr = new StringBuilder();
		for (int i = 0; null != orgList && i < orgList.size(); i++) {
			orgListJsStr.append( "'" + orgList.get(i) + "'" );
			if ((i+1) < orgList.size()) {
				orgListJsStr.append(",");
			}
		}
		return orgListJsStr.toString();
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("frequencyMap", MeasureDataCode.getFrequencyMap(true));
		HfKpi kpi = (HfKpi) mm.get("kpi");
		mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocompleteByKpiId(kpi.getId())));
		mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocompleteByKpiId(kpi.getId())));
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfKpi kpi = this.kpiService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		mm.put("kpi", kpi);		
		HfFormula formula = new HfFormula();
		formula.setForId( kpi.getForId() );
		formula = this.formulaService.selectByUniqueKey(formula).getValueEmptyThrowMessage();
		mm.put("formula", formula);
		HfAggregationMethod aggrMethod = new HfAggregationMethod();
		aggrMethod.setAggrId( kpi.getAggrId() );
		aggrMethod = this.aggregationMethodService.selectByUniqueKey(aggrMethod).getValueEmptyThrowMessage();
		mm.put("aggrMethod", aggrMethod);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005M")
	@RequestMapping("/hfMeasureDataPage")
	public String mainPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetch(mm, oid);
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005M")
	@RequestMapping(value = "/hfMeasureDataUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doUpdate(HttpServletRequest request, HfKpi kpi) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			/*
			this.update(
					result, 
					kpi, 
					request.getParameter("forOid"), 
					request.getParameter("aggrOid"), 
					request.getParameter("selKpiDept"), 
					request.getParameter("selKpiEmp"));
			*/
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
}
