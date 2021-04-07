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
import org.qifu.hillfog.entity.HfKeyRes;
import org.qifu.hillfog.mapper.HfKeyResMapper;
import org.qifu.hillfog.service.IKeyResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class KeyResServiceImpl extends BaseService<HfKeyRes, String> implements IKeyResService<HfKeyRes, String> {
	
	@Autowired
	HfKeyResMapper hfKeyResMapper;
	
	@Override
	protected IBaseMapper<HfKeyRes, String> getBaseMapper() {
		return this.hfKeyResMapper;
	}

	@Override
	public Map<String, String> findSelectOptionsMapByObjectiveOid(boolean pleaseSelect, String objectiveOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(objectiveOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, String> optionsMap = PleaseSelect.pageSelectMap(pleaseSelect);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objectiveOid);
		DefaultResult<List<HfKeyRes>> searchResult = this.selectListByParams(paramMap, "NAME", SortType.ASC);
		for (int i = 0; searchResult.getValue() != null && i < searchResult.getValue().size(); i++) {
			HfKeyRes keyRes = searchResult.getValue().get(i);
			optionsMap.put(keyRes.getOid(), keyRes.getName());
		}
		return optionsMap;
	}
	
}
