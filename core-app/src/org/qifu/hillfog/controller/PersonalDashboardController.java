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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.core.util.UserUtils;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PersonalDashboardController extends BaseControllerSupport implements IPageNamespaceProvide {

	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Override
	public String viewPageNamespace() {
		return "hillfog_pd";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		HfEmployee employee = new HfEmployee();
		employee.setAccount( this.getAccountId() );
		employee = this.employeeService.selectByUniqueKey(employee).getValueEmptyThrowMessage();
		mm.put("frequencyMap", MeasureDataCode.getFrequencyMap(true));
		List<String> empList = null;
		if (UserUtils.isAdmin()) {
			empList = this.employeeService.findInputAutocomplete();	
		} else {
			empList = new ArrayList<String>();
			empList.add( this.employeeService.getPagefieldValue(employee) );				
		}
		if (CollectionUtils.isEmpty(empList)) {
			throw new ControllerException( BaseSystemMessage.dataErrors() );
		}
		mm.put("employee", employee);
		mm.put("employeeSelect", this.employeeService.getPagefieldValue(employee));
		mm.put("empList", empList);
		mm.put("empInputAutocomplete", pageAutocompleteContent(empList));		
		
		String currentYear = SimpleUtils.getStrYMD(SimpleUtils.IS_YEAR);
		String currentMonth = SimpleUtils.getStrYMD(SimpleUtils.IS_MONTH);
		
		// Year
		mm.put("startDateY", currentYear + "-01-01");
		mm.put("endDateY", currentYear + "-12-" + SimpleUtils.getMaxDayOfMonth(Integer.parseInt(currentYear), Integer.parseInt("12")));
		
		// Half of year
		if (Integer.parseInt(currentMonth) > 6) { // 下半年
			mm.put("startDateH", currentYear + "-07-01");
			mm.put("endDateH", currentYear + "-12-" + SimpleUtils.getMaxDayOfMonth(Integer.parseInt(currentYear), Integer.parseInt("12")));			
		} else { // 上半年
			mm.put("startDateH", currentYear + "-01-01");
			mm.put("endDateH", currentYear + "-06-" + SimpleUtils.getMaxDayOfMonth(Integer.parseInt(currentYear), Integer.parseInt("6")));
		}
		
		// Quarter
		Map<String, String> quarterMap = SimpleUtils.getQuarterlyStartEnd(SimpleUtils.getStrYMD(""));
		mm.put("startDateQ", quarterMap.get("date1").replaceAll("/", "-"));
		mm.put("endDateQ", quarterMap.get("date2").replaceAll("/", "-"));		
		
		// Month
		mm.put("startDateM", currentYear + "-" + currentMonth + "-01");
		mm.put("endDateM", currentYear + "-" + currentMonth + "-" + SimpleUtils.getMaxDayOfMonth(Integer.parseInt(currentYear), Integer.parseInt(currentMonth)));		
		
	}
	
	@ControllerMethodAuthority(check = true, programId = "HF_PROG001D0007Q")
	@RequestMapping("/hfPersonalDashboardPage")
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
	
}
