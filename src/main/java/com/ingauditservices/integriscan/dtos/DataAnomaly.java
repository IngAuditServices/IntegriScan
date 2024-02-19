package com.ingauditservices.integriscan.dtos;

public class DataAnomaly extends ReferentialIntegrityAnomaly {
	private String constraintName;
	private String anomalyLocation;
	
	public DataAnomaly() {
		super();
	}

	public DataAnomaly(String tableName, String constraintName, String anomalyLocation) {
		super(tableName);
		this.constraintName = constraintName;
		this.anomalyLocation = anomalyLocation;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public String getAnomalyLocation() {
		return anomalyLocation;
	}
}
