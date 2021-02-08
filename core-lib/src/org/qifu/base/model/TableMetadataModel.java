package org.qifu.base.model;

public class TableMetadataModel implements java.io.Serializable {
	private static final long serialVersionUID = -9149192240295226124L;
	
	private String columnName;
	private String typeName;
	private int columnSize;
	
	public TableMetadataModel() {
		
	}
	
	public TableMetadataModel(String columnName, String typeName, int columnSize) {
		super();
		this.columnName = columnName;
		this.typeName = typeName;
		this.columnSize = columnSize;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

}
