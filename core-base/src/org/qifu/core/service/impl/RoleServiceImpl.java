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
package org.qifu.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbRole;
import org.qifu.core.mapper.TbRoleMapper;
import org.qifu.core.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class RoleServiceImpl extends BaseService<TbRole, String> implements IRoleService<TbRole, String> {
	
	@Autowired
	TbRoleMapper roleMapper;
	
	@Override
	protected IBaseMapper<TbRole, String> getBaseMapper() {
		return this.roleMapper;
	}
	
	/**
	 * 查帳戶下有的 role
	 * 
	 * @param account
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public List<TbRole> findForAccount(String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(account)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("account", account);		
		return this.roleMapper.findForAccount(paramMap);
	}
	
	/**
	 * 查某隻程式屬於的role
	 * 
	 * select OID, ROLE, DESCRIPTION from tb_role where ROLE in (
	 * 		select ROLE from tb_sys_menu_role WHERE PROG_ID = :progId 
	 * )
	 * 
	 */
	@Override
	public List<TbRole> findForProgram(String progId) throws ServiceException, Exception {
		if (StringUtils.isBlank(progId)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progId", progId);
		return this.roleMapper.findForProgram(paramMap);
	}
	
}
