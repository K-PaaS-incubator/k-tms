package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.NumberUtil;

public class SessionTrafficVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 5048314996892878188L;

	private long				rNum				= 0;
	private long				totalRowSize		= 0;
	private String				startDate			= "";
	private String				endDate				= "";
	private String				server;
	private long				serverPort;
	private long				wserverPort;
	private String 				strServerPort;
	private long				countClientIp;
	private String				client;
	private long				countServerIp;
	private long				serverBytes;
	private long				clientBytes;
	private long				totalBytes;
	private long				count;
	private long				totalNum;
	private long				tot;
	private String				tmstart;
	
	public long getrNum() {
		return rNum;
	}
	
	public void setrNum(long rNum) {
		this.rNum = rNum;
	}
	
	public long getTotalRowSize() {
		return totalRowSize;
	}
	
	public void setTotalRowSize(long totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public long getServerPort() {
		return serverPort;
	}
	
	public void setServerPort(long serverPort) {
            if (serverPort < 0L) {
                this.serverPort = NumberUtil.portSignedToLong(serverPort);
            } else {
		this.serverPort = serverPort;
            }
	}
	
	public long getWserverPort() {
		return wserverPort;
	}

	public void setWserverPort(long wserverPort) {
            if (wserverPort < 0L) {
                this.wserverPort = NumberUtil.portSignedToLong(wserverPort);
            } else {
		this.wserverPort = wserverPort;
            }
	}

	public String getStrServerPort() {
		return strServerPort;
	}

	public void setStrServerPort(String strServerPort) {
		this.strServerPort = strServerPort;
	}

	public long getCountClientIp() {
		return countClientIp;
	}
	
	public void setCountClientIp(long countClientIp) {
		this.countClientIp = countClientIp;
	}
	
	public String getClient() {
		return client;
	}
	
	public void setClient(String client) {
		this.client = client;
	}
	
	public long getCountServerIp() {
		return countServerIp;
	}
	
	public void setCountServerIp(long countServerIp) {
		this.countServerIp = countServerIp;
	}
	
	public long getServerBytes() {
		return serverBytes;
	}
	
	public void setServerBytes(long serverBytes) {
		this.serverBytes = serverBytes;
	}
	
	public long getClientBytes() {
		return clientBytes;
	}
	
	public void setClientBytes(long clientBytes) {
		this.clientBytes = clientBytes;
	}
	
	public long getTotalBytes() {
		return totalBytes;
	}
	
	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}
	
	public long getCount() {
		return count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public long getTotalNum() {
		return totalNum;
	}
	
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}
	
	public long getTot() {
		return tot;
	}
	
	public void setTot(long tot) {
		this.tot = tot;
	}

	public String getTmstart() {
		return tmstart;
	}

	public void setTmstart(String tmstart) {
		this.tmstart = tmstart;
	}

	@Override
	public String toString() {
		return "SessionTrafficVO [rNum=" + rNum + ", totalRowSize="
				+ totalRowSize + ", startDate=" + startDate + ", endDate="
				+ endDate + ", server=" + server + ", serverPort=" + serverPort
				+ ", wserverPort=" + wserverPort + ", countClientIp="
				+ countClientIp + ", client=" + client + ", countServerIp="
				+ countServerIp + ", serverBytes=" + serverBytes
				+ ", clientBytes=" + clientBytes + ", totalBytes=" + totalBytes
				+ ", count=" + count + ", totalNum=" + totalNum + ", tot="
				+ tot + ", tmstart=" + tmstart + "]";
	}
	
}
