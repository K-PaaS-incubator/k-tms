package com.kglory.tms.web.model.vo;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.util.NumberUtil;

public class SessionMonitorVO extends CommonBean {
	private String	serviceName;
	private Long	serverPort;
	private String	serverIp;
	private String	clientIp;
	private Long	totalTraffic;
	private Long	serverBytes;
	private Long	clientBytes;
	private String	tmStart;
	private String	tmEnd;
	private Long	vSensorIndex;
	private String	vSensorName;
	private Long	srcNetIndex;
	private String	srcNetworkName;
	private Long	dstNetIndex;
	private String	dstNetworkName;
	private Integer lNetGroupIndex;

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Long getServerPort() {
		return serverPort;
	}
	public void setServerPort(Long serverPort) {
            if (serverPort < 0L) {
                this.serverPort = NumberUtil.portSignedToLong(serverPort);
            } else {
		this.serverPort = serverPort;
            }
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public Long getTotalTraffic() {
		return totalTraffic;
	}
	public void setTotalTraffic(Long totalTraffic) {
		this.totalTraffic = totalTraffic;
	}
	public Long getServerBytes() {
		return serverBytes;
	}
	public void setServerBytes(Long serverBytes) {
		this.serverBytes = serverBytes;
	}
	public Long getClientBytes() {
		return clientBytes;
	}
	public void setClientBytes(Long clientBytes) {
		this.clientBytes = clientBytes;
	}
	public String getTmStart() {
		return tmStart;
	}
	public void setTmStart(String tmStart) {
		this.tmStart = tmStart;
	}
	public String getTmEnd() {
		return tmEnd;
	}
	public void setTmEnd(String tmEnd) {
		this.tmEnd = tmEnd;
	}
	public Long getvSensorIndex() {
		return vSensorIndex;
	}
	public void setvSensorIndex(Long vSensorIndex) {
		this.vSensorIndex = vSensorIndex;
	}
	public String getvSensorName() {
		return vSensorName;
	}
	public void setvSensorName(String vSensorName) {
		this.vSensorName = vSensorName;
	}
	public Long getSrcNetIndex() {
		return srcNetIndex;
	}
	public void setSrcNetIndex(Long srcNetIndex) {
		this.srcNetIndex = srcNetIndex;
	}
	public String getSrcNetworkName() {
		return srcNetworkName;
	}
	public void setSrcNetworkName(String srcNetworkName) {
		this.srcNetworkName = srcNetworkName;
	}
	public Long getDstNetIndex() {
		return dstNetIndex;
	}
	public void setDstNetIndex(Long dstNetIndex) {
		this.dstNetIndex = dstNetIndex;
	}
	public String getDstNetworkName() {
		return dstNetworkName;
	}
	public void setDstNetworkName(String dstNetworkName) {
		this.dstNetworkName = dstNetworkName;
	}
	
	public Integer getlNetGroupIndex() {
		return lNetGroupIndex;
	}
	public void setlNetGroupIndex(Integer lNetGroupIndex) {
		this.lNetGroupIndex = lNetGroupIndex;
	}
	@Override
	public String toString() {
		return "SessionMonitorVO [serviceName=" + serviceName + ", serverPort="
				+ serverPort + ", serverIp=" + serverIp + ", clientIp="
				+ clientIp + ", totalTraffic=" + totalTraffic
				+ ", serverBytes=" + serverBytes + ", clientBytes="
				+ clientBytes + ", tmStart=" + tmStart + ", tmEnd=" + tmEnd
				+ ", vSensorIndex=" + vSensorIndex + ", vSensorName="
				+ vSensorName + ", srcNetIndex=" + srcNetIndex
				+ ", srcNetworkName=" + srcNetworkName + ", dstNetIndex="
				+ dstNetIndex + ", dstNetworkName=" + dstNetworkName
				+ ", lNetGroupIndex=" + lNetGroupIndex + "]";
	}
	
}
