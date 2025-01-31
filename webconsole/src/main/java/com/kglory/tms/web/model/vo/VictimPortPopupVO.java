package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class VictimPortPopupVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 7818704369854867911L;

	private Long				rNum;
	private Integer				nProtocol;
	private Integer				nDestPort;
	private Integer				sLcodeCount;
	private BigInteger			srcIpCount;
	private BigInteger			destIpCount;
	private BigInteger			nSum;
	private BigInteger			totalRowSize;
	private BigInteger			totalNSum;
	private BigDecimal			bps;
	
	private String				startDate;
	private String				endDate;
	
	public Long getrNum() {
		return rNum;
	}
	
	public void setrNum(Long rNum) {
		this.rNum = rNum;
	}
	
	public Integer getnProtocol() {
		return nProtocol;
	}
	
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}
	
	public Integer getnDestPort() {
		return nDestPort;
	}
	
	public void setnDestPort(Integer nDestPort) {
		this.nDestPort = nDestPort;
	}
	
	public Integer getsLcodeCount() {
		return sLcodeCount;
	}
	
	public void setsLcodeCount(Integer sLcodeCount) {
		this.sLcodeCount = sLcodeCount;
	}
	
	public BigInteger getSrcIpCount() {
		return srcIpCount;
	}
	
	public void setSrcIpCount(BigInteger srcIpCount) {
		this.srcIpCount = srcIpCount;
	}
	
	public BigInteger getDestIpCount() {
		return destIpCount;
	}
	
	public void setDestIpCount(BigInteger destIpCount) {
		this.destIpCount = destIpCount;
	}
	
	public BigInteger getnSum() {
		return nSum;
	}
	
	public void setnSum(BigInteger nSum) {
		this.nSum = nSum;
	}
	
	public BigInteger getTotalRowSize() {
		return totalRowSize;
	}
	
	public void setTotalRowSize(BigInteger totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	
	public BigInteger getTotalNSum() {
		return totalNSum;
	}
	
	public void setTotalNSum(BigInteger totalNSum) {
		this.totalNSum = totalNSum;
	}
	
	public BigDecimal getBps() {
		return bps;
	}
	
	public void setBps(BigDecimal bps) {
		this.bps = bps;
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
	
	@Override
	public String toString() {
		return "VictimPortPopupVO [rNum=" + rNum + ", nProtocol=" + nProtocol + ", nDestPort=" + nDestPort
				+ ", sLcodeCount=" + sLcodeCount + ", srcIpCount=" + srcIpCount + ", destIpCount=" + destIpCount
				+ ", nSum=" + nSum + ", totalRowSize=" + totalRowSize + ", totalNSum=" + totalNSum + ", bps=" + bps
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
}
