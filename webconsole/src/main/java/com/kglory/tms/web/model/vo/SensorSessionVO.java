package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class SensorSessionVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= -4211989691698281197L;

	private String				tmCur;
	private Long				lSensorIndex;
	private Long				dblTcpSynFrame;
	private Long				dblTcpSynBytes;
	private Long				dblTcpSynAckFrame;
	private Long				dblTcpSynAckBytes;
	private Long				dblTcpPstFrame;
	private Long				dblTcpRstBytes;
	private Long				dblTcpFinFrame;
	private Long				dblTcpFinBytes;
	
	public String getTmCur() {
		return tmCur;
	}
	
	public void setTmCur(String tmCur) {
		this.tmCur = tmCur;
	}
	
	public Long getlSensorIndex() {
		return lSensorIndex;
	}
	
	public void setlSensorIndex(Long lSensorIndex) {
		this.lSensorIndex = lSensorIndex;
	}
	
	public Long getDblTcpSynFrame() {
		return dblTcpSynFrame;
	}
	
	public void setDblTcpSynFrame(Long dblTcpSynFrame) {
		this.dblTcpSynFrame = dblTcpSynFrame;
	}
	
	public Long getDblTcpSynBytes() {
		return dblTcpSynBytes;
	}
	
	public void setDblTcpSynBytes(Long dblTcpSynBytes) {
		this.dblTcpSynBytes = dblTcpSynBytes;
	}
	
	public Long getDblTcpSynAckFrame() {
		return dblTcpSynAckFrame;
	}
	
	public void setDblTcpSynAckFrame(Long dblTcpSynAckFrame) {
		this.dblTcpSynAckFrame = dblTcpSynAckFrame;
	}
	
	public Long getDblTcpSynAckBytes() {
		return dblTcpSynAckBytes;
	}
	
	public void setDblTcpSynAckBytes(Long dblTcpSynAckBytes) {
		this.dblTcpSynAckBytes = dblTcpSynAckBytes;
	}
	
	public Long getDblTcpPstFrame() {
		return dblTcpPstFrame;
	}
	
	public void setDblTcpPstFrame(Long dblTcpPstFrame) {
		this.dblTcpPstFrame = dblTcpPstFrame;
	}
	
	public Long getDblTcpRstBytes() {
		return dblTcpRstBytes;
	}
	
	public void setDblTcpRstBytes(Long dblTcpRstBytes) {
		this.dblTcpRstBytes = dblTcpRstBytes;
	}
	
	public Long getDblTcpFinFrame() {
		return dblTcpFinFrame;
	}
	
	public void setDblTcpFinFrame(Long dblTcpFinFrame) {
		this.dblTcpFinFrame = dblTcpFinFrame;
	}
	
	public Long getDblTcpFinBytes() {
		return dblTcpFinBytes;
	}
	
	public void setDblTcpFinBytes(Long dblTcpFinBytes) {
		this.dblTcpFinBytes = dblTcpFinBytes;
	}
	
	@Override
	public String toString() {
		return "SensorSessionVO [tmCur=" + tmCur + ", lSensorIndex=" + lSensorIndex + ", dblTcpSynFrame="
				+ dblTcpSynFrame + ", dblTcpSynBytes=" + dblTcpSynBytes + ", dblTcpSynAckFrame=" + dblTcpSynAckFrame
				+ ", dblTcpSynAckBytes=" + dblTcpSynAckBytes + ", dblTcpPstFrame=" + dblTcpPstFrame
				+ ", dblTcpRstBytes=" + dblTcpRstBytes + ", dblTcpFinFrame=" + dblTcpFinFrame + ", dblTcpFinBytes="
				+ dblTcpFinBytes + "]";
	}
	
}
