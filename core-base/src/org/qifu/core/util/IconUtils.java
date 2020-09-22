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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbSysIcon;
import org.qifu.core.service.ISysIconService;

public class IconUtils {
	public static final String ICON_FOLDER = "icons/";
	
	private static ISysIconService<TbSysIcon, String> sysIconService;
	
	static {
		sysIconService = (ISysIconService<TbSysIcon, String>) AppContext.context.getBean(ISysIconService.class);
	}

	public static String getUrl(String basePath, String iconId) throws ServiceException, Exception {
		String url = "";
		if (StringUtils.isBlank(iconId)) {
			return url;
		}
		TbSysIcon sysIcon = new TbSysIcon();
		sysIcon.setIconId(iconId);
		DefaultResult<TbSysIcon> result = sysIconService.selectByUniqueKey(sysIcon);
		sysIcon = result.getValue();
		if (sysIcon!=null && StringUtils.defaultString(sysIcon.getFileName()).trim().length()>0) {
			url = basePath + ICON_FOLDER + StringUtils.defaultString(sysIcon.getFileName());
		}
		return url;
	}
	
	public static String getMenuIcon(String basePath, String iconId) throws ServiceException, Exception {
		String img = getHtmlImg(basePath, iconId);
		if (!"".equals(img)) {
			img += "&nbsp;";
		}
		return img;		
	}
	
	public static String getHtmlImg(String basePath, String iconId) throws ServiceException, Exception {
		String img = "";
		String url = "";
		if (!"".equals( url=getUrl(basePath, iconId) ) ) {
			img = "<img src='" + url + "' border='0' />";
		}
		return img;
	}
	
	public static Map<String, String> getIconsSelectData() throws ServiceException, Exception {
		Map<String, String> dataMap = new LinkedHashMap<String, String>();
		DefaultResult<List<TbSysIcon>> result = sysIconService.selectList();
		if (null==result.getValue() || result.getValue().size()<1) {
			return dataMap;
		}
		for (TbSysIcon entity : result.getValue()) {
			String label = "<img src='./" + ICON_FOLDER + entity.getFileName() + "' border='0'/>&nbsp;" + entity.getIconId();
			dataMap.put(entity.getOid(), label);
		}
		return dataMap;
	}
	
	public static String getJsData() throws ServiceException, Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("var _iconData = [];");
		sb.append("\n");
		DefaultResult<List<TbSysIcon>> result = sysIconService.selectList();
		if (null==result.getValue() || result.getValue().size()<1) {
			return sb.toString();
		}
		for (TbSysIcon entity : result.getValue()) {
			sb.append("_iconData.push({\"oid\" : \"").append( entity.getOid() ).append("\", \"iconId\" : \"").append( entity.getIconId() ).append("\", \"fileName\" : \"").append( entity.getFileName() ).append("\"});");
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
