package org.qifu.hillfog.entity;

import java.util.Date;
import java.util.List;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;
import org.qifu.util.SimpleUtils;

public class HfPdcaItem implements java.io.Serializable {
	private static final long serialVersionUID = 6854983635831542061L;

	private String oid;
    private String pdcaOid;
    private String type;
    private String parentOid;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    private List<String> ownerNameList; // PDCA報表顯示用
    
	public List<String> getOwnerNameList() {
		return ownerNameList;
	}

	public void setOwnerNameList(List<String> ownerNameList) {
		this.ownerNameList = ownerNameList;
	}    
    
    public String getStartDateShow() {
        return SimpleUtils.getStrYMD(this.startDate, "-");
    }
    
    public String getEndDateShow() {
        return SimpleUtils.getStrYMD(this.endDate, "-");
    }
    
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getParentOid() {
        return parentOid;
    }
    
    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }
    
    @EntityUK(name = "name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
