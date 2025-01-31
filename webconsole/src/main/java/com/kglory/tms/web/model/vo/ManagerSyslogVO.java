package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.ManagerSyslogDto;

public class ManagerSyslogVO extends CommonBean implements Serializable {
	private static final long serialVersionUID = -907747720574318350L;

	// 시간동기화
	private Integer nUseTimeSync;
	private String strTimeServerName;
	private Integer nTimeSyncPeriod;

	// syslog연동
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
	private String	strSensorName;
	
	
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
	public String getStrSensorName() {
		return strSensorName;
	}
	public void setStrSensorName(String strSensorName) {
		this.strSensorName = strSensorName;
	}
	public boolean equalsTimeSync(ManagerSyslogDto dto) {
        if (nUseTimeSync.equals(dto.getnUseTimeSync()) && nTimeSyncPeriod.equals(dto.getnTimeSyncPeriod())
                && dto.getStrTimeServerName().equals(strTimeServerName)) {
            return true;
        }
        	return false;
        }
        
    public boolean eqalsSyslog(ManagerSyslogDto dto) {
        if (((strSyslogServerIp == null && dto.getStrSyslogServerIp() == null) || (strSyslogServerIp != null && strSyslogServerIp.equals(dto.getStrSyslogServerIp()))) &&
                dto.getnSyslogServerPort().equals(nSyslogServerPort)  && dto.getnSyslogEventLog().equals(nSyslogEventLog) &&
                dto.getnSyslogRawPacket().equals(nSyslogRawPacket) && dto.getnSyslogManagerSystem().equals(nSyslogManagerSystem) &&
                dto.getnSyslogSensorSystem().equals(nSyslogSensorSystem) && dto.getnSyslogAnomalyLog().equals(nSyslogAnomalyLog) &&
                dto.getnIpsSyslog().equals(nIpsSyslog) && dto.getnIpsSyslogServerPort().equals(nIpsSyslogServerPort) &&
                ((strIpsSyslogServerIp == null && dto.getStrIpsSyslogServerIp() == null) 
                		|| (strIpsSyslogServerIp != null && strIpsSyslogServerIp.equals(dto.getStrIpsSyslogServerIp()))) &&
        		((strSensorSyslogServerIp == null && dto.getStrSensorSyslogServerIp() == null) 
                		|| (strSensorSyslogServerIp != null && strSensorSyslogServerIp.equals(dto.getStrSensorSyslogServerIp())))
        ) 
        {
            return true;
        }
        return false;
    }
}
