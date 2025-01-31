package com.kglory.tms.web.model.dto;

import com.kglory.tms.web.model.CommonBean;
import java.util.List;

public class SystemConfigDto extends CommonBean {
    private static final long serialVersionUID = 7591777641315705331L;

    private long lIndex;
    private String strName;
    private String strDescription;
    private String lPrivateIpInput;
    private String lPublicIpInput;
    private long lPrivateIp;
    private long lPublicIp;

    private String queryTableName;
    private String queryTableName2;

    private String strFromIp;
    private String strToIp;
    private Integer bType;
    private long lsensorIndex;
    private long nHyperScanHitCount;
    private Integer sUseBlackList;
    private List<IpMonitorDto> ipMonitorList;
    private List<IpMonitorDto> deleteIpMonitorList;

    public long getlIndex() {
        return lIndex;
    }

    public void setlIndex(long lIndex) {
        this.lIndex = lIndex;
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

    public String getlPrivateIpInput() {
        return lPrivateIpInput;
    }

    public void setlPrivateIpInput(String lPrivateIpInput) {
        this.lPrivateIpInput = lPrivateIpInput;
    }

    public String getlPublicIpInput() {
        return lPublicIpInput;
    }

    public void setlPublicIpInput(String lPublicIpInput) {
        this.lPublicIpInput = lPublicIpInput;
    }

    public long getlPrivateIp() {
        return lPrivateIp;
    }

    public void setlPrivateIp(long lPrivateIp) {
        this.lPrivateIp = lPrivateIp;
    }

    public long getlPublicIp() {
        return lPublicIp;
    }

    public void setlPublicIp(long lPublicIp) {
        this.lPublicIp = lPublicIp;
    }

    public String getQueryTableName() {
        return queryTableName;
    }

    public void setQueryTableName(String queryTableName) {
        this.queryTableName = queryTableName;
    }

    public String getQueryTableName2() {
        return queryTableName2;
    }

    public void setQueryTableName2(String queryTableName2) {
        this.queryTableName2 = queryTableName2;
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

    public long getLsensorIndex() {
        return lsensorIndex;
    }

    public void setLsensorIndex(long lsensorIndex) {
        this.lsensorIndex = lsensorIndex;
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

    public List<IpMonitorDto> getIpMonitorList() {
        return ipMonitorList;
    }

    public void setIpMonitorList(List<IpMonitorDto> ipMonitorList) {
        this.ipMonitorList = ipMonitorList;
    }

    public List<IpMonitorDto> getDeleteIpMonitorList() {
        return deleteIpMonitorList;
    }

    public void setDeleteIpMonitorList(List<IpMonitorDto> deleteIpMonitorList) {
        this.deleteIpMonitorList = deleteIpMonitorList;
    }
    
    public String getIpMonitorListStr() {
        StringBuffer sb = new StringBuffer("");
        if (this.ipMonitorList != null && this.ipMonitorList.size() > 0) {
            int i = 1;
            sb.append(", IP Monitorring [");
            for (IpMonitorDto item : this.ipMonitorList) {
                sb.append(item.getStrFromIp());
                sb.append("/");
                sb.append(item.getStrToIp());
                if (this.ipMonitorList.size() > i) {
                    sb.append(", ");
                }
                i++;
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
