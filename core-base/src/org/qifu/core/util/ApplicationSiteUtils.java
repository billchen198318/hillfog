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
package org.qifu.core.util;

//import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.params.HttpClientParams;
//import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.core.entity.TbSys;
import org.qifu.core.service.ISysService;

//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class ApplicationSiteUtils {
	protected static Logger logger = LogManager.getLogger(ApplicationSiteUtils.class);
	public static final String UPDATE_HOST_ALWAYS = "2";
	public static final String UPDATE_HOST_ONLY_FIRST_ONE = "1";
	private static final int TEST_JSON_HTTP_TIMEOUT = 3000; // 3秒
	private static Map<String, String> contextPathMap = new HashMap<String, String>();	
	
	private static ISysService<TbSys, String> sysService;
	
	static {
		sysService = (ISysService<TbSys, String>) AppContext.context.getBean(ISysService.class);
	}
	
	public static List<TbSys> getSystems() throws ServiceException, Exception {	
		return sysService.selectList().getValue();
	}
	
	public static TbSys getSys(String sysId) {
		TbSys sys = new TbSys();
		sys.setSysId(sysId);
		try {
			sys = sysService.selectByUniqueKey(sys).getValue();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return sys;
	}	
	
	public static String getHost(String sysId) {
		String host = "";
		try {
			TbSys sys = getSys(sysId);
			if ( sys != null ) {
				host = sys.getHost();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return host;
	}
	
	@SuppressWarnings("unchecked")
	public static String getBasePath(String sysId, HttpServletRequest request) {
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		if (StringUtils.isBlank(sysId)) {
			return basePath;
		}
		try {
			TbSys sys = getSys(sysId);
			if (sys != null) {
				basePath = request.getScheme() + "://" + sys.getHost() + "/" + sys.getContextPath() + "/";
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return basePath;
	}
	
	public static String getContextPath(String sysId) {
		String contextPath = "";
		try {
			TbSys sys = getSys(sysId);
			if (null != sys) {
				contextPath = sys.getContextPath(); 
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contextPath;		
	}
	
//	public static String getContextPathFromMap(String sysId) {
//		if (contextPathMap.get(sysId)!=null) {
//			return contextPathMap.get(sysId);
//		}
//		String contextPath = getContextPath(sysId);
//		if (!StringUtils.isBlank(contextPath)) {
//			contextPathMap.put(sysId, contextPath);
//		}
//		return contextPath;
//	}
	
//	@SuppressWarnings("unchecked")
//	public static void configureHost(String sysId, String logConfFileFullPath) {
//		File logConfFile = new File( logConfFileFullPath );
//		try {
//			if (!logConfFile.exists()) {
//				FileUtils.writeStringToFile(logConfFile, UPDATE_HOST_ONLY_FIRST_ONE);
//			}
//			String logValue = FileUtils.readFileToString(logConfFile);
//			if (!StringUtils.isBlank(logValue) && UPDATE_HOST_ONLY_FIRST_ONE.equals(Constants.getApplicationSiteHostUpdateMode())) {
//				// has before start log file, and UPDATE_HOST_ONLY_FIRST_ONE mode
//				FileUtils.writeStringToFile(logConfFile, UPDATE_HOST_ONLY_FIRST_ONE);
//				logConfFile = null;
//				return;
//			}			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		ISysService<SysVO, TbSys, String> sysService = 
//				(ISysService<SysVO, TbSys, String>)AppContext.getBean("core.service.SysService");		
//		SysVO sys = new SysVO();
//		sys.setSysId(sysId);
//		try {
//			DefaultResult<SysVO> result = sysService.findByUK(sys);
//			if (result.getValue()==null) {
//				System.out.println(result.getSystemMessage().getValue());				
//				return;
//			}
//			sys = result.getValue();
//			// 2016-06-29 rem
//			/*
//			String port = "";
//			String tmp[] = sys.getHost().split(":");
//			if ( tmp!=null && tmp.length==2 ) {
//				port = tmp[1];
//			}
//			*/
//			String port = String.valueOf( HostUtils.getHttpPort() ); // 2016-06-29 add
//			String hostAddress = HostUtils.getHostAddress();
//			sys.setHost(hostAddress);
//			if (!StringUtils.isBlank(port)) {				
//				sys.setHost( hostAddress + ":" + port );
//			}
//			sysService.updateObject(sys);
//			
//			
//			if (UPDATE_HOST_ALWAYS.equals(Constants.getApplicationSiteHostUpdateMode())) {
//				FileUtils.writeStringToFile(logConfFile, UPDATE_HOST_ALWAYS);
//			} else {
//				FileUtils.writeStringToFile(logConfFile, UPDATE_HOST_ONLY_FIRST_ONE);
//			}
//			
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (logConfFile != null) {
//				logConfFile = null;
//			}
//		}
//	}
//	
//	private static boolean checkCrossSite(String host, HttpServletRequest request) {
//		boolean corssSite = false;
//		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
//		String basePath80 = request.getScheme()+"://"+request.getServerName();
//		basePath = basePath.toLowerCase();
//		basePath80 = basePath80.toLowerCase();	
//		if (request.getServerPort() == 80 || request.getServerPort() == 443) {
//			if (basePath.indexOf( host ) == -1 && basePath80.indexOf( host ) == -1) {
//				corssSite = true;
//			}
//		} else {
//			if (basePath.indexOf( host ) == -1) {
//				corssSite = true;
//			}
//		}		
//		return corssSite;
//	}
//	
//	private static boolean checkTestConnection(String host, String contextPath, HttpServletRequest request) {
//		boolean test = false;
//		String basePath = request.getScheme()+"://" + host + "/" + contextPath;
//		String urlStr = basePath + "/pages/system/testJsonResult.do";
//		try {
//			logger.info("checkTestConnection , url=" + urlStr);
//			HttpClient client = new HttpClient();
//			HttpMethod method = new GetMethod(urlStr);
//			HttpClientParams params = new HttpClientParams();
//			params.setConnectionManagerTimeout(TEST_JSON_HTTP_TIMEOUT);
//			params.setSoTimeout(TEST_JSON_HTTP_TIMEOUT);
//			client.setParams(params);
//			client.executeMethod(method);
//			byte[] responseBody = method.getResponseBody();
//			if (null == responseBody) {
//				test = false;
//				return test;
//			}			
//			String content = new String(responseBody, Constants.BASE_ENCODING);
//			ObjectMapper mapper = new ObjectMapper();
//			@SuppressWarnings("unchecked")
//			Map<String, Object> dataMap = (Map<String, Object>) mapper.readValue(content, HashMap.class);
//			if (YesNo.YES.equals(dataMap.get("success"))) {
//				test = true;
//			}
//		} catch (JsonParseException e) {
//			logger.error( e.getMessage().toString() );
//		} catch (JsonMappingException e) {
//			logger.error( e.getMessage().toString() );
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (!test) {
//				logger.warn("checkTestConnection : " + String.valueOf(test));
//			} else {
//				logger.info("checkTestConnection : " + String.valueOf(test));
//			}
//		}
//		return test;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static boolean checkLoginUrlWithAllSysHostConfig(HttpServletRequest request) {
//		boolean pathSuccess = true;
//		ISysService<SysVO, TbSys, String> sysService = (ISysService<SysVO, TbSys, String>)AppContext.getBean("core.service.SysService");				
//		try {
//			List<TbSys> sysList = sysService.findListByParams( null );
//			for (int i=0; sysList != null && i < sysList.size() && pathSuccess; i++) {
//				TbSys sys = sysList.get(i);
//				pathSuccess = !checkCrossSite(sys.getHost().toLowerCase(), request);
//				if (pathSuccess) {
//					pathSuccess = checkTestConnection(sys.getHost(), sys.getContextPath(), request);
//				}
//			}
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return pathSuccess;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static List<SysVO> getSystemsCheckCrossSiteWithTestConnection(HttpServletRequest request) {
//		ISysService<SysVO, TbSys, String> sysService = (ISysService<SysVO, TbSys, String>)AppContext.getBean("core.service.SysService");
//		List<SysVO> sysList = null;	
//		try {
//			sysList = sysService.findListVOByParams( null );
//			for (SysVO sys : sysList) {
//				if ( checkCrossSite(sys.getHost().toLowerCase(), request) ) {
//					sys.setCrossSiteFlag( YesNo.YES );
//				} else {
//					sys.setCrossSiteFlag( YesNo.NO );
//				}
//				if ( checkTestConnection(sys.getHost(), sys.getContextPath(), request) ) {
//					sys.setTestFlag( YesNo.YES );
//				} else {
//					sys.setTestFlag( YesNo.NO );
//				}
//			}			
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sysList;
//	}
	
}
