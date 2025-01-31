package com.kglory.tms.web.model.vo;

import java.io.Serializable;

public class WarningSetVO extends CommandVO implements Serializable{

	private static final long serialVersionUID = 6209183785700483520L;

	private Long lWarningSetIndex;
	private String strAlarmType;
	private Integer nCount;
	private Integer nSecond;
	private Integer lMailGroup;
	private Long 	lSmsGroup;
	private Integer	nType;
	private Integer nAlarmType;
	
	public Long getlWarningSetIndex() {
		return lWarningSetIndex;
	}
	public void setlWarningSetIndex(Long lWarningSetIndex) {
		this.lWarningSetIndex = lWarningSetIndex;
	}
	public String getStrAlarmType() {
		return strAlarmType;
	}
	public void setStrAlarmType(String strAlarmType) {
		this.strAlarmType = strAlarmType;
	}
	public Integer getnCount() {
		return nCount;
	}
	public void setnCount(Integer nCount) {
		this.nCount = nCount;
	}
	public Integer getnSecond() {
		return nSecond;
	}
	public void setnSecond(Integer nSecond) {
		this.nSecond = nSecond;
	}
	public Integer getlMailGroup() {
		return lMailGroup;
	}
	public void setlMailGroup(Integer lMailGroup) {
		this.lMailGroup = lMailGroup;
	}
	public Long getlSmsGroup() {
		return lSmsGroup;
	}
	public void setlSmsGroup(Long lSmsGroup) {
		this.lSmsGroup = lSmsGroup;
	}
	public Integer getnType() {
		return nType;
	}
	public void setnType(Integer nType) {
		this.nType = nType;
	}
	public Integer getnAlarmType() {
		return nAlarmType;
	}
	public void setnAlarmType(Integer nAlarmType) {
		this.nAlarmType = nAlarmType;
	}
	@Override
	public String toString() {
		return "WarningSetVO [lWarningSetIndex=" + lWarningSetIndex
				+ ", strAlarmType=" + strAlarmType + ", nCount=" + nCount
				+ ", nSecond=" + nSecond + ", lMailGroup=" + lMailGroup
				+ ", lSmsGroup=" + lSmsGroup + ", nType=" + nType
				+ ", nAlarmType=" + nAlarmType + "]";
	}

}
