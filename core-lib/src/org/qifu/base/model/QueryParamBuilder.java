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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.Constants;
import org.qifu.util.OgnlContextDefaultMemberAccessBuildUtils;

import ognl.Ognl;
import ognl.OgnlException;

public class QueryParamBuilder {
	
	private Map<String, Object> paramMap = new HashMap<String, Object>();
	
	private SearchBody searchBody = null;
	
	public static QueryParamBuilder build(SearchBody searchBody) {
		QueryParamBuilder paramBuilder = new QueryParamBuilder();
		paramBuilder.searchBody = searchBody;
		return paramBuilder;
	}
	
	public static QueryParamBuilder build() {
		QueryParamBuilder paramBuilder = new QueryParamBuilder();
		return paramBuilder;
	}	
	
	private Object doExpression(String expression) {
		Object val = null;
		try {
			val = Ognl.getValue(expression, OgnlContextDefaultMemberAccessBuildUtils.newOgnlContext(), this.searchBody.getField());
		} catch (OgnlException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public QueryParamBuilder selectOption(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}		
		String value = this.searchBody.getField().get(paramName);		
		if (!StringUtils.isBlank(value) && !PleaseSelect.isAllOption(value)) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}	
	
	public QueryParamBuilder fullEquals(String paramName, String value) {
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder fullLink(String paramName, String value) {
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, "%"+value+"%");
		}
		return this;
	}
	
	public QueryParamBuilder beginningLike(String paramName, String value) {
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, "%"+value);
		}
		return this;		
	}
	
	public QueryParamBuilder endingLike(String paramName, String value) {
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, value+"%");
		}
		return this;		
	}	
	
	public QueryParamBuilder isNumber(String paramName, String value) {
		if (NumberUtils.isCreatable(value)) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder fullEqualsNotNull(String paramName, Object value) {
		if (null != value) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder fullEquals(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}		
		String value = this.searchBody.getField().get(paramName);		
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder fullLink(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}		
		String value = this.searchBody.getField().get(paramName);		
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, "%"+value+"%");
		}
		return this;
	}
	
	public QueryParamBuilder beginningLike(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}		
		String value = this.searchBody.getField().get(paramName);		
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, "%"+value);
		}
		return this;		
	}
	
	public QueryParamBuilder endingLike(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}		
		String value = this.searchBody.getField().get(paramName);		
		if (!StringUtils.isBlank(value)) {
			this.paramMap.put(paramName, value+"%");
		}
		return this;		
	}	
	
	public QueryParamBuilder isNumber(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}		
		String value = this.searchBody.getField().get(paramName);		
		if (NumberUtils.isCreatable(value)) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder fullEqualsNotNull(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}
		String value = this.searchBody.getField().get(paramName);
		if (null != value) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}	
	
	public QueryParamBuilder addField(String paramName, String expression) {
		Object obj = this.doExpression(expression);
		boolean test = false;
		if (obj != null && obj instanceof Boolean) {
			test = (Boolean) obj;
		}
		if (test) {
			String value = this.searchBody.getField().get(paramName);
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder addField(String paramName, boolean flag) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return this;
		}			
		String value = this.searchBody.getField().get(paramName);
		if (flag) {
			this.paramMap.put(paramName, value);
		}
		return this;
	}
	
	public QueryParamBuilder addFieldByExpression(String paramName, String expression) {
		Object obj = this.doExpression(expression);
		this.paramMap.put(paramName, obj);
		return this;
	}
	
	public Map<String, Object> value() {
		return this.paramMap;
	}
	
	public String asString(String paramName) {
		return (String) this.searchBody.getField().get(paramName);
	}
	
	public BigDecimal asDecimal(String paramName) {
		BigDecimal value = null;
		if ( this.searchBody.getField().get(paramName) != null ) {
			value = new BigDecimal( this.searchBody.getField().get(paramName) );
		}
		return value;
	}
	
	public long asLong(String paramName) {
		return NumberUtils.toLong((String) this.searchBody.getField().get(paramName), -1);
	}
	
	public int asInt(String paramName) {
		return NumberUtils.toInt((String) this.searchBody.getField().get(paramName), -1);
	}
	
	public String[] asDefaultSplitDelimiter(String paramName) {
		if (null == this.searchBody || null == this.searchBody.getField()) {
			return null;
		}
		return StringUtils.defaultString(this.searchBody.getField().get(paramName)).split(Constants.DEFAULT_SPLIT_DELIMITER);
	}
	
	public List<String> asDefaultSplitDelimiterList(String paramName) {
		String value[] = this.asDefaultSplitDelimiter(paramName);
		if (null == value) {
			return new ArrayList<String>();
		}
		return Arrays.asList(value);
	}
	
}
