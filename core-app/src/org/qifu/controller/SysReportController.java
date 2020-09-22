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

import java.io.IOException;
import java.util.HashMap;
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
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.core.entity.TbSysJreport;
import org.qifu.core.entity.TbSysJreportParam;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.logic.ISystemJreportLogicService;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.service.ISysJreportParamService;
import org.qifu.core.service.ISysJreportService;
import org.qifu.core.service.ISysUploadService;
import org.qifu.core.util.JReportUtils;
import org.qifu.core.util.UploadSupportUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysReportController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysJreportService<TbSysJreport, String> sysJreportService;
	
	@Autowired
	ISysJreportParamService<TbSysJreportParam, String> sysJreportParamService;
	
	@Autowired
	ISysUploadService<TbSysUpload, String> sysUploadService;
	
	@Autowired
	ISystemJreportLogicService systemJreportLogicService;	
	
	@Override
	public String viewPageNamespace() {
		return "sys_rpt";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	private void fetch(String oid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		DefaultResult<TbSysJreport> result = this.sysJreportService.selectByPrimaryKeySimple(oid);
		if ( result.getValue() == null ) {
			throw new ControllerException( result.getMessage() );
		}
		TbSysJreport sysJreport = result.getValue();
		mm.put("sysJreport", sysJreport);		
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005Q")
	@RequestMapping(value = "/sysReportPage")	
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005Q")
	@RequestMapping(value = "/sysReportQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj<List<TbSysJreport>>  queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysJreport> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysJreport> > queryResult = this.sysJreportService.findPage(
					"count",
					"findPageSimple",
					this.queryParameter(searchValue).fullEquals("reportId").value(), 
					pageOf.orderBy("REPORT_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005A")
	@RequestMapping(value = "/sysReportCreatePage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005E")
	@RequestMapping(value = "/sysReportEditPage")
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005S01Q")
	@RequestMapping(value = "/sysReportParamPage")
	public String paramPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("param-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("editParamPage", mm);
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
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005S01Q")
	@RequestMapping(value = "/sysJreportParamQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysJreportParam>>  paramQueryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysJreportParam> > result = this.getQueryJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysJreportParam> > queryResult = this.sysJreportParamService.findPage(
					this.queryParameter(searchValue).fullEquals("reportId").value(), 
					pageOf.orderBy("URL_PARAM").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005S01Q")
	@RequestMapping(value = "/sysReportPreviewPage")
	public String previewPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {	
		String viewName = this.viewPageWithNamespace("preview-page");
		this.getDefaultModelMap(mm, this.currentMethodAuthority());
		try {
			this.init("previewPage", mm);
			this.fetch(oid, mm);
			TbSysJreport sysJreport = (TbSysJreport) mm.get("sysJreport");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("reportId", sysJreport.getReportId());			
			DefaultResult<List<TbSysJreportParam>> paramListResult = this.sysJreportParamService.selectListByParams( paramMap );
			mm.put("paramList", paramListResult.getValue());
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;		
	}		
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("reportId", sysJreport, "@org.apache.commons.lang3.StringUtils@isBlank(reportId)", "Id is blank!")
		.testField("reportId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(sysJreport.getReportId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("reportId", ( PleaseSelect.noSelect(sysJreport.getReportId()) ), "Please change Id value!") // 不能用  "all" 這個下拉值
		.throwMessage();
	}
	
	private void checkFieldsForParam(DefaultControllerJsonResultObj<TbSysJreportParam> result, TbSysJreportParam param) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("urlParam", param, "@org.apache.commons.lang3.StringUtils@isBlank(urlParam)", "URL parameter is blank!")
		.testField("rptParam", param, "@org.apache.commons.lang3.StringUtils@isBlank(rptParam)", "Report variable is blank!")
		.testField("urlParam", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09(urlParam)", "URL parameter only normal character!")
		.testField("rptParam", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( @org.apache.commons.lang3.StringUtils@defaultString(rptParam).replaceAll(\"_\", \"\") )", "Report variable only normal character!")
		.throwMessage();
	}	
	
	private void fillUploadFileContent(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport, String uploadOid) throws ServiceException, IOException, Exception {
		DefaultResult<TbSysUpload> uResult = this.sysUploadService.selectByPrimaryKeySimple(uploadOid);
		if ( uResult.getValue() == null ) {
			throw new ServiceException( uResult.getMessage() );
		}
		String fileNameHead = uResult.getValue().getShowName().split("[.]")[0];
		this.getCheckControllerFieldHandler(result).testField("reportId", ( !fileNameHead.equals(sysJreport.getReportId()) ), "Please change Id value as " + fileNameHead).throwMessage();
		sysJreport.setContent( UploadSupportUtils.getDataBytes(uploadOid) );		
	}
	
	private void save(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport, String uploadOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysJreport);
		if (StringUtils.isBlank(uploadOid)) {
			throw new ControllerException("Please upload report file!");
		}		
		JReportUtils.selfTestDecompress4Upload(uploadOid);
		this.fillUploadFileContent(result, sysJreport, uploadOid);
		DefaultResult<TbSysJreport> rResult = this.systemJreportLogicService.create(sysJreport);
		if ( rResult.getValue() != null ) {
			JReportUtils.deployReport( rResult.getValue() );
			rResult.getValue().setContent( null ); // 不傳回 content byte[] 內容
			result.setValue( rResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( rResult.getMessage() );
	}	
	
	private void update(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport, String uploadOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysJreport);
		if (!StringUtils.isBlank(uploadOid)) {
			JReportUtils.selfTestDecompress4Upload(uploadOid);
			this.fillUploadFileContent(result, sysJreport, uploadOid);
		}
		DefaultResult<TbSysJreport> rResult = this.systemJreportLogicService.update(sysJreport);
		if ( rResult.getValue() != null ) {
			if (!StringUtils.isBlank(uploadOid)) { // 由於 content 內容改變了(有重新上傳報表), 所以重新部屬
				JReportUtils.deployReport( rResult.getValue() );
			}
			rResult.getValue().setContent( null ); // 不傳回 content byte[] 內容
			result.setValue( rResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( rResult.getMessage() );		
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysJreport sysJreport) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> tResult = this.systemJreportLogicService.delete(sysJreport);
		if ( tResult.getValue() != null && tResult.getValue() ) {
			result.setValue( Boolean.TRUE );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getMessage() );
	}	
	
	private void saveParam(DefaultControllerJsonResultObj<TbSysJreportParam> result, TbSysJreportParam sysJreportParam, String reportOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFieldsForParam(result, sysJreportParam);
		DefaultResult<TbSysJreportParam> pResult = this.systemJreportLogicService.createParam(sysJreportParam, reportOid);
		if ( pResult.getValue() != null ) {
			result.setValue( pResult.getValue() );
			result.setSuccess(YES);
		}
		result.setMessage( pResult.getMessage() );
	}
	
	private void deleteParam(DefaultControllerJsonResultObj<Boolean> result, TbSysJreportParam sysJreportParam) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> pResult = this.systemJreportLogicService.deleteParam(sysJreportParam);
		if ( pResult.getValue() != null && pResult.getValue() ) {
			result.setValue( Boolean.TRUE );
			result.setSuccess( YES );
		}
		result.setMessage( pResult.getMessage() );
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005A")
	@RequestMapping(value = "/sysReportSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysJreport> doSave(TbSysJreport sysJreport, @RequestParam("uploadOid") String uploadOid) {
		DefaultControllerJsonResultObj<TbSysJreport> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, sysJreport, uploadOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005E")
	@RequestMapping(value = "/sysReportUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DefaultControllerJsonResultObj<TbSysJreport> doUpdate(TbSysJreport sysJreport, @RequestParam("uploadOid") String uploadOid) {
		DefaultControllerJsonResultObj<TbSysJreport> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, sysJreport, uploadOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005D")
	@RequestMapping(value = "/sysReportDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysJreport sysJreport) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysJreport);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005Q")
	@RequestMapping(value = "/sysReportDownloadContentJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<String> doDownloadContent(TbSysJreport sysJreport) {
		DefaultControllerJsonResultObj<String> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			DefaultResult<TbSysJreport> rResult = this.sysJreportService.selectByEntityPrimaryKey(sysJreport);
			if ( rResult.getValue() == null ) {
				throw new ControllerException( rResult.getMessage() );
			}
			sysJreport = rResult.getValue();
			result.setValue( UploadSupportUtils.create(this.getSystem(), UploadTypes.IS_TEMP, true, sysJreport.getContent(), sysJreport.getReportId()+".zip") );
			result.setSuccess( YES );
			result.setMessage( BaseSystemMessage.insertSuccess() );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005S01A")
	@RequestMapping(value = "/sysJreportParamSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysJreportParam> doParamSave(TbSysJreportParam sysJreportParam, @RequestParam("reportOid") String reportOid) {
		DefaultControllerJsonResultObj<TbSysJreportParam> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.saveParam(result, sysJreportParam, reportOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROG001D0005S01D")
	@RequestMapping(value = "/sysJreportParamDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doParamDelete(TbSysJreportParam sysJreportParam) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.deleteParam(result, sysJreportParam);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
}
