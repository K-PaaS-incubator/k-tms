package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

public class ManagerBackupDto extends CommonBean {

	private long 				nDayConfigFlag;
	private String 				strDayBookTime;
	private long 				nDayBookDayBefore;
	private long 				nDayFileFlag;
	private String 				strDayFileName;
	private long 				nDayTableDeleteFlag;
	private long 				nDayTableCheckValue;
	
	private long 				nMonthConfigFlag;
	private String 				strMonthBookTime;
	private long 				nMonthBookDay;
	private long 				nMonthBookDayBefore;
	private long 				nMonthFileFlag;
	private String 				strMonthFileName;
	private long 				nMonthTableDeleteFlag;
	private long 				nMonthTableCheckValue;
	private String				strBackupPathName;
	private String 				nDayFileFlagCheckedValue;
	private String 				nMonthFileFlagCheckedValue;
	private String 				strDayBookTimeValue;
	private String 				strMonthBookTimeValue;
	
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
	public String getnDayFileFlagCheckedValue() {
		return nDayFileFlagCheckedValue;
	}
	public void setnDayFileFlagCheckedValue(String nDayFileFlagCheckedValue) {
		this.nDayFileFlagCheckedValue = nDayFileFlagCheckedValue;
	}
	public String getnMonthFileFlagCheckedValue() {
		return nMonthFileFlagCheckedValue;
	}
	public void setnMonthFileFlagCheckedValue(String nMonthFileFlagCheckedValue) {
		this.nMonthFileFlagCheckedValue = nMonthFileFlagCheckedValue;
	}
	public String getStrDayBookTimeValue() {
		return strDayBookTimeValue;
	}
	public void setStrDayBookTimeValue(String strDayBookTimeValue) {
		this.strDayBookTimeValue = strDayBookTimeValue;
	}
	public String getStrMonthBookTimeValue() {
		return strMonthBookTimeValue;
	}
	public void setStrMonthBookTimeValue(String strMonthBookTimeValue) {
		this.strMonthBookTimeValue = strMonthBookTimeValue;
	}
	@Override
	public String toString() {
		return "ManagerBackupDto [nDayConfigFlag=" + nDayConfigFlag
				+ ", strDayBookTime=" + strDayBookTime + ", nDayBookDayBefore="
				+ nDayBookDayBefore + ", nDayFileFlag=" + nDayFileFlag
				+ ", strDayFileName=" + strDayFileName
				+ ", nDayTableDeleteFlag=" + nDayTableDeleteFlag
				+ ", nDayTableCheckValue=" + nDayTableCheckValue
				+ ", nMonthConfigFlag=" + nMonthConfigFlag
				+ ", strMonthBookTime=" + strMonthBookTime + ", nMonthBookDay="
				+ nMonthBookDay + ", nMonthBookDayBefore="
				+ nMonthBookDayBefore + ", nMonthFileFlag=" + nMonthFileFlag
				+ ", strMonthFileName=" + strMonthFileName
				+ ", nMonthTableDeleteFlag=" + nMonthTableDeleteFlag
				+ ", nMonthTableCheckValue=" + nMonthTableCheckValue
				+ ", strBackupPathName=" + strBackupPathName + "]";
	}
	
	
}
