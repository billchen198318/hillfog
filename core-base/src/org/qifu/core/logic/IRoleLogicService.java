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
package org.qifu.core.logic;

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbRole;
import org.qifu.core.entity.TbRolePermission;

public interface IRoleLogicService {
	
	/**
	 * 建立 TB_ROLE 資料
	 * 
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<TbRole> create(TbRole role) throws ServiceException, Exception;
	
	/**
	 * 更新 TB_ROLE 資料
	 * 
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<TbRole> update(TbRole role) throws ServiceException, Exception;
	
	/**
	 * 刪除 TB_ROLE, TB_ROLE_PERMISSION, TB_USER_ROLE 資料
	 * 
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<Boolean> delete(TbRole role) throws ServiceException, Exception;
	
	/**
	 * 產生 TB_ROLE_PERMISSION 資料
	 * 
	 * @param permission
	 * @param roleOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<TbRolePermission> createPermission(TbRolePermission permission, String roleOid) throws ServiceException, Exception;
	
	/**
	 * 刪除 TB_ROLE_PERMISSION 資料
	 * 
	 * @param permission
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<Boolean> deletePermission(TbRolePermission permission) throws ServiceException, Exception;
	
	/**
	 * 找出全部的role與某帳戶下的role
	 * 
	 * map 中的  key 
	 * enable	- 帳戶下的role
	 * all	- 所有role
	 * 
	 * @param accountOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public Map<String, List<TbRole>> findForAccountRoleEnableAndAll(String accountOid) throws ServiceException, Exception;
	
	/**
	 * 更新帳戶的role
	 * 
	 * @param accountOid
	 * @param roles
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<Boolean> updateUserRole(String accountOid, List<String> roles) throws ServiceException, Exception;
	
	/**
	 * 找出全部的role與某程式menu所屬的role
	 * 
	 * map 中的  key 
	 * enable	- 程式menu的role
	 * all	- 所有role
	 * 
	 * @param programOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public Map<String, List<TbRole>> findForProgramRoleEnableAndAll(String programOid) throws ServiceException, Exception;
	
	/**
	 * 更新存在選單中程式的選單所屬 role
	 * 
	 * @param progOid
	 * @param roles
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<Boolean> updateMenuRole(String progOid, List<String> roles) throws ServiceException, Exception;
	
	/**
	 * 拷備一份role
	 * 
	 * @param fromRoleOid
	 * @param role
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<TbRole> copyAsNew(String fromRoleOid, TbRole role) throws ServiceException, Exception;
	
	/**
	 * 使用者設的role-id(角色), 它設定在 tb_sys_code中
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public String getDefaultUserRole() throws ServiceException, Exception;
	
	/**
	 * 使用者設的role(角色), 它設定在 tb_sys_code中
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public TbRole getDefaultUserRoleEntity() throws ServiceException, Exception;
	
}
