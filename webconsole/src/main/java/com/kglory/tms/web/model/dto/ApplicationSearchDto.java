package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.List;

public class ApplicationSearchDto extends SearchDto {

    private Long 		lIndex;
    private BigInteger 	lnetgroupIndex;
    private BigInteger 	lnetworkIndex;
    private BigInteger 	lvsensorIndex;
    private BigInteger 	lsensorIndex;

    private String 		startDateInput;
    private String 		endDateInput;
    private List<String>tableNames;
    private String 		toIpInput;
    private String 		fromIpInput;
    private Long 		toIp;
    private Long 		fromIp;
    private Integer 	maskInput;
    private BigInteger 	destPortInput;
    private BigInteger 	attackPortInput;
    private String 		simpleTimeSelect;
    private Integer 	startRowSize;
    private Integer 	endRowSize;
    private String 		protocol;
    private Integer 	nProtocol;
    private String 		destIp;
    private String 		srcIp;
    private Long 		sourceIp;
    private Long 		destinationIp;
    private String 		bType;
    private long 		topN;
    private String 		graphItem;
    private Integer 	graphPlusItem;
    private long 		timeDifference; 	//endDate - startDate 분으로 환산 합계의 평균을 구할때 사용
    private Integer 	nSourcePort;
    private Integer 	nDestinationPort;
    private Long 		deDestinationIp;

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
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

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
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

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public long getTopN() {
        return topN;
    }

    public void setTopN(long topN) {
        this.topN = topN;
    }

    public String getGraphItem() {
        return graphItem;
    }

    public void setGraphItem(String graphItem) {
        this.graphItem = graphItem;
    }

    public Integer getGraphPlusItem() {
        return graphPlusItem;
    }

    public void setGraphPlusItem(Integer graphPlusItem) {
        this.graphPlusItem = graphPlusItem;
    }

    public long getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(long timeDifference) {
        this.timeDifference = timeDifference;
    }

	public Integer getnSourcePort() {
		return nSourcePort;
	}

	public void setnSourcePort(Integer nSourcePort) {
		this.nSourcePort = nSourcePort;
	}

	public Integer getnDestinationPort() {
		return nDestinationPort;
	}

	public void setnDestinationPort(Integer nDestinationPort) {
		this.nDestinationPort = nDestinationPort;
	}

	public Long getDeDestinationIp() {
		return deDestinationIp;
	}

	public void setDeDestinationIp(Long deDestinationIp) {
		this.deDestinationIp = deDestinationIp;
	}
}
