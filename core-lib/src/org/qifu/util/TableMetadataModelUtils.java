package org.qifu.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.qifu.base.model.TableMetadataModel;

public class TableMetadataModelUtils {
	
	public static List<TableMetadataModel> build(String dataSourceId, String tableName) {
		List<TableMetadataModel> tableMetadataModelList = new LinkedList<TableMetadataModel>();
		try {
			Map<String, Map<String, String>> tableMetadataMap = DataUtils.getTableMetadataWithDoReleaseConnection(dataSourceId, tableName);
			for (Map.Entry<String, Map<String, String>> entry : tableMetadataMap.entrySet()) {
				Map<String, String> metadataMap = entry.getValue();
				TableMetadataModel model = new TableMetadataModel(
						metadataMap.get("COLUMN_NAME"), 
						metadataMap.get("TYPE_NAME"), 
						NumberUtils.toInt(metadataMap.get("COLUMN_SIZE"), 0));
				tableMetadataModelList.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableMetadataModelList;
	}
	
}
