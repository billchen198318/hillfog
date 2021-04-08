package org.qifu.hillfog.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;
import org.qifu.util.SimpleUtils;

public class HfObjective implements java.io.Serializable {
	private static final long serialVersionUID = 1324236282306951288L;

    private String oid;
    private String name;
    private String startDate;
    private String endDate;
    private String description;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    // =====================================================================
    // 查詢顯示用
    private int objectiveSize = 0;
    private int initiativeSize = 0;
    
    // 顯示用 - 達成率
    private BigDecimal progressPercentage = BigDecimal.ZERO;
    
    public int getObjectiveSize() {
		return objectiveSize;
	}

	public void setObjectiveSize(int objectiveSize) {
		this.objectiveSize = objectiveSize;
	}

	public int getInitiativeSize() {
		return initiativeSize;
	}

	public void setInitiativeSize(int initiativeSize) {
		this.initiativeSize = initiativeSize;
	}    
	
	// 編輯頁面顯示用
	private List<HfKeyRes> keyResList = null;
    private List<HfInitiatives> initiativeList = null;
	
	public List<HfKeyRes> getKeyResList() {
		return keyResList;
	}
	
	public void setKeyResList(List<HfKeyRes> keyResList) {
		this.keyResList = keyResList;
	}
	
	public List<HfInitiatives> getInitiativeList() {
		return initiativeList;
	}
	
	public void setInitiativeList(List<HfInitiatives> initiativeList) {
		this.initiativeList = initiativeList;
	}    
	
    public String getStartDateShow() {
    	if (!SimpleUtils.isDate(this.startDate)) {
    		return "";
    	}
        return startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8);
    }	
    
    public String getEndDateShow() {
    	if (!SimpleUtils.isDate(this.endDate)) {
    		return "";
    	}
    	return endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8);
    }
    
	public BigDecimal getProgressPercentage() {
		return progressPercentage;
	}

	public void setProgressPercentage(BigDecimal progressPercentage) {
		this.progressPercentage = progressPercentage;
	}
	
    // =====================================================================

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
