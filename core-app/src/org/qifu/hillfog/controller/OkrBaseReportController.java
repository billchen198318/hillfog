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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.model.PDCABase;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.service.IPdcaService;
import org.qifu.hillfog.util.OkrProgressRateUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OkrBaseReportController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;	
	
	@Autowired
	IPdcaService<HfPdca, String> pdcaService;	
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_obr";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("mainPage".equals(type)) {
			mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocomplete()));
			mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocomplete()));			
		}
		if ("detailPage".equals(type)) {
			HfObjective objective = (HfObjective) mm.get("objective");
			mm.put("selOrgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocompleteByObjectiveOid(objective.getOid())));
			mm.put("selEmpInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocompleteByObjectiveOid(objective.getOid())));				
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfObjective objective = this.objectiveService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
		OkrProgressRateUtils.build().fromObjective(objective).process().loadInitiatives();
		mm.put("objective", objective);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mstType", PDCABase.SOURCE_MASTER_OBJECTIVE_TYPE);
		paramMap.put("mstOid", objective.getOid());
		paramMap.put("isNotConfirm", YesNo.YES);
		List<HfPdca> pdcaList = this.pdcaService.selectListByParams(paramMap, "START_DATE", SortType.ASC).getValue();
		mm.put("pdcaList", pdcaList);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG003D0001Q")
	@RequestMapping("/hfOkrReportPage")
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
	
	private void queryObjectives(DefaultControllerJsonResultObj<List<HfObjective>> result, HttpServletRequest request) throws ServiceException, Exception {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String departmentId = StringUtils.defaultString(request.getParameter("departmentId"));
		String accountId = StringUtils.defaultString(request.getParameter("ownerAccount"));
		if (!SimpleUtils.isDate(startDate) || !SimpleUtils.isDate(endDate)) {
			startDate = "";
			endDate = "";
		}
		startDate = startDate.replaceAll("-", "").replaceAll("/", "");
		endDate = endDate.replaceAll("-", "").replaceAll("/", "");
		departmentId = StringUtils.deleteWhitespace(departmentId.split("/")[0]);
		String tmp[] = accountId.split("/");
		if (tmp != null && tmp.length >= 3) {
			accountId = StringUtils.deleteWhitespace(tmp[1]);
		}
		String name = StringUtils.defaultString(request.getParameter("name"));
		DefaultResult<List<HfObjective>> qResult = this.objectiveService.selectQueryObjectiveList(accountId, departmentId, startDate, endDate, name);
		for (int i = 0; qResult.getValue() != null && i < qResult.getValue().size(); i++) {
			OkrProgressRateUtils.build().fromObjective(qResult.getValue().get(i)).process();
		}
		this.setDefaultResponseJsonResult(result, qResult);
		result.setSuccess(YES);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG003D0001Q")
	@RequestMapping(value = "/hfOkrReportQueryJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<List<HfObjective>> doQuery(HttpServletRequest request) {
		DefaultControllerJsonResultObj<List<HfObjective>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.queryObjectives(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG003D0001Q01D")
	@RequestMapping("/hfOkrReportDetailPage")
	public String detailPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("detail-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetch(mm, oid);
			this.init("detailPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
}
