package org.qifu.hillfog.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfScColor implements java.io.Serializable {
	private static final long serialVersionUID = -902319920448823415L;
	
    private String oid;
    private String scOid;
    private String type;
    private Integer range1;
    private Integer range2;
    private String fontColor;
    private String bgColor;
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
    
    public String getScOid() {
        return scOid;
    }
    
    public void setScOid(String scOid) {
        this.scOid = scOid;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getRange1() {
        return range1;
    }
    
    public void setRange1(Integer range1) {
        this.range1 = range1;
    }
    
    public Integer getRange2() {
        return range2;
    }
    
    public void setRange2(Integer range2) {
        this.range2 = range2;
    }
    
    public String getFontColor() {
        return fontColor;
    }
    
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
    
    public String getBgColor() {
        return bgColor;
    }
    
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
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
