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
package org.qifu.base.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.SysMsgConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseSystemMessage implements java.io.Serializable {
	private static final long serialVersionUID = -4317284943704877962L;

	private static final String _CONFIG = "org/qifu/base/message/BaseSystemMessage.json";
	
	private static Map<String, Object> messageMap = null;
	
	private static String _messageDatas = " { } ";
	
	static {
		try {
			InputStream is = BaseSystemMessage.class.getClassLoader().getResource( _CONFIG ).openStream();
			_messageDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
			is = null;
			messageMap = loadDatas();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==messageMap) {
				messageMap = new HashMap<String, Object>();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> loadDatas() {
		Map<String, Object> datas = null;
		try {
			datas = (Map<String, Object>)new ObjectMapper().readValue( _messageDatas, LinkedHashMap.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}	
	
	private static String getMessage(String code) {
		String msg = (String) messageMap.get(code);
		if (StringUtils.isBlank(msg)) {
			return code;
		}
		return msg;
	}
	
	public static String noPermission() {
		return getMessage(SysMsgConstants.NO_PERMISSION);
	}
	
	public static String noLoginAccessDenied() {
		return getMessage(SysMsgConstants.NO_LOGIN_ACCESS_DENIED);
	}
	
	public static String parameterBlank() {
		return getMessage(SysMsgConstants.PARAMS_BLANK);
	}
	
	public static String parameterIncorrect() {
		return getMessage(SysMsgConstants.PARAMS_INCORRECT);
	}
	
	public static String objectNull() {
		return getMessage(SysMsgConstants.OBJ_NULL);
	}
	
	public static String controllerTryAgain() {
		return getMessage(SysMsgConstants.CONTROLLER_TRY_AGAIN);
	}
	
	public static String dataNoExist() {
		return getMessage(SysMsgConstants.DATA_NO_EXIST);
	}
	
	public static String dataIsExist() {
		return getMessage(SysMsgConstants.DATA_IS_EXIST);
	}
	
	public static String updateSuccess() {
		return getMessage(SysMsgConstants.UPDATE_SUCCESS);
	}
	
	public static String updateFail() {
		return getMessage(SysMsgConstants.UPDATE_FAIL);
	}
	
	public static String insertSuccess() {
		return getMessage(SysMsgConstants.INSERT_SUCCESS);
	}
	
	public static String insertFail() {
		return getMessage(SysMsgConstants.INSERT_FAIL);
	}
	
	public static String deleteSuccess() {
		return getMessage(SysMsgConstants.DELETE_SUCCESS);
	}
	
	public static String deleteFail() {
		return getMessage(SysMsgConstants.DELETE_FAIL);
	}
	
	public static String searchNoData() {
		return getMessage(SysMsgConstants.SEARCH_NO_DATA);
	}
	
	public static String dataCannotDelete() {
		return getMessage(SysMsgConstants.DATA_CANNOT_DELETE);
	}
	
	public static String loginFail() {
		return getMessage(SysMsgConstants.LOGIN_FAIL);
	}
	
	public static String updateFileTypeError() {
		return getMessage(SysMsgConstants.UPLOAD_FILE_TYPE_ERROR);
	}
	
	public static String uploadFileNoSelect() {
		return getMessage(SysMsgConstants.UPLOAD_FILE_NO_SELECT);
	}
	
	public static String uploadFileOnlyImage() {
		return getMessage(SysMsgConstants.UPLOAD_FILE_ONLY_IMAGE);
	}
	
	public static String dataErrors() {
		return getMessage(SysMsgConstants.DATA_ERRORS);
	}
	
}
