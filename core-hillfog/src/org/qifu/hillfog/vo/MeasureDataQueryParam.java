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

import org.apache.commons.lang3.StringUtils;
import org.qifu.hillfog.model.MeasureDataCode;

public class MeasureDataQueryParam {
	
	private String accountId = MeasureDataCode.MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
	private String orgId = MeasureDataCode.MEASURE_DATA_EMPLOYEE_OR_ORGANIZATION_FULL;
	
	public MeasureDataQueryParam() {
		super();
	}

	public MeasureDataQueryParam(String accountId, String orgId) {
		super();
		if (!StringUtils.isBlank(accountId)) {
			this.accountId = accountId;
		}
		if (!StringUtils.isBlank(orgId)) {
			this.orgId = orgId;
		}
	}
	
	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getOrgId() {
		return orgId;
	}
	
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
