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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysIcon;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.ISystemProgramLogicService;
import org.qifu.core.model.MenuItemType;
import org.qifu.core.service.ISysIconService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.qifu.core.util.IconUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysProgramController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysIconService<TbSysIcon, String> sysIconService;
	
	@Autowired
	ISystemProgramLogicService systemProgramLogicService;
	
	public SysProgramController() {
		super();
	}
	
	@Override
	public String viewPageNamespace() {
		return "sys_prog";
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		mm.put( "sysMap", this.sysService.findSysMap(true) );
		mm.put( "iconMap", IconUtils.getIconsSelectData() );		
	}
	
	private void fetch(ModelMap mm, String oid) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<TbSysProg> result = this.sysProgService.selectByPrimaryKey(oid);
		if (result.getValue() == null) {
			throw new ControllerException(result.getMessage());
		}
		TbSysProg sysProg = result.getValue();
		mm.put("sysProg", sysProg);
		
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setIconId(sysProg.getIcon());
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.selectByUniqueKey(sysIcon);
		if (iconResult.getValue() == null) {
			throw new ControllerException( iconResult.getMessage() );
		}
		sysIcon = iconResult.getValue();		
		mm.put("iconSelectOid", sysIcon.getOid());		
		
		TbSys sys = new TbSys();
		sys.setSysId(sysProg.getProgSystem());
		DefaultResult<TbSys> sysResult = this.sysService.selectByUniqueKey(sys);
		if (sysResult.getValue() == null) {
			throw new ControllerException( sysResult.getMessage() );
		}
		sys = sysResult.getValue();
		mm.put("sysSelectOid", sys.getOid());
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002Q")
	@RequestMapping("/sysProgramPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002A")
	@RequestMapping("/sysProgramCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002E")
	@RequestMapping("/sysProgramEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002Q")
	@RequestMapping(value = "/sysProgramQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<TbSysProg>> queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj<List<TbSysProg>> result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult<List<TbSysProg>> queryResult = this.sysProgService.findPage(
					this.queryParameter(searchValue).fullEquals("progId").fullLink("name").value(), 
					pageOf);
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}		
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysProg> result, TbSysProg sysProg, String sysOid, String iconOid, String w, String h) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("progSystemOid", ( PleaseSelect.noSelect(sysOid) ), "Please select system!")
		.testField("progId", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(progId)", "Id is blank!")
		.testField("progId", ( PleaseSelect.noSelect(sysProg.getProgId()) ), "Please change Id value!") // PROG-ID 不能用  "all" 這個下拉值
		.testField("progId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(sysProg.getProgId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("name", sysProg, "@org.apache.commons.lang3.StringUtils@isBlank(name)", "Name is blank!")
		.testField("url", ( (MenuItemType.ITEM.equals(sysProg.getItemType()) && StringUtils.isBlank(sysProg.getUrl())) ), "URL is blank!")
		.testField("itemType", ( PleaseSelect.noSelect(sysProg.getItemType()) ), "Please select item-type!")
		.testField("iconOid", ( PleaseSelect.noSelect(iconOid) ), "Please select icon!")
		.testField("dialogWidth", ( (YES.equals(sysProg.getIsDialog()) && !NumberUtils.isCreatable(w)) ), "Please input dialog width!")
		.testField("dialogHeight", ( (YES.equals(sysProg.getIsDialog()) && !NumberUtils.isCreatable(h)) ), "Please input dialog height!")
		.throwMessage();		
	}	
	
	private void save(DefaultControllerJsonResultObj<TbSysProg> result, TbSysProg sysProg, String sysOid, String iconOid, String w, String h) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysProg, sysOid, iconOid, w, h);
		sysProg.setDialogW( NumberUtils.toInt(w) );
		sysProg.setDialogH( NumberUtils.toInt(h) );
		DefaultResult<TbSysProg> progResult = this.systemProgramLogicService.create(sysProg, sysOid, iconOid);
		this.setDefaultResponseJsonResult(result, progResult);
	}
	
	private void update(DefaultControllerJsonResultObj<TbSysProg> result, TbSysProg sysProg, String sysOid, String iconOid, String w, String h) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysProg, sysOid, iconOid, w, h);
		sysProg.setDialogW( NumberUtils.toInt(w) );
		sysProg.setDialogH( NumberUtils.toInt(h) );
		DefaultResult<TbSysProg> progResult = this.systemProgramLogicService.update(sysProg, sysOid, iconOid);
		this.setDefaultResponseJsonResult(result, progResult);
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysProg sysProg) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> progResult = this.systemProgramLogicService.delete(sysProg);
		this.setDefaultResponseJsonResult(result, progResult);
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002A")
	@RequestMapping(value = "/sysProgramSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysProg> doSave(HttpServletRequest request, TbSysProg sysProg) {
		DefaultControllerJsonResultObj<TbSysProg> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(
					result, 
					sysProg, 
					request.getParameter("progSystemOid"),
					request.getParameter("iconOid"),
					request.getParameter("dialogWidth"), 
					request.getParameter("dialogHeight"));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002E")
	@RequestMapping(value = "/sysProgramUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysProg> doUpdate(HttpServletRequest request, TbSysProg sysProg) {
		DefaultControllerJsonResultObj<TbSysProg> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(
					result, 
					sysProg, 
					request.getParameter("progSystemOid"),
					request.getParameter("iconOid"),
					request.getParameter("dialogWidth"), 
					request.getParameter("dialogHeight"));
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}		
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0002D")
	@RequestMapping(value = "/sysProgramDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysProg sysProg) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysProg);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;		
	}
	
}
