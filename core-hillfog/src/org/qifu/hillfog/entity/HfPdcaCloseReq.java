package org.qifu.hillfog.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfPdcaCloseReq implements java.io.Serializable {
	private static final long serialVersionUID = 8911759764738515289L;

	private String oid;
    private String pdcaOid;
    private String description;
    private String confirmUid;
    private Date confirmDate;
    private String applyFlag;
    private String applyText;
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
    
    public String getPdcaOid() {
        return pdcaOid;
    }
    
    public void setPdcaOid(String pdcaOid) {
        this.pdcaOid = pdcaOid;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getConfirmUid() {
        return confirmUid;
    }
    
    public void setConfirmUid(String confirmUid) {
        this.confirmUid = confirmUid;
    }
    
    public Date getConfirmDate() {
        return confirmDate;
    }
    
    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }
    
    public String getApplyFlag() {
        return applyFlag;
    }
    
    public void setApplyFlag(String applyFlag) {
        this.applyFlag = applyFlag;
    }
    
    public String getApplyText() {
		return applyText;
	}

	public void setApplyText(String applyText) {
		this.applyText = applyText;
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
