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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.qifu.base.Constants;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.core.util.TemplateUtils;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfFormula;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IMeasureDataLogicService;
import org.qifu.hillfog.model.KpiBasicCode;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IFormulaService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IMeasureDataService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.qifu.hillfog.vo.MeasureDataInputBody;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MeasureDataController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	private static final String[][] resourceTables = new String[][]{
		{ MeasureDataCode.FREQUENCY_DAY, 			"META-INF/resource/measure-data-day.ftl" 		},
		{ MeasureDataCode.FREQUENCY_WEEK, 			"META-INF/resource/measure-data-week.ftl"		},
		{ MeasureDataCode.FREQUENCY_MONTH,			"META-INF/resource/measure-data-month.ftl"		},
		{ MeasureDataCode.FREQUENCY_QUARTER,		"META-INF/resource/measure-data-quarter.ftl"	},
		{ MeasureDataCode.FREQUENCY_HALF_OF_YEAR,	"META-INF/resource/measure-data-halfyear.ftl"	},
		{ MeasureDataCode.FREQUENCY_YEAR,			"META-INF/resource/measure-data-year.ftl"		}
	};	
	
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
	
	@Autowired
	IMeasureDataLogicService measureDataLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_md";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("frequencyMap", MeasureDataCode.getFrequencyMap(true));
		HfKpi kpi = (HfKpi) mm.get("kpi");
		mm.put("empInputAutocomplete", pageAutocompleteContent(this.employeeService.findInputAutocompleteByKpiId(kpi.getId())));
		mm.put("orgInputAutocomplete", pageAutocompleteContent(this.orgDeptService.findInputAutocompleteByKpiId(kpi.getId())));
		mm.put("systemDate", this.getNowDate2());
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
	
	private void contentBody(DefaultControllerJsonResultObj<MeasureDataInputBody> result, HttpServletRequest request) throws AuthorityException, ControllerException, ServiceException, Exception {
		MeasureDataInputBody inputBody = new MeasureDataInputBody();
		result.setValue( inputBody );
		String dateStatus = StringUtils.defaultString(request.getParameter("dateStatus"));
		String dateStr = StringUtils.defaultString(request.getParameter("date"));
		String frequency = StringUtils.defaultString(request.getParameter("frequency"));
		String dataFor = KpiBasicCode.DATA_TYPE_BOTH;
		String kpiOrga = StringUtils.defaultString(request.getParameter("kpiOrga"));
		String kpiEmpl = StringUtils.defaultString(request.getParameter("kpiEmpl"));
		String orgId = MeasureDataCode.MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
		String account = MeasureDataCode.MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
		if (!StringUtils.isBlank(dateStatus) && SimpleUtils.isDate(dateStr) && !PleaseSelect.noSelect(frequency)
				&& ("1".equals(dateStatus) || "-1".equals(dateStatus)) ) {
			DateTime dateTime = new DateTime(dateStr);
			if ("1".equals(dateStatus)) { // date +1
				if (MeasureDataCode.FREQUENCY_DAY.equals(frequency) || MeasureDataCode.FREQUENCY_WEEK.equals(frequency) ) { // 上一個月
					dateTime = dateTime.plusMonths(+1);
				} else { // 上一個年
					dateTime = dateTime.plusYears(+1);
				}	
			}
			if ("-1".equals(dateStatus)) { // date -1
				if (MeasureDataCode.FREQUENCY_DAY.equals(frequency) || MeasureDataCode.FREQUENCY_WEEK.equals(frequency) ) { // 下一個月
					dateTime = dateTime.plusMonths(-1);
				} else { // 下一個年
					dateTime = dateTime.plusYears(-1);
				}
			}
			dateStr = dateTime.toString("yyyy-MM-dd");
		}		
		if (!StringUtils.isBlank(kpiOrga)) {
			orgId = StringUtils.deleteWhitespace( kpiOrga.split("/")[0] );
			dataFor = KpiBasicCode.DATA_TYPE_DEPARTMENT;
		}
		if (!StringUtils.isBlank(kpiEmpl)) {
			String val[] = kpiEmpl.split("/");
			if (val.length != 3) {
				throw new ControllerException(BaseSystemMessage.parameterIncorrect());
			}
			account = StringUtils.deleteWhitespace( StringUtils.defaultString(val[1]) );
			dataFor = KpiBasicCode.DATA_TYPE_PERSONAL;
		}
		String content = this.renderBody(
				request.getParameter("kpiOid"), 
				dateStr, 
				frequency, 
				dataFor, 
				orgId, 
				account);
		inputBody.setContent(content);
		inputBody.setDate(dateStr);
		if (!StringUtils.isBlank(content)) {
			result.setSuccess(YES);
		}		
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005M")
	@RequestMapping(value = "/hfMeasureDataBodyJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<MeasureDataInputBody> doContentBody(HttpServletRequest request) {
		DefaultControllerJsonResultObj<MeasureDataInputBody> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.contentBody(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	private void update(DefaultControllerJsonResultObj<Boolean> result, HttpServletRequest request) throws AuthorityException, ControllerException, ServiceException, Exception {
		List<Map<String, String>> fieldDataList = new ArrayList<Map<String, String>>();
		Enumeration<String> reqParam = request.getParameterNames();
		while (reqParam.hasMoreElements()) {
			String paramName = reqParam.nextElement();
			if (paramName.startsWith(MeasureDataCode.MEASURE_DATA_TARGET_ID)) {
				String dateStr = paramName.split(Constants.INPUT_NAME_DELIMITER)[1];
				String actualParamName = MeasureDataCode.MEASURE_DATA_ACTUAL_ID + dateStr;
				Map<String, String> miMap = new HashMap<String, String>();
				miMap.put(paramName, StringUtils.defaultString(request.getParameter(paramName)).trim());
				miMap.put(actualParamName, StringUtils.defaultString(request.getParameter(actualParamName)).trim());
				fieldDataList.add(miMap);
			}
		}
		DefaultResult<Boolean> uResult = this.measureDataLogicService.createOrUpdate(
				request.getParameter("kpiOid"), 
				request.getParameter("frequency"), 
				request.getParameter("date"),
				request.getParameter("dataFor"), 
				request.getParameter("account"),
				request.getParameter("orgId"),
				fieldDataList);
		this.setDefaultResponseJsonResult(result, uResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0005M")
	@RequestMapping(value = "/hfMeasureDataUpdateJson", method = RequestMethod.POST)	
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doUpdate(HttpServletRequest request) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, request);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	private String getTemplateResource(String frequency) {
		String resource = "";
		for (int i=0; i < resourceTables.length; i++) {
			if (resourceTables[i][0].equals(frequency)) {
				resource = resourceTables[i][1];
			}
		}
		return resource;
	}	
	
	private HfKpi findKpi(String oid) throws ServiceException, Exception {
		return this.kpiService.selectByPrimaryKey(oid).getValueEmptyThrowMessage();
	}	
	
	private List<HfMeasureData> findMeasureData(HfKpi kpi, String date, String frequency, String dataFor, String orgId, String account) throws ServiceException, Exception {
		List<HfMeasureData> searchList = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpi.getId());
		paramMap.put("frequency", frequency);
		paramMap.put("orgId", orgId);
		paramMap.put("account", account);
		String queryDate = MeasureDataCode.getQueryDate(date, frequency);
		paramMap.put("dateLike", queryDate+"%");
		searchList = this.measureDataService.selectListByParams(paramMap).getValue();
		if (null == searchList) {
			searchList = new ArrayList<HfMeasureData>();
		}
		return searchList;
	}	
	
	private Map<String, Object> getParameter(HfKpi kpi, String date, String frequency, String dataFor, String orgId, String account) throws ServiceException, Exception {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("date", date);
		parameter.put("frequency", frequency);
		parameter.put("dataFor", dataFor);
		parameter.put("kpi", kpi);
		parameter.put("orgId", orgId);
		parameter.put("account", account);
		parameter.put("masureDatas", findMeasureData(kpi, date, frequency, dataFor, orgId, account) );
		return parameter;
	}	
	
	private void fillParameterForDay(Map<String, Object> parameter) throws Exception {
		String tmp[] = ( (String)parameter.get("date") ).split("-");
		String yyyy = tmp[0];
		String mm = tmp[1];
		String yyyyMM = yyyy+mm;
		int maxday = SimpleUtils.getMaxDayOfMonth(SimpleUtils.getInt(yyyy, 1990), SimpleUtils.getInt(mm, 1) );
		int dayOfWeek = SimpleUtils.getDayOfWeek(SimpleUtils.getInt(yyyy, 1990), SimpleUtils.getInt(mm, 1) );
		int showLen = (maxday+dayOfWeek) / 7;
		if ( (maxday + dayOfWeek) % 7 > 1 ) {
			showLen = showLen + 1;		
		}		
		int previousMonthMaxDay = 0;
		int previousMonth = SimpleUtils.getInt(mm, 1)-1;
		int previousYear = SimpleUtils.getInt(yyyy, 1990);
		if (previousMonth < 1 ) {
			previousYear = previousYear - 1;
			previousMonth = 12;
		}
		previousMonthMaxDay = SimpleUtils.getMaxDayOfMonth(previousYear, previousMonth);
		
		parameter.put("yyyy", yyyy);
		parameter.put("mm", mm);
		parameter.put("yyyyMM", yyyyMM);
		parameter.put("maxday", maxday);
		parameter.put("dayOfWeek", dayOfWeek);
		parameter.put("showLen", showLen);
		parameter.put("previousMonthMaxDay", previousMonthMaxDay);
		parameter.put("previousMonth", previousMonth);
		parameter.put("previousYear", previousYear);
	}
	
	private void fillParameterForWeekOrMonth(Map<String, Object> parameter) throws Exception {
		String tmp[] = ( (String)parameter.get("date") ).split("-");
		String yyyy = tmp[0];
		String mm = tmp[1];
		String yyyyMM = yyyy+mm;
		
		parameter.put("yyyy", yyyy);
		parameter.put("mm", mm);
		parameter.put("yyyyMM", yyyyMM);		
	}
	
	private void fillParameterForQuarterOrYear(Map<String, Object> parameter) throws Exception {
		String tmp[] = ( (String)parameter.get("date") ).split("-");
		String yyyy = tmp[0];
		parameter.put("yyyy", yyyy);		
	}	
	
	private String render(Map<String, Object> parameter, String templateResource) throws Exception {
		return TemplateUtils.processTemplate(
				"resourceTemplate", 
				MeasureDataController.class.getClassLoader(), 
				templateResource, 
				parameter);
	}	
	
	private String renderBody(String kpiOid, String date, String frequency, String dataFor, String orgId, String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(kpiOid) || StringUtils.isBlank(date) || StringUtils.isBlank(frequency) || StringUtils.isBlank(dataFor) ) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		HfKpi kpi = findKpi(kpiOid);
		Map<String, Object> parameter = getParameter(kpi, date, frequency, dataFor, orgId, account);
		if (MeasureDataCode.FREQUENCY_DAY.equals(frequency) ) {
			fillParameterForDay(parameter);
		}
		if (MeasureDataCode.FREQUENCY_WEEK.equals(frequency) || MeasureDataCode.FREQUENCY_MONTH.equals(frequency) ) {
			fillParameterForWeekOrMonth(parameter);
		}
		if (MeasureDataCode.FREQUENCY_QUARTER.equals(frequency) || MeasureDataCode.FREQUENCY_HALF_OF_YEAR.equals(frequency) || MeasureDataCode.FREQUENCY_YEAR.equals(frequency) ) {
			fillParameterForQuarterOrYear(parameter);
		}		
		return render(parameter, getTemplateResource(frequency) );
	}
	
}
