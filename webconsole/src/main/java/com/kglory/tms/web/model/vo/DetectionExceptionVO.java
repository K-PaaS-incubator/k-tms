package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class DetectionExceptionVO extends CommonBean implements Serializable {
    private static final long serialVersionUID = 975932532907239490L;

    private long lIndex;
    private long lVioCode;
    private long nClassType;
    private long lvsensorIndex;
    private String strSrcIpFrom;
    private String strSrcIpTo;
    private String nSport;
    private long lsrcNetworkIndex;
    private String lsrcNetworkIndexName;
    private String strDstIpFrom;
    private String strDstIpTo;
    private String nDport;
    private long ldstNetworkIndex;
    private String ldstNetworkIndexName;
    private long nProtocol;
    private Long nDetect;
    private String strDescriptionValue;
    private long sSeverity;
    private long sClassType;
    private long lCode;
    private Integer chkVioCode;
    private Integer chkSrcNetwork;
    private Integer chkDstNetwork;
    private Integer chkSrcIP;
    private Integer chkDstIP;
    private Integer chkProtocol;
    private Integer chkSPort;
    private Integer chkDPort;

    public long getlIndex() {
        return lIndex;
    }

    public void setlIndex(long lIndex) {
        this.lIndex = lIndex;
    }

    public long getlVioCode() {
        return lVioCode;
    }

    public void setlVioCode(long lVioCode) {
        this.lVioCode = lVioCode;
    }

    public long getnClassType() {
        return nClassType;
    }

    public void setnClassType(long nClassType) {
        this.nClassType = nClassType;
    }

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
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

    public String getnSport() {
        return nSport;
    }

    public void setnSport(String nSport) {
        this.nSport = nSport;
    }

    public long getLsrcNetworkIndex() {
        return lsrcNetworkIndex;
    }

    public void setLsrcNetworkIndex(long lsrcNetworkIndex) {
        this.lsrcNetworkIndex = lsrcNetworkIndex;
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

    public String getnDport() {
        return nDport;
    }

    public void setnDport(String nDport) {
        this.nDport = nDport;
    }

    public long getLdstNetworkIndex() {
        return ldstNetworkIndex;
    }

    public void setLdstNetworkIndex(long ldstNetworkIndex) {
        this.ldstNetworkIndex = ldstNetworkIndex;
    }

    public long getnProtocol() {
        return nProtocol;
    }

    public void setnProtocol(Long nProtocol) {
        this.nProtocol = nProtocol;
    }

    public Long getnDetect() {
        return nDetect;
    }

    public void setnDetect(long nDetect) {
        this.nDetect = nDetect;
    }

    public String getStrDescriptionValue() {
        return strDescriptionValue;
    }

    public void setStrDescriptionValue(String strDescriptionValue) {
        this.strDescriptionValue = strDescriptionValue;
    }

    public long getsSeverity() {
        return sSeverity;
    }

    public void setsSeverity(long sSeverity) {
        this.sSeverity = sSeverity;
    }

    public long getsClassType() {
        return sClassType;
    }

    public void setsClassType(long sClassType) {
        this.sClassType = sClassType;
    }

    public long getlCode() {
        return lCode;
    }

    public void setlCode(long lCode) {
        this.lCode = lCode;
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

    public Integer getChkVioCode() {
        return chkVioCode;
    }

    public void setChkVioCode(Integer chkVioCode) {
        this.chkVioCode = chkVioCode;
    }

    public Integer getChkSrcNetwork() {
        return chkSrcNetwork;
    }

    public void setChkSrcNetwork(Integer chkSrcNetwork) {
        this.chkSrcNetwork = chkSrcNetwork;
    }

    public Integer getChkDstNetwork() {
        return chkDstNetwork;
    }

    public void setChkDstNetwork(Integer chkDstNetwork) {
        this.chkDstNetwork = chkDstNetwork;
    }

    public Integer getChkSrcIP() {
        return chkSrcIP;
    }

    public void setChkSrcIP(Integer chkSrcIP) {
        this.chkSrcIP = chkSrcIP;
    }

    public Integer getChkDstIP() {
        return chkDstIP;
    }

    public void setChkDstIP(Integer chkDstIP) {
        this.chkDstIP = chkDstIP;
    }

    public Integer getChkProtocol() {
        return chkProtocol;
    }

    public void setChkProtocol(Integer chkProtocol) {
        this.chkProtocol = chkProtocol;
    }

    public Integer getChkSPort() {
        return chkSPort;
    }

    public void setChkSPort(Integer chkSPort) {
        this.chkSPort = chkSPort;
    }

    public Integer getChkDPort() {
        return chkDPort;
    }

    public void setChkDPort(Integer chkDPort) {
        this.chkDPort = chkDPort;
    }
}
