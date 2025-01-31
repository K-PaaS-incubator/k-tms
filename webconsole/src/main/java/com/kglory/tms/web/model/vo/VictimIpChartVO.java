package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class VictimIpChartVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 5048314996892878188L;

	
	private String				time				= "";
	private long				data				= 0;
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

	public void setStrName(String strName) {
		this.strName = strName;
	}

	@Override
	public String toString() {
		return "VictimIpChartVO [time=" + time + ", data=" + data
				+ ", strName=" + strName + "]";
	}
}
