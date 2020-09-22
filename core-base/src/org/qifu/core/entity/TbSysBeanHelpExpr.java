package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbSysBeanHelpExpr implements java.io.Serializable {
	private static final long serialVersionUID = -4857192361640044013L;
	
	private String oid;
	private String helpOid;
	private String exprId;
	private String exprSeq;
	private String runType;
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
	
	@EntityUK(name = "helpOid")
	public String getHelpOid() {
		return helpOid;
	}
	
	public void setHelpOid(String helpOid) {
		this.helpOid = helpOid;
	}
	
	@EntityUK(name = "exprId")
	public String getExprId() {
		return exprId;
	}
	
	public void setExprId(String exprId) {
		this.exprId = exprId;
	}
	
	public String getExprSeq() {
		return exprSeq;
	}
	
	public void setExprSeq(String exprSeq) {
		this.exprSeq = exprSeq;
	}
	
	@EntityUK(name = "runType")
	public String getRunType() {
		return runType;
	}
	
	public void setRunType(String runType) {
		this.runType = runType;
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