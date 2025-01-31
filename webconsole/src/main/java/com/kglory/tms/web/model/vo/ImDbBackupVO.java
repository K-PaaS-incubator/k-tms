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
public class ImDbBackupVO  extends CommonBean {
    Long lIndex;
    String tmFrom;
    String tmTo;
    String strFileName;
    Integer nTableDel;
    Integer nTableCheckValue;
    String strBackupFilePath;
    String tmregDate;
    Long totalRow;
    Integer rNum;

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public String getTmFrom() {
        return tmFrom;
    }

    public void setTmFrom(String tmFrom) {
        this.tmFrom = tmFrom;
    }

    public String getTmTo() {
        return tmTo;
    }

    public void setTmTo(String tmTo) {
        this.tmTo = tmTo;
    }

    public String getStrFileName() {
        return strFileName;
    }

    public void setStrFileName(String strFileName) {
        this.strFileName = strFileName;
    }

    public Integer getnTableDel() {
        return nTableDel;
    }

    public void setnTableDel(Integer nTableDel) {
        this.nTableDel = nTableDel;
    }

    public Integer getnTableCheckValue() {
        return nTableCheckValue;
    }

    public void setnTableCheckValue(Integer nTableCheckValue) {
        this.nTableCheckValue = nTableCheckValue;
    }

    public String getStrBackupFilePath() {
        return strBackupFilePath;
    }

    public void setStrBackupFilePath(String strBackupFilePath) {
        this.strBackupFilePath = strBackupFilePath;
    }

    public String getTmregDate() {
        return tmregDate;
    }

    public void setTmregDate(String tmregDate) {
        this.tmregDate = tmregDate;
    }

    public Long getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Long totalRow) {
        this.totalRow = totalRow;
    }

    public Integer getrNum() {
        return rNum;
    }

    public void setrNum(Integer rNum) {
        this.rNum = rNum;
    }
}
