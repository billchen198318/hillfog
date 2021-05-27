package org.qifu.hillfog.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfPdcaAttc implements java.io.Serializable {
	private static final long serialVersionUID = 9048313352103983217L;

    private String oid;
    private String pdcaOid;
    private String uploadOid;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    private String showName; // PDCA detail檢視用
    
    public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

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
    
    public String getUploadOid() {
        return uploadOid;
    }
    
    public void setUploadOid(String uploadOid) {
        this.uploadOid = uploadOid;
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
