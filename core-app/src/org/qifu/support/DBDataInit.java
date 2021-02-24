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
package org.qifu.support;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;
import org.qifu.util.DataUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DBDataInit {
	
	private static final String RES_HEAD = "";
	
	private static void process(Connection conn) throws SQLException, Exception {
		String driverName = conn.getMetaData().getDriverName();
		if (driverName.indexOf("H2") > -1) {
			processH2(conn);
			return;
		}
	}
	
	private static String loadSqlResource(String fileRes) throws Exception {
		String sqlStr = "";
		InputStream is = null;
		try {
			is = DBDataInit.class.getClassLoader().getResource( fileRes ).openStream();
			sqlStr = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is = null;
		}		
		return sqlStr;
	}
	
	private static void doSqlCommand(Connection conn, String sqlStr) throws SQLException, Exception {
		String[] tables = DataUtils.getTables(conn);
		boolean isFound = false;
		for (int i = 0 ; tables != null && i < tables.length; i++) {
			if (tables[i].toLowerCase().equals("tb_sys_prog")) {
				isFound = true;
			}
		}
		Statement statement = null;
		if (!isFound) {
			statement = conn.createStatement();
			statement.execute( sqlStr );
		}
		
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tables = null;
		statement = null;			
	}
	
	private static void processH2(Connection conn) throws SQLException, Exception {
		String initScriptResJson = loadSqlResource("org/qifu/support/DBDataInit.json");
		Map<String, String> jsonData = (Map<String, String>) new ObjectMapper().readValue( initScriptResJson, LinkedHashMap.class );
		String sqlStr = loadSqlResource(RES_HEAD + jsonData.get("initScript"));
		if (StringUtils.isBlank(sqlStr)) {
			return;
		}	
		doSqlCommand(conn, sqlStr);
	}
	
	public static void create(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			process(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
