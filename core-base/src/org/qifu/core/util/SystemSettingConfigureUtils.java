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
package org.qifu.core.util;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbSysCode;
import org.qifu.core.service.ISysCodeService;

public class SystemSettingConfigureUtils {
	
	private static final String CODE_TYPE = "CNF";
	private static final String _MAIL_DEFAULT_FROM_MAIL_CODE = "CNF_CONF001";
	private static final String _MAIL_ENABLE_CODE = "CNF_CONF002";
	private static final String _FIRST_LOAD_JAVASCRIPT_CODE = "CNF_CONF003";
	
	private static ISysCodeService<TbSysCode, String> sysCodeService;
	
	static {
		sysCodeService = (ISysCodeService<TbSysCode, String>) AppContext.context.getBean(ISysCodeService.class);
	}
	
	public static TbSysCode getCode(String code) {
		TbSysCode sysCode = new TbSysCode();
		sysCode.setType(CODE_TYPE);
		sysCode.setCode(code);
		try {
			DefaultResult<TbSysCode> result = sysCodeService.selectByUniqueKey(sysCode);
			if (result.getValue()!=null) {
				sysCode = result.getValue();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sysCode;
	}	
	
	public static void updateParam1(String code, String value) throws ServiceException, Exception {
		TbSysCode sysCode = new TbSysCode();
		sysCode.setType(CODE_TYPE);
		sysCode.setCode(code);
		DefaultResult<TbSysCode> result = sysCodeService.selectByUniqueKey(sysCode);
		if (result.getValue()==null) {
			throw new ServiceException(result.getMessage());
		}
		sysCode = result.getValue();
		sysCode.setParam1( value );
		result = sysCodeService.update(sysCode);
		if (result.getValue()==null) {
			throw new ServiceException(result.getMessage());
		}
	}	
	
	public static TbSysCode getMailDefaultFrom() {
		return getCode(_MAIL_DEFAULT_FROM_MAIL_CODE);
	}
	
	public static String getMailDefaultFromValue() {
		TbSysCode sysCode = getMailDefaultFrom();
		return StringUtils.defaultString(sysCode.getParam1());
	}	
	
	public static void updateMailDefaultFromValue(String value) throws ServiceException, Exception {
		updateParam1(_MAIL_DEFAULT_FROM_MAIL_CODE, value);
	}	
	
	public static TbSysCode getMailEnable() {
		return getCode(_MAIL_ENABLE_CODE);
	}
	
	public static String getMailEnableValue() {
		TbSysCode sysCode = getMailEnable();
		return StringUtils.defaultString(sysCode.getParam1()).trim();
	}	
	
	public static void updateMailEnableValue(String value) throws ServiceException, Exception {
		updateParam1(_MAIL_ENABLE_CODE, value);
	}
	
	public static TbSysCode getFirstLoadJavascript() {
		return getCode(_FIRST_LOAD_JAVASCRIPT_CODE);
	}	
	
	public static String getFirstLoadJavascriptValue() {
		TbSysCode sysCode = getFirstLoadJavascript();
		return StringUtils.defaultString( sysCode.getParam1() );
	}
	
}
