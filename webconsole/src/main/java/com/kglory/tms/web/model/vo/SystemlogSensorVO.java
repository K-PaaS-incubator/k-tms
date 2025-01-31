package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class SystemlogSensorVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 3943683195207356968L;

	private BigInteger			lSystemlogIndex;
	private String				tmOccur;
	private BigInteger			lSensorIndex;
	private String				sensorName;
	private String				tmSensorRup;
	private Integer				dwTimeSyncNum;
	private Integer				dwCpuSpeed;
	private Integer				dwCpuNum;
	private Integer				dwMemTotal;
	private Integer				dblMaxCpuUsage;
	private Integer				dblMinCpuUsage;
	private Integer				dblAvgCpuUsage;
	private Integer				dblCurCpuUsage;
	private Integer				dblMaxMemUsed;
	private Integer				dblMinMemUsed;
	private Integer				dblAvgMemUsed;
	private Integer				dblCurMemUsed;
	private Integer				dwProcessNum;
	private String				strHddName;
	private Long				dwHddTotal;
	private Long				dwHddUsed;
	
	public BigInteger getlSystemlogIndex() {
		return lSystemlogIndex;
	}
	
	public void setlSystemlogIndex(BigInteger lSystemlogIndex) {
		this.lSystemlogIndex = lSystemlogIndex;
	}
	
	public String getTmOccur() {
		return tmOccur;
	}
	
	public void setTmOccur(String tmOccur) {
		this.tmOccur = tmOccur;
	}
	
	public BigInteger getlSensorIndex() {
		return lSensorIndex;
	}
	
	public void setlSensorIndex(BigInteger lSensorIndex) {
		this.lSensorIndex = lSensorIndex;
	}
	
	public String getSensorName() {
		return sensorName;
	}
	
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	public String getTmSensorRup() {
		return tmSensorRup;
	}
	
	public void setTmSensorRup(String tmSensorRup) {
		this.tmSensorRup = tmSensorRup;
	}
	
	public Integer getDwTimeSyncNum() {
		return dwTimeSyncNum;
	}
	
	public void setDwTimeSyncNum(Integer dwTimeSyncNum) {
		this.dwTimeSyncNum = dwTimeSyncNum;
	}
	
	public Integer getDwCpuSpeed() {
		return dwCpuSpeed;
	}
	
	public void setDwCpuSpeed(Integer dwCpuSpeed) {
		this.dwCpuSpeed = dwCpuSpeed;
	}
	
	public Integer getDwCpuNum() {
		return dwCpuNum;
	}
	
	public void setDwCpuNum(Integer dwCpuNum) {
		this.dwCpuNum = dwCpuNum;
	}
	
	public Integer getDwMemTotal() {
		return dwMemTotal;
	}
	
	public void setDwMemTotal(Integer dwMemTotal) {
		this.dwMemTotal = dwMemTotal;
	}
	
	public Integer getDblMaxCpuUsage() {
		return dblMaxCpuUsage;
	}
	
	public void setDblMaxCpuUsage(Integer dblMaxCpuUsage) {
		this.dblMaxCpuUsage = dblMaxCpuUsage;
	}
	
	public Integer getDblMinCpuUsage() {
		return dblMinCpuUsage;
	}
	
	public void setDblMinCpuUsage(Integer dblMinCpuUsage) {
		this.dblMinCpuUsage = dblMinCpuUsage;
	}
	
	public Integer getDblAvgCpuUsage() {
		return dblAvgCpuUsage;
	}
	
	public void setDblAvgCpuUsage(Integer dblAvgCpuUsage) {
		this.dblAvgCpuUsage = dblAvgCpuUsage;
	}
	
	public Integer getDblCurCpuUsage() {
		return dblCurCpuUsage;
	}
	
	public void setDblCurCpuUsage(Integer dblCurCpuUsage) {
		this.dblCurCpuUsage = dblCurCpuUsage;
	}
	
	public Integer getDblMaxMemUsed() {
		return dblMaxMemUsed;
	}
	
	public void setDblMaxMemUsed(Integer dblMaxMemUsed) {
		this.dblMaxMemUsed = dblMaxMemUsed;
	}
	
	public Integer getDblMinMemUsed() {
		return dblMinMemUsed;
	}
	
	public void setDblMinMemUsed(Integer dblMinMemUsed) {
		this.dblMinMemUsed = dblMinMemUsed;
	}
	
	public Integer getDblAvgMemUsed() {
		return dblAvgMemUsed;
	}
	
	public void setDblAvgMemUsed(Integer dblAvgMemUsed) {
		this.dblAvgMemUsed = dblAvgMemUsed;
	}
	
	public Integer getDblCurMemUsed() {
		return dblCurMemUsed;
	}
	
	public void setDblCurMemUsed(Integer dblCurMemUsed) {
		this.dblCurMemUsed = dblCurMemUsed;
	}
	
	public Integer getDwProcessNum() {
		return dwProcessNum;
	}
	
	public void setDwProcessNum(Integer dwProcessNum) {
		this.dwProcessNum = dwProcessNum;
	}
	
	public String getStrHddName() {
		return strHddName;
	}
	
	public void setStrHddName(String strHddName) {
		this.strHddName = strHddName;
	}
	
	public Long getDwHddTotal() {
		return dwHddTotal;
	}
	
	public void setDwHddTotal(Long dwHddTotal) {
		this.dwHddTotal = dwHddTotal;
	}
	
	public Long getDwHddUsed() {
		return dwHddUsed;
	}
	
	public void setDwHddUsed(Long dwHddUsed) {
		this.dwHddUsed = dwHddUsed;
	}
	
	@Override
	public String toString() {
		return "SystemlogSensorVO [lSystemlogIndex=" + lSystemlogIndex + ", tmOccur=" + tmOccur + ", lSensorIndex="
				+ lSensorIndex + ", sensorName=" + sensorName + ", tmSensorRup=" + tmSensorRup + ", dwTimeSyncNum="
				+ dwTimeSyncNum + ", dwCpuSpeed=" + dwCpuSpeed + ", dwCpuNum=" + dwCpuNum + ", dwMemTotal="
				+ dwMemTotal + ", dblMaxCpuUsage=" + dblMaxCpuUsage + ", dblMinCpuUsage=" + dblMinCpuUsage
				+ ", dblAvgCpuUsage=" + dblAvgCpuUsage + ", dblCurCpuUsage=" + dblCurCpuUsage + ", dblMaxMemUsed="
				+ dblMaxMemUsed + ", dblMinMemUsed=" + dblMinMemUsed + ", dblAvgMemUsed=" + dblAvgMemUsed
				+ ", dblCurMemUsed=" + dblCurMemUsed + ", dwProcessNum=" + dwProcessNum + ", strHddName=" + strHddName
				+ ", dwHddTotal=" + dwHddTotal + ", dwHddUsed=" + dwHddUsed + "]";
	}
	
}
