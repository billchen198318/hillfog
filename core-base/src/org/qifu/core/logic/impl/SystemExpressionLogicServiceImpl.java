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
import org.qifu.core.entity.TbSysBeanHelpExpr;
import org.qifu.core.entity.TbSysExprJob;
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.logic.ISystemExpressionLogicService;
import org.qifu.core.model.ExpressionJobConstants;
import org.qifu.core.service.ISysBeanHelpExprService;
import org.qifu.core.service.ISysExprJobService;
import org.qifu.core.service.ISysExpressionService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SystemExpressionLogicServiceImpl extends BaseLogicService implements ISystemExpressionLogicService {
	protected Logger logger=LogManager.getLogger(SystemExpressionLogicServiceImpl.class);
	
	private static final int MAX_CONTENT_LENGTH = 8000;
	private static final int MAX_DESCRIPTION_LENGTH = 500;
	
	@Autowired
	ISysExpressionService<TbSysExpression, String> sysExpressionService;
	
	@Autowired
	ISysBeanHelpExprService<TbSysBeanHelpExpr, String> sysBeanHelpExprService;
	
	@Autowired
	ISysExprJobService<TbSysExprJob, String> sysExprJobService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	public SystemExpressionLogicServiceImpl() {
		super();
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysExpression> create(TbSysExpression expression) throws ServiceException, Exception {
		if (expression==null) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		if (super.defaultString(expression.getContent()).length() > MAX_CONTENT_LENGTH) {
			throw new ServiceException("Expression only 8,000 words!");
		}
		this.setStringValueMaxLength(expression, "description", MAX_DESCRIPTION_LENGTH);
		return this.sysExpressionService.insert(expression);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysExpression> update(TbSysExpression expression) throws ServiceException, Exception {
		if (expression==null || super.isBlank(expression.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		if (super.defaultString(expression.getContent()).length() > MAX_CONTENT_LENGTH) {
			throw new ServiceException("Expression only 8,000 words!");
		}
		this.setStringValueMaxLength(expression, "description", MAX_DESCRIPTION_LENGTH);
		TbSysExpression oldSysExpression = this.sysExpressionService.selectByEntityPrimaryKey(expression).getValueEmptyThrowMessage();
		expression.setExprId( oldSysExpression.getExprId() );
		return this.sysExpressionService.update(expression);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> delete(TbSysExpression expression) throws ServiceException, Exception {
		if (expression==null || super.isBlank(expression.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSysExpression oldSysExpression = this.sysExpressionService.selectByEntityPrimaryKey(expression).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("exprId", oldSysExpression.getExprId() );
		if ( this.sysBeanHelpExprService.count(paramMap) > 0) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}
		if ( this.sysExprJobService.count(paramMap) > 0 ) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}
		return this.sysExpressionService.delete(expression);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbSysExprJob> createJob(TbSysExprJob exprJob, String systemOid, String expressionOid) throws ServiceException, Exception {
		if (exprJob==null || super.isBlank(systemOid) || super.isBlank(expressionOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}	
		TbSys sys = this.sysService.selectByPrimaryKey(systemOid).getValueEmptyThrowMessage();
		TbSysExpression expression = this.sysExpressionService.selectByPrimaryKey(expressionOid).getValueEmptyThrowMessage();
		exprJob.setSystem( sys.getSysId() );
		exprJob.setExprId( expression.getExprId() );
		exprJob.setRunStatus( ExpressionJobConstants.RUNSTATUS_SUCCESS ); // 預設值
		this.setStringValueMaxLength(exprJob, "description", MAX_DESCRIPTION_LENGTH);
		return this.sysExprJobService.insert(exprJob);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbSysExprJob> updateJob(TbSysExprJob exprJob, String systemOid, String expressionOid) throws ServiceException, Exception {
		if ( null == exprJob || StringUtils.isBlank(exprJob.getOid()) || StringUtils.isBlank(systemOid) || StringUtils.isBlank(expressionOid) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSys sys = this.sysService.selectByPrimaryKey(systemOid).getValueEmptyThrowMessage();		
		TbSysExpression expression = this.sysExpressionService.selectByPrimaryKey(expressionOid).getValueEmptyThrowMessage();
		TbSysExprJob oldSysExprJob = this.sysExprJobService.selectByEntityPrimaryKey(exprJob).getValueEmptyThrowMessage();
		exprJob.setId( oldSysExprJob.getExprId() );
		exprJob.setSystem( sys.getSysId() );
		exprJob.setExprId( expression.getExprId() );
		exprJob.setRunStatus( oldSysExprJob.getRunStatus() );
		if (super.isBlank(oldSysExprJob.getRunStatus())) {
			exprJob.setRunStatus( ExpressionJobConstants.RUNSTATUS_FAULT );
			logger.warn( "Before runStatus flag is blank. Expression Job ID: " + oldSysExprJob.getId() );			
		}
		this.setStringValueMaxLength(exprJob, "description", MAX_DESCRIPTION_LENGTH);
		return this.sysExprJobService.update(exprJob);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<Boolean> deleteJob(TbSysExprJob exprJob) throws ServiceException, Exception {
		if ( null == exprJob || StringUtils.isBlank(exprJob.getOid()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		exprJob = this.sysExprJobService.selectByEntityPrimaryKey(exprJob).getValueEmptyThrowMessage();
		return this.sysExprJobService.delete( exprJob );
	}
	
}
