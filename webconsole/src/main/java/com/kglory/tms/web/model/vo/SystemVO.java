/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.SystemDto;
import com.kglory.tms.web.util.StringUtil;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author leecjong
 */
public class SystemVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -1841514089689391842L;

    private Integer timeSync;
    private String timeSyncServer;
    private Integer timeSyncPeriod;
    private String emailServer;
    private String emailSecurity;
    private String emailPort;
    private String emailUserId;
    private String emailUserPwd;
    private String modDate;
    private List<SystemConfVO> systemConfList;
    private List<String> loginAuthIpList;

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

    public List<SystemConfVO> getSystemConfList() {
        return systemConfList;
    }

    public void setSystemConfList(List<SystemConfVO> systemConfList) {
        this.systemConfList = systemConfList;
    }

    public List<String> getLoginAuthIpList() {
        return loginAuthIpList;
    }

    public void setLoginAuthIpList(List<String> loginAuthIpList) {
        this.loginAuthIpList = loginAuthIpList;
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

    public boolean equalsEmailSetting(SystemDto dto) {
        if (!StringUtil.isStringEqualse(emailServer, dto.getEmailServer()) || !StringUtil.isStringEqualse(emailPort, dto.getEmailPort()) || !StringUtil.isStringEqualse(emailSecurity, dto.getEmailSecurity()) ||
                !StringUtil.isStringEqualse(emailUserId, dto.getEmailUserId()) || !StringUtil.isStringEqualse(emailUserPwd, dto.getEmailUserPwd())) {
            return false;
        } else {
            return true;
        }
    }
}
