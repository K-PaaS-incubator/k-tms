package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class GlobalThreatVO extends CommonBean implements Serializable{
	private static final long serialVersionUID = 8868398632635369968L;

	
	Long		lIndex;
	Integer		bSeverity;
	String		strSubject;
	String		strContent;
	String		tmUpdate;
	Integer		rNum;
	Integer		totalRowSize;
	
	public Long getlIndex() {
		return lIndex;
	}
	public void setlIndex(Long lIndex) {
		this.lIndex = lIndex;
	}
	public Integer getbSeverity() {
		return bSeverity;
	}
	public void setbSeverity(Integer bSeverity) {
		this.bSeverity = bSeverity;
	}
	public String getStrSubject() {
		return strSubject;
	}
	public void setStrSubject(String strSubject) {
		this.strSubject = strSubject;
	}
	public String getStrContent() {
		return strContent;
	}
	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	public String getTmUpdate() {
		return tmUpdate;
	}
	public void setTmUpdate(String tmUpdate) {
		this.tmUpdate = tmUpdate;
	}
	
	public Integer getrNum() {
		return rNum;
	}
	public void setrNum(Integer rNum) {
		this.rNum = rNum;
	}
	public Integer getTotalRowSize() {
		return totalRowSize;
	}
	public void setTotalRowSize(Integer totalRowSize) {
		this.totalRowSize = totalRowSize;
	}
	@Override
	public String toString() {
		return "GlobalThreatVO [lIndex=" + lIndex + ", bSeverity=" + bSeverity
				+ ", strSubject=" + strSubject + ", strContent=" + strContent
				+ ", tmUpdate=" + tmUpdate + ", rNum=" + rNum
				+ ", totalRowSize=" + totalRowSize + "]";
	}

	
}
