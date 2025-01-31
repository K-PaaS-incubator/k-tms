package com.kglory.tms.web.model.dto;

public class TargetSensorDto extends SearchDto {
	
	String 	category;
	String 	refIndex;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRefIndex() {
		return refIndex;
	}

	public void setRefIndex(String refIndex) {
		this.refIndex = refIndex;
	}

	@Override
	public String toString(){
		return "TargetSensorDto [ category="+category+", refIndex="+refIndex+"]";
	}
}
