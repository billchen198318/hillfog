package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysUpload implements java.io.Serializable {
	private static final long serialVersionUID = 4559886755738815004L;
	
	private String oid;
	private String system;
	private String subDir;
	private String type;
	private String fileName;
	private String showName;
	private String isFile;
	private byte[] content;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getSubDir() {
		return subDir;
	}
	
	public void setSubDir(String subDir) {
		this.subDir = subDir;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getShowName() {
		return showName;
	}
	
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getIsFile() {
		return isFile;
	}
	
	public void setIsFile(String isFile) {
		this.isFile = isFile;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}	
	
	@CreateUserField(name = "cuserid")
	public String getCuserid() {
		return cuserid;
	}
	
	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}
	
	@CreateDateField(name = "cdate")
	public Date getCdate() {
		return cdate;
	}
	
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	
	@UpdateUserField(name = "uuserid")
	public String getUuserid() {
		return uuserid;
	}
	
	public void setUuserid(String uuserid) {
		this.uuserid = uuserid;
	}
	
	@UpdateDateField(name = "udate")
	public Date getUdate() {
		return udate;
	}
	
	public void setUdate(Date udate) {
		this.udate = udate;
	}
	
}