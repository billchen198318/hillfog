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
package org.qifu.base.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.model.ZeroKeyProvide;
import org.qifu.base.properties.LdapLoginConfigProperties;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.model.User;
import org.qifu.core.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation= Propagation.REQUIRED, timeout=300, readOnly=true)
public class BaseUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IAccountService<TbAccount, String> accountService;
    
    @Autowired
    LdapLoginConfigProperties ldapLoginConfigProperties;
    
    @Autowired
    LdapTemplate ldapTemplate;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    HttpServletRequest request;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("login account: {}", username);
        if (StringUtils.isBlank(username)) {
        	logger.warn("account value blank.");
        	throw new UsernameNotFoundException( BaseSystemMessage.parameterBlank() );
        }
        if (!YesNo.YES.equals(ldapLoginConfigProperties.getLoginEnable())) {
        	return this.loadFromDB(username);
        }
        return this.loadFromLDAP(username);
    }
    
    private UserDetails loadFromLDAP(String username) throws UsernameNotFoundException {
    	String password = request.getParameter("password");
    	if (StringUtils.isBlank(password)) {
    		throw new UsernameNotFoundException( "password " + BaseSystemMessage.parameterBlank() );
    	}
    	Boolean auth = false;
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(ldapLoginConfigProperties.getSearchFilter(), username));
		String ouArr[] = StringUtils.defaultString(ldapLoginConfigProperties.getAuthSearchBase()).split(Constants.DEFAULT_SPLIT_DELIMITER);
		if (null == ouArr || ouArr.length < 1) {
			auth = ldapTemplate.authenticate("", filter.encode(), password);
		}
		for (int i = 0; !auth && ouArr != null && i < ouArr.length; i++) {
			auth = ldapTemplate.authenticate(StringUtils.deleteWhitespace(ouArr[i]), filter.encode(), password);
		}
		if (!auth) {
			throw new UsernameNotFoundException("LDAP auth fail!");
		}
		User user = new User(ZeroKeyProvide.OID_KEY, username, passwordEncoder.encode(password), YesNo.YES);
		user.setByLdap(YesNo.YES);
    	return user;
    }
    
    private UserDetails loadFromDB(String username) throws UsernameNotFoundException {
        TbAccount accObj = new TbAccount();
        accObj.setAccount(username);
        DefaultResult<TbAccount> result = null;
        try {
            result = this.accountService.selectByUniqueKey(accObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null || result.getValue() == null) {
            throw new UsernameNotFoundException( result.getMessage() );
        }
        accObj = result.getValue();
        if (!YesNo.YES.equals(accObj.getOnJob())) {
        	throw new UsernameNotFoundException("auth fail!");
        }
        return new User(accObj.getOid(), accObj.getAccount(), accObj.getPassword(), accObj.getOnJob());    	
    }
    
}
