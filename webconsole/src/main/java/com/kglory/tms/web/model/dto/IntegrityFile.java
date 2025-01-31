package com.kglory.tms.web.model.dto;

public class IntegrityFile implements Comparable<IntegrityFile> {

    private Long integrityLIndex;
    private String strFileName;
    private String strPath;
    private String fileHashcode;
    private Integer nCheck;

    public Long getIntegrityLIndex() {
        return integrityLIndex;
    }

    public void setIntegrityLIndex(Long integrityLIndex) {
        this.integrityLIndex = integrityLIndex;
    }

    public String getStrFileName() {
        return strFileName;
    }

    public void setStrFileName(String strFileName) {
        this.strFileName = strFileName;
    }

    public String getStrPath() {
        return strPath;
    }

    public void setStrPath(String strPath) {
        this.strPath = strPath;
    }

    public String getFileHashcode() {
        return fileHashcode;
    }

    public void setFileHashcode(String fileHashcode) {
        this.fileHashcode = fileHashcode;
    }

    public Integer getnCheck() {
        return nCheck;
    }

    public void setnCheck(Integer nCheck) {
        this.nCheck = nCheck;
    }

    @Override
    public String toString() {
        return "IntegrityFile [integrityLIndex=" + integrityLIndex
                + ", strFileName=" + strFileName + ", nCheck=" + nCheck + "]";
    }

    @Override
    public int compareTo(IntegrityFile o) {
        long compareNo = ((IntegrityFile) o).getIntegrityLIndex();

        return this.integrityLIndex < compareNo ? -1 : this.integrityLIndex > compareNo ? 1 : 0;
    }

}
