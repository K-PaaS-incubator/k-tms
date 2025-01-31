package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class AuditDto extends CommonBean {
	private String			tmstart;
	private String			tmend;
	private String			strContent;
	private String			strOperator;
	private long			lAuditSetIndex;
	private long			type1;
	private long			type2;
	private String			strComment;
	private String 			startDateInput;
	private String 			endDateInput;
	private String 			listViewInput;
	private int 			auditType;
	private List<String> 	tableNames;
	private String			tableName;
	private long			topN;
	private int				startRowSize;
	private int				endRowSize;
	private String			isDownload;
	
	public String getTmstart() {
		return tmstart;
	}

	public void setTmstart(String tmstart) {
		this.tmstart = tmstart;
	}

	public String getTmend() {
		return tmend;
	}

	public void setTmend(String tmend) {
		this.tmend = tmend;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}

	public String getStrOperator() {
		return strOperator;
	}

	public void setStrOperator(String strOperator) {
		this.strOperator = strOperator;
	}

	public long getlAuditSetIndex() {
		return lAuditSetIndex;
	}

	public void setlAuditSetIndex(long lAuditSetIndex) {
		this.lAuditSetIndex = lAuditSetIndex;
	}

	public long getType1() {
		return type1;
	}

	public void setType1(long type1) {
		this.type1 = type1;
	}

	public long getType2() {
		return type2;
	}

	public void setType2(long type2) {
		this.type2 = type2;
	}

	public String getStrComment() {
		return strComment;
	}

	public void setStrComment(String strComment) {
		this.strComment = strComment;
	}

	public String getStartDateInput() {
		return startDateInput;
	}

	public void setStartDateInput(String startDateInput) {
		this.startDateInput = startDateInput;
	}

	public String getEndDateInput() {
		return endDateInput;
	}

	public void setEndDateInput(String endDateInput) {
		this.endDateInput = endDateInput;
	}

	public String getListViewInput() {
		return listViewInput;
	}

	public void setListViewInput(String listViewInput) {
		this.listViewInput = listViewInput;
	}

	public int getAuditType() {
		return auditType;
	}

	public void setAuditType(int auditType) {
		this.auditType = auditType;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getTopN() {
		return topN;
	}

	public void setTopN(long topN) {
		this.topN = topN;
	}

	public int getStartRowSize() {
		return startRowSize;
	}

	public void setStartRowSize(int startRowSize) {
		this.startRowSize = startRowSize;
	}

	public int getEndRowSize() {
		return endRowSize;
	}

	public void setEndRowSize(int endRowSize) {
		this.endRowSize = endRowSize;
	}
	
	public String getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(String isDownload) {
		this.isDownload = isDownload;
	}

	@Override
	public String toString() {
		return "AuditDto [tmstart=" + tmstart + ", tmend=" + tmend
				+ ", strContent=" + strContent + ", strOperator=" + strOperator
				+ ", lAuditSetIndex=" + lAuditSetIndex + ", type1=" + type1
				+ ", type2=" + type2 + ", strComment=" + strComment
				+ ", startDateInput=" + startDateInput + ", endDateInput="
				+ endDateInput + ", listViewInput=" + listViewInput
				+ ", auditType=" + auditType + ", tableNames=" + tableNames
				+ ", tableName=" + tableName + ", topN=" + topN
				+ ", startRowSize=" + startRowSize + ", endRowSize="
				+ endRowSize + ", isDownload=" + isDownload + "]";
	}
}
