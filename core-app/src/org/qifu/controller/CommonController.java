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

import org.apache.commons.text.StringEscapeUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.model.MenuItemType;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommonController extends BaseControllerSupport {
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	private TbSys findSys(String oid) throws ControllerException, ServiceException, Exception {
		DefaultResult<TbSys> sysResult = this.sysService.selectByPrimaryKey(oid);
		if (sysResult.getValue() == null) {
			throw new ControllerException( sysResult.getMessage() );
		}
		return sysResult.getValue();
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROGCOMM0001Q")
	@RequestMapping(value = "/getCommonProgramFolderJson")	
	public @ResponseBody DefaultControllerJsonResultObj<Map<String, String>> doQueryProgramFolder(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj<Map<String, String>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			result.setValue( PleaseSelect.pageSelectMap(true) );
			return result;
		}		
		try {
			TbSys sys = this.findSys(oid);
			result.setValue( this.sysProgService.findSysProgFolderMap(this.getBasePath(request), sys.getSysId(), MenuItemType.FOLDER, true) );
			result.setSuccess( YES );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			baseExceptionResult(result, e);		
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROGCOMM0002Q")
	@RequestMapping(value = "/getCommonProgramFolderMenuItemJson")	
	public @ResponseBody DefaultControllerJsonResultObj<Map<String, String>> doQueryProgramList(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		DefaultControllerJsonResultObj<Map<String, String>> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			result.setValue( PleaseSelect.pageSelectMap(true) );
			return result;
		}		
		try {
			TbSys sys = this.findSys(oid);
			List<TbSysProg> menuProgList = this.sysProgService.findForInTheFolderMenuItems(sys.getSysId(), null, null);
			Map<String, String> dataMap = PleaseSelect.pageSelectMap(true);
			for (int i=0; menuProgList!=null && i<menuProgList.size(); i++) {
				TbSysProg sysProg = menuProgList.get(i);
				dataMap.put(sysProg.getOid(), StringEscapeUtils.escapeHtml4(sysProg.getName()));
			}
			result.setValue( dataMap );
			result.setSuccess( YES );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			baseExceptionResult(result, e);
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}	
	
}
