package org.qifu.core.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysExprJob implements java.io.Serializable {
	private static final long serialVersionUID = -3250939070255018399L;
	
	private String oid;
	private String system;
	private String id;
	private String name;
	private String active;
	private String description;
	private String runStatus;
	private String checkFault;
	private String exprId;
	private String runDayOfWeek;
	private String runHour;
	private String runMinute;
	private String contactMode;
	private String contact;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	// 查詢Grid 顯示用
	public String getRunDatetime() {
		return StringUtils.defaultString(this.runDayOfWeek) 
				+ ( (!StringUtils.isBlank(this.runDayOfWeek) && !StringUtils.isBlank(this.runHour)) ? "/" : "" ) 
				+ StringUtils.defaultString(this.runHour) 
				+ ( (!StringUtils.isBlank(this.runHour) && !StringUtils.isBlank(this.runMinute)) ? "/" : "" )
				+ StringUtils.defaultString(this.runMinute);
	}	
	
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
	
	@EntityUK(name = "id")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getActive() {
		return active;
	}
	
	public void setActive(String active) {
		this.active = active;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRunStatus() {
		return runStatus;
	}
	
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	
	public String getCheckFault() {
		return checkFault;
	}
	
	public void setCheckFault(String checkFault) {
		this.checkFault = checkFault;
	}
	
	public String getExprId() {
		return exprId;
	}
	
	public void setExprId(String exprId) {
		this.exprId = exprId;
	}
	
	public String getRunDayOfWeek() {
		return runDayOfWeek;
	}
	
	public void setRunDayOfWeek(String runDayOfWeek) {
		this.runDayOfWeek = runDayOfWeek;
	}
	
	public String getRunHour() {
		return runHour;
	}
	
	public void setRunHour(String runHour) {
		this.runHour = runHour;
	}
	
	public String getRunMinute() {
		return runMinute;
	}
	
	public void setRunMinute(String runMinute) {
		this.runMinute = runMinute;
	}
	
	public String getContactMode() {
		return contactMode;
	}
	
	public void setContactMode(String contactMode) {
		this.contactMode = contactMode;
	}
	
	public String getContact() {
		return contact;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
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