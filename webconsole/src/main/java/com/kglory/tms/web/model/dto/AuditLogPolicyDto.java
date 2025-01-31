package com.kglory.tms.web.model.dto;

import java.math.BigInteger;

public class AuditLogPolicyDto {

	BigInteger					lAuditSetIndex;
	BigInteger					lType1;
	BigInteger					lType2;
	String						strContent;
	BigInteger					lWarningIndex;
	String						strSmsContent;
	Integer						nApply;
	
	Long						lWarningSetIndex;
	String						strAlarmType;
	Integer						nCount;
	Integer						nSecond;
	Integer						lMailGroup;
	Long						lSmsGroup;
	Integer						nType;
	Integer						nAlarmType;
	
	public BigInteger getlAuditSetIndex() {
		return lAuditSetIndex;
	}
	public void setlAuditSetIndex(BigInteger lAuditSetIndex) {
		this.lAuditSetIndex = lAuditSetIndex;
	}
	public BigInteger getlType1() {
		return lType1;
	}
	public void setlType1(BigInteger lType1) {
		this.lType1 = lType1;
	}
	public BigInteger getlType2() {
		return lType2;
	}
	public void setlType2(BigInteger lType2) {
		this.lType2 = lType2;
	}
	public String getStrContent() {
		return strContent;
	}
	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	public BigInteger getlWarningIndex() {
		return lWarningIndex;
	}
	public void setlWarningIndex(BigInteger lWarningIndex) {
		this.lWarningIndex = lWarningIndex;
	}
	public String getStrSmsContent() {
		return strSmsContent;
	}
	public void setStrSmsContent(String strSmsContent) {
		this.strSmsContent = strSmsContent;
	}
	public Integer getnApply() {
		return nApply;
	}
	public void setnApply(Integer nApply) {
		this.nApply = nApply;
	}
	public Long getlWarningSetIndex() {
		return lWarningSetIndex;
	}
	public void setlWarningSetIndex(Long lWarningSetIndex) {
		this.lWarningSetIndex = lWarningSetIndex;
	}
	public String getStrAlarmType() {
		return strAlarmType;
	}
	public void setStrAlarmType(String strAlarmType) {
		this.strAlarmType = strAlarmType;
	}
	public Integer getnCount() {
		return nCount;
	}
	public void setnCount(Integer nCount) {
		this.nCount = nCount;
	}
	public Integer getnSecond() {
		return nSecond;
	}
	public void setnSecond(Integer nSecond) {
		this.nSecond = nSecond;
	}
	public Integer getlMailGroup() {
		return lMailGroup;
	}
	public void setlMailGroup(Integer lMailGroup) {
		this.lMailGroup = lMailGroup;
	}
	public Long getlSmsGroup() {
		return lSmsGroup;
	}
	public void setlSmsGroup(Long lSmsGroup) {
		this.lSmsGroup = lSmsGroup;
	}
	public Integer getnType() {
		return nType;
	}
	public void setnType(Integer nType) {
		this.nType = nType;
	}
	public Integer getnAlarmType() {
		return nAlarmType;
	}
	public void setnAlarmType(Integer nAlarmType) {
		this.nAlarmType = nAlarmType;
	}
	@Override
	public String toString() {
		return "AuditLogPolicyDto [lAuditSetIndex=" + lAuditSetIndex
				+ ", lType1=" + lType1 + ", lType2=" + lType2 + ", strContent="
				+ strContent + ", lWarningIndex=" + lWarningIndex
				+ ", strSmsContent=" + strSmsContent + ", nApply=" + nApply
				+ ", lWarningSetIndex=" + lWarningSetIndex + ", strAlarmType="
				+ strAlarmType + ", nCount=" + nCount + ", nSecond=" + nSecond
				+ ", lMailGroup=" + lMailGroup + ", lSmsGroup=" + lSmsGroup
				+ ", nType=" + nType + ", nAlarmType=" + nAlarmType + "]";
	}
	
}