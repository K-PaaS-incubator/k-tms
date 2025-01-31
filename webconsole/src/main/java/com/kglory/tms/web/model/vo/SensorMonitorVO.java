package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.packet.PacketAnalyzer;
import static com.kglory.tms.web.util.packet.PacketAnalyzer.byteArrayCopy;
import static com.kglory.tms.web.util.packet.PacketAnalyzer.byteToInt;
import static com.kglory.tms.web.util.packet.PacketAnalyzer.longTo4Byte;
import java.nio.ByteOrder;

public class SensorMonitorVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 7423054286518862213L;

	private Long				lindex;
	private String				strName;
	private Long				dblPps;
	private Long				dblBps;
	private Long				dblLps;
	private Long				dblDpps1000;
	private Long				dblSession;
	private Long				dblMaliciousBps;
	private Long				dblMaliciousPps;
	private Long				dblTcpSynFrame;
	private Long				dblTcpSynBytes;
	private Long				dblTcpSynAckFrame;
	private Long				dblTcpSynAckBytes;
	private Long				dblTcpRstFrame;
	private Long				dblTcpRstBytes;
	private Long				dblTcpFinFrame;
	private Long				dblTcpFinBytes;
	
	public Long getLindex() {
		return lindex;
	}
	
	public void setLindex(Long lindex) {
		this.lindex = lindex;
	}
	
	public String getStrName() {
		return strName;
	}
	
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	public Long getDblPps() {
		return dblPps;
	}
	
	public void setDblPps(Long dblPps) {
		this.dblPps = dblPps;
	}
	
	public Long getDblBps() {
		return dblBps;
	}
	
	public void setDblBps(Long dblBps) {
		this.dblBps = dblBps;
	}
	
	public Long getDblLps() {
		return dblLps;
	}
	
	public void setDblLps(Long dblLps) {
		this.dblLps = dblLps;
	}
	
	public Long getDblDpps1000() {
		return dblDpps1000;
	}
	
	public void setDblDpps1000(Long dblDpps1000) {
//            if (dblDpps1000 != null) {
//		this.dblDpps1000 = Long.valueOf((PacketAnalyzer.byteToInt(byteArrayCopy(longTo4Byte(dblDpps1000), 2, 2), ByteOrder.LITTLE_ENDIAN)));
//            } else {
                this.dblDpps1000 = dblDpps1000;
//            }
	}
	
	public Long getDblSession() {
		return dblSession;
	}
	
	public void setDblSession(Long dblSession) {
		this.dblSession = dblSession;
	}
	
	public Long getDblMaliciousBps() {
		return dblMaliciousBps;
	}
	
	public void setDblMaliciousBps(Long dblMaliciousBps) {
		this.dblMaliciousBps = dblMaliciousBps;
	}
	
	public Long getDblMaliciousPps() {
		return dblMaliciousPps;
	}
	
	public void setDblMaliciousPps(Long dblMaliciousPps) {
		this.dblMaliciousPps = dblMaliciousPps;
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
	
	public Long getDblTcpRstFrame() {
		return dblTcpRstFrame;
	}
	
	public void setDblTcpRstFrame(Long dblTcpRstFrame) {
		this.dblTcpRstFrame = dblTcpRstFrame;
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
		return "SensorMonitorVO [lindex=" + lindex + ", strName=" + strName + ", dblPps=" + dblPps + ", dblBps="
				+ dblBps + ", dblLps=" + dblLps + ", dblDpps1000=" + dblDpps1000 + ", dblSession=" + dblSession
				+ ", dblMaliciousBps=" + dblMaliciousBps + ", dblMaliciousPps=" + dblMaliciousPps + ", dblTcpSynFrame="
				+ dblTcpSynFrame + ", dblTcpSynBytes=" + dblTcpSynBytes + ", dblTcpSynAckFrame=" + dblTcpSynAckFrame
				+ ", dblTcpSynAckBytes=" + dblTcpSynAckBytes + ", dblTcpRstFrame=" + dblTcpRstFrame
				+ ", dblTcpRstBytes=" + dblTcpRstBytes + ", dblTcpFinFrame=" + dblTcpFinFrame + ", dblTcpFinBytes="
				+ dblTcpFinBytes + "]";
	}
	
}
