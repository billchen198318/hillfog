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
package org.qifu.hillfog.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.hillfog.entity.HfScColor;
import org.qifu.hillfog.model.ScoreColor;
import org.qifu.hillfog.service.IScColorService;
import org.qifu.util.SimpleUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ScoreColorUtils {
	private static final String _CONFIG = "org/qifu/hillfog/util/ScoreColorUtils.json";
	
	private static Map<String, Object> srcMap = null;
	
	private static String _srcDatas = " { } ";
	
	private static IScColorService<HfScColor, String> scColorService;
	
	static {
		try {
			InputStream is = ScoreColorUtils.class.getClassLoader().getResource( _CONFIG ).openStream();
			_srcDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
			is = null;
			srcMap = loadDatas();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==srcMap) {
				srcMap = new HashMap<String, Object>();
			}
		}
	}
	
	public static Map<String, Object> getSrcMap() {
		return srcMap;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> loadDatas() {
		Map<String, Object> datas = null;
		try {
			datas = (Map<String, Object>)new ObjectMapper().readValue( _srcDatas, LinkedHashMap.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}	
	
	public static ScoreColor getUnknown() {
		ScoreColor sc = new ScoreColor();
		if (null == srcMap || srcMap.get("unknown") == null) {
			return sc;
		}
		String val[] = String.valueOf(srcMap.get("unknown")).split(Constants.DEFAULT_SPLIT_DELIMITER);
		sc.setBackgroundColor( StringUtils.deleteWhitespace(val[0]) );
		sc.setFontColor( StringUtils.deleteWhitespace(val[1]) );
		return sc;
	}
	
	public static ScoreColor get(BigDecimal score) {
		ScoreColor sc = getUnknown();
		if (null == score) {
			return sc;
		}
		if (null == srcMap || srcMap.get("range") == null) {
			return sc;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> scoreMap = (Map<String, String>) srcMap.get("range");
		if (scoreMap == null) {
			return sc;
		}
		for (Map.Entry<String, String> entry : scoreMap.entrySet()) {
			String scoreVal[] = entry.getKey().split(Constants.DEFAULT_SPLIT_DELIMITER);
			String colorVal[] = entry.getValue().split(Constants.DEFAULT_SPLIT_DELIMITER);
			if (scoreVal.length != 2 || colorVal.length != 2) {
				continue;
			}
			if ( SimpleUtils.isBetween(score, new BigDecimal(StringUtils.deleteWhitespace(scoreVal[0])), new BigDecimal(StringUtils.deleteWhitespace(scoreVal[1]))) ) {
				sc.setBackgroundColor( StringUtils.deleteWhitespace(colorVal[0]) );
				sc.setFontColor( StringUtils.deleteWhitespace(colorVal[1]) );				
			}
		}
		return sc;
	}
	
	public static ScoreColor get(String scorecardOid, BigDecimal score) {
		ScoreColor sc = getUnknown();
		if (null == score) {
			return sc;
		}
		if (StringUtils.isBlank(scorecardOid) || null == score) {
			return sc;
		}
		if (null == scColorService) {
			try {
				scColorService = (IScColorService<HfScColor, String>) AppContext.getBean(IScColorService.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (null == scColorService) {
			return sc;
		}
		Integer scoreVal = (int) Math.floor( score.floatValue() );
		HfScColor defaultColor = null;
		HfScColor currColor = null;
		try {
			defaultColor = scColorService.findByDefault(scorecardOid);
			currColor = scColorService.findByScore(scorecardOid, scoreVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != currColor) {
			sc = new ScoreColor();
			sc.setBackgroundColor( currColor.getBgColor() );
			sc.setFontColor( currColor.getFontColor() );
			return sc;
		}
		if (currColor == null && defaultColor != null) {
			sc = new ScoreColor();
			sc.setBackgroundColor( defaultColor.getBgColor() );
			sc.setFontColor( defaultColor.getFontColor() );
			return sc;			
		}
		return sc;
	}
	
}
