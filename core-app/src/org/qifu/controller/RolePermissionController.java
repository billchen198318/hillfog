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
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.logic.IRoleLogicService;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RolePermissionController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	IRoleService<TbRole, String> roleService;
	
	@Autowired
	IRolePermissionService<TbRolePermission, String> rolePermissionService;
	
	@Autowired
	IRoleLogicService roleLogicService;

	@Override
	public String viewPageNamespace() {
		return "role";
	}

	private void init(String type, HttpServletRequest request, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		Map<String, String> permTypeMap = PleaseSelect.pageSelectMap(true);
		permTypeMap.put("CONTROLLER", "Controller");
		permTypeMap.put("COMPOMENT", "Compoment/Service");
		mm.put("permTypeMap", permTypeMap);
		
		TbRole role = new TbRole();
		this.fillObjectFromRequest(request, role);
		DefaultResult<TbRole> roleResult = this.roleService.selectByEntityPrimaryKey(role);
		if ( roleResult.getValue() == null ) {
			throw new ControllerException( roleResult.getMessage() );
		}
		role = roleResult.getValue();
		mm.put("role", role);
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001S01Q")
	@RequestMapping(value = "/rolePermissionPage")	
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewPageWithNamespace("permission");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("mainPage", request, mm);
			TbRole role = (TbRole) mm.get("role");
			if (Constants.SUPER_ROLE_ADMIN.equals(role.getRole()) || Constants.SUPER_ROLE_ALL.equals(role.getRole())) {
				viewName = this.getWarningPage("Super/Admin role no need set permission!", mm);
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001S01Q")
	@RequestMapping(value = "/rolePermissionQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<TbRolePermission>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<TbRolePermission>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<TbRolePermission>> queryResult = this.rolePermissionService.findPage(
					this.queryParameter(searchValue).addField("role", true).value(), 
					pageOf.orderBy("PERM_TYPE").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}		
	
	private void checkFields(DefaultControllerJsonResultObj<TbRolePermission> result, TbRolePermission rolePermission) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("permission", rolePermission, "@org.apache.commons.lang3.StringUtils@isBlank(permission)", "Permission is blank!")
		.testField("permissionType", ( PleaseSelect.noSelect(rolePermission.getPermType()) ), "Please select type!") 
		.throwMessage();		
	}
	
	private void save(DefaultControllerJsonResultObj<TbRolePermission> result, TbRolePermission rolePermission, String roleOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, rolePermission);
		DefaultResult<TbRolePermission> permResult = this.roleLogicService.createPermission(rolePermission, roleOid);
		this.setDefaultResponseJsonResult(result, permResult);
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbRolePermission rolePermission) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> permResult = this.roleLogicService.deletePermission(rolePermission);
		this.setDefaultResponseJsonResult(result, permResult);
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001S01A")
	@RequestMapping(value = "/rolePermissionSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbRolePermission> doSave(TbRolePermission rolePermission, @RequestParam(name="roleOid") String roleOid) {
		DefaultControllerJsonResultObj<TbRolePermission> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, rolePermission, roleOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG002D0001S01D")
	@RequestMapping(value = "/rolePermissionDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbRolePermission rolePermission) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, rolePermission);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
