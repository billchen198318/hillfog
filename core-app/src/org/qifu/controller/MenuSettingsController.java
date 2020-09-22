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
import org.qifu.base.model.PleaseSelect;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.ISystemMenuLogicService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuSettingsController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISystemMenuLogicService systemMenuLogicService;	
	
	@Override
	public String viewPageNamespace() {
		return "menu_set";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put( "sysMap", this.sysService.findSysMap(true) );
		mm.put("folderProgMap", PleaseSelect.pageSelectMap(true));
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0003Q")
	@RequestMapping(value = "/menuSettingsPage")	
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("mainPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ServiceException | ControllerException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0003Q")
	@RequestMapping(value = "/menuSettingsQueryProgramListByFolderOidJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj< Map<String, List<TbSysProg>> > queryProgramListByFolderOid(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj< Map<String, List<TbSysProg>> > result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}		
		try {
			Map<String, List<TbSysProg>> searchDataMap = this.systemMenuLogicService.findForMenuSettingsEnableAndAll(oid);
			result.setValue( searchDataMap );
			result.setSuccess( YES );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}		
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0003Q")
	@RequestMapping(value = "/menuSettingsUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> updateMenu(HttpServletRequest request, @RequestParam(name="folderProgramOid") String folderProgramOid, @RequestParam(name="appendOid") String appendOid) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			DefaultResult<Boolean> updateResult = this.systemMenuLogicService.createOrUpdate(folderProgramOid, this.transformAppendKeyStringToList(appendOid));
			if (updateResult.getValue() != null && updateResult.getValue()) {
				result.setSuccess(YES);
			}
			result.setMessage( updateResult.getMessage() );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}
