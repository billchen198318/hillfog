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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.mapper.TbSysUploadMapper;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.service.ISysUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysUploadServiceImpl extends BaseService<TbSysUpload, String> implements ISysUploadService<TbSysUpload, String> {
	
	@Autowired
	TbSysUploadMapper tbSysUploadMapper;
	
	@Override
	protected IBaseMapper<TbSysUpload, String> getBaseMapper() {
		return this.tbSysUploadMapper;
	}

	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 * 
	 * @param oid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Override
	public DefaultResult<TbSysUpload> selectByPrimaryKeySimple(String oid) throws ServiceException, Exception {
		if (null == oid || StringUtils.isBlank(oid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysUpload> result = new DefaultResult<TbSysUpload>();
		TbSysUpload value = this.tbSysUploadMapper.selectByPrimaryKeySimple(oid);
		if (value != null) {
			result.setValue(value);
		} else { 
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}

	/**
	 * 刪除 IS_FILE='N' && CONTENT 欄位放byte 類別為暫存的資料
	 * 
	 * @param systemId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<Boolean> deleteTmpContentBySystem(String systemId) throws ServiceException, Exception {
		if (StringUtils.isBlank(systemId)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("system", systemId);
		paramMap.put("type", UploadTypes.IS_TEMP);
		paramMap.put("isFile", YesNo.NO);
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		result.setValue( this.tbSysUploadMapper.deleteTmpContentBySystem(paramMap) );
		if (result.getValue() != null && result.getValue()) {
			result.setMessage(BaseSystemMessage.deleteSuccess());
		} else {
			result.setMessage(BaseSystemMessage.deleteFail());
		}
		return result;
	}
	
}
