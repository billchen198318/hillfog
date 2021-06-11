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

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.PleaseSelect;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommonQueryController extends BaseControllerSupport {
	
	private static final String TYPE_EMPLOYEE = "emp";
	private static final String TYPE_ORGANIZATION = "org";
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;
	
	private void queryKpiInputAutocomplete(DefaultControllerJsonResultObj<List<String>> result, String type, String kpiOid) throws ServiceException, ControllerException, Exception {
		if (PleaseSelect.noSelect(kpiOid)) {
			if (TYPE_EMPLOYEE.equals(type)) { // employee
				result.setValue( this.employeeService.findInputAutocomplete() );
			} else { // organization
				result.setValue( this.orgDeptService.findInputAutocomplete() );
			}
			return;
		}
		HfKpi kpi = this.kpiService.selectByPrimaryKey(kpiOid).getValueEmptyThrowMessage();
		if (TYPE_EMPLOYEE.equals(type)) { // employee
			result.setValue( this.employeeService.findInputAutocompleteByKpiId(kpi.getId()) );
		} else { // organization
			result.setValue( this.orgDeptService.findInputAutocompleteByKpiId(kpi.getId()) );
		}
	}
	
	private void queryKpiInputAutocompleteBoth(DefaultControllerJsonResultObj<Map<String, List<String>>> result, String kpiOid) throws ServiceException, ControllerException, Exception {
		Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
		if (PleaseSelect.noSelect(kpiOid)) {
			dataMap.put(TYPE_EMPLOYEE, this.employeeService.findInputAutocomplete());
			dataMap.put(TYPE_ORGANIZATION, this.orgDeptService.findInputAutocomplete());
		} else {
			HfKpi kpi = this.kpiService.selectByPrimaryKey(kpiOid).getValueEmptyThrowMessage();
			dataMap.put(TYPE_EMPLOYEE, this.employeeService.findInputAutocompleteByKpiId(kpi.getId()));
			dataMap.put(TYPE_ORGANIZATION, this.orgDeptService.findInputAutocompleteByKpiId(kpi.getId()));
		}
		result.setValue(dataMap);
	}	
	
	private void queryScorecardInputAutocompleteBoth(DefaultControllerJsonResultObj<Map<String, List<String>>> result, String scorecardOid) throws ServiceException, ControllerException, Exception {
		Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
		if (PleaseSelect.noSelect(scorecardOid)) {
			dataMap.put(TYPE_EMPLOYEE, this.employeeService.findInputAutocomplete());
			dataMap.put(TYPE_ORGANIZATION, this.orgDeptService.findInputAutocomplete());
		} else {
			dataMap.put(TYPE_EMPLOYEE, this.employeeService.findInputAutocompleteByScorecard(scorecardOid));
			dataMap.put(TYPE_ORGANIZATION, this.orgDeptService.findInputAutocompleteByScorecard(scorecardOid));
		}
		result.setValue(dataMap);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROGCOMM0001Q")
	@RequestMapping(value = "/hfCommonKpiInputAutocompleteJson")	
	public @ResponseBody DefaultControllerJsonResultObj<Map<String, List<String>>> doQueryKpiInputAutocomplete(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj<Map<String, List<String>>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			this.queryKpiInputAutocompleteBoth(result, oid);
			result.setSuccess(YES);
			result.setMessage( "success!" );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			baseExceptionResult(result, e);		
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROGCOMM0001Q")
	@RequestMapping(value = "/hfCommonKpiEmpInputAutocompleteJson")	
	public @ResponseBody DefaultControllerJsonResultObj<List<String>> doQueryKpiEmpInputAutocomplete(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj<List<String>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			this.queryKpiInputAutocomplete(result, TYPE_EMPLOYEE, oid);
			result.setSuccess(YES);
			result.setMessage( "success!" );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			baseExceptionResult(result, e);		
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}	

	@ControllerMethodAuthority(check = true, programId = "HF_PROGCOMM0001Q")
	@RequestMapping(value = "/hfCommonKpiOrgInputAutocompleteJson")	
	public @ResponseBody DefaultControllerJsonResultObj<List<String>> doQueryKpiOrgInputAutocomplete(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj<List<String>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			this.queryKpiInputAutocomplete(result, TYPE_ORGANIZATION, oid);
			result.setSuccess(YES);
			result.setMessage( "success!" );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			baseExceptionResult(result, e);		
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROGCOMM0001Q")
	@RequestMapping(value = "/hfCommonScorecardInputAutocompleteJson")	
	public @ResponseBody DefaultControllerJsonResultObj<Map<String, List<String>>> doQueryScorecardInputAutocomplete(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj<Map<String, List<String>>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			this.queryScorecardInputAutocompleteBoth(result, oid);
			result.setSuccess(YES);
			result.setMessage( "success!" );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			baseExceptionResult(result, e);		
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}	
	
}
