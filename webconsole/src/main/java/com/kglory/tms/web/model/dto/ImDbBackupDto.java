package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

public class ImDbBackupDto extends CommonBean {

    Long lIndex;
    Long nData;
    String tmFrom;
    String tmTo;

    Integer nFileFlag;

    String strFileName;
    Integer nTableDel;
    Integer nTableCheckValue;

    String strBackupFilePath;
    
    Integer startRowSize;

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public Long getnData() {
        return nData;
    }

    public void setnData(Long nData) {
        this.nData = nData;
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

    public Integer getnFileFlag() {
        return nFileFlag;
    }

    public void setnFileFlag(Integer nFileFlag) {
        this.nFileFlag = nFileFlag;
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

    public Integer getStartRowSize() {
        return startRowSize;
    }

    public void setStartRowSize(Integer startRowSize) {
        this.startRowSize = startRowSize;
    }
}
