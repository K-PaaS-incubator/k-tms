package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class OriginalLogVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = 5048314996892878188L;

    private long rNum = 0;
    private long lIndex;
    private long lCode = 0;
    private long bSeverity = 0;
    private String strTitle = "";

    private String deDestinationIp = "";
    private String dwSourceIp = "";
    private long nSourcePort = 0;
    private long nDestinationPort = 0;
    private long nProtocol;
    private long dwpacketcounter;
    private long dwpktsize;
    private long dweventnum;
    private long ucCreateLogType;

    private long cntInfo = 0;
    private long cntLow = 0;
    private long cntMed = 0;
    private long cntHigh = 0;

    private String startDate = "";
    private String endDate = "";

    private long totalRowSize = 0;
    private long total_sum;

    private String sData;
    private long lMode;
    private BigInteger lvsensorIndex;
    private BigInteger lsensorIndex;
    private String strSourceMac;
    private String strDestinationMac;
    private String bType;
    private long dwDstPortCounter;
    private long dwSrcIpCounter;
    private long dwDstIpCounter;
    private Integer nTtl;
    private Integer wInbound;
    private String srcNetworkName;
    private String dstNetworkName;
    private String sensorName;
    private String vsensorName;

    private BigInteger dwMaliciousSrvFrame;
    private BigInteger dwMaliciousSrvByte;
    private String strData;

    private String srcService;
    private String dstService;
    private String rawdata;

    private String strSrcNationIso;
    private String strDestNationIso;

    private String strSrcCategory;
    private Integer nSrcScore;
    private String strDestCategory;
    private Integer nDestScore;

    public long getrNum() {
        return rNum;
    }

    public void setrNum(long rNum) {
        this.rNum = rNum;
    }

    public long getlIndex() {
        return lIndex;
    }

    public void setlIndex(long lIndex) {
        this.lIndex = lIndex;
    }

    public long getlCode() {
        return lCode;
    }

    public void setlCode(long lCode) {
        this.lCode = lCode;
    }

    public long getbSeverity() {
        return bSeverity;
    }

    public void setbSeverity(long bSeverity) {
        this.bSeverity = bSeverity;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getDeDestinationIp() {
        return deDestinationIp;
    }

    public void setDeDestinationIp(String deDestinationIp) {
        this.deDestinationIp = deDestinationIp;
    }

    public String getDwSourceIp() {
        return dwSourceIp;
    }

    public void setDwSourceIp(String dwSourceIp) {
        this.dwSourceIp = dwSourceIp;
    }

    public long getnSourcePort() {
        return nSourcePort;
    }

    public void setnSourcePort(long nSourcePort) {
        this.nSourcePort = nSourcePort;
    }

    public long getnDestinationPort() {
        return nDestinationPort;
    }

    public void setnDestinationPort(long nDestinationPort) {
        this.nDestinationPort = nDestinationPort;
    }

    public long getnProtocol() {
        return nProtocol;
    }

    public void setnProtocol(long nProtocol) {
        this.nProtocol = nProtocol;
    }

    public long getDwpacketcounter() {
        return dwpacketcounter;
    }

    public void setDwpacketcounter(long dwpacketcounter) {
        this.dwpacketcounter = dwpacketcounter;
    }

    public long getDwpktsize() {
        return dwpktsize;
    }

    public void setDwpktsize(long dwpktsize) {
        this.dwpktsize = dwpktsize;
    }

    public long getDweventnum() {
        return dweventnum;
    }

    public void setDweventnum(long dweventnum) {
        this.dweventnum = dweventnum;
    }

    public long getUcCreateLogType() {
        return ucCreateLogType;
    }

    public void setUcCreateLogType(long ucCreateLogType) {
        this.ucCreateLogType = ucCreateLogType;
    }

    public long getCntInfo() {
        return cntInfo;
    }

    public void setCntInfo(long cntInfo) {
        this.cntInfo = cntInfo;
    }

    public long getCntLow() {
        return cntLow;
    }

    public void setCntLow(long cntLow) {
        this.cntLow = cntLow;
    }

    public long getCntMed() {
        return cntMed;
    }

    public void setCntMed(long cntMed) {
        this.cntMed = cntMed;
    }

    public long getCntHigh() {
        return cntHigh;
    }

    public void setCntHigh(long cntHigh) {
        this.cntHigh = cntHigh;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getTotalRowSize() {
        return totalRowSize;
    }

    public void setTotalRowSize(long totalRowSize) {
        this.totalRowSize = totalRowSize;
    }

    public long getTotal_sum() {
        return total_sum;
    }

    public void setTotal_sum(long total_sum) {
        this.total_sum = total_sum;
    }

    public String getsData() {
        return sData;
    }

    public void setsData(String sData) {
        this.sData = sData;
    }

    public long getlMode() {
        return lMode;
    }

    public void setlMode(long lMode) {
        this.lMode = lMode;
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

    public String getStrSourceMac() {
        return strSourceMac;
    }

    public void setStrSourceMac(String strSourceMac) {
        this.strSourceMac = strSourceMac;
    }

    public String getStrDestinationMac() {
        return strDestinationMac;
    }

    public void setStrDestinationMac(String strDestinationMac) {
        this.strDestinationMac = strDestinationMac;
    }

    public String getbType() {
        return bType;
    }

    public void setbType(String bType) {
        this.bType = bType;
    }

    public long getDwDstPortCounter() {
        return dwDstPortCounter;
    }

    public void setDwDstPortCounter(long dwDstPortCounter) {
        this.dwDstPortCounter = dwDstPortCounter;
    }

    public long getDwSrcIpCounter() {
        return dwSrcIpCounter;
    }

    public void setDwSrcIpCounter(long dwSrcIpCounter) {
        this.dwSrcIpCounter = dwSrcIpCounter;
    }

    public long getDwDstIpCounter() {
        return dwDstIpCounter;
    }

    public void setDwDstIpCounter(long dwDstIpCounter) {
        this.dwDstIpCounter = dwDstIpCounter;
    }

    public Integer getnTtl() {
        return nTtl;
    }

    public void setnTtl(Integer nTtl) {
        this.nTtl = nTtl;
    }

    public Integer getwInbound() {
        return wInbound;
    }

    public void setwInbound(Integer wInbound) {
        this.wInbound = wInbound;
    }

    public String getSrcNetworkName() {
        return srcNetworkName;
    }

    public void setSrcNetworkName(String srcNetworkName) {
        this.srcNetworkName = srcNetworkName;
    }

    public String getDstNetworkName() {
        return dstNetworkName;
    }

    public void setDstNetworkName(String dstNetworkName) {
        this.dstNetworkName = dstNetworkName;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getVsensorName() {
        return vsensorName;
    }

    public void setVsensorName(String vsensorName) {
        this.vsensorName = vsensorName;
    }

    public BigInteger getDwMaliciousSrvFrame() {
        return dwMaliciousSrvFrame;
    }

    public void setDwMaliciousSrvFrame(BigInteger dwMaliciousSrvFrame) {
        this.dwMaliciousSrvFrame = dwMaliciousSrvFrame;
    }

    public BigInteger getDwMaliciousSrvByte() {
        return dwMaliciousSrvByte;
    }

    public void setDwMaliciousSrvByte(BigInteger dwMaliciousSrvByte) {
        this.dwMaliciousSrvByte = dwMaliciousSrvByte;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getSrcService() {
        return srcService;
    }

    public void setSrcService(String srcService) {
        this.srcService = srcService;
    }

    public String getDstService() {
        return dstService;
    }

    public void setDstService(String dstService) {
        this.dstService = dstService;
    }

    public String getRawdata() {
        return rawdata;
    }

    public void setRawdata(String rawdata) {
        this.rawdata = rawdata;
    }

    public String getStrSrcNationIso() {
        return strSrcNationIso;
    }

    public void setStrSrcNationIso(String strSrcNationIso) {
        this.strSrcNationIso = strSrcNationIso;
    }

    public String getStrDestNationIso() {
        return strDestNationIso;
    }

    public void setStrDestNationIso(String strDestNationIso) {
        this.strDestNationIso = strDestNationIso;
    }

    public String getStrSrcCategory() {
        return strSrcCategory;
    }

    public void setStrSrcCategory(String strSrcCategory) {
        this.strSrcCategory = strSrcCategory;
    }

    public Integer getnSrcScore() {
        return nSrcScore;
    }

    public void setnSrcScore(Integer nSrcScore) {
        this.nSrcScore = nSrcScore;
    }

    public String getStrDestCategory() {
        return strDestCategory;
    }

    public void setStrDestCategory(String strDestCategory) {
        this.strDestCategory = strDestCategory;
    }

    public Integer getnDestScore() {
        return nDestScore;
    }

    public void setnDestScore(Integer nDestScore) {
        this.nDestScore = nDestScore;
    }

    @Override
    public String toString() {
        return "OriginalLogVO [rNum=" + rNum + ", lIndex=" + lIndex
                + ", lCode=" + lCode + ", bSeverity=" + bSeverity
                + ", strTitle=" + strTitle + ", deDestinationIp="
                + deDestinationIp + ", dwSourceIp=" + dwSourceIp
                + ", nSourcePort=" + nSourcePort + ", nDestinationPort="
                + nDestinationPort + ", nProtocol=" + nProtocol
                + ", dwpacketcounter=" + dwpacketcounter + ", dwpktsize="
                + dwpktsize + ", dweventnum=" + dweventnum
                + ", ucCreateLogType=" + ucCreateLogType + ", cntInfo="
                + cntInfo + ", cntLow=" + cntLow + ", cntMed=" + cntMed
                + ", cntHigh=" + cntHigh + ", startDate=" + startDate
                + ", endDate=" + endDate + ", totalRowSize=" + totalRowSize
                + ", total_sum=" + total_sum + ", sData=" + sData + ", lMode="
                + lMode + ", lvsensorIndex=" + lvsensorIndex
                + ", lsensorIndex=" + lsensorIndex + ", strSourceMac="
                + strSourceMac + ", strDestinationMac=" + strDestinationMac
                + ", bType=" + bType + ", dwDstPortCounter=" + dwDstPortCounter
                + ", dwSrcIpCounter=" + dwSrcIpCounter + ", dwDstIpCounter="
                + dwDstIpCounter + ", nTtl=" + nTtl + ", wInbound=" + wInbound
                + ", srcNetworkName=" + srcNetworkName + ", dstNetworkName="
                + dstNetworkName + ", sensorName=" + sensorName
                + ", vsensorName=" + vsensorName + ", dwMaliciousSrvFrame="
                + dwMaliciousSrvFrame + ", dwMaliciousSrvByte="
                + dwMaliciousSrvByte + ", strData=" + strData + ", srcService="
                + srcService + ", dstService=" + dstService + ", rawdata="
                + rawdata + "]";
    }
}
