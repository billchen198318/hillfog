package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;
import org.qifu.util.SimpleUtils;

public class TbSysExprJobLog implements java.io.Serializable {
	private static final long serialVersionUID = -8972374676604198929L;
	
	private String oid;
	private String id;
	private String logStatus;
	private Date beginDatetime;
	private Date endDatetime;
	private String faultMsg;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	/* for query Grid show */
	public String getBeginDatetimeString() {
		return SimpleUtils.getDateFormat_yyyyMMddHHmmss(this.beginDatetime);
	}	
	
	/* for query Grid show */
	public String getEndDatetimeString() {
		return SimpleUtils.getDateFormat_yyyyMMddHHmmss(this.endDatetime);
	}	
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLogStatus() {
		return logStatus;
	}
	
	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}
	
	public Date getBeginDatetime() {
		return beginDatetime;
	}
	
	public void setBeginDatetime(Date beginDatetime) {
		this.beginDatetime = beginDatetime;
	}
	
	public Date getEndDatetime() {
		return endDatetime;
	}
	
	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}
	
	public String getFaultMsg() {
		return faultMsg;
	}
	
	public void setFaultMsg(String faultMsg) {
		this.faultMsg = faultMsg;
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