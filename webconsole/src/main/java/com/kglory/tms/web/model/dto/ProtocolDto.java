package com.kglory.tms.web.model.dto;

public class ProtocolDto  extends SearchDto {
	String	tableName;
	
	private String	tmstart;
	private String	tmend;
	private long 	timeHour;
	private long	dblbps;
        private Integer nProtocol;
        private String protocolName;
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTmstart() {
		return tmstart;
	}
	
	public void setTmstart(String tmstart) {
		this.tmstart = tmstart;
	}
	
	public String getTmend() {
		return tmend;
	}
	
	public void setTmend(String tmend) {
		this.tmend = tmend;
	}
	
	public long getDblbps() {
		return dblbps;
	}
	
	public void setDblbps(long dblbps) {
		this.dblbps = dblbps;
	}
	
	public long getTimeHour() {
		return timeHour;
	}

	public void setTimeHour(long timeHour) {
		this.timeHour = timeHour;
	}

        public Integer getnProtocol() {
                return nProtocol;
        }

        public void setnProtocol(Integer nProtocol) {
                this.nProtocol = nProtocol;
        }
        

	public String getProtocolName() {
			return protocolName;
		}

		public void setProtocolName(String protocolName) {
			this.protocolName = protocolName;
		}

	@Override
	public String toString() {
		return "ProtocolDto [tableName=" + tableName + ", tmstart=" + tmstart
				+ ", tmend=" + tmend + ", timeHour=" + timeHour + ", dblbps="
				+ dblbps + "]";
	}

	
}
