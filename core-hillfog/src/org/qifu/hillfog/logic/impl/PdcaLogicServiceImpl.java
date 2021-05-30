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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.SortType;
import org.qifu.base.model.ZeroKeyProvide;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.util.UploadSupportUtils;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.entity.HfPdcaAttc;
import org.qifu.hillfog.entity.HfPdcaCloseReq;
import org.qifu.hillfog.entity.HfPdcaItem;
import org.qifu.hillfog.entity.HfPdcaItemOwner;
import org.qifu.hillfog.entity.HfPdcaOwner;
import org.qifu.hillfog.logic.IPdcaLogicService;
import org.qifu.hillfog.model.PDCABase;
import org.qifu.hillfog.service.IEmployeeService;
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IPdcaAttcService;
import org.qifu.hillfog.service.IPdcaCloseReqService;
import org.qifu.hillfog.service.IPdcaItemOwnerService;
import org.qifu.hillfog.service.IPdcaItemService;
import org.qifu.hillfog.service.IPdcaOwnerService;
import org.qifu.hillfog.service.IPdcaService;
import org.qifu.hillfog.vo.PdcaItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class PdcaLogicServiceImpl extends BaseLogicService implements IPdcaLogicService {
	protected Logger logger = LogManager.getLogger(PdcaLogicServiceImpl.class);
	
	private static final int MAX_LENGTH = 500; // for description field
	
	@Autowired
	IObjectiveService<HfObjective, String> objectiveService;
	
	@Autowired
	IKpiService<HfKpi, String> kpiService;
	
	@Autowired
	IPdcaService<HfPdca, String> pdcaService;
	
	@Autowired
	IPdcaOwnerService<HfPdcaOwner, String> pdcaOwnerService;
	
	@Autowired
	IPdcaAttcService<HfPdcaAttc, String> pdcaAttcService;
	
	@Autowired
	IPdcaItemService<HfPdcaItem, String> pdcaItemService;
	
	@Autowired
	IPdcaItemOwnerService<HfPdcaItemOwner, String> pdcaItemOwnerService;
	
	@Autowired
	IPdcaCloseReqService<HfPdcaCloseReq, String> pdcaCloseReqService;
	
	@Autowired
	IEmployeeService<HfEmployee, String> employeeService;	
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<HfPdca> create(HfPdca pdca, String masterType, String masterOid, List<String> ownerList, List<String> uploadOidsList, 
			List<Map<String, Object>> planMapList, List<Map<String, Object>> doMapList, List<Map<String, Object>> checkMapList, List<Map<String, Object>> actMapList) throws ServiceException, Exception {
		if (null == pdca || StringUtils.isBlank(masterOid) || null == ownerList || null == uploadOidsList
				|| null == planMapList || null == doMapList || null == checkMapList || null == actMapList) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		if (ownerList.size() < 1) {
			throw new ServiceException("Need add PDCA owner!");
		}
		if (planMapList.size() < 1) {
			throw new ServiceException("Need add Plan result!");
		}
		String head = "OKR";
		HfObjective objective = null;
		HfKpi kpi = null;
		if (PDCABase.SOURCE_MASTER_OBJECTIVE_TYPE.equals(masterType)) {
			objective = this.objectiveService.selectByPrimaryKey(masterOid).getValueEmptyThrowMessage();
		} else {
			kpi = this.kpiService.selectByPrimaryKey(masterOid).getValueEmptyThrowMessage();
			head = kpi.getId();
		}
		this.setStringValueMaxLength(pdca, "description", MAX_LENGTH);
		pdca.setStartDate( this.defaultString(pdca.getStartDate()).replaceAll("-", "").replaceAll("/", "") );
		pdca.setEndDate( this.defaultString(pdca.getEndDate()).replaceAll("-", "").replaceAll("/", "") );
		pdca.setPdcaNum( this.pdcaService.selectMaxPdcaNum(head) );
		pdca.setMstOid(masterOid);
		pdca.setMstType(masterType);
		DefaultResult<HfPdca> mResult = this.pdcaService.insert(pdca);
		pdca = mResult.getValueEmptyThrowMessage();
		this.createPdcaOwner(pdca, ownerList);
		this.updateUploadType(pdca, uploadOidsList);
		Map<String, HfPdcaItem> planItemMap = new HashMap<String, HfPdcaItem>();
		Map<String, HfPdcaItem> doItemMap = new HashMap<String, HfPdcaItem>();
		Map<String, HfPdcaItem> checkItemMap = new HashMap<String, HfPdcaItem>();
		this.createPdcaItem(pdca, PDCABase.TYPE_P, planMapList, null, planItemMap);
		this.createPdcaItem(pdca, PDCABase.TYPE_D, doMapList, planItemMap, doItemMap);
		this.createPdcaItem(pdca, PDCABase.TYPE_C, checkMapList, doItemMap, checkItemMap);
		this.createPdcaItem(pdca, PDCABase.TYPE_A, actMapList, checkItemMap, null);
		planItemMap.clear();
		doItemMap.clear();
		checkItemMap.clear();
		return mResult;
	}
	
	private void createPdcaItem(HfPdca pdca, String itemType, List<Map<String, Object>> itemMapList, Map<String, HfPdcaItem> parentMap, Map<String, HfPdcaItem> fillMap) throws ServiceException, Exception {
		int size = 0;
		for (Map<String, Object> itemDataMap : itemMapList) {
			HfPdcaItem pdcaItem = new HfPdcaItem();
			BeanUtils.populate(pdcaItem, itemDataMap);
			pdcaItem.setType(itemType);
			pdcaItem.setPdcaOid(pdca.getOid());
			if (PDCABase.TYPE_P.equals(itemType)) {
				pdcaItem.setParentOid(ZeroKeyProvide.OID_KEY);
			} else {
				if (parentMap == null || parentMap.get((String)itemDataMap.get("parentOid")) == null) {
					throw new ServiceException( BaseSystemMessage.dataErrors() );
				}
				HfPdcaItem parentItem = parentMap.get((String)itemDataMap.get("parentOid"));
				pdcaItem.setParentOid( parentItem.getOid() ); // 設定上正確存到DB中的 父 PDCA_ITEM OID 的值
			}
			if (PleaseSelect.noSelect(pdcaItem.getParentOid())) {
				throw new ServiceException("Please select parent item for PDCA item(" + pdcaItem.getName() + ")");
			}
			this.setStringValueMaxLength(pdcaItem, "description", MAX_LENGTH);
			pdcaItem.setStartDate( pdcaItem.getStartDate().replaceAll("-", "").replaceAll("/", "") );
			pdcaItem.setEndDate( pdcaItem.getEndDate().replaceAll("-", "").replaceAll("/", "") );
			pdcaItem = this.pdcaItemService.insert(pdcaItem).getValueEmptyThrowMessage();
			if (null != fillMap) {
				fillMap.put((String)itemDataMap.get("oid"), pdcaItem); // 這邊紀錄的是html畫面上的變數oid
			}
			List<String> ownerList = (List<String>) itemDataMap.get("ownerList");
			this.createPdcaItemOwner(pdca.getOid(), pdcaItem.getOid(), ownerList);
			size++;
		}
		if (PDCABase.TYPE_P.equals(itemType) && size < 1) {
			throw new ServiceException("At least one Plan item is required!");
		}
	}
	
	private void createPdcaItemOwner(String pdcaOid, String itemOid, List<String> ownerList) throws ServiceException, Exception {
		int size = 0;
		for (String str : ownerList) {
			String tmp[] = str.split("/");
			if (tmp == null || tmp.length != 3) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
			String account = StringUtils.deleteWhitespace(tmp[1]);
			if (this.isBlank(account)) {
				continue;
			}
			HfPdcaItemOwner owner = new HfPdcaItemOwner();
			owner.setOwnerUid(account);
			owner.setPdcaOid(pdcaOid);
			owner.setItemOid(itemOid);
			this.pdcaItemOwnerService.insert(owner);
			size++;
		}
		if (size < 1) {
			throw new ServiceException( "Need add PDCA item owner!" );
		}		
	}
	
	private void deleteAttc(HfPdca pdca) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdca.getOid());
		List<HfPdcaAttc> attcList = this.pdcaAttcService.selectListByParams(paramMap).getValue();
		if (null == attcList || attcList.size() < 1) {
			return;
		}
		for (HfPdcaAttc attc : attcList) {
			UploadSupportUtils.updateType(attc.getUploadOid(), UploadTypes.IS_TEMP);
		}
		for (HfPdcaAttc attc : attcList) {
			this.pdcaAttcService.delete(attc);
		}
	}
	
	private void updateUploadType(HfPdca pdca, List<String> uploadOidsList) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdca.getOid());
		List<HfPdcaAttc> attcList = this.pdcaAttcService.selectListByParams(paramMap).getValue();
		if (null != attcList && attcList.size() > 0) {
			for (HfPdcaAttc attc : attcList) {
				this.pdcaAttcService.delete(attc);
			}			
		}
		for (String uploadOid : uploadOidsList) {
			TbSysUpload upload = UploadSupportUtils.findUploadNoByteContent(uploadOid);
			if (!UploadTypes.IS_TEMP.equals(upload.getType())) {
				continue;
			}
			UploadSupportUtils.updateType(uploadOid, UploadTypes.IS_COMMON);
			HfPdcaAttc attc = new HfPdcaAttc(); 
			attc.setPdcaOid(pdca.getOid());
			attc.setUploadOid(uploadOid);
			this.pdcaAttcService.insert(attc);
		}
	}
	
	private void createPdcaOwner(HfPdca pdca, List<String> ownerList) throws ServiceException, Exception {
		int size = 0;
		for (String str : ownerList) {
			String tmp[] = str.split("/");
			if (tmp == null || tmp.length != 3) {
				throw new ServiceException( BaseSystemMessage.searchNoData() );
			}
			String account = StringUtils.deleteWhitespace(tmp[1]);
			if (this.isBlank(account)) {
				continue;
			}
			HfPdcaOwner owner = new HfPdcaOwner();
			owner.setOwnerUid(account);
			owner.setPdcaOid(pdca.getOid());
			this.pdcaOwnerService.insert(owner);
			size++;
		}
		if (size < 1) {
			throw new ServiceException( BaseSystemMessage.dataErrors() );
		}
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<PdcaItems> findPdcaItems(String pdcaOid, boolean fetchOwner) throws ServiceException, Exception {
		if (this.isBlank(pdcaOid)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() ); 
		}
		return this.findPdcaItems( this.pdcaService.selectByPrimaryKey(pdcaOid).getValueEmptyThrowMessage(), fetchOwner );
	}

	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<PdcaItems> findPdcaItems(HfPdca pdca, boolean fetchOwner) throws ServiceException, Exception {
		if (null == pdca || this.isBlank(pdca.getOid()) || this.isBlank(pdca.getName())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() ); 
		}
		PdcaItems pdcaItems = new PdcaItems( pdca );
		DefaultResult<PdcaItems> result = new DefaultResult<PdcaItems>();
		result.setValue(pdcaItems);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdca.getOid());
		paramMap.put("type", PDCABase.TYPE_P);
		pdcaItems.setPlanItemList( this.pdcaItemService.selectListByParams(paramMap, "START_DATE", SortType.ASC).getValue() );
		paramMap.put("type", PDCABase.TYPE_D);
		pdcaItems.setDoItemList( this.pdcaItemService.selectListByParams(paramMap, "START_DATE", SortType.ASC).getValue() );
		paramMap.put("type", PDCABase.TYPE_C);
		pdcaItems.setCheckItemList( this.pdcaItemService.selectListByParams(paramMap, "START_DATE", SortType.ASC).getValue() );
		paramMap.put("type", PDCABase.TYPE_A);
		pdcaItems.setActItemList( this.pdcaItemService.selectListByParams(paramMap, "START_DATE", SortType.ASC).getValue() );
		paramMap.clear();
		if (!fetchOwner) {
			return result;
		}
		pdca.setOwnerNameList( this.employeeService.findInputAutocompleteByPdcaOid(pdca.getOid()) );
		for (HfPdcaItem item : pdcaItems.getPlanItemList()) {
			item.setOwnerNameList( this.employeeService.findInputAutocompleteByPdcaItemOid(pdca.getOid(), item.getOid()) );
		}
		for (HfPdcaItem item : pdcaItems.getDoItemList()) {
			item.setOwnerNameList( this.employeeService.findInputAutocompleteByPdcaItemOid(pdca.getOid(), item.getOid()) );
		}
		for (HfPdcaItem item : pdcaItems.getCheckItemList()) {
			item.setOwnerNameList( this.employeeService.findInputAutocompleteByPdcaItemOid(pdca.getOid(), item.getOid()) );
		}
		for (HfPdcaItem item : pdcaItems.getActItemList()) {
			item.setOwnerNameList( this.employeeService.findInputAutocompleteByPdcaItemOid(pdca.getOid(), item.getOid()) );
		}		
		return result;
	}

	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<HfPdca> update(HfPdca pdca, List<String> ownerList, List<String> uploadOidsList, 
			List<Map<String, Object>> planMapList, List<Map<String, Object>> doMapList, List<Map<String, Object>> checkMapList, List<Map<String, Object>> actMapList) throws ServiceException, Exception {
		if (null == pdca || StringUtils.isBlank(pdca.getOid()) || null == ownerList || null == uploadOidsList
				|| null == planMapList || null == doMapList || null == checkMapList || null == actMapList) {
			throw new ServiceException( BaseSystemMessage.objectNull() );
		}
		if (ownerList.size() < 1) {
			throw new ServiceException("Need add PDCA owner!");
		}
		if (planMapList.size() < 1) {
			throw new ServiceException("Need add Plan result!");
		}
		
		HfPdca checkPdca = this.pdcaService.selectByPrimaryKey(pdca.getOid()).getValueEmptyThrowMessage();
		if (checkPdca.getConfirmDate() != null || !this.isBlank(checkPdca.getConfirmUid())) {
			throw new ServiceException("Cannot update, this PDCA project status is confirm!");
		}
		
		pdca.setMstOid(checkPdca.getMstOid());
		pdca.setMstType(checkPdca.getMstType());
		HfPdca checkUkPdca = this.pdcaService.selectByUniqueKey(checkPdca).getValue(); 
		if (checkUkPdca != null) {
			if (!pdca.getOid().equals(checkUkPdca.getOid())) {
				throw new ServiceException("Please change name, has other PDCA project use.");
			}
		}
		
		this.setStringValueMaxLength(pdca, "description", MAX_LENGTH);
		pdca.setStartDate( this.defaultString(pdca.getStartDate()).replaceAll("-", "").replaceAll("/", "") );
		pdca.setEndDate( this.defaultString(pdca.getEndDate()).replaceAll("-", "").replaceAll("/", "") );
		DefaultResult<HfPdca> mResult = this.pdcaService.update(pdca);
		pdca = mResult.getValueEmptyThrowMessage();
		this.deltePdcaOwner(pdca);
		this.createPdcaOwner(pdca, ownerList);
		// 取出之前的attc
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdca.getOid());
		List<HfPdcaAttc> beforeAttcList = this.pdcaAttcService.selectListByParams(paramMap).getValue();
		this.updateUploadType(pdca, uploadOidsList);
		this.updateNoNeedAttcUploadTypeForBeforeData(uploadOidsList, beforeAttcList);
		this.deletePdcaItemAndItemOwner(pdca);
		Map<String, HfPdcaItem> planItemMap = new HashMap<String, HfPdcaItem>();
		Map<String, HfPdcaItem> doItemMap = new HashMap<String, HfPdcaItem>();
		Map<String, HfPdcaItem> checkItemMap = new HashMap<String, HfPdcaItem>();
		this.createPdcaItem(pdca, PDCABase.TYPE_P, planMapList, null, planItemMap);
		this.createPdcaItem(pdca, PDCABase.TYPE_D, doMapList, planItemMap, doItemMap);
		this.createPdcaItem(pdca, PDCABase.TYPE_C, checkMapList, doItemMap, checkItemMap);
		this.createPdcaItem(pdca, PDCABase.TYPE_A, actMapList, checkItemMap, null);
		planItemMap.clear();
		doItemMap.clear();
		checkItemMap.clear();
		return mResult;
	}	
	
	private void deletePdcaItemAndItemOwner(HfPdca pdca) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdca.getOid());
		List<HfPdcaItem> itemList = this.pdcaItemService.selectListByParams(paramMap).getValue();
		for (HfPdcaItem item : itemList) {
			this.pdcaItemService.delete(item);
		}
		List<HfPdcaItemOwner> itemOwnerList = this.pdcaItemOwnerService.selectListByParams(paramMap).getValue();
		for (HfPdcaItemOwner itemOwner : itemOwnerList) {
			this.pdcaItemOwnerService.delete(itemOwner);
		}
	}
	
	/**
	 * 更新時, 也許之前上傳的檔案不需要了, 此時將不需要保留的 TB_SYS_UPLOAD 狀態改為 tmp
	 * 
	 * @param uploadOidsList
	 * @param beforeAttcList
	 * @throws ServiceException
	 * @throws Exception
	 */
	private void updateNoNeedAttcUploadTypeForBeforeData(List<String> uploadOidsList, List<HfPdcaAttc> beforeAttcList) throws ServiceException, Exception {
		if (null == uploadOidsList || uploadOidsList.size() < 1 || null == beforeAttcList || beforeAttcList.size() < 1) {
			return;
		}
		for (HfPdcaAttc attc : beforeAttcList) {
			boolean found = false;
			for (String currentUploadOid : uploadOidsList) {
				if (currentUploadOid.equals(attc.getUploadOid())) {
					found = true;
				}
			}
			if (!found) {
				UploadSupportUtils.updateType(attc.getUploadOid(), UploadTypes.IS_TEMP);
			}
		}
	}
	
	private void deltePdcaOwner(HfPdca pdca) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaOid", pdca.getOid());
		List<HfPdcaOwner> ownerList = this.pdcaOwnerService.selectListByParams(paramMap).getValue();
		for (HfPdcaOwner owner : ownerList) {
			this.pdcaOwnerService.delete(owner);
		}
	}

	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> delete(HfPdca pdca) throws ServiceException, Exception {
		if (null == pdca || this.isBlank(pdca.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() ); 
		}
		pdca = this.pdcaService.selectByEntityPrimaryKey(pdca).getValueEmptyThrowMessage();
		if (pdca.getConfirmDate() != null || !this.isBlank(pdca.getConfirmUid())) {
			throw new ServiceException("Cannot delete, this PDCA project status is confirm!");
		}
		this.deltePdcaOwner(pdca);
		this.deletePdcaItemAndItemOwner(pdca);
		this.deleteAttc(pdca);
		return this.pdcaService.delete(pdca);
	}
	
}
