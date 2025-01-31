package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.ManagerBackupDto;

public class ManagerBackupVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = -9130271204339869004L;


    private long nDayConfigFlag;
    private String strDayBookTime;
    private long nDayBookDayBefore;
    private long nDayFileFlag;
    private String strDayFileName;
    private long nDayTableDeleteFlag;
    private long nDayTableCheckValue;

    private long nMonthConfigFlag;
    private String strMonthBookTime;
    private long nMonthBookDay;
    private long nMonthBookDayBefore;
    private long nMonthFileFlag;
    private String strMonthFileName;
    private long nMonthTableDeleteFlag;
    private long nMonthTableCheckValue;
    private String strBackupPathName;
    private long nMinDriveFreeSize;
    private int dayHour = 0;
    private int dayMin = 0;
    private int daySec = 0;

    public long getnDayConfigFlag() {
        return nDayConfigFlag;
    }

    public void setnDayConfigFlag(long nDayConfigFlag) {
        this.nDayConfigFlag = nDayConfigFlag;
    }

    public String getStrDayBookTime() {
        return strDayBookTime;
    }

    public void setStrDayBookTime(String strDayBookTime) {
        this.strDayBookTime = strDayBookTime;
    }

    public long getnDayBookDayBefore() {
        return nDayBookDayBefore;
    }

    public void setnDayBookDayBefore(long nDayBookDayBefore) {
        this.nDayBookDayBefore = nDayBookDayBefore;
    }

    public long getnDayFileFlag() {
        return nDayFileFlag;
    }

    public void setnDayFileFlag(long nDayFileFlag) {
        this.nDayFileFlag = nDayFileFlag;
    }

    public String getStrDayFileName() {
        return strDayFileName;
    }

    public void setStrDayFileName(String strDayFileName) {
        this.strDayFileName = strDayFileName;
    }

    public long getnDayTableDeleteFlag() {
        return nDayTableDeleteFlag;
    }

    public void setnDayTableDeleteFlag(long nDayTableDeleteFlag) {
        this.nDayTableDeleteFlag = nDayTableDeleteFlag;
    }

    public long getnDayTableCheckValue() {
        return nDayTableCheckValue;
    }

    public void setnDayTableCheckValue(long nDayTableCheckValue) {
        this.nDayTableCheckValue = nDayTableCheckValue;
    }

    public long getnMonthConfigFlag() {
        return nMonthConfigFlag;
    }

    public void setnMonthConfigFlag(long nMonthConfigFlag) {
        this.nMonthConfigFlag = nMonthConfigFlag;
    }

    public String getStrMonthBookTime() {
        return strMonthBookTime;
    }

    public void setStrMonthBookTime(String strMonthBookTime) {
        this.strMonthBookTime = strMonthBookTime;
    }

    public long getnMonthBookDay() {
        return nMonthBookDay;
    }

    public void setnMonthBookDay(long nMonthBookDay) {
        this.nMonthBookDay = nMonthBookDay;
    }

    public long getnMonthBookDayBefore() {
        return nMonthBookDayBefore;
    }

    public void setnMonthBookDayBefore(long nMonthBookDayBefore) {
        this.nMonthBookDayBefore = nMonthBookDayBefore;
    }

    public long getnMonthFileFlag() {
        return nMonthFileFlag;
    }

    public void setnMonthFileFlag(long nMonthFileFlag) {
        this.nMonthFileFlag = nMonthFileFlag;
    }

    public String getStrMonthFileName() {
        return strMonthFileName;
    }

    public void setStrMonthFileName(String strMonthFileName) {
        this.strMonthFileName = strMonthFileName;
    }

    public long getnMonthTableDeleteFlag() {
        return nMonthTableDeleteFlag;
    }

    public void setnMonthTableDeleteFlag(long nMonthTableDeleteFlag) {
        this.nMonthTableDeleteFlag = nMonthTableDeleteFlag;
    }

    public long getnMonthTableCheckValue() {
        return nMonthTableCheckValue;
    }

    public void setnMonthTableCheckValue(long nMonthTableCheckValue) {
        this.nMonthTableCheckValue = nMonthTableCheckValue;
    }

    public String getStrBackupPathName() {
        return strBackupPathName;
    }

    public void setStrBackupPathName(String strBackupPathName) {
        this.strBackupPathName = strBackupPathName;
    }

    public long getnMinDriveFreeSize() {
        return nMinDriveFreeSize;
    }

    public void setnMinDriveFreeSize(long nMinDriveFreeSize) {
        this.nMinDriveFreeSize = nMinDriveFreeSize;
    }

    public int getDayHour() {
        return dayHour;
    }

    public void setDayHour(int dayHour) {
        this.dayHour = dayHour;
    }

    public int getDayMin() {
        return dayMin;
    }

    public void setDayMin(int dayMin) {
        this.dayMin = dayMin;
    }

    public int getDaySec() {
        return daySec;
    }

    public void setDaySec(int daySec) {
        this.daySec = daySec;
    }

    public boolean equalseDbBackup(ManagerBackupDto dto)  {
        if (longEquals(nDayConfigFlag, dto.getnDayConfigFlag()) && strEquals(strDayBookTime, dto.getStrDayBookTime())
                && longEquals(nDayBookDayBefore, dto.getnDayBookDayBefore())
                && strEquals(strBackupPathName, dto.getStrBackupPathName()) && strEquals(strBackupPathName, dto.getStrBackupPathName())
                && longEquals(nDayFileFlag, dto.getnDayFileFlag()) && strEquals(strDayFileName, dto.getStrDayFileName()) 
                && longEquals(nDayTableDeleteFlag, dto.getnDayTableDeleteFlag()) && longEquals(nDayTableCheckValue, dto.getnDayTableCheckValue())) {
            return true;
        }
        return false;
    }
    
    private boolean strEquals(String check, String value) {
        if ((check == null && check == null) || (check != null && check.equals(value))) {
            return true;
        }
        return false;
    }
    
    private boolean longEquals(Long check, Long value) {
        if (check == null && value == null) {
            return true;
        } else if(check != null && value != null) {
            if (Long.valueOf(check).intValue() == Long.valueOf(value).intValue()) {
                return true;
            }
        }
        return false;
    }
}
