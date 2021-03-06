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
package org.qifu.hillfog.logic;

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.hillfog.entity.HfScorecard;

public interface IScorecardLogicService {
	
	public DefaultResult<HfScorecard> create(HfScorecard scorecard, List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception;
	
	public DefaultResult<HfScorecard> update(HfScorecard scorecard, List<Map<String, Object>> perspectivesDataMapList) throws ServiceException, Exception;
	
	public DefaultResult<Boolean> delete(HfScorecard scorecard) throws ServiceException, Exception;
	
	public DefaultResult<List<Map<String, Object>>> findPerspectivesItemForEditPage(String scorecardOid) throws ServiceException, Exception;
	
	public DefaultResult<Boolean> updateColor(
			HfScorecard scorecard, List<Map<String, Object>> defaultScoreColorDataMapList, List<Map<String, Object>> customScoreColorDataMapList) throws ServiceException, Exception;
	
}
