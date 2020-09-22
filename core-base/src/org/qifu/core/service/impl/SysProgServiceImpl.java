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
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.SortType;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.mapper.TbSysProgMapper;
import org.qifu.core.service.ISysProgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysProgServiceImpl extends BaseService<TbSysProg, String> implements ISysProgService<TbSysProg, String> {
	
	@Autowired
	TbSysProgMapper tbSysProgMapper;
	
	@Override
	protected IBaseMapper<TbSysProg, String> getBaseMapper() {
		return this.tbSysProgMapper;
	}

	@Override
	public Map<String, String> findSysProgFolderMap(String basePath, String progSystem, String itemType, boolean pleaseSelect) throws ServiceException, Exception {
		if (StringUtils.isBlank(progSystem)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("progSystem", progSystem);
		params.put("itemType", itemType);
		DefaultResult<List<TbSysProg>> searchResult = this.selectListByParams(params);
		if (null == searchResult.getValue() || searchResult.getValue().size()<1) {
			return dataMap;
		}
		for (TbSysProg sysProg : searchResult.getValue()) {
			dataMap.put(sysProg.getOid(), sysProg.getProgId() + " - " + sysProg.getName());
		}
		return dataMap;
	}

	@Override
	public List<TbSysProg> findForInTheFolderMenuItems(String progSystem, String menuParentOid, String itemType) throws ServiceException, Exception {
		if (StringUtils.isBlank(progSystem)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progSystem", progSystem);
		if (!StringUtils.isBlank(menuParentOid)) {
			paramMap.put("menuParentOid", menuParentOid);
		}
		if (!StringUtils.isBlank(itemType)) {
			paramMap.put("itemType", itemType);
		}
		return this.tbSysProgMapper.findForInTheFolderMenuItems(paramMap);
	}
	
	@Override
	public List<TbSysProg> findForSystemItems(String progSystem) throws ServiceException, Exception {
		if (StringUtils.isBlank(progSystem)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progSystem", progSystem);
		DefaultResult<List<TbSysProg>> result = this.selectListByParams(paramMap, "PROG_ID", SortType.ASC);
		return result.getValue();
	}
	
}
