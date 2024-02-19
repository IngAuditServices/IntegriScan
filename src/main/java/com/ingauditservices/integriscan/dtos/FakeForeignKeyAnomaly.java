package com.ingauditservices.integriscan.dtos;

public class FakeForeignKeyAnomaly extends ReferentialIntegrityAnomaly {
	private String columnName;

	public FakeForeignKeyAnomaly() {
		super();
	}

	public FakeForeignKeyAnomaly(String tableName, String columnName) {
		super(tableName);
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}
}
