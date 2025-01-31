package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class MaliciousTrafficVO extends CommonBean implements Serializable {

	/**
	 * @author leekyunghee
	 */
	private static final long serialVersionUID = 6984063240402704560L;

	private BigInteger			rNum;
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
	private BigDecimal			malBps;
	private BigDecimal			malPps;	
	private BigDecimal			malCps;
	private BigDecimal			malIngressBps;
	private BigDecimal			malEgressBps;
	private BigDecimal			malIngressPps;
	private BigDecimal			malEgressPps;
	private BigDecimal			malIngressCps;
	private BigDecimal			malEgressCps;
	
	private BigDecimal			prevBps;
	private BigDecimal			prevPps;
	private BigDecimal			prevCps;
	private BigDecimal			prevIngressBps;
	private BigDecimal			prevEgressBps;
	private BigDecimal			prevIngressPps;
	private BigDecimal			prevEgressPps;
	private BigDecimal			prevIngressCps;
	private BigDecimal			prevEgressCps;
	
	private BigDecimal			malPrevBps;
	private BigDecimal			malPrevPps;
	private BigDecimal			malPrevCps;
	private BigDecimal			malPrevIngressBps;
	private BigDecimal			malPrevEgressBps;
	private BigDecimal			malPrevIngressPps;
	private BigDecimal			malPrevEgressPps;
	private BigDecimal			malPrevIngressCps;
	private BigDecimal			malPrevEgressCps;
	
	public BigInteger getrNum() {
		return rNum;
	}
	public void setrNum(BigInteger rNum) {
		this.rNum = rNum;
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
	public long getTotalRowSize() {
		return totalRowSize;
	}
	public void setTotalRowSize(long totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	public String getnProtocol() {
		return nProtocol;
	}
	public void setnProtocol(String nProtocol) {
		this.nProtocol = nProtocol;
	}
	public BigDecimal getCps() {
		return cps;
	}
	public void setCps(BigDecimal cps) {
		this.cps = cps;
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
	public BigDecimal getMalBps() {
		return malBps;
	}
	public void setMalBps(BigDecimal malBps) {
		this.malBps = malBps;
	}
	public BigDecimal getMalPps() {
		return malPps;
	}
	public void setMalPps(BigDecimal malPps) {
		this.malPps = malPps;
	}
	public BigDecimal getMalCps() {
		return malCps;
	}
	public void setMalCps(BigDecimal malCps) {
		this.malCps = malCps;
	}
	public BigDecimal getMalIngressBps() {
		return malIngressBps;
	}
	public void setMalIngressBps(BigDecimal malIngressBps) {
		this.malIngressBps = malIngressBps;
	}
	public BigDecimal getMalEgressBps() {
		return malEgressBps;
	}
	public void setMalEgressBps(BigDecimal malEgressBps) {
		this.malEgressBps = malEgressBps;
	}
	public BigDecimal getMalIngressPps() {
		return malIngressPps;
	}
	public void setMalIngressPps(BigDecimal malIngressPps) {
		this.malIngressPps = malIngressPps;
	}
	public BigDecimal getMalEgressPps() {
		return malEgressPps;
	}
	public void setMalEgressPps(BigDecimal malEgressPps) {
		this.malEgressPps = malEgressPps;
	}
	public BigDecimal getMalIngressCps() {
		return malIngressCps;
	}
	public void setMalIngressCps(BigDecimal malIngressCps) {
		this.malIngressCps = malIngressCps;
	}
	public BigDecimal getMalEgressCps() {
		return malEgressCps;
	}
	public void setMalEgressCps(BigDecimal malEgressCps) {
		this.malEgressCps = malEgressCps;
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
	public BigDecimal getMalPrevBps() {
		return malPrevBps;
	}
	public void setMalPrevBps(BigDecimal malPrevBps) {
		this.malPrevBps = malPrevBps;
	}
	public BigDecimal getMalPrevPps() {
		return malPrevPps;
	}
	public void setMalPrevPps(BigDecimal malPrevPps) {
		this.malPrevPps = malPrevPps;
	}
	public BigDecimal getMalPrevCps() {
		return malPrevCps;
	}
	public void setMalPrevCps(BigDecimal malPrevCps) {
		this.malPrevCps = malPrevCps;
	}
	public BigDecimal getMalPrevIngressBps() {
		return malPrevIngressBps;
	}
	public void setMalPrevIngressBps(BigDecimal malPrevIngressBps) {
		this.malPrevIngressBps = malPrevIngressBps;
	}
	public BigDecimal getMalPrevEgressBps() {
		return malPrevEgressBps;
	}
	public void setMalPrevEgressBps(BigDecimal malPrevEgressBps) {
		this.malPrevEgressBps = malPrevEgressBps;
	}
	public BigDecimal getMalPrevIngressPps() {
		return malPrevIngressPps;
	}
	public void setMalPrevIngressPps(BigDecimal malPrevIngressPps) {
		this.malPrevIngressPps = malPrevIngressPps;
	}
	public BigDecimal getMalPrevEgressPps() {
		return malPrevEgressPps;
	}
	public void setMalPrevEgressPps(BigDecimal malPrevEgressPps) {
		this.malPrevEgressPps = malPrevEgressPps;
	}
	public BigDecimal getMalPrevIngressCps() {
		return malPrevIngressCps;
	}
	public void setMalPrevIngressCps(BigDecimal malPrevIngressCps) {
		this.malPrevIngressCps = malPrevIngressCps;
	}
	public BigDecimal getMalPrevEgressCps() {
		return malPrevEgressCps;
	}
	public void setMalPrevEgressCps(BigDecimal malPrevEgressCps) {
		this.malPrevEgressCps = malPrevEgressCps;
	}
}
