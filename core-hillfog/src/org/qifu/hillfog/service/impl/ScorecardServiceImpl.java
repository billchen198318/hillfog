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

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfScorecard;
import org.qifu.hillfog.mapper.HfScorecardMapper;
import org.qifu.hillfog.service.IScorecardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class ScorecardServiceImpl extends BaseService<HfScorecard, String> implements IScorecardService<HfScorecard, String> {
	
	@Autowired
	HfScorecardMapper hfScorecardMapper;

	@Override
	protected IBaseMapper<HfScorecard, String> getBaseMapper() {
		return this.hfScorecardMapper;
	}

	@Override
	public DefaultResult<List<HfScorecard>> findListByParamsNoContent(Map<String, Object> paramMap) throws ServiceException, Exception {
		DefaultResult<List<HfScorecard>> result = new DefaultResult<List<HfScorecard>>();
		List<HfScorecard> searchList = this.hfScorecardMapper.findListByParamsNoContent(paramMap);
		if (searchList != null && searchList.size() > 0) {
			result.setValue(searchList);
		} else {
			result.setMessage( BaseSystemMessage.searchNoData() );
		}
		return result;
	}

	@Override
	public Map<String, String> findMap(boolean pleaseSelect) throws ServiceException, Exception {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		List<HfScorecard> scorecards = this.findListByParamsNoContent(null).getValue();
		if (null == scorecards) {
			return dataMap;
		}
		for (HfScorecard sc : scorecards) {
			dataMap.put(sc.getOid(), sc.getName());
		}
		return dataMap;
	}
	
}
