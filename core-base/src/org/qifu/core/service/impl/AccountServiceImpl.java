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
package org.qifu.core.service.impl;

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.SortType;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.mapper.TbAccountMapper;
import org.qifu.core.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class AccountServiceImpl extends BaseService<TbAccount, String> implements IAccountService<TbAccount, String> {
	
	@Autowired
	TbAccountMapper accountMapper;

	@Override
	protected IBaseMapper<TbAccount, String> getBaseMapper() {
		return this.accountMapper;
	}

	/**
	 * 下拉Select 要用
	 */
	@Override
	public Map<String, String> findForAllMap(boolean pleaseSelect) throws ServiceException, Exception {
		List<TbAccount> searchList = this.selectList("ACCOUNT", SortType.ASC).getValue();
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		if (searchList==null || searchList.size()<1) {
			return dataMap;
		}
		for (TbAccount account : searchList) {
			dataMap.put(account.getOid(), account.getAccount());
		}
		return dataMap;
	}
	
}
