package com.kglory.tms.web.util.packet.idx;

public class MacHeaderIndex {
	
	private short macStartIdx;
	private short macEndtIdx;
	private short dstMacStartIdx;
	private short dstMacEndIdx;
	private short srcMacStartIdx;
	private short srcMacEndIdx;
	private short etherTypeStartIdx;
	private short etherTypeEndIdx;
	
	public MacHeaderIndex(){
		this.setValue();
	}
	
	public short getMacStartIdx() {
		return macStartIdx;
	}

	public void setMacStartIdx(short macStartIdx) {
		this.macStartIdx = macStartIdx;
	}

	public short getMacEndtIdx() {
		return macEndtIdx;
	}

	public void setMacEndtIdx(short macEndtIdx) {
		this.macEndtIdx = macEndtIdx;
	}

	public short getDstMacStartIdx() {
		return dstMacStartIdx;
	}

	public void setDstMacStartIdx(short dstMacStartIdx) {
		this.dstMacStartIdx = dstMacStartIdx;
	}

	public short getDstMacEndIdx() {
		return dstMacEndIdx;
	}

	public void setDstMacEndIdx(short dstMacEndIdx) {
		this.dstMacEndIdx = dstMacEndIdx;
	}

	public short getSrcMacStartIdx() {
		return srcMacStartIdx;
	}

	public void setSrcMacStartIdx(short srcMacStartIdx) {
		this.srcMacStartIdx = srcMacStartIdx;
	}

	public short getSrcMacEndIdx() {
		return srcMacEndIdx;
	}

	public void setSrcMacEndIdx(short srcMacEndIdx) {
		this.srcMacEndIdx = srcMacEndIdx;
	}

	public short getEtherTypeStartIdx() {
		return etherTypeStartIdx;
	}

	public void setEtherTypeStartIdx(short etherTypeStartIdx) {
		this.etherTypeStartIdx = etherTypeStartIdx;
	}

	public short getEtherTypeEndIdx() {
		return etherTypeEndIdx;
	}

	public void setEtherTypeEndIdx(short etherTypeEndIdx) {
		this.etherTypeEndIdx = etherTypeEndIdx;
	}

	private void setValue(){	
		this.setMacStartIdx(		(short)0	);
		this.setMacEndtIdx(			(short)13	);
		this.setDstMacStartIdx(		(short)0	);
		this.setDstMacEndIdx(		(short)5	);
		this.setSrcMacStartIdx(		(short)6	);
		this.setSrcMacEndIdx(		(short)11	);
		this.setEtherTypeStartIdx(	(short)12	);
		this.setEtherTypeEndIdx(	(short)13	);
	}
	
}
