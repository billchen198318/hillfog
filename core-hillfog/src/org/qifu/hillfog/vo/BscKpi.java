/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.hillfog.vo;

import java.math.BigDecimal;
import java.util.List;

import org.qifu.base.exception.ServiceException;
import org.qifu.hillfog.entity.HfKpi;
import org.qifu.hillfog.model.BalancedScorecardData;
import org.qifu.hillfog.model.KpiBasicCode;
import org.qifu.hillfog.util.AggregationMethodUtils;
import org.qifu.hillfog.util.FormulaUtils;

public class BscKpi extends BalancedScorecardData implements java.io.Serializable {
	private static final long serialVersionUID = -2001910516656052720L;
	
	private HfKpi source = null;
	
    private String oid;
    private String id;
    private String name;
    private String description;
    /**
     * FIXME : 改為填入 HfSoOwnerKpis.cardWeight
     */
    private BigDecimal weight;
    private String unit;
    private String forId;
    private BigDecimal max;
    private BigDecimal target;
    private BigDecimal min;
    private String management;
    private String compareType;
    private String aggrId;
    private String dataType;
    private Integer quasiRange;
    
    private List<String> employees;
    private List<String> organizations;
    private List<DateRangeScore> dataRangeScores;
    
    public String getManagementName() {
    	return KpiBasicCode.getManagementMap(false).get(this.management);
    }    
    
    public String getCalculationName() {
    	try {
			return AggregationMethodUtils.findAggregationMethodById(this.aggrId).getName();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return this.aggrId;
    }
    
    public String getFormulaName() {
    	try {
			return FormulaUtils.getFormulaById(this.forId).getName();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return this.forId;
    }
    
	public HfKpi getSource() {
		return source;
	}

	public void setSource(HfKpi source) {
		this.source = source;
	}

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
	
	public String getAggrId() {
		return aggrId;
	}
	
	public void setAggrId(String aggrId) {
		this.aggrId = aggrId;
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

	public List<String> getEmployees() {
		return employees;
	}

	public void setEmployees(List<String> employees) {
		this.employees = employees;
	}

	public List<String> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}

	public List<DateRangeScore> getDataRangeScores() {
		return dataRangeScores;
	}

	public void setDataRangeScores(List<DateRangeScore> dataRangeScores) {
		this.dataRangeScores = dataRangeScores;
	}
    
}
