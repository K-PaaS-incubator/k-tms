package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;

public class DetectionExceptionDto extends CommonBean {

    private Integer lIndex;
    private long lvsensorIndex;
    private long lVioCode;
    private long nProtocol;
    private String strSrcIpFrom = null;
    private String strSrcIpTo = null;
    private long nSport;
    private long nDport;
    private String strDstIpFrom = null;
    private String strDstIpTo = null;
    private long ldstNetworkIndex;
    private long lsrcNetworkIndex;
    private Long nDetect;
    private Long nDetectValue;
    private long nClassType;
    private Integer deletelIndex;
    private String strDescriptionValue;

    private Integer nchkVioCode;
    private Integer nchkSrcNetwork;
    private Integer nchkDstNetwork;
    private Integer nchkSrcIp;
    private Integer nchkDstIp;
    private Integer nchkProtocol;
    private Integer nchkDport;
    private Integer nchkSport;
    private long lsensorIndex;

    private String lsrcNetworkIndexName;
    private String ldstNetworkIndexName;

    public Integer getlIndex() {
        return lIndex;
    }

    public void setlIndex(Integer lIndex) {
        this.lIndex = lIndex;
    }

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public long getlVioCode() {
        return lVioCode;
    }

    public void setlVioCode(long lVioCode) {
        this.lVioCode = lVioCode;
    }

    public long getnProtocol() {
        return nProtocol;
    }

    public void setnProtocol(long nProtocol) {
        this.nProtocol = nProtocol;
    }

    public String getStrSrcIpFrom() {
        return strSrcIpFrom;
    }

    public void setStrSrcIpFrom(String strSrcIpFrom) {
        this.strSrcIpFrom = strSrcIpFrom;
    }

    public String getStrSrcIpTo() {
        return strSrcIpTo;
    }

    public void setStrSrcIpTo(String strSrcIpTo) {
        this.strSrcIpTo = strSrcIpTo;
    }

    public long getnSport() {
        return nSport;
    }

    public void setnSport(long nSport) {
        this.nSport = nSport;
    }

    public long getnDport() {
        return nDport;
    }

    public void setnDport(long nDport) {
        this.nDport = nDport;
    }

    public String getStrDstIpFrom() {
        return strDstIpFrom;
    }

    public void setStrDstIpFrom(String strDstIpFrom) {
        this.strDstIpFrom = strDstIpFrom;
    }

    public String getStrDstIpTo() {
        return strDstIpTo;
    }

    public void setStrDstIpTo(String strDstIpTo) {
        this.strDstIpTo = strDstIpTo;
    }

    public long getLdstNetworkIndex() {
        return ldstNetworkIndex;
    }

    public void setLdstNetworkIndex(long ldstNetworkIndex) {
        this.ldstNetworkIndex = ldstNetworkIndex;
    }

    public long getLsrcNetworkIndex() {
        return lsrcNetworkIndex;
    }

    public void setLsrcNetworkIndex(long lsrcNetworkIndex) {
        this.lsrcNetworkIndex = lsrcNetworkIndex;
    }

    public Long getnDetect() {
        return nDetect;
    }

    public void setnDetect(Long nDetect) {
        this.nDetect = nDetect;
    }

    public Long getnDetectValue() {
        return nDetectValue;
    }

    public void setnDetectValue(Long nDetectValue) {
        this.nDetectValue = nDetectValue;
    }

    public long getnClassType() {
        return nClassType;
    }

    public void setnClassType(long nClassType) {
        this.nClassType = nClassType;
    }

    public Integer getDeletelIndex() {
        return deletelIndex;
    }

    public void setDeletelIndex(Integer deletelIndex) {
        this.deletelIndex = deletelIndex;
    }

    public String getStrDescriptionValue() {
        return strDescriptionValue;
    }

    public void setStrDescriptionValue(String strDescriptionValue) {
        this.strDescriptionValue = strDescriptionValue;
    }

    public Integer getNchkVioCode() {
        return nchkVioCode;
    }

    public void setNchkVioCode(Integer nchkVioCode) {
        this.nchkVioCode = nchkVioCode;
    }

    public Integer getNchkSrcNetwork() {
        return nchkSrcNetwork;
    }

    public void setNchkSrcNetwork(Integer nchkSrcNetwork) {
        this.nchkSrcNetwork = nchkSrcNetwork;
    }

    public Integer getNchkDstNetwork() {
        return nchkDstNetwork;
    }

    public void setNchkDstNetwork(Integer nchkDstNetwork) {
        this.nchkDstNetwork = nchkDstNetwork;
    }

    public Integer getNchkSrcIp() {
        return nchkSrcIp;
    }

    public void setNchkSrcIp(Integer nchkSrcIp) {
        this.nchkSrcIp = nchkSrcIp;
    }

    public Integer getNchkDstIp() {
        return nchkDstIp;
    }

    public void setNchkDstIp(Integer nchkDstIp) {
        this.nchkDstIp = nchkDstIp;
    }

    public Integer getNchkProtocol() {
        return nchkProtocol;
    }

    public void setNchkProtocol(Integer nchkProtocol) {
        this.nchkProtocol = nchkProtocol;
    }

    public Integer getNchkDport() {
        return nchkDport;
    }

    public void setNchkDport(Integer nchkDport) {
        this.nchkDport = nchkDport;
    }

    public Integer getNchkSport() {
        return nchkSport;
    }

    public void setNchkSport(Integer nchkSport) {
        this.nchkSport = nchkSport;
    }

    public long getLsensorIndex() {
        return lsensorIndex;
    }

    public void setLsensorIndex(long lsensorIndex) {
        this.lsensorIndex = lsensorIndex;
    }

    public String getLsrcNetworkIndexName() {
        return lsrcNetworkIndexName;
    }

    public void setLsrcNetworkIndexName(String lsrcNetworkIndexName) {
        this.lsrcNetworkIndexName = lsrcNetworkIndexName;
    }

    public String getLdstNetworkIndexName() {
        return ldstNetworkIndexName;
    }

    public void setLdstNetworkIndexName(String ldstNetworkIndexName) {
        this.ldstNetworkIndexName = ldstNetworkIndexName;
    }
}
