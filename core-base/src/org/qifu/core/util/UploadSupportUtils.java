/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.service.ISysUploadService;
import org.qifu.util.SimpleUtils;

public class UploadSupportUtils {
	protected static Logger logger=LogManager.getLogger(UploadSupportUtils.class);
	public static final String HELP_EXPRESSION_VARIABLE = "datas";
	private static final long DEFAULT_UPLOAD_MAX_SIZE = 8388608; // default max 8MB 
	private static long UPLOAD_MAX_SIZE = DEFAULT_UPLOAD_MAX_SIZE;
	private static Properties props = new Properties();
	private static String VIEW_MODE_FILE_EXTENSION[] = null;	
	
	private static BaseInfoConfigProperties baseInfoConfigProperties;
	
	private static ISysUploadService<TbSysUpload, String> sysUploadService;
	
	static {
		baseInfoConfigProperties = AppContext.context.getBean( BaseInfoConfigProperties.class );
		sysUploadService = (ISysUploadService<TbSysUpload, String>) AppContext.context.getBean( ISysUploadService.class );
		try {
			props.load(UploadSupportUtils.class.getClassLoader().getResource("META-INF/upload-support-utils.properties").openStream());
			VIEW_MODE_FILE_EXTENSION = SimpleUtils.getStr(props.getProperty("FILE_EXTENSION")).trim().split(",");
			UPLOAD_MAX_SIZE = NumberUtils.toLong(props.getProperty("UPLOAD_MAX_SIZE"), 0);
			if (UPLOAD_MAX_SIZE < 1048576) { // 1MB binary byte = 1048576
				UPLOAD_MAX_SIZE = 1048576;
			}
			if (UPLOAD_MAX_SIZE > DEFAULT_UPLOAD_MAX_SIZE) {
				UPLOAD_MAX_SIZE = DEFAULT_UPLOAD_MAX_SIZE;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static long getUploadMaxSize() {
		return UPLOAD_MAX_SIZE;
	}
	
	public static String getViewMode(String fileShowName) throws Exception {
		String viewMode = YesNo.NO;
		String fileExtensionName = StringUtils.defaultString( getFileExtensionName(fileShowName) ).trim().toLowerCase();
		for (int i=0; VIEW_MODE_FILE_EXTENSION!=null && i<VIEW_MODE_FILE_EXTENSION.length; i++) {
			if (VIEW_MODE_FILE_EXTENSION[i].toLowerCase().equals(fileExtensionName)) {
				viewMode = YesNo.YES;
			}
		}
		return viewMode;
	}
	
	/**
	 * 把 上傳(upload) 的XML檔轉成Object 
	 * 
	 * @param uploadOid
	 * @param classesToBeBound
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	public Object getTransformObjectData(String uploadOid, Class<?> classesToBeBound) throws ServiceException, Exception {
		Object result = null;
		byte[] xmlBytes = getDataBytes(uploadOid);
		JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		result = jaxbUnmarshaller.unmarshal( new ByteArrayInputStream(xmlBytes) );		
		return result;
	}
	
	public static void cleanTempUpload() throws ServiceException, Exception {		
		cleanTempUpload( baseInfoConfigProperties.getSystem() );
	}
	
	public static void cleanTempUpload(String system) throws ServiceException, Exception {
		logger.info("clean upload(" + system + ") temp begin...");
		sysUploadService.deleteTmpContentBySystem(system);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("system", system);
		paramMap.put("type", UploadTypes.IS_TEMP);
		paramMap.put("isFile", YesNo.YES);		
		DefaultResult<List<TbSysUpload>> searchListResult = sysUploadService.selectListByParams(paramMap);		
		for (int i=0; searchListResult.getValue() != null && i < searchListResult.getValue().size(); i++) {
			TbSysUpload entity = searchListResult.getValue().get(i);
			String dir = getUploadFileDir(entity.getSystem(), entity.getSubDir(), entity.getType());
			String fileFullPath = dir + "/" + entity.getFileName();
			File file = new File(fileFullPath);
			if (!file.exists()) {
				file = null;
				continue;
			}
			try {
				logger.warn("delete : " + file.getPath());
				FileUtils.forceDelete(file);
				sysUploadService.delete(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		logger.info("end...");
	}	
	
	public static String getSubDir() {
		return SimpleUtils.getStrYMD(SimpleUtils.IS_YEAR);
	}
	
	public static String getUploadFileDir(String system, String type) {
		return getUploadFileDir(system, getSubDir(), type);
	}
	
	public static String getUploadFileDir(String system, String subDir, String type) {
		if (StringUtils.isBlank(system) || StringUtils.isBlank(subDir) || StringUtils.isBlank(type)) {
			throw new java.lang.IllegalArgumentException("system, sub-dir and type cann't blank!");
		}
		return baseInfoConfigProperties.getUploadDir() + "/" + system + "/" + type + "/" + subDir + "/";
	}	
	
	public static File mkdirUploadFileDir(String system, String type) throws IOException, Exception {
		return mkdirUploadFileDir(system, getSubDir(), type);
	}
	
	public static File mkdirUploadFileDir(String system, String subDir, String type) throws IOException, Exception {
		String uploadDir = getUploadFileDir(system, subDir, type);
		File dir = new File(uploadDir);
		if (dir.exists() && dir.isDirectory()) {
			return dir;
		}
		FileUtils.forceMkdir(dir);
		return dir;
	}
	
	public static String generateRealFileName(String showName) {
		if (StringUtils.isBlank(showName)) {
			throw new java.lang.IllegalArgumentException("name is blank!");
		}
		String extension = getFileExtensionName(showName);
		if (StringUtils.isBlank(extension)) {
			return SimpleUtils.getUUIDStr();
		}
		if (extension.length()>13) { // uuid 加上 點 "." 後是 37 字元 , TB_SYS_UPLOAD.FILE_NAME 最大為 50 , 50-37 = 13
			extension = extension.substring(0, 13);
		}
		return SimpleUtils.getUUIDStr() + "." + extension;
	}	
	
	public static String generateRealFileName(File file) {
		if (file==null) {
			throw new java.lang.IllegalArgumentException("file is null!");
		}
		return generateRealFileName( file.getName() );
	}
	
	public static File getRealFile(String uploadOid) throws ServiceException, IOException, Exception {
		if (StringUtils.isBlank(uploadOid)) {
			throw new Exception("parameter is blank!");
		}
		TbSysUpload uploadObj = findUpload(uploadOid);
		File packageFile = null;
		if (!YesNo.YES.equals(uploadObj.getIsFile())) {
			File dir = new File( Constants.getWorkTmpDir() + "/" + UploadSupportUtils.class.getSimpleName() );
			if (!dir.exists() || !dir.isDirectory()) {
				FileUtils.forceMkdir(dir);
			}			
			String tmpFileName = dir.getPath() + "/" + SimpleUtils.getUUIDStr() + "." + getFileExtensionName(uploadObj.getShowName());
			dir = null;
			OutputStream fos = null;
			try {
				packageFile = new File( tmpFileName );
				fos = new FileOutputStream(packageFile);
				IOUtils.write(uploadObj.getContent(), fos);		
				fos.flush();
			} catch (IOException e) {
				throw e;
			} finally {
				if (fos!=null) {
					fos.close();
				}
				fos = null;
			}			
		} else {
			String uploadDir = getUploadFileDir(uploadObj.getSystem(), uploadObj.getSubDir(), uploadObj.getType());
			packageFile = new File( uploadDir + "/" + uploadObj.getFileName() );			
		}		
		if (!packageFile.exists()) {
			throw new Exception("File is missing: " + uploadObj.getFileName() );
		}
		return packageFile;
	}
	
	public static byte[] getDataBytes(String uploadOid) throws ServiceException, IOException, Exception {
		if (StringUtils.isBlank(uploadOid)) {
			throw new Exception("parameter is blank!");
		}
		byte datas[] = null;
		TbSysUpload uploadObj = findUpload(uploadOid);
		datas = uploadObj.getContent();
		if (YesNo.YES.equals(uploadObj.getIsFile())) {
			String uploadDir = getUploadFileDir(uploadObj.getSystem(), uploadObj.getSubDir(), uploadObj.getType());
			File file = new File( uploadDir + "/" + uploadObj.getFileName() );
			datas = FileUtils.readFileToByteArray(file);
			file = null;
		}
		return datas;
	}
	
	public static DefaultResult<Boolean> updateType(String oid, String type) throws ServiceException, IOException, Exception {
		DefaultResult<TbSysUpload> uploadResult = sysUploadService.selectByPrimaryKeySimple(oid);
		if (uploadResult.getValue()==null) {
			throw new ServiceException(uploadResult.getMessage());
		}
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		result.setValue( false );
		result.setMessage( BaseSystemMessage.updateFail() );
		TbSysUpload upload = uploadResult.getValue();
		if (!YesNo.YES.equals(upload.getIsFile())) {
			upload.setType(type);
			uploadResult = sysUploadService.update(upload);			
			if (uploadResult.getValue() != null) {
				result.setValue( true );
				result.setMessage( uploadResult.getMessage() );
			}
			return result;
		}
		
		// move file to new dir.
		String oldFullPath = getUploadFileDir(upload.getSystem(), upload.getSubDir(), upload.getType()) + upload.getFileName();
		String newFullPath = getUploadFileDir(upload.getSystem(), upload.getSubDir(), type) + upload.getFileName();
		File newFile = new File(newFullPath);
		if (newFile.isFile() && newFile.exists()) {
			newFile = null;
			throw new Exception("error. file exists, cannot operate!");
		}
		//newFile = null;
		File oldFile = new File(oldFullPath);
		if (!oldFile.exists()) {
			oldFile = null;
			throw new Exception("error. file no exists: " + oldFullPath);
		}
		
		try {
			FileUtils.moveFile(oldFile, newFile);
			
			upload.setType(type);
			uploadResult = sysUploadService.update(upload);
			
		} catch (Exception e) {
			newFile = null;
			oldFile = null;
			throw e;
		}
		return result;
	}
	
	public static String create(String system, String type, boolean isFile, File file, String showName) throws ServiceException, IOException, Exception {
		if (StringUtils.isBlank(type) || null == file || StringUtils.isBlank(showName)) {
			throw new Exception("parameter is blank!");
		}				
		if (!file.exists()) {
			throw new Exception("file no exists!");
		}
		TbSysUpload upload = new TbSysUpload();		
		upload.setIsFile( ( isFile ? YesNo.YES : YesNo.NO ) );
		upload.setShowName(showName);
		upload.setSystem(system);
		upload.setType(type);
		upload.setSubDir( getSubDir() );		
		if (isFile) {
			String uploadDir = getUploadFileDir(system, type);
			String uploadFileName = generateRealFileName(file);
			mkdirUploadFileDir(system, type);
			FileUtils.copyFile(file, new File(uploadDir + "/" + uploadFileName) );
			upload.setFileName( uploadFileName );			
		} else {
			upload.setContent( FileUtils.readFileToByteArray(file) );
			upload.setFileName( " " );			
		}		
		DefaultResult<TbSysUpload> result =  sysUploadService.insert(upload);
		if ( result.getValue() == null ) {
			throw new ServiceException( result.getMessage() );
		}		
		return result.getValue().getOid();
	}
	
	public static String create(String system, String type, boolean isFile, byte[] datas, String showName) throws ServiceException, IOException, Exception {
		if (StringUtils.isBlank(type) || null == datas || StringUtils.isBlank(showName)) {
			throw new Exception("parameter is blank!");
		}				
		TbSysUpload upload = new TbSysUpload();		
		upload.setIsFile( ( isFile ? YesNo.YES : YesNo.NO ) );
		upload.setShowName(showName);
		upload.setSystem(system);
		upload.setType(type);
		upload.setSubDir( getSubDir() );		
		if (isFile) {
			String uploadDir = getUploadFileDir(system, type);
			String uploadFileName = generateRealFileName(showName);
			mkdirUploadFileDir(system, type);
			File file = null;
			try {
				file = new File( uploadDir + "/" + uploadFileName );
				FileUtils.writeByteArrayToFile(file, datas);
			} catch (Exception e) {
				throw e;
			} finally {
				file = null;
			}
			upload.setFileName( uploadFileName );			
		} else {
			upload.setContent( datas );
			upload.setFileName( " " );			
		}		
		DefaultResult<TbSysUpload> result =  sysUploadService.insert(upload);
		if ( result.getValue() == null ) {
			throw new ServiceException( result.getMessage() );
		}		
		return result.getValue().getOid();
	}	
	
	public static String getFileExtensionName(String fileFullName) {
		if (StringUtils.isBlank(fileFullName)) {
			return "";
		}
		String extension = "";
		String[] tmp = fileFullName.split("[.]");
		for (int i=1; tmp!=null && i<tmp.length; i++) {
			extension = tmp[i];
		}
		return extension;
	}
	
	public static TbSysUpload findUpload(String uploadOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(uploadOid)) {
			throw new Exception("Upload OID parameter is blank!");
		}		
		DefaultResult<TbSysUpload> result = sysUploadService.selectByPrimaryKey(uploadOid);
		if (result.getValue()==null) {
			throw new ServiceException(result.getMessage());
		}
		return result.getValue();
	}
	
	public static TbSysUpload findUploadNoByteContent(String uploadOid) throws ServiceException, Exception {
		if (StringUtils.isBlank(uploadOid)) {
			throw new Exception("Upload OID parameter is blank!");
		}		
		DefaultResult<TbSysUpload> result = sysUploadService.selectByPrimaryKeySimple(uploadOid);
		if (result.getValue()==null) {
			throw new ServiceException(result.getMessage());
		}
		return result.getValue();
	}
	
}
