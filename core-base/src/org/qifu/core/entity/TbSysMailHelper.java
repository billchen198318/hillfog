package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysMailHelper implements java.io.Serializable {
	private static final long serialVersionUID = 830954774590698208L;
	
	private String oid;
	private String mailId;
	private String subject;
	private byte[] text;
	private String mailFrom;
	private String mailTo;
	private String mailCc;
	private String mailBcc;
	private String successFlag;
	private Date successTime;
	private String retainFlag;
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
	
	@EntityUK(name = "mailId")
	public String getMailId() {
		return mailId;
	}
	
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public byte[] getText() {
		return text;
	}
	
	public void setText(byte[] text) {
		this.text = text;
	}	
	
	public String getMailFrom() {
		return mailFrom;
	}
	
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	
	public String getMailTo() {
		return mailTo;
	}
	
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	
	public String getMailCc() {
		return mailCc;
	}
	
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	
	public String getMailBcc() {
		return mailBcc;
	}
	
	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}
	
	public String getSuccessFlag() {
		return successFlag;
	}
	
	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}
	
	public Date getSuccessTime() {
		return successTime;
	}
	
	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}
	
	public String getRetainFlag() {
		return retainFlag;
	}
	
	public void setRetainFlag(String retainFlag) {
		this.retainFlag = retainFlag;
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