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
package org.qifu.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.Constants;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.model.PageOf;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigJsController extends BaseControllerSupport {
	
	@RequestMapping(value = "/configJs.js", method = RequestMethod.GET)
	public @ResponseBody String execute(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("var _qifu_delimiter='").append(Constants.ID_DELIMITER).append("';").append("\n");
		sb.append("var _qifu_inputNameDelimiter='").append(Constants.INPUT_NAME_DELIMITER).append("';").append("\n");
		sb.append("var _qifu_success_flag='").append(YES).append("';").append("\n");
		sb.append("var _qifu_please_select_id='").append(Constants.HTML_SELECT_NO_SELECT_ID).append("';").append("\n");
		sb.append("var _qifu_please_select_name='").append(Constants.HTML_SELECT_NO_SELECT_NAME).append("';").append("\n");
		sb.append("var _qifu_default_pageRowSize=").append(PageOf.Rows[0]).append(";").append("\n");
		sb.append("var _qifu_basePath='").append( super.getBasePath(request) ).append("';").append("\n");
		sb.append("var _qifu_jqXhrType='").append( super.getJqXhrType() ).append("';").append("\n");
		sb.append("var _qifu_jqXhrTimeout=").append( super.getJqXhrTimeout() ).append(";").append("\n");
		sb.append("var _qifu_jqXhrCache=").append( YES.equals(super.getJqXhrCache()) ? "true" : "false" ).append(";").append("\n");
		sb.append("var _qifu_jqXhrAsync=").append( YES.equals(super.getJqXhrAsync()) ? "true" : "false" ).append(";").append("\n");
		sb.append("var _qifu_defaultSelfPleaseWaitShow='").append(NO).append("';").append("\n"); // YES使用iframe 內部的 please wait, NO使用外部的 parent 的 please wait
		sb.append("var _qifu_maxUploadSize=").append( super.getMaxUploadSize() ).append(";").append("\n");
		sb.append("var _qifu_pageInTabIframeParamName='").append(Constants.QIFU_PAGE_IN_TAB_IFRAME).append("';").append("\n");
		return sb.toString();
	}	

}
