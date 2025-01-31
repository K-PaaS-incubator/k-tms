package com.kglory.tms.web.model.dto;

import java.math.BigInteger;

public class DetectionAttackHelpDto extends DetectionMonitorSearchDto{

	private BigInteger lIndex;

	public BigInteger getlIndex() {
		return lIndex;
	}

	public void setlIndex(BigInteger lIndex) {
		this.lIndex = lIndex;
	}

	@Override
	public String toString() {
		return "DetectionAttackHelpDto [lIndex=" + lIndex
				+ ", getLnetgroupIndex()=" + getLnetgroupIndex()
				+ ", getLnetworkIndex()=" + getLnetworkIndex()
				+ ", getLvsensorIndex()=" + getLvsensorIndex()
				+ ", getLsensorIndex()=" + getLsensorIndex()
				+ ", getDestPortInput()=" + getDestPortInput()
				+ ", getnProtocol()=" + getnProtocol() + ", getlCode()="
				+ getlCode() + ", getDestinationIp()=" + getDestinationIp()
				+ ", getSourceIp()=" + getSourceIp() + ", getStrDestIp()="
				+ getStrDestIp() + ", getStrSrcIp()=" + getStrSrcIp()
				+ ", toString()=" + super.toString() + ", getStartDateInput()="
				+ getStartDateInput() + ", getEndDateInput()="
				+ getEndDateInput() + ", getWinBoundSelect()="
				+ getWinBoundSelect() + ", getQueryTableName()="
				+ getQueryTableName() + ", getTableNames()=" + getTableNames()
				+ ", getAepTableNames()=" + getAepTableNames()
				+ ", getVepTableNames()=" + getVepTableNames()
				+ ", getReturnType()=" + getReturnType() + ", getErrorCode()="
				+ getErrorCode() + ", getErrorMessage()=" + getErrorMessage()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
}
