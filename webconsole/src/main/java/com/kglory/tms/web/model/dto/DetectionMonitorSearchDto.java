package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class DetectionMonitorSearchDto extends SearchDto {
	private BigInteger	lIndex;
	private Integer		srcPortInput;
	private Integer		destPortInput;
	private Integer		nProtocol;
	private BigInteger	lCode;
	private String		attackSearchKeyword;
	private Long		destinationIp;
	private Long		sourceIp;
	private String		strDestIp;
	private String		strSrcIp;
	private Long		attackTypeSelect;
    private String      attackType;
	
	private Boolean severityHCheck;
	private Boolean severityMCheck;
	private Boolean severityLCheck;
	private Boolean severityICheck;
	
	private List<Integer> severities;
	private Long ipType;
	private Long ucCreateLogType;
	
	public BigInteger getlIndex() {
		return lIndex;
	}
	
	public void setlIndex(BigInteger lIndex) {
		this.lIndex = lIndex;
	}
	
	public Integer getDestPortInput() {
		return destPortInput;
	}
	
	public void setDestPortInput(Integer destPortInput) {
		this.destPortInput = destPortInput;
	}
	
	public Integer getnProtocol() {
		return nProtocol;
	}
	
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}
	
	public BigInteger getlCode() {
		return lCode;
	}
	
	public void setlCode(BigInteger lCode) {
		this.lCode = lCode;
	}
	
	public String getAttackSearchKeyword() {
		return attackSearchKeyword;
	}
	
	public void setAttackSearchKeyword(String attackSearchKeyword) {
		this.attackSearchKeyword = attackSearchKeyword;
	}
	
	public Long getDestinationIp() {
		return destinationIp;
	}
	
	public void setDestinationIp(Long destinationIp) {
		this.destinationIp = destinationIp;
	}
	
	public Long getSourceIp() {
		return sourceIp;
	}
	
	public void setSourceIp(Long sourceIp) {
		this.sourceIp = sourceIp;
	}
	
	public String getStrDestIp() {
		return strDestIp;
	}
	
	public void setStrDestIp(String strDestIp) {
		this.strDestIp = strDestIp;
	}
	
	public String getStrSrcIp() {
		return strSrcIp;
	}
	
	public void setStrSrcIp(String strSrcIp) {
		this.strSrcIp = strSrcIp;
	}
	
	public Integer getSrcPortInput() {
		return srcPortInput;
	}

	public void setSrcPortInput(Integer srcPortInput) {
		this.srcPortInput = srcPortInput;
	}

	public Long getAttackTypeSelect() {
		return attackTypeSelect;
	}

	public void setAttackTypeSelect(Long attackTypeSelect) {
		this.attackTypeSelect = attackTypeSelect;
	}

	public Boolean getSeverityHCheck() {
		return severityHCheck;
	}

	public void setSeverityHCheck(Boolean severityHCheck) {
		this.severityHCheck = severityHCheck;
	}

	public Boolean getSeverityMCheck() {
		return severityMCheck;
	}

	public void setSeverityMCheck(Boolean severityMCheck) {
		this.severityMCheck = severityMCheck;
	}

	public Boolean getSeverityLCheck() {
		return severityLCheck;
	}

	public void setSeverityLCheck(Boolean severityLCheck) {
		this.severityLCheck = severityLCheck;
	}

	public Boolean getSeverityICheck() {
		return severityICheck;
	}

	public void setSeverityICheck(Boolean severityICheck) {
		this.severityICheck = severityICheck;
	}

	public List<Integer> getSeverities() {
		return severities;
	}

	public void setSeverities(List<Integer> severities) {
		this.severities = severities;
	}

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

	public Long getIpType() {
		return ipType;
	}

	public void setIpType(Long ipType) {
		this.ipType = ipType;
	}

	public Long getUcCreateLogType() {
		return ucCreateLogType;
	}

	public void setUcCreateLogType(Long ucCreateLogType) {
		this.ucCreateLogType = ucCreateLogType;
	}

	@Override
	public String toString() {
		return "DetectionMonitorSearchDto [lIndex=" + lIndex
				+ ", srcPortInput=" + srcPortInput + ", destPortInput="
				+ destPortInput + ", nProtocol=" + nProtocol + ", lCode="
				+ lCode + ", attackSearchKeyword=" + attackSearchKeyword
				+ ", destinationIp=" + destinationIp + ", sourceIp=" + sourceIp
				+ ", strDestIp=" + strDestIp + ", strSrcIp=" + strSrcIp
				+ ", attackTypeSelect=" + attackTypeSelect + ", attackType="
				+ attackType + ", severityHCheck=" + severityHCheck
				+ ", severityMCheck=" + severityMCheck + ", severityLCheck="
				+ severityLCheck + ", severityICheck=" + severityICheck
				+ ", severities=" + severities + ", ipType=" + ipType
				+ ", ucCreateLogType=" + ucCreateLogType + "]";
	}
}
