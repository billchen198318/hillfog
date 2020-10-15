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
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.SortType;
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.mapper.HfKpiMapper;
import org.qifu.hillfog.service.IKpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class KpiServiceImpl extends BaseService<HfKpi, String> implements IKpiService<HfKpi, String> {
	
	@Autowired
	HfKpiMapper kpiMapper;
	
	@Override
	protected IBaseMapper<HfKpi, String> getBaseMapper() {
		return this.kpiMapper;
	}

	@Override
	public Map<String, String> findMap(boolean pleaseSelect) throws ServiceException, Exception {
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		List<HfKpi> kpis = this.selectList("ID", SortType.ASC).getValue();
		if (null == kpis) {
			return dataMap;
		}
		for (HfKpi kpi : kpis) {
			dataMap.put(kpi.getOid(), kpi.getId() + " - " + kpi.getName());
		}
		return dataMap;
	}
	
}
