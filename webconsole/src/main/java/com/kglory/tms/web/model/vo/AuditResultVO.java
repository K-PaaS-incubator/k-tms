/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class AuditResultVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = 3769608593478528255L;

    private Long logindex;
    private Long lauditlogindex;
    private String strmessage;
    private String userid;
    private Integer sendtype;
    private Integer result;
    private String regdate;

    public Long getLogindex() {
        return logindex;
    }

    public void setLogindex(Long logindex) {
        this.logindex = logindex;
    }

    public Long getLauditlogindex() {
        return lauditlogindex;
    }

    public void setLauditlogindex(Long lauditlogindex) {
        this.lauditlogindex = lauditlogindex;
    }

    public String getStrmessage() {
        return strmessage;
    }

    public void setStrmessage(String strmessage) {
        this.strmessage = strmessage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getSendtype() {
        return sendtype;
    }

    public void setSendtype(Integer sendtype) {
        this.sendtype = sendtype;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}
