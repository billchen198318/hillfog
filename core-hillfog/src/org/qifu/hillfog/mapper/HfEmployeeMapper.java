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
package org.qifu.hillfog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.hillfog.entity.HfEmployee;

@Mapper
public interface HfEmployeeMapper extends IBaseMapper<HfEmployee, String> {
	
	/**
	 * KPI的負責人
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HfEmployee> findKpiOwner(Map<String, Object> paramMap);
	
	/**
	 * Objective的負責人
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HfEmployee> findObjectiveOwner(Map<String, Object> paramMap);
	
	/**
	 * PDCA 負責人
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HfEmployee> findPdcaOwner(Map<String, Object> paramMap);
	
	/**
	 * PDCA-item 負責人
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HfEmployee> findPdcaItemOwner(Map<String, Object> paramMap);
	
	/**
	 * Scorecard擁有的KPI的負責人
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<HfEmployee> findScorecardKpisOwner(Map<String, Object> paramMap);
	
}
