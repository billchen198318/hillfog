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
package org.qifu.hillfog.model;

public class ScoreColor implements java.io.Serializable {
	private static final long serialVersionUID = 458591037725272032L;
	
	private String fontColor = "";
	private String backgroundColor = "";
	
	public String getFontColor() {
		return fontColor;
	}
	
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public String toString() {
		return "ScoreColor [fontColor=" + fontColor + ", backgroundColor=" + backgroundColor + "]";
	}
	
}
