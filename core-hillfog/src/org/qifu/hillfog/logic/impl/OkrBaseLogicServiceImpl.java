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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfInitiatives;
import org.qifu.hillfog.entity.HfKeyRes;
import org.qifu.hillfog.entity.HfKeyResVal;
import org.qifu.hillfog.entity.HfObjDept;
import org.qifu.hillfog.entity.HfObjOwner;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfOrgDept;
import org.qifu.hillfog.logic.IOkrBaseLogicService;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IInitiativesService;
import org.qifu.hillfog.service.IKeyResService;
import org.qifu.hillfog.service.IKeyResValService;
import org.qifu.hillfog.service.IObjDeptService;
import org.qifu.hillfog.service.IObjOwnerService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IOrgDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class OkrBaseLogicServiceImpl extends BaseLogicService implements IOkrBaseLogicService {
	protected Logger logger = LogManager.getLogger(OkrBaseLogicServiceImpl.class);
	
	private static final int MAX_LENGTH = 500; // for description field
	private static final int MAX_CONTENT_LENGTH = 2000; // for initiative content field
	
	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;
	
	@Autowired
	IObjOwnerService<HfObjOwner, String> objOwnerService;
	
	@Autowired
	IObjDeptService<HfObjDept, String> objDeptService;
	
	@Autowired
	IKeyResService<HfKeyRes, String> keyResService;
	
	@Autowired
	IKeyResValService<HfKeyResVal, String> keyResValService;
	
	@Autowired
	IInitiativesService<HfInitiatives, String> initiativesService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;
	
	@Autowired
	IOrgDeptService<HfOrgDept, String> orgDeptService;	
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<HfObjective> create(HfObjective objective, List<String> objDeptList, List<String> objOwnerList, List<Map<String, Object>> keyResultMapList, List<Map<String, Object>> initiativesMapList) throws ServiceException, Exception {
		if (null == objective || null == objDeptList || null == objOwnerList || null == keyResultMapList || null == initiativesMapList) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		if (keyResultMapList.size() < 1) {
			throw new ServiceException("Need key result!");
		}
		this.setStringValueMaxLength(objective, "description", MAX_LENGTH);
		objective.setStartDate( this.defaultString(objective.getStartDate()).replaceAll("-", "").replaceAll("/", "") );
		objective.setEndDate( this.defaultString(objective.getEndDate()).replaceAll("-", "").replaceAll("/", "") );
		DefaultResult<HfObjective> mResult = this.objectiveService.insert(objective);
		objective = mResult.getValueEmptyThrowMessage();
		int size = this.createObjectiveOwner(objective, objOwnerList);
		size += this.createObjectiveDept(objective, objDeptList);
		if (size < 1) {
			throw new ServiceException("Objective's organization or owner need one record!");
		}
		this.createKeyResult(objective, keyResultMapList);
		this.createInitiative(objective, initiativesMapList);
		return mResult;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )  	
	@Override
	public DefaultResult<HfObjective> update(HfObjective objective, List<String> objDeptList, List<String> objOwnerList, List<Map<String, Object>> keyResultMapList, List<Map<String, Object>> initiativesMapList) throws ServiceException, Exception {
		if (null == objective || this.isBlank(objective.getOid()) || null == objDeptList || null == objOwnerList || null == keyResultMapList || null == initiativesMapList) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		if (keyResultMapList.size() < 1) {
			throw new ServiceException("Need key result!");
		}
		this.setStringValueMaxLength(objective, "description", MAX_LENGTH);
		objective.setStartDate( this.defaultString(objective.getStartDate()).replaceAll("-", "").replaceAll("/", "") );
		objective.setEndDate( this.defaultString(objective.getEndDate()).replaceAll("-", "").replaceAll("/", "") );
		HfObjective checkUkObjective = this.objectiveService.selectByUniqueKey(objective).getValue();
		if (checkUkObjective != null && !this.isBlank(checkUkObjective.getOid())) {
			if (!checkUkObjective.getOid().equals(objective.getOid())) {
				throw new ServiceException("Please change name, has other objective use.");
			}
		}
		DefaultResult<HfObjective> mResult = this.objectiveService.update(objective);
		objective = mResult.getValueEmptyThrowMessage();
		this.removeObjectiveOwner(objective);
		this.removeObjectiveDept(objective);
		int size = this.createObjectiveOwner(objective, objOwnerList);
		size += this.createObjectiveDept(objective, objDeptList);
		if (size < 1) {
			throw new ServiceException("Objective's organization or owner need one record!");
		}
		this.createOrUpdateOrRemoveKeyResult(objective, keyResultMapList);
		this.removeInitiative(objective);
		this.createInitiative(objective, initiativesMapList);
		return mResult;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} ) 	
	@Override
	public DefaultResult<Boolean> delete(HfObjective objective) throws ServiceException, Exception {
		if (null == objective || this.isBlank(objective.getOid())) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		this.removeInitiative(objective);
		this.removeObjectiveDept(objective);
		this.removeObjectiveOwner(objective);
		// remove key result & key result measure data.
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objective.getOid());		
		List<HfKeyRes> keyResList = this.keyResService.selectListByParams(paramMap).getValue();
		for (HfKeyRes keyRes : keyResList) {
			this.keyResService.delete(keyRes);
		}
		this.keyResValService.deleteForObjOid(objective.getOid());
		return this.objectiveService.delete(objective);
	}	
	
	private void createKeyResult(HfObjective objective, List<Map<String, Object>> keyResultMapList) throws ServiceException, Exception {
		for (int i = 0 ; i < keyResultMapList.size(); i++) {
			Map<String, Object> dataMap = keyResultMapList.get(i);
			HfKeyRes keyRes = new HfKeyRes();
			keyRes.setObjOid( objective.getOid() );
			keyRes.setName( String.valueOf(dataMap.get("name")) );
			keyRes.setTarget( new BigDecimal(NumberUtils.toInt(String.valueOf(dataMap.get("target")), 0)) );
			keyRes.setGpType( String.valueOf(dataMap.get("gpType")) );
			keyRes.setOpTarget( String.valueOf(dataMap.get("opTarget")) );
			keyRes.setDescription( String.valueOf(dataMap.get("description")) );
			this.setStringValueMaxLength(keyRes, "description", MAX_LENGTH);
			this.keyResService.insert(keyRes);
		}
	}
	
	private void createOrUpdateOrRemoveKeyResult(HfObjective objective, List<Map<String, Object>> keyResultMapList) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objective.getOid());		
		List<HfKeyRes> oldKeyResList = this.keyResService.selectListByParams(paramMap).getValue();
		for (HfKeyRes oldKeyRes : oldKeyResList) { // 1. first remove old key result data, record no found in update.
			boolean isFound = false;
			for (int i = 0 ; i < keyResultMapList.size(); i++) {
				Map<String, Object> dataMap = keyResultMapList.get(i);
				String oid = (String)dataMap.get("oid");
				if (oldKeyRes.getOid().equals(oid)) {
					isFound = true;
				}
			}
			if (!isFound) {
				this.keyResService.delete(oldKeyRes);
				// delete key result value
				this.keyResValService.deleteForObjOidAndResOid(objective.getOid(), oldKeyRes.getOid());
			}
		}
		for (int i = 0 ; i < keyResultMapList.size(); i++) { // 2. create or update key result.
			Map<String, Object> dataMap = keyResultMapList.get(i);
			String oid = (String)dataMap.get("oid");
			HfKeyRes keyRes = new HfKeyRes();
			keyRes.setObjOid( objective.getOid() );
			keyRes.setName( String.valueOf(dataMap.get("name")) );
			keyRes.setTarget( new BigDecimal(NumberUtils.toInt(String.valueOf(dataMap.get("target")), 0)) );
			keyRes.setGpType( String.valueOf(dataMap.get("gpType")) );
			keyRes.setOpTarget( String.valueOf(dataMap.get("opTarget")) );
			keyRes.setDescription( String.valueOf(dataMap.get("description")) );
			this.setStringValueMaxLength(keyRes, "description", MAX_LENGTH);			
			if (this.isBlank(oid) || "0".equals(oid)) { // create
				this.keyResService.insert(keyRes);
			} else { // update
				keyRes.setOid( oid );
				this.keyResService.update(keyRes);
			}
		}
	}
	
	private void createInitiative(HfObjective objective, List<Map<String, Object>> initiativesMapList) throws ServiceException, Exception {
		for (int i = 0 ; i < initiativesMapList.size(); i++) {
			Map<String, Object> dataMap = initiativesMapList.get(i);
			if (dataMap.get("content") == null || this.isBlank((String)dataMap.get("content"))) {
				continue;
			}
			HfInitiatives initiative = new HfInitiatives();
			initiative.setObjOid( objective.getOid() );
			initiative.setContent( String.valueOf(dataMap.get("content")) );
			this.setStringValueMaxLength(initiative, "content", MAX_CONTENT_LENGTH);
			this.initiativesService.insert(initiative);
		}
	}
	
	private int createObjectiveOwner(HfObjective objective, List<String> objOwnerList) throws ServiceException, Exception {
		int size = 0;
		for (String str : objOwnerList) {
			String tmp[] = str.split("/");
			if (tmp == null || tmp.length != 3) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
			String account = StringUtils.deleteWhitespace(tmp[1]);
			if (this.isBlank(account)) {
				continue;
			}
			HfObjOwner objOwner = new HfObjOwner();
			objOwner.setAccount(account);
			objOwner.setObjOid(objective.getOid());
			this.objOwnerService.insert(objOwner);
			size++;
		}
		return size;
	}
	
	private int createObjectiveDept(HfObjective objective, List<String> objDeptList) throws ServiceException, Exception {
		int size = 0;
		for (String str : objDeptList) {
			String orgId = StringUtils.deleteWhitespace(str.split("/")[0]);
			if (this.isBlank(orgId)) {
				continue;
			}
			HfObjDept objDept = new HfObjDept();
			objDept.setOrgId(orgId);
			objDept.setObjOid(objective.getOid());
			this.objDeptService.insert(objDept);
			size++;
		}
		return size;
	}
	
	private void removeInitiative(HfObjective objective) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objective.getOid());
		List<HfInitiatives> initiativeList = this.initiativesService.selectListByParams(paramMap).getValue();
		for (HfInitiatives initiative : initiativeList) {
			this.initiativesService.delete(initiative);
		}
	}
	
	private void removeObjectiveOwner(HfObjective objective) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objective.getOid());
		List<HfObjOwner> ownerList = this.objOwnerService.selectListByParams(paramMap).getValue();
		for (HfObjOwner owner : ownerList) {
			this.objOwnerService.delete(owner);
		}
	}
	
	private void removeObjectiveDept(HfObjective objective) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("objOid", objective.getOid());
		List<HfObjDept> deptList = this.objDeptService.selectListByParams(paramMap).getValue();
		for (HfObjDept dept : deptList) {
			this.objDeptService.delete(dept);
		}
	}
	
}
