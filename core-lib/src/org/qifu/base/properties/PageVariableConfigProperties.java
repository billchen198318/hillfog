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
package org.qifu.base.properties;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:appConfig.properties")
@ConfigurationProperties(prefix = "page")
public class PageVariableConfigProperties {
	
	private String jqXhrType;
	
	private String jqXhrTimeout;
	
	private String jqXhrCache;
	
	private String jqXhrAsync;
	
	private String maxUploadSize;

	public String getJqXhrType() {
		return jqXhrType;
	}

	public void setJqXhrType(String jqXhrType) {
		this.jqXhrType = jqXhrType;
	}

	public String getJqXhrTimeout() {
		return jqXhrTimeout;
	}

	public void setJqXhrTimeout(String jqXhrTimeout) {
		this.jqXhrTimeout = jqXhrTimeout;
	}

	public String getJqXhrCache() {
		return jqXhrCache;
	}

	public void setJqXhrCache(String jqXhrCache) {
		this.jqXhrCache = jqXhrCache;
	}

	public String getJqXhrAsync() {
		return jqXhrAsync;
	}

	public void setJqXhrAsync(String jqXhrAsync) {
		this.jqXhrAsync = jqXhrAsync;
	}

	public String getMaxUploadSize() {
		if (NumberUtils.toInt(this.maxUploadSize, 0) < 1048576) {
			return "1048576";
		}
		return maxUploadSize;
	}

	public void setMaxUploadSize(String maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}
	
}
