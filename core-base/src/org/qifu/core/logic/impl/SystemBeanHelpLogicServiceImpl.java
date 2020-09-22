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
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysBeanHelp;
import org.qifu.core.entity.TbSysBeanHelpExpr;
import org.qifu.core.entity.TbSysBeanHelpExprMap;
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.logic.ISystemBeanHelpLogicService;
import org.qifu.core.service.ISysBeanHelpExprMapService;
import org.qifu.core.service.ISysBeanHelpExprService;
import org.qifu.core.service.ISysBeanHelpService;
import org.qifu.core.service.ISysExpressionService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SystemBeanHelpLogicServiceImpl extends BaseLogicService implements ISystemBeanHelpLogicService {
	protected Logger logger=LogManager.getLogger(SystemBeanHelpLogicServiceImpl.class);
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysBeanHelpService<TbSysBeanHelp, String> sysBeanHelpService;	
	
	@Autowired
	ISysBeanHelpExprService<TbSysBeanHelpExpr, String> sysBeanHelpExprService;
	
	@Autowired
	ISysBeanHelpExprMapService<TbSysBeanHelpExprMap, String> sysBeanHelpExprMapService;
	
	@Autowired
	ISysExpressionService<TbSysExpression, String> sysExpressionService;
	
	public SystemBeanHelpLogicServiceImpl() {
		super();
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysBeanHelp> create(TbSysBeanHelp beanHelp, String systemOid) throws ServiceException, Exception {
		if (beanHelp==null || super.isBlank(systemOid) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSys sys = this.sysService.selectByPrimaryKey(systemOid).getValueEmptyThrowMessage();
		beanHelp.setSystem(sys.getSysId());		
		return sysBeanHelpService.insert(beanHelp);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysBeanHelp> update(TbSysBeanHelp beanHelp, String systemOid) throws ServiceException, Exception {
		if (beanHelp==null || super.isBlank(beanHelp.getOid()) || super.isBlank(systemOid) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSys sys = this.sysService.selectByPrimaryKey(systemOid).getValueEmptyThrowMessage();
		beanHelp.setSystem(sys.getSysId());		
		return sysBeanHelpService.update(beanHelp);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> delete(TbSysBeanHelp beanHelp) throws ServiceException, Exception {
		if (beanHelp==null || super.isBlank(beanHelp.getOid()) ) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		// delete TB_SYS_BEAN_HELP_EXPR
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("helpOid", beanHelp.getOid());
		List<TbSysBeanHelpExpr> exprList = this.sysBeanHelpExprService.selectListByParams(paramMap).getValue();
		for (int i=0; exprList!=null && i<exprList.size(); i++) {
			// delete TB_SYS_BEAN_HELP_EXPR_MAP
			TbSysBeanHelpExpr helpExpr = exprList.get(i);
			paramMap.clear();
			paramMap.put("helpExprOid", helpExpr.getOid());
			List<TbSysBeanHelpExprMap> exprMapList = this.sysBeanHelpExprMapService.selectListByParams(paramMap).getValue();
			for (int j=0; exprMapList!=null && j<exprMapList.size(); j++) {
				TbSysBeanHelpExprMap helpExprMap = exprMapList.get(j);
				this.sysBeanHelpExprMapService.delete(helpExprMap);
			}
			this.sysBeanHelpExprService.delete(helpExpr);
		}				
		return sysBeanHelpService.delete(beanHelp);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSysBeanHelpExpr> createExpr(TbSysBeanHelpExpr beanHelpExpr, String helpOid, String expressionOid) throws ServiceException, Exception {
		if (beanHelpExpr==null || super.isBlank(helpOid) || super.isBlank(expressionOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSysBeanHelp sysBeanHelp = this.sysBeanHelpService.selectByPrimaryKey(helpOid).getValueEmptyThrowMessage(); // 查看有沒有資料
		TbSysExpression sysExpression = this.sysExpressionService.selectByPrimaryKey(expressionOid).getValueEmptyThrowMessage(); // 查看有沒有資料
		beanHelpExpr.setHelpOid( sysBeanHelp.getOid() );
		beanHelpExpr.setExprId( sysExpression.getExprId() );		
		return this.sysBeanHelpExprService.insert(beanHelpExpr);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> deleteExpr(TbSysBeanHelpExpr beanHelpExpr) throws ServiceException, Exception {
		if (null==beanHelpExpr || super.isBlank(beanHelpExpr.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSysBeanHelpExpr oldSysBeanHelpExpr = this.sysBeanHelpExprService.selectByEntityPrimaryKey(beanHelpExpr).getValueEmptyThrowMessage(); // 查看有沒有資料
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("helpExprOid", oldSysBeanHelpExpr.getOid() );
		List<TbSysBeanHelpExprMap> mapList = this.sysBeanHelpExprMapService.selectListByParams(paramMap).getValue();
		for (int i=0; mapList!=null && i<mapList.size(); i++) {
			this.sysBeanHelpExprMapService.delete( mapList.get(i) );
		}		
		return this.sysBeanHelpExprService.delete(beanHelpExpr);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )				
	@Override
	public DefaultResult<TbSysBeanHelpExprMap> createExprMap(TbSysBeanHelpExprMap beanHelpExprMap, String helpExprOid) throws ServiceException, Exception {
		if (beanHelpExprMap==null || super.isBlank(helpExprOid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbSysBeanHelpExpr sysBeanHelpExpr = this.sysBeanHelpExprService.selectByPrimaryKey(helpExprOid).getValueEmptyThrowMessage(); // 查看有沒有資料
		beanHelpExprMap.setHelpExprOid( sysBeanHelpExpr.getOid() );		
		return this.sysBeanHelpExprMapService.insert(beanHelpExprMap);
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> deleteExprMap(TbSysBeanHelpExprMap beanHelpExprMap) throws ServiceException, Exception {
		if (beanHelpExprMap==null || super.isBlank(beanHelpExprMap.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		return this.sysBeanHelpExprMapService.delete(beanHelpExprMap);
	}
	
}
