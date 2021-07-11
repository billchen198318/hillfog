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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
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
import org.qifu.base.model.ZeroKeyProvide;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfEmployeeHier;
import org.qifu.hillfog.entity.HfObjOwner;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.model.EmployeeHierObjective;
import org.qifu.hillfog.model.PDCABase;
import org.qifu.hillfog.service.IEmployeeHierService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IObjOwnerService;
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

import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	IEmployeeHierService<HfEmployeeHier, String> employeeHierService;
	
	@Autowired
	IObjOwnerService<HfObjOwner, String> objOwnerService;
	
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
		if ("hierarchyPage".equals(type)) {
			this.okrHierarchyData(mm);
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
			mm.put("defaultQuerySelectEmployee", "");
			if (!StringUtils.isBlank(request.getParameter("oid"))) { // 由 hfOkrHierarchyViewPage 點選員工node帶入的 HF_EMPLOYEE.OID
				HfEmployee employee = this.employeeService.selectByPrimaryKey( request.getParameter("oid") ).getValueEmptyThrowMessage();
				mm.put("defaultQuerySelectEmployee", this.employeeService.getPagefieldValue(employee));
			}
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG003D0001H")
	@RequestMapping("/hfOkrHierarchyViewPage")
	public String hierarchyPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPageWithNamespace("hierarchy-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("hierarchyPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	private void okrHierarchyData(ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		String varName = "datascourceJsonData";
		List<HfEmployeeHier> empHierList = this.employeeHierService.selectList().getValue();
		if (null == empHierList || empHierList.size() < 1) {
			mm.put(varName, "{ }");
			return;
		}
		List<HfObjective> objectivesList = this.objectiveService.selectList().getValue();
		if (null == objectivesList || objectivesList.size() < 1) {
			mm.put(varName, "{ }");
			return;			
		}
		Map<String, EmployeeHierObjective> employeeObjectivesMap = new HashMap<String, EmployeeHierObjective>();
		Map<String, String> okrProcessMap = new HashMap<String, String>();
		for (HfEmployeeHier h : empHierList) {
			HfEmployee e = this.employeeService.selectByPrimaryKey( h.getEmpOid() ).getValueEmptyThrowMessage();
			List<HfObjective> userObjectiveList = new ArrayList<HfObjective>();
			EmployeeHierObjective eho = new EmployeeHierObjective(h, e, userObjectiveList);
			employeeObjectivesMap.put(h.getEmpOid(), eho);
			for (HfObjective o : objectivesList) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("objOid", o.getOid());
				paramMap.put("account", e.getAccount());
				if (this.objOwnerService.count(paramMap) < 1) {
					continue;
				}
				if (okrProcessMap.get(o.getOid()) == null) {
					OkrProgressRateUtils.build().fromObjective(o).process();
					okrProcessMap.put(o.getOid(), YesNo.YES);
				}
				eho.getObjectives().add(o);
			}
		}
		
		int hasOkrEmployeeSize = 0;
		BigDecimal topTotalProgressPercentage = BigDecimal.ZERO;
		for (Map.Entry<String, EmployeeHierObjective> entry : employeeObjectivesMap.entrySet()) {
			if (entry.getValue().getObjectives().size() < 1) {
				continue;
			}
			hasOkrEmployeeSize += 1;
			topTotalProgressPercentage = topTotalProgressPercentage.add( entry.getValue().totalProgressPercentage() );
		}
		if ( topTotalProgressPercentage.floatValue() != 0.0f && hasOkrEmployeeSize > 0 ) {
			topTotalProgressPercentage = topTotalProgressPercentage.divide(new BigDecimal(hasOkrEmployeeSize), 2, RoundingMode.HALF_UP);
		}
		
		Map<String, Object> datascourceMap = new HashMap<String, Object>();
		
		// first node
		List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
		datascourceMap.put("oid", ZeroKeyProvide.OID_KEY);
		datascourceMap.put("name", "Employee hierarchy");
		datascourceMap.put("title", "Total<br><img src=\"./images/logo.png\" class=\"rounded-circle\" style=\"max-height:96px;max-width:96px;\"><br>OKRs process&nbsp;(" + topTotalProgressPercentage + "%)<br><div class=\"progress\"><div class=\"progress-bar bg-success\" role=\"progressbar\" style=\"width: " + topTotalProgressPercentage + "%\" aria-valuenow=\"" + topTotalProgressPercentage + "\" aria-valuemin=\"0\" aria-valuemax=\"100\"></div></div>");
		datascourceMap.put("children", childrenList);
		datascourceMap.put("collapsed", false);
		for (Map.Entry<String, EmployeeHierObjective> entry : employeeObjectivesMap.entrySet()) {
			if (ZeroKeyProvide.OID_KEY.equals( entry.getValue().getEmployeeHier().getParentOid() )) {
				EmployeeHierObjective eho = entry.getValue();
				HfEmployee e = eho.getEmployee();
				Map<String, Object> childMap = new HashMap<String, Object>();
				childrenList.add(childMap);
				this.fillChildNodeData(childMap, e, eho.getObjectives().size(), eho.totalProgressPercentage());
			}
		}
		for (Map<String, Object> firstLeveLChildMap : childrenList) {
			this.fillChildHierarchyData(firstLeveLChildMap, employeeObjectivesMap, empHierList);
		}
		
		String json = new ObjectMapper().writeValueAsString(datascourceMap);
		mm.put(varName, json);
		
	}
	
	private void fillChildHierarchyData(Map<String, Object> childrenDataMap, Map<String, EmployeeHierObjective> employeeObjectivesMap, List<HfEmployeeHier> empHierList) throws AuthorityException, ControllerException, ServiceException, Exception {
		String currentEmpOid = (String) childrenDataMap.get("oid");
		for (HfEmployeeHier h : empHierList) {
			if (h.getParentOid().equals(currentEmpOid)) {
				childrenDataMap.put("collapsed", false);
				EmployeeHierObjective eho = employeeObjectivesMap.get(h.getEmpOid());
				HfEmployee e = eho.getEmployee();
				List<Map<String, Object>> childrenList = null;
				if (childrenDataMap.get("children") == null) {
					childrenList = new ArrayList<Map<String, Object>>();
					childrenDataMap.put("children", childrenList);
				} else {
					childrenList = (List<Map<String, Object>>) childrenDataMap.get("children");
				}
				
				Map<String, Object> childMap = new HashMap<String, Object>();
				childrenList.add(childMap);
				this.fillChildNodeData(childMap, e, eho.getObjectives().size(), eho.totalProgressPercentage());		
				this.fillChildHierarchyData(childMap, employeeObjectivesMap, empHierList);
			}
		}
	}
	
	private void fillChildNodeData(Map<String, Object> childMap, HfEmployee e, int okrSize, BigDecimal progressPercentage) {
		String title = "";
		if (!StringUtils.isBlank(e.getJobTitle())) {
			title += StringEscapeUtils.escapeJson( e.getJobTitle() );
		} else {
			title += "(no title)";
		}
		String imgSrc = "<img src=\"./images/logo.png\" class=\"rounded-circle\" style=\"max-height:96px;max-width:96px;\">";
		if (!StringUtils.isBlank( e.getUploadOid() )) {
			imgSrc = "<img src=\"./commonViewFile?oid=" + e.getUploadOid() + "\" class=\"rounded-circle\" style=\"max-height:96px;max-width:96px;\">";
		}				
		if (okrSize < 1) {
			title += "<br>" + imgSrc + "<br>(no objecitves)";
		} else {
			title += "<br>" + imgSrc + "<br>OKRs progress&nbsp;(" + progressPercentage + "%)<br><div class=\"progress\"><div class=\"progress-bar bg-success\" role=\"progressbar\" style=\"width: " + progressPercentage + "%\" aria-valuenow=\"" + progressPercentage + "\" aria-valuemin=\"0\" aria-valuemax=\"100\"></div></div>";
		}
		childMap.put("oid", e.getOid());
		childMap.put("name", StringEscapeUtils.escapeJson(e.getEmpId() + " - " + e.getName()));
		childMap.put("title", title);		
		childMap.put("collapsed", false);
	}
	
}
