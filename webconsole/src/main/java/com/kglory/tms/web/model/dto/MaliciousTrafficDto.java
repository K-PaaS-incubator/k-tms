package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.util.MessageUtil;

public class MaliciousTrafficDto extends SearchDto {

    private Integer startRowSize;
    private Integer endRowSize;
    private String sortSelect;
    private Long graphItem;
    private Integer ucType;
    private Integer nProtocol;
    private String allTrafficName = MessageUtil.getMessage("traffic.all");
    private String maliciousTrafficName = MessageUtil.getMessage("traffic.malicious");

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

    public Long getGraphItem() {
        return graphItem;
    }

    public void setGraphItem(Long graphItem) {
        this.graphItem = graphItem;
    }

    public Integer getUcType() {
        return ucType;
    }

    public void setUcType(Integer ucType) {
        this.ucType = ucType;
    }

    public Integer getnProtocol() {
        return nProtocol;
    }

    public void setnProtocol(Integer nProtocol) {
        this.nProtocol = nProtocol;
    }

    public String getAllTrafficName() {
        return allTrafficName;
    }

    public void setAllTrafficName(String allTrafficName) {
        this.allTrafficName = allTrafficName;
    }

    public String getMaliciousTrafficName() {
        return maliciousTrafficName;
    }

    public void setMaliciousTrafficName(String maliciousTrafficName) {
        this.maliciousTrafficName = maliciousTrafficName;
    }

    @Override
    public String toString() {
        return "MaliciousTrafficDto [startRowSize=" + startRowSize
                + ", endRowSize=" + endRowSize + ", sortSelect=" + sortSelect
                + ", graphItem=" + graphItem + ", ucType=" + ucType
                + ", nProtocol=" + nProtocol + "]";
    }
}
