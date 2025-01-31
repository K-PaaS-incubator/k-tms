package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class DetectionPolicyVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = -4764694664879521852L;

    private long lCode;
    private long sAlive;
    private String strAlive;
    private long sClassType;
    private String strDescription;
    private long sSeverity;
    private String strSeverity;
    private String strSigRule;
    private int sigRuleYn;
    private long lUsed;
    private String strUsed;
    private long lResponse;
    private long lThresholdTime;
    private long lThresholdNum;
    private long lvsensorIndex;
    private String strAttackType;
    private String strFalsePositive;
    private String strSolution;
    private String strReference;
    private String strCveId;
    private String strbId;
    private String strVul;
    private String strNotVul;
    private String strSummary;
    private String strhelpDescription;
    private String strAddrsPoof;

    private long nClassType;
    private String strName;
    private BigInteger rNum;
    private BigInteger totalRowSize;
    private String strServiceGroup;

    public int getSigRuleYn() {
        return sigRuleYn;
    }

    public void setSigRuleYn(int sigRuleYn) {
        this.sigRuleYn = sigRuleYn;
    }

    public long getlCode() {
        return lCode;
    }

    public void setlCode(long lCode) {
        this.lCode = lCode;
    }

    public long getsAlive() {
        return sAlive;
    }

    public void setsAlive(long sAlive) {
        this.sAlive = sAlive;
    }

    public long getsClassType() {
        return sClassType;
    }

    public void setsClassType(long sClassType) {
        this.sClassType = sClassType;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public long getsSeverity() {
        return sSeverity;
    }

    public void setsSeverity(long sSeverity) {
        this.sSeverity = sSeverity;
    }

    public String getStrSigRule() {
        return strSigRule;
    }

    public void setStrSigRule(String strSigRule) {
//        this.strSigRule = AesUtil.decryptSignature(strSigRule, this.lCode);
        this.strSigRule = strSigRule;
    }

    public long getlUsed() {
        return lUsed;
    }

    public void setlUsed(long lUsed) {
        this.lUsed = lUsed;
    }

    public long getlResponse() {
        return lResponse;
    }

    public void setlResponse(long lResponse) {
        this.lResponse = lResponse;
    }

    public long getlThresholdTime() {
        return lThresholdTime;
    }

    public void setlThresholdTime(long lThresholdTime) {
        this.lThresholdTime = lThresholdTime;
    }

    public long getlThresholdNum() {
        return lThresholdNum;
    }

    public void setlThresholdNum(long lThresholdNum) {
        this.lThresholdNum = lThresholdNum;
    }

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public String getStrAttackType() {
        return strAttackType;
    }

    public void setStrAttackType(String strAttackType) {
        this.strAttackType = strAttackType;
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

    public String getStrhelpDescription() {
        return strhelpDescription;
    }

    public void setStrhelpDescription(String strhelpDescription) {
        this.strhelpDescription = strhelpDescription;
    }

    public String getStrSummary() {
        return strSummary;
    }

    public void setStrSummary(String strSummary) {
        this.strSummary = strSummary;
    }

    public String getStrAddrsPoof() {
        return strAddrsPoof;
    }

    public void setStrAddrsPoof(String strAddrsPoof) {
        this.strAddrsPoof = strAddrsPoof;
    }

    public String getStrNotVul() {
        return strNotVul;
    }

    public void setStrNotVul(String strNotVul) {
        this.strNotVul = strNotVul;
    }

    public long getnClassType() {
        return nClassType;
    }

    public void setnClassType(long nClassType) {
        this.nClassType = nClassType;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public BigInteger getrNum() {
        return rNum;
    }

    public void setrNum(BigInteger rNum) {
        this.rNum = rNum;
    }

    public BigInteger getTotalRowSize() {
        return totalRowSize;
    }

    public void setTotalRowSize(BigInteger totalRowSize) {
        this.totalRowSize = totalRowSize;
    }

    public String getStrServiceGroup() {
        return strServiceGroup;
    }

    public void setStrServiceGroup(String strServiceGroup) {
        this.strServiceGroup = strServiceGroup;
    }

    public String getStrAlive() {
        return strAlive;
    }

    public void setStrAlive(String strAlive) {
        this.strAlive = strAlive;
    }

    public String getStrSeverity() {
        return strSeverity;
    }

    public void setStrSeverity(String strSeverity) {
        this.strSeverity = strSeverity;
    }

    public String getStrUsed() {
        return strUsed;
    }

    public void setStrUsed(String strUsed) {
        this.strUsed = strUsed;
    }

    public static String[] getDetectionCsvHeader() {
        String[] header = new String[]{"lCode", "sAlive", "sClassType", "strDescription", "sSeverity", "strSigRule",
            "lUsed", "lResponse", "lThresholdTime", "lThresholdNum", "strServiceGroup"};

        return header;
    }

    public static String[] getDetectionResponseCsvHeader() {
        String[] header = new String[]{"lCode", "lUsed", "lResponse", "lThresholdTime", "lThresholdNum", "lvsensorIndex",};

        return header;
    }
}
