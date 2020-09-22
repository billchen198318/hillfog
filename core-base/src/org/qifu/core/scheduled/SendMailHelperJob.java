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
package org.qifu.core.scheduled;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.scheduled.BaseScheduledTasksProvide;
import org.qifu.core.entity.TbSysMailHelper;
import org.qifu.core.service.ISysMailHelperService;
import org.qifu.core.util.MailClientUtils;
import org.qifu.core.util.SystemSettingConfigureUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SendMailHelperJob extends BaseScheduledTasksProvide {
	protected Logger log = LogManager.getLogger(SendMailHelperJob.class);
	
	@Autowired
	ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService;
	
	@Scheduled(initialDelay = 5000, fixedDelay = 180000)
	public void execute() {
		//log.info("begin...");
		try {
			this.login();
			if (YesNo.YES.equals(SystemSettingConfigureUtils.getMailEnableValue())) {
				String linkMailId = SimpleUtils.getStrYMD("").substring(0, 6);
				DefaultResult<List<TbSysMailHelper>> result = sysMailHelperService.findForJobList(linkMailId);			
				if (result.getValue()!=null) {
					this.process(sysMailHelperService, result.getValue());
				}				
			} else {
				log.warn("************ mail sender is disable. please modify config CNF/CNF_CONF002 ************");				
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.logout();
		}
		//log.info("end...");
	}
	
	private void process(ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService, List<TbSysMailHelper> mailHelperList) throws ServiceException, Exception {
		if (mailHelperList==null || mailHelperList.size()<1) {
			return;
		}	
		for (TbSysMailHelper mailHelper : mailHelperList) {
			new ProcessWorker(sysMailHelperService, mailHelper);
		}
	}
	
	private class ProcessWorker extends Thread {
		private ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService = null;
		private TbSysMailHelper mailHelper = null;
		private Thread flag = this;
		private long sleepTime = 3000; // 3 - sec
		private int rety = 3; // 重試3次
		private boolean success = false;
		
		public ProcessWorker(ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService, TbSysMailHelper mailHelper) {
			this.sysMailHelperService = sysMailHelperService;
			this.mailHelper = mailHelper;
			this.flag = this;
			this.start();
		}
		
		public void run() {
			
			this.flag = this;
			while ( !this.success && this.flag == Thread.currentThread() && rety>0 ) {
				if ( this.mailHelper == null) {
					this.flag = null;
				}
				
				try {
					log.info("process mail-id: " + this.mailHelper.getMailId());	
					MailClientUtils.send(
							this.mailHelper.getMailFrom(), 
							this.mailHelper.getMailTo(), 
							this.mailHelper.getMailCc(), 
							this.mailHelper.getMailBcc(), 
							this.mailHelper.getSubject(), 
							new String(this.mailHelper.getText(), "utf8") );
					success = true;					
				} catch (MailException e1) {
					e1.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				if (success) {
					try {
						if ( YesNo.YES.equals(this.mailHelper.getRetainFlag()) ) {
							this.mailHelper.setSuccessFlag(YesNo.YES);
							this.mailHelper.setSuccessTime(new Date());							
							this.sysMailHelperService.update(mailHelper);
						} else {
							this.sysMailHelperService.delete(mailHelper);
						}						
					} catch (ServiceException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					log.info("success mail-id: " + this.mailHelper.getMailId());
					this.flag = null;
				}
				this.rety--;
				
				try {
					Thread.sleep(this.sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}			
			this.flag = null;	
		}
		
	}
	
}
