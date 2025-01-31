package com.kglory.tms.web.model.dto;

public class SessionMonitorPolicyFile {

	private long	sessionIndex;
	private long	sessionVsensorIndex;
	private long	nPort;
	private long	nRenewOption;
	
	public long getSessionIndex() {
		return sessionIndex;
	}

	public void setSessionIndex(long sessionIndex) {
		this.sessionIndex = sessionIndex;
	}

	public long getSessionVsensorIndex() {
		return sessionVsensorIndex;
	}

	public void setSessionVsensorIndex(long sessionVsensorIndex) {
		this.sessionVsensorIndex = sessionVsensorIndex;
	}

	public long getnPort() {
		return nPort;
	}

	public void setnPort(long nPort) {
		this.nPort = nPort;
	}

	public long getnRenewOption() {
		return nRenewOption;
	}

	public void setnRenewOption(long nRenewOption) {
		this.nRenewOption = nRenewOption;
	}

	@Override
	public String toString() {
		return "SessionMonitorPolicyFile [sessionIndex=" + sessionIndex
				+ ", sessionVsensorIndex=" + sessionVsensorIndex + ", nPort="
				+ nPort + ", nRenewOption=" + nRenewOption + "]";
	}	
}
