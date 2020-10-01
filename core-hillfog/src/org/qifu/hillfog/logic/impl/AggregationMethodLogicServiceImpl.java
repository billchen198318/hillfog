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
package org.qifu.hillfog.logic.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.hillfog.entity.HfAggregationMethod;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.logic.IAggregationMethodLogicService;
import org.qifu.hillfog.service.IAggregationMethodService;
import org.qifu.hillfog.service.IKpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class AggregationMethodLogicServiceImpl extends BaseLogicService implements IAggregationMethodLogicService {

	@Autowired
	IAggregationMethodService<HfAggregationMethod, String> aggregationMethodService;
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;	
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )   	
	@Override
	public DefaultResult<Boolean> delete(HfAggregationMethod aggregationMethod) throws ServiceException, Exception {
		if (null == aggregationMethod || this.isBlank(aggregationMethod.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		aggregationMethod = this.aggregationMethodService.selectByEntityPrimaryKey(aggregationMethod).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cal", aggregationMethod.getAggrId());
		if (this.kpiService.count(paramMap) > 0) {
			throw new ServiceException( BaseSystemMessage.dataCannotDelete() ); 
		}
		return this.aggregationMethodService.delete(aggregationMethod);
	}
	
}
