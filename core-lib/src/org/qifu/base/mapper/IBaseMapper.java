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
package org.qifu.base.mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @param <T>	MyBatis Entity
 * @param <K>	PK屬性
 */
public interface IBaseMapper<T, K> {
	
	public T selectByPrimaryKey(K oid);
	
	public List<T> selectListByParams(Map<String, Object> paramMap);
	
	public int insert(T mapperObj);	
	
	public int update(T mapperObj);
	
	public Boolean delete(T mapperObj);
	
	public Long count(Map<String, Object> paramMap);	
	
	public List<Object> findPage(Map<String, Object> paramMap);
	
}
