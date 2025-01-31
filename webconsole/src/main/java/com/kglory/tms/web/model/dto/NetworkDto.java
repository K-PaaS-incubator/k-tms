package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class NetworkDto extends CommonBean {

    private Integer index;
    private String strName;
    private long lnetworkIndex;
    private String strDescription;
    private Integer lValue1;
    private long lvsensorIndex;
    private long dbLbandWidth;
    private long lparentGroupIndex;
    private Integer sType;
    private Integer lId;
    private List<IpBlockDto> ipBlockList;
    private List<IpBlockDto> ipBlockListDel;
    private String type; // 커맨드 실행시 커맨드 유형이 어떤 것인지 판별하기 위함 

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public long getLnetworkIndex() {
        return lnetworkIndex;
    }

    public void setLnetworkIndex(long lnetworkIndex) {
        this.lnetworkIndex = lnetworkIndex;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public Integer getlValue1() {
        return lValue1;
    }

    public void setlValue1(Integer lValue1) {
        this.lValue1 = lValue1;
    }

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public long getDbLbandWidth() {
        return dbLbandWidth;
    }

    public void setDbLbandWidth(long dbLbandWidth) {
        this.dbLbandWidth = dbLbandWidth;
    }

    public Integer getsType() {
        return sType;
    }

    public void setsType(Integer sType) {
        this.sType = sType;
    }

    public Integer getlId() {
        return lId;
    }

    public void setlId(Integer lId) {
        this.lId = lId;
    }

    public List<IpBlockDto> getIpBlockList() {
        return ipBlockList;
    }

    public void setIpBlockList(List<IpBlockDto> ipBlockList) {
        this.ipBlockList = ipBlockList;
    }

    public List<IpBlockDto> getIpBlockListDel() {
        return ipBlockListDel;
    }

    public void setIpBlockListDel(List<IpBlockDto> ipBlockListDel) {
        this.ipBlockListDel = ipBlockListDel;
    }

    public long getLparentGroupIndex() {
        return lparentGroupIndex;
    }

    public void setLparentGroupIndex(long lparentGroupIndex) {
        this.lparentGroupIndex = lparentGroupIndex;
    }

    @Override
    public String toString() {
        return "NetworkDto [strName=" + strName + ", lnetworkIndex="
                + lnetworkIndex + ", strDescription=" + strDescription
                + ", lValue1=" + lValue1 + ", lvsensorIndex=" + lvsensorIndex
                + ", dbLbandWidth=" + dbLbandWidth + ", lparentGroupIndex="
                + lparentGroupIndex + ", sType=" + sType + ", lId=" + lId
                + ", ipBlockList=" + ipBlockList + ", type=" + type + "]";
    }
}
