package com.kglory.tms.web.model.dto;

import java.math.BigInteger;

public class AttackIpSearchDto extends SearchDto {
	
	private Integer		destPortInput;
	private Integer		nProtocol;
	private BigInteger	lCode;
	private String		destIp;
	private String		srcIp;
	private Integer		startRowSize;
	private Integer		endRowSize;
	
	private Long		sourceIp;
	private Long		destinationIp;
	
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
	
	public String getDestIp() {
		return destIp;
	}
	
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}
	
	public String getSrcIp() {
		return srcIp;
	}
	
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	
	public Integer getStartRowSize() {
		return startRowSize;
	}
	
	public void setStartRowSize(Integer startRowSize) {
		this.startRowSize = startRowSize;
	}
	
	public Integer getEndRowSize() {
		return endRowSize;
	}
	
	public void setEndRowSize(Integer endRowSize) {
		this.endRowSize = endRowSize;
	}
	
	public Long getSourceIp() {
		return sourceIp;
	}
	
	public void setSourceIp(Long sourceIp) {
		this.sourceIp = sourceIp;
	}
	
	public Long getDestinationIp() {
		return destinationIp;
	}
	
	public void setDestinationIp(Long destinationIp) {
		this.destinationIp = destinationIp;
	}
	
	@Override
	public String toString() {
		return "AttackIpSearchDto [destPortInput=" + destPortInput + ", nProtocol=" + nProtocol + ", lCode=" + lCode
				+ ", destIp=" + destIp + ", srcIp=" + srcIp + ", startRowSize=" + startRowSize + ", endRowSize="
				+ endRowSize + ", sourceIp=" + sourceIp + ", destinationIp=" + destinationIp + "]";
	}
	
}
