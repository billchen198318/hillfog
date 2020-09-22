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
package org.qifu.core.logic;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbSysJreport;
import org.qifu.core.entity.TbSysJreportParam;

public interface ISystemJreportLogicService {
	
	public DefaultResult<TbSysJreport> create(TbSysJreport report) throws ServiceException, Exception; 
	
	public DefaultResult<TbSysJreport> update(TbSysJreport report) throws ServiceException, Exception;
	
	public DefaultResult<Boolean> delete(TbSysJreport report) throws ServiceException, Exception;
	
	public DefaultResult<TbSysJreportParam> createParam(TbSysJreportParam reportParam, String reportOid) throws ServiceException, Exception;
	
	public DefaultResult<Boolean> deleteParam(TbSysJreportParam reportParam) throws ServiceException, Exception;
	
}
