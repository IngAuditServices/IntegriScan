package com.ingauditservices.integriscan.dtos;

public class IsolatedTableAnomaly extends ReferentialIntegrityAnomaly {

	public IsolatedTableAnomaly() {
		super();
	}

	public IsolatedTableAnomaly(String tableName) {
		super(tableName);
	}
}
