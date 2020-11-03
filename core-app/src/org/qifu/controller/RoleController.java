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

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.Constants;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.core.entity.TbRole;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IRoleService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoleController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IRoleService<TbRole, String> roleService;
	
	@Autowired
	IRoleLogicService roleLogicService; 
	
	@Override
	public String viewPageNamespace() {
		return "role";
	}		

	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<TbRole> roleResult = this.roleService.selectByPrimaryKey(oid);
		if ( roleResult.getValue() == null ) {
			throw new ControllerException( roleResult.getMessage() );
		}
		TbRole role = roleResult.getValue();
		mm.put("role", role);
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001Q")
	@RequestMapping(value = "/rolePage")	
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
	
	private void checkFields(DefaultControllerJsonResultObj<TbRole> result, TbRole role) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("role", role, "@org.apache.commons.lang3.StringUtils@isBlank(role)", "Role is blank!")
		.testField("role", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(role.getRole()).replaceAll("-", "").replaceAll("_", "")) ), "Role only normal character!")
		.testField("role", ( PleaseSelect.noSelect(role.getRole()) ), "Please change Role value!") // Role 不能用  "all" 這個下拉值
		.throwMessage();
	}
	
	private void save(DefaultControllerJsonResultObj<TbRole> result, TbRole role) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, role);
		DefaultResult<TbRole> roleResult = this.roleLogicService.create(role);
		this.setDefaultResponseJsonResult(result, roleResult);
	}
	
	private void update(DefaultControllerJsonResultObj<TbRole> result, TbRole role) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, role);
		DefaultResult<TbRole> roleResult = this.roleLogicService.update(role);
		this.setDefaultResponseJsonResult(result, roleResult);
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbRole role) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> roleResult = this.roleLogicService.delete(role);
		this.setDefaultResponseJsonResult(result, roleResult);
	}
	
	private void saveAsNew(DefaultControllerJsonResultObj<TbRole> result, String fromRoleOid, TbRole role) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, role);
		DefaultResult<TbRole> roleResult = this.roleLogicService.copyAsNew(fromRoleOid, role);
		this.setDefaultResponseJsonResult(result, roleResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001Q")
	@RequestMapping(value = "/roleQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<TbRole>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<TbRole>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<TbRole>> queryResult = this.roleService.findPage(
					this.queryParameter(searchValue).fullEquals("role").value(), 
					pageOf.orderBy("ROLE").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001A")
	@RequestMapping(value = "/roleCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001E")
	@RequestMapping(value = "/roleEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("editPage", mm);
			this.fetch(mm, oid);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001S02Q")
	@RequestMapping(value = "/roleCopyPage")
	public String copyPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("copy");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("copyPage", mm);
			this.fetch(mm, oid);
			TbRole role = (TbRole) mm.get("role");
			if (Constants.SUPER_ROLE_ADMIN.equals(role.getRole()) || Constants.SUPER_ROLE_ALL.equals(role.getRole())) {
				viewName = this.getWarningPage("Super/Admin cannot copy as new!", mm);
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001A")
	@RequestMapping(value = "/roleSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbRole> doSave(TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, role);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001E")
	@RequestMapping(value = "/roleUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbRole> doUpdate(TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, role);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001D")
	@RequestMapping(value = "/roleDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbRole role) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, role);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001S02A")
	@RequestMapping(value = "/roleCopySaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbRole> doSaveCopyAsNew(@RequestParam(name="fromRoleOid") String fromRoleOid, TbRole role) {
		DefaultControllerJsonResultObj<TbRole> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.saveAsNew(result, fromRoleOid, role);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
}
