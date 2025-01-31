package com.kglory.tms.web.model.dto;

import java.math.BigInteger;

public class TrafficServiceDto extends SearchDto {
	private Long				destPortInput;
	private Integer			protocolSelect;
	private String			thresholdSelect;
	private long			thresholdNumInput;
	private String			sortSelect;
	
	private Integer			startRowSize;
	private Integer			endRowSize;

	private String			graphItem;
	private BigInteger		graphPlusItem;
	private Integer 		nProtocol;
	private Integer 		wService;
	private long 			timeDifference;
	
	private Integer			ipSelectboxValue;

	public Long getDestPortInput() {
		return destPortInput;
	}

	public void setDestPortInput(Long destPortInput) {
		this.destPortInput = destPortInput;
	}

	public Integer getProtocolSelect() {
		return protocolSelect;
	}

	public void setProtocolSelect(Integer protocolSelect) {
		this.protocolSelect = protocolSelect;
	}

	public String getThresholdSelect() {
		return thresholdSelect;
	}

	public void setThresholdSelect(String thresholdSelect) {
		this.thresholdSelect = thresholdSelect;
	}

	public long getThresholdNumInput() {
		return thresholdNumInput;
	}

	public void setThresholdNumInput(long thresholdNumInput) {
		this.thresholdNumInput = thresholdNumInput;
	}

	public String getSortSelect() {
		return sortSelect;
	}

	public void setSortSelect(String sortSelect) {
		this.sortSelect = sortSelect;
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

	public Integer getnProtocol() {
		return nProtocol;
	}

	public void setnProtocol(Integer nProtocol) {
		this.nProtocol = nProtocol;
	}

	public Integer getwService() {
		return wService;
	}

	public void setwService(Integer wService) {
		this.wService = wService;
	}

	public long getTimeDifference() {
		return timeDifference;
	}

	public void setTimeDifference(long timeDifference) {
		this.timeDifference = timeDifference;
	}

	public Integer getIpSelectboxValue() {
		return ipSelectboxValue;
	}

	public void setIpSelectboxValue(Integer ipSelectboxValue) {
		this.ipSelectboxValue = ipSelectboxValue;
	}

	public BigInteger getGraphPlusItem() {
		return graphPlusItem;
	}

	public void setGraphPlusItem(BigInteger graphPlusItem) {
		this.graphPlusItem = graphPlusItem;
	}
}