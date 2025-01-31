package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class ManagerStateVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -7993053361614236225L;

    private long dwCpuSpeed = 0;
    private long dwCpuNum = 0;
    private long dblcurCpuUsage = 0;
    private long dblcurMemUsed = 0;
    private long dwMemTotal = 0;
    private long dwHddUsed = 0;
    private long dwHddTotal = 0;
    private long dwProcessNum = 0;
    private String tmoccur = "";
    private long lsystemlogIndex = 0;
    private String dbUsed;

    public long getDwCpuSpeed() {
        return dwCpuSpeed;
    }

    public void setDwCpuSpeed(long dwCpuSpeed) {
        this.dwCpuSpeed = dwCpuSpeed;
    }

    public long getDwCpuNum() {
        return dwCpuNum;
    }

    public void setDwCpuNum(long dwCpuNum) {
        this.dwCpuNum = dwCpuNum;
    }

    public long getDblcurCpuUsage() {
        return dblcurCpuUsage;
    }

    public void setDblcurCpuUsage(long dblcurCpuUsage) {
        this.dblcurCpuUsage = dblcurCpuUsage;
    }

    public long getDblcurMemUsed() {
        return dblcurMemUsed;
    }

    public void setDblcurMemUsed(long dblcurMemUsed) {
        this.dblcurMemUsed = dblcurMemUsed;
    }

    public long getDwMemTotal() {
        return dwMemTotal;
    }

    public void setDwMemTotal(long dwMemTotal) {
        this.dwMemTotal = dwMemTotal;
    }

    public long getDwHddUsed() {
        return dwHddUsed;
    }

    public void setDwHddUsed(long dwHddUsed) {
        this.dwHddUsed = dwHddUsed;
    }

    public long getDwHddTotal() {
        return dwHddTotal;
    }

    public void setDwHddTotal(long dwHddTotal) {
        this.dwHddTotal = dwHddTotal;
    }

    public long getDwProcessNum() {
        return dwProcessNum;
    }

    public void setDwProcessNum(long dwProcessNum) {
        this.dwProcessNum = dwProcessNum;
    }

    public String getTmoccur() {
        return tmoccur;
    }

    public void setTmoccur(String tmoccur) {
        this.tmoccur = tmoccur;
    }

    public long getLsystemlogIndex() {
        return lsystemlogIndex;
    }

    public void setLsystemlogIndex(long lsystemlogIndex) {
        this.lsystemlogIndex = lsystemlogIndex;
    }

    public String getDbUsed() {
        return dbUsed;
    }

    public void setDbUsed(String dbUsed) {
        this.dbUsed = dbUsed;
    }

    @Override
    public String toString() {
        return "ManagerStateVO [dwCpuSpeed=" + dwCpuSpeed + ", dwCpuNum="
                + dwCpuNum + ", dblcurCpuUsage=" + dblcurCpuUsage
                + ", dblcurMemUsed=" + dblcurMemUsed + ", dwMemTotal="
                + dwMemTotal + ", dwHddUsed=" + dwHddUsed + ", dwHddTotal="
                + dwHddTotal + ", dwProcessNum=" + dwProcessNum + ", tmoccur="
                + tmoccur + "]";
    }
}
