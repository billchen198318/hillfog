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
import org.qifu.base.model.DefaultResult;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysMailHelper;
import org.qifu.core.mapper.TbSysMailHelperMapper;
import org.qifu.core.service.ISysMailHelperService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysMailHelperServiceImpl extends BaseService<TbSysMailHelper, String> implements ISysMailHelperService<TbSysMailHelper, String> {
	
	@Autowired
	TbSysMailHelperMapper tbSysMailHelperMapper;
	
	@Override
	protected IBaseMapper<TbSysMailHelper, String> getBaseMapper() {
		return this.tbSysMailHelperMapper;
	}
	
	@Override
	public DefaultResult<List<TbSysMailHelper>> findForJobList(String mailId) throws ServiceException, Exception {
		if ( StringUtils.isBlank(mailId) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (mailId.endsWith("%")) {
			paramMap.put("mailId", mailId);
		} else {
			paramMap.put("mailId", mailId+"%");
		}
		DefaultResult<List<TbSysMailHelper>> result = new DefaultResult<List<TbSysMailHelper>>();
		List<TbSysMailHelper> searchList = this.tbSysMailHelperMapper.findForJobList(paramMap);
		if (searchList!=null && searchList.size()>0) {
			result.setValue(searchList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}
	
	@Override
	public String findForMaxMailId(String mailId) throws ServiceException, Exception {
		if (StringUtils.isBlank(mailId)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (mailId.endsWith("%")) {
			paramMap.put("mailId", mailId);
		} else {
			paramMap.put("mailId", mailId+"%");
		}
		return this.tbSysMailHelperMapper.findForMaxMailId(paramMap);
	}	
	
	@Override
	public String findForMaxMailIdComplete(String mailId) throws ServiceException, Exception {
		if (StringUtils.isBlank(mailId) || !SimpleUtils.isDate(mailId)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		String maxMailId = this.findForMaxMailId(mailId);
		if (StringUtils.isBlank(maxMailId)) {
			return mailId + "000000001";
		}
		int maxSeq = Integer.parseInt( maxMailId.substring(8, 17) ) + 1;
		if (maxSeq > 999999999) {
			throw new ServiceException(BaseSystemMessage.dataErrors() + " over max mail-id 999999999!");
		}
		return mailId + StringUtils.leftPad(String.valueOf(maxSeq), 9, "0");
	}	
	
}
