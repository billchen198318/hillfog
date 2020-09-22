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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbSysTemplate;
import org.qifu.core.entity.TbSysTemplateParam;
import org.qifu.core.logic.ISystemTemplateLogicService;
import org.qifu.core.model.TemplateCode;
import org.qifu.core.service.ISysTemplateParamService;
import org.qifu.core.service.ISysTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SystemTemplateLogicServiceImpl extends BaseLogicService implements ISystemTemplateLogicService {
	protected Logger logger=LogManager.getLogger(SystemTemplateLogicServiceImpl.class);
	private static final int MAX_MESSAGE_LENGTH = 4000;
	
	@Autowired
	ISysTemplateService<TbSysTemplate, String> sysTemplateService;
	
	@Autowired
	ISysTemplateParamService<TbSysTemplateParam, String> sysTemplateParamService;
	
	public SystemTemplateLogicServiceImpl() {
		super();
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysTemplate> create(TbSysTemplate sysTemplate) throws ServiceException, Exception {
		if (sysTemplate==null) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		if (super.defaultString(sysTemplate.getMessage()).length() > MAX_MESSAGE_LENGTH ) {
			throw new ServiceException("Content max only " + MAX_MESSAGE_LENGTH + " words!");
		}
		return sysTemplateService.insert(sysTemplate);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )				
	@Override
	public DefaultResult<TbSysTemplate> update(TbSysTemplate sysTemplate) throws ServiceException, Exception {
		if (sysTemplate==null || super.isBlank(sysTemplate.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		if (super.defaultString(sysTemplate.getMessage()).length() > MAX_MESSAGE_LENGTH ) {
			throw new ServiceException("Content max only " + MAX_MESSAGE_LENGTH + " words!");
		}		
		DefaultResult<TbSysTemplate> oldResult = this.sysTemplateService.selectByEntityPrimaryKey(sysTemplate);
		if (oldResult.getValue()==null) {
			throw new ServiceException(oldResult.getMessage());
		}
		sysTemplate.setTemplateId( oldResult.getValue().getTemplateId() );		
		return sysTemplateService.update(sysTemplate);
	}	
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> delete(TbSysTemplate sysTemplate) throws ServiceException, Exception {
		if (sysTemplate==null || super.isBlank(sysTemplate.getOid()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysTemplate> oldResult = this.sysTemplateService.selectByEntityPrimaryKey(sysTemplate);
		if (oldResult.getValue()==null) {
			throw new ServiceException(oldResult.getMessage());
		}
		sysTemplate = oldResult.getValue();
		if (TemplateCode.isUsed(sysTemplate.getTemplateId())) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", sysTemplate.getTemplateId());
		DefaultResult<List<TbSysTemplateParam>> templateParamListResult = this.sysTemplateParamService.selectListByParams(params);
		for (int i=0; templateParamListResult.getValue() !=null && i < templateParamListResult.getValue().size(); i++) {
			this.sysTemplateParamService.delete(templateParamListResult.getValue().get(i));
		}
		return this.sysTemplateService.delete(sysTemplate);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbSysTemplateParam> createParam(TbSysTemplateParam sysTemplateParam, String templateOid) throws ServiceException, Exception {
		if (sysTemplateParam==null || super.isBlank(templateOid) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysTemplate> mResult = this.sysTemplateService.selectByPrimaryKey(templateOid);
		if (mResult.getValue()==null) {
			throw new ServiceException(mResult.getMessage());
		}
		TbSysTemplate sysTemplate = mResult.getValue();
		sysTemplateParam.setTemplateId(sysTemplate.getTemplateId());
		return this.sysTemplateParamService.insert(sysTemplateParam);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> deleteParam(TbSysTemplateParam sysTemplateParam) throws ServiceException, Exception {
		if (sysTemplateParam==null || super.isBlank(sysTemplateParam.getOid()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		return this.sysTemplateParamService.delete(sysTemplateParam);
	}	
	
}
