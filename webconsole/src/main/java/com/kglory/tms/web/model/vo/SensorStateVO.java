package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class SensorStateVO extends CommonBean implements Serializable{

	/**
	 * @author leekyunghee
	 */
	private static final long serialVersionUID = -4344885865695218663L;

	private long			  lIndex 			= 0;
	private long			  lsensorIndex 		= 0;
	private long			  lsystemlogIndex 	= 0;
	private String			  strName			= "";
	private String			  tmOccur			= "";
	private long			  dblcurCpuUsage	= 0;
	private long			  dblcurMemUsed		= 0;
	private long			  dwMemTotal		= 0;
	private long			  dwHddUsed			= 0;
	private long			  dwHddTotal		= 0;
	private long			  dwProcessNum		= 0;
	
	public long getlIndex() {
		return lIndex;
	}
	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
	}
	public long getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(long lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
	public long getLsystemlogIndex() {
		return lsystemlogIndex;
	}
	public void setLsystemlogIndex(long lsystemlogIndex) {
		this.lsystemlogIndex = lsystemlogIndex;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getTmOccur() {
		return tmOccur;
	}
	public void setTmOccur(String tmOccur) {
		this.tmOccur = tmOccur;
	}
	public long getDblcurCpuUsage() {
		return dblcurCpuUsage;
	}
	public void setDblcurCpuUsage(long dblcurCpuUsage) {
		this.dblcurCpuUsage = dblcurCpuUsage;
	}
	public long getDblcurMemUsed() {
		return dblcurMemUsed;
	}
	public void setDblcurMemUsed(long dblcurMemUsed) {
		this.dblcurMemUsed = dblcurMemUsed;
	}
	public long getDwMemTotal() {
		return dwMemTotal;
	}
	public void setDwMemTotal(long dwMemTotal) {
		this.dwMemTotal = dwMemTotal;
	}
	public long getDwHddUsed() {
		return dwHddUsed;
	}
	public void setDwHddUsed(long dwHddUsed) {
		this.dwHddUsed = dwHddUsed;
	}
	public long getDwHddTotal() {
		return dwHddTotal;
	}
	public void setDwHddTotal(long dwHddTotal) {
		this.dwHddTotal = dwHddTotal;
	}
	public long getDwProcessNum() {
		return dwProcessNum;
	}
	public void setDwProcessNum(long dwProcessNum) {
		this.dwProcessNum = dwProcessNum;
	}

	@Override
	public String toString() {
		return "SensorStateVO [lIndex=" + lIndex + ", lsensorIndex="
				+ lsensorIndex + ", lsystemlogIndex=" + lsystemlogIndex
				+ ", strName=" + strName + ", tmOccur=" + tmOccur
				+ ", dblcurCpuUsage=" + dblcurCpuUsage + ", dblcurMemUsed="
				+ dblcurMemUsed + ", dwMemTotal=" + dwMemTotal + ", dwHddUsed="
				+ dwHddUsed + ", dwHddTotal=" + dwHddTotal + ", dwProcessNum="
				+ dwProcessNum + "]";
	}
}
