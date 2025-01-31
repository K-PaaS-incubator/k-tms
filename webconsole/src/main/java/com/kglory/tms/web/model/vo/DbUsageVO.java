package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

public class DbUsageVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 5048314996892878188L;

	private long				ltablespaceindex				= 0;
	private String				strName							= "";
	private String				tmoccur							= "";
	private long				dblTotal						= 0;
	private long				dblUsed							= 0;
	private long				dblSpace							= 0;
	
	public long getLtablespaceindex() {
		return ltablespaceindex;
	}
	public void setLtablespaceindex(long ltablespaceindex) {
		this.ltablespaceindex = ltablespaceindex;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getTmoccur() {
		return tmoccur;
	}
	public void setTmoccur(String tmoccur) {
		this.tmoccur = tmoccur;
	}
	
	public long getDblTotal() {
		return dblTotal;
	}
	public void setDblTotal(long dblTotal) {
		this.dblTotal = dblTotal;
	}
	public long getDblUsed() {
		return dblUsed;
	}
	public void setDblUsed(long dblUsed) {
		this.dblUsed = dblUsed;
	}
	
	public long getDblSpace() {
		return dblSpace;
	}
	public void setDblSpace(long dblSpace) {
		this.dblSpace = dblSpace;
	}
	@Override
	public String toString() {
		return "DbUsageVO [ltablespaceindex=" + ltablespaceindex + ", strName="
				+ strName + ", tmoccur=" + tmoccur + ", dblTotal=" + dblTotal
				+ ", dblUsed=" + dblUsed + ", dblSpace=" + dblSpace + "]";
	}

	
}
