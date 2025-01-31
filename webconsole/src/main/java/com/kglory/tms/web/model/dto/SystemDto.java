/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author leecjong
 */
public class SystemDto extends CommonBean implements Serializable {
    private static final long serialVersionUID = -8437013314646437168L;

    private Integer timeSync;
    private String timeSyncServer;
    private Integer timeSyncPeriod;
    private String emailServer;
    private String emailSecurity;
    private String emailPort;
    private String emailUserId;
    private String emailUserPwd;
    private String modDate;
    private List<SystemConfDto> systemConfList;
    private List<SystemConfDto> sftpConfList;
    private List<String> loginAuthIpList;
    private String sessionValue;
    private String lockValue;
    private String timeValue;

    public Integer getTimeSync() {
        return timeSync;
    }

    public void setTimeSync(Integer timeSync) {
        this.timeSync = timeSync;
    }

    public String getTimeSyncServer() {
        return timeSyncServer;
    }

    public void setTimeSyncServer(String timeSyncServer) {
        this.timeSyncServer = timeSyncServer;
    }

    public Integer getTimeSyncPeriod() {
        return timeSyncPeriod;
    }

    public void setTimeSyncPeriod(Integer timeSyncPeriod) {
        this.timeSyncPeriod = timeSyncPeriod;
    }

    public String getEmailServer() {
        return emailServer;
    }

    public void setEmailServer(String emailServer) {
        this.emailServer = emailServer;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public List<SystemConfDto> getSystemConfList() {
        return systemConfList;
    }

    public void setSystemConfList(List<SystemConfDto> systemConfList) {
        this.systemConfList = systemConfList;
    }

    public List<SystemConfDto> getSftpConfList() {
        return sftpConfList;
    }

    public void setSftpConfList(List<SystemConfDto> sftpConfList) {
        this.sftpConfList = sftpConfList;
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

    public String getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(String emailPort) {
        this.emailPort = emailPort;
    }

    public String getEmailUserId() {
        return emailUserId;
    }

    public void setEmailUserId(String emailUserId) {
        this.emailUserId = emailUserId;
    }

    public String getEmailUserPwd() {
        return emailUserPwd;
    }

    public void setEmailUserPwd(String emailUserPwd) {
        this.emailUserPwd = emailUserPwd;
    }

    public String getEmailSecurity() {
        return emailSecurity;
    }

    public void setEmailSecurity(String emailSecurity) {
        this.emailSecurity = emailSecurity;
    }
}
