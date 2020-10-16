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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.SortType;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.util.KpiScore;
import org.qifu.hillfog.vo.ScoreCalculationData;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KpiBaseReportController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_kbr";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("frequencyMap", MeasureDataCode.getFrequencyMap(true));
		mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocomplete()));
		mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocomplete()));	
		mm.put("kpiMap", this.kpiService.findMap(true));
		mm.put("date1", this.getNowDate2());
		mm.put("date2", this.getNowDate2());
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG002D0001Q")
	@RequestMapping("/hfKpiReportPage")
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
	
	private void checkFields(DefaultControllerJsonResultObj<List<ScoreCalculationData>> result, HttpServletRequest request) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("date1", ( !SimpleUtils.isDate(request.getParameter("date1")) ), "Please input start date!")
		.testField("date2", ( !SimpleUtils.isDate(request.getParameter("date2")) ), "Please input end date!")
		.testField("frequency", PleaseSelect.noSelect(request.getParameter("frequency")), "Please select frequency!")
		.testField("kpiEmpl", ( StringUtils.isBlank(request.getParameter("kpiEmpl")) && StringUtils.isBlank(request.getParameter("kpiOrga")) ), "Please input Organization or Employee!")
		.testField("kpiOrga", ( StringUtils.isBlank(request.getParameter("kpiEmpl")) && StringUtils.isBlank(request.getParameter("kpiOrga")) ), "Please input Organization or Employee!")		
		.throwMessage();
	}
	
	private void queryContent(DefaultControllerJsonResultObj<List<ScoreCalculationData>> result, HttpServletRequest request) throws ControllerException, Exception {
		this.checkFields(result, request);
		List<HfKpi> kpis = null;
		String kpiOid = request.getParameter("kpiOid");
		if (PleaseSelect.noSelect(kpiOid)) {
			kpis = this.kpiService.selectList("ID", SortType.ASC).getValue();
		} else {
			HfKpi kpi = this.kpiService.selectByPrimaryKey(kpiOid).getValueEmptyThrowMessage();
			kpis = new ArrayList<HfKpi>();
			kpis.add(kpi);
		}
		if (null == kpis || kpis.size() < 1) {
			throw new ControllerException( BaseSystemMessage.dataErrors() );
		}
		String accountId = MeasureDataCode.MEASURE_DATA_EMPLOYEE_FULL;
		String orgId = MeasureDataCode.MEASURE_DATA_ORGANIZATION_FULL;
		if (!StringUtils.isBlank(request.getParameter("kpiEmpl"))) {
			accountId = request.getParameter("kpiEmpl");
			String tmp[] = accountId.split("/");
			if (tmp == null || tmp.length != 3) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
			accountId = StringUtils.deleteWhitespace(tmp[1]);
			if (StringUtils.isBlank(accountId)) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
		}
		if (!StringUtils.isBlank(request.getParameter("kpiOrga"))) {
			orgId = request.getParameter("kpiOrga");
			orgId = StringUtils.deleteWhitespace(orgId.split("/")[0]);
		}
		List<ScoreCalculationData> scores = KpiScore.build().add(
				kpis, 
				request.getParameter("frequency"), 
				request.getParameter("date1"), 
				request.getParameter("date2"), 
				accountId, 
				orgId).processDefault().processDateRange().reduce().valueThrowMessage();
		if (scores != null && scores.size() > 0) {
			result.setValue(scores);
			result.setSuccess(YES);
			result.setMessage( "success!" );
		} else {
			result.setMessage( BaseSystemMessage.dataErrors() );
		}
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG002D0001Q")
	@RequestMapping(value = "/hfKpiReportContentDataJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<List<ScoreCalculationData>> doQueryContent(HttpServletRequest request) {
		DefaultControllerJsonResultObj<List<ScoreCalculationData>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.queryContent(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
}
