package org.qifu.hillfog.entity;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;
import org.qifu.base.model.YesNo;
import org.qifu.util.SimpleUtils;

public class HfPdca implements java.io.Serializable {
	private static final long serialVersionUID = -7009235858039916707L;
	
    private String oid;
    private String name;
    private String mstType;
    private String mstOid;
    private String kpiFrequency;
    private String kpiMeasureDate1;
    private String kpiMeasureDate2;
    private String startDate;
    private String endDate;
    private String confirmUid;
    private Date confirmDate;
    private String description;
    private String pdcaNum;
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
	
	public String getConfirm() { // Query gird 顯示用
		if (!StringUtils.isBlank(this.confirmUid)) {
			return YesNo.YES;
		}
		return YesNo.NO;
	}
	
	public String getStartEndDateShow() { // Query gird 顯示用
		return getStartDateShow() + " - " + getEndDateShow();
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
    
    @EntityUK(name = "name")
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @EntityUK(name = "mstType")
    public String getMstType() {
        return mstType;
    }
    
    public void setMstType(String mstType) {
        this.mstType = mstType;
    }
    
    @EntityUK(name = "mstOid")
    public String getMstOid() {
        return mstOid;
    }
    
    public void setMstOid(String mstOid) {
        this.mstOid = mstOid;
    }
    
    public String getKpiFrequency() {
        return kpiFrequency;
    }
    
    public void setKpiFrequency(String kpiFrequency) {
        this.kpiFrequency = kpiFrequency;
    }
    
    public String getKpiMeasureDate1() {
        return kpiMeasureDate1;
    }
    
    public void setKpiMeasureDate1(String kpiMeasureDate1) {
        this.kpiMeasureDate1 = kpiMeasureDate1;
    }
    
    public String getKpiMeasureDate2() {
        return kpiMeasureDate2;
    }
    
    public void setKpiMeasureDate2(String kpiMeasureDate2) {
        this.kpiMeasureDate2 = kpiMeasureDate2;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPdcaNum() {
        return pdcaNum;
    }
    
    public void setPdcaNum(String pdcaNum) {
        this.pdcaNum = pdcaNum;
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
