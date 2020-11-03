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
package org.qifu.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.entity.TbRole;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserRoleController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IAccountService<TbAccount, String> accountService;
	
	@Autowired
	IRoleLogicService roleLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "role_misc";
	}	

	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put("accountMap", this.accountService.findForAllMap(true));
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0002Q")
	@RequestMapping(value = "/userRolePage")	
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPageWithNamespace("user-role-page");
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0002Q")
	@RequestMapping(value = "/userRoleListByAccountOidJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj< Map<String, List<TbRole>> > queryRoleListByAccountOid(HttpServletRequest request, @RequestParam(name="accountOid") String accountOid) {
		DefaultControllerJsonResultObj< Map<String, List<TbRole>> > result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			Map<String, List<TbRole>> searchDataMap = this.roleLogicService.findForAccountRoleEnableAndAll(accountOid);
			result.setValue(searchDataMap);
			result.setSuccess( YES );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}		
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0002Q")
	@RequestMapping(value = "/userRoleUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> updateMenu(HttpServletRequest request, @RequestParam(name="accountOid") String accountOid, @RequestParam(name="appendOid") String appendOid) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			DefaultResult<Boolean> updateResult = this.roleLogicService.updateUserRole(accountOid, this.transformAppendKeyStringToList(appendOid));
			this.setDefaultResponseJsonResult(result, updateResult);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
}
