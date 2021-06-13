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

import java.util.ArrayList;
import java.util.List;

import org.qifu.hillfog.model.BalancedScorecardData;

public class BscVision extends BalancedScorecardData implements java.io.Serializable {
	private static final long serialVersionUID = -2797510285907105130L;
	
    private String oid;
    private String name;
    private String content;
    private String mission;
    private int row = 0;
    
    private List<BscPerspective> perspectives = new ArrayList<BscPerspective>();
    
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
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getMission() {
		return mission;
	}
	
	public void setMission(String mission) {
		this.mission = mission;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public List<BscPerspective> getPerspectives() {
		return perspectives;
	}

	public void setPerspectives(List<BscPerspective> perspectives) {
		this.perspectives = perspectives;
	}
	
}
