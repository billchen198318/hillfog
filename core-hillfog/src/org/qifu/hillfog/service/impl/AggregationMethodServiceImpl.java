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
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.mapper.HfAggregationMethodMapper;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class AggregationMethodServiceImpl extends BaseService<HfAggregationMethod, String> implements IAggregationMethodService<HfAggregationMethod, String> {
	
	@Autowired
	HfAggregationMethodMapper aggregationMethodMapper;
	
	@Override
	protected IBaseMapper<HfAggregationMethod, String> getBaseMapper() {
		return this.aggregationMethodMapper;
	}

	@Override
	public Map<String, String> findMap(boolean pleaseSelect) throws ServiceException, Exception {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		List<HfAggregationMethod> listData = this.aggregationMethodMapper.findForSelectItem();
		for (int i = 0; listData != null && i < listData.size(); i++) {
			HfAggregationMethod aggrMethod = listData.get(i);
			dataMap.put(aggrMethod.getOid(), aggrMethod.getAggrId() + " - " + aggrMethod.getName());
		}
		return dataMap;
	}

	@Override
	public DefaultResult<HfAggregationMethod> findForSimple(String aggrId) throws ServiceException, Exception {
		if (StringUtils.isBlank(aggrId)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("aggrId", aggrId);
		List<HfAggregationMethod> aggrList = this.aggregationMethodMapper.selectForSimple();
		DefaultResult<HfAggregationMethod> result = new DefaultResult<HfAggregationMethod>();
		if (aggrList != null && aggrList.size() > 0) {
			result.setValue(aggrList.get(0));
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}
	
}
