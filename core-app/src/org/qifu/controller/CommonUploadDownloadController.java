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
package org.qifu.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.service.ISysUploadService;
import org.qifu.core.util.UploadSupportUtils;
import org.qifu.util.FSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Controller
public class CommonUploadDownloadController extends BaseControllerSupport {
	
	@Autowired
	ISysUploadService<TbSysUpload, String> sysUploadService;
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROGCOMM0003Q")
	@RequestMapping(value = "/commonCheckUploadFileJson")		
	public @ResponseBody DefaultControllerJsonResultObj<String> checkUpload(HttpServletResponse response, @RequestParam("oid") String oid) {
		DefaultControllerJsonResultObj<String> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		if (StringUtils.isBlank(oid)) {
			result.setMessage( BaseSystemMessage.parameterBlank() );
			return result;
		}
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("oid", oid);
			if ( this.sysUploadService.count(paramMap) == 1 ) {
				result.setValue( oid );
				result.setSuccess( YES );
				result.setMessage( BaseSystemMessage.dataIsExist() );
			} else {
				result.setMessage( BaseSystemMessage.dataNoExist() );
			}
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			exceptionResult(result, e);
		}
		return result;
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROGCOMM0003Q")
	@RequestMapping(value = "/commonDownloadFile")
	public void downloadFile(HttpServletResponse response, @RequestParam("oid") String oid) throws UnsupportedEncodingException, IOException {
		TbSysUpload uploadData = null;
		String fileName = "";
		byte[] content = null;		
		try {
			DefaultResult<TbSysUpload> result = sysUploadService.selectByPrimaryKey(oid);
			if (result.getValue() != null) {
				uploadData = result.getValue();
				fileName = UploadSupportUtils.generateRealFileName( uploadData.getShowName() );
				content = uploadData.getContent();
				if (content == null && YES.equals(uploadData.getIsFile())) { // 檔案模式, 所以沒有byte content
					content = UploadSupportUtils.getDataBytes(oid);
				}
			}
		} catch (AuthorityException | ServiceException | ControllerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( content == null ) { // 沒有資料
            OutputStream outputStream = response.getOutputStream();
            outputStream.write( BaseSystemMessage.dataNoExist().getBytes(Constants.BASE_ENCODING) );
            outputStream.close();
            return;				
		}
		response.setContentType( MediaType.APPLICATION_OCTET_STREAM_VALUE );
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
		response.setContentLength( content.length );
		FileCopyUtils.copy(content, response.getOutputStream());
	}
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROGCOMM0003Q")
	@RequestMapping(value = "/commonViewFile")
	public void viewFile(HttpServletResponse response, @RequestParam("oid") String oid) throws UnsupportedEncodingException, IOException {
		TbSysUpload uploadData = null;
		String fileName = "";
		String mimeType = "";
		byte[] content = null;		
		try {
			DefaultResult<TbSysUpload> result = sysUploadService.selectByPrimaryKey(oid);
			if (result.getValue() != null) {
				uploadData = result.getValue();
				//fileName = UploadSupportUtils.generateRealFileName( uploadData.getShowName() );
				fileName = uploadData.getShowName();
				content = uploadData.getContent();
				if (content == null && YES.equals(uploadData.getIsFile())) { // 檔案模式, 所以沒有byte content
					content = UploadSupportUtils.getDataBytes(oid);
				}
				mimeType = FSUtils.getMimeType(fileName);
			}
		} catch (AuthorityException | ServiceException | ControllerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( content == null ) { // 沒有資料
            OutputStream outputStream = response.getOutputStream();
            outputStream.write( BaseSystemMessage.dataNoExist().getBytes(Constants.BASE_ENCODING) );
            outputStream.close();
            return;				
		}
		if (StringUtils.isBlank(mimeType)) {
			mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		response.setContentType( mimeType );
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
		response.setContentLength( content.length );
		FileCopyUtils.copy(content, response.getOutputStream());
	}	
	
	@ControllerMethodAuthority(check = true, programId = "CORE_PROGCOMM0003Q")
	@RequestMapping(value = "/commonUploadFileJson", method = { RequestMethod.POST }, headers = "content-type=multipart/*" )		
	public @ResponseBody DefaultControllerJsonResultObj<String> uploadFile(
			@RequestParam("commonUploadFile") MultipartFile file, 
			@RequestParam("commonUploadFileType") String type, 
			@RequestParam("commonUploadFileIsFileMode") String isFile,
			@RequestParam("commonUploadFileSystem") String system) {
		
		DefaultControllerJsonResultObj<String> result = this.getDefaultJsonResult(this.currentMethodAuthority());
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}	
		if (null == file || file.getSize() < 1) {
			result.setMessage( BaseSystemMessage.uploadFileNoSelect() );
			return result;			
		}
		if (file.getSize() > UploadSupportUtils.getUploadMaxSize()) {
			result.setMessage( "File max size only " + UploadSupportUtils.getUploadMaxSize() + " bytes!"  );
			return result;
		}
		if (!UploadTypes.check(type)) {
			result.setMessage( BaseSystemMessage.updateFileTypeError() );
			return result;
		}
		try {
			String uploadOid = UploadSupportUtils.create(system, type, ( YES.equals(isFile) ? true : false ), file.getBytes(), file.getOriginalFilename());
			if (!StringUtils.isBlank(uploadOid)) {
				result.setSuccess( YES );
				result.setValue(uploadOid);
				result.setMessage( BaseSystemMessage.insertSuccess() );
			} else {
				result.setMessage( BaseSystemMessage.insertFail() );
			}
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			exceptionResult(result, e);
		}		
		return result;
	}

}