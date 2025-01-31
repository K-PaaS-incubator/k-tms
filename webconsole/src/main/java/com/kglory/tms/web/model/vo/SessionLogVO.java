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
public class SessionLogVO  extends CommonBean implements Serializable {
    private static final long serialVersionUID = 8921712809043395565L;

    private String sessionIndex;
    private Long serverIp;
    private Long clientIp;
    private String strServerIp;
    private String strClientIp;
    private Long serverPort;
    private Long clientPort;
    private String serverService;
    private String clientService;
    private String information;
    private Long serverBytes;
    private Long clientBytes;
    private String fileName;
    private Integer sessionStatus;
    private Integer dwVioLationcode;
    private Integer reserved;
    private Integer srcNetIndex;
    private Integer dstNetIndex;
    private String srcNetName;
    private String dstNetName;
    private String startTime;
    private String endTime;
    private String strUser;
    
    private Long totalRowSize = 0L;
    private Long rNum = 0L;

    public String getSessionIndex() {
        return sessionIndex;
    }

    public void setSessionIndex(String sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public Long getServerIp() {
        return serverIp;
    }

    public void setServerIp(Long serverIp) {
        this.serverIp = serverIp;
    }

    public Long getClientIp() {
        return clientIp;
    }

    public void setClientIp(Long clientIp) {
        this.clientIp = clientIp;
    }

    public String getStrServerIp() {
        return strServerIp;
    }

    public void setStrServerIp(String strServerIp) {
        this.strServerIp = strServerIp;
    }

    public String getStrClientIp() {
        return strClientIp;
    }

    public void setStrClientIp(String strClientIp) {
        this.strClientIp = strClientIp;
    }

    public Long getServerPort() {
        return serverPort;
    }

    public void setServerPort(Long serverPort) {
        this.serverPort = serverPort;
    }

    public Long getClientPort() {
        return clientPort;
    }

    public void setClientPort(Long clientPort) {
        this.clientPort = clientPort;
    }

    public String getServerService() {
        return serverService;
    }

    public void setServerService(String serverService) {
        this.serverService = serverService;
    }

    public String getClientService() {
        return clientService;
    }

    public void setClientService(String clientService) {
        this.clientService = clientService;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Long getServerBytes() {
        return serverBytes;
    }

    public void setServerBytes(Long serverBytes) {
        this.serverBytes = serverBytes;
    }

    public Long getClientBytes() {
        return clientBytes;
    }

    public void setClientBytes(Long clientBytes) {
        this.clientBytes = clientBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(Integer sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Integer getDwVioLationcode() {
        return dwVioLationcode;
    }

    public void setDwVioLationcode(Integer dwVioLationcode) {
        this.dwVioLationcode = dwVioLationcode;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Integer getSrcNetIndex() {
        return srcNetIndex;
    }

    public void setSrcNetIndex(Integer srcNetIndex) {
        this.srcNetIndex = srcNetIndex;
    }

    public Integer getDstNetIndex() {
        return dstNetIndex;
    }

    public void setDstNetIndex(Integer dstNetIndex) {
        this.dstNetIndex = dstNetIndex;
    }

    public String getSrcNetName() {
        return srcNetName;
    }

    public void setSrcNetName(String srcNetName) {
        this.srcNetName = srcNetName;
    }

    public String getDstNetName() {
        return dstNetName;
    }

    public void setDstNetName(String dstNetName) {
        this.dstNetName = dstNetName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStrUser() {
        return strUser;
    }

    public void setStrUser(String strUser) {
        this.strUser = strUser;
    }

    public Long getTotalRowSize() {
        return totalRowSize;
    }

    public void setTotalRowSize(Long totalRowSize) {
        this.totalRowSize = totalRowSize;
    }

    public Long getrNum() {
        return rNum;
    }

    public void setrNum(Long rNum) {
        this.rNum = rNum;
    }
}
