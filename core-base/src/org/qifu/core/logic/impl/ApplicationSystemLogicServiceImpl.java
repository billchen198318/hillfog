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
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.IApplicationSystemLogicService;
import org.qifu.core.service.ISysIconService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class ApplicationSystemLogicServiceImpl extends BaseLogicService implements IApplicationSystemLogicService {
	protected Logger logger=LogManager.getLogger(ApplicationSystemLogicServiceImpl.class);
	
	@Autowired
	ISysIconService<TbSysIcon, String> sysIconService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysProgService<TbSysProg, String> sysProgService;
	
	public ApplicationSystemLogicServiceImpl() {
		super();
	}
	
	/**
	 * 建立 TB_SYS 資料
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
	public DefaultResult<TbSys> create(TbSys sys, String iconOid) throws ServiceException, Exception {
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setOid(iconOid);
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.selectByPrimaryKey(iconOid);
		if (iconResult.getValue()==null) {
			throw new ServiceException( iconResult.getMessage() );
		}		
		sys.setIcon(iconResult.getValue().getIconId());
		return this.sysService.insert(sys);
	}
	
	/**
	 * 刪除 TB_SYS 資料
	 * 
	 * @param sys
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
	public DefaultResult<Boolean> delete(TbSys sys) throws ServiceException, Exception {
		if (sys==null || StringUtils.isBlank(sys.getOid()) ) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<TbSys> sysResult = this.sysService.selectByEntityPrimaryKey(sys);
		if (sysResult.getValue()==null) {
			throw new ServiceException(sysResult.getMessage());
		}
		sys = sysResult.getValue();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("progSystem", sys.getSysId());
		if (this.sysProgService.count(params)>0) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}		
		return this.sysService.delete(sys);		
	}
	
	/**
	 * 更新 TB_SYS 資料
	 * 
	 * @param sys
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
	public DefaultResult<TbSys> update(TbSys sys, String iconOid) throws ServiceException, Exception {
		if (null == sys || StringUtils.isBlank(sys.getOid()) ) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<TbSysIcon> iconResult = this.sysIconService.selectByPrimaryKey(iconOid);
		if (iconResult.getValue()==null) {
			throw new ServiceException(iconResult.getMessage());
		}		
		sys.setIcon(iconResult.getValue().getIconId());
		return this.sysService.update(sys);
	}
	
}
