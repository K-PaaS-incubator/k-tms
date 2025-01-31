package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class OriginalLogSearchDto extends SearchDto {

    BigInteger lnetgroupIndex;
    BigInteger lnetworkIndex;
    BigInteger lvsensorIndex;
    BigInteger lsensorIndex;

    String startDateInput;
    String endDateInput;

    String attackNameInput;
    List<String> attackNames;

    BigInteger attackTypeSelect;
    List<Integer> attackTypeLcodes;

    Boolean severityHCheck;
    Boolean severityMCheck;
    Boolean severityLCheck;
    Boolean severityICheck;

    List<Integer> severities;
    String toIpInput;
    String fromIpInput;
    Long toIp;
    Long fromIp;
    Integer maskInput;
    Integer winBoundSelect;
    BigInteger destPortInput;
    BigInteger attackPortInput;
    String simpleTimeSelect;
    Integer endRowSize;
    List<Integer> lCodes;
    Long lCode;
    String listViewInput;
    List<String> tableNames;

    String protocol;
    Integer nProtocol;
    String destIp;
    String srcIp;
    Long sourceIp;
    Long destinationIp;
    Long lIndex;

    List<String> tableNamesSecond;
    String encodingType;
    String strData;
    Long ucCreateLogType;

    public BigInteger getLnetgroupIndex() {
        return lnetgroupIndex;
    }

    public void setLnetgroupIndex(BigInteger lnetgroupIndex) {
        this.lnetgroupIndex = lnetgroupIndex;
    }

    public BigInteger getLnetworkIndex() {
        return lnetworkIndex;
    }

    public void setLnetworkIndex(BigInteger lnetworkIndex) {
        this.lnetworkIndex = lnetworkIndex;
    }

    public BigInteger getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(BigInteger lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public BigInteger getLsensorIndex() {
        return lsensorIndex;
    }

    public void setLsensorIndex(BigInteger lsensorIndex) {
        this.lsensorIndex = lsensorIndex;
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

    public Integer getMaskInput() {
        return maskInput;
    }

    public void setMaskInput(Integer maskInput) {
        this.maskInput = maskInput;
    }

    public Integer getWinBoundSelect() {
        return winBoundSelect;
    }

    public void setWinBoundSelect(Integer winBoundSelect) {
        this.winBoundSelect = winBoundSelect;
    }

    public BigInteger getDestPortInput() {
        return destPortInput;
    }

    public void setDestPortInput(BigInteger destPortInput) {
        this.destPortInput = destPortInput;
    }

    public BigInteger getAttackPortInput() {
        return attackPortInput;
    }

    public void setAttackPortInput(BigInteger attackPortInput) {
        this.attackPortInput = attackPortInput;
    }

    public String getSimpleTimeSelect() {
        return simpleTimeSelect;
    }

    public void setSimpleTimeSelect(String simpleTimeSelect) {
        this.simpleTimeSelect = simpleTimeSelect;
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

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
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

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public List<String> getTableNamesSecond() {
        return tableNamesSecond;
    }

    public void setTableNamesSecond(List<String> tableNamesSecond) {
        this.tableNamesSecond = tableNamesSecond;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public Long getUcCreateLogType() {
        return ucCreateLogType;
    }

    public void setUcCreateLogType(Long ucCreateLogType) {
        this.ucCreateLogType = ucCreateLogType;
    }
}
