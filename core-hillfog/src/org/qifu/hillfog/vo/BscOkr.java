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

import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.model.BalancedScorecardData;

public class BscOkr extends BalancedScorecardData implements java.io.Serializable {
	private static final long serialVersionUID = -7056574337645999799L;
	
	private HfObjective source = null;
	
    private String oid;
    private String name;
    private String startDate;
    private String endDate;
    private String description;
    private BigDecimal progressPercentage = BigDecimal.ZERO;
    
	public HfObjective getSource() {
		return source;
	}

	public void setSource(HfObjective source) {
		this.source = source;
	}

	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
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
	
	public BigDecimal getProgressPercentage() {
		return progressPercentage;
	}
	
	public void setProgressPercentage(BigDecimal progressPercentage) {
		this.setScore(progressPercentage);
		this.progressPercentage = progressPercentage;
	}
    
}
