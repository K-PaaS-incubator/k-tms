package com.kglory.tms.web.model.vo;

import java.io.Serializable;

import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.SnmpTrans;

/**
 * @author leekyunghee
 *
 */
public class ManagerSnmpTransVO extends CommonBean implements Comparable<ManagerSnmpTransVO>{

	
	private Integer				lIndex;
	private Integer 			nEventType;
	private Integer 			nMainType;
	private Integer 			nSubType1;
	
	private Integer				nClassType;
	private String				strName;
	public Integer getlIndex() {
		return lIndex;
	}
	public void setlIndex(Integer lIndex) {
		this.lIndex = lIndex;
	}
	public Integer getnEventType() {
		return nEventType;
	}
	public void setnEventType(Integer nEventType) {
		this.nEventType = nEventType;
	}
	public Integer getnMainType() {
		return nMainType;
	}
	public void setnMainType(Integer nMainType) {
		this.nMainType = nMainType;
	}
	public Integer getnSubType1() {
		return nSubType1;
	}
	public void setnSubType1(Integer nSubType1) {
		this.nSubType1 = nSubType1;
	}
	public Integer getnClassType() {
		return nClassType;
	}
	public void setnClassType(Integer nClassType) {
		this.nClassType = nClassType;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	@Override
	public String toString() {
		return "ManagerSnmpTransVO [lIndex=" + lIndex + ", nEventType="
				+ nEventType + ", nMainType=" + nMainType + ", nSubType1="
				+ nSubType1 + ", nClassType=" + nClassType + ", strName="
				+ strName + "]";
	}

    @Override
    public int compareTo(ManagerSnmpTransVO o) {
        int compareNo = o.getlIndex();
        return lIndex < compareNo? -1: lIndex > compareNo? 1:0;
    }
    
    public static ManagerSnmpTransVO dtoToVo(SnmpTrans item) {
        ManagerSnmpTransVO rtn = new ManagerSnmpTransVO();
        rtn.setlIndex(item.getlIndex());
        rtn.setnEventType(item.getnEventType());
        rtn.setnMainType(item.getnMainType());
        rtn.setnSubType1(item.getnSubType1());
        return rtn;
    }
}
