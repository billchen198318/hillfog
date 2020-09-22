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
package org.qifu.base.model;

/**
 * 早期用hibernate時的 bambooBSC,bambooCORE,qifu2 的 EntityParameterGenerateUtil 才有需要用到這個
 */
@Deprecated
public interface EntityParameterGenerate {
	public static final String RETURN_SQL="sql";
	public static final String RETURN_PARAMS="params";
	public static final String NOT_ENTITY_BEAN="not entity bean.";
}
