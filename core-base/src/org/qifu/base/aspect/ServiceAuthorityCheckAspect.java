/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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
package org.qifu.base.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.qifu.base.Constants;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.core.model.User;
import org.qifu.core.support.SysEventLogSupport;
import org.qifu.core.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(0)
@Aspect
@Component
public class ServiceAuthorityCheckAspect implements IBaseAspectService {
	protected Logger logger=LogManager.getLogger(ServiceAuthorityCheckAspect.class);
	
	@Autowired
	BaseInfoConfigProperties baseInfoConfigProperties;	
	
	/**
	 * no enable for scan Base service package
	 */
	//@Around( AspectConstants.BASE_SERVICE_PACKAGE )
	@Override
	public Object baseServiceProcess(ProceedingJoinPoint pjp) throws AuthorityException, ServiceException, Throwable {
		/**
		 * do something...
		 */
		return pjp.proceed();
	}
	
	@Around( AspectConstants.LOGIC_SERVICE_PACKAGE )
	public Object logicServiceProcess(ProceedingJoinPoint pjp) throws AuthorityException, ServiceException, Throwable {
		MethodSignature signature=(MethodSignature)pjp.getSignature();
		Annotation[] annotations=pjp.getTarget().getClass().getAnnotations();
		String serviceId = AspectConstants.getServiceId(annotations);
		if (StringUtils.isBlank(serviceId)) {
			serviceId = WordUtils.uncapitalize(pjp.getTarget().getClass().getSimpleName());
		}
		Method method = signature.getMethod();
		String username = "";
		User user = UserUtils.getCurrentUser();
		if (user != null) {
			username = user.getUsername();
		}
		String systemId = baseInfoConfigProperties.getSystem();
		if (UserUtils.hasRole(Constants.SUPER_ROLE_ALL) || UserUtils.hasRole(Constants.SUPER_ROLE_ADMIN)) {
			log(username, systemId, this.getEventId(serviceId, method.getName()), true );
			return pjp.proceed();
		}
		if (StringUtils.isBlank(serviceId)) { // 沒有 service id 無法判斷檢查 
			log(username, systemId, this.getEventId(serviceId, method.getName()), true );
			return pjp.proceed();
		}
		if (!this.isServiceAuthorityCheck(annotations)) { // 沒有 ServiceAuthority 或 check=false 就不用檢查了 
			log(username, systemId, this.getEventId(serviceId, method.getName()), true );
			return pjp.proceed();
		}		
		Annotation[] methodAnnotations = method.getAnnotations();
		if (this.isServiceMethodAuthority(serviceId, methodAnnotations)) {
			log(username, systemId, this.getEventId(serviceId, method.getName()), true );
			return pjp.proceed();
		}
		logger.warn(
				"[decline] user[" + UserUtils.getCurrentUser().getUsername() + "] " 
						+ pjp.getTarget().getClass().getName() 
						+ " - " 
						+ signature.getMethod().getName());		
		log(username, systemId, this.getEventId(serviceId, method.getName()), false );
		throw new AuthorityException(BaseSystemMessage.noPermission());
	}
	
	private void log(String username, String systemId, String eventId, boolean permit) {
		if (!YesNo.YES.equals(baseInfoConfigProperties.getEnableServiceAuthChecLog())) {
			return;
		}
		SysEventLogSupport.log(username, systemId, eventId, permit);
	}
	
	private boolean isServiceAuthorityCheck(Annotation[] annotations) { // 沒有 ServiceAuthority 或 check=false 就不用檢查了 
		if (null==annotations || annotations.length<1) {
			return false;
		}
		boolean check = false;
		for (Annotation anno : annotations) {
			if (anno instanceof ServiceAuthority) {
				check = ((ServiceAuthority)anno).check();
			}
		}
		return check;
	}
	
	private boolean isServiceMethodAuthority(String serviceId, Annotation[] annotations) {
		if (annotations==null || annotations.length<1) { // 沒有 @ServiceMethodAuthority 不要檢查權限
			return true;
		}
		boolean status = false;
		boolean foundServiceMethodAuthority = false;
		for (Annotation anno : annotations) {
			if (anno instanceof ServiceMethodAuthority) {
				foundServiceMethodAuthority = true;
				ServiceMethodType[] types = ((ServiceMethodAuthority)anno).type();
				for (int i=0; types!=null && !status && i<types.length; i++) {
					ServiceMethodType type = types[i];
					// 如 core.service.logic.ApplicationSystemLogicService#INSERT
					String methodPerm = serviceId + Constants.SERVICE_ID_TYPE_DISTINGUISH_SYMBOL + type.toString();
					if (UserUtils.isPermitted(methodPerm)) {
						status = true;
					}
				}
			}
		}
		if (!foundServiceMethodAuthority) { // 沒有 @ServiceMethodAuthority 不要檢查權限 
			return true;
		}
		return status;
	}
	
	private String getEventId(String serviceId, String methodName) {
		return serviceId + "@" + methodName;
	}
	
}
