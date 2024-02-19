package com.ingauditservices.integriscan.dtos;

public abstract class ReferentialIntegrityAnomaly {
	private String tableName;

	public ReferentialIntegrityAnomaly() {
		super();
	}
	
	public ReferentialIntegrityAnomaly(String tableName) {
		super();
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}	
}
