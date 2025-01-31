package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class VictimPortDto extends SearchDto {
	String			attackNameInput;
	List<String>	attackNames;
	String			sortSelect;
	String			thresholdSelect;
	Long			thresholdNumInput;
	Integer			destPortInput;
	Integer			startRowSize;
	Integer			endRowSize;
	String			listViewInput;
	BigInteger		graphItem;
	BigInteger		graphPlusItem;
	BigInteger		lCode;
	String			protocol;
	Integer			nProtocol;
	String			destIp;
	Long			destinationIp;
	String			srcIp;
	Long			sourceIp;
	
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
	
	public String getListViewInput() {
		return listViewInput;
	}
	
	public void setListViewInput(String listViewInput) {
		this.listViewInput = listViewInput;
	}
	
	public BigInteger getGraphItem() {
		return graphItem;
	}
	
	public void setGraphItem(BigInteger graphItem) {
		this.graphItem = graphItem;
	}
	
	public BigInteger getlCode() {
		return lCode;
	}
	
	public void setlCode(BigInteger lCode) {
		this.lCode = lCode;
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
	
	public Long getDestinationIp() {
		return destinationIp;
	}
	
	public void setDestinationIp(Long destinationIp) {
		this.destinationIp = destinationIp;
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
	
	public BigInteger getGraphPlusItem() {
		return graphPlusItem;
	}

	public void setGraphPlusItem(BigInteger graphPlusItem) {
		this.graphPlusItem = graphPlusItem;
	}

	@Override
	public String toString() {
		return "VictimPortDto [attackNameInput=" + attackNameInput
				+ ", attackNames=" + attackNames + ", sortSelect=" + sortSelect
				+ ", thresholdSelect=" + thresholdSelect
				+ ", thresholdNumInput=" + thresholdNumInput
				+ ", destPortInput=" + destPortInput + ", startRowSize="
				+ startRowSize + ", endRowSize=" + endRowSize
				+ ", listViewInput=" + listViewInput + ", graphItem="
				+ graphItem + ", graphPlusItem=" + graphPlusItem + ", lCode="
				+ lCode + ", protocol=" + protocol + ", nProtocol=" + nProtocol
				+ ", destIp=" + destIp + ", destinationIp=" + destinationIp
				+ ", srcIp=" + srcIp + ", sourceIp=" + sourceIp + "]";
	}
	
}
