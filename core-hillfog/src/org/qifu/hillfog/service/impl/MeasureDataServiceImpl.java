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
package org.qifu.hillfog.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.mapper.HfMeasureDataMapper;
import org.qifu.hillfog.service.IMeasureDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class MeasureDataServiceImpl extends BaseService<HfMeasureData, String> implements IMeasureDataService<HfMeasureData, String> {
	
	@Autowired
	HfMeasureDataMapper measureDataMapper;
	
	@Override
	protected IBaseMapper<HfMeasureData, String> getBaseMapper() {
		return this.measureDataMapper;
	}

	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public void deleteByKpiId(String kpiId) throws ServiceException, Exception {
		if (StringUtils.isBlank(kpiId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpiId);
		this.measureDataMapper.deleteByKpiId(paramMap);
	}

	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public void deleteByOrgId(String orgId) throws ServiceException, Exception {
		if (StringUtils.isBlank(orgId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orgId", orgId);		
		this.measureDataMapper.deleteByOrgId(paramMap);
	}

	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public void deleteByAccount(String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(account)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("account", account);	
		this.measureDataMapper.deleteByAccount(paramMap);
	}
	
}
