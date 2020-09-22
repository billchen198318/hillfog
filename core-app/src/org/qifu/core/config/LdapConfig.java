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
package org.qifu.core.config;

import org.qifu.base.properties.LdapLoginConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {
	
	@Autowired
	LdapLoginConfigProperties ldapLoginConfigProperties;
	
    @Bean
    public LdapContextSource contextSource() {
    	LdapContextSource contextSource = new LdapContextSource();
    	contextSource.setUrl( ldapLoginConfigProperties.getContextUrl() );
    	contextSource.setBase( ldapLoginConfigProperties.getContextBase() );
    	contextSource.setUserDn( ldapLoginConfigProperties.getContextUserDn() );
    	contextSource.setPassword( ldapLoginConfigProperties.getContextPassword() );
    	return contextSource;
    }
    
    @Bean
    public LdapTemplate ldapTemplate() {
    	LdapTemplate template = new LdapTemplate(contextSource());
    	return template;
    }
    
}
