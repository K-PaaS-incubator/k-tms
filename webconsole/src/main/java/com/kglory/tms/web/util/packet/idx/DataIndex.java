package com.kglory.tms.web.util.packet.idx;

public class DataIndex {
	private int dataStartIdx;
	private int dataEndIdx;
	
	public DataIndex(int offset, int endOffset){
		this.setValue(offset, endOffset);
	}
	
	public int getDataStartIdx() {
		return dataStartIdx;
	}
	public void setDataStartIdx(int dataStartIdx) {
		this.dataStartIdx = dataStartIdx;
	}
	public int getDataEndIdx() {
		return dataEndIdx;
	}
	public void setDataEndIdx(int dataEndIdx) {
		this.dataEndIdx = dataEndIdx;
	}
	
	private void setValue(int offset, int endOffset){
		this.setDataStartIdx(offset);
		this.setDataEndIdx(endOffset);	
	}
	
}
