package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class AttackIpDto extends SearchDto {
	private String			listViewInput;
	
	private String			attackNameInput;
	private List<String>	attackNames;
	private List<Integer>	lCodes;
	private BigInteger		lCode;
	
	private Integer			attackTypeSelect;
	private Integer			severityCheck;
	
	private String			sortSelect;
	
	private String			thresholdSelect;
	private Long			thresholdNumInput;
	private Integer			destPortInput;
	private String			protocol;
	private Integer			nProtocol;
	
	private String			toIpInput;
	private String			fromIpInput;
	private Long			toIp;
	private Long			fromIp;
	
	private String			destIp;
	private String			srcIp;
	private Long			sourceIp;
	private Long			destinationIp;
	
	private Integer			startRowSize;
	private Integer			endRowSize;
	
	private String			graphItem;
	private Long			graphLongItem;
	private String			isDownload;
	
	
	public String getListViewInput() {
		return listViewInput;
	}

	public void setListViewInput(String listViewInput) {
		this.listViewInput = listViewInput;
	}

	public String getAttackNameInput() {
		return attackNameInput;
	}
	
	public void setAttackNameInput(String attackNameInput) {
		this.attackNameInput = attackNameInput;
	}
	
	public List<String> getAttackNames() {
		return attackNames;
	}
	
	public void setAttackNames(List<String> attackNames) {
		this.attackNames = attackNames;
	}
	
	public List<Integer> getlCodes() {
		return lCodes;
	}
	
	public void setlCodes(List<Integer> lCodes) {
		this.lCodes = lCodes;
	}
	
	public BigInteger getlCode() {
		return lCode;
	}
	
	public void setlCode(BigInteger lCode) {
		this.lCode = lCode;
	}
	
	public Integer getAttackTypeSelect() {
		return attackTypeSelect;
	}
	
	public void setAttackTypeSelect(Integer attackTypeSelect) {
		this.attackTypeSelect = attackTypeSelect;
	}
	
	public Integer getSeverityCheck() {
		return severityCheck;
	}
	
	public void setSeverityCheck(Integer severityCheck) {
		this.severityCheck = severityCheck;
	}
	
	public String getSortSelect() {
		return sortSelect;
	}
	
	public void setSortSelect(String sortSelect) {
		this.sortSelect = sortSelect;
	}
	
	public String getThresholdSelect() {
		return thresholdSelect;
	}
	
	public void setThresholdSelect(String thresholdSelect) {
		this.thresholdSelect = thresholdSelect;
	}
	
	public Long getThresholdNumInput() {
		return thresholdNumInput;
	}
	
	public void setThresholdNumInput(Long thresholdNumInput) {
		this.thresholdNumInput = thresholdNumInput;
	}
	
	public Integer getDestPortInput() {
		return destPortInput;
	}
	
	public void setDestPortInput(Integer destPortInput) {
		this.destPortInput = destPortInput;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public Integer getnProtocol() {
		return nProtocol;
	}
	
	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}
	
	public String getToIpInput() {
		return toIpInput;
	}
	
	public void setToIpInput(String toIpInput) {
		this.toIpInput = toIpInput;
	}
	
	public String getFromIpInput() {
		return fromIpInput;
	}
	
	public void setFromIpInput(String fromIpInput) {
		this.fromIpInput = fromIpInput;
	}
	
	public Long getToIp() {
		return toIp;
	}
	
	public void setToIp(Long toIp) {
		this.toIp = toIp;
	}
	
	public Long getFromIp() {
		return fromIp;
	}
	
	public void setFromIp(Long fromIp) {
		this.fromIp = fromIp;
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
	
	public String getGraphItem() {
		return graphItem;
	}
	
	public void setGraphItem(String graphItem) {
		this.graphItem = graphItem;
	}
	
	public Long getGraphLongItem() {
		return graphLongItem;
	}
	
	public void setGraphLongItem(Long graphLongItem) {
		this.graphLongItem = graphLongItem;
	}
	
	public String getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(String isDownload) {
		this.isDownload = isDownload;
	}

	@Override
	public String toString() {
		return "AttackIpDto [ListViewInput=" + listViewInput + ", attackNameInput=" + attackNameInput
				+ ", attackNames=" + attackNames + ", lCodes=" + lCodes + ", lCode=" + lCode + ", attackTypeSelect="
				+ attackTypeSelect + ", severityCheck=" + severityCheck + ", sortSelect=" + sortSelect
				+ ", thresholdSelect=" + thresholdSelect + ", thresholdNumInput=" + thresholdNumInput
				+ ", destPortInput=" + destPortInput + ", protocol=" + protocol + ", nProtocol=" + nProtocol
				+ ", toIpInput=" + toIpInput + ", fromIpInput=" + fromIpInput + ", toIp=" + toIp + ", fromIp=" + fromIp
				+ ", destIp=" + destIp + ", srcIp=" + srcIp + ", sourceIp=" + sourceIp + ", destinationIp="
				+ destinationIp + ", startRowSize=" + startRowSize + ", endRowSize=" + endRowSize + ", graphItem="
				+ graphItem + ", graphLongItem=" + graphLongItem + ", isDownload="+ isDownload +"]";
	}
	
}