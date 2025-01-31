package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kglory.tms.web.model.CommonBean;

public class SystemlogStoreCountVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 3943683195207356968L;

	private BigInteger			lSystemlogIndex;
	private String				tmOccur;
	private Integer				dwRcvLog;
	private Integer				dwDropLog;
	private Integer				dwDropIpLog;
	private Integer				dwStoreLog;
	private Integer				dwRawRcv;
	private Integer				dwDropRawData;
	private Integer				dwStoreRawData;
	private Integer				dwRcvProtocol;
	private Integer				dwDropProtocol;
	private Integer				dwDropIpcProtocol;
	private Integer				dwStoreProtocol;
	private Integer				dwRcvService;
	private Integer				dwDropService;
	private Integer				dwDropIpcService;
	private Integer				dwStoreService;
	private Integer				dwRcvIpTraffic;
	private Integer				dwDropIpTraffic;
	private Integer				dwDropIpcIpTraffic;
	private Integer				dwStoreIpTraffic;
	private Integer				dwRcvTrafficIp;
	private Integer				dwDropTrafficIp;
	private Integer				dwDropIpcTrafficIp;
	private Integer				dwStoreTrafficIp;
	private Integer				dwRcvSession;
	private Integer				dwDropSession;
	private Integer				dwDropIpcSession;
	private Integer				dwStoreSession;
	private Integer				dwRcvIpsLog;
	private Integer				dwDropIpsLog;
	private Integer				dwIpcIpsLog;
	private Integer				dwStoreIpsLog;
	
	public BigInteger getlSystemlogIndex() {
		return lSystemlogIndex;
	}
	
	public void setlSystemlogIndex(BigInteger lSystemlogIndex) {
		this.lSystemlogIndex = lSystemlogIndex;
	}
	
	public String getTmOccur() {
		return tmOccur;
	}
	
	public void setTmOccur(String tmOccur) {
		this.tmOccur = tmOccur;
	}
	
	public Integer getDwRcvLog() {
		return dwRcvLog;
	}
	
	public void setDwRcvLog(Integer dwRcvLog) {
		this.dwRcvLog = dwRcvLog;
	}
	
	public Integer getDwDropLog() {
		return dwDropLog;
	}
	
	public void setDwDropLog(Integer dwDropLog) {
		this.dwDropLog = dwDropLog;
	}
	
	public Integer getDwDropIpLog() {
		return dwDropIpLog;
	}
	
	public void setDwDropIpLog(Integer dwDropIpLog) {
		this.dwDropIpLog = dwDropIpLog;
	}
	
	public Integer getDwStoreLog() {
		return dwStoreLog;
	}
	
	public void setDwStoreLog(Integer dwStoreLog) {
		this.dwStoreLog = dwStoreLog;
	}
	
	public Integer getDwRawRcv() {
		return dwRawRcv;
	}
	
	public void setDwRawRcv(Integer dwRawRcv) {
		this.dwRawRcv = dwRawRcv;
	}
	
	public Integer getDwDropRawData() {
		return dwDropRawData;
	}
	
	public void setDwDropRawData(Integer dwDropRawData) {
		this.dwDropRawData = dwDropRawData;
	}
	
	public Integer getDwStoreRawData() {
		return dwStoreRawData;
	}
	
	public void setDwStoreRawData(Integer dwStoreRawData) {
		this.dwStoreRawData = dwStoreRawData;
	}
	
	public Integer getDwRcvProtocol() {
		return dwRcvProtocol;
	}
	
	public void setDwRcvProtocol(Integer dwRcvProtocol) {
		this.dwRcvProtocol = dwRcvProtocol;
	}
	
	public Integer getDwDropProtocol() {
		return dwDropProtocol;
	}
	
	public void setDwDropProtocol(Integer dwDropProtocol) {
		this.dwDropProtocol = dwDropProtocol;
	}
	
	public Integer getDwDropIpcProtocol() {
		return dwDropIpcProtocol;
	}
	
	public void setDwDropIpcProtocol(Integer dwDropIpcProtocol) {
		this.dwDropIpcProtocol = dwDropIpcProtocol;
	}
	
	public Integer getDwStoreProtocol() {
		return dwStoreProtocol;
	}
	
	public void setDwStoreProtocol(Integer dwStoreProtocol) {
		this.dwStoreProtocol = dwStoreProtocol;
	}
	
	public Integer getDwRcvService() {
		return dwRcvService;
	}
	
	public void setDwRcvService(Integer dwRcvService) {
		this.dwRcvService = dwRcvService;
	}
	
	public Integer getDwDropService() {
		return dwDropService;
	}
	
	public void setDwDropService(Integer dwDropService) {
		this.dwDropService = dwDropService;
	}
	
	public Integer getDwDropIpcService() {
		return dwDropIpcService;
	}
	
	public void setDwDropIpcService(Integer dwDropIpcService) {
		this.dwDropIpcService = dwDropIpcService;
	}
	
	public Integer getDwStoreService() {
		return dwStoreService;
	}
	
	public void setDwStoreService(Integer dwStoreService) {
		this.dwStoreService = dwStoreService;
	}
	
	public Integer getDwRcvIpTraffic() {
		return dwRcvIpTraffic;
	}
	
	public void setDwRcvIpTraffic(Integer dwRcvIpTraffic) {
		this.dwRcvIpTraffic = dwRcvIpTraffic;
	}
	
	public Integer getDwDropIpTraffic() {
		return dwDropIpTraffic;
	}
	
	public void setDwDropIpTraffic(Integer dwDropIpTraffic) {
		this.dwDropIpTraffic = dwDropIpTraffic;
	}
	
	public Integer getDwDropIpcIpTraffic() {
		return dwDropIpcIpTraffic;
	}
	
	public void setDwDropIpcIpTraffic(Integer dwDropIpcIpTraffic) {
		this.dwDropIpcIpTraffic = dwDropIpcIpTraffic;
	}
	
	public Integer getDwStoreIpTraffic() {
		return dwStoreIpTraffic;
	}
	
	public void setDwStoreIpTraffic(Integer dwStoreIpTraffic) {
		this.dwStoreIpTraffic = dwStoreIpTraffic;
	}
	
	public Integer getDwRcvTrafficIp() {
		return dwRcvTrafficIp;
	}
	
	public void setDwRcvTrafficIp(Integer dwRcvTrafficIp) {
		this.dwRcvTrafficIp = dwRcvTrafficIp;
	}
	
	public Integer getDwDropTrafficIp() {
		return dwDropTrafficIp;
	}
	
	public void setDwDropTrafficIp(Integer dwDropTrafficIp) {
		this.dwDropTrafficIp = dwDropTrafficIp;
	}
	
	public Integer getDwDropIpcTrafficIp() {
		return dwDropIpcTrafficIp;
	}
	
	public void setDwDropIpcTrafficIp(Integer dwDropIpcTrafficIp) {
		this.dwDropIpcTrafficIp = dwDropIpcTrafficIp;
	}
	
	public Integer getDwStoreTrafficIp() {
		return dwStoreTrafficIp;
	}
	
	public void setDwStoreTrafficIp(Integer dwStoreTrafficIp) {
		this.dwStoreTrafficIp = dwStoreTrafficIp;
	}
	
	public Integer getDwRcvSession() {
		return dwRcvSession;
	}
	
	public void setDwRcvSession(Integer dwRcvSession) {
		this.dwRcvSession = dwRcvSession;
	}
	
	public Integer getDwDropSession() {
		return dwDropSession;
	}
	
	public void setDwDropSession(Integer dwDropSession) {
		this.dwDropSession = dwDropSession;
	}
	
	public Integer getDwDropIpcSession() {
		return dwDropIpcSession;
	}
	
	public void setDwDropIpcSession(Integer dwDropIpcSession) {
		this.dwDropIpcSession = dwDropIpcSession;
	}
	
	public Integer getDwStoreSession() {
		return dwStoreSession;
	}
	
	public void setDwStoreSession(Integer dwStoreSession) {
		this.dwStoreSession = dwStoreSession;
	}
	
	public Integer getDwRcvIpsLog() {
		return dwRcvIpsLog;
	}
	
	public void setDwRcvIpsLog(Integer dwRcvIpsLog) {
		this.dwRcvIpsLog = dwRcvIpsLog;
	}
	
	public Integer getDwDropIpsLog() {
		return dwDropIpsLog;
	}
	
	public void setDwDropIpsLog(Integer dwDropIpsLog) {
		this.dwDropIpsLog = dwDropIpsLog;
	}
	
	public Integer getDwIpcIpsLog() {
		return dwIpcIpsLog;
	}
	
	public void setDwIpcIpsLog(Integer dwIpcIpsLog) {
		this.dwIpcIpsLog = dwIpcIpsLog;
	}
	
	public Integer getDwStoreIpsLog() {
		return dwStoreIpsLog;
	}
	
	public void setDwStoreIpsLog(Integer dwStoreIpsLog) {
		this.dwStoreIpsLog = dwStoreIpsLog;
	}
	
	@Override
	public String toString() {
		return "SystemlogStoreCountVO [lSystemlogIndex=" + lSystemlogIndex + ", tmOccur=" + tmOccur + ", dwRcvLog="
				+ dwRcvLog + ", dwDropLog=" + dwDropLog + ", dwDropIpLog=" + dwDropIpLog + ", dwStoreLog=" + dwStoreLog
				+ ", dwRawRcv=" + dwRawRcv + ", dwDropRawData=" + dwDropRawData + ", dwStoreRawData=" + dwStoreRawData
				+ ", dwRcvProtocol=" + dwRcvProtocol + ", dwDropProtocol=" + dwDropProtocol + ", dwDropIpcProtocol="
				+ dwDropIpcProtocol + ", dwStoreProtocol=" + dwStoreProtocol + ", dwRcvService=" + dwRcvService
				+ ", dwDropService=" + dwDropService + ", dwDropIpcService=" + dwDropIpcService + ", dwStoreService="
				+ dwStoreService + ", dwRcvIpTraffic=" + dwRcvIpTraffic + ", dwDropIpTraffic=" + dwDropIpTraffic
				+ ", dwDropIpcIpTraffic=" + dwDropIpcIpTraffic + ", dwStoreIpTraffic=" + dwStoreIpTraffic
				+ ", dwRcvTrafficIp=" + dwRcvTrafficIp + ", dwDropTrafficIp=" + dwDropTrafficIp
				+ ", dwDropIpcTrafficIp=" + dwDropIpcTrafficIp + ", dwStoreTrafficIp=" + dwStoreTrafficIp
				+ ", dwRcvSession=" + dwRcvSession + ", dwDropSession=" + dwDropSession + ", dwDropIpcSession="
				+ dwDropIpcSession + ", dwStoreSession=" + dwStoreSession + ", dwRcvIpsLog=" + dwRcvIpsLog
				+ ", dwDropIpsLog=" + dwDropIpsLog + ", dwIpcIpsLog=" + dwIpcIpsLog + ", dwStoreIpsLog="
				+ dwStoreIpsLog + "]";
	}
	
}
