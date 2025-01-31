package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class VsensorDto extends CommonBean {

    private Long lvsensorIndex;
    private String strName;
    private String strDescription;
    private Long sZip1;

    private String lsensorName;

    private List<SessionMonitoringFile> fileList;
    private List<VirtualSensorFile> resultList;
    private long lIndex;
    private Long lvsensorIndexUpdate;
    private Long lIndexUpdate;

    private Long lnetworkIndex;
    private Integer nSaveAppData;

    public Long getLvsensorIndex() {
        return lvsensorIndex;
    }

    public void setLvsensorIndex(Long lvsensorIndex) {
        this.lvsensorIndex = lvsensorIndex;
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

    public Long getsZip1() {
        return sZip1;
    }

    public void setsZip1(Long sZip1) {
        this.sZip1 = sZip1;
    }

    public String getLsensorName() {
        return lsensorName;
    }

    public void setLsensorName(String lsensorName) {
        this.lsensorName = lsensorName;
    }

    public List<SessionMonitoringFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<SessionMonitoringFile> fileList) {
        this.fileList = fileList;
    }

    public List<VirtualSensorFile> getResultList() {
        return resultList;
    }

    public void setResultList(List<VirtualSensorFile> resultList) {
        this.resultList = resultList;
    }

    public long getlIndex() {
        return lIndex;
    }

    public void setlIndex(long lIndex) {
        this.lIndex = lIndex;
    }

    public Long getLvsensorIndexUpdate() {
        return lvsensorIndexUpdate;
    }

    public void setLvsensorIndexUpdate(Long lvsensorIndexUpdate) {
        this.lvsensorIndexUpdate = lvsensorIndexUpdate;
    }

    public Long getlIndexUpdate() {
        return lIndexUpdate;
    }

    public void setlIndexUpdate(Long lIndexUpdate) {
        this.lIndexUpdate = lIndexUpdate;
    }

    public Long getLnetworkIndex() {
        return lnetworkIndex;
    }

    public void setLnetworkIndex(Long lnetworkIndex) {
        this.lnetworkIndex = lnetworkIndex;
    }

    public Integer getnSaveAppData() {
        return nSaveAppData;
    }

    public void setnSaveAppData(Integer nSaveAppData) {
        this.nSaveAppData = nSaveAppData;
    }


    public boolean equalsFilelist(List<SessionMonitoringFile> list) {
        boolean rtn = true;
        if (fileList.size() != list.size()) {
            return false;
        }
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).lIndex.equals(list.get(i).getlIndex()) && !fileList.get(i).nPort.equals(list.get(i).getnPort()) && !fileList.get(i).nRenewOption.equals(list.get(i).getnRenewOption())) {
                rtn = false;
                break;
            }
        }
        return rtn;
    }
}
