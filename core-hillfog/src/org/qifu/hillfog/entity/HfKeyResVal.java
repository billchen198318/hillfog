package org.qifu.hillfog.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfKeyResVal implements java.io.Serializable {
	private static final long serialVersionUID = -3865636848706559936L;

    private String oid;
    private String objOid;
    private String resOid;
    private String date;
    private BigDecimal value;
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
    
    @EntityUK(name = "objOid")
    public String getObjOid() {
        return objOid;
    }
    
    public void setObjOid(String objOid) {
        this.objOid = objOid;
    }
    
    @EntityUK(name = "resOid")
    public String getResOid() {
        return resOid;
    }
    
    public void setResOid(String resOid) {
        this.resOid = resOid;
    }
    
    @EntityUK(name = "date")
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public void setValue(BigDecimal value) {
        this.value = value;
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
