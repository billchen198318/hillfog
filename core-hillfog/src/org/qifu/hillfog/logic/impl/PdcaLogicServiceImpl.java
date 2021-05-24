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
import org.qifu.base.model.ZeroKeyProvide;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.util.UploadSupportUtils;
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
import org.qifu.hillfog.service.IKpiService;
import org.qifu.hillfog.service.IObjectiveService;
import org.qifu.hillfog.service.IPdcaAttcService;
import org.qifu.hillfog.service.IPdcaCloseReqService;
import org.qifu.hillfog.service.IPdcaItemOwnerService;
import org.qifu.hillfog.service.IPdcaItemService;
import org.qifu.hillfog.service.IPdcaOwnerService;
import org.qifu.hillfog.service.IPdcaService;
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
		DefaultResult<HfPdca> mResult = this.pdcaService.insert(pdca);
		pdca = mResult.getValueEmptyThrowMessage();
		this.createPdcaOwner(pdca, ownerList);
		this.updateUploadType(uploadOidsList);
		this.createPdcaItem(pdca, PDCABase.TYPE_P, planMapList);
		this.createPdcaItem(pdca, PDCABase.TYPE_D, doMapList);
		this.createPdcaItem(pdca, PDCABase.TYPE_C, checkMapList);
		this.createPdcaItem(pdca, PDCABase.TYPE_A, actMapList);
		return mResult;
	}
	
	private void createPdcaItem(HfPdca pdca, String itemType, List<Map<String, Object>> itemMapList) throws ServiceException, Exception {
		int size = 0;
		for (Map<String, Object> itemDataMap : itemMapList) {
			HfPdcaItem pdcaItem = new HfPdcaItem();
			BeanUtils.populate(pdcaItem, itemDataMap);
			pdcaItem.setType(itemType);
			pdcaItem.setPdcaOid(pdca.getOid());
			if (PDCABase.TYPE_P.equals(itemType)) {
				pdcaItem.setParentOid(ZeroKeyProvide.OID_KEY);
			}
			if (PleaseSelect.noSelect(pdcaItem.getParentOid())) {
				throw new ServiceException("Please select parent item for PDCA item(" + pdcaItem.getName() + ")");
			}
			pdcaItem = this.pdcaItemService.insert(pdcaItem).getValueEmptyThrowMessage();
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
	
	private void updateUploadType(List<String> uploadOidsList) throws ServiceException, Exception {
		for (String uploadOid : uploadOidsList) {
			TbSysUpload upload = UploadSupportUtils.findUploadNoByteContent(uploadOid);
			if (!UploadTypes.IS_TEMP.equals(upload.getType())) {
				continue;
			}
			UploadSupportUtils.updateType(uploadOid, UploadTypes.IS_COMMON);
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
	
}
