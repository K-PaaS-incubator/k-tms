package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author leekyunghee
 *
 */
public class ManagerIntegrityFileVO extends CommonBean implements Comparable<ManagerIntegrityFileVO>{

	
	private Long				integrityLIndex;
	private Long 				nStartIntegrity;
	private Long 				nAutoIntegrity;
	private Long 				nAutoIntegrityMin;
	
	private String				strFileName;
	private Long				nCheck;
	
	public Long getIntegrityLIndex() {
		return integrityLIndex;
	}
	public void setIntegrityLIndex(Long integrityLIndex) {
		this.integrityLIndex = integrityLIndex;
	}
	public Long getnStartIntegrity() {
		return nStartIntegrity;
	}
	public void setnStartIntegrity(Long nStartIntegrity) {
		this.nStartIntegrity = nStartIntegrity;
	}
	public Long getnAutoIntegrity() {
		return nAutoIntegrity;
	}
	public void setnAutoIntegrity(Long nAutoIntegrity) {
		this.nAutoIntegrity = nAutoIntegrity;
	}
	public Long getnAutoIntegrityMin() {
		return nAutoIntegrityMin;
	}
	public void setnAutoIntegrityMin(Long nAutoIntegrityMin) {
		this.nAutoIntegrityMin = nAutoIntegrityMin;
	}
	public String getStrFileName() {
		return strFileName;
	}
	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}
	public Long getnCheck() {
		return nCheck;
	}
	public void setnCheck(Long nCheck) {
		this.nCheck = nCheck;
	}
	@Override
	public String toString() {
		return "ManagerIntegrityFileVO [integrityLIndex=" + integrityLIndex
				+ ", nStartIntegrity=" + nStartIntegrity + ", nAutoIntegrity="
				+ nAutoIntegrity + ", nAutoIntegrityMin=" + nAutoIntegrityMin
				+ ", strFileName=" + strFileName + ", nCheck=" + nCheck + "]";
	}

    @Override
    public int compareTo(ManagerIntegrityFileVO o) {
        long compareNo = ((ManagerIntegrityFileVO) o).getIntegrityLIndex();
        
        return this.integrityLIndex < compareNo? -1: this.integrityLIndex > compareNo? 1:0;
    }
}
