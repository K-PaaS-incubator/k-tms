package com.kglory.tms.web.util.packet.idx;

public class PatternDetectedIndex {
	private int dwMaliciousSrvFrame;
	private int dwMaliciousSrvByte;
	
	public PatternDetectedIndex( int dwMaliciousSrvFrame, int dwMaliciousSrvByte){
		this.setValue(dwMaliciousSrvFrame, dwMaliciousSrvByte);
	}

	public int getDwMaliciousSrvFrame() {
		return dwMaliciousSrvFrame;
	}

	public void setDwMaliciousSrvFrame(int dwMaliciousSrvFrame) {
		this.dwMaliciousSrvFrame = dwMaliciousSrvFrame;
	}

	public int getDwMaliciousSrvByte() {
		return dwMaliciousSrvByte;
	}

	public void setDwMaliciousSrvByte(int dwMaliciousSrvByte) {
		this.dwMaliciousSrvByte = dwMaliciousSrvByte;
	}

	private void setValue(int dwMaliciousSrvFrame, int dwMaliciousSrvByte){
		this.setDwMaliciousSrvFrame(dwMaliciousSrvFrame);
		this.setDwMaliciousSrvByte(dwMaliciousSrvByte);
	}
}
