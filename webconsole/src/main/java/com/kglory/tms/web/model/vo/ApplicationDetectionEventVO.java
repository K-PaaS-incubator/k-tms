package com.kglory.tms.web.model.vo;

import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.packet.PacketAnalyzer;

public class ApplicationDetectionEventVO extends CommonBean {
	
	private BigInteger 	lIndex;
	private String 		tmLogTime;
	private String 		dwSourceIp;
	private String 		strSourceIp;
	private String 		deDestinationIp;
	private String 		strDestinationIp;
	private Integer 	nProtocol;
	private Integer 	nSourcePort;
	private Integer 	nDestinationPort;
	private Integer 	bType;
	private Integer 	bIpType;
	private Long 		wDataSize;
	private BigInteger 	lSrcNetIndex;
	private BigInteger 	lDstNetIndex;
	private BigInteger 	lvsensorIndex;
	private BigInteger 	lsensorIndex;
	private String 		sData;
	private String 		tmDbTime;
	private String		sensorName;
	private String 		vsensorName;
	
	public BigInteger getlIndex() {
		return lIndex;
	}
	public void setlIndex(BigInteger lIndex) {
		this.lIndex = lIndex;
	}
	public String getTmLogTime() {
		return tmLogTime;
	}
	public void setTmLogTime(String tmLogTime) {
		this.tmLogTime = tmLogTime;
	}
	public String getDwSourceIp() {
		return dwSourceIp;
	}
	public void setDwSourceIp(String dwSourceIp) {
		this.dwSourceIp = dwSourceIp;
	}
	public String getStrSourceIp() {
		return strSourceIp;
	}
	public void setStrSourceIp(String strSourceIp) {
		this.strSourceIp = strSourceIp;
	}
    public String getDeDestinationIp() {
		return deDestinationIp;
	}
	public void setDeDestinationIp(String deDestinationIp) {
		this.deDestinationIp = deDestinationIp;
	}
	public String getStrDestinationIp() {
            return strDestinationIp;
    }
    public void setStrDestinationIp(String strDestinationIp) {
            this.strDestinationIp = strDestinationIp;
    }
	public Integer getnProtocol() {
		return nProtocol;
	}
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}
	public Integer getnSourcePort() {
		return nSourcePort;
	}
	public void setnSourcePort(Integer nSourcePort) {
		this.nSourcePort = nSourcePort;
	}
	public Integer getnDestinationPort() {
		return nDestinationPort;
	}
	public void setnDestinationPort(Integer nDestinationPort) {
		this.nDestinationPort = nDestinationPort;
	}
	public Integer getbType() {
		return bType;
	}
	public void setbType(Integer bType) {
		this.bType = bType;
	}
	public Integer getbIpType() {
		return bIpType;
	}
	public void setbIpType(Integer bIpType) {
		this.bIpType = bIpType;
	}
	public Long getwDataSize() {
		return wDataSize;
	}
	public void setwDataSize(Long wDataSize) {
		this.wDataSize = wDataSize;
	}
	public BigInteger getlSrcNetIndex() {
		return lSrcNetIndex;
	}
	public void setlSrcNetIndex(BigInteger lSrcNetIndex) {
		this.lSrcNetIndex = lSrcNetIndex;
	}
	public BigInteger getlDstNetIndex() {
		return lDstNetIndex;
	}
	public void setlDstNetIndex(BigInteger lDstNetIndex) {
		this.lDstNetIndex = lDstNetIndex;
	}
	public BigInteger getLvsensorIndex() {
		return lvsensorIndex;
	}
	public void setLvsensorIndex(BigInteger lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}
	public BigInteger getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(BigInteger lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
	public String getsData() {
		return sData;
	}
	public void setsData(String sData) {
	    if (sData != null && !sData.isEmpty()) {
	        sData = PacketAnalyzer.applicationHexToString(sData, Integer.valueOf(this.wDataSize.intValue()));
	    }
		this.sData = sData;
	}
	public String getTmDbTime() {
		return tmDbTime;
	}
	public void setTmDbTime(String tmDbTime) {
		this.tmDbTime = tmDbTime;
	}
	public String getSensorName() {
		return sensorName;
	}
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	public String getVsensorName() {
		return vsensorName;
	}
	public void setVsensorName(String vsensorName) {
		this.vsensorName = vsensorName;
	}
}
