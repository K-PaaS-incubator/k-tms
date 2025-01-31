package com.kglory.tms.web.model.dto;

import java.math.BigInteger;
import java.util.Arrays;

import com.kglory.tms.web.model.CommonBean;

public class PolicyDto extends CommonBean {
	
	String[]	attackNames;
	BigInteger	attackType;
	Long		lCode;
	
	public String[] getAttackNames() {
		return attackNames;
	}
	
	public void setAttackNames(String[] attackNames) {
		this.attackNames = attackNames;
	}
	
	public BigInteger getAttackType() {
		return attackType;
	}
	
	public void setAttackType(BigInteger attackType) {
		this.attackType = attackType;
	}
	
	public long getlCode() {
		return lCode;
	}

	public void setlCode(Long lCode) {
		this.lCode = lCode;
	}

	@Override
	public String toString() {
		return "PolicyDto [attackNames=" + Arrays.toString(attackNames)
				+ ", attackType=" + attackType + ", lCode=" + lCode + "]";
	}
}
