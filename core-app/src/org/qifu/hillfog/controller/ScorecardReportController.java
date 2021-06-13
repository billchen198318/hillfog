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

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.PleaseSelect;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.entity.HfScorecard;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.service.IScorecardService;
import org.qifu.hillfog.util.BalancedScorecard;
import org.qifu.hillfog.vo.BscVision;
import org.qifu.hillfog.vo.MeasureDataQueryParam;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ScorecardReportController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IScorecardService<HfScorecard, String> scorecardService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;	
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_scr";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("scorecardMap", this.scorecardService.findMap(true));
		mm.put("frequencyMap", MeasureDataCode.getFrequencyMap(true));
		mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocomplete()));
		mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocomplete()));	
		mm.put("date1", this.getNowDate2());
		mm.put("date2", this.getNowDate2());		
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG005D0001Q")
	@RequestMapping("/hfScorecardReportPage")
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
	
	private void checkFields(DefaultControllerJsonResultObj<BscVision> result, HttpServletRequest request) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("scorecardOid", PleaseSelect.noSelect(request.getParameter("scorecardOid")), "Please select Scorecard(Vision)!")
		.testField("date1", ( !SimpleUtils.isDate(request.getParameter("date1")) ), "Please input start date!")
		.testField("date2", ( !SimpleUtils.isDate(request.getParameter("date2")) ), "Please input end date!")
		.testField("frequency", PleaseSelect.noSelect(request.getParameter("frequency")), "Please select frequency!")
		.throwMessage();		
	}
	
	private void queryContent(DefaultControllerJsonResultObj<BscVision> result, HttpServletRequest request) throws ControllerException, Exception {
		this.checkFields(result, request);
		String scorecardOid = request.getParameter("scorecardOid");
		String frequency = request.getParameter("frequency");
		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		MeasureDataQueryParam queryParam = MeasureDataCode.queryParam(request);
		BscVision bv = BalancedScorecard.build()
				.from(scorecardOid, frequency, date1, date2, queryParam.getAccountId(), queryParam.getOrgId())
				.process().value();
		result.setMessage( BaseSystemMessage.searchNoData() );
		if (bv != null && bv.getPerspectives() != null && bv.getPerspectives().size() > 0) {
			result.setValue(bv);
			result.setSuccess( YES );
			result.setMessage("success!");	
		}
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG005D0001Q")
	@RequestMapping(value = "/hfScorecardReportContentDataJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<BscVision> doQueryContent(HttpServletRequest request) {
		DefaultControllerJsonResultObj<BscVision> result = this.getDefaultJsonResult(this.currentMethodAuthority());
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
