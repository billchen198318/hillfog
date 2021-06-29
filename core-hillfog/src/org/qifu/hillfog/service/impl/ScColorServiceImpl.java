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
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfScColor;
import org.qifu.hillfog.mapper.HfScColorMapper;
import org.qifu.hillfog.service.IScColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class ScColorServiceImpl extends BaseService<HfScColor, String> implements IScColorService<HfScColor, String> {
	
	@Autowired
	HfScColorMapper hfScColorMapper;

	@Override
	protected IBaseMapper<HfScColor, String> getBaseMapper() {
		return this.hfScColorMapper;
	}

	@Override
	public HfScColor findByDefault(String scorecardOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(scorecardOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecardOid);
		List<HfScColor> searchList = this.hfScColorMapper.findByDefault(paramMap);
		if (null == searchList || searchList.size() < 1) {
			return null;
		}
		return searchList.get(0);
	}

	@Override
	public HfScColor findByScore(String scorecardOid, int score) throws ServiceException, Exception {
		if (StringUtils.isBlank(scorecardOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("scOid", scorecardOid);
		paramMap.put("score", score);
		List<HfScColor> searchList = this.hfScColorMapper.findByScore(paramMap);
		if (null == searchList || searchList.size() < 1) {
			return null;
		}
		return searchList.get(0);
	}
	
}
