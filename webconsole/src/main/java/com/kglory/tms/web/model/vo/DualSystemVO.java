/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;

/**
 *
 * @author leecjong
 */
public class DualSystemVO extends CommonBean {

    private Integer writeMode = 1;  //1 : write off, 0 : write on
    private Integer currentMode = 1;  //1 : master, 0 : slave
    private Integer masterStat = 1;  //0 : off, 1 : on
    private Integer slaveStat = 1;  //0 : off, 1 : on

    public Integer getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(Integer writeMode) {
        this.writeMode = writeMode;
    }

    public Integer getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Integer currentMode) {
        this.currentMode = currentMode;
    }

    public Integer getMasterStat() {
        return masterStat;
    }

    public void setMasterStat(Integer masterStat) {
        this.masterStat = masterStat;
    }

    public Integer getSlaveStat() {
        return slaveStat;
    }

    public void setSlaveStat(Integer slaveStat) {
        this.slaveStat = slaveStat;
    }
}
