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
package org.qifu.core.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qifu.base.Constants;
import org.qifu.base.CoreAppConstants;
import org.qifu.base.model.YesNo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class BaseLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public BaseLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String xhrHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xhrHeader)) {
        	response.setCharacterEncoding( Constants.BASE_ENCODING );
        	response.setContentType("application/json");
        	response.getWriter().write(Constants.NO_AUTHZ_JSON_DATA);
        	return;
        }
        if (YesNo.YES.equals(request.getParameter(Constants.QIFU_PAGE_IN_TAB_IFRAME))) {
        	response.sendRedirect(CoreAppConstants.SYS_PAGE_TAB_LOGIN_AGAIN);
        	return;
        }
        super.commence(request, response, authException);
    }
    
}
