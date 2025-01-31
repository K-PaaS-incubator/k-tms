package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class InstitutionDto extends CommonBean{

	BigInteger		lnetgroupIndex;
	BigInteger		lnetworkIndex;
	BigInteger		lvsensorIndex;
	BigInteger		lsensorIndex;
	
	String			startDateInput;
	String			endDateInput;
	
	int				winBoundSelect;
	String			winBoundProtocol;
	
	String			queryTableName;
	List<String>	tableNames;
	
	long			avgTime;
	
	String			tableUnit;
	Long			timeDiffSecond;	
	Long			ipType;
	
	public BigInteger getLnetgroupIndex() {
		return lnetgroupIndex;
	}
	public void setLnetgroupIndex(BigInteger lnetgroupIndex) {
		this.lnetgroupIndex = lnetgroupIndex;
	}
	public BigInteger getLnetworkIndex() {
		return lnetworkIndex;
	}
	public void setLnetworkIndex(BigInteger lnetworkIndex) {
		this.lnetworkIndex = lnetworkIndex;
	}
	public BigInteger getLvsensorIndex() {
		return lvsensorIndex;
	}
	public void setLvsensorIndex(BigInteger lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}
	public String getStartDateInput() {
		return startDateInput;
	}
	public void setStartDateInput(String startDateInput) {
		this.startDateInput = startDateInput;
	}
	public String getEndDateInput() {
		return endDateInput;
	}
	public void setEndDateInput(String endDateInput) {
		this.endDateInput = endDateInput;
	}
	public int getWinBoundSelect() {
		return winBoundSelect;
	}
	public void setWinBoundSelect(int winBoundSelect) {
		this.winBoundSelect = winBoundSelect;
	}
	public String getQueryTableName() {
		return queryTableName;
	}
	public void setQueryTableName(String queryTableName) {
		this.queryTableName = queryTableName;
	}
	public List<String> getTableNames() {
		return tableNames;
	}
	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	public BigInteger getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(BigInteger lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
	
	public String getWinBoundProtocol() {
		return winBoundProtocol;
	}
	public void setWinBoundProtocol(String winBoundProtocol) {
		this.winBoundProtocol = winBoundProtocol;
	}
	public long getAvgTime() {
		return avgTime;
	}
	public void setAvgTime(long avgTime) {
		this.avgTime = avgTime;
	}
	public String getTableUnit() {
		return tableUnit;
	}
	public void setTableUnit(String tableUnit) {
		this.tableUnit = tableUnit;
	}
	public Long getTimeDiffSecond() {
		return timeDiffSecond;
	}
	public void setTimeDiffSecond(Long timeDiffSecond) {
		this.timeDiffSecond = timeDiffSecond;
	}
	public Long getIpType() {
		return ipType;
	}
	public void setIpType(Long ipType) {
		this.ipType = ipType;
	}
	@Override
	public String toString() {
		return "InstitutionDto [lnetgroupIndex=" + lnetgroupIndex
				+ ", lnetworkIndex=" + lnetworkIndex + ", lvsensorIndex="
				+ lvsensorIndex + ", lsensorIndex=" + lsensorIndex
				+ ", startDateInput=" + startDateInput + ", endDateInput="
				+ endDateInput + ", winBoundSelect=" + winBoundSelect
				+ ", winBoundProtocol=" + winBoundProtocol
				+ ", queryTableName=" + queryTableName + ", tableNames="
				+ tableNames + ", avgTime=" + avgTime + ", tableUnit="
				+ tableUnit + ", timeDiffSecond=" + timeDiffSecond
				+ ", ipType=" + ipType + "]";
	}
	
}
