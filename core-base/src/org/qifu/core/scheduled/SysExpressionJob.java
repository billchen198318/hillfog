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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.scheduled.BaseScheduledTasksProvide;
import org.qifu.core.util.SystemExpressionJobUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SysExpressionJob extends BaseScheduledTasksProvide {
	protected Logger log = LogManager.getLogger(SysExpressionJob.class);
	
	/* fixedDelay 60000 , do not to modify */
	@Scheduled(initialDelay = 10000, fixedDelay = 60000)
	public void execute() {
		try {
			this.login();
			SystemExpressionJobUtils.executeJobs();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.logout();		
		}
	}
	
}
