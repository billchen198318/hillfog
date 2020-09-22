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
package org.qifu.core.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.YesNo;
import org.qifu.base.model.ZeroKeyProvide;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbSysMenu;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.ISystemMenuLogicService;
import org.qifu.core.model.MenuItemType;
import org.qifu.core.service.ISysMenuService;
import org.qifu.core.service.ISysProgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SystemMenuLogicServiceImpl extends BaseLogicService implements ISystemMenuLogicService {
	protected Logger logger=LogManager.getLogger(SystemMenuLogicServiceImpl.class);
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISysMenuService<TbSysMenu, String> sysMenuService;
	
	public SystemMenuLogicServiceImpl() {
		super();
	}
	
	/**
	 * 找出選單設定功能要的
	 * 已在選單的程式 與 同SYS的程式
	 * 
	 * map 中的  key 
	 * enable	- 已在選單的程式
	 * all	- 同SYS的程式
	 * 
	 * @param folderProgramOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Override
	public Map<String, List<TbSysProg>> findForMenuSettingsEnableAndAll(String folderProgramOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(folderProgramOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysProg> spResult = this.sysProgService.selectByPrimaryKey(folderProgramOid);
		if (spResult.getValue()==null) {
			throw new ServiceException(spResult.getMessage());
		}
		TbSysProg sysProg = spResult.getValue();
		Map<String, List<TbSysProg>> dataMap = new HashMap<String, List<TbSysProg>>();
		TbSysMenu sysMenu = new TbSysMenu();
		List<TbSysProg> enableList = null;
		List<TbSysProg> allList = null;
		sysMenu.setProgId(sysProg.getProgId());
		sysMenu.setParentOid(ZeroKeyProvide.OID_KEY);
		DefaultResult<TbSysMenu> smResult = this.sysMenuService.selectByUniqueKey(sysMenu);
		if (smResult.getValue()!=null) {
			sysMenu = smResult.getValue();						
			enableList = this.sysProgService.findForInTheFolderMenuItems(sysProg.getProgSystem(), sysMenu.getOid(), MenuItemType.ITEM);
		}
		allList = this.sysProgService.findForSystemItems(sysProg.getProgSystem());
		if (enableList==null) {
			enableList = new ArrayList<TbSysProg>();
		}
		if (allList==null) {
			allList = new ArrayList<TbSysProg>();
		}
		dataMap.put("enable", enableList);
		dataMap.put("all", allList);
		return dataMap;
	}
	
	/**
	 * 更新或是新增 TB_SYS_MENU 資料
	 * 
	 * @param folderProgramOid
	 * @param childProgramOidList
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = {ServiceMethodType.INSERT, ServiceMethodType.UPDATE})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> createOrUpdate(String folderProgramOid, List<String> childProgramOidList) throws ServiceException, Exception {
		if (StringUtils.isBlank(folderProgramOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		result.setValue(false);
		result.setMessage( BaseSystemMessage.updateFail() );
		
		// 找 TB_SYS_PROG 資料
		DefaultResult<TbSysProg> spResult = this.sysProgService.selectByPrimaryKey(folderProgramOid);
		if (spResult.getValue()==null) {
			throw new ServiceException(spResult.getMessage());
		}
		TbSysProg sysProg = spResult.getValue();
		
		// 找出 TB_SYS_MENU 原資料 , 沒有資料就是新增
		TbSysMenu sysMenu = new TbSysMenu();
		sysMenu.setProgId(sysProg.getProgId());
		sysMenu.setParentOid(ZeroKeyProvide.OID_KEY);
		if (this.sysMenuService.countByUK(sysMenu)>0) { // update 更新
			DefaultResult<TbSysMenu> smResult = this.sysMenuService.selectByUniqueKey(sysMenu);
			if (smResult.getValue()==null) {
				throw new ServiceException(smResult.getMessage());
			}
			sysMenu = smResult.getValue();
		} else { // create new 新產
			sysMenu.setProgId(sysProg.getProgId());
			sysMenu.setParentOid(ZeroKeyProvide.OID_KEY);
			sysMenu.setEnableFlag(YesNo.YES);
			DefaultResult<TbSysMenu> smResult = this.sysMenuService.insert(sysMenu);
			if (smResult.getValue()==null) {
				throw new ServiceException(smResult.getMessage());
			}
			sysMenu = smResult.getValue();
		}
		
		this.removeMenuChildData(sysMenu);
		this.createOrUpdate(sysMenu, childProgramOidList);		
		result.setValue(true);
		result.setMessage(BaseSystemMessage.updateSuccess());
		return result;
	}	
	
	private void removeMenuChildData(TbSysMenu parentSysMenu) throws ServiceException, Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parentOid", parentSysMenu.getOid());
		DefaultResult<List<TbSysMenu>> sysMenuResult = this.sysMenuService.selectListByParams(paramMap);
		if (sysMenuResult.getValue()==null || sysMenuResult.getValue().size()<1) {
			return;
		}
		for (TbSysMenu childSysMenu : sysMenuResult.getValue()) {
			this.sysMenuService.delete(childSysMenu);			
		}
	}
	
	private void createOrUpdate(TbSysMenu parentSysMenu, List<String> childProgramOidList) throws ServiceException, Exception {
		for (String progOid : childProgramOidList) {
			DefaultResult<TbSysProg> spResult = this.sysProgService.selectByPrimaryKey(progOid);
			if (spResult.getValue()==null) {
				throw new ServiceException(spResult.getMessage());
			}
			TbSysProg sysProg = spResult.getValue();			
			if ("CORE_PROG999D9999Q".equals(sysProg.getProgId())) { // CORE_PROG999D9999Q program is About page.
				throw new ServiceException("The program - CORE_PROG999D9999Q cannot settings.");
			}			
			TbSysMenu childSysMenu = new TbSysMenu();
			childSysMenu.setProgId(sysProg.getProgId());
			childSysMenu.setParentOid(parentSysMenu.getOid());
			childSysMenu.setEnableFlag(YesNo.YES);
			DefaultResult<TbSysMenu> result = this.sysMenuService.insert(childSysMenu);
			if (result.getValue()==null) {
				throw new ServiceException(result.getMessage());
			}
		}
	}
	
}
