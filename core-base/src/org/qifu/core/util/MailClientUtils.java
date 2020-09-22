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
package org.qifu.core.util;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.properties.SpringMailSessionConfigProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailClientUtils {
	
	private static JavaMailSender mailSender;
	
	private static SpringMailSessionConfigProperties springMailSessionConfigProperties;
	
	static {
		mailSender = AppContext.context.getBean(JavaMailSender.class);
		springMailSessionConfigProperties = AppContext.context.getBean(SpringMailSessionConfigProperties.class);
	}
	
	public static void send(
			String from, String to, 
			String subject, String text) throws MailException, Exception {
		send(from, to, null, null, null, null, subject, text);
	}		
	
	public static void send(
			String from, String to, 
			String cc[], 
			String subject, String text) throws MailException, Exception {
		send(from, to, cc, null, null, null, subject, text);
	}	
	
	public static void send(
			String from, String to, 
			String cc[], String bcc[], 
			String subject, String text) throws MailException, Exception {
		send(from, to, cc, bcc, null, null, subject, text);
	}
	
	public static void send(
			String from, String to, 
			String cc, String bcc, 
			String subject, String text) throws MailException, Exception {
		
		String mailCc[] = null;
		String mailBcc[] = null;
		if (!StringUtils.isBlank(cc)) {
			mailCc = cc.split(Constants.DEFAULT_SPLIT_DELIMITER);
		}
		if (!StringUtils.isBlank(bcc)) {
			mailBcc = bcc.split(Constants.DEFAULT_SPLIT_DELIMITER);
		}
		send(from, to, mailCc, mailBcc, subject, text);
	}
	
	public static void send(
			String from, String to, 
			String cc[], String bcc[], 
			String fileNames[], File files[],
			String subject, String text) throws MailException, Exception {
		
		if (mailSender==null) {
			throw new Exception("null mailSender!");
		}
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			throw new Exception("from and to is required!");
		}
		if (fileNames!=null && files!=null) {
			if (fileNames.length != files.length) {
				throw new Exception("File parameter error!");
			}
		}
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, Constants.BASE_ENCODING);
		message.getSession().setDebug( springMailSessionConfigProperties.enableDebug() );
		helper.setFrom(from);
		helper.setTo( to.endsWith(";") ? to.substring(0, to.length()-1) : to );
		helper.setSubject(subject);
		helper.setText(text, true);
		if (null!=cc && cc.length>0) {
			helper.setCc(cc);
		}
		if (null!=bcc && bcc.length>0) {
			helper.setBcc(bcc);
		}
		for (int i=0; fileNames!=null && i<fileNames.length; i++) {
			helper.addAttachment(fileNames[i], files[i]);
		}
		mailSender.send(message);
	}
	
}
