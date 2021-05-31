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
		if (empList == null || empList.size() < 1) {
			throw new ControllerException( BaseSystemMessage.dataErrors() );
		}
		mm.put("employee", employee);
		mm.put("empList", empList);
		mm.put("empInputAutocomplete", pageAutocompleteContent(empList));			
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
