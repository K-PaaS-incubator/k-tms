package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;

/**
 * @author really486
 *
 */
public class TargetVO extends CommonBean implements Serializable {
	
	private static final long	serialVersionUID	= 1682196618980559767L;

	private String				strName;
	private String				type;
	private String				lIndex;
	private long				lParentGroupIndex;
	private String				path;
	private int					cntYType;
	
	
	public String getStrName() {
		return strName;
	}
	
	public void setStrName(String strName) {
		this.strName = strName;
	}
	
	public String getlIndex() {
		return lIndex;
	}
	
	public void setlIndex(String lIndex) {
		this.lIndex = lIndex;
	}
	
	public long getlParentGroupIndex() {
		return lParentGroupIndex;
	}
	
	public void setlParentGroupIndex(long lParentGroupIndex) {
		this.lParentGroupIndex = lParentGroupIndex;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public int getCntYType() {
		return cntYType;
	}
	
	public void setCntYType(int cntYType) {
		this.cntYType = cntYType;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "TargetVO [strName=" + strName + ", type=" + type + ", lIndex="
				+ lIndex + ", lParentGroupIndex=" + lParentGroupIndex
				+ ", path=" + path + ", cntYType=" + cntYType
				+ "]";
	}
	
}
