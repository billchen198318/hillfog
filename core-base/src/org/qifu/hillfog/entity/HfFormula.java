package org.qifu.hillfog.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class HfFormula implements java.io.Serializable {
	private static final long serialVersionUID = -16028570648600377L;
	
    private String oid;
    private String forId;
    private String name;
    private String type;
    private String returnMode;
    private String returnVar;
    private String expression;
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
    
    @EntityUK(name = "forId")
    public String getForId() {
        return forId;
    }
    
    public void setForId(String forId) {
        this.forId = forId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getReturnMode() {
        return returnMode;
    }
    
    public void setReturnMode(String returnMode) {
        this.returnMode = returnMode;
    }
    
    public String getReturnVar() {
        return returnVar;
    }
    
    public void setReturnVar(String returnVar) {
        this.returnVar = returnVar;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
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
