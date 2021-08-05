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

import org.apache.commons.collections.CollectionUtils;
import org.qifu.hillfog.entity.HfEmployee;
import org.qifu.hillfog.entity.HfEmployeeHier;
import org.qifu.hillfog.entity.HfObjective;

public class EmployeeHierObjective implements java.io.Serializable {
	private static final long serialVersionUID = -2396342678213021313L;
	
	private HfEmployeeHier employeeHier;
	private HfEmployee employee;
	private List<HfObjective> objectives;
	
	public EmployeeHierObjective(HfEmployeeHier employeeHier, HfEmployee employee, List<HfObjective> objectives) {
		super();
		this.employeeHier = employeeHier;
		this.employee = employee;
		this.objectives = objectives;
	}

	public HfEmployeeHier getEmployeeHier() {
		return employeeHier;
	}

	public void setEmployeeHier(HfEmployeeHier employeeHier) {
		this.employeeHier = employeeHier;
	}

	public HfEmployee getEmployee() {
		return employee;
	}
	
	public void setEmployee(HfEmployee employee) {
		this.employee = employee;
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
		if (CollectionUtils.isEmpty(objectives)) {
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
