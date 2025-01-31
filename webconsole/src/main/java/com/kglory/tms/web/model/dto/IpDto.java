package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class IpDto extends SearchDto {

    List<String> tableNames;

    String startDateInput;
    String endDateInput;
    String listViewInput;

    int endRowSize;

    String toIpInput;
    String fromIpInput;

    Long toIp;
    Long fromIp;

    String graphItem;
    Long graphLongItem;

    Long longIp;

    Long timeDifference; //endDate - startDate 분으로 환산 합계의 평균을 구할때 사용
    String ip;
    String ipInput;
    private List<BigInteger> lNetworkList;

    public Long getLongIp() {
        return longIp;
    }

    public void setLongIp(Long longIp) {
        this.longIp = longIp;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
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

    public int getEndRowSize() {
        return endRowSize;
    }

    public void setEndRowSize(int endRowSize) {
        this.endRowSize = endRowSize;
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

    public Long getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(Long timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpInput() {
        return ipInput;
    }

    public void setIpInput(String ipInput) {
        this.ipInput = ipInput;
    }

    public List<BigInteger> getlNetworkList() {
        return lNetworkList;
    }

    public void setlNetworkList(List<BigInteger> lNetworkList) {
        this.lNetworkList = lNetworkList;
    }
}
