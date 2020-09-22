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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysExprJob;
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.logic.ISystemExpressionLogicService;
import org.qifu.core.model.ExpressionJobConstants;
import org.qifu.core.service.ISysExprJobService;
import org.qifu.core.service.ISysExpressionService;
import org.qifu.core.service.ISysService;
import org.qifu.core.util.SystemExpressionJobUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysExpressionJobController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysExpressionService<TbSysExpression, String> sysExpressionService;
	
	@Autowired
	ISysExprJobService<TbSysExprJob, String> sysExprJobService; 
	
	@Autowired
	ISystemExpressionLogicService systemExpressionLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "sys_exprjob";
	}		
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("createPage".equals(type) || "editPage".equals(type)) {
			mm.put("expressionMap", this.sysExpressionService.findExpressionMap(true));
			mm.put("sysMap", this.sysService.findSysMap(true));
			
			Map<String, String> runDayOfWeekMap = new LinkedHashMap<String, String>();
			Map<String, String> runHourMap = new LinkedHashMap<String, String>();
			Map<String, String> runMinuteMap = new LinkedHashMap<String, String>();
			
			runDayOfWeekMap.put(ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL, ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL);
			for (int day=1; day<=7; day++) {
				runDayOfWeekMap.put(String.valueOf(day), String.valueOf(day));
			}
			
			runHourMap.put(ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL, ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL);
			for (int hour=0; hour<=23; hour++) {
				runHourMap.put(String.valueOf(hour), String.valueOf(hour));
			}
			
			runMinuteMap.put(ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL, ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL);
			for (int minute=0; minute<=59; minute++) {
				runMinuteMap.put(String.valueOf(minute), String.valueOf(minute));
			}
			
			mm.put("runDayOfWeekMap", runDayOfWeekMap);
			mm.put("runHourMap", runHourMap);
			mm.put("runMinuteMap", runMinuteMap);
		}
	}
	
	private void fetch(String sysExprJobOid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		TbSysExprJob sysExprJob = this.sysExprJobService.selectByPrimaryKey(sysExprJobOid).getValueEmptyThrowMessage();
		mm.put("sysExprJob", sysExprJob);
		TbSys sys = new TbSys();
		sys.setSysId(sysExprJob.getSystem());
		sys = this.sysService.selectByUniqueKey(sys).getValueEmptyThrowMessage();
		mm.put("systemOid", sys.getOid());
		TbSysExpression expression = new TbSysExpression();
		expression.setExprId( sysExprJob.getExprId() );
		expression = this.sysExpressionService.selectByUniqueKey(expression).getValueEmptyThrowMessage();
		mm.put("expressionOid", expression.getOid());
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006Q")
	@RequestMapping(value = "/sysExpressionJobPage")	
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006Q")
	@RequestMapping(value = "/sysExpressionJobQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysExprJob> > queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysExprJob> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysExprJob> > queryResult = this.sysExprJobService.findPage(
					this.queryParameter(searchValue).fullEquals("id").fullLink("name").value(), 
					pageOf.orderBy("SYSTEM,ID,NAME").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006A")
	@RequestMapping(value = "/sysExpressionJobCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006E")
	@RequestMapping(value = "/sysExpressionJobEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("editPage", mm);
			this.fetch(oid, mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysExprJob> result, TbSysExprJob sysExprJob, String systemOid, String expressionOid) throws ControllerException, Exception {
		CheckControllerFieldHandler<TbSysExprJob> checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("systemOid", PleaseSelect.noSelect(systemOid), "Please select system!")
		.testField("id", sysExprJob, "@org.apache.commons.lang3.StringUtils@isBlank( id )", "Id is required!")
		.testField("id", sysExprJob, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( id.replaceAll(\"-\", \"\").replaceAll(\"_\", \"\") )", "Id only normal character!")
		.testField("name", sysExprJob, "@org.apache.commons.lang3.StringUtils@isBlank( name )", "Name is required!")
		.testField("systemOid", PleaseSelect.noSelect(expressionOid), "Please select expression!")
		.testField("contact", ( !ExpressionJobConstants.CONTACT_MODE_NO.equals( sysExprJob.getContactMode() ) && StringUtils.isBlank(sysExprJob.getContact()) ), "Contact is required!");
		if (!ExpressionJobConstants.CONTACT_MODE_NO.equals( sysExprJob.getContactMode() )) {
			checkHandler.testField("contact", sysExprJob, " @org.apache.commons.lang3.StringUtils@defaultString( contact ).indexOf(\"@\") < 1 || @org.apache.commons.lang3.StringUtils@defaultString( contact ).length < 3 ", "Contact value is not email address!");
		}
		checkHandler.throwMessage();
	}
	
	private void save(DefaultControllerJsonResultObj<TbSysExprJob> result, TbSysExprJob sysExprJob, String systemOid, String expressionOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysExprJob, systemOid, expressionOid);
		DefaultResult<TbSysExprJob> cResult = this.systemExpressionLogicService.createJob(sysExprJob, systemOid, expressionOid);
		if ( cResult.getValue() != null ) {
			result.setValue( cResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( cResult.getMessage() );		
	}	
	
	private void update(DefaultControllerJsonResultObj<TbSysExprJob> result, TbSysExprJob sysExprJob, String systemOid, String expressionOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysExprJob, systemOid, expressionOid);
		DefaultResult<TbSysExprJob> uResult = this.systemExpressionLogicService.updateJob(sysExprJob, systemOid, expressionOid);
		if ( uResult.getValue() != null ) {
			result.setValue( uResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( uResult.getMessage() );		
	}	
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysExprJob sysExprJob) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.systemExpressionLogicService.deleteJob(sysExprJob);
		if ( dResult.getValue() != null && dResult.getValue() ) {
			result.setValue( dResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );		
	}
	
	private void manualExecute(DefaultControllerJsonResultObj<TbSysExprJob> result, HttpServletRequest request, TbSysExprJob sysExprJob) throws AuthorityException, ControllerException, ServiceException, Exception {
		sysExprJob = this.sysExprJobService.selectByEntityPrimaryKey(sysExprJob).getValueEmptyThrowMessage();
		if (this.getMainSystem().equals(sysExprJob.getSystem())) { // 是自己 CORE系統, 所以不用觸發遠端的服務
			SystemExpressionJobUtils.executeJobForManual(sysExprJob.getOid());
		} else {
			throw new ControllerException("Current version no support call other system job.");
			/*
			TbSysExprJobLog jobLog = SystemExpressionJobUtils.executeJobForManualWebClient(sysExprJob, super.getAccountId(), request);
			if (null != jobLog && !StringUtils.isBlank(jobLog.getFaultMsg())) {
				throw new ControllerException( jobLog.getFaultMsg() );
			}
			if (null == jobLog) {
				throw new ControllerException( BaseSystemMessage.dataErrors() );
			}
			*/
		}	
		result.setValue( sysExprJob );
		result.setSuccess( YES );
		result.setMessage( BaseSystemMessage.updateSuccess() );
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006A")
	@RequestMapping(value = "/sysExpressionJobSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysExprJob> doSave(TbSysExprJob sysExprJob, @RequestParam("systemOid") String systemOid, @RequestParam("expressionOid") String expressionOid) {
		DefaultControllerJsonResultObj<TbSysExprJob> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, sysExprJob, systemOid, expressionOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006E")
	@RequestMapping(value = "/sysExpressionJobUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysExprJob> doUpdate(TbSysExprJob sysExprJob, @RequestParam("systemOid") String systemOid, @RequestParam("expressionOid") String expressionOid) {
		DefaultControllerJsonResultObj<TbSysExprJob> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, sysExprJob, systemOid, expressionOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006D")
	@RequestMapping(value = "/sysExpressionJobDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysExprJob sysExprJob) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysExprJob);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG003D0006D")
	@RequestMapping(value = "/sysExpressionJobManualExecuteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysExprJob> doManualExecute(HttpServletRequest request, TbSysExprJob sysExprJob) {
		DefaultControllerJsonResultObj<TbSysExprJob> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.manualExecute(result, request, sysExprJob);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}		
	
}
