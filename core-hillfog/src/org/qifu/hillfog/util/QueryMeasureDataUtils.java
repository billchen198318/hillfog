/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.hillfog.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.SortType;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.service.IMeasureDataService;
import org.qifu.hillfog.vo.ScoreCalculationData;

public class QueryMeasureDataUtils {
	
	private static IMeasureDataService<HfMeasureData, String> measureDataService;
	
	static {
		measureDataService = AppContext.context.getBean(IMeasureDataService.class);
	}
	
	public static List<HfMeasureData> queryForScoreCalculationData(ScoreCalculationData data) throws ServiceException, Exception {
		return queryForScoreCalculationData(
				data.getKpi().getId(), data.getFrequency(), data.getDate1(), data.getDate2(), data.getMeasureDataOrgId(), data.getMeasureDataAccount());
	}
	
	public static List<HfMeasureData> queryForScoreCalculationData(String kpiId, String frequency, String startDate, String endDate, String orgId, String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(kpiId) || StringUtils.isBlank(frequency) || StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)
				|| StringUtils.isBlank(orgId) || StringUtils.isBlank(account)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpiId);
		paramMap.put("frequency", frequency);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("orgId", orgId);
		paramMap.put("account", account);
		return measureDataService.selectListByParams(paramMap, "DATE", SortType.ASC).getValue();
	}
	
}
