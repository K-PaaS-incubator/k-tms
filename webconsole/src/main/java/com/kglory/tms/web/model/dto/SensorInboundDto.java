package com.kglory.tms.web.model.dto;

public class SensorInboundDto {
	
	long lsensorIndex;
	String strEnable;
	String strType;
	String strNicInfo;
	String strIpInfo;
	
	public long getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(long lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
	public String getStrEnable() {
		return strEnable;
	}
	public void setStrEnable(String strEnable) {
		this.strEnable = strEnable;
	}
	public String getStrType() {
		return strType;
	}
	public void setStrType(String strType) {
		this.strType = strType;
	}
	public String getStrNicInfo() {
		return strNicInfo;
	}
	public void setStrNicInfo(String strNicInfo) {
		this.strNicInfo = strNicInfo;
	}
	public String getStrIpInfo() {
		return strIpInfo;
	}
	public void setStrIpInfo(String strIpInfo) {
		this.strIpInfo = strIpInfo;
	}
	@Override
	public String toString() {
		return "SensorInboundDto [lsensorIndex=" + lsensorIndex
				+ ", strEnable=" + strEnable + ", strType=" + strType
				+ ", strNicInfo=" + strNicInfo + ", strIpInfo=" + strIpInfo
				+ "]";
	}
}
