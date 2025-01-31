package com.kglory.tms.web.model.dto;

import java.util.List;

import com.kglory.tms.web.model.CommonBean;

public class SensorIntegrityFileDto extends CommonBean {

	private long lIndex;
	private long nStartIntegrity;
	private long nAutoIntegrity;
	private long nAutoIntegrityMin;

	private List<Integer> integrityLIndex;
	private List<String> strFileName;
	private List<Integer> nCheck;

	private List<IntegrityFile> fileList;

	public long getlIndex() {
		return lIndex;
	}

	public void setlIndex(long lIndex) {
		this.lIndex = lIndex;
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

	public List<Integer> getIntegrityLIndex() {
		return integrityLIndex;
	}

	public void setIntegrityLIndex(List<Integer> integrityLIndex) {
		this.integrityLIndex = integrityLIndex;
	}

	public List<String> getStrFileName() {
		return strFileName;
	}

	public void setStrFileName(List<String> strFileName) {
		this.strFileName = strFileName;
	}

	public List<Integer> getnCheck() {
		return nCheck;
	}

	public void setnCheck(List<Integer> nCheck) {
		this.nCheck = nCheck;
	}

	public List<IntegrityFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<IntegrityFile> fileList) {
		this.fileList = fileList;
	}

	@Override
	public String toString() {
		return "SensorIntegrityFileDto [lIndex=" + lIndex
				+ ", nStartIntegrity=" + nStartIntegrity + ", nAutoIntegrity="
				+ nAutoIntegrity + ", nAutoIntegrityMin=" + nAutoIntegrityMin
				+ ", integrityLIndex=" + integrityLIndex + ", strFileName="
				+ strFileName + ", nCheck=" + nCheck + ", fileList=" + fileList
				+ "]";
	}

}
