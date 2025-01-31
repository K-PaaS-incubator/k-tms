package com.kglory.tms.web.model.dto;

public class SessionMonitoringFile {
	
	Long lIndex;
	Long lvsensorIndex;
	Long nPort;
	Long nRenewOption;
	
	
	public Long getlIndex() {
		return lIndex;
	}

	public void setlIndex(Long lIndex) {
		this.lIndex = lIndex;
	}

	public Long getLvsensorIndex() {
		return lvsensorIndex;
	}

	public void setLvsensorIndex(Long lvsensorIndex) {
		this.lvsensorIndex = lvsensorIndex;
	}

	public Long getnPort() {
		return nPort;
	}

	public void setnPort(Long nPort) {
		this.nPort = nPort;
	}

	public Long getnRenewOption() {
		return nRenewOption;
	}

	public void setnRenewOption(Long nRenewOption) {
		this.nRenewOption = nRenewOption;
	}

	@Override
	public String toString() {
		return "SessionMonitoringFile [lIndex=" + lIndex + ", lvsensorIndex="
				+ lvsensorIndex + ", nPort=" + nPort + ", nRenewOption="
				+ nRenewOption + "]";
	}
}
