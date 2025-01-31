package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.security.AesUtil;

public class PolicyVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = 5048314996892878188L;

    private long lCode;
    private String strDescription;
    private long attackType;
    private long bSeverity;
    private String signatureRule;

    public long getlCode() {
        return lCode;
    }

    public void setlCode(long lCode) {
        this.lCode = lCode;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public long getAttackType() {
        return attackType;
    }

    public void setAttackType(long attackType) {
        this.attackType = attackType;
    }

    public long getbSeverity() {
        return bSeverity;
    }

    public void setbSeverity(long bSeverity) {
        this.bSeverity = bSeverity;
    }

    public String getSignatureRule() {
        return signatureRule;
    }

    public void setSignatureRule(String signatureRule) {
        this.signatureRule = signatureRule;
    }

    @Override
    public String toString() {
        return "PolicyVO [lCode=" + lCode + ", strDescription="
                + strDescription + ", attackType=" + attackType
                + ", bSeverity=" + bSeverity + ", signatureRule="
                + signatureRule + "]";
    }

}
