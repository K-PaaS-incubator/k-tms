package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class ThreatPolicyVO extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4764694664879521852L;

	private long lIndex;
	private String strName;
	private long lSetupIndex;
	private long lvsensorIndex;
	private long lnetworkIndex;
	
	private long  nLowWeight;
	private long  nMidWeight;
	private long  nHighWeight;
	private long  nSecondGrade;
	private long  nThirdGrade;
	private long  nFourthGrade;
	private long  nFifthGrade;
	public long getlIndex() {
		return lIndex;
	}
	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
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
	public long getLnetworkIndex() {
		return lnetworkIndex;
	}
	public void setLnetworkIndex(long lnetworkIndex) {
		this.lnetworkIndex = lnetworkIndex;
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
	@Override
	public String toString() {
		return "ThreatPolicyVO [lIndex=" + lIndex + ", strName=" + strName
				+ ", lSetupIndex=" + lSetupIndex + ", lvsensorIndex="
				+ lvsensorIndex + ", lnetworkIndex=" + lnetworkIndex
				+ ", nLowWeight=" + nLowWeight + ", nMidWeight=" + nMidWeight
				+ ", nHighWeight=" + nHighWeight + ", nSecondGrade="
				+ nSecondGrade + ", nThirdGrade=" + nThirdGrade
				+ ", nFourthGrade=" + nFourthGrade + ", nFifthGrade="
				+ nFifthGrade + "]";
	}
}