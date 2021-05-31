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
package org.qifu.hillfog.service;

import java.util.List;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.service.IBaseService;
import org.qifu.hillfog.entity.HfEmployee;

public interface IEmployeeService<T, E> extends IBaseService<HfEmployee, String> {
	
	public List<String> findInputAutocomplete() throws ServiceException, Exception;
	
	/**
	 * KPI的負責人
	 * 
	 * @param kpiId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<List<HfEmployee>> findKpiOwner(String kpiId) throws ServiceException, Exception;
	
	/**
	 * KPI的負責人
	 * 
	 * @param kpiId
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<String> findInputAutocompleteByKpiId(String kpiId) throws ServiceException, Exception;
	
	/**
	 * Objective的負責人
	 * 
	 * @param oid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<String> findInputAutocompleteByObjectiveOid(String oid) throws ServiceException, Exception;
	
	/**
	 * PDCA 負責人
	 * 
	 * @param pdcaOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<List<HfEmployee>> findPdcaOwner(String pdcaOid) throws ServiceException, Exception;
	
	/**
	 * PDCA-item 負責人
	 * 
	 * @param pdcaOid
	 * @param itemOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public DefaultResult<List<HfEmployee>> findPdcaItemOwner(String pdcaOid, String itemOid) throws ServiceException, Exception;
	
	/**
	 * PDCA 負責人
	 * 
	 * @param pdcaOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<String> findInputAutocompleteByPdcaOid(String pdcaOid) throws ServiceException, Exception;
	
	/**
	 * PDCA-item 負責人
	 * 
	 * @param pdcaOid
	 * @param itemOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<String> findInputAutocompleteByPdcaItemOid(String pdcaOid, String itemOid) throws ServiceException, Exception;
	
	public String getPagefieldValue(HfEmployee employee);
	
}
