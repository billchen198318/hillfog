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
package org.qifu.core.service;

import java.util.List;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.service.IBaseService;
import org.qifu.core.entity.TbRole;

public interface IRoleService<T, E> extends IBaseService<TbRole, String> {
	
	/**
	 * 查帳戶下有的 role
	 * 
	 * @param account
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<TbRole> findForAccount(String account) throws ServiceException, Exception;	
	
	/**
	 * 查某隻程式屬於的role
	 * 
	 * select OID, ROLE, DESCRIPTION from tb_role where ROLE in (
	 * 		select ROLE from tb_sys_menu_role WHERE PROG_ID = :progId 
	 * )
	 * 
	 * @param progId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<TbRole> findForProgram(String progId) throws ServiceException, Exception;
	
}
