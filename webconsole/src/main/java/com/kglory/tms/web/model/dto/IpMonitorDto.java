package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

public class IpMonitorDto extends CommonBean {
	
	private String strFromIp;
	private String strToIp;
	private Integer lIndex;
	private Long lsensorIndex;
	
	public String getStrFromIp() {
		return strFromIp;
	}
	public void setStrFromIp(String strFromIp) {
		this.strFromIp = strFromIp;
	}
	public String getStrToIp() {
		return strToIp;
	}
	public void setStrToIp(String strToIp) {
		this.strToIp = strToIp;
	}
	public Integer getlIndex() {
		return lIndex;
	}
	public void setlIndex(Integer lIndex) {
		this.lIndex = lIndex;
	}
	public Long getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(Long lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
}
