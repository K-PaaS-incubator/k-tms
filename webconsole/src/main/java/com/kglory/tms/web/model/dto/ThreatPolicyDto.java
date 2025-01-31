package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

public class ThreatPolicyDto extends CommonBean {

	private String cudType;
	
	private long lSetupIndex;
	private long lvsensorIndex;
	private long lsensorIndex;
	private long lnetworkIndex;
	private long lnetGroupIndex;
	
	private long  lIndex;
	private long  nLowWeight;
	private long  nMidWeight;
	private long  nHighWeight;
	private long  nSecondGrade;
	private long  nThirdGrade;
	private long  nFourthGrade;
	private long  nFifthGrade;
	
	public long getlSetupIndex() {
		return lSetupIndex;
	}
	public void setlSetupIndex(long lSetupIndex) {
		this.lSetupIndex = lSetupIndex;
	}
	public long getLvsensorIndex() {
		return lvsensorIndex;
	}
	public void setLvsensorIndex(long lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}
	public long getLsensorIndex() {
		return lsensorIndex;
	}
	public void setLsensorIndex(long lsensorIndex) {
		this.lsensorIndex = lsensorIndex;
	}
	public long getLnetworkIndex() {
		return lnetworkIndex;
	}
	public void setLnetworkIndex(long lnetworkIndex) {
		this.lnetworkIndex = lnetworkIndex;
	}
	public long getLnetGroupIndex() {
		return lnetGroupIndex;
	}
	public void setLnetGroupIndex(long lnetGroupIndex) {
		this.lnetGroupIndex = lnetGroupIndex;
	}
	public long getlIndex() {
		return lIndex;
	}
	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
	}
	public long getnLowWeight() {
		return nLowWeight;
	}
	public void setnLowWeight(long nLowWeight) {
		this.nLowWeight = nLowWeight;
	}
	public long getnMidWeight() {
		return nMidWeight;
	}
	public void setnMidWeight(long nMidWeight) {
		this.nMidWeight = nMidWeight;
	}
	public long getnHighWeight() {
		return nHighWeight;
	}
	public void setnHighWeight(long nHighWeight) {
		this.nHighWeight = nHighWeight;
	}
	public long getnSecondGrade() {
		return nSecondGrade;
	}
	public void setnSecondGrade(long nSecondGrade) {
		this.nSecondGrade = nSecondGrade;
	}
	public long getnThirdGrade() {
		return nThirdGrade;
	}
	public void setnThirdGrade(long nThirdGrade) {
		this.nThirdGrade = nThirdGrade;
	}
	public long getnFourthGrade() {
		return nFourthGrade;
	}
	public void setnFourthGrade(long nFourthGrade) {
		this.nFourthGrade = nFourthGrade;
	}
	public long getnFifthGrade() {
		return nFifthGrade;
	}
	public void setnFifthGrade(long nFifthGrade) {
		this.nFifthGrade = nFifthGrade;
	}
	
	public String getCudType() {
		return cudType;
	}
	public void setCudType(String cudType) {
		this.cudType = cudType;
	}
	@Override
	public String toString() {
		return "ThreatPolicyDto [cudType=" + cudType + ", lSetupIndex="
				+ lSetupIndex + ", lvsensorIndex=" + lvsensorIndex
				+ ", lsensorIndex=" + lsensorIndex + ", lnetworkIndex="
				+ lnetworkIndex + ", lnetGroupIndex=" + lnetGroupIndex
				+ ", lIndex=" + lIndex + ", nLowWeight=" + nLowWeight
				+ ", nMidWeight=" + nMidWeight + ", nHighWeight=" + nHighWeight
				+ ", nSecondGrade=" + nSecondGrade + ", nThirdGrade="
				+ nThirdGrade + ", nFourthGrade=" + nFourthGrade
				+ ", nFifthGrade=" + nFifthGrade + "]";
	}

}
