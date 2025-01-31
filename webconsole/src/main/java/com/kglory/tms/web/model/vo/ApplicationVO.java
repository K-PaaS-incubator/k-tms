package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.packet.PacketAnalyzer;

public class ApplicationVO extends CommonBean implements Serializable {

	private static final long serialVersionUID = -2799998348748545181L;

	private long 		rNum = 0;
	private long 		lIndex;
	private String 		tmLogTime;
	private Integer 	bType;
	private Integer 	bIpType;
	private String 		deDestinationIp = "";
	private String 		strDestinationIp;
	private String 		dwSourceIp = "";
	private String 		strSourceIp;
	private long 		nSourcePort = 0;
	private long 		nDestinationPort = 0;
	private	long 		nProtocol;
	private String 		startDate = "";
	private String 		endDate ="";
	private Long 		totalRowSize = 0L;
	private BigInteger 	lvsensorIndex;
	private BigInteger 	lsensorIndex;
	private BigInteger 	lSrcNetIndex;
	private BigInteger 	lDstNetIndex;
	private String		sensorName;
	private String 		vsensorName;
	private String 		srcNetworkName;
	private String 		dstNetworkName;
	private String		destIp;
	private String		srcIp;
	private Long		sourceIp;
	private String		sData;
	private String 		tmDbTime;
	private Long		wDataSize;
	private long		sumCount;
	private long		totalCount;
	
	private String		time = "";
	private long		data = 0;
	private String		strName;
	private long 		destIpCount;
	private long		sourceIpCount;
	private long		srcIpCount;
	private long		dstIpCount;
	private long		sPortCount;
	private long		dPortCount;
	private long		strIpCount;
	private long		lTotCount;
	private Integer		nType;
	private Integer		nIpType;
	
	public long getrNum() {
		return rNum;
	}
	public void setrNum(long rNum) {
		this.rNum = rNum;
	}
	public long getlIndex() {
		return lIndex;
	}
	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
	}
	public String getTmLogTime() {
		return tmLogTime;
	}
	public void setTmLogTime(String tmLogTime) {
		this.tmLogTime = tmLogTime;
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
	public String getDeDestinationIp() {
		return deDestinationIp;
	}
	public void setDeDestinationIp(String deDestinationIp) {
		this.deDestinationIp = deDestinationIp;
	}
	public String getDwSourceIp() {
		return dwSourceIp;
	}
	public void setDwSourceIp(String dwSourceIp) {
		this.dwSourceIp = dwSourceIp;
	}
	public long getnSourcePort() {
		return nSourcePort;
	}
	public void setnSourcePort(long nSourcePort) {
		this.nSourcePort = nSourcePort;
	}
	public long getnDestinationPort() {
		return nDestinationPort;
	}
	public void setnDestinationPort(long nDestinationPort) {
		this.nDestinationPort = nDestinationPort;
	}
	public long getnProtocol() {
		return nProtocol;
	}
	public void setnProtocol(long nProtocol) {
		this.nProtocol = nProtocol;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getTotalRowSize() {
		return totalRowSize;
	}
	public void setTotalRowSize(Long totalRowSize) {
		this.totalRowSize = totalRowSize;
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
	public String getSrcNetworkName() {
		return srcNetworkName;
	}
	public void setSrcNetworkName(String srcNetworkName) {
		this.srcNetworkName = srcNetworkName;
	}
	public String getDstNetworkName() {
		return dstNetworkName;
	}
	public void setDstNetworkName(String dstNetworkName) {
		this.dstNetworkName = dstNetworkName;
	}
	public String getDestIp() {
		return destIp;
	}
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	public String getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	public Long getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(Long sourceIp) {
		this.sourceIp = sourceIp;
	}
	
	public String getsData() {
		return sData;
	}
	public void setsData(String sData) {
		this.sData = sData;
	}
	public String getTmDbTime() {
		return tmDbTime;
	}
	public void setTmDbTime(String tmDbTime) {
		this.tmDbTime = tmDbTime;
	}
	
	public Long getwDataSize() {
		return wDataSize;
	}
	public void setwDataSize(Long wDataSize) {
		this.wDataSize = wDataSize;
	}
	public String getStrDestinationIp() {
		return strDestinationIp;
	}
	public void setStrDestinationIp(String strDestinationIp) {
		this.strDestinationIp = strDestinationIp;
	}
	public String getStrSourceIp() {
		return strSourceIp;
	}
	public void setStrSourceIp(String strSourceIp) {
		this.strSourceIp = strSourceIp;
	}
	public long getSumCount() {
		return sumCount;
	}
	public void setSumCount(long sumCount) {
		this.sumCount = sumCount;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public long getData() {
		return data;
	}
	public void setData(long data) {
		this.data = data;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public long getDestIpCount() {
		return destIpCount;
	}
	public void setDestIpCount(long destIpCount) {
		this.destIpCount = destIpCount;
	}
	public long getSourceIpCount() {
		return sourceIpCount;
	}
	public void setSourceIpCount(long sourceIpCount) {
		this.sourceIpCount = sourceIpCount;
	}
	public long getSrcIpCount() {
		return srcIpCount;
	}
	public void setSrcIpCount(long srcIpCount) {
		this.srcIpCount = srcIpCount;
	}
	public long getsPortCount() {
		return sPortCount;
	}
	public void setsPortCount(long sPortCount) {
		this.sPortCount = sPortCount;
	}
	public long getStrIpCount() {
		return strIpCount;
	}
	public void setStrIpCount(long strIpCount) {
		this.strIpCount = strIpCount;
	}
	public long getdPortCount() {
		return dPortCount;
	}
	public void setdPortCount(long dPortCount) {
		this.dPortCount = dPortCount;
	}
	public long getDstIpCount() {
		return dstIpCount;
	}
	public void setDstIpCount(long dstIpCount) {
		this.dstIpCount = dstIpCount;
	}
	public long getlTotCount() {
		return lTotCount;
	}
	public void setlTotCount(long lTotCount) {
		this.lTotCount = lTotCount;
	}
	public Integer getnType() {
		return nType;
	}
	public void setnType(Integer nType) {
		this.nType = nType;
	}
	public Integer getnIpType() {
		return nIpType;
	}
	public void setnIpType(Integer nIpType) {
		this.nIpType = nIpType;
	}
	@Override
	public String toString() {
		return "ApplicationVO [rNum=" + rNum + ", lIndex=" + lIndex
				+ ", tmLogTime=" + tmLogTime + ", bType=" + bType
				+ ", bIpType=" + bIpType + ", deDestinationIp="
				+ deDestinationIp + ", strDestinationIp=" + strDestinationIp
				+ ", dwSourceIp=" + dwSourceIp + ", strSourceIp=" + strSourceIp
				+ ", nSourcePort=" + nSourcePort + ", nDestinationPort="
				+ nDestinationPort + ", nProtocol=" + nProtocol
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", totalRowSize=" + totalRowSize + ", lvsensorIndex="
				+ lvsensorIndex + ", lsensorIndex=" + lsensorIndex
				+ ", lSrcNetIndex=" + lSrcNetIndex + ", lDstNetIndex="
				+ lDstNetIndex + ", sensorName=" + sensorName
				+ ", vsensorName=" + vsensorName + ", srcNetworkName="
				+ srcNetworkName + ", dstNetworkName=" + dstNetworkName
				+ ", destIp=" + destIp + ", srcIp=" + srcIp + ", sourceIp="
				+ sourceIp + ", sData=" + sData + ", tmDbTime=" + tmDbTime
				+ ", wDataSize=" + wDataSize + ", sumCount=" + sumCount
				+ ", totalCount=" + totalCount + ", time=" + time + ", data="
				+ data + ", strName=" + strName + ", destIpCount="
				+ destIpCount + ", sourceIpCount=" + sourceIpCount
				+ ", srcIpCount=" + srcIpCount + ", dstIpCount=" + dstIpCount
				+ ", sPortCount=" + sPortCount + ", dPortCount=" + dPortCount
				+ ", strIpCount=" + strIpCount + ", lTotCount=" + lTotCount
				+ ", nType=" + nType + ", nIpType=" + nIpType + "]";
	}
}
