package com.kglory.tms.web.model.dto;

public class SnmpTrans {
	Integer lIndex;
	Integer nEventType;
	Integer nMainType;
	Integer nSubType1;
	Integer nSubType2;
	
	public Integer getlIndex() {
		return lIndex;
	}
	public void setlIndex(Integer lIndex) {
		this.lIndex = lIndex;
	}
	public Integer getnEventType() {
		return nEventType;
	}
	public void setnEventType(Integer nEventType) {
		this.nEventType = nEventType;
	}
	public Integer getnMainType() {
		return nMainType;
	}
	public void setnMainType(Integer nMainType) {
		this.nMainType = nMainType;
	}
	public Integer getnSubType1() {
		return nSubType1;
	}
	public void setnSubType1(Integer nSubType1) {
		this.nSubType1 = nSubType1;
	}
	
	public Integer getnSubType2() {
		return nSubType2;
	}
	public void setnSubType2(Integer nSubType2) {
		this.nSubType2 = nSubType2;
	}
	@Override
	public String toString() {
		return "SnmpTrans [lIndex=" + lIndex + ", nEventType=" + nEventType
				+ ", nMainType=" + nMainType + ", nSubType1=" + nSubType1
				+ ", nSubType2=" + nSubType2 + "]";
	}

	
}
