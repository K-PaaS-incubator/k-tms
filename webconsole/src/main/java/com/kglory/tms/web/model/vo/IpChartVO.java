package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class IpChartVO extends CommonBean implements Serializable {
	private static final long  	serialVersionUID = -2154118604430619665L;

	
	private String			   	time			 = "";
	private long				data			 = 0;
	private String				strName;
	
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
	@Override
	public String toString() {
		return "IpChartVO [time=" + time + ", data=" + data + ", strName="
				+ strName + "]";
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	
}
