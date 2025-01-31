package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import java.util.ArrayList;
import java.util.List;

public class NetworkVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = 7242212627742960509L;

    private Integer index;
    private long lnetworkIndex;
    private String strName;
    private String strDescription;
    private long lparentgroupIndex;
    private long lvsensorIndex;
    private String lvsensorName;
    private Integer sType;
    private Integer lValue1;
    private Integer lValue2;
    private Integer sZip1;
    private long dbLbandWidth;

    private String dwToIp;
    private String dwFromIp;
    private Integer lId;
    private List<NetworkVO> ipBolckList = new ArrayList<>();

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public long getLnetworkIndex() {
        return lnetworkIndex;
    }

    public void setLnetworkIndex(long lnetworkIndex) {
        this.lnetworkIndex = lnetworkIndex;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public long getLparentgroupIndex() {
        return lparentgroupIndex;
    }

    public void setLparentgroupIndex(long lparentgroupIndex) {
        this.lparentgroupIndex = lparentgroupIndex;
    }

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public String getLvsensorName() {
        return lvsensorName;
    }

    public void setLvsensorName(String lvsensorName) {
        this.lvsensorName = lvsensorName;
    }

    public Integer getsType() {
        return sType;
    }

    public void setsType(Integer sType) {
        this.sType = sType;
    }

    public Integer getlValue1() {
        return lValue1;
    }

    public void setlValue1(Integer lValue1) {
        this.lValue1 = lValue1;
    }

    public Integer getlValue2() {
        return lValue2;
    }

    public void setlValue2(Integer lValue2) {
        this.lValue2 = lValue2;
    }

    public Integer getsZip1() {
        return sZip1;
    }

    public void setsZip1(Integer sZip1) {
        this.sZip1 = sZip1;
    }

    public long getDbLbandWidth() {
        return dbLbandWidth;
    }

    public void setDbLbandWidth(long dbLbandWidth) {
        this.dbLbandWidth = dbLbandWidth;
    }

    public String getDwToIp() {
        return dwToIp;
    }

    public void setDwToIp(String dwToIp) {
        this.dwToIp = dwToIp;
    }

    public String getDwFromIp() {
        return dwFromIp;
    }

    public void setDwFromIp(String dwFromIp) {
        this.dwFromIp = dwFromIp;
    }

    public Integer getlId() {
        return lId;
    }

    public void setlId(Integer lId) {
        this.lId = lId;
    }

    public List<NetworkVO> getIpBolckList() {
        return ipBolckList;
    }

    public void setIpBolckList(List<NetworkVO> ipBolckList) {
        this.ipBolckList = ipBolckList;
    }
}
