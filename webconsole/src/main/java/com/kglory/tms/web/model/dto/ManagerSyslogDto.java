package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class ManagerSyslogDto extends CommonBean {

	// 시간동기화
	private Integer nUseTimeSync;
	private String strTimeServerName;
	private Integer nTimeSyncPeriod;
	// syslog 연동
	private String strSyslogServerIp;
	private Integer nSyslogServerPort;
	private Integer nSyslogEventLog;
	private Integer nSyslogRawPacket;
	private Integer nSyslogManagerSystem;
	private Integer nSyslogSensorSystem;
	private Integer nSyslogAnomalyLog;
	private String strIpsSyslogServerIp;
	private Integer nIpsSyslogServerPort;
	private Integer nIpsSyslog;
	private String	strSensorSyslogServerIp;
	private List<String> strSensorName;
	
	public Integer getnUseTimeSync() {
		return nUseTimeSync;
	}
	public void setnUseTimeSync(Integer nUseTimeSync) {
		this.nUseTimeSync = nUseTimeSync;
	}
	public String getStrTimeServerName() {
		return strTimeServerName;
	}
	public void setStrTimeServerName(String strTimeServerName) {
		this.strTimeServerName = strTimeServerName;
	}
	public Integer getnTimeSyncPeriod() {
		return nTimeSyncPeriod;
	}
	public void setnTimeSyncPeriod(Integer nTimeSyncPeriod) {
		this.nTimeSyncPeriod = nTimeSyncPeriod;
	}
	
	public String getStrSyslogServerIp() {
		return strSyslogServerIp;
	}
	public void setStrSyslogServerIp(String strSyslogServerIp) {
		this.strSyslogServerIp = strSyslogServerIp;
	}
	public Integer getnSyslogServerPort() {
		return nSyslogServerPort;
	}
	public void setnSyslogServerPort(Integer nSyslogServerPort) {
		this.nSyslogServerPort = nSyslogServerPort;
	}
	public Integer getnSyslogEventLog() {
		return nSyslogEventLog;
	}
	public void setnSyslogEventLog(Integer nSyslogEventLog) {
		this.nSyslogEventLog = nSyslogEventLog;
	}
	public Integer getnSyslogRawPacket() {
		return nSyslogRawPacket;
	}
	public void setnSyslogRawPacket(Integer nSyslogRawPacket) {
		this.nSyslogRawPacket = nSyslogRawPacket;
	}
	public Integer getnSyslogManagerSystem() {
		return nSyslogManagerSystem;
	}
	public void setnSyslogManagerSystem(Integer nSyslogManagerSystem) {
		this.nSyslogManagerSystem = nSyslogManagerSystem;
	}
	public Integer getnSyslogSensorSystem() {
		return nSyslogSensorSystem;
	}
	public void setnSyslogSensorSystem(Integer nSyslogSensorSystem) {
		this.nSyslogSensorSystem = nSyslogSensorSystem;
	}
	public Integer getnSyslogAnomalyLog() {
		return nSyslogAnomalyLog;
	}
	public void setnSyslogAnomalyLog(Integer nSyslogAnomalyLog) {
		this.nSyslogAnomalyLog = nSyslogAnomalyLog;
	}
	public String getStrIpsSyslogServerIp() {
		return strIpsSyslogServerIp;
	}
	public void setStrIpsSyslogServerIp(String strIpsSyslogServerIp) {
		this.strIpsSyslogServerIp = strIpsSyslogServerIp;
	}
	public Integer getnIpsSyslogServerPort() {
		return nIpsSyslogServerPort;
	}
	public void setnIpsSyslogServerPort(Integer nIpsSyslogServerPort) {
		this.nIpsSyslogServerPort = nIpsSyslogServerPort;
	}
	public Integer getnIpsSyslog() {
		return nIpsSyslog;
	}
	public void setnIpsSyslog(Integer nIpsSyslog) {
		this.nIpsSyslog = nIpsSyslog;
	}
	
	public String getStrSensorSyslogServerIp() {
		return strSensorSyslogServerIp;
	}
	public void setStrSensorSyslogServerIp(String strSensorSyslogServerIp) {
		this.strSensorSyslogServerIp = strSensorSyslogServerIp;
	}
	
	public List<String> getStrSensorName() {
		return strSensorName;
	}
	public void setStrSensorName(List<String> strSensorName) {
		this.strSensorName = strSensorName;
	}
	@Override
	public String toString() {
		return "ManagerSyslogDto [nUseTimeSync=" + nUseTimeSync
				+ ", strTimeServerName=" + strTimeServerName
				+ ", nTimeSyncPeriod=" + nTimeSyncPeriod
				+ ", strSyslogServerIp=" + strSyslogServerIp
				+ ", nSyslogServerPort=" + nSyslogServerPort
				+ ", nSyslogEventLog=" + nSyslogEventLog
				+ ", nSyslogRawPacket=" + nSyslogRawPacket
				+ ", nSyslogManagerSystem=" + nSyslogManagerSystem
				+ ", nSyslogSensorSystem=" + nSyslogSensorSystem
				+ ", nSyslogAnomalyLog=" + nSyslogAnomalyLog
				+ ", strIpsSyslogServerIp=" + strIpsSyslogServerIp
				+ ", nIpsSyslogServerPort=" + nIpsSyslogServerPort
				+ ", nIpsSyslog=" + nIpsSyslog + ", strSensorSyslogServerIp="
				+ strSensorSyslogServerIp + ", strSensorName=" + strSensorName
				+ "]";
	}
	 
}
