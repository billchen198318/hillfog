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
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysIcon;
import org.qifu.core.entity.TbSysMenu;
import org.qifu.core.entity.TbSysMenuRole;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.ISystemProgramLogicService;
import org.qifu.core.service.ISysIconService;
import org.qifu.core.service.ISysMenuRoleService;
import org.qifu.core.service.ISysMenuService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SystemProgramLogicServiceImpl extends BaseLogicService implements ISystemProgramLogicService {
	protected Logger logger=LogManager.getLogger(SystemProgramLogicServiceImpl.class);
	
	@Autowired
	ISysIconService<TbSysIcon, String> sysIconService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	@Autowired
	ISysMenuService<TbSysMenu, String> sysMenuService;
	
	@Autowired
	ISysMenuRoleService<TbSysMenuRole, String> sysMenuRoleService;	
	
	public SystemProgramLogicServiceImpl() {
		super();
	}

	/**
	 * 產生 TB_SYS_PROG 資料
	 * 
	 * @param sys
	 * @param iconOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysProg> create(TbSysProg sysProg, String sysOid, String iconOid) throws ServiceException, Exception {
		TbSys sys = new TbSys();
		sys.setOid(sysOid);
		DefaultResult<TbSys> sysResult = this.sysService.selectByEntityPrimaryKey(sys);
		if (sysResult.getValue()==null) {
			throw new ServiceException(sysResult.getMessage());
		}
		sys = sysResult.getValue();
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setOid(iconOid);
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.selectByEntityPrimaryKey(sysIcon);
		if (iconResult.getValue()==null) {
			throw new ServiceException(iconResult.getMessage());
		}	
		sysIcon = iconResult.getValue();
		sysProg.setProgSystem(sys.getSysId());
		sysProg.setIcon(sysIcon.getIconId());		
		return this.sysProgService.insert(sysProg);
	}

	/**
	 * 更新 TB_SYS_PROG 資料
	 * 
	 * @param sysProg
	 * @param sysOid
	 * @param iconOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbSysProg> update(TbSysProg sysProg, String sysOid, String iconOid) throws ServiceException, Exception {
		if (sysProg==null || StringUtils.isBlank(sysProg.getOid()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSys sys = new TbSys();
		sys.setOid(sysOid);
		DefaultResult<TbSys> sysResult = this.sysService.selectByEntityPrimaryKey(sys);
		if (sysResult.getValue()==null) {
			throw new ServiceException(sysResult.getMessage());
		}
		sys = sysResult.getValue();
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setOid(iconOid);
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.selectByEntityPrimaryKey(sysIcon);
		if (iconResult.getValue()==null) {
			throw new ServiceException(iconResult.getMessage());
		}		
		sysIcon = iconResult.getValue();
		sysProg.setProgSystem(sys.getSysId());
		sysProg.setIcon(sysIcon.getIconId());			
		return this.sysProgService.update(sysProg);
	}
	
	/**
	 * 刪除 TB_SYS_PROG 資料
	 * 
	 * @param sysProg
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> delete(TbSysProg sysProg) throws ServiceException, Exception {
		if (sysProg==null || StringUtils.isBlank(sysProg.getOid()) ) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}		
		DefaultResult<TbSysProg> sysProgResult = this.sysProgService.selectByEntityPrimaryKey(sysProg);
		if (sysProgResult.getValue()==null) {
			throw new ServiceException(sysProgResult.getMessage());
		}
		sysProg = sysProgResult.getValue(); 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("progId", sysProg.getProgId());
		if (this.sysMenuService.count(params)>0) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}
		// 刪除 TB_SYS_MENU_ROLE 資料
		DefaultResult<List<TbSysMenuRole>> sysMenuRoleResult = this.sysMenuRoleService.selectListByParams(params);
		List<TbSysMenuRole> sysMenuRoleList = sysMenuRoleResult.getValue();
		for (int i=0; sysMenuRoleList!=null && i<sysMenuRoleList.size(); i++) {
			TbSysMenuRole sysMenuRole = sysMenuRoleList.get(i);
			this.sysMenuRoleService.delete(sysMenuRole);
		}
		return this.sysProgService.delete(sysProg);
	}
	
}
