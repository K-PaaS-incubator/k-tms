package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class SensorAliveVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= -4344885865695218663L;

	private Long				lindex;
	private String				strName;
	private String				tmCur;
	private Long				dblTotalPacketFrame;
	private Long				dblTotalPacketBytes;
	private Long				dblPps;
	private Long				dblBps;
	private Long				dblDropPacket;
	private Long				dblDpps1000;
	private Long				dblSession;
	private Long				dblMaliciousSession;
	private Long				dblLps;
	private Long				dblDlps;
	private Long				dblMaliciousFrame;
	private Long				dblMaliciousBytes;
	private Long				dblMaliciousBps;
	private Long				dblMaliciousPps;
	private Long				lCpuSpeed;
	private Integer				nCpuNum;
	private Integer				nCpuUsage;
	private Long				dblMemTotal;
	private Long				dblMemUsed;
	private Long				lProcessNum;
	private String				tmSensorUpTime;
	private Long				lTimeSyncNum;
	private String				strDriveName;
	private Long				dblHddTotal;
	private Long				dblHddUsed;
	
	public Long getLindex() {
		return lindex;
	}
	
	public void setLindex(Long lindex) {
		this.lindex = lindex;
	}
	
	public String getStrName() {
		return strName;
	}
	
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	public String getTmCur() {
		return tmCur;
	}
	
	public void setTmCur(String tmCur) {
		this.tmCur = tmCur;
	}
	
	public Long getDblTotalPacketFrame() {
		return dblTotalPacketFrame;
	}
	
	public void setDblTotalPacketFrame(Long dblTotalPacketFrame) {
		this.dblTotalPacketFrame = dblTotalPacketFrame;
	}
	
	public Long getDblTotalPacketBytes() {
		return dblTotalPacketBytes;
	}
	
	public void setDblTotalPacketBytes(Long dblTotalPacketBytes) {
		this.dblTotalPacketBytes = dblTotalPacketBytes;
	}
	
	public Long getDblPps() {
		return dblPps;
	}
	
	public void setDblPps(Long dblPps) {
		this.dblPps = dblPps;
	}
	
	public Long getDblBps() {
		return dblBps;
	}
	
	public void setDblBps(Long dblBps) {
		this.dblBps = dblBps;
	}
	
	public Long getDblDropPacket() {
		return dblDropPacket;
	}
	
	public void setDblDropPacket(Long dblDropPacket) {
		this.dblDropPacket = dblDropPacket;
	}
	
	public Long getDblDpps1000() {
		return dblDpps1000;
	}
	
	public void setDblDpps1000(Long dblDpps1000) {
		this.dblDpps1000 = dblDpps1000;
	}
	
	public Long getDblSession() {
		return dblSession;
	}
	
	public void setDblSession(Long dblSession) {
		this.dblSession = dblSession;
	}
	
	public Long getDblMaliciousSession() {
		return dblMaliciousSession;
	}
	
	public void setDblMaliciousSession(Long dblMaliciousSession) {
		this.dblMaliciousSession = dblMaliciousSession;
	}
	
	public Long getDblLps() {
		return dblLps;
	}
	
	public void setDblLps(Long dblLps) {
		this.dblLps = dblLps;
	}
	
	public Long getDblDlps() {
		return dblDlps;
	}
	
	public void setDblDlps(Long dblDlps) {
		this.dblDlps = dblDlps;
	}
	
	public Long getDblMaliciousFrame() {
		return dblMaliciousFrame;
	}
	
	public void setDblMaliciousFrame(Long dblMaliciousFrame) {
		this.dblMaliciousFrame = dblMaliciousFrame;
	}
	
	public Long getDblMaliciousBytes() {
		return dblMaliciousBytes;
	}
	
	public void setDblMaliciousBytes(Long dblMaliciousBytes) {
		this.dblMaliciousBytes = dblMaliciousBytes;
	}
	
	public Long getDblMaliciousBps() {
		return dblMaliciousBps;
	}
	
	public void setDblMaliciousBps(Long dblMaliciousBps) {
		this.dblMaliciousBps = dblMaliciousBps;
	}
	
	public Long getDblMaliciousPps() {
		return dblMaliciousPps;
	}
	
	public void setDblMaliciousPps(Long dblMaliciousPps) {
		this.dblMaliciousPps = dblMaliciousPps;
	}
	
	public Long getlCpuSpeed() {
		return lCpuSpeed;
	}
	
	public void setlCpuSpeed(Long lCpuSpeed) {
		this.lCpuSpeed = lCpuSpeed;
	}
	
	public Integer getnCpuNum() {
		return nCpuNum;
	}
	
	public void setnCpuNum(Integer nCpuNum) {
		this.nCpuNum = nCpuNum;
	}
	
	public Integer getnCpuUsage() {
		return nCpuUsage;
	}
	
	public void setnCpuUsage(Integer nCpuUsage) {
		this.nCpuUsage = nCpuUsage;
	}
	
	public Long getDblMemTotal() {
		return dblMemTotal;
	}
	
	public void setDblMemTotal(Long dblMemTotal) {
		this.dblMemTotal = dblMemTotal;
	}
	
	public Long getDblMemUsed() {
		return dblMemUsed;
	}
	
	public void setDblMemUsed(Long dblMemUsed) {
		this.dblMemUsed = dblMemUsed;
	}
	
	public Long getlProcessNum() {
		return lProcessNum;
	}
	
	public void setlProcessNum(Long lProcessNum) {
		this.lProcessNum = lProcessNum;
	}
	
	public String getTmSensorUpTime() {
		return tmSensorUpTime;
	}
	
	public void setTmSensorUpTime(String tmSensorUpTime) {
		this.tmSensorUpTime = tmSensorUpTime;
	}
	
	public Long getlTimeSyncNum() {
		return lTimeSyncNum;
	}
	
	public void setlTimeSyncNum(Long lTimeSyncNum) {
		this.lTimeSyncNum = lTimeSyncNum;
	}
	
	public String getStrDriveName() {
		return strDriveName;
	}
	
	public void setStrDriveName(String strDriveName) {
		this.strDriveName = strDriveName;
	}
	
	public Long getDblHddTotal() {
		return dblHddTotal;
	}
	
	public void setDblHddTotal(Long dblHddTotal) {
		this.dblHddTotal = dblHddTotal;
	}
	
	public Long getDblHddUsed() {
		return dblHddUsed;
	}
	
	public void setDblHddUsed(Long dblHddUsed) {
		this.dblHddUsed = dblHddUsed;
	}
	
	@Override
	public String toString() {
		return "SensorStateVO [lindex=" + lindex + ", strName=" + strName + ", tmCur=" + tmCur
				+ ", dblTotalPacketFrame=" + dblTotalPacketFrame + ", dblTotalPacketBytes=" + dblTotalPacketBytes
				+ ", dblPps=" + dblPps + ", dblBps=" + dblBps + ", dblDropPacket=" + dblDropPacket + ", dblDpps1000="
				+ dblDpps1000 + ", dblSession=" + dblSession + ", dblMaliciousSession=" + dblMaliciousSession
				+ ", dblLps=" + dblLps + ", dblDlps=" + dblDlps + ", dblMaliciousFrame=" + dblMaliciousFrame
				+ ", dblMaliciousBytes=" + dblMaliciousBytes + ", dblMaliciousBps=" + dblMaliciousBps
				+ ", dblMaliciousPps=" + dblMaliciousPps + ", lCpuSpeed=" + lCpuSpeed + ", nCpuNum=" + nCpuNum
				+ ", nCpuUsage=" + nCpuUsage + ", dblMemTotal=" + dblMemTotal + ", dblMemUsed=" + dblMemUsed
				+ ", lProcessNum=" + lProcessNum + ", tmSensorUpTime=" + tmSensorUpTime + ", lTimeSyncNum="
				+ lTimeSyncNum + ", strDriveName=" + strDriveName + ", dblHddTotal=" + dblHddTotal + ", dblHddUsed="
				+ dblHddUsed + "]";
	}
	
}
