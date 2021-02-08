/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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
package org.qifu.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.qifu.base.AppContext;
import org.qifu.base.model.PleaseSelect;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class DataUtils {
	
	public static Connection getConnection(DataSource dataSource) 
		throws CannotGetJdbcConnectionException, Exception {
		
		if (dataSource==null) {
			return null;
		}
		return DataSourceUtils.getConnection(dataSource);
	}
	
	public static Connection getConnection(String dataSourceId) {
		Connection conn = null;
		DataSource dataSource = (DataSource)AppContext.context.getBean(dataSourceId);
		try {
			conn = getConnection(dataSource);
		} catch (CannotGetJdbcConnectionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void doReleaseConnection(String dataSourceId, Connection connection) {
		if (null == connection) {
			return;
		}
		DataSource dataSource = (DataSource)AppContext.context.getBean(dataSourceId);
		try {
			DataSourceUtils.doReleaseConnection(connection, dataSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	public static PlatformTransactionManager getPlatformTransactionManager() {
//		return (PlatformTransactionManager)AppContext.getBean("transactionManager");
//	}
//
//	public static TransactionTemplate getTransactionTemplate() {
//		return (TransactionTemplate)AppContext.getBean("transactionTemplate");
//	}		
	
	public static String[] getTables(Connection connection) throws Exception {
		DatabaseMetaData metaData = connection.getMetaData();
		String[] types = {"TABLE"};
		ResultSet rs = metaData.getTables(null, null, "%", types);
		List<String> tables = new LinkedList<String>();
		while (rs.next()) {
			tables.add(rs.getString("TABLE_NAME"));
		}
		String[] names = new String[tables.size()];
		tables.toArray(names);		
		return names;
	}
	
	public static Map<String, Map<String, String>> getTableMetadata(Connection connection, String tableName) throws Exception {
		Map<String, Map<String, String>> tableMetadatas = new LinkedHashMap<String, Map<String, String>>();
		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
		while (resultSet.next()) {
			Map<String, String> metaDataMap = new LinkedHashMap<String, String>();
			String name = resultSet.getString("COLUMN_NAME");
			String type = resultSet.getString("TYPE_NAME");
			int size = resultSet.getInt("COLUMN_SIZE");
			metaDataMap.put("COLUMN_NAME", name);
			metaDataMap.put("TYPE_NAME", type);
			metaDataMap.put("COLUMN_SIZE", Integer.toString(size));
			tableMetadatas.put(name, metaDataMap);
		}
		return tableMetadatas;
	}
	
//	public static Map<String,  ClassMetadata> getClassMetadata(Session session) throws Exception {		
//		Map<String, ClassMetadata> classMetadataMap = session.getSessionFactory().getAllClassMetadata();		
//		return classMetadataMap;
//	}
	
	public static NamedParameterJdbcTemplate getManualJdbcTemplate(DataSource dataSource) throws Exception {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate;
	}
	
	public static NamedParameterJdbcTemplate getJdbcTemplate() throws Exception {
		return (NamedParameterJdbcTemplate)AppContext.getBean("namedParameterJdbcTemplate");
	}	
	
	public static NamedParameterJdbcTemplate getManualJdbcTemplate(
			Class<?> dataSourceClass, String url, String user, String password) throws Exception {
		return getManualJdbcTemplate( ManualDataSourceFactory.getDataSource(dataSourceClass, url, user, password) );
	}
	
//	public List<String> getEntityNameList(Session session) throws Exception {		
//		Map<String, ClassMetadata> classMetadataMap = getClassMetadata(session);
//		List<String> names = new LinkedList<String>();
//		for (Map.Entry<String, ClassMetadata> entry : classMetadataMap.entrySet()) {
//			names.add(entry.getValue().getEntityName());
//		}		
//		return names;
//	}
//	
//	public ClassMetadata getClassMetadataByEntityName(Session session, String name) throws Exception {
//		return session.getSessionFactory().getClassMetadata(name);
//	}
	
	/**
	 * 排除系統表
	 */
	public static String[] getTablesWithDoReleaseConnection(String dataSourceId) throws Exception {
		Connection connection = getConnection(dataSourceId);
		DatabaseMetaData metaData = connection.getMetaData();
		String[] types = {"TABLE"};
		ResultSet rs = metaData.getTables(null, null, "%", types);
		List<String> tables = new ArrayList<String>();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			if (tableName.startsWith("tb_") || tableName.startsWith("TB_") || tableName.startsWith("act_") || tableName.indexOf("qrtz_") > -1) {
				continue;
			}
			tables.add(tableName);
		}
		String[] names = new String[tables.size()];
		tables.toArray(names);		
		doReleaseConnection(dataSourceId, connection);
		return names;
	}
	
	public static Map<String, String> getTablesWithDoReleaseConnection(String dataSourceId, boolean pleaseSelect) throws Exception {
		String[] tables = getTablesWithDoReleaseConnection(dataSourceId);
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		for (int i = 0; tables != null && i < tables.length; i++) {
			dataMap.put(tables[i], tables[i]);
		}
		return dataMap;
	}
	
	public static Map<String, Map<String, String>> getTableMetadataWithDoReleaseConnection(String dataSourceId, String tableName) throws Exception {
		Connection connection = getConnection(dataSourceId);
		Map<String, Map<String, String>> tableMetadatas = new LinkedHashMap<String, Map<String, String>>();
		DatabaseMetaData metadata = connection.getMetaData();
		ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
		while (resultSet.next()) {
			Map<String, String> metaDataMap = new HashMap<String, String>();
			String name = resultSet.getString("COLUMN_NAME");
			String nametToLowerCase = name.toLowerCase();
			if (nametToLowerCase.equals("oid") || nametToLowerCase.equals("cuserid") || nametToLowerCase.equals("cdate")
					|| nametToLowerCase.equals("uuserid") || nametToLowerCase.equals("udate") ) { // 不顯示這5個欄位
				continue;
			}
			String type = resultSet.getString("TYPE_NAME");
			int size = resultSet.getInt("COLUMN_SIZE");
			metaDataMap.put("COLUMN_NAME", name);
			metaDataMap.put("TYPE_NAME", type);
			metaDataMap.put("COLUMN_SIZE", Integer.toString(size));
			tableMetadatas.put(name, metaDataMap);
		}
		doReleaseConnection(dataSourceId, connection);
		return tableMetadatas;
	}	
	
}
