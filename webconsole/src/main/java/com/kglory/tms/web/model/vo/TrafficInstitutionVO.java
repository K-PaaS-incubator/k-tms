package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kglory.tms.web.model.CommonBean;

public class TrafficInstitutionVO  extends CommonBean implements Serializable{

	private static final long serialVersionUID = 7199778554666026306L;

	private String 		strName;
	private String 		type;
	private Long 		lIndex;
	private Long 		lParentGroupIndex;
	private Long 		rNum;
	private String 		groupCode;
	private Long 		groupCodeCount;
	private Long 		rank;
	
	private BigDecimal 		pps;
	private BigDecimal 		bps;
	private BigDecimal 		cps;
	private BigDecimal 		ingressBps;
	private BigDecimal 		egressBps;
	private BigDecimal 		ingressPps;
	private BigDecimal 		egressPps;
	private BigDecimal 		ingressCps;
	private BigDecimal 		egressCps;
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getlIndex() {
		return lIndex;
	}
	public void setlIndex(Long lIndex) {
		this.lIndex = lIndex;
	}
	public Long getlParentGroupIndex() {
		return lParentGroupIndex;
	}
	public void setlParentGroupIndex(Long lParentGroupIndex) {
		this.lParentGroupIndex = lParentGroupIndex;
	}
	public Long getrNum() {
		return rNum;
	}
	public void setrNum(Long rNum) {
		this.rNum = rNum;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public Long getGroupCodeCount() {
		return groupCodeCount;
	}
	public void setGroupCodeCount(Long groupCodeCount) {
		this.groupCodeCount = groupCodeCount;
	}
	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
		this.rank = rank;
	}
	
	
	public BigDecimal getPps() {
		return pps;
	}
	public void setPps(BigDecimal pps) {
		this.pps = pps;
	}
	public BigDecimal getBps() {
		return bps;
	}
	public void setBps(BigDecimal bps) {
		this.bps = bps;
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
	@Override
	public String toString() {
		return "TrafficInstitutionVO [strName=" + strName + ", type=" + type
				+ ", lIndex=" + lIndex + ", lParentGroupIndex="
				+ lParentGroupIndex + ", rNum=" + rNum + ", groupCode="
				+ groupCode + ", groupCodeCount=" + groupCodeCount + ", rank="
				+ rank + "]";
	}
	
}
