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
package org.qifu.hillfog.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.qifu.hillfog.entity.HfObjective;
import org.qifu.hillfog.entity.HfOrgDept;

public class OrganizationObjective implements java.io.Serializable {
	private static final long serialVersionUID = -281384845630825128L;
	
	private HfOrgDept orgDept;
	private List<HfObjective> objectives;
	
	public OrganizationObjective(HfOrgDept orgDept, List<HfObjective> objectives) {
		super();
		this.orgDept = orgDept;
		this.objectives = objectives;
	}
	
	public HfOrgDept getOrgDept() {
		return orgDept;
	}
	
	public void setOrgDept(HfOrgDept orgDept) {
		this.orgDept = orgDept;
	}
	
	public List<HfObjective> getObjectives() {
		return objectives;
	}
	
	public void setObjectives(List<HfObjective> objectives) {
		this.objectives = objectives;
	}
	
	public BigDecimal getTotalProgressPercentage() {
		return this.totalProgressPercentage();
	}
	
	public BigDecimal totalProgressPercentage() {
		if (this.objectives == null || this.objectives.size() < 1) {
			return BigDecimal.ZERO;
		}
		if (this.objectives.size() == 1) {
			return this.objectives.get(0).getProgressPercentage();
		}
		BigDecimal sum = BigDecimal.ZERO;
		for (HfObjective o : this.objectives) {
			sum = sum.add( o.getProgressPercentage() );
		}
		if (sum.floatValue() == 0.0f) {
			return sum;
		}
		return sum.divide(new BigDecimal(this.objectives.size()), 2, RoundingMode.HALF_UP);
	}	
	
}
