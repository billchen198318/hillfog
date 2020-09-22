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
package org.qifu.base;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.qifu.base.model.YesNo;

public class Constants {
	
	/**
	 * 不要更改這個設定
	 */
	public static final String BASE_ENCODING = "utf-8";
	
	/**
	 * EncryptorUtils 要用的 key1
	 */
	public static final String ENCRYPTOR_KEY1 = "pOk%$ewQaIUyBvCS@Oj!~%O$kW1p2Rh9";
	
	/**
	 * EncryptorUtils 要用的 key2
	 */
	public static final String ENCRYPTOR_KEY2 = "7913670289654325";		
	
	/**
	 * 保留查詢參數名稱 for PageOf , BaseDAO
	 */
	public static final String _RESERVED_PARAM_NAME_QUERY_SORT_TYPE = "sortType";
	
	/**
	 * 保留查詢參數名稱 for PageOf , BaseDAO
	 */
	public static final String _RESERVED_PARAM_NAME_QUERY_ORDER_BY = "orderBy";	
	
	public static final String QUERY_TYPE_OF_SELECT="select"; // BaseService 查詢 grid 要用
	public static final String QUERY_TYPE_OF_COUNT="count"; // BaseService 查詢 grid 要用	
	
	public static final String SUPER_ROLE_ALL = "*";
	public static final String SUPER_ROLE_ADMIN = "admin";
	public static final String SUPER_PERMISSION = "*";
	public static final String SYSTEM_BACKGROUND_USER = "system"; // 背景程式要用 , 配 SubjectBuilderForBackground.java 與 shiro.ini
	public static final String SYSTEM_BACKGROUND_PASSWORD = "password99"; // 背景程式要用 , 配 SubjectBuilderForBackground.java 與 shiro.ini
	
	public static final String SERVICE_ID_TYPE_DISTINGUISH_SYMBOL = ":"; // logic service 用來組 service id 與 ServiceMethodType 成字串, 查有沒有權限
	
	public static final String SESS_ACCOUNT="SESSION_QIFU3_ACCOUNT"; // 登入 account id 放到 session 變數名
	public static final String SESS_LANG = "SESSION_QIFU3_LANG";
	public static final String SESS_SYSCURRENT_ID = "SESSION_QIFU3_SYSCURRENT_ID";
	
	public static final String APP_SITE_CURRENTID_COOKIE_NAME = "QIFU3_SYSCURRENT_ID"; // 跨站 cookie 要用的名稱
	
	public static final String QIFU_PAGE_IN_TAB_IFRAME = "qifuIframePage";
	public static final String QIFU_PAGE_PROG_PARAM = "qifuProgId";
	
	/**
	 * GreenStepBaseFormAuthenticationFilter 要用的
	 */
	public static final String NO_LOGIN_JSON_DATA = "{ \"success\":\"" + YesNo.NO + "\",\"message\":\"Please login!\",\"login\":\"" + YesNo.NO + "\",\"isAuthorize\":\"" + YesNo.NO + "\" }";
	/**
	 * GreenStepBaseFormAuthenticationFilter 要用的
	 */	
	public static final String NO_AUTHZ_JSON_DATA = "{ \"success\":\"" + YesNo.NO + "\",\"message\":\"no authorize!\",\"login\":\"" + YesNo.YES + "\",\"isAuthorize\":\"" + YesNo.NO + "\" }";
	
	public static final String PAGE_MESSAGE="pageMessage";
	
	public static final String HTML_SELECT_NO_SELECT_ID="all";
	public static final String HTML_SELECT_NO_SELECT_NAME=" - please select - ";	
	
	public static final String ID_DELIMITER = ";"; // 有時要將多筆 OID 或 key 組成一組字串 , 這是就用這個符號來區分	
	
	public static final String INPUT_NAME_DELIMITER = ":"; // 有時輸入欄位id或名稱,想要有區分一些有意義的資料時用
	
	public static final String DEFAULT_SPLIT_DELIMITER = ";|,";
	
	public static final String TMP_SUB_DIR_NAME = "qifu3";
	
	public static final String HTML_BR = "<br>";
	
	public static String getTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
	public static String getWorkTmpDir() {
		String dirPath = getTmpDir() + "/" + TMP_SUB_DIR_NAME + "/";
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			try {
				FileUtils.forceMkdir(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		file = null;
		return dirPath;
	}
	
	public static final int MAX_SYS_DESCRIPTION_LENGTH = 500;
	
}
