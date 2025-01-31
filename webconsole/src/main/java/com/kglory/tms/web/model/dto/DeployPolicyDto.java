package com.kglory.tms.web.model.dto;

public class DeployPolicyDto {
	private Long lcode;
	private Long lThresholdNum;
	private Long lThresholdTime;
	private Long lResponse;
	private Integer lUsed;
	private Long lIndex;
	private Boolean lResponseBool;
	private Boolean	lBlockBool;
	
	public Long getLcode() {
		return lcode;
	}
	public void setLcode(Long lcode) {
		this.lcode = lcode;
	}
	public Long getlThresholdNum() {
		return lThresholdNum;
	}
	public void setlThresholdNum(Long lThresholdNum) {
		this.lThresholdNum = lThresholdNum;
	}
	public Long getlThresholdTime() {
		return lThresholdTime;
	}
	public void setlThresholdTime(Long lThresholdTime) {
		this.lThresholdTime = lThresholdTime;
	}
	public Long getlResponse() {
		return lResponse;
	}
	public void setlResponse(Long lResponse) {
		this.lResponse = lResponse;
	}
	public Integer getlUsed() {
		return lUsed;
	}
	public void setlUsed(Integer lUsed) {
		this.lUsed = lUsed;
	}
	public Long getlIndex() {
		return lIndex;
	}
	public void setlIndex(Long lIndex) {
		this.lIndex = lIndex;
	}
	public Boolean getlResponseBool() {
		return lResponseBool;
	}
	public void setlResponseBool(Boolean lResponseBool) {
		this.lResponseBool = lResponseBool;
	}
	public Boolean getlBlockBool() {
		return lBlockBool;
	}
	public void setlBlockBool(Boolean lBlockBool) {
		this.lBlockBool = lBlockBool;
	}
	@Override
	public String toString() {
		return "DeployPolicyDto [lcode=" + lcode + ", lThresholdNum="
				+ lThresholdNum + ", lThresholdTime=" + lThresholdTime
				+ ", lResponse=" + lResponse + ", lUsed=" + lUsed + ", lIndex="
				+ lIndex + ", lResponseBool=" + lResponseBool + ", lBlockBool="
				+ lBlockBool + "]";
	}
}
