/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.ext.sms;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.WarningLogDto;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class LogSms extends CommonBean implements Serializable {
    private static final long serialVersionUID = -4047527593821548149L;

    //보내는 사람 전화번호
    private String srcMdn;
    //발신자명 
    private String srcName;
    //받는 사람 전화번호
    private String destMdn;
    //SMS 메세지
    private String msg;
    
    private WarningLogDto warningLog;

    public LogSms() {
    }

    public LogSms(String srcMdn, String srcName, String destMdn, String msg) {
        this.srcMdn = srcMdn;
        this.srcName = srcName;
        this.destMdn = destMdn;
        this.msg = msg;
    }

    public LogSms(String srcMdn, String srcName, String destMdn, String msg, WarningLogDto warningLog) {
        this.srcMdn = srcMdn;
        this.srcName = srcName;
        this.destMdn = destMdn;
        this.msg = msg;
        this.warningLog = warningLog;
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

    public String getDestMdn() {
        return destMdn;
    }

    public void setDestMdn(String destMdn) {
        this.destMdn = destMdn;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public WarningLogDto getWarningLog() {
        return warningLog;
    }

    public void setWarningLog(WarningLogDto warningLog) {
        this.warningLog = warningLog;
    }
}
