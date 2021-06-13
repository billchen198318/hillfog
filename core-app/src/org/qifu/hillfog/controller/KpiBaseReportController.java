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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.logic.IPdcaLogicService;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.model.PDCABase;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.service.IPdcaService;
import org.qifu.hillfog.util.KpiScore;
import org.qifu.hillfog.vo.MeasureDataQueryParam;
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
	
	@Autowired
	IPdcaService<HfPdca, String> pdcaService;
	
	@Autowired
	IPdcaLogicService pdcaLogicService;
	
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
		CheckControllerFieldHandler<List<ScoreCalculationData>> checkHandler = this.getCheckControllerFieldHandler(result);
		checkHandler
		.testField("date1", ( !SimpleUtils.isDate(request.getParameter("date1")) ), "Please input start date!")
		.testField("date2", ( !SimpleUtils.isDate(request.getParameter("date2")) ), "Please input end date!")
		.testField("frequency", PleaseSelect.noSelect(request.getParameter("frequency")), "Please select frequency!")
		.throwMessage();
		//.testField("kpiEmpl", ( StringUtils.isBlank(request.getParameter("kpiEmpl")) && StringUtils.isBlank(request.getParameter("kpiOrga")) ), "Please input Organization or Employee!")
		//.testField("kpiOrga", ( StringUtils.isBlank(request.getParameter("kpiEmpl")) && StringUtils.isBlank(request.getParameter("kpiOrga")) ), "Please input Organization or Employee!")		
	}
	
	private void queryContent(DefaultControllerJsonResultObj<List<ScoreCalculationData>> result, HttpServletRequest request) throws ControllerException, Exception {
		this.checkFields(result, request);
		String noPdca = request.getParameter("noPdca"); // Personal board 取 KPIs 用 , noPdca = Y時 - 不需要再抓 PDCA 資料
		String checkKpiOwner = request.getParameter("checkKpiOwner");
		List<HfKpi> kpis = null;
		String kpiOid = request.getParameter("kpiOid");
		if (PleaseSelect.noSelect(kpiOid)) {
			if (YES.equals(checkKpiOwner)) { // checkKpiOwner = Y, Personal board 取 KPIs 用
				kpis = this.kpiService.findKpisByOwnerAccount( request.getParameter("kpiEmpl") ).getValue();
			} else { // KPI base report 取 KPIs 用
				kpis = this.kpiService.selectList("ID", SortType.ASC).getValue();
			}
		} else { // KPI base report 取 KPIs 用 , 有取下拉 KPI 項目
			HfKpi kpi = this.kpiService.selectByPrimaryKey(kpiOid).getValueEmptyThrowMessage();
			kpis = new ArrayList<HfKpi>();
			kpis.add(kpi);
		}
		if (null == kpis || kpis.size() < 1) {
			throw new ControllerException( BaseSystemMessage.dataErrors() );
		}
		MeasureDataQueryParam queryParam = MeasureDataCode.queryParam(request);
		List<ScoreCalculationData> scores = KpiScore.build().add(
				kpis, 
				request.getParameter("frequency"), 
				request.getParameter("date1"), 
				request.getParameter("date2"), 
				queryParam.getAccountId(), 
				queryParam.getOrgId()).processDefault().processDateRange().reduce().valueThrowMessage();
		if (scores != null && scores.size() > 0) {
			if (!YES.equals(noPdca)) {
				this.loadPdca(scores);
			}
			result.setValue(scores);
			result.setSuccess(YES);
			result.setMessage( "success!" );
		} else {
			result.setMessage( BaseSystemMessage.dataErrors() );
		}
	}
	
	private void loadPdca(List<ScoreCalculationData> scores) throws ControllerException, Exception {
		for (ScoreCalculationData data : scores) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("mstType", PDCABase.SOURCE_MASTER_KPI_TYPE);
			paramMap.put("mstOid", data.getKpi().getOid());
			paramMap.put("isNotConfirm", YesNo.YES);
			List<HfPdca> pdcaList = this.pdcaService.selectListByParams(paramMap, "START_DATE", SortType.ASC).getValue();
			if (pdcaList == null || pdcaList.size() < 1) {
				continue;
			}
			for (HfPdca pdca : pdcaList) {
				data.getPdcaItems().add( this.pdcaLogicService.findPdcaItems(pdca, false).getValue() );
			}
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
