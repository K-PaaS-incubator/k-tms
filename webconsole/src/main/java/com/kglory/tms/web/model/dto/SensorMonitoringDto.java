package com.kglory.tms.web.model.dto;

public class SensorMonitoringDto extends SearchDto {
	private String	sensorAliveTableName;
	private String	sensorSessionTableName;
	
	public String getSensorAliveTableName() {
		return sensorAliveTableName;
	}
	
	public void setSensorAliveTableName(String sensorAliveTableName) {
		this.sensorAliveTableName = sensorAliveTableName;
	}
	
	public String getSensorSessionTableName() {
		return sensorSessionTableName;
	}
	
	public void setSensorSessionTableName(String sensorSessionTableName) {
		this.sensorSessionTableName = sensorSessionTableName;
	}
	
	@Override
	public String toString() {
		return "SensorMonitoringDto [sensorAliveTableName=" + sensorAliveTableName + ", sensorSessionTableName="
				+ sensorSessionTableName + "]";
	}
	
}
