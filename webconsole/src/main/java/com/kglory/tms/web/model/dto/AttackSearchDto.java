package com.kglory.tms.web.model.dto;

public class AttackSearchDto extends SearchDto{
	
	private Long lCode;
	
	
	public Long getlCode() {
		return lCode;
	}

	public void setlCode(Long lCode) {
		this.lCode = lCode;
	}

	@Override
	public String toString() {
		return "AttackSearchDto [lCode=" + lCode + "]";
	}

}
