package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.MessageUtil;

public class SearchDto extends CommonBean {

    private BigInteger lnetgroupIndex;
    private BigInteger lnetworkIndex;
    private BigInteger lvsensorIndex;
    private BigInteger lsensorIndex;
    private String defaultPath = MessageUtil.getMessage("all.string");
    private String lnetgroupName = MessageUtil.getMessage("target.networkGroup");
    private String lnetworkName = MessageUtil.getMessage("target.network");
    private String lvsensorName = MessageUtil.getMessage("target.vSensor");
    private String lsensorName = MessageUtil.getMessage("target.sensor");
    private Integer indexType;

    private String startDateInput;
    private String endDateInput;

    private String startDateBeforeInput;
    private String endDateBeforeInput;

    private Integer winBoundSelect;
    private String strWinbound;

    private Integer ucCreateLogTypeSelect;
    private String strUcCreateLogType;

    private String queryTableName;
    private String querySubTableName;
    private List<String> tableNames;
    private List<String> tableBeforeNames;
    private List<String> aepTableNames;
    private List<String> vepTableNames;
    private List<String> ipv6TableNames;

    private String isDownload;

    private long avgTime;

    private String tableUnit;
    private Long timeDiffSecond;
    private Integer monitorLimitLength = 100;  //모니터링 제한 수

    private String pathName;
    private String strSeverity;
    private String strType;
    private String strUcType;
    private String strUcSubType;
    private String strSession;
    private Long ipType = 4L;
    private String nation;  // 국가 이름
    private String strSourceIp;
    private String strDestinationIp;
    
    private Integer startRowSize;
    private Integer rowSize;

    public Integer getIndexType() {
        return indexType;
    }

    public void setIndexType(Integer indexType) {
        this.indexType = indexType;
    }

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

    public Integer getWinBoundSelect() {
        return winBoundSelect;
    }

    public void setWinBoundSelect(Integer winBoundSelect) {
        this.winBoundSelect = winBoundSelect;
    }

    public String getQueryTableName() {
        return queryTableName;
    }

    public String getQuerySubTableName() {
        return querySubTableName;
    }

    public void setQuerySubTableName(String querySubTableName) {
        this.querySubTableName = querySubTableName;
    }

    public void setQueryTableName(String queryTableName) {
        this.queryTableName = queryTableName;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public List<String> getAepTableNames() {
        return aepTableNames;
    }

    public void setAepTableNames(List<String> aepTableNames) {
        this.aepTableNames = aepTableNames;
    }

    public List<String> getVepTableNames() {
        return vepTableNames;
    }

    public void setVepTableNames(List<String> vepTableNames) {
        this.vepTableNames = vepTableNames;
    }

    public String getStartDateBeforeInput() {
        return startDateBeforeInput;
    }

    public void setStartDateBeforeInput(String startDateBeforeInput) {
        this.startDateBeforeInput = startDateBeforeInput;
    }

    public String getEndDateBeforeInput() {
        return endDateBeforeInput;
    }

    public void setEndDateBeforeInput(String endDateBeforeInput) {
        this.endDateBeforeInput = endDateBeforeInput;
    }

    public List<String> getTableBeforeNames() {
        return tableBeforeNames;
    }

    public void setTableBeforeNames(List<String> tableBeforeNames) {
        this.tableBeforeNames = tableBeforeNames;
    }

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
    }

    public long getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(long avgTime) {
        this.avgTime = avgTime;
    }

    public String getTableUnit() {
        return tableUnit;
    }

    public void setTableUnit(String tableUnit) {
        this.tableUnit = tableUnit;
    }

    public Long getTimeDiffSecond() {
        return timeDiffSecond;
    }

    public void setTimeDiffSecond(Long timeDiffSecond) {
        this.timeDiffSecond = timeDiffSecond;
    }

    public String getStrWinbound() {
        return strWinbound;
    }

    public void setStrWinbound(String strWinbound) {
        this.strWinbound = strWinbound;
    }

    public Integer getUcCreateLogTypeSelect() {
        return ucCreateLogTypeSelect;
    }

    public void setUcCreateLogTypeSelect(Integer ucCreateLogTypeSelect) {
        this.ucCreateLogTypeSelect = ucCreateLogTypeSelect;
    }

    public String getStrUcCreateLogType() {
        return strUcCreateLogType;
    }

    public void setStrUcCreateLogType(String strUcCreateLogType) {
        this.strUcCreateLogType = strUcCreateLogType;
    }

    public List<String> getIpv6TableNames() {
        return ipv6TableNames;
    }

    public void setIpv6TableNames(List<String> ipv6TableNames) {
        this.ipv6TableNames = ipv6TableNames;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getStrSeverity() {
        return strSeverity;
    }

    public void setStrSeverity(String strSeverity) {
        this.strSeverity = strSeverity;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrUcType() {
        return strUcType;
    }

    public void setStrUcType(String strUcType) {
        this.strUcType = strUcType;
    }

    public String getStrUcSubType() {
        return strUcSubType;
    }

    public void setStrUcSubType(String strUcSubType) {
        this.strUcSubType = strUcSubType;
    }

    public String getStrSession() {
        return strSession;
    }

    public void setStrSession(String strSession) {
        this.strSession = strSession;
    }

    public Long getIpType() {
        return ipType;
    }

    public void setIpType(Long ipType) {
        this.ipType = ipType;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getStrSourceIp() {
        return strSourceIp;
    }

    public void setStrSourceIp(String strSourceIp) {
        this.strSourceIp = strSourceIp;
    }

    public String getStrDestinationIp() {
        return strDestinationIp;
    }

    public void setStrDestinationIp(String strDestinationIp) {
        this.strDestinationIp = strDestinationIp;
    }

    public Integer getMonitorLimitLength() {
        return monitorLimitLength;
    }

    public void setMonitorLimitLength(Integer monitorLimitLength) {
        this.monitorLimitLength = monitorLimitLength;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String getLnetgroupName() {
        return lnetgroupName;
    }

    public void setLnetgroupName(String lnetgroupName) {
        this.lnetgroupName = lnetgroupName;
    }

    public String getLnetworkName() {
        return lnetworkName;
    }

    public void setLnetworkName(String lnetworkName) {
        this.lnetworkName = lnetworkName;
    }

    public String getLvsensorName() {
        return lvsensorName;
    }

    public void setLvsensorName(String lvsensorName) {
        this.lvsensorName = lvsensorName;
    }

    public String getLsensorName() {
        return lsensorName;
    }

    public void setLsensorName(String lsensorName) {
        this.lsensorName = lsensorName;
    }

    public Integer getStartRowSize() {
        return startRowSize;
    }

    public void setStartRowSize(Integer startRowSize) {
        this.startRowSize = startRowSize;
    }
}
