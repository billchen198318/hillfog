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
package org.qifu.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.base.model.BaseUserInfo;
import org.qifu.base.model.YesNo;
import org.qifu.base.model.ZeroKeyProvide;
import org.qifu.base.util.UserLocalUtils;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.entity.TbUserRole;
import org.qifu.core.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
	
	private static List<TbUserRole> backgroundRoleList = new ArrayList<TbUserRole>(); 
	
	private static User backgroundUser = null;
	
	static {
		TbUserRole backgroundUserRole1 = new TbUserRole();
		backgroundUserRole1.setOid(ZeroKeyProvide.OID_KEY);
		backgroundUserRole1.setAccount(Constants.SYSTEM_BACKGROUND_USER);
		backgroundUserRole1.setRole(Constants.SUPER_ROLE_ADMIN);
		backgroundUserRole1.setDescription("");
		TbUserRole backgroundUserRole2 = new TbUserRole();
		backgroundUserRole2.setOid("1");
		backgroundUserRole2.setAccount(Constants.SYSTEM_BACKGROUND_USER);
		backgroundUserRole2.setRole(Constants.SUPER_ROLE_ALL);
		backgroundUserRole2.setDescription("");		
		backgroundRoleList.add(backgroundUserRole1);
		backgroundRoleList.add(backgroundUserRole2);
		backgroundUser = new User(ZeroKeyProvide.OID_KEY, Constants.SYSTEM_BACKGROUND_USER, "", YesNo.YES, backgroundRoleList);
	}
	
	public static BaseUserInfo setUserInfoForUserLocalUtils() {
		User user = getCurrentUser();
		if (user == null) {
			return null;
		}
		return setUserInfoForUserLocalUtils(user.getUsername());
	}
	
	public static BaseUserInfo setUserInfoForUserLocalUtils(String accountId) {
		BaseUserInfo userInfo = new BaseUserInfo();
		userInfo.setUserId(accountId);
		UserLocalUtils.setUserInfo(userInfo);
		return userInfo;
	}	
	
	public static BaseUserInfo setUserInfoForUserLocalUtilsBackgroundMode() {
		return setUserInfoForUserLocalUtils( Constants.SYSTEM_BACKGROUND_USER );
	}		
	
	public static void removeForUserLocalUtils() {
		UserLocalUtils.remove();
	}
	
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() != null && (auth.getPrincipal() instanceof User)) {
			return (User) auth.getPrincipal();
		}
		if ( UserLocalUtils.getUserInfo() != null && Constants.SYSTEM_BACKGROUND_USER.equals(UserLocalUtils.getUserInfo().getUserId()) ) {
			if (backgroundUser != null) {
				return backgroundUser;
			}
			return new User(ZeroKeyProvide.OID_KEY, Constants.SYSTEM_BACKGROUND_USER, "", YesNo.YES, backgroundRoleList);
		}
		return null;
	}
	
	public static boolean isAdmin() {
		User user = getCurrentUser();
		boolean isAdm = false;
		if (user != null && user.getRoles() != null) {
			for (int i = 0; i < user.getRoles().size() && !isAdm; i++) {
				if (Constants.SUPER_ROLE_ADMIN.equals(user.getRoles().get(i).getRole()) || Constants.SUPER_ROLE_ALL.equals(user.getRoles().get(i).getRole())) {
					isAdm = true;
				}
			}
		}
		return isAdm;
	}
	
	public static boolean hasRole(String roleId) {
		if (isAdmin()) {
			return true;
		}
		if (StringUtils.isBlank(roleId)) {
			return false;
		}
		User user = getCurrentUser();
		boolean hasRole = false;
		if (user != null && user.getRoles() != null) {
			for (int i = 0; i < user.getRoles().size() && !hasRole; i++) {
				if (roleId.equals(user.getRoles().get(i).getRole())) {
					hasRole = true;
				}
			}
		}
		return hasRole;		
	}
	
	public static boolean isPermitted(String perm) {
		if (isAdmin()) {
			return true;
		}
		if (StringUtils.isBlank(perm)) {
			return false;
		}
		User user = getCurrentUser();
		boolean isPrem = false;
		if (user != null && user.getRoles() != null) {
			for (int i = 0; i < user.getRoles().size() && !isPrem; i++) {
				TbUserRole userRole = user.getRoles().get(i);
				for (int j = 0; userRole.getRolePermission() != null && j < userRole.getRolePermission().size(); j++) {
					TbRolePermission rolePerm = userRole.getRolePermission().get(j);
					if (perm.equals(rolePerm.getPermission())) {
						isPrem = true;
					}
				}
			}
		}
		return isPrem;
	}
	
}
