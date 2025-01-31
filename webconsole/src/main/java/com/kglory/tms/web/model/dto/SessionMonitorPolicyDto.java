package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class SessionMonitorPolicyDto extends CommonBean {

    private long lIndex;
    private String strName;
    private String lvsensorIndex;

    private long sessionIndex;
    private long sessionVsensorIndex;
    private long nPort;
    private long nRenewOption;
    private Integer deletelIndex;

    private List<SessionMonitorPolicyFile> checkList;
    private long unPort;
    private String unStrName;
    private Integer nCheck;

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

    public String getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(String lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public long getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(long sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public long getSessionVsensorIndex() {
        return sessionVsensorIndex;
    }

    public void setSessionVsensorIndex(long sessionVsensorIndex) {
        this.sessionVsensorIndex = sessionVsensorIndex;
    }

    public long getnPort() {
        return nPort;
    }

    public void setnPort(long nPort) {
        this.nPort = nPort;
    }

    public long getnRenewOption() {
        return nRenewOption;
    }

    public void setnRenewOption(long nRenewOption) {
        this.nRenewOption = nRenewOption;
    }

    public List<SessionMonitorPolicyFile> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<SessionMonitorPolicyFile> checkList) {
        this.checkList = checkList;
    }

    public Integer getDeletelIndex() {
        return deletelIndex;
    }

    public void setDeletelIndex(Integer deletelIndex) {
        this.deletelIndex = deletelIndex;
    }

    public long getUnPort() {
        return unPort;
    }

    public void setUnPort(long unPort) {
        this.unPort = unPort;
    }

    public String getUnStrName() {
        return unStrName;
    }

    public void setUnStrName(String unStrName) {
        this.unStrName = unStrName;
    }

    public Integer getnCheck() {
        return nCheck;
    }

    public void setnCheck(Integer nCheck) {
        this.nCheck = nCheck;
    }
}
