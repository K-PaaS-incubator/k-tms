package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class AuditLogPolicyVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = -7820304771701540762L;

    private BigInteger lAuditSetIndex;
    private BigInteger lType1;
    private BigInteger lType2;
    private String strContent;
    private BigInteger lWarningIndex;
    private String strSmsContent;
    private Integer nApply;
    private Long lWarningSetIndex;
    private String strAlarmType;
    private Integer nCount;
    private Integer nSecond;
    private Integer lMailGroup;
    private Integer lSmsGroup;
    private Integer nType;
    private Integer nAlarmType;

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

    public Integer getlSmsGroup() {
        return lSmsGroup;
    }

    public void setlSmsGroup(Integer lSmsGroup) {
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
}
