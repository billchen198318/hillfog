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

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.core.entity.TbSysUpload;

@Mapper
public interface TbSysUploadMapper extends IBaseMapper<TbSysUpload, String> {
	
	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 * 
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	public TbSysUpload selectByPrimaryKeySimple(String oid) throws Exception;
	
	/**
	 * 刪除 IS_FILE='N' && CONTENT 欄位放byte 類別為暫存的資料
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTmpContentBySystem(Map<String, Object> paramMap) throws Exception;
	
}
