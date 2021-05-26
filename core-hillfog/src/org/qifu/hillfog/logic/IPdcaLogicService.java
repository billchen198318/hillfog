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
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.vo.PdcaItems;

public interface IPdcaLogicService {
	
	public DefaultResult<HfPdca> create(HfPdca pdca, String masterType, String masterOid, List<String> ownerList, List<String> uploadOidsList,
			List<Map<String, Object>> planMapList, List<Map<String, Object>> doMapList, List<Map<String, Object>> checkMapList, List<Map<String, Object>> actMapList) throws ServiceException, Exception;
	
	public DefaultResult<PdcaItems> findPdcaItems(String pdcaOid) throws ServiceException, Exception;
	
}