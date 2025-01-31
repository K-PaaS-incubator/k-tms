package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leekyunghee
 *
 */
public class SensorVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = 4050784764927327079L;

    private long lvsensorIndex = 0;
    private long lIndex = 0;
    private long sType = 0;
    private String strName = "";
    private String strDescription = "";
    private String lPrivateIp;
    private String lPublicIp;
    private long sPort;
    private long sMode;

    private long nStartIntegrity;
    private long nAutoIntegrity;
    private long nAutoIntegrityMin;

    private long dblTotalPacketBytes;
    private long dblPps1000;

    private long dblSession;
    private long dblLps;

    private long lProcessNum;

    private long dblCpuTotal;
    private long dblCurCpuUsage;

    private long dblMemTotal;
    private long dblMemUsed;

    private long dblHddTotal;
    private long dblHddUsed;

    private String tmCur;

    private long lsensorIndex;
    private String strFromIp;
    private String strToIp;
    private Integer bType;
    private long nHyperScanHitCount;
    private Integer sUseBlackList;
    private Long nDirectIntegrity;
    
    private List<EthoNetVO> ethoNetList = new ArrayList<>();

    public long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
    }

    public long getlIndex() {
        return lIndex;
    }

    public void setlIndex(long lIndex) {
        this.lIndex = lIndex;
    }

    public long getsType() {
        return sType;
    }

    public void setsType(long sType) {
        this.sType = sType;
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

    public String getlPrivateIp() {
        return lPrivateIp;
    }

    public void setlPrivateIp(String lPrivateIp) {
        this.lPrivateIp = lPrivateIp;
    }

    public String getlPublicIp() {
        return lPublicIp;
    }

    public void setlPublicIp(String lPublicIp) {
        this.lPublicIp = lPublicIp;
    }

    public long getsPort() {
        return sPort;
    }

    public void setsPort(long sPort) {
        this.sPort = sPort;
    }

    public long getsMode() {
        return sMode;
    }

    public void setsMode(long sMode) {
        this.sMode = sMode;
    }

    public long getnStartIntegrity() {
        return nStartIntegrity;
    }

    public void setnStartIntegrity(long nStartIntegrity) {
        this.nStartIntegrity = nStartIntegrity;
    }

    public long getnAutoIntegrity() {
        return nAutoIntegrity;
    }

    public void setnAutoIntegrity(long nAutoIntegrity) {
        this.nAutoIntegrity = nAutoIntegrity;
    }

    public long getnAutoIntegrityMin() {
        return nAutoIntegrityMin;
    }

    public void setnAutoIntegrityMin(long nAutoIntegrityMin) {
        this.nAutoIntegrityMin = nAutoIntegrityMin;
    }

    public long getDblTotalPacketBytes() {
        return dblTotalPacketBytes;
    }

    public void setDblTotalPacketBytes(long dblTotalPacketBytes) {
        this.dblTotalPacketBytes = dblTotalPacketBytes;
    }

    public long getDblPps1000() {
        return dblPps1000;
    }

    public void setDblPps1000(long dblPps1000) {
        this.dblPps1000 = dblPps1000;
    }

    public long getDblSession() {
        return dblSession;
    }

    public void setDblSession(long dblSession) {
        this.dblSession = dblSession;
    }

    public long getDblLps() {
        return dblLps;
    }

    public void setDblLps(long dblLps) {
        this.dblLps = dblLps;
    }

    public long getDblMemTotal() {
        return dblMemTotal;
    }

    public void setDblMemTotal(long dblMemTotal) {
        this.dblMemTotal = dblMemTotal;
    }

    public long getDblMemUsed() {
        return dblMemUsed;
    }

    public void setDblMemUsed(long dblMemUsed) {
        this.dblMemUsed = dblMemUsed;
    }

    public long getlProcessNum() {
        return lProcessNum;
    }

    public void setlProcessNum(long lProcessNum) {
        this.lProcessNum = lProcessNum;
    }

    public long getDblHddTotal() {
        return dblHddTotal;
    }

    public void setDblHddTotal(long dblHddTotal) {
        this.dblHddTotal = dblHddTotal;
    }

    public long getDblHddUsed() {
        return dblHddUsed;
    }

    public void setDblHddUsed(long dblHddUsed) {
        this.dblHddUsed = dblHddUsed;
    }

    public String getTmCur() {
        return tmCur;
    }

    public void setTmCur(String tmCur) {
        this.tmCur = tmCur;
    }

    public long getDblCpuTotal() {
        return dblCpuTotal;
    }

    public void setDblCpuTotal(long dblCpuTotal) {
        this.dblCpuTotal = dblCpuTotal;
    }

    public long getDblCurCpuUsage() {
        return dblCurCpuUsage;
    }

    public void setDblCurCpuUsage(long dblCurCpuUsage) {
        this.dblCurCpuUsage = dblCurCpuUsage;
    }

    public long getLsensorIndex() {
        return lsensorIndex;
    }

    public void setLsensorIndex(long lsensorIndex) {
        this.lsensorIndex = lsensorIndex;
    }

    public String getStrFromIp() {
        return strFromIp;
    }

    public void setStrFromIp(String strFromIp) {
        this.strFromIp = strFromIp;
    }

    public String getStrToIp() {
        return strToIp;
    }

    public void setStrToIp(String strToIp) {
        this.strToIp = strToIp;
    }

    public Integer getbType() {
        return bType;
    }

    public void setbType(Integer bType) {
        this.bType = bType;
    }

    public long getnHyperScanHitCount() {
        return nHyperScanHitCount;
    }

    public void setnHyperScanHitCount(long nHyperScanHitCount) {
        this.nHyperScanHitCount = nHyperScanHitCount;
    }

    public Integer getsUseBlackList() {
        return sUseBlackList;
    }

    public void setsUseBlackList(Integer sUseBlackList) {
        this.sUseBlackList = sUseBlackList;
    }

    public Long getnDirectIntegrity() {
        return nDirectIntegrity;
    }

    public void setnDirectIntegrity(Long nDirectIntegrity) {
        this.nDirectIntegrity = nDirectIntegrity;
    }

    public List<EthoNetVO> getEthoNetList() {
        return ethoNetList;
    }

    public void setEthoNetList(List<EthoNetVO> ethoNetList) {
        this.ethoNetList = ethoNetList;
    }

    @Override
    public String toString() {
        return "SensorVO [lvsensorIndex=" + lvsensorIndex + ", lIndex="
                + lIndex + ", sType=" + sType + ", strName=" + strName
                + ", strDescription=" + strDescription + ", lPrivateIp="
                + lPrivateIp + ", lPublicIp=" + lPublicIp + ", sPort=" + sPort
                + ", sMode=" + sMode + ", nStartIntegrity=" + nStartIntegrity
                + ", nAutoIntegrity=" + nAutoIntegrity + ", nAutoIntegrityMin="
                + nAutoIntegrityMin + ", dblTotalPacketBytes="
                + dblTotalPacketBytes + ", dblPps1000=" + dblPps1000
                + ", dblSession=" + dblSession + ", dblLps=" + dblLps
                + ", lProcessNum=" + lProcessNum + ", dblCpuTotal="
                + dblCpuTotal + ", dblCurCpuUsage=" + dblCurCpuUsage
                + ", dblMemTotal=" + dblMemTotal + ", dblMemUsed=" + dblMemUsed
                + ", dblHddTotal=" + dblHddTotal + ", dblHddUsed=" + dblHddUsed
                + ", tmCur=" + tmCur + ", lsensorIndex=" + lsensorIndex
                + ", strFromIp=" + strFromIp + ", strToIp=" + strToIp
                + ", bType=" + bType + ", nHyperScanHitCount="
                + nHyperScanHitCount + ", sUseBlackList=" + sUseBlackList
                + ", nDirectIntegrity=" + nDirectIntegrity + "]";
    }
}
