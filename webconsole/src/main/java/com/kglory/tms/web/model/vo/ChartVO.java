package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;

/**
 * 차트에게 데이터를 보낼 때 사용하는 DTO<br/>
 * ldata는 탐지건수와 같이 정수형 데이터를 차트에게 보낼 때 사용하며, ddata는 트래픽과 같이 실수형 데이터를 차트에게 보낼 때 사용한다.
 * 
 * @author idess
 *
 */
public class ChartVO extends CommonBean {
	
	private String	time;
	private Long	ldata;		// data type is long
	private Long	minLData;
	private Long	maxLData;
	private Double	ddata;		// data type is double
	private Double	ddataIn;		// data type is double
	private Double	ddataOut;		// data type is double
	private Double	minDData;
	private Double	maxDData;
	private Double	avgData;
	private Double	sumData;
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public Long getLdata() {
		return ldata;
	}
	
	public void setLdata(Long ldata) {
		this.ldata = ldata;
	}
	
	public Long getMinLData() {
		return minLData;
	}
	
	public void setMinLData(Long minLData) {
		this.minLData = minLData;
	}
	
	public Long getMaxLData() {
		return maxLData;
	}
	
	public void setMaxLData(Long maxLData) {
		this.maxLData = maxLData;
	}
	
	public Double getDdata() {
		return ddata;
	}
	
	public void setDdata(Double ddata) {
		this.ddata = ddata;
	}
	
	public Double getMinDData() {
		return minDData;
	}

	public void setMinDData(Double minDData) {
		this.minDData = minDData;
	}

	public Double getMaxDData() {
		return maxDData;
	}

	public void setMaxDData(Double maxDData) {
		this.maxDData = maxDData;
	}

	public Double getAvgData() {
		return avgData;
	}
	
	public void setAvgData(Double avgData) {
		this.avgData = avgData;
	}
	public Double getSumData() {
		return sumData;
	}
	public void setSumData(Double sumData) {
		this.sumData = sumData;
	}

	public Double getDdataIn() {
		return ddataIn;
	}

	public void setDdataIn(Double ddataIn) {
		this.ddataIn = ddataIn;
	}

	public Double getDdataOut() {
		return ddataOut;
	}

	public void setDdataOut(Double ddataOut) {
		this.ddataOut = ddataOut;
	}

	@Override
	public String toString() {
		return "ChartVO [time=" + time + ", ldata=" + ldata + ", minLData="
				+ minLData + ", maxLData=" + maxLData + ", ddata=" + ddata
				+ ", ddataIn=" + ddataIn + ", ddataOut=" + ddataOut
				+ ", minDData=" + minDData + ", maxDData=" + maxDData
				+ ", avgData=" + avgData + ", sumData=" + sumData + "]";
	}
	
}
