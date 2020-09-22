package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;
import org.qifu.util.SimpleUtils;

public class TbSysEventLog implements java.io.Serializable {
	private static final long serialVersionUID = 3155402228651343287L;
	
	private String oid;
	private String user;
	private String sysId;
	private String executeEvent;
	private String isPermit;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	/* for query grid show */
	public String getCdateString() {
		return SimpleUtils.getDateFormat_yyyyMMddHHmmss(this.cdate);
	}	
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getSysId() {
		return sysId;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public String getExecuteEvent() {
		return executeEvent;
	}
	
	public void setExecuteEvent(String executeEvent) {
		this.executeEvent = executeEvent;
	}
	
	public String getIsPermit() {
		return isPermit;
	}
	
	public void setIsPermit(String isPermit) {
		this.isPermit = isPermit;
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