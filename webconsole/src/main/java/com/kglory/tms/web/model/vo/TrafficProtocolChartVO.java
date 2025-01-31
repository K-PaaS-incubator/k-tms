package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class TrafficProtocolChartVO extends CommonBean implements Serializable {
	private static final long serialVersionUID = -2690458932448698094L;

	
	private String				time				= "";
	private long				data				= 0;
	private long				dataIn				= 0;
	private long				dataOut				= 0;
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
	public long getDataIn() {
		return dataIn;
	}
	public void setDataIn(long dataIn) {
		this.dataIn = dataIn;
	}
	public long getDataOut() {
		return dataOut;
	}
	public void setDataOut(long dataOut) {
		this.dataOut = dataOut;
	}
	
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	@Override
	public String toString() {
		return "TrafficProtocolChartVO [time=" + time + ", data=" + data
				+ ", dataIn=" + dataIn + ", dataOut=" + dataOut + ", strName="
				+ strName + "]";
	}
	
}
