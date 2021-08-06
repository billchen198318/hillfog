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
package org.qifu.core.config;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.qifu.base.CoreAppConstants;
import org.qifu.base.interceptor.MDCInterceptor;
import org.qifu.core.directive.CoreUiDirectiveSimpleHash;
import org.qifu.core.interceptor.ControllerAuthorityCheckInterceptor;
import org.qifu.core.model.LocaleMessageTemplateMethodModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	FreeMarkerConfigurer freeMarkerConfigurer;
	
	@PostConstruct
	public void freeMarkerConfigurer() {
		freeMarkerConfigurer.getConfiguration().setSharedVariable("getText", new LocaleMessageTemplateMethodModel());
        freeMarkerConfigurer.getConfiguration().setSharedVariable("qifu", new CoreUiDirectiveSimpleHash(freeMarkerConfigurer.getConfiguration().getObjectWrapper()));
    }
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        .addResourceHandler( CoreAppConstants.WebConfig_resource )
        .addResourceLocations( CoreAppConstants.WebConfig_resourceLocations );
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
    }    
    
    @Bean 
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }     
    
    @Bean
    MDCInterceptor MDCInterceptor() {
    	return new MDCInterceptor();
    }
    
    @Bean
    ControllerAuthorityCheckInterceptor ControllerAuthorityCheckInterceptor() {
    	return new ControllerAuthorityCheckInterceptor();
    }
    
    /*
    @Bean
    UserBuilderInterceptor UserBuilderInterceptor() {
    	return new UserBuilderInterceptor();
    }
    */
    
    // ============================================================================
    // for messages
    // ============================================================================
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver lr = new CookieLocaleResolver();
        lr.setDefaultLocale(Locale.US);
        return lr;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
    	LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("_lang");
        return lci;
    }
    // ============================================================================
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(MDCInterceptor())
        	.addPathPatterns("/*", "/**")
        	.excludePathPatterns( CoreAppConstants.getWebConfiginterceptorExcludePathPatterns() );
        
        registry.addInterceptor(ControllerAuthorityCheckInterceptor())
        	.addPathPatterns("/*", "/**")
        	.excludePathPatterns( CoreAppConstants.getWebConfiginterceptorExcludePathPatterns() );
        
        // for messages
        registry.addInterceptor(localeChangeInterceptor());
        
        /*
        registry.addInterceptor(UserBuilderInterceptor())
        	.addPathPatterns("/*", "/**")
        	.excludePathPatterns( Constants.getWebConfiginterceptorExcludePathPatterns() );
        */  
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**")
    		.allowedOrigins("*")
    		.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
    		.allowCredentials(true)
    		.maxAge(3600)
    		.allowedHeaders("*");
    }
    
}
