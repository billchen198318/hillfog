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

import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.entity.HfPdcaItem;

public class PdcaItems implements java.io.Serializable {
	private static final long serialVersionUID = -2844690491132182655L;
	
	private HfPdca main;
	
	private List<HfPdcaItem> planItemList = new ArrayList<HfPdcaItem>();
	
	private List<HfPdcaItem> doItemList = new ArrayList<HfPdcaItem>();
	
	private List<HfPdcaItem> checkItemList = new ArrayList<HfPdcaItem>();
	
	private List<HfPdcaItem> actItemList = new ArrayList<HfPdcaItem>();

	public PdcaItems(HfPdca pdca) {
		super();
		this.main = pdca;
	}

	public HfPdca getMain() {
		return main;
	}

	public void setMain(HfPdca main) {
		this.main = main;
	}

	public List<HfPdcaItem> getPlanItemList() {
		return planItemList;
	}

	public void setPlanItemList(List<HfPdcaItem> planItemList) {
		this.planItemList = planItemList;
	}

	public List<HfPdcaItem> getDoItemList() {
		return doItemList;
	}

	public void setDoItemList(List<HfPdcaItem> doItemList) {
		this.doItemList = doItemList;
	}

	public List<HfPdcaItem> getCheckItemList() {
		return checkItemList;
	}

	public void setCheckItemList(List<HfPdcaItem> checkItemList) {
		this.checkItemList = checkItemList;
	}
	
	public List<HfPdcaItem> getActItemList() {
		return actItemList;
	}
	
	public void setActItemList(List<HfPdcaItem> actItemList) {
		this.actItemList = actItemList;
	}
	
}
