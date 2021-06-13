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
import java.util.ArrayList;
import java.util.List;

import org.qifu.hillfog.model.BalancedScorecardData;

public class BscStrategyObjective extends BalancedScorecardData implements java.io.Serializable {
	private static final long serialVersionUID = 8613610164652782683L;
	
	private String oid;
    private String perOid;
    private String name;
    private BigDecimal weight;
    private int row = 0;
    
    private List<BscKpi> kpis = new ArrayList<BscKpi>();
    private List<BscOkr> okrs = new ArrayList<BscOkr>();
    
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getPerOid() {
		return perOid;
	}
	
	public void setPerOid(String perOid) {
		this.perOid = perOid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BigDecimal getWeight() {
		return weight;
	}
	
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public List<BscKpi> getKpis() {
		return kpis;
	}

	public void setKpis(List<BscKpi> kpis) {
		this.kpis = kpis;
	}

	public List<BscOkr> getOkrs() {
		return okrs;
	}

	public void setOkrs(List<BscOkr> okrs) {
		this.okrs = okrs;
	}
	
}
