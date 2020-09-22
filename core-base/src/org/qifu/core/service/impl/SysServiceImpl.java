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

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSys;
import org.qifu.core.mapper.TbSysMapper;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysServiceImpl extends BaseService<TbSys, String> implements ISysService<TbSys, String> {
	
	@Autowired
	TbSysMapper tbSysMapper;
	
	@Override
	protected IBaseMapper<TbSys, String> getBaseMapper() {
		return this.tbSysMapper;
	}

	@Override
	public Map<String, String> findSysMap(boolean pleaseSelect) throws ServiceException, Exception {
		Map<String, String> sysMap = PleaseSelect.pageSelectMap(pleaseSelect);
		DefaultResult<List<TbSys>> searchResult = this.selectList();
		if (searchResult.getValue()==null || searchResult.getValue().size()<1) {
			return sysMap;
		}
		for (TbSys sys : searchResult.getValue()) {
			sysMap.put(sys.getOid(), sys.getSysId() + " - " + sys.getName());
		}
		return sysMap;
	}
	
}
