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
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.model.PDCABase;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IPdcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PdcaController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;	
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;	
	
	@Autowired
	IPdcaService<HfPdca, String> pdcaService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_pdca";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type)) {
			if (mm.get("masterType") != null) {
				this.generatePdcaNumber(mm);
			}
		}
		if ("createPage".equals(type) || "editPage".equals(type)) {
			mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocomplete()));			
		}		
	}
	
	private void generatePdcaNumber(ModelMap mm) throws ServiceException, Exception {
		String head = "OKR";
		if (mm.get("kpi") != null) {
			HfKpi kpi = (HfKpi) mm.get("kpi");
			head = kpi.getId();
		}
		mm.put("pdcaNum", this.pdcaService.selectMaxPdcaNum(head));
	}
	
	private void fetchForCreatePage(ModelMap mm, String oid, String masterType) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("masterType", masterType);
		mm.put("oid", oid);
		if (PDCABase.SOURCE_MASTER_KPI_TYPE.equals(masterType)) {
			HfKpi kpi = kpiService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
			mm.put("kpi", kpi);
		} else if (PDCABase.SOURCE_MASTER_OBJECTIVE_TYPE.equals(masterType)) {
			HfObjective objective = objectiveService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
			mm.put("objective", objective);			
		} else {
			throw new ControllerException("args type error!");
		}
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001Q")
	@RequestMapping("/hfPdcaPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001A")
	@RequestMapping("/hfPdcaCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String sourceMasterOid, @RequestParam(name="masterType") String masterType) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.fetchForCreatePage(mm, sourceMasterOid, masterType);
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001E")
	@RequestMapping("/hfPdcaEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG004D0001V")
	@RequestMapping("/hfPdcaViewDetailPage")
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
