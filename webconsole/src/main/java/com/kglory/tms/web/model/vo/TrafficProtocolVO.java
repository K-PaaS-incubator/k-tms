package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class TrafficProtocolVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= -2672487273179299188L;

	
	private String				strName;
	private String				ucType;
	private String				nProtocol;
	private BigDecimal			bps;
	private BigDecimal			pps;
	private BigDecimal			cps;
	private long				totalRowSize;
	private BigDecimal			ingressBps;
	private BigDecimal			egressBps;
	private BigDecimal			ingressPps;
	private BigDecimal			egressPps;
	private BigDecimal			ingressCps;
	private BigDecimal			egressCps;
	
	private BigDecimal			prevBps;
	private BigDecimal			prevPps;
	private BigDecimal			prevCps;
	private BigDecimal			prevIngressBps;
	private BigDecimal			prevEgressBps;
	private BigDecimal			prevIngressPps;
	private BigDecimal			prevEgressPps;
	private BigDecimal			prevIngressCps;
	private BigDecimal			prevEgressCps;
	
	private BigInteger			rNum;
	private BigInteger			prevRNum;	
	
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getUcType() {
		return ucType;
	}
	public void setUcType(String ucType) {
		this.ucType = ucType;
	}
	public String getnProtocol() {
		return nProtocol;
	}
	public void setnProtocol(String nProtocol) {
		this.nProtocol = nProtocol;
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
	public BigDecimal getCps() {
		return cps;
	}
	public void setCps(BigDecimal cps) {
		this.cps = cps;
	}
	public long getTotalRowSize() {
		return totalRowSize;
	}
	public void setTotalRowSize(long totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	public BigDecimal getIngressBps() {
		return ingressBps;
	}
	public void setIngressBps(BigDecimal ingressBps) {
		this.ingressBps = ingressBps;
	}
	public BigDecimal getEgressBps() {
		return egressBps;
	}
	public void setEgressBps(BigDecimal egressBps) {
		this.egressBps = egressBps;
	}
	public BigDecimal getIngressPps() {
		return ingressPps;
	}
	public void setIngressPps(BigDecimal ingressPps) {
		this.ingressPps = ingressPps;
	}
	public BigDecimal getEgressPps() {
		return egressPps;
	}
	public void setEgressPps(BigDecimal egressPps) {
		this.egressPps = egressPps;
	}
	public BigDecimal getIngressCps() {
		return ingressCps;
	}
	public void setIngressCps(BigDecimal ingressCps) {
		this.ingressCps = ingressCps;
	}
	public BigDecimal getEgressCps() {
		return egressCps;
	}
	public void setEgressCps(BigDecimal egressCps) {
		this.egressCps = egressCps;
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
	public BigDecimal getPrevCps() {
		return prevCps;
	}
	public void setPrevCps(BigDecimal prevCps) {
		this.prevCps = prevCps;
	}
	public BigDecimal getPrevIngressBps() {
		return prevIngressBps;
	}
	public void setPrevIngressBps(BigDecimal prevIngressBps) {
		this.prevIngressBps = prevIngressBps;
	}
	public BigDecimal getPrevEgressBps() {
		return prevEgressBps;
	}
	public void setPrevEgressBps(BigDecimal prevEgressBps) {
		this.prevEgressBps = prevEgressBps;
	}
	public BigDecimal getPrevIngressPps() {
		return prevIngressPps;
	}
	public void setPrevIngressPps(BigDecimal prevIngressPps) {
		this.prevIngressPps = prevIngressPps;
	}
	public BigDecimal getPrevEgressPps() {
		return prevEgressPps;
	}
	public void setPrevEgressPps(BigDecimal prevEgressPps) {
		this.prevEgressPps = prevEgressPps;
	}
	public BigDecimal getPrevIngressCps() {
		return prevIngressCps;
	}
	public void setPrevIngressCps(BigDecimal prevIngressCps) {
		this.prevIngressCps = prevIngressCps;
	}
	public BigDecimal getPrevEgressCps() {
		return prevEgressCps;
	}
	public void setPrevEgressCps(BigDecimal prevEgressCps) {
		this.prevEgressCps = prevEgressCps;
	}
	
	public BigInteger getrNum() {
		return rNum;
	}
	public void setrNum(BigInteger rNum) {
		this.rNum = rNum;
	}
	public BigInteger getPrevRNum() {
		return prevRNum;
	}
	public void setPrevRNum(BigInteger prevRNum) {
		this.prevRNum = prevRNum;
	}
	@Override
	public String toString() {
		return "TrafficProtocolVO [strName=" + strName + ", ucType=" + ucType
				+ ", nProtocol=" + nProtocol + ", bps=" + bps + ", pps=" + pps
				+ ", cps=" + cps + ", totalRowSize=" + totalRowSize
				+ ", ingressBps=" + ingressBps + ", egressBps=" + egressBps
				+ ", ingressPps=" + ingressPps + ", egressPps=" + egressPps
				+ ", ingressCps=" + ingressCps + ", egressCps=" + egressCps
				+ ", prevBps=" + prevBps + ", prevPps=" + prevPps
				+ ", prevCps=" + prevCps + ", prevIngressBps=" + prevIngressBps
				+ ", prevEgressBps=" + prevEgressBps + ", prevIngressPps="
				+ prevIngressPps + ", prevEgressPps=" + prevEgressPps
				+ ", prevIngressCps=" + prevIngressCps + ", prevEgressCps="
				+ prevEgressCps + ", rNum=" + rNum + ", prevRNum=" + prevRNum
				+ "]";
	}
	
	
}
