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
package org.qifu.core.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.TbSysEventLog;
import org.qifu.core.service.ISysEventLogService;

public class SysEventLogSupport {
	protected static Logger log = LogManager.getLogger(SysEventLogSupport.class);
	
	private static ISysEventLogService<TbSysEventLog, String> sysEventLogService;
	
	static {
		sysEventLogService = AppContext.context.getBean(ISysEventLogService.class);
	}
	
	public static void log(String userId, String sysId, String executeEventId, boolean permit) {
		if ( StringUtils.isBlank(userId) || StringUtils.isBlank(sysId) || StringUtils.isBlank(executeEventId) ) {
			log.warn("parameter has null value, userId=" + userId + ", sysId=" + sysId + ", executeEventId=" + executeEventId);
			return;
		}
		TbSysEventLog eventLog = new TbSysEventLog();
		eventLog.setUser(userId);
		eventLog.setSysId(sysId);
		eventLog.setExecuteEvent(executeEventId.length()>255 ? executeEventId.substring(0, 255) : executeEventId);
		eventLog.setIsPermit( permit ? YesNo.YES : YesNo.NO );
		try {
			sysEventLogService.insert(eventLog);
		} catch (Exception e) {
			e.printStackTrace();
			log.error( e.getMessage().toString() );
		}
	}
	
}
