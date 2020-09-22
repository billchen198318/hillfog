package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysBeanHelpExprMap implements java.io.Serializable {
	private static final long serialVersionUID = -6097063690459430189L;
	
	private String oid;
	private String helpExprOid;
	private String methodResultFlag;
	private String methodParamClass;
	private Integer methodParamIndex;
	private String varName;
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
	
	@EntityUK(name = "helpExprOid")
	public String getHelpExprOid() {
		return helpExprOid;
	}
	
	public void setHelpExprOid(String helpExprOid) {
		this.helpExprOid = helpExprOid;
	}
	
	public String getMethodResultFlag() {
		return methodResultFlag;
	}
	
	public void setMethodResultFlag(String methodResultFlag) {
		this.methodResultFlag = methodResultFlag;
	}
	
	public String getMethodParamClass() {
		return methodParamClass;
	}
	
	public void setMethodParamClass(String methodParamClass) {
		this.methodParamClass = methodParamClass;
	}
	
	public Integer getMethodParamIndex() {
		return methodParamIndex;
	}
	
	public void setMethodParamIndex(Integer methodParamIndex) {
		this.methodParamIndex = methodParamIndex;
	}
	
	@EntityUK(name = "varName")
	public String getVarName() {
		return varName;
	}
	
	public void setVarName(String varName) {
		this.varName = varName;
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