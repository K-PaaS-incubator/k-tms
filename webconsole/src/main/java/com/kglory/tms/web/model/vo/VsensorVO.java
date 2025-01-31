package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.VsensorDto;

public class VsensorVO extends CommonBean implements Serializable {

    private static final long serialVersionUID = 1304889140214435079L;

    private Long lvsensorIndex;
    private String strName = "";
    private String strDescription = "";
    private Long sZip1;

    private String lsensorName = "";
    private Long lIndex;
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

    public Long getlIndex() {
        return lIndex;
    }

    public void setlIndex(Long lIndex) {
        this.lIndex = lIndex;
    }

    public Integer getnSaveAppData() {
        return nSaveAppData;
    }

    public void setnSaveAppData(Integer nSaveAppData) {
        this.nSaveAppData = nSaveAppData;
    }

    @Override
    public String toString() {
        return "VsensorVO [lvsensorIndex=" + lvsensorIndex + ", strName="
                + strName + ", strDescription=" + strDescription + ", sZip1="
                + sZip1 + ", lsensorName=" + lsensorName + ", lIndex=" + lIndex
                + "]";
    }

    public boolean equalsVsensorInfo(VsensorDto dto) {
        if (strName.equals(dto.getStrName()) && strDescription.equals(dto.getStrDescription())) {
            return true;
        }
        return false;
    }
}
