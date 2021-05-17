package org.qifu.hillfog.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfPdcaItemOwner implements java.io.Serializable {
	private static final long serialVersionUID = -5498281401419155091L;
	
    private String oid;
    private String pdcaOid;
    private String itemOid;
    private String ownerUid;
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
    
    @EntityUK(name = "pdcaOid")
    public String getPdcaOid() {
        return pdcaOid;
    }
    
    public void setPdcaOid(String pdcaOid) {
        this.pdcaOid = pdcaOid;
    }
    
    @EntityUK(name = "itemOid")
    public String getItemOid() {
        return itemOid;
    }
    
    public void setItemOid(String itemOid) {
        this.itemOid = itemOid;
    }
    
    @EntityUK(name = "ownerUid")
    public String getOwnerUid() {
        return ownerUid;
    }
    
    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
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
