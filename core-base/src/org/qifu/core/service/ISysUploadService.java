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

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.service.IBaseService;
import org.qifu.core.entity.TbSysUpload;

public interface ISysUploadService<T, E> extends IBaseService<TbSysUpload, String> {
	
	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 * 
	 * @param oid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<TbSysUpload> selectByPrimaryKeySimple(String oid) throws ServiceException, Exception;	
	
	/**
	 * 刪除 IS_FILE='N' && CONTENT 欄位放byte 類別為暫存的資料
	 * 
	 * @param systemId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<Boolean> deleteTmpContentBySystem(String systemId) throws ServiceException, Exception;
	
}
