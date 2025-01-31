package com.kglory.tms.web.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class ServiceDto extends SearchDto {
	long	nprotocol;
	long	wservice;
	BigDecimal	dblbps;
	BigDecimal	sumDblbps;
	BigDecimal	totalbps;
	
	String	tableName;
	String	tableDate;
	
	String	tmstart;
	String	tmend;
	
	long	topN;
	List<String> 		tableNames;
	
	public long getNprotocol() {
		return nprotocol;
	}
	
	public void setNprotocol(long nprotocol) {
		this.nprotocol = nprotocol;
	}
	
	public long getWservice() {
		return wservice;
	}
	
	public void setWservice(long wservice) {
		this.wservice = wservice;
	}
	
	public BigDecimal getDblbps() {
		return dblbps;
	}

	public void setDblbps(BigDecimal dblbps) {
		this.dblbps = dblbps;
	}

	public BigDecimal getSumDblbps() {
		return sumDblbps;
	}

	public void setSumDblbps(BigDecimal sumDblbps) {
		this.sumDblbps = sumDblbps;
	}

	public BigDecimal getTotalbps() {
		return totalbps;
	}

	public void setTotalbps(BigDecimal totalbps) {
		this.totalbps = totalbps;
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

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	@Override
	public String toString() {
		return "ServiceDto [nprotocol=" + nprotocol + ", wservice=" + wservice
				+ ", dblbps=" + dblbps + ", sumDblbps=" + sumDblbps
				+ ", totalbps=" + totalbps + ", tableName=" + tableName
				+ ", tableDate=" + tableDate + ", tmstart=" + tmstart
				+ ", tmend=" + tmend + ", topN=" + topN + ", tableNames="
				+ tableNames + "]";
	}
}
