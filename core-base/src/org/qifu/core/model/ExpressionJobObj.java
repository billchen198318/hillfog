/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.model;

import org.qifu.core.entity.TbSysExprJob;
import org.qifu.core.entity.TbSysExprJobLog;
import org.qifu.core.entity.TbSysExpression;

public class ExpressionJobObj implements java.io.Serializable {
	private static final long serialVersionUID = 7251297482415387237L;
	private TbSysExpression sysExpression;
	private TbSysExprJob sysExprJob;
	private TbSysExprJobLog sysExprJobLog;
	
	public ExpressionJobObj() {
		
	}
	
	public ExpressionJobObj(TbSysExpression sysExpression, TbSysExprJob sysExprJob, TbSysExprJobLog sysExprJobLog) {
		super();
		this.sysExpression = sysExpression;
		this.sysExprJob = sysExprJob;
		this.sysExprJobLog = sysExprJobLog;
	}

	public TbSysExpression getSysExpression() {
		return sysExpression;
	}

	public void setSysExpression(TbSysExpression sysExpression) {
		this.sysExpression = sysExpression;
	}

	public TbSysExprJob getSysExprJob() {
		return sysExprJob;
	}

	public void setSysExprJob(TbSysExprJob sysExprJob) {
		this.sysExprJob = sysExprJob;
	}

	public TbSysExprJobLog getSysExprJobLog() {
		return sysExprJobLog;
	}

	public void setSysExprJobLog(TbSysExprJobLog sysExprJobLog) {
		this.sysExprJobLog = sysExprJobLog;
	}
	
}
