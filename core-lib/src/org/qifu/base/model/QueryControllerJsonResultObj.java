/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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

public class QueryControllerJsonResultObj<T> extends DefaultControllerJsonResultObj<T> {
	private static final long serialVersionUID = 3754125684960027639L;
	
	private int pageOfShowRow = PageOf.Rows[0];
	private int pageOfSelect = 1;
	private long pageOfCountSize = 0;
	private int pageOfSize = 1;
	
	public static <T> QueryControllerJsonResultObj<T> build() {
		QueryControllerJsonResultObj<T> obj = new QueryControllerJsonResultObj<T>();
		return obj;
	}

	public int getPageOfShowRow() {
		return pageOfShowRow;
	}

	public void setPageOfShowRow(int pageOfShowRow) {
		this.pageOfShowRow = pageOfShowRow;
	}

	public int getPageOfSelect() {
		return pageOfSelect;
	}

	public void setPageOfSelect(int pageOfSelect) {
		this.pageOfSelect = pageOfSelect;
	}

	public long getPageOfCountSize() {
		return pageOfCountSize;
	}

	public void setPageOfCountSize(long pageOfCountSize) {
		this.pageOfCountSize = pageOfCountSize;
	}

	public int getPageOfSize() {
		return pageOfSize;
	}

	public void setPageOfSize(int pageOfSize) {
		this.pageOfSize = pageOfSize;
	}
	
}
