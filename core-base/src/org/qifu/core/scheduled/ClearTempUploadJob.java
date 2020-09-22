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
package org.qifu.core.scheduled;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.scheduled.BaseScheduledTasksProvide;
import org.qifu.core.logic.impl.SystemJreportLogicServiceImpl;
import org.qifu.core.util.UploadSupportUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearTempUploadJob extends BaseScheduledTasksProvide {
	protected Logger logger = LogManager.getLogger(SystemJreportLogicServiceImpl.class);
	
	@Scheduled(cron = "1 0 1 * * *")
	public void execute() {
		logger.warn("Clear upload type is TMP data.");
		this.login();
		try {
			UploadSupportUtils.cleanTempUpload();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logout();
		logger.info("fine.");
	}
	
}
