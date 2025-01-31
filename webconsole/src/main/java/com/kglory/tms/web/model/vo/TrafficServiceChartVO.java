package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class TrafficServiceChartVO extends CommonBean implements Serializable {
	private static final long serialVersionUID = -2432720556062427746L;


	private String				time				= "";
	private long				data				= 0;
	private String				strName;
	private long				dataIn				= 0;
	private long				dataOut				= 0;

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
	@Override
	public String toString() {
		return "TrafficServiceChartVO [time=" + time + ", data=" + data
				+ ", strName=" + strName + ", dataIn=" + dataIn + ", dataOut="
				+ dataOut + "]";
	}
	
}
