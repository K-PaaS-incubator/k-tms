package com.kglory.tms.web.model.dto;

public class TcpFlagDto extends SearchDto {
	private Long			graphItem;
	
	private Integer			startRowSize;
	private Integer			endRowSize;
	
	private String			sortSelect;

	public Long getGraphItem() {
		return graphItem;
	}

	public void setGraphItem(Long graphItem) {
		this.graphItem = graphItem;
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

	public String getSortSelect() {
		return sortSelect;
	}

	public void setSortSelect(String sortSelect) {
		this.sortSelect = sortSelect;
	}
	
	@Override
	public String toString() {
		return "TcpFlagDto [graphItem=" + graphItem + ", startRowSize="
				+ startRowSize + ", endRowSize=" + endRowSize + ", sortSelect="
				+ sortSelect + "]";
	}
	
}