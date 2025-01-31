package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class AttackHelpVO extends CommonBean implements Serializable {

	private static final long serialVersionUID = 4424651678069484928L;

	private Long lCode;
	private String strTitle;
	private String strAttackType;
	private Long bSeverity;
	private String strSummary;
	private String strDescription;
	private String strFalsePositive;
	private String strSolution;
	private String strReference;
	private String strCveId;
	private String strbId;
	private String strVul;
	private String strNotVul;
	private String strAddrspoof;
	private String strDetect;
	private String signatureRule;
	private Integer totalRowSize;
	private Integer rNum;
	private Integer cntInfo;
	private Integer cntLow;
	private Integer cntMed;
	private Integer cntHigh;
	private String sigHelpDescription;
	private Integer nClassType;
	private String strName;
	private String bType;

	public Integer getnClassType() {
		return nClassType;
	}
	public void setnClassType(Integer nClassType) {
		this.nClassType = nClassType;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getSigHelpDescription() {
		return sigHelpDescription;
	}
	public void setSigHelpDescription(String sigHelpDescription) {
		this.sigHelpDescription = sigHelpDescription;
	}
	public Long getlCode() {
		return lCode;
	}
	public void setlCode(Long lCode) {
		this.lCode = lCode;
	}
	public String getStrTitle() {
		return strTitle;
	}
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	
	public String getStrAttackType() {
		return strAttackType;
	}
	public void setStrAttackType(String strAttackType) {
		this.strAttackType = strAttackType;
	}
	public Long getbSeverity() {
		return bSeverity;
	}
	public void setbSeverity(Long bSeverity) {
		this.bSeverity = bSeverity;
	}
	public String getStrSummary() {
		return strSummary;
	}
	public void setStrSummary(String strSummary) {
		this.strSummary = strSummary;
	}
	public String getStrDescription() {
		return strDescription;
	}
	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}
	public String getStrFalsePositive() {
		return strFalsePositive;
	}
	public void setStrFalsePositive(String strFalsePositive) {
		this.strFalsePositive = strFalsePositive;
	}
	public String getStrSolution() {
		return strSolution;
	}
	public void setStrSolution(String strSolution) {
		this.strSolution = strSolution;
	}
	public String getStrReference() {
		return strReference;
	}
	public void setStrReference(String strReference) {
		this.strReference = strReference;
	}
	public String getStrCveId() {
		return strCveId;
	}
	public void setStrCveId(String strCveId) {
		this.strCveId = strCveId;
	}
	public String getStrbId() {
		return strbId;
	}
	public void setStrbId(String strbId) {
		this.strbId = strbId;
	}
	public String getStrVul() {
		return strVul;
	}
	public void setStrVul(String strVul) {
		this.strVul = strVul;
	}
	public String getStrNotVul() {
		return strNotVul;
	}
	public void setStrNotVul(String strNotVul) {
		this.strNotVul = strNotVul;
	}
	public String getStrAddrspoof() {
		return strAddrspoof;
	}
	public void setStrAddrspoof(String strAddrspoof) {
		this.strAddrspoof = strAddrspoof;
	}
	public String getStrDetect() {
		return strDetect;
	}
	public void setStrDetect(String strDetect) {
		this.strDetect = strDetect;
	}
	
	public String getSignatureRule() {
		return signatureRule;
	}
	public void setSignatureRule(String signatureRule) {
		this.signatureRule = signatureRule;
	}
	public Integer getTotalRowSize() {
		return totalRowSize;
	}
	public void setTotalRowSize(Integer totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	public Integer getrNum() {
		return rNum;
	}
	public void setrNum(Integer rNum) {
		this.rNum = rNum;
	}
	public Integer getCntLow() {
		return cntLow;
	}
	public void setCntLow(Integer cntLow) {
		this.cntLow = cntLow;
	}
	public Integer getCntMed() {
		return cntMed;
	}
	public void setCntMed(Integer cntMed) {
		this.cntMed = cntMed;
	}
	public Integer getCntHigh() {
		return cntHigh;
	}
	public void setCntHigh(Integer cntHigh) {
		this.cntHigh = cntHigh;
	}
	
	public Integer getCntInfo() {
		return cntInfo;
	}
	public void setCntInfo(Integer cntInfo) {
		this.cntInfo = cntInfo;
	}
	
	public String getbType() {
		return bType;
	}
	public void setbType(String bType) {
		this.bType = bType;
	}
	@Override
	public String toString() {
		return "AttackHelpVO [lCode=" + lCode + ", strTitle=" + strTitle
				+ ", strAttackType=" + strAttackType + ", bSeverity="
				+ bSeverity + ", strSummary=" + strSummary
				+ ", strDescription=" + strDescription + ", strFalsePositive="
				+ strFalsePositive + ", strSolution=" + strSolution
				+ ", strReference=" + strReference + ", strCveId=" + strCveId
				+ ", strbId=" + strbId + ", strVul=" + strVul + ", strNotVul="
				+ strNotVul + ", strAddrspoof=" + strAddrspoof + ", strDetect="
				+ strDetect + ", signatureRule=" + signatureRule
				+ ", totalRowSize=" + totalRowSize + ", rNum=" + rNum
				+ ", cntInfo=" + cntInfo + ", cntLow=" + cntLow + ", cntMed="
				+ cntMed + ", cntHigh=" + cntHigh + ", sigHelpDescription="
				+ sigHelpDescription + ", nClassType=" + nClassType
				+ ", strName=" + strName + ", bType=" + bType + "]";
	}

}
