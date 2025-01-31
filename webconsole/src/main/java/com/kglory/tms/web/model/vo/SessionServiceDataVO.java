package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class SessionServiceDataVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -1594741508973254869L;


    private Long lIndex;
    private String strName;
    private Long nPort;
    private Long nRenewOption;

    private Long lvsensorIndex;
    private Integer nCheck;
    private Long sessionIndex;
    private String strVsensorName;
    private Long sessionVsensorIndex;

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Long getnPort() {
        return nPort;
    }

    public void setnPort(Long nPort) {
        this.nPort = nPort;
    }

    public Long getnRenewOption() {
        return nRenewOption;
    }

    public void setnRenewOption(Long nRenewOption) {
        this.nRenewOption = nRenewOption;
    }

    public Long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(Long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public Integer getnCheck() {
        return nCheck;
    }

    public void setnCheck(Integer nCheck) {
        this.nCheck = nCheck;
    }

    public Long getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(Long sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public String getStrVsensorName() {
        return strVsensorName;
    }

    public void setStrVsensorName(String strVsensorName) {
        this.strVsensorName = strVsensorName;
    }

    public Long getSessionVsensorIndex() {
        return sessionVsensorIndex;
    }

    public void setSessionVsensorIndex(Long sessionVsensorIndex) {
        this.sessionVsensorIndex = sessionVsensorIndex;
    }

    @Override
    public String toString() {
        return "SessionServiceDataVO [lIndex=" + lIndex + ", strName="
                + strName + ", nPort=" + nPort + ", nRenewOption="
                + nRenewOption + ", lvsensorIndex=" + lvsensorIndex
                + ", nCheck=" + nCheck + ", sessionIndex=" + sessionIndex
                + ", strVsensorName=" + strVsensorName
                + ", sessionVsensorIndex=" + sessionVsensorIndex + "]";
    }
}
