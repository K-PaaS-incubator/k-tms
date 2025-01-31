package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class SensorInboundVO  extends CommonBean implements Serializable{

	private static final long serialVersionUID = 3549284534273028701L;

	private long lsensorIndex;
	private String strEnable;
	private String strType;
	private String strNicInfo;
	private String strIpInfo;
	
	private List<String> ipInfo;
	
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
	public List<String> getIpInfo() {
		return ipInfo;
	}
	public void setIpInfo(List<String> ipInfo) {
		this.ipInfo = ipInfo;
	}
	@Override
	public String toString() {
		return "SensorInboundVO [lsensorIndex=" + lsensorIndex + ", strEnable="
				+ strEnable + ", strType=" + strType + ", strNicInfo="
				+ strNicInfo + ", strIpInfo=" + strIpInfo + ", ipInfo="
				+ ipInfo + "]";
	}
	
}
