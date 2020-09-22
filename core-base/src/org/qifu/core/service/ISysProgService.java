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
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.service.IBaseService;
import org.qifu.core.entity.TbSysProg;

public interface ISysProgService<T, E> extends IBaseService<TbSysProg, String> {
	
	public Map<String, String> findSysProgFolderMap(String basePath, String progSystem, String itemType, boolean pleaseSelect) throws ServiceException, Exception;
	
	/**
	 * 找在選單中(FOLDER) 之下已存在的項目
	 * 
	 * @param progSystem
	 * @param menuParentOid
	 * @param itemType
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<TbSysProg> findForInTheFolderMenuItems(String progSystem, String menuParentOid, String itemType) throws ServiceException, Exception;
	
	/**
	 * 找同 PROG_SYSTEM 的資料
	 * 
	 * select OID, PROG_ID, NAME, PROG_SYSTEM, ICON 
	 * from tb_sys_prog 
	 * where PROG_SYSTEM=:progSystem
	 * and ITEM_TYPE='ITEM' 
	 * and EDIT_MODE='N';
	 * 
	 * @param progSystem
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<TbSysProg> findForSystemItems(String progSystem) throws ServiceException, Exception;	
	
}
