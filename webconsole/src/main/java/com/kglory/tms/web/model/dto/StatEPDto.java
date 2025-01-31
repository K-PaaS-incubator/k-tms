package com.kglory.tms.web.model.dto;

import java.util.List;

public class StatEPDto extends SearchDto{
	long	lcode;
	String	strTitle;
	long	eventCount;
	long	totalCount;
	
	String	tableName;
	String	tableDate;
	
	long	topN;
	
	String	tmstart;
	String	tmend;
	
	long 	timeHour;
	
	long	eventDblbps;
	long	dblbps;
	
	String	strname;
	
	String	protocolTableName;
	List<String> protocolTableNames;
	String	statEPTableName;
	List<String> statEPTableNames;
	String	statVEPTableName;
	public String getStatVEPTableName() {
		return statVEPTableName;
	}

	public void setStatVEPTableName(String statVEPTableName) {
		this.statVEPTableName = statVEPTableName;
	}

	public List<String> getStatVEPTableNames() {
		return statVEPTableNames;
	}

	public void setStatVEPTableNames(List<String> statVEPTableNames) {
		this.statVEPTableNames = statVEPTableNames;
	}

	List<String> statVEPTableNames;
	
	public long getLcode() {
		return lcode;
	}
	
	public void setLcode(long lcode) {
		this.lcode = lcode;
	}
	
	public String getStrTitle() {
		return strTitle;
	}
	
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	
	public long getEventCount() {
		return eventCount;
	}
	
	public void setEventCount(long eventCount) {
		this.eventCount = eventCount;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableDate() {
		return tableDate;
	}
	
	public void setTableDate(String tableDate) {
		this.tableDate = tableDate;
	}
	
	public long getTopN() {
		return topN;
	}
	
	public void setTopN(long topN) {
		this.topN = topN;
	}
	
	public String getTmstart() {
		return tmstart;
	}
	
	public void setTmstart(String tmstart) {
		this.tmstart = tmstart;
	}
	
	public String getTmend() {
		return tmend;
	}
	
	public void setTmend(String tmend) {
		this.tmend = tmend;
	}
	
	public long getEventDblbps() {
		return eventDblbps;
	}
	
	public void setEventDblbps(long eventDblbps) {
		this.eventDblbps = eventDblbps;
	}
	
	public long getDblbps() {
		return dblbps;
	}
	
	public void setDblbps(long dblbps) {
		this.dblbps = dblbps;
	}
	
	public String getStrname() {
		return strname;
	}
	
	public void setStrname(String strname) {
		this.strname = strname;
	}
	
	public String getProtocolTableName() {
		return protocolTableName;
	}
	
	public void setProtocolTableName(String protocolTableName) {
		this.protocolTableName = protocolTableName;
	}
	
	public String getStatEPTableName() {
		return statEPTableName;
	}
	
	public void setStatEPTableName(String statEPTableName) {
		this.statEPTableName = statEPTableName;
	}
	
	public long getTimeHour() {
		return timeHour;
	}

	public void setTimeHour(long timeHour) {
		this.timeHour = timeHour;
	}

	public List<String> getProtocolTableNames() {
		return protocolTableNames;
	}

	public void setProtocolTableNames(List<String> protocolTableNames) {
		this.protocolTableNames = protocolTableNames;
	}

	public List<String> getStatEPTableNames() {
		return statEPTableNames;
	}

	public void setStatEPTableNames(List<String> statEPTableNames) {
		this.statEPTableNames = statEPTableNames;
	}

	@Override
	public String toString() {
		return "StatEPDto [lcode=" + lcode + ", strTitle=" + strTitle
				+ ", eventCount=" + eventCount + ", totalCount=" + totalCount
				+ ", tableName=" + tableName + ", tableDate=" + tableDate
				+ ", topN=" + topN + ", tmstart=" + tmstart + ", tmend="
				+ tmend + ", timeHour=" + timeHour + ", eventDblbps="
				+ eventDblbps + ", dblbps=" + dblbps + ", strname=" + strname
				+ ", protocolTableName=" + protocolTableName
				+ ", protocolTableNames=" + protocolTableNames
				+ ", statEPTableName=" + statEPTableName
				+ ", statEPTableNames=" + statEPTableNames
				+ ", statVEPTableName=" + statVEPTableName
				+ ", statVEPTableNames=" + statVEPTableNames + "]";
	}
}
