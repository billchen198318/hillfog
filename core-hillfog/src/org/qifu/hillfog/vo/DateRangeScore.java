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
package org.qifu.hillfog.vo;

import java.math.BigDecimal;

public class DateRangeScore implements java.io.Serializable {
	private static final long serialVersionUID = 8715540156477843268L;
	
	private String date;
	private BigDecimal score;
	private BigDecimal target;
	private BigDecimal min;
	private String fontColor;
	private String bgColor;
	private String imgIcon;
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public BigDecimal getScore() {
		return score;
	}
	
	public void setScore(BigDecimal score) {
		this.score = score;
	}
	
	public BigDecimal getTarget() {
		return target;
	}
	
	public void setTarget(BigDecimal target) {
		this.target = target;
	}
	
	public BigDecimal getMin() {
		return min;
	}
	
	public void setMin(BigDecimal min) {
		this.min = min;
	}
	
	public String getFontColor() {
		return fontColor;
	}
	
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	
	public String getBgColor() {
		return bgColor;
	}
	
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}
	
	public String getImgIcon() {
		return imgIcon;
	}
	
	public void setImgIcon(String imgIcon) {
		this.imgIcon = imgIcon;
	}
	
}
