package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class AttackVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 5048314996892878188L;

	private BigInteger			rNum;
	private BigInteger			lCode;
	private String				strTitle;
	private BigInteger			nSum;
	private BigDecimal			bps;
	private BigDecimal			pps;
	private BigInteger			sPortCount;
	
	private BigInteger			prevRNum;	
	private BigInteger			prevNSum;
	private BigDecimal			prevBps;
	private BigDecimal			prevPps;
	
	private BigInteger			destIpCount;
	private BigInteger			srcIpCount;
	private BigInteger			totalRowSize;
	private BigInteger			totalNSum;
	
	private Integer				bSeverity;
	
	private BigInteger			cntInfo;
	private BigInteger			cntLow;
	private BigInteger			cntMed;
	private BigInteger			cntHigh;
	
	private String				startDate;
	private String				endDate;
	
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
	
	public String getStrTitle() {
		return strTitle;
	}
	
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
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
	
	public BigInteger getSrcIpCount() {
		return srcIpCount;
	}
	
	public void setSrcIpCount(BigInteger srcIpCount) {
		this.srcIpCount = srcIpCount;
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
	
	public Integer getbSeverity() {
		return bSeverity;
	}
	
	public void setbSeverity(Integer bSeverity) {
		this.bSeverity = bSeverity;
	}
	
	public BigInteger getCntInfo() {
		return cntInfo;
	}
	
	public void setCntInfo(BigInteger cntInfo) {
		this.cntInfo = cntInfo;
	}
	
	public BigInteger getCntLow() {
		return cntLow;
	}
	
	public void setCntLow(BigInteger cntLow) {
		this.cntLow = cntLow;
	}
	
	public BigInteger getCntMed() {
		return cntMed;
	}
	
	public void setCntMed(BigInteger cntMed) {
		this.cntMed = cntMed;
	}
	
	public BigInteger getCntHigh() {
		return cntHigh;
	}
	
	public void setCntHigh(BigInteger cntHigh) {
		this.cntHigh = cntHigh;
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
		return "AttackVO [rNum=" + rNum + ", prevRNum=" + prevRNum + ", lCode="
				+ lCode + ", strTitle=" + strTitle + ", nSum=" + nSum
				+ ", bps=" + bps + ", pps=" + pps + ", sPortCount="
				+ sPortCount + ", prevNSum=" + prevNSum + ", prevBps="
				+ prevBps + ", prevPps=" + prevPps + ", destIpCount="
				+ destIpCount + ", srcIpCount=" + srcIpCount
				+ ", totalRowSize=" + totalRowSize + ", totalNSum=" + totalNSum
				+ ", bSeverity=" + bSeverity + ", cntInfo=" + cntInfo
				+ ", cntLow=" + cntLow + ", cntMed=" + cntMed + ", cntHigh="
				+ cntHigh + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}
	
}
