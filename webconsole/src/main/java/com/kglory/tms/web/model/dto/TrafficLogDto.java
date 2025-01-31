/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

/**
 *
 * @author leecjong
 */
public class TrafficLogDto extends SearchDto {
    private Long lIndex;
    private Integer lMode;
    private Long lCode;
    private String detection;
    private String sourceIp;
    private String destnationIp;
    private Long sourcePort;
    private Long destnationPort;
    private Integer protocol;
    private String strProtocol;
    private String tmcur;

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public Integer getlMode() {
        return lMode;
    }

    public void setlMode(Integer lMode) {
        this.lMode = lMode;
    }

    public Long getlCode() {
        return lCode;
    }

    public void setlCode(Long lCode) {
        this.lCode = lCode;
    }

    public String getDetection() {
        return detection;
    }

    public void setDetection(String detection) {
        this.detection = detection;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestnationIp() {
        return destnationIp;
    }

    public void setDestnationIp(String destnationIp) {
        this.destnationIp = destnationIp;
    }

    public Long getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Long sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Long getDestnationPort() {
        return destnationPort;
    }

    public void setDestnationPort(Long destnationPort) {
        this.destnationPort = destnationPort;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public String getStrProtocol() {
        return strProtocol;
    }

    public void setStrProtocol(String strProtocol) {
        this.strProtocol = strProtocol;
    }

    public String getTmcur() {
        return tmcur;
    }

    public void setTmcur(String tmcur) {
        this.tmcur = tmcur;
    }
}
