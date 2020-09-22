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
package org.qifu.base.model;

import java.util.HashMap;
import java.util.Map;

public class SearchBody implements java.io.Serializable {
	private static final long serialVersionUID = -8184870209865241457L;
	
	private Map<String, String> field = null;
	
	private PageOf pageOf = null;
	
	public SearchBody() {
		this.pageOf = new PageOf();
		this.field = new HashMap<String, String>();
	}
	
	public SearchBody(Map<String, String> field) {
		this.field = field;
	}
	
	public SearchBody(PageOf pageOf, Map<String, String> field) {
		this.pageOf = pageOf;
		this.field = field;
	}	
	
	public Map<String, String> getField() {
		return field;
	}

	public void setField(Map<String, String> field) {
		this.field = field;
	}

	public PageOf getPageOf() {
		return pageOf;
	}

	public void setPageOf(PageOf pageOf) {
		this.pageOf = pageOf;
	}
	
}
