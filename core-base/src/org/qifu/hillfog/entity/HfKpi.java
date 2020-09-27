package org.qifu.hillfog.entity;

import java.math.BigDecimal;
import java.util.Date;

public class HfKpi implements java.io.Serializable {
	private static final long serialVersionUID = -5876998216153568487L;
	
    private String oid;
    private String id;
    private String name;
    private String description;
    private BigDecimal weight;
    private String unit;
    private String forId;
    private BigDecimal max;
    private BigDecimal target;
    private BigDecimal min;
    private String management;
    private String compareType;
    private String cal;
    private String dataType;
    private Integer quasiRange;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    public String getOid() {
        return oid;
    }
    
    public void setOid(String oid) {
        this.oid = oid;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public BigDecimal getWeight() {
        return weight;
    }
    
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getForId() {
        return forId;
    }
    
    public void setForId(String forId) {
        this.forId = forId;
    }
    
    public BigDecimal getMax() {
        return max;
    }
    
    public void setMax(BigDecimal max) {
        this.max = max;
    }
    
    public BigDecimal getTarget() {
        return target;
    }
    
    public void setTarget(BigDecimal target) {
        this.target = target;
    }
    
    public BigDecimal getMin() {
        return min;
    }
    
    public void setMin(BigDecimal min) {
        this.min = min;
    }
    
    public String getManagement() {
        return management;
    }
    
    public void setManagement(String management) {
        this.management = management;
    }
    
    public String getCompareType() {
        return compareType;
    }
    
    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }
    
    public String getCal() {
        return cal;
    }
    
    public void setCal(String cal) {
        this.cal = cal;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public Integer getQuasiRange() {
        return quasiRange;
    }
    
    public void setQuasiRange(Integer quasiRange) {
        this.quasiRange = quasiRange;
    }
    
    public String getCuserid() {
        return cuserid;
    }
    
    public void setCuserid(String cuserid) {
        this.cuserid = cuserid;
    }
    
    public Date getCdate() {
        return cdate;
    }
    
    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
    
    public String getUuserid() {
        return uuserid;
    }
    
    public void setUuserid(String uuserid) {
        this.uuserid = uuserid;
    }
    
    public Date getUdate() {
        return udate;
    }
    
    public void setUdate(Date udate) {
        this.udate = udate;
    }
    
}
