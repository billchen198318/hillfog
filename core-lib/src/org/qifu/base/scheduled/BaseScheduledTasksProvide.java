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
package org.qifu.base.scheduled;

import org.qifu.base.Constants;
import org.qifu.base.model.BaseUserInfo;
import org.qifu.base.util.UserLocalUtils;

public abstract class BaseScheduledTasksProvide {	
	
	public abstract void execute();
	
	private BaseUserInfo buildUser() {
		BaseUserInfo userInfo = new BaseUserInfo();
		userInfo.setUserId( Constants.SYSTEM_BACKGROUND_USER );
		UserLocalUtils.setUserInfo(userInfo);
		return userInfo;
	}
	
	public BaseUserInfo login() {
		return this.buildUser();
	}
	
	public void logout() {
		UserLocalUtils.setUserInfo( null );
		UserLocalUtils.remove();
	}
	
}
