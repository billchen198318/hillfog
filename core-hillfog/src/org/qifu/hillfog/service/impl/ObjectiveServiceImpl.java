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
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.mapper.HfObjectiveMapper;
import org.qifu.hillfog.service.IObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class ObjectiveServiceImpl extends BaseService<HfObjective, String> implements IObjectiveService<HfObjective, String> {
	
	@Autowired
	HfObjectiveMapper hfObjectiveMapper;
	
	@Override
	protected IBaseMapper<HfObjective, String> getBaseMapper() {
		return this.hfObjectiveMapper;
	}

	@Override
	public DefaultResult<List<HfObjective>> selectQueryObjectiveList(String ownerAccount, String departmentId, String startDate, String endDate, String name) throws ServiceException, Exception {
		DefaultResult<List<HfObjective>> result = new DefaultResult<List<HfObjective>>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtils.isBlank(ownerAccount)) {
			paramMap.put("ownerAccount", ownerAccount);
		}
		if (!StringUtils.isBlank(departmentId)) {
			paramMap.put("departmentId", departmentId);
		}
		if (!StringUtils.isBlank(startDate)) {
			paramMap.put("startDate", this.defaultString(startDate).replaceAll("-", "").replaceAll("/", ""));
		}
		if (!StringUtils.isBlank(endDate)) {
			paramMap.put("endDate", this.defaultString(endDate).replaceAll("-", "").replaceAll("/", ""));
		}
		if (!StringUtils.isBlank(name)) {
			paramMap.put("name", "%"+name+"%");
		}
		List<HfObjective> searchList = this.hfObjectiveMapper.selectQueryObjectiveList(paramMap);
		if (searchList.size() < 1) {
			result.setMessage( BaseSystemMessage.searchNoData() );
		} else {
			result.setValue( searchList );
		}
		result.setSuccess( YesNo.YES );
		return result;
	}
	
}
