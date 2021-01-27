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
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysMenu;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.model.MenuItemType;
import org.qifu.core.model.MenuResult;
import org.qifu.core.model.User;
import org.qifu.core.service.ISysMenuService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.qifu.core.vo.SysMenuVO;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class MenuSupportUtils {
	private static final String _MODAL_HTML_RES = "META-INF/resource/modal.htm.ftl";
	
	private static ISysService<TbSys, String> sysService;
	
	private static ISysMenuService<TbSysMenu, String> sysMenuService;
	
	private static ISysProgService<TbSysProg, String> sysProgService;
	
	private static String _MODAL_TEMPLATE_STR = "";
	
	static {
		
		sysService = (ISysService<TbSys, String>) AppContext.context.getBean(ISysService.class);
		sysMenuService = (ISysMenuService<TbSysMenu, String>) AppContext.context.getBean(ISysMenuService.class);
		sysProgService = (ISysProgService<TbSysProg, String>) AppContext.context.getBean(ISysProgService.class);
		
		InputStream is = null;
		try {
			is = MenuSupportUtils.class.getClassLoader().getResource( _MODAL_HTML_RES ).openStream();
			_MODAL_TEMPLATE_STR = IOUtils.toString(is, Constants.BASE_ENCODING);			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
			is = null;			
		}
	}
	
	private static String getModalHtml(TbSysProg prog) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prog", prog);
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		templateLoader.putTemplate("resourceTemplate", _MODAL_TEMPLATE_STR );
		Configuration cfg = new Configuration( Configuration.VERSION_2_3_21 );
		cfg.setTemplateLoader(templateLoader);
		Template template = cfg.getTemplate("resourceTemplate", Constants.BASE_ENCODING);
		Writer out = new StringWriter();
		template.process(paramMap, out);
		paramMap.clear();
		paramMap = null;
		return out.toString();
	}
	
	public static TbSysProg loadSysProg(String progId) throws ServiceException, Exception {
		TbSysProg tbSysProg = new TbSysProg();
		tbSysProg.setProgId(progId);
		DefaultResult<TbSysProg> result = sysProgService.selectByUniqueKey(tbSysProg);
		if (null == result.getValue()) {
			throw new ServiceException( BaseSystemMessage.dataErrors() );
		}
		return result.getValue();
	}		
	
	public static String getUrl(String basePath, TbSys sys, TbSysProg sysProg) throws Exception {
		String url = "";
		if (StringUtils.isBlank(sysProg.getUrl())) {
			return url;
		}
		if (YesNo.YES.equals(sys.getIsLocal())) {
			url = basePath + sysProg.getUrl() + ( (sysProg.getUrl().indexOf("?")>0 || sysProg.getUrl().indexOf("&")>0) ? "&" : "?" ) + Constants.QIFU_PAGE_IN_TAB_IFRAME + "=" + YesNo.YES;
		} else {
			String head = "http://";
			if (basePath.startsWith("https")) {
				head = "https://";
			}
			url = head + (sys.getHost() + "/" + sys.getContextPath() + "/" + sysProg.getUrl()).replaceAll("///", "/").replaceAll("//", "/")
					+ ( (sysProg.getUrl().indexOf("?")>0 || sysProg.getUrl().indexOf("&")>0) ? "&" : "?" ) + Constants.QIFU_PAGE_IN_TAB_IFRAME + "=" + YesNo.YES;
		}
		return url;
	}
	
	public static String getFirstLoadJavascript() throws ServiceException, Exception {		
		return SystemSettingConfigureUtils.getFirstLoadJavascriptValue();
	}		
	
	public static MenuResult getMenuData(String basePath) throws ServiceException, Exception {
		DefaultResult<List<TbSys>> sysListResult = sysService.selectList("NAME", SortType.ASC);
		if (sysListResult.getValue() == null) {
			throw new ServiceException( sysListResult.getMessage() );
		}
		MenuResult resultObj = new MenuResult();
		StringBuilder jsSb = new StringBuilder();
		StringBuilder navHtmlSb = new StringBuilder();
		StringBuilder modalHtmlSb = new StringBuilder();
		jsSb.append("var _prog = []; ").append("\n");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (TbSys sys : sysListResult.getValue()) {
			paramMap.clear();
			paramMap.put("progSystem", sys.getSysId());
			DefaultResult<List<TbSysProg>> sysProgListResult = sysProgService.selectListByParams(paramMap, "PROG_ID", SortType.ASC);
			List<TbSysProg> sysProgList = sysProgListResult.getValue();
			for (int i=0; sysProgListResult.getValue()!=null && i<sysProgListResult.getValue().size(); i++) {
				TbSysProg sysProg = sysProgListResult.getValue().get(i);
				jsSb.append("_prog.push({\"id\" : \"" + sysProg.getProgId() + "\", \"itemType\" : \"" + sysProg.getItemType() + "\", \"name\" : \"" + sysProg.getName() + "\", \"icon\" : \"" + IconUtils.getUrl(basePath, sysProg.getIcon()) + "\", \"url\" : \"" + getUrl(basePath, sys, sysProg) + "\", \"font_icon_class_id\" : \"" + sysProg.getFontIconClassId() + "\"});").append("\n");
				
				if (YesNo.YES.equals(sysProg.getIsDialog())) {
					modalHtmlSb.append( getModalHtml(sysProg) );
				}
				
			}
			
			User user = UserUtils.getCurrentUser();
			String account = user.getUsername();
			if (UserUtils.isAdmin()) {
				account = null;
			} 			
			DefaultResult<List<SysMenuVO>> menuResult = sysMenuService.findForMenuGenerator(sys.getSysId(), account);
			if (menuResult.getValue() == null) {
				continue;
			}
			List<SysMenuVO> menuList = menuResult.getValue();
			List<SysMenuVO> parentSysMenuList = searchFolder(menuList);
			for (SysMenuVO pMenu : parentSysMenuList) {
				List<SysMenuVO> childSysMenuList = searchItem(pMenu.getOid(), menuList);
				if (childSysMenuList==null || childSysMenuList.size()<1) {
					continue;
				}		
				TbSysProg pSysProg = searchProg(pMenu, sysProgList);
				if (null == pSysProg) {
					throw new ServiceException( BaseSystemMessage.dataErrors() );
				}
				
				navHtmlSb.append("<li class=\"treeview\"><a class=\"app-menu__item\" href=\"#\" data-toggle=\"treeview\"><i class=\"app-menu__icon fa fa-" + pSysProg.getFontIconClassId() + "\"></i><span class=\"app-menu__label\">" + pSysProg.getName() + "</span><i class=\"treeview-indicator fa fa-angle-right\"></i></a>");
				navHtmlSb.append("<ul class=\"treeview-menu\">");
				
				for (SysMenuVO cMenu : childSysMenuList) {
					TbSysProg cSysProg = searchProg(cMenu, sysProgList);
					if (null == cSysProg) {
						throw new ServiceException( BaseSystemMessage.dataErrors() );
					}
					navHtmlSb.append("<li><a class=\"treeview-item\" href=\"#\" onclick=\"addTab('" + cSysProg.getProgId() + "', null);\"><i class=\"icon fa fa-" + cSysProg.getFontIconClassId() + "\"></i>" + "&nbsp;&nbsp;" + cSysProg.getName() + "</a></li>");
				}
				
				navHtmlSb.append("</ul>");
				navHtmlSb.append("</li>");
				
			}
			
		}
		
		resultObj.setNavItemHtmlData(navHtmlSb.toString());
		resultObj.setJavascriptData(jsSb.toString());
		resultObj.setModalHtmlData(modalHtmlSb.toString());
		return resultObj;
	}
	
	public static String getProgramName(String progId) {
		String name = "unknown-program";
		if (StringUtils.isBlank(progId)) {
			return name;
		}
		DefaultResult<TbSysProg> result = null;
		TbSysProg sysProg = new TbSysProg();
		sysProg.setProgId(progId);
		try {
			result = sysProgService.selectByUniqueKey(sysProg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result.getValue() != null) {
			name = result.getValue().getName();
		}
		return name;
	}	
	
	protected static TbSysProg searchProg(SysMenuVO menu, List<TbSysProg> sysProgList) throws Exception {
		TbSysProg prog = null;
		for (int i=0; i<sysProgList.size() && prog == null; i++) {
			if ( sysProgList.get(i).getProgId().equals(menu.getProgId()) ) {
				prog = sysProgList.get(i);
				i = sysProgList.size();
			}
		}
		return prog;
	}	
	
	/**
	 * 取是目錄選單的資料
	 * 
	 * @param sysMenuList
	 * @return
	 * @throws Exception
	 */
	protected static List<SysMenuVO> searchFolder(List<SysMenuVO> sysMenuList) throws Exception {
		List<SysMenuVO> folderList = new ArrayList<SysMenuVO>();
		for (SysMenuVO sysMenu : sysMenuList) {
			if (MenuItemType.FOLDER.equals(sysMenu.getItemType()) && YesNo.YES.equals(sysMenu.getEnableFlag()) ) {
				folderList.add(sysMenu);
			}
		}
		return folderList;
	}	
	
	/**
	 * 取目錄下的選單項目
	 * 
	 * @param parentOid
	 * @param sysMenuList
	 * @return
	 * @throws Exception
	 */
	protected static List<SysMenuVO> searchItem(String parentOid, List<SysMenuVO> sysMenuList) throws Exception {
		List<SysMenuVO> folderList = new ArrayList<SysMenuVO>();
		for (SysMenuVO sysMenu : sysMenuList) {
			if (MenuItemType.ITEM.equals(sysMenu.getItemType()) && parentOid.equals(sysMenu.getParentOid())
					&& YesNo.YES.equals(sysMenu.getEnableFlag()) ) {
				folderList.add(sysMenu);
			}
		}
		return folderList;
	}
	
}
