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
package org.qifu.hillfog.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.service.BaseService;
import org.qifu.hillfog.entity.HfPdca;
import org.qifu.hillfog.mapper.HfPdcaMapper;
import org.qifu.hillfog.service.IPdcaService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class PdcaServiceImpl extends BaseService<HfPdca, String> implements IPdcaService<HfPdca, String> {
	
	@Autowired
	HfPdcaMapper hfPdcaMapper;

	@Override
	protected IBaseMapper<HfPdca, String> getBaseMapper() {
		return this.hfPdcaMapper;
	}
	
	private String getCurrentMonthText() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.ENGLISH);
		String monthStr = sdf.format(cal.getTime());
		if (monthStr.length() > 3) {
			monthStr = monthStr.substring(0, 3);
		}
		return monthStr;
	}	
	
	@Override
	public String selectMaxPdcaNum(String head) throws ServiceException, Exception {
		if (StringUtils.isBlank(head)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		String pdcaNum = head + "-" + SimpleUtils.getStrYMD(SimpleUtils.IS_YEAR) + "-" + this.getCurrentMonthText();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pdcaNum", pdcaNum+"%");
		String currentMaxNum = this.hfPdcaMapper.selectMaxPdcaNum(paramMap);
		if (StringUtils.isBlank(currentMaxNum)) {
			return pdcaNum + "-001";
		}
		String tmp[] = currentMaxNum.split("-");
		if (tmp == null || tmp.length != 4) {
			throw new ServiceException(BaseSystemMessage.dataErrors());
		}
		String num = tmp[tmp.length-1];
		if (!NumberUtils.isCreatable(num)) {
			throw new ServiceException(BaseSystemMessage.dataErrors());
		}
		return pdcaNum + "-" + StringUtils.leftPad((Integer.parseInt(num)+1)+"", 3, "0");
	}

	@Override
	public List<String> selectPdcaOidListForOwnerBeRelated(String ownerUid) throws ServiceException, Exception {
		if (StringUtils.isBlank(ownerUid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ownerUid", ownerUid);
		return this.hfPdcaMapper.selectPdcaOidListForOwnerBeRelated(paramMap);
	}
	
}
