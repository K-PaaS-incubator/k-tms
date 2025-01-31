package com.kglory.tms.web.model.dto;

public class IpSearchDto {
	
	private String	ip;
	
	private String	whoisServer;
	
	private Boolean	hostMapping;
	private Integer	maximumHops;
	private Integer	timeout;
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getWhoisServer() {
		return whoisServer;
	}
	
	public void setWhoisServer(String whoisServer) {
		this.whoisServer = whoisServer;
	}
	
	public Boolean getHostMapping() {
		return hostMapping;
	}
	
	public void setHostMapping(Boolean hostMapping) {
		this.hostMapping = hostMapping;
	}
	
	public Integer getMaximumHops() {
		return maximumHops;
	}
	
	public void setMaximumHops(Integer maximumHops) {
		this.maximumHops = maximumHops;
	}
	
	public Integer getTimeout() {
		return timeout;
	}
	
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public String toString() {
		return "IpSearchDto [ip=" + ip + ", whoisServer=" + whoisServer + ", hostMapping=" + hostMapping
				+ ", maximumHops=" + maximumHops + ", timeout=" + timeout + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
