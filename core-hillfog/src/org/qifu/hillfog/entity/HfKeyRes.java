package org.qifu.hillfog.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfKeyRes implements java.io.Serializable {
	private static final long serialVersionUID = 6832173187697145168L;
	
	private String oid;
    private String objOid;
    private String name;
    private BigDecimal target;
    private String gpType;
    private String opTarget;
    private String description;
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
    
    @EntityUK(name = "name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getTarget() {
        return target;
    }
    
    public void setTarget(BigDecimal target) {
        this.target = target;
    }
    
    public String getGpType() {
        return gpType;
    }
    
    public void setGpType(String gpType) {
        this.gpType = gpType;
    }
    
    public String getOpTarget() {
        return opTarget;
    }
    
    public void setOpTarget(String opTarget) {
        this.opTarget = opTarget;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
