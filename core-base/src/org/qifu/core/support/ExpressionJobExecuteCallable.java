/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.TbSysExprJob;
import org.qifu.core.entity.TbSysExprJobLog;
import org.qifu.core.entity.TbSysMailHelper;
import org.qifu.core.model.ExpressionJobConstants;
import org.qifu.core.model.ExpressionJobObj;
import org.qifu.core.service.ISysExprJobLogService;
import org.qifu.core.service.ISysExprJobService;
import org.qifu.core.service.ISysMailHelperService;
import org.qifu.core.util.SystemSettingConfigureUtils;
import org.qifu.core.util.UserUtils;
import org.qifu.util.ScriptExpressionUtils;
import org.qifu.util.SimpleUtils;

public class ExpressionJobExecuteCallable implements Callable<ExpressionJobObj> {
	protected static Logger logger = LogManager.getLogger(ExpressionJobExecuteCallable.class);
	private ExpressionJobObj jobObj = null;
	
	public ExpressionJobExecuteCallable(ExpressionJobObj jobObj) {
		this.jobObj = jobObj;
	}

	public ExpressionJobObj getJobObj() {
		return jobObj;
	}

	public void setJobObj(ExpressionJobObj jobObj) {
		this.jobObj = jobObj;
	}
	
	@Override
	public ExpressionJobObj call() throws Exception {
		Date beginDatetime = new Date();
		String faultMsg = "";
		String runStatus = "";
		String logStatus = "";
		boolean setUserInfoBackgroundMode = false;
		
		ISysExprJobService<TbSysExprJob, String> sysExprJobService = AppContext.context.getBean(ISysExprJobService.class);
		
		ISysExprJobLogService<TbSysExprJobLog, String> sysExprJobLogService = AppContext.context.getBean(ISysExprJobLogService.class);
		
		try {
			
			if (UserUtils.getCurrentUser() == null) {
				UserUtils.setUserInfoForUserLocalUtilsBackgroundMode();
				setUserInfoBackgroundMode = true;
			}
			
			logger.info("[Expression-Job] (Start) ID: " + this.jobObj.getSysExprJob().getId() + " , NAME: " + this.jobObj.getSysExprJob().getName());
			
			if (StringUtils.isBlank(jobObj.getSysExpression().getContent())) {
				faultMsg = "No expression content!";
				runStatus = ExpressionJobConstants.RUNSTATUS_FAULT;
				logStatus = ExpressionJobConstants.LOGSTATUS_NO_EXECUTE;
				return this.jobObj;
			}
			if (YesNo.YES.equals(this.jobObj.getSysExprJob().getCheckFault()) 
					&& ExpressionJobConstants.RUNSTATUS_FAULT.equals(this.jobObj.getSysExprJob().getRunStatus())) {
				faultMsg = "Before proccess fault, cannot execute expression job!";
				runStatus = ExpressionJobConstants.RUNSTATUS_FAULT;
				logStatus = ExpressionJobConstants.LOGSTATUS_NO_EXECUTE;
				return this.jobObj;
			}
			
			this.jobObj.getSysExprJob().setRunStatus(ExpressionJobConstants.RUNSTATUS_PROCESS_NOW);
			sysExprJobService.update( this.jobObj.getSysExprJob() );
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("jobObj", this.jobObj);
			ScriptExpressionUtils.execute(
					jobObj.getSysExpression().getType(), 
					jobObj.getSysExpression().getContent(), 
					paramMap, 
					paramMap);
			runStatus = ExpressionJobConstants.RUNSTATUS_SUCCESS;
			logStatus = ExpressionJobConstants.LOGSTATUS_SUCCESS;			
		} catch (ServiceException se) {
			faultMsg = se.getMessage().toString();
			runStatus = ExpressionJobConstants.RUNSTATUS_FAULT;
			logStatus = ExpressionJobConstants.LOGSTATUS_FAULT;				
			logger.error( se.getMessage().toString() );
		} catch (Exception e) {
			faultMsg = e.getMessage().toString();
			if (e.getMessage()==null) { 
				faultMsg=e.toString();
			} else {
				faultMsg=e.getMessage().toString();
			}			
			runStatus = ExpressionJobConstants.RUNSTATUS_FAULT;
			logStatus = ExpressionJobConstants.LOGSTATUS_FAULT;			
			logger.error( faultMsg );
		} finally {
			if (faultMsg.length()>2000) {
				faultMsg = faultMsg.substring(0, 2000);
			}
			jobObj.getSysExprJob().setRunStatus(runStatus);
			jobObj.getSysExprJobLog().setFaultMsg(faultMsg);
			jobObj.getSysExprJobLog().setLogStatus(logStatus);
			jobObj.getSysExprJobLog().setId( jobObj.getSysExprJob().getId() );
			jobObj.getSysExprJobLog().setBeginDatetime(beginDatetime);
			jobObj.getSysExprJobLog().setEndDatetime( new Date() );
			
			sysExprJobService.update( this.jobObj.getSysExprJob() );
			DefaultResult<TbSysExprJobLog> jobLogResult = sysExprJobLogService.insert(jobObj.getSysExprJobLog());
			if (jobLogResult.getValue() != null) {
				jobObj.setSysExprJobLog( jobLogResult.getValue() );
			}
			
			this.sendMail();
			
			logger.info("[Expression-Job] (End) ID: " + this.jobObj.getSysExprJob().getId() + " , NAME: " + this.jobObj.getSysExprJob().getName());
			
			if (setUserInfoBackgroundMode) {
				UserUtils.removeForUserLocalUtils();
			}
			
		}
		return this.jobObj;
	}	
	
	private void sendMail() {
		try {
			if (ExpressionJobConstants.CONTACT_MODE_NO.equals(this.jobObj.getSysExprJob().getContactMode())) {
				return;
			}
			if (ExpressionJobConstants.CONTACT_MODE_ONLY_FAULT.equals(this.jobObj.getSysExprJob().getContactMode()) 
					&& !ExpressionJobConstants.RUNSTATUS_FAULT.equals(this.jobObj.getSysExprJob().getRunStatus())) {
				return;
			}
			if (ExpressionJobConstants.CONTACT_MODE_ONLY_SUCCESS.equals(this.jobObj.getSysExprJob().getContactMode()) 
					&& !ExpressionJobConstants.RUNSTATUS_SUCCESS.equals(this.jobObj.getSysExprJob().getRunStatus())) {
				return;
			}
			String contact = StringUtils.defaultString(this.jobObj.getSysExprJob().getContact()).trim();
			if (StringUtils.isBlank(contact)) {
				return;
			}
			String subject = this.jobObj.getSysExprJob().getId() + " - " + this.jobObj.getSysExprJob().getName();
			String content = subject + Constants.HTML_BR;
			content += "Run status: " + this.jobObj.getSysExprJob().getRunStatus() + Constants.HTML_BR;
			content += "Start: " + this.jobObj.getSysExprJobLog().getBeginDatetime().toString() + Constants.HTML_BR;
			content += "End: " + this.jobObj.getSysExprJobLog().getEndDatetime().toString() + Constants.HTML_BR;
			if (ExpressionJobConstants.RUNSTATUS_FAULT.equals(this.jobObj.getSysExprJob().getRunStatus())) {
				content += Constants.HTML_BR;
				content += "Fault: " + Constants.HTML_BR;
				content += this.jobObj.getSysExprJobLog().getFaultMsg();
			}
			
			ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService = AppContext.context.getBean(ISysMailHelperService.class);
			
			String mailId = SimpleUtils.getStrYMD("");
			TbSysMailHelper mailHelper = new TbSysMailHelper();
			mailHelper.setMailId( sysMailHelperService.findForMaxMailIdComplete(mailId) );
			mailHelper.setMailFrom( SystemSettingConfigureUtils.getMailDefaultFromValue() );
			mailHelper.setMailTo( contact );
			mailHelper.setSubject( subject );
			mailHelper.setText( content.getBytes("utf8") );
			mailHelper.setRetainFlag(YesNo.NO);
			mailHelper.setSuccessFlag(YesNo.NO);
			sysMailHelperService.insert(mailHelper);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
