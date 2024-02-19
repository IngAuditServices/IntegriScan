package com.ingauditservices.integriscan.dtos;

public class DisabledCheckConstraintsAnomaly extends ReferentialIntegrityAnomaly {
	private String constraintName;

	public DisabledCheckConstraintsAnomaly() {
		super();
	}

	public DisabledCheckConstraintsAnomaly(String tableName, String constraintName) {
		super(tableName);
		this.constraintName = constraintName;
	}

	public String getConstraintName() {
		return constraintName;
	}
}
