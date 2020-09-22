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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.TbSysTemplate;
import org.qifu.core.entity.TbSysTemplateParam;
import org.qifu.core.model.TemplateResultObj;
import org.qifu.core.service.ISysTemplateParamService;
import org.qifu.core.service.ISysTemplateService;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import ognl.Ognl;

public class TemplateUtils {
	private static final String IS_TITLE = "title";
	private static final String IS_MESSAGE = "message";
	private static ISysTemplateService<TbSysTemplate, String> sysTemplateService;
	private static ISysTemplateParamService<TbSysTemplateParam, String> sysTemplateParamService;
	
	static {
		sysTemplateService = AppContext.context.getBean(ISysTemplateService.class);
		sysTemplateParamService = AppContext.context.getBean(ISysTemplateParamService.class);
	}
	
	private static TbSysTemplate loadSysTemplate(String templateId) throws ServiceException, Exception {
		TbSysTemplate sysTemplate = new TbSysTemplate();
		sysTemplate.setTemplateId(templateId);
		sysTemplate = sysTemplateService.selectByUniqueKey(sysTemplate).getValueEmptyThrowMessage();	
		return sysTemplate;
	}
	
	private static List<TbSysTemplateParam> loadSysTemplateParam(String templateId) throws ServiceException, Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", templateId);
		List<TbSysTemplateParam> searchList = sysTemplateParamService.selectListByParams(params).getValue();
		if (searchList==null) {
			searchList = new ArrayList<TbSysTemplateParam>();
		}
		return searchList;
	}
	
	private static String processTemplate(String resource, Map<String, Object> params) throws Exception {
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate("sysTemplate", resource);
		Configuration cfg = new Configuration( Configuration.VERSION_2_3_21 );
		cfg.setTemplateLoader(templateLoader);
		Template template = cfg.getTemplate("sysTemplate", Constants.BASE_ENCODING);
		Writer out = new StringWriter();
		template.process(params, out);
		return out.toString();
	}
	
	private static Map<String, Object> getTemplateParamMap(String type, List<TbSysTemplateParam> sysTemplateParamList, 
			Object dataObj) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		for (TbSysTemplateParam sysTemplateParam : sysTemplateParamList) {
			if (IS_TITLE.equals(type) ) {
				if ( YesNo.YES.equals(sysTemplateParam.getIsTitle()) ) {
					params.put(sysTemplateParam.getTemplateVar(), Ognl.getValue(sysTemplateParam.getObjectVar(), OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), dataObj) );
				}
			} else { // message
				if ( !YesNo.YES.equals(sysTemplateParam.getIsTitle()) ) {
					params.put(sysTemplateParam.getTemplateVar(), Ognl.getValue(sysTemplateParam.getObjectVar(), OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), dataObj) );
				}				
			}
		}
		return params;
	}
	
	/**
	 * 產生 template 結果
	 * Map 值放入 title 與 message
	 * 
	 * @param templateId	樣板id
	 * @param dataObj		資料來源
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public static TemplateResultObj getResult(String templateId, Object dataObj) throws ServiceException, Exception {
		if (StringUtils.isBlank(templateId) || null==dataObj) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TemplateResultObj resultObj = new TemplateResultObj();
		TbSysTemplate sysTemplate = loadSysTemplate(templateId);
		List<TbSysTemplateParam> sysTemplateParamList = loadSysTemplateParam(templateId);
		Map<String, Object> titleParams = getTemplateParamMap(IS_TITLE, sysTemplateParamList, dataObj);
		Map<String, Object> msgParams = getTemplateParamMap(IS_MESSAGE, sysTemplateParamList, dataObj);
		resultObj.setTitle( processTemplate(sysTemplate.getTitle(), titleParams) );
		resultObj.setContent( processTemplate(sysTemplate.getMessage(), msgParams) );
		return resultObj;
	}
	
	public static String escapeHtml4TemplateHtmlContent(String strContent) throws Exception {
		if (StringUtils.isBlank(strContent)) {
			return "";
		}
		return StringEscapeUtils.escapeHtml4(strContent).replaceAll("\n", Constants.HTML_BR);
	}
	
	/**
	 * 單獨提供讀取出 META-INF/ 之下的 template 檔案內容
	 * 
	 * @param classLoader
	 * @param metaInfFile
	 * @return
	 */
	public static String getResourceSrc(ClassLoader classLoader, String metaInfFile) {
		String out = "";
		try {
			out = IOUtils.toString(classLoader.getResource(metaInfFile).openStream(), Constants.BASE_ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return out;
	}	
	
	/**
	 * 單獨提供單處理 template 取出結果
	 * 
	 * @param name
	 * @param classLoader
	 * @param templateResource
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static String processTemplate(String name, ClassLoader classLoader, String templateResource, Map<String, Object> parameter) throws Exception {
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate("resourceTemplate", getResourceSrc(classLoader, templateResource) );
		Configuration cfg = new Configuration( Configuration.VERSION_2_3_21 );
		cfg.setTemplateLoader(templateLoader);
		Template template = cfg.getTemplate("resourceTemplate", Constants.BASE_ENCODING);
		Writer out = new StringWriter();
		template.process(parameter, out);
		return out.toString();
	}
	
}
