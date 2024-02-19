package com.ingauditservices.integriscan.dtos;

public class DisabledTriggerAnomaly extends ReferentialIntegrityAnomaly {
	private String triggerName;

	public DisabledTriggerAnomaly() {
		super();
	}
	
	public DisabledTriggerAnomaly(String tableName, String triggerName) {
		super(tableName);
		this.triggerName = triggerName;
	}

	public String getTriggerName() {
		return triggerName;
	}
}
