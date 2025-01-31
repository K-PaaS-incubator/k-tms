/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;
import java.io.Serializable;

/**
 *
 * @author leecjong
 */
public class WarningLogDto extends CommonBean implements Serializable {
    private static final long serialVersionUID = 7963030701360662679L;

    private Long lWarningLogIndex;
    private Integer nType;
    private String tmTime;
    private String strAlarmType;
    private String strDescription;
    private Integer wType;
    private Long lRefIndex;
    private String strRefTable;
    
    private Integer lUserGroupIndex;
    private Integer lUserIndex;
    private Integer nStatus;
    private String strError;
    
    private String warningTable;
    private String warningResultTable;

    public Long getlWarningLogIndex() {
        return lWarningLogIndex;
    }

    public void setlWarningLogIndex(Long lWarningLogIndex) {
        this.lWarningLogIndex = lWarningLogIndex;
    }

    public Integer getnType() {
        return nType;
    }

    public void setnType(Integer nType) {
        this.nType = nType;
    }

    public String getTmTime() {
        return tmTime;
    }

    public void setTmTime(String tmTime) {
        this.tmTime = tmTime;
    }

    public String getStrAlarmType() {
        return strAlarmType;
    }

    public void setStrAlarmType(String strAlarmType) {
        this.strAlarmType = strAlarmType;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public Integer getwType() {
        return wType;
    }

    public void setwType(Integer wType) {
        this.wType = wType;
    }

    public Long getlRefIndex() {
        return lRefIndex;
    }

    public void setlRefIndex(Long lRefIndex) {
        this.lRefIndex = lRefIndex;
    }

    public String getStrRefTable() {
        return strRefTable;
    }

    public void setStrRefTable(String strRefTable) {
        this.strRefTable = strRefTable;
    }

    public Integer getlUserGroupIndex() {
        return lUserGroupIndex;
    }

    public void setlUserGroupIndex(Integer lUserGroupIndex) {
        this.lUserGroupIndex = lUserGroupIndex;
    }

    public Integer getlUserIndex() {
        return lUserIndex;
    }

    public void setlUserIndex(Integer lUserIndex) {
        this.lUserIndex = lUserIndex;
    }

    public Integer getnStatus() {
        return nStatus;
    }

    public void setnStatus(Integer nStatus) {
        this.nStatus = nStatus;
    }

    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }

    public String getWarningTable() {
        return warningTable;
    }

    public void setWarningTable(String warningTable) {
        this.warningTable = warningTable;
    }

    public String getWarningResultTable() {
        return warningResultTable;
    }

    public void setWarningResultTable(String warningResultTable) {
        this.warningResultTable = warningResultTable;
    }
}
