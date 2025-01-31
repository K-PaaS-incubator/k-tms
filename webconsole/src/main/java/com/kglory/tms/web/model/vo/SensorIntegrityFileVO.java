package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author leekyunghee
 *
 */
public class SensorIntegrityFileVO extends CommonBean implements Serializable{

	private static final long serialVersionUID = 4050784764927327079L;

	private long				integrityLIndex;
	private long 				nStartIntegrity;
	private long 				nAutoIntegrity;
	private long 				nAutoIntegrityMin;
	
	private String				strFileName;
	private long				nCheck;
	
	public long getIntegrityLIndex() {
		return integrityLIndex;
	}
	public void setIntegrityLIndex(long integrityLIndex) {
		this.integrityLIndex = integrityLIndex;
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
	public String getStrFileName() {
		return strFileName;
	}
	public void setStrFileName(String strFileName) {
		this.strFileName = strFileName;
	}
	public long getnCheck() {
		return nCheck;
	}
	public void setnCheck(long nCheck) {
		this.nCheck = nCheck;
	}
	@Override
	public String toString() {
		return "SensorIntegrityFileVO [integrityLIndex=" + integrityLIndex
				+ ", nStartIntegrity=" + nStartIntegrity + ", nAutoIntegrity="
				+ nAutoIntegrity + ", nAutoIntegrityMin=" + nAutoIntegrityMin
				+ ", strFileName=" + strFileName + ", nCheck=" + nCheck + "]";
	}
	
	
}
