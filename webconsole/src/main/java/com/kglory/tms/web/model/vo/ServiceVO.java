package com.kglory.tms.web.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author really486
 *
 */
public class ServiceVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 1682196618980559767L;

	private long				nprotocol;
	private long				wservice;
	private BigDecimal			dblbps;
	private BigDecimal			sumDblbps;
	private BigDecimal			totalbps;
	
	public long getNprotocol() {
		return nprotocol;
	}
	
	public void setNprotocol(long nprotocol) {
		this.nprotocol = nprotocol;
	}
	
	public long getWservice() {
		return wservice;
	}
	
	public void setWservice(long wservice) {
		this.wservice = wservice;
	}
	
	public BigDecimal getDblbps() {
		return dblbps;
	}

	public void setDblbps(BigDecimal dblbps) {
		this.dblbps = dblbps;
	}

	public BigDecimal getSumDblbps() {
		return sumDblbps;
	}

	public void setSumDblbps(BigDecimal sumDblbps) {
		this.sumDblbps = sumDblbps;
	}

	public BigDecimal getTotalbps() {
		return totalbps;
	}

	public void setTotalbps(BigDecimal totalbps) {
		this.totalbps = totalbps;
	}

	@Override
	public String toString() {
		return "ServiceVO [nprotocol=" + nprotocol + ", wservice=" + wservice
				+ ", dblbps=" + dblbps + ", sumDblbps=" + sumDblbps
				+ ", totalbps=" + totalbps + "]";
	}
	
}
