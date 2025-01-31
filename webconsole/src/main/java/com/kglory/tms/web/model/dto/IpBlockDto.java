package com.kglory.tms.web.model.dto;

public class IpBlockDto {
    
    private Integer index;
    private String dwToIp;
    private String dwFromIp;
    private Long longToIp;
    private Long longFromIp;
    private Integer lId;
    private String strFromIpv6;
    private String strToIpv6;
    private Integer bType;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

    public Long getLongToIp() {
        return longToIp;
    }

    public void setLongToIp(Long longToIp) {
        this.longToIp = longToIp;
    }

    public Long getLongFromIp() {
        return longFromIp;
    }

    public void setLongFromIp(Long longFromIp) {
        this.longFromIp = longFromIp;
    }

    public Integer getlId() {
        return lId;
    }

    public void setlId(Integer lId) {
        this.lId = lId;
    }

    public String getStrFromIpv6() {
        return strFromIpv6;
    }

    public void setStrFromIpv6(String strFromIpv6) {
        this.strFromIpv6 = strFromIpv6;
    }

    public String getStrToIpv6() {
        return strToIpv6;
    }

    public void setStrToIpv6(String strToIpv6) {
        this.strToIpv6 = strToIpv6;
    }

    public Integer getbType() {
        return bType;
    }

    public void setbType(Integer bType) {
        this.bType = bType;
    }
}
