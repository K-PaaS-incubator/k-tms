package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.vo.ManagerIntegrityFileVO;
import com.kglory.tms.web.util.NumberUtil;

public class ManagerIntegrityFileDto extends CommonBean {

    private long nStartIntegrity;
    private long nAutoIntegrity;
    private long nAutoIntegrityMin;

    private List<IntegrityFile> fileList;

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

    public List<IntegrityFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<IntegrityFile> fileList) {
        this.fileList = fileList;
    }

    @Override
    public String toString() {
        return "ManagerIntegrityFileDto [nStartIntegrity=" + nStartIntegrity
                + ", nAutoIntegrity=" + nAutoIntegrity + ", nAutoIntegrityMin="
                + nAutoIntegrityMin + ", fileList=" + fileList + "]";
    }

    public boolean equalsFilelist(List<ManagerIntegrityFileVO> list) {
        boolean rtn = true;
        if (fileList.size() != list.size()) {
            return false;
        }
        for (int i = 0; i < fileList.size(); i++) {
            if (NumberUtil.longEquals(fileList.get(i).getIntegrityLIndex(), list.get(i).getIntegrityLIndex()) == false
                    || fileList.get(i).getnCheck() != Long.valueOf(list.get(i).getnCheck()).intValue()) {
                rtn = false;
                break;
            }
        }
        return rtn;
    }
}
