package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author really486
 *
 */
public class ProtocolVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 1682196618980559767L;

	private String				tmstart;
	private long				dblbps;
        private Integer nProtocol;
        private String protocolName;
	
	public String getTmstart() {
		return tmstart;
	}
	
	public void setTmstart(String tmstart) {
		this.tmstart = tmstart;
	}
	
	public long getDblbps() {
		return dblbps;
	}
	
	public void setDblbps(long dblbps) {
		this.dblbps = dblbps;
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
}
