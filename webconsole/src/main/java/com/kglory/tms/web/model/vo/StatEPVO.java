package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author really486
 *
 */
public class StatEPVO extends CommonBean implements Serializable {
	private static final long	serialVersionUID	= 1682196618980559767L;

	
	private long				lcode;
	private String				strTitle;
	private long				eventCount;
	private long				totalCount;
	private String				tmstart;
	private long				eventDblbps;
	private long				dblbps;
	private long				lvsensorindex;
	private String				strName;
	
	public long getLcode() {
		return lcode;
	}
	
	public void setLcode(long lcode) {
		this.lcode = lcode;
	}
	
	public String getStrTitle() {
		return strTitle;
	}
	
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
	}
	
	public long getEventCount() {
		return eventCount;
	}
	
	public void setEventCount(long eventCount) {
		this.eventCount = eventCount;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public String getTmstart() {
		return tmstart;
	}
	
	public void setTmstart(String tmstart) {
		this.tmstart = tmstart;
	}
	
	public long getEventDblbps() {
		return eventDblbps;
	}
	
	public void setEventDblbps(long eventDblbps) {
		this.eventDblbps = eventDblbps;
	}
	
	public long getLvsensorindex() {
		return lvsensorindex;
	}
	
	public void setLvsensorindex(long lvsensorindex) {
		this.lvsensorindex = lvsensorindex;
	}
	
	public String getStrName() {
		return strName;
	}
	
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	public long getDblbps() {
		return dblbps;
	}
	
	public void setDblbps(long dblbps) {
		this.dblbps = dblbps;
	}
	
	@Override
	public String toString() {
		return "StatEPVO [lcode=" + lcode + ", strTitle=" + strTitle + ", eventCount=" + eventCount + ", totalCount="
				+ totalCount + ", tmstart=" + tmstart + ", eventDblbps=" + eventDblbps + ", dblbps=" + dblbps
				+ ", lvsensorindex=" + lvsensorindex + ", strName=" + strName + "]";
	}
}
