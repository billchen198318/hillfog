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
package org.qifu.core.util;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryResult;
import org.qifu.base.util.BaseApiSupport;
import org.qifu.base.util.UserLocalUtils;

public class CoreApiSupport extends BaseApiSupport {
	
	private static final long serialVersionUID = -4750183395777134636L;	
	
	/*
	@Autowired
	RoleCheckUtils roleCheckUtils;
	
	@Autowired
	UserBuilder userBuilder;
	
	@Autowired
	IPrpduserService<Prpduser, String> prpduserService;
	
	public boolean hasRole(String comcode, String usercode, String gradecode, String taskcode) {
		return roleCheckUtils.hasRole(comcode, usercode, gradecode, taskcode);
	}
	
	public boolean hasRole(String comcode, String usercode, String gradecode) {
		return roleCheckUtils.hasRole(comcode, usercode, gradecode);
	}
	
	public String getUserCodeFromToken(String token) {
		Map<String, Claim> claimToken = this.getTokenClaim(token);
		if (claimToken != null && claimToken.get(Constants.SINOSOFT_USER_PARAM_NAME) != null && !StringUtils.isBlank(claimToken.get(Constants.SINOSOFT_USER_PARAM_NAME).asString())) {
			return claimToken.get(Constants.SINOSOFT_USER_PARAM_NAME).asString();
		}
		return "";
	}
	
	public <T> boolean checkAuthToken(String token) {
		if (UserLocalUtils.getUserInfo() != null) {
			return true;
		}
		String userCode = this.getUserCodeFromToken(token);
		if (!StringUtils.isBlank(userCode)) {
			this.buildUser(userCode);
			return true;
		}
		return false;
	}
	
	public <T> boolean checkAuthToken(String token, DefaultResult<T> result) {
		boolean check = this.checkAuthToken(token);
		result.setIsAuth(YES);
		if (!check) {
			result.setIsAuth(NO);
			result.setMessage(BaseSystemMessage.noLoginAccessDenied());			
		}
		return check;
	}
	
	public <T> boolean checkAuthFromUserLocalUtils(DefaultResult<T> result) {
		result.setIsAuth(YES);
		if (UserLocalUtils.getUserInfo() != null) {			
			return true;
		}
		result.setIsAuth(NO);
		result.setMessage(BaseSystemMessage.noLoginAccessDenied());
		return false;
	}
	
	public UserInfo buildUser(String userId) {
		UserInfo userInfo = UserBuilder.build(userId);
		userBuilder.setPrpduser(userInfo);
		userBuilder.setUtiusergradetasks(userInfo);
		return userInfo;
	}
	
	public UserInfo buildUser(String userId, String comCode) {
		UserInfo userInfo = UserBuilder.build(userId, comCode);
		userBuilder.setPrpduser(userInfo);
		userBuilder.setUtiusergradetasks(userInfo);		
		return userInfo;
	}
	
	public void logout() {
		UserBuilder.logout();
	}
	*/

	public <T> void fillEventResult2ResponseResult(QueryResult<T> queryResult, QueryResult<T> responseResult) {
		if (queryResult == null || responseResult == null) {
			return;
		}
		// FIXME: 要改 UserLocalUtils 為 Apache-shiro 或別的登入session管理元件
		if (UserLocalUtils.getUserInfo() != null) {
			responseResult.setIsAuth(YES);
		}
		if (StringUtils.isBlank(responseResult.getMessage())) {
			if (!StringUtils.isBlank(queryResult.getMessage())) {
				responseResult.setMessage( queryResult.getMessage() );
			}
		}
		if ( queryResult.getValue() != null ) {
			responseResult.setValue( queryResult.getValue() );
			this.successResult(responseResult);
		}
	}
	
	public <T> void fillEventResult2ResponseResult(DefaultResult<T> queryResult, QueryResult<T> responseResult) {
		if (queryResult == null || responseResult == null) {
			return;
		}
		// FIXME: 要改 UserLocalUtils 為 Apache-shiro 或別的登入session管理元件
		if (UserLocalUtils.getUserInfo() != null) {
			responseResult.setIsAuth(YES);
		}
		if (StringUtils.isBlank(responseResult.getMessage())) {
			if (!StringUtils.isBlank(queryResult.getMessage())) {
				responseResult.setMessage( queryResult.getMessage() );
			}
		}
		if ( queryResult.getValue() != null ) {
			responseResult.setValue( queryResult.getValue() );
			this.successResult(responseResult);
		}
	}	
	
	public <T> void fillEventResult2ResponseResultForPage(QueryResult<T> queryResult, QueryResult<T> responseResult, PageOf pageOf) {
		this.fillEventResult2ResponseResult(queryResult, responseResult);
		responseResult.setPageOf( pageOf );
	}		

	public <T> void fillEventResult2ResponseResultForPage(DefaultResult<T> queryResult, QueryResult<T> responseResult, PageOf pageOf) {
		this.fillEventResult2ResponseResult(queryResult, responseResult);
		responseResult.setPageOf( pageOf );
	}	
	
}
