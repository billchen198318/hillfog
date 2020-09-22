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

import java.util.Map;

import org.qifu.base.exception.ControllerException;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;

import ognl.Ognl;
import ognl.OgnlException;

public class CheckControllerFieldHandler<T> {
	private DefaultControllerJsonResultObj<T> result = null;
	private StringBuilder msg = new StringBuilder();
	
	public static <T> CheckControllerFieldHandler<T> build(DefaultControllerJsonResultObj<T> result) {
		CheckControllerFieldHandler<T> handler = new CheckControllerFieldHandler<T>();
		handler.setResult(result);
		return handler;
	}
	
	public DefaultControllerJsonResultObj<T> getResult() {
		return result;
	}

	public void setResult(DefaultControllerJsonResultObj<T> result) {
		this.result = result;
	}
	
	public String getCheckFieldsMessage() {
		msg.setLength(0);
		for (Map.Entry<String, String> entry : this.result.getCheckFields().entrySet()) {
			msg.append( entry.getValue() ).append("\n");
		}
		return msg.toString();
	}
	
	public CheckControllerFieldHandler<T> testField(String id, boolean checkStatus, String message) {
		if (this.result.getCheckFields().get(id) != null) {
			return this;
		}
		if (checkStatus) {
			this.result.getCheckFields().put(id, message);
		}
		return this;
	}
	
	public CheckControllerFieldHandler<T> testField(String id, Object paramObj, String expression, String message) {
		if (this.result.getCheckFields().get(id) != null) {
			return this;
		}
		try {
			Object val = Ognl.getValue(expression, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), paramObj);
			if ( val instanceof Boolean && (Boolean) val ) {
				this.result.getCheckFields().put(id, message);
			}
		} catch (OgnlException e) {
			e.printStackTrace();
		}
		return this;
	}	
	
	public void throwMessage() throws ControllerException {
		if (this.getCheckFieldsMessage().length() > 0) {
			throw new ControllerException(this.msg.toString());
		}
	}
	
	public void throwMessage(String id, String message) throws ControllerException {
		this.result.getCheckFields().put(id, message);
		this.throwMessage();
	}
	
}
