package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.kglory.tms.web.model.CommonBean;

public class CommandVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 4587426853913636408L;

	Long						lReqIndex;
	Long						lCode;
	String						strArgument;
	String						owner;
	Date						tmRegDate;
	Date						tmRead;
	Integer						result;
	Date						tmResult;
	
	public Long getlReqIndex() {
		return lReqIndex;
	}
	
	public void setlReqIndex(Long lReqIndex) {
		this.lReqIndex = lReqIndex;
	}
	
	public Long getlCode() {
		return lCode;
	}
	
	public void setlCode(Long lCode) {
		this.lCode = lCode;
	}
	
	public String getStrArgument() {
		return strArgument;
	}
	
	public void setStrArgument(String strArgument) {
		this.strArgument = strArgument;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public Date getTmRegDate() {
		return tmRegDate;
	}
	
	public void setTmRegDate(Date tmRegDate) {
		this.tmRegDate = tmRegDate;
	}
	
	public Date getTmRead() {
		return tmRead;
	}
	
	public void setTmRead(Date tmRead) {
		this.tmRead = tmRead;
	}
	
	public Integer getResult() {
		return result;
	}
	
	public void setResult(Integer result) {
		this.result = result;
	}
	
	public Date getTmResult() {
		return tmResult;
	}
	
	public void setTmResult(Date tmResult) {
		this.tmResult = tmResult;
	}
	
	@Override
	public String toString() {
		return "CommandVO [lReqIndex=" + lReqIndex + ", lCode=" + lCode + ", strArgument=" + strArgument + ", owner="
				+ owner + ", tmRegDate=" + tmRegDate + ", tmRead=" + tmRead + ", result=" + result + ", tmResult="
				+ tmResult + "]";
	}
	
}
