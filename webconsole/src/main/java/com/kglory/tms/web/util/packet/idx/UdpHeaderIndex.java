package com.kglory.tms.web.util.packet.idx;

public class UdpHeaderIndex {

	private int udpHeaderStartIdx;
	private int udpHeaderEndIdx;
	private int udpSrcPortStartIdx;
	private int udpSrcPortEndIdx;
	private int udpDstPortStartIdx;
	private int udpDstPortEndIdx;
	private int udpLengthStartIdx;
	private int udpLengthEndIdx;
	private int udpChecksumStartIdx;
	private int udpChecksumEndIdx;
	
	public UdpHeaderIndex(int offset){
		this.setValue(offset);
	}
	
	public int getUdpHeaderStartIdx() {
		return udpHeaderStartIdx;
	}
	public void setUdpHeaderStartIdx(int udpHeaderStartIdx) {
		this.udpHeaderStartIdx = udpHeaderStartIdx;
	}
	public int getUdpHeaderEndIdx() {
		return udpHeaderEndIdx;
	}
	public void setUdpHeaderEndIdx(int udpHeaderEndIdx) {
		this.udpHeaderEndIdx = udpHeaderEndIdx;
	}
	public int getUdpSrcPortStartIdx() {
		return udpSrcPortStartIdx;
	}
	public void setUdpSrcPortStartIdx(int udpSrcPortStartIdx) {
		this.udpSrcPortStartIdx = udpSrcPortStartIdx;
	}
	public int getUdpSrcPortEndIdx() {
		return udpSrcPortEndIdx;
	}
	public void setUdpSrcPortEndIdx(int udpSrcPortEndIdx) {
		this.udpSrcPortEndIdx = udpSrcPortEndIdx;
	}
	public int getUdpDstPortStartIdx() {
		return udpDstPortStartIdx;
	}
	public void setUdpDstPortStartIdx(int udpDstPortStartIdx) {
		this.udpDstPortStartIdx = udpDstPortStartIdx;
	}
	public int getUdpDstPortEndIdx() {
		return udpDstPortEndIdx;
	}
	public void setUdpDstPortEndIdx(int udpDstPortEndIdx) {
		this.udpDstPortEndIdx = udpDstPortEndIdx;
	}
	public int getUdpLengthStartIdx() {
		return udpLengthStartIdx;
	}
	public void setUdpLengthStartIdx(int udpLengthStartIdx) {
		this.udpLengthStartIdx = udpLengthStartIdx;
	}
	public int getUdpLengthEndIdx() {
		return udpLengthEndIdx;
	}
	public void setUdpLengthEndIdx(int udpLengthEndIdx) {
		this.udpLengthEndIdx = udpLengthEndIdx;
	}
	public int getUdpChecksumStartIdx() {
		return udpChecksumStartIdx;
	}
	public void setUdpChecksumStartIdx(int udpChecksumStartIdx) {
		this.udpChecksumStartIdx = udpChecksumStartIdx;
	}
	public int getUdpChecksumEndIdx() {
		return udpChecksumEndIdx;
	}
	public void setUdpChecksumEndIdx(int udpChecksumEndIdx) {
		this.udpChecksumEndIdx = udpChecksumEndIdx;
	}

	private void setValue(int offset){	
		this.setUdpHeaderStartIdx(offset);
		this.setUdpHeaderEndIdx(offset     + 7 );
		this.setUdpSrcPortStartIdx(offset);
		this.setUdpSrcPortEndIdx(offset	   + 1 );
		this.setUdpDstPortStartIdx(offset  + 2 );
		this.setUdpDstPortEndIdx(offset    + 3 );
		this.setUdpLengthStartIdx(offset   + 4 );
		this.setUdpLengthEndIdx(offset     + 5 );
		this.setUdpChecksumStartIdx(offset + 6 );
		this.setUdpChecksumEndIdx(offset   + 7 );
	}
	
}
