package com.kglory.tms.web.model.dto;

public class SystemStatusMonitorDto {
	private String	tmManagerUp;
	private Integer	dwCpuSpeed;
	private Integer	dwCpuNum;
	private Integer	dwMemTotal;
	private Integer	dblMaxCpuUsage;
	private Integer	dblMincpuUsage;
	private Integer	dblAvgCpuUsage;
	private Integer	dblCurCpuUsage;
	private Integer	dblMaxMemUsed;
	private Integer	dblMinMemUsed;
	private Integer	dblAvgMemUsed;
	private Integer	dblCurMemUsed;
	private Integer	dwProcessNum;
	private String	strHddName;
	private Long	dwHddTotal;
	private Long	dwHddUsed;
	private Integer	dwStoreLog;
	private Integer	dwStoreTraffic;
	
	public String getTmManagerUp() {
		return tmManagerUp;
	}
	
	public void setTmManagerUp(String tmManagerUp) {
		this.tmManagerUp = tmManagerUp;
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
	
	public Integer getDblMincpuUsage() {
		return dblMincpuUsage;
	}
	
	public void setDblMincpuUsage(Integer dblMincpuUsage) {
		this.dblMincpuUsage = dblMincpuUsage;
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
	
	public Integer getDwStoreLog() {
		return dwStoreLog;
	}
	
	public void setDwStoreLog(Integer dwStoreLog) {
		this.dwStoreLog = dwStoreLog;
	}
	
	public Integer getDwStoreTraffic() {
		return dwStoreTraffic;
	}
	
	public void setDwStoreTraffic(Integer dwStoreTraffic) {
		this.dwStoreTraffic = dwStoreTraffic;
	}
	
	@Override
	public String toString() {
		return "SystemStatusMonitorDto [tmManagerUp=" + tmManagerUp + ", dwCpuSpeed=" + dwCpuSpeed + ", dwCpuNum="
				+ dwCpuNum + ", dwMemTotal=" + dwMemTotal + ", dblMaxCpuUsage=" + dblMaxCpuUsage + ", dblMincpuUsage="
				+ dblMincpuUsage + ", dblAvgCpuUsage=" + dblAvgCpuUsage + ", dblCurCpuUsage=" + dblCurCpuUsage
				+ ", dblMaxMemUsed=" + dblMaxMemUsed + ", dblMinMemUsed=" + dblMinMemUsed + ", dblAvgMemUsed="
				+ dblAvgMemUsed + ", dblCurMemUsed=" + dblCurMemUsed + ", dwProcessNum=" + dwProcessNum
				+ ", strHddName=" + strHddName + ", dwHddTotal=" + dwHddTotal + ", dwHddUsed=" + dwHddUsed
				+ ", dwStoreLog=" + dwStoreLog + ", dwStoreTraffic=" + dwStoreTraffic + "]";
	}
	
}
