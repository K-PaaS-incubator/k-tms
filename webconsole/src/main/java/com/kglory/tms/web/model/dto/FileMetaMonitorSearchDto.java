package com.kglory.tms.web.model.dto;

import java.math.BigInteger;

public class FileMetaMonitorSearchDto extends SearchDto {
	
	private BigInteger	lIndex;
	private Integer		srcPortInput;
	private Integer		destPortInput;
	private Integer		nProtocol;
	private Long		destinationIp;
	private Long		sourceIp;
	private String		strDestIp;
	private String		strSrcIp;
	private String 		fileNameInput;

	public BigInteger getlIndex() {
		return lIndex;
	}

	public void setlIndex(BigInteger lIndex) {
		this.lIndex = lIndex;
	}

	public Integer getSrcPortInput() {
		return srcPortInput;
	}

	public void setSrcPortInput(Integer srcPortInput) {
		this.srcPortInput = srcPortInput;
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

	public String getFileNameInput() {
		return fileNameInput;
	}

	public void setFileNameInput(String fileNameInput) {
		this.fileNameInput = fileNameInput;
	}

}
