package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class VictimIpVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 5809217094773455213L;

	private BigInteger			rNum;
	private BigInteger			lCode;
	private String				dwDestinationIp;
	private String				strDestinationIp;
	private Byte				bSeverity;
	private Integer				nDestPort;
	private BigInteger			sLcodeCount;
	private BigInteger			srcIpCount;
	private BigInteger			sPortCount;
	private Integer				nProtocol;
	private BigInteger			nSum;
	private BigDecimal			bps;
	private BigDecimal			pps;
	private String				strNationIso;
	private BigInteger			totalRowSize;
	private BigInteger			totalNSum;
	private BigDecimal			totalBps;
	
	private BigInteger			prevRNum;	
	private BigInteger			prevNSum;
	private BigDecimal			prevBps;
	private BigDecimal			prevPps;
	
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
	
	public String getDwDestinationIp() {
		return dwDestinationIp;
	}
	
	public void setDwDestinationIp(String dwDestinationIp) {
		this.dwDestinationIp = dwDestinationIp;
	}
	
	public Byte getbSeverity() {
		return bSeverity;
	}
	
	public void setbSeverity(Byte bSeverity) {
		this.bSeverity = bSeverity;
	}
	
	public Integer getnDestPort() {
		return nDestPort;
	}
	
	public void setnDestPort(Integer nDestPort) {
		this.nDestPort = nDestPort;
	}
	
	public BigInteger getsLcodeCount() {
		return sLcodeCount;
	}
	
	public void setsLcodeCount(BigInteger sLcodeCount) {
		this.sLcodeCount = sLcodeCount;
	}
	
	public BigInteger getSrcIpCount() {
		return srcIpCount;
	}
	
	public void setSrcIpCount(BigInteger srcIpCount) {
		this.srcIpCount = srcIpCount;
	}
	
	public BigInteger getsPortCount() {
		return sPortCount;
	}
	
	public void setsPortCount(BigInteger sPortCount) {
		this.sPortCount = sPortCount;
	}
	
	public Integer getnProtocol() {
		return nProtocol;
	}
	
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
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
	
	public String getStrNationIso() {
		return strNationIso;
	}
	
	public void setStrNationIso(String strNationIso) {
		this.strNationIso = strNationIso;
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


	public BigDecimal getTotalBps() {
		return totalBps;
	}

	public void setTotalBps(BigDecimal totalBps) {
		this.totalBps = totalBps;
	}

	public String getStrDestinationIp() {
		return strDestinationIp;
	}

	public void setStrDestinationIp(String strDestinationIp) {
		this.strDestinationIp = strDestinationIp;
	}

	@Override
	public String toString() {
		return "VictimIpVO [rNum=" + rNum + ", lCode=" + lCode
				+ ", dwDestinationIp=" + dwDestinationIp
				+ ", strDestinationIp=" + strDestinationIp + ", bSeverity="
				+ bSeverity + ", nDestPort=" + nDestPort + ", sLcodeCount="
				+ sLcodeCount + ", srcIpCount=" + srcIpCount + ", sPortCount="
				+ sPortCount + ", nProtocol=" + nProtocol + ", nSum=" + nSum
				+ ", bps=" + bps + ", pps=" + pps + ", strNationIso="
				+ strNationIso + ", totalRowSize=" + totalRowSize
				+ ", totalNSum=" + totalNSum + ", totalBps=" + totalBps
				+ ", prevRNum=" + prevRNum + ", prevNSum=" + prevNSum
				+ ", prevBps=" + prevBps + ", prevPps=" + prevPps + "]";
	}
	
}