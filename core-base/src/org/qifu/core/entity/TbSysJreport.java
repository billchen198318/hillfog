package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysJreport implements java.io.Serializable {
	private static final long serialVersionUID = -7017671423905196440L;
	
	private String oid;
	private String reportId;
	private String file;
	private String isCompile;
	private byte[] content;
	private String description;
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
	
	@EntityUK(name = "reportId")
	public String getReportId() {
		return reportId;
	}
	
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public String getIsCompile() {
		return isCompile;
	}
	
	public void setIsCompile(String isCompile) {
		this.isCompile = isCompile;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}		
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
