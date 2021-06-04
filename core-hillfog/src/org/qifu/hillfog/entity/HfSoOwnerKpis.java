package org.qifu.hillfog.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfSoOwnerKpis implements java.io.Serializable {
	private static final long serialVersionUID = 1178962614553290055L;

    private String oid;
    private String soOid;
    private String kpiOid;
    private BigDecimal cardWeight;
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
    
    @EntityUK(name = "soOid")
    public String getSoOid() {
        return soOid;
    }
    
    public void setSoOid(String soOid) {
        this.soOid = soOid;
    }
    
    @EntityUK(name = "kpiOid")
    public String getKpiOid() {
        return kpiOid;
    }
    
    public void setKpiOid(String kpiOid) {
        this.kpiOid = kpiOid;
    }
    
    public BigDecimal getCardWeight() {
        return cardWeight;
    }
    
    public void setCardWeight(BigDecimal cardWeight) {
        this.cardWeight = cardWeight;
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
