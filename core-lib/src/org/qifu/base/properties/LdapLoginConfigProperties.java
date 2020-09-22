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
package org.qifu.base.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:appConfig.properties")
@ConfigurationProperties(prefix = "ldap")
public class LdapLoginConfigProperties {
	
	private String loginEnable;
	
	private String contextUrl;
	
	private String contextBase;
	
	private String contextUserDn;
	
	private String contextPassword;
	
	private String searchFilter;
	
	private String authSearchBase;

	public String getLoginEnable() {
		return loginEnable;
	}

	public void setLoginEnable(String loginEnable) {
		this.loginEnable = loginEnable;
	}

	public String getContextUrl() {
		return contextUrl;
	}

	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	public String getContextBase() {
		return contextBase;
	}

	public void setContextBase(String contextBase) {
		this.contextBase = contextBase;
	}

	public String getContextUserDn() {
		return contextUserDn;
	}

	public void setContextUserDn(String contextUserDn) {
		this.contextUserDn = contextUserDn;
	}

	public String getContextPassword() {
		return contextPassword;
	}

	public void setContextPassword(String contextPassword) {
		this.contextPassword = contextPassword;
	}

	public String getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	public String getAuthSearchBase() {
		return authSearchBase;
	}

	public void setAuthSearchBase(String authSearchBase) {
		this.authSearchBase = authSearchBase;
	}
	
}
