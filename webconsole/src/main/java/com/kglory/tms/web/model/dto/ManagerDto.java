package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class ManagerDto extends CommonBean {

    private Long nEventPortInput;
    private String strDbtsnInput;
    private String strDbpwdInput;
    private String strDbuidInput;

    private String strSeverSelect;
    private Long nPortInput;
    private Long nAutoUpdateCheck;
    private Long nDateSelect;
    private Long nTimeSelect;
    private String nDateValue;
    private String nTimeValue;

    private String strEmailServerInput;
    private String strAlertProgramPathNameInput;

    private Long nRawPeriodicInput;
    private Long nAuditPeriodicInput;

    private Long nDiskUsage;
    private Long nDiskWarn;
    private Long nDirectIntegrity;

    private List<SystemConfDto> systemConfList;
    private List<String> loginAuthIpList;

    private String sessionValue;
    private String lockValue;
    private String timeValue;

    public Long getnEventPortInput() {
        return nEventPortInput;
    }

    public void setnEventPortInput(Long nEventPortInput) {
        this.nEventPortInput = nEventPortInput;
    }

    public String getStrDbtsnInput() {
        return strDbtsnInput;
    }

    public void setStrDbtsnInput(String strDbtsnInput) {
        this.strDbtsnInput = strDbtsnInput;
    }

    public String getStrDbpwdInput() {
        return strDbpwdInput;
    }

    public void setStrDbpwdInput(String strDbpwdInput) {
        this.strDbpwdInput = strDbpwdInput;
    }

    public String getStrDbuidInput() {
        return strDbuidInput;
    }

    public void setStrDbuidInput(String strDbuidInput) {
        this.strDbuidInput = strDbuidInput;
    }

    public String getStrSeverSelect() {
        return strSeverSelect;
    }

    public void setStrSeverSelect(String strSeverSelect) {
        this.strSeverSelect = strSeverSelect;
    }

    public Long getnPortInput() {
        return nPortInput;
    }

    public void setnPortInput(Long nPortInput) {
        this.nPortInput = nPortInput;
    }

    public Long getnAutoUpdateCheck() {
        return nAutoUpdateCheck;
    }

    public void setnAutoUpdateCheck(Long nAutoUpdateCheck) {
        this.nAutoUpdateCheck = nAutoUpdateCheck;
    }

    public Long getnDateSelect() {
        return nDateSelect;
    }

    public void setnDateSelect(Long nDateSelect) {
        this.nDateSelect = nDateSelect;
    }

    public Long getnTimeSelect() {
        return nTimeSelect;
    }

    public void setnTimeSelect(Long nTimeSelect) {
        this.nTimeSelect = nTimeSelect;
    }

    public String getnDateValue() {
        return nDateValue;
    }

    public void setnDateValue(String nDateValue) {
        this.nDateValue = nDateValue;
    }

    public String getnTimeValue() {
        return nTimeValue;
    }

    public void setnTimeValue(String nTimeValue) {
        this.nTimeValue = nTimeValue;
    }

    public String getStrEmailServerInput() {
        return strEmailServerInput;
    }

    public void setStrEmailServerInput(String strEmailServerInput) {
        this.strEmailServerInput = strEmailServerInput;
    }

    public String getStrAlertProgramPathNameInput() {
        return strAlertProgramPathNameInput;
    }

    public void setStrAlertProgramPathNameInput(String strAlertProgramPathNameInput) {
        this.strAlertProgramPathNameInput = strAlertProgramPathNameInput;
    }

    public Long getnRawPeriodicInput() {
        return nRawPeriodicInput;
    }

    public void setnRawPeriodicInput(Long nRawPeriodicInput) {
        this.nRawPeriodicInput = nRawPeriodicInput;
    }

    public Long getnAuditPeriodicInput() {
        return nAuditPeriodicInput;
    }

    public void setnAuditPeriodicInput(Long nAuditPeriodicInput) {
        this.nAuditPeriodicInput = nAuditPeriodicInput;
    }

    public Long getnDiskUsage() {
        return nDiskUsage;
    }

    public void setnDiskUsage(Long nDiskUsage) {
        this.nDiskUsage = nDiskUsage;
    }

    public Long getnDiskWarn() {
        return nDiskWarn;
    }

    public void setnDiskWarn(Long nDiskWarn) {
        this.nDiskWarn = nDiskWarn;
    }

    public List<SystemConfDto> getSystemConfList() {
        return systemConfList;
    }

    public void setSystemConfList(List<SystemConfDto> systemConfList) {
        this.systemConfList = systemConfList;
    }

    public List<String> getLoginAuthIpList() {
        return loginAuthIpList;
    }

    public void setLoginAuthIpList(List<String> loginAuthIpList) {
        this.loginAuthIpList = loginAuthIpList;
    }

    public String getSessionValue() {
        return sessionValue;
    }

    public void setSessionValue(String sessionValue) {
        this.sessionValue = sessionValue;
    }

    public String getLockValue() {
        return lockValue;
    }

    public void setLockValue(String lockValue) {
        this.lockValue = lockValue;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public Long getnDirectIntegrity() {
        return nDirectIntegrity;
    }

    public void setnDirectIntegrity(Long nDirectIntegrity) {
        this.nDirectIntegrity = nDirectIntegrity;
    }
}
