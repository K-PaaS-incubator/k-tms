/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.ext.sms;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class SmsConfig extends CommonBean implements Serializable {
    private static final long serialVersionUID = 3812841653193224763L;

    private String ip;
    //SMS Port
    private Integer port;
    //SMS ID
    private String loginId;
    //SMS Password
    private String loginPasswd;
    //발신자 번호
    private String srcMdn;
    // 발신자명
    private String srcName; 

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPasswd() {
        return loginPasswd;
    }

    public void setLoginPasswd(String loginPasswd) {
        this.loginPasswd = loginPasswd;
    }

    public String getSrcMdn() {
        return srcMdn;
    }

    public void setSrcMdn(String srcMdn) {
        this.srcMdn = srcMdn;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }
}
