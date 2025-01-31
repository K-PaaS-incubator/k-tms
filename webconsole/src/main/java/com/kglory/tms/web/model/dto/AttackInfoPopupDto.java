package com.kglory.tms.web.model.dto;

import java.util.List;

public class AttackInfoPopupDto {

    private Integer attackTypeSelect;
    private List<Integer> attackTypeLcodes;
    private Integer searchSelect;
    private String searchInput;

    private Boolean severityHCheck;
    private Boolean severityMCheck;
    private Boolean severityLCheck;
    private Boolean severityICheck;

    private List<Integer> severities;

    private Integer startRowSize;
    private Integer endRowSize;
    private Integer rowSize;

    public Integer getAttackTypeSelect() {
        return attackTypeSelect;
    }

    public void setAttackTypeSelect(Integer attackTypeSelect) {
        this.attackTypeSelect = attackTypeSelect;
    }

    public List<Integer> getAttackTypeLcodes() {
        return attackTypeLcodes;
    }

    public void setAttackTypeLcodes(List<Integer> attackTypeLcodes) {
        this.attackTypeLcodes = attackTypeLcodes;
    }

    public Integer getSearchSelect() {
        return searchSelect;
    }

    public void setSearchSelect(Integer searchSelect) {
        this.searchSelect = searchSelect;
    }

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
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

    public Integer getRowSize() {
        return rowSize;
    }

    public void setRowSize(Integer rowSize) {
        this.rowSize = rowSize;
    }

    @Override
    public String toString() {
        return "AttackInfoPopupDto [attackTypeSelect=" + attackTypeSelect
                + ", attackTypeLcodes=" + attackTypeLcodes + ", searchSelect="
                + searchSelect + ", searchInput=" + searchInput
                + ", severityHCheck=" + severityHCheck + ", severityMCheck="
                + severityMCheck + ", severityLCheck=" + severityLCheck
                + ", severityICheck=" + severityICheck + ", severities="
                + severities + ", startRowSize=" + startRowSize
                + ", endRowSize=" + endRowSize + "]";
    }
}
