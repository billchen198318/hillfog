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
package org.qifu.core.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.core.entity.TbRole;

@Mapper
public interface TbRoleMapper extends IBaseMapper<TbRole, String> {
	
	/**
	 * 查某隻程式屬於的role
	 * 
	 * select OID, ROLE, DESCRIPTION from tb_role where ROLE in (
	 * 		select ROLE from tb_sys_menu_role WHERE PROG_ID = :progId 
	 * )
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TbRole> findForProgram(Map<String, Object> paramMap);
	
	/**
	 * 查帳戶下有的 role
	 * SELECT * from tb_role 
	 * WHERE ROLE in (	
	 * 		select ROLE from tb_user_role WHERE ACCOUNT = :account
	 * )
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TbRole> findForAccount(Map<String, Object> paramMap);
	
}
