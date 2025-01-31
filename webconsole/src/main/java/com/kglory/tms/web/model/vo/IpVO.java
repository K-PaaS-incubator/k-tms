package com.kglory.tms.web.model.vo;

public class IpVO {
	
	private String ipInput;
	private String	ip;
	private Float	bps;
	private Float	pps;
	private long	sumBps;
	private long	sumPps;
	private long	totalRowSize;
	private long	rNum;
	
	
	private String protocol;
	private int port;
	private Float srcBps;
	private Float dstBps;
	private Float srcPps;
	private Float dstPps;
	
	
	public String getIpInput() {
		return ipInput;
	}

	public void setIpInput(String ipInput) {
		this.ipInput = ipInput;
	}

	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Float getBps() {
		return bps;
	}
	
	public void setBps(Float bps) {
		this.bps = bps;
	}
	
	public Float getPps() {
		return pps;
	}
	
	public void setPps(Float pps) {
		this.pps = pps;
	}
	
	public long getSumBps() {
		return sumBps;
	}
	
	public void setSumBps(long sumBps) {
		this.sumBps = sumBps;
	}
	
	public long getSumPps() {
		return sumPps;
	}
	
	public void setSumPps(long sumPps) {
		this.sumPps = sumPps;
	}
	
	public long getTotalRowSize() {
		return totalRowSize;
	}
	
	public void setTotalRowSize(long totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	
	public long getrNum() {
		return rNum;
	}
	
	public void setrNum(long rNum) {
		this.rNum = rNum;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Float getSrcBps() {
		return srcBps;
	}

	public void setSrcBps(Float srcBps) {
		this.srcBps = srcBps;
	}

	public Float getDstBps() {
		return dstBps;
	}

	public void setDstBps(Float dstBps) {
		this.dstBps = dstBps;
	}

	public Float getSrcPps() {
		return srcPps;
	}

	public void setSrcPps(Float srcPps) {
		this.srcPps = srcPps;
	}

	public Float getDstPps() {
		return dstPps;
	}

	public void setDstPps(Float dstPps) {
		this.dstPps = dstPps;
	}

	@Override
	public String toString() {
		return "IpVO [ip=" + ip + ", bps=" + bps + ", pps=" + pps + ", sumBps="
				+ sumBps + ", sumPps=" + sumPps + ", totalRowSize="
				+ totalRowSize + ", rNum=" + rNum + ", protocol=" + protocol
				+ ", port=" + port + ", srcBps=" + srcBps + ", dstBps="
				+ dstBps + ", srcPps=" + srcPps + ", dstPps=" + dstPps + "]";
	}
	
}
