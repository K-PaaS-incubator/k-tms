package com.kglory.tms.web.model.dto;

public class VirtualSensorFile {

	private long lIndex;
	private long lvsensorIndex;
	
	public long getlIndex() {
		return lIndex;
	}
	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
	}
	public long getLvsensorIndex() {
		return lvsensorIndex;
	}
	public void setLvsensorIndex(long lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}
	@Override
	public String toString() {
		return "VirtualSensorFile [lIndex=" + lIndex + ", lvsensorIndex="
				+ lvsensorIndex + "]";
	}
	
}
