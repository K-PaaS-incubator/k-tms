package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class AttackDto extends SearchDto {
	
	private String			attackNameInput;
	private List<String>	attackNames;
	
	private BigInteger		attackTypeSelect;
	private List<Integer>	attackTypeLcodes;
	
	private Boolean			severityHCheck;
	private Boolean			severityMCheck;
	private Boolean			severityLCheck;
	private Boolean			severityICheck;
	
	private List<Integer>	severities;
	
	private String			sortSelect;
	
	private String			thresholdSelect;
	private Long			thresholdNumInput;
	private BigInteger		destPortInput;
	
	private Integer			startRowSize;
	private Integer			endRowSize;
	
	private List<Integer>	lCodes;
	private Long			lCode;
	
	private String			listViewInput;
	
	private Long			graphItem;
	
	private String			protocol;
	private Integer			nProtocol;
	private String			destIp;
	private String			srcIp;
	private Long			sourceIp;
	private Long			destinationIp;
	private String			nation;
	
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
	
	public BigInteger getAttackTypeSelect() {
		return attackTypeSelect;
	}
	
	public void setAttackTypeSelect(BigInteger attackTypeSelect) {
		this.attackTypeSelect = attackTypeSelect;
	}
	
	public List<Integer> getAttackTypeLcodes() {
		return attackTypeLcodes;
	}
	
	public void setAttackTypeLcodes(List<Integer> attackTypeLcodes) {
		this.attackTypeLcodes = attackTypeLcodes;
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
	
	public BigInteger getDestPortInput() {
		return destPortInput;
	}
	
	public void setDestPortInput(BigInteger destPortInput) {
		this.destPortInput = destPortInput;
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
	
	public List<Integer> getlCodes() {
		return lCodes;
	}
	
	public void setlCodes(List<Integer> lCodes) {
		this.lCodes = lCodes;
	}
	
	public Long getlCode() {
		return lCode;
	}
	
	public void setlCode(Long lCode) {
		this.lCode = lCode;
	}
	
	public String getListViewInput() {
		return listViewInput;
	}
	
	public void setListViewInput(String listViewInput) {
		this.listViewInput = listViewInput;
	}
	
	public Long getGraphItem() {
		return graphItem;
	}
	
	public void setGraphItem(Long graphItem) {
		this.graphItem = graphItem;
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

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	

	@Override
	public String toString() {
		return "AttackDto [attackNameInput=" + attackNameInput
				+ ", attackNames=" + attackNames + ", attackTypeSelect="
				+ attackTypeSelect + ", attackTypeLcodes=" + attackTypeLcodes
				+ ", severityHCheck=" + severityHCheck + ", severityMCheck="
				+ severityMCheck + ", severityLCheck=" + severityLCheck
				+ ", severityICheck=" + severityICheck + ", severities="
				+ severities + ", sortSelect=" + sortSelect
				+ ", thresholdSelect=" + thresholdSelect
				+ ", thresholdNumInput=" + thresholdNumInput
				+ ", destPortInput=" + destPortInput + ", startRowSize="
				+ startRowSize + ", endRowSize=" + endRowSize + ", lCodes="
				+ lCodes + ", lCode=" + lCode + ", listViewInput="
				+ listViewInput + ", graphItem=" + graphItem + ", protocol="
				+ protocol + ", nProtocol=" + nProtocol + ", destIp=" + destIp
				+ ", srcIp=" + srcIp + ", sourceIp=" + sourceIp
				+ ", destinationIp=" + destinationIp + ", nation=" + nation
				+ "]";
	}
	
}
