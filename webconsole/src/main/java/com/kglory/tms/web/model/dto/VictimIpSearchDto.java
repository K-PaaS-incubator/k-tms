package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class VictimIpSearchDto extends SearchDto {

    private String sortSelect;
    private String simpleTimeSelect;

    private String toIpInput;
    private String fromIpInput;
    private Long toIp;
    private Long fromIp;
    private int maskInput;

    private String attackNameInput;
    private List<String> attackNames;
    private List<Integer> lCodes;
    private BigInteger lCode;

    private String thresholdSelect;
    private long thresholdNumInput;
    private BigInteger destPortInput;
    private long sLcodeCount;
    private long sPortCount;
    private long nSum;
    private long bps;
    private int endRowSize;

    private String listViewInput;

    private String graphItem;
    private long graphLongItem;

    private String protocol;
    private Integer nProtocol;
    private String destIp;
    private String srcIp;
    private Long sourceIp;
    private Long destinationIp;

    public String getSortSelect() {
        return sortSelect;
    }

    public void setSortSelect(String sortSelect) {
        this.sortSelect = sortSelect;
    }

    public String getSimpleTimeSelect() {
        return simpleTimeSelect;
    }

    public void setSimpleTimeSelect(String simpleTimeSelect) {
        this.simpleTimeSelect = simpleTimeSelect;
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

    public int getMaskInput() {
        return maskInput;
    }

    public void setMaskInput(int maskInput) {
        this.maskInput = maskInput;
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

    public BigInteger getDestPortInput() {
        return destPortInput;
    }

    public void setDestPortInput(BigInteger destPortInput) {
        this.destPortInput = destPortInput;
    }

    public long getsLcodeCount() {
        return sLcodeCount;
    }

    public void setsLcodeCount(long sLcodeCount) {
        this.sLcodeCount = sLcodeCount;
    }

    public long getsPortCount() {
        return sPortCount;
    }

    public void setsPortCount(long sPortCount) {
        this.sPortCount = sPortCount;
    }

    public long getnSum() {
        return nSum;
    }

    public void setnSum(long nSum) {
        this.nSum = nSum;
    }

    public long getBps() {
        return bps;
    }

    public void setBps(long bps) {
        this.bps = bps;
    }

    public int getEndRowSize() {
        return endRowSize;
    }

    public void setEndRowSize(int endRowSize) {
        this.endRowSize = endRowSize;
    }

    public String getListViewInput() {
        return listViewInput;
    }

    public void setListViewInput(String listViewInput) {
        this.listViewInput = listViewInput;
    }

    public String getGraphItem() {
        return graphItem;
    }

    public void setGraphItem(String graphItem) {
        this.graphItem = graphItem;
    }

    public long getGraphLongItem() {
        return graphLongItem;
    }

    public void setGraphLongItem(long graphLongItem) {
        this.graphLongItem = graphLongItem;
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
}
