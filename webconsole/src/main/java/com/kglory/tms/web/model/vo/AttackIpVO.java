package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class AttackIpVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 5048314996892878188L;

	private BigInteger			rNum;
	private BigInteger			lCode;
	private BigInteger			nSum;
	private BigDecimal			bps;
	private BigDecimal			pps;
	private BigInteger			sPortCount;
	private BigInteger			destIpCount;
	private BigInteger			sLcodeCount;
	private Integer				bSeverity;
	private String				dwSourceIp;
	private String				strNationIso;
	private Integer				nDestPort;
	
	private BigInteger			prevRNum;	
	private BigInteger			prevNSum;
	private BigDecimal			prevBps;
	private BigDecimal			prevPps;
	
	private BigInteger			totalRowSize;
	private BigInteger			totalNSum;
	
	public BigInteger getrNum() {
		return rNum;
	}
	
	public void setrNum(BigInteger rNum) {
		this.rNum = rNum;
	}
	
	public BigInteger getlCode() {
		return lCode;
	}
	
	public void setlCode(BigInteger lCode) {
		this.lCode = lCode;
	}
	
	public BigInteger getnSum() {
		return nSum;
	}
	
	public void setnSum(BigInteger nSum) {
		this.nSum = nSum;
	}
	
	public BigDecimal getBps() {
		return bps;
	}
	
	public void setBps(BigDecimal bps) {
		this.bps = bps;
	}
	
	public BigDecimal getPps() {
		return pps;
	}
	
	public void setPps(BigDecimal pps) {
		this.pps = pps;
	}
	
	public BigInteger getsPortCount() {
		return sPortCount;
	}
	
	public void setsPortCount(BigInteger sPortCount) {
		this.sPortCount = sPortCount;
	}
	
	public BigInteger getDestIpCount() {
		return destIpCount;
	}
	
	public void setDestIpCount(BigInteger destIpCount) {
		this.destIpCount = destIpCount;
	}
	
	public BigInteger getsLcodeCount() {
		return sLcodeCount;
	}
	
	public void setsLcodeCount(BigInteger sLcodeCount) {
		this.sLcodeCount = sLcodeCount;
	}
	
	public Integer getbSeverity() {
		return bSeverity;
	}
	
	public void setbSeverity(Integer bSeverity) {
		this.bSeverity = bSeverity;
	}
	
	public String getDwSourceIp() {
		return dwSourceIp;
	}
	
	public void setDwSourceIp(String dwSourceIp) {
		this.dwSourceIp = dwSourceIp;
	}
	
	public String getStrNationIso() {
		return strNationIso;
	}
	
	public void setStrNationIso(String strNationIso) {
		this.strNationIso = strNationIso;
	}
	
	public Integer getnDestPort() {
		return nDestPort;
	}
	
	public void setnDestPort(Integer nDestPort) {
		this.nDestPort = nDestPort;
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
	
	public BigInteger getPrevRNum() {
		return prevRNum;
	}

	public void setPrevRNum(BigInteger prevRNum) {
		this.prevRNum = prevRNum;
	}

	public BigInteger getPrevNSum() {
		return prevNSum;
	}

	public void setPrevNSum(BigInteger prevNSum) {
		this.prevNSum = prevNSum;
	}

	public BigDecimal getPrevBps() {
		return prevBps;
	}

	public void setPrevBps(BigDecimal prevBps) {
		this.prevBps = prevBps;
	}

	public BigDecimal getPrevPps() {
		return prevPps;
	}

	public void setPrevPps(BigDecimal prevPps) {
		this.prevPps = prevPps;
	}

	@Override
	public String toString() {
		return "AttackIpVO [rNum=" + rNum + ", lCode=" + lCode + ", nSum="
				+ nSum + ", bps=" + bps + ", pps=" + pps + ", sPortCount="
				+ sPortCount + ", destIpCount=" + destIpCount
				+ ", sLcodeCount=" + sLcodeCount + ", bSeverity=" + bSeverity
				+ ", dwSourceIp=" + dwSourceIp + ", strNationIso="
				+ strNationIso + ", nDestPort=" + nDestPort + ", prevRNum="
				+ prevRNum + ", prevNSum=" + prevNSum + ", prevBps=" + prevBps
				+ ", prevPps=" + prevPps + ", totalRowSize=" + totalRowSize
				+ ", totalNSum=" + totalNSum + "]";
	}
	
}
