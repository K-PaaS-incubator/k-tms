package com.kglory.tms.web.model.dto;

import java.util.Date;

import com.kglory.tms.web.model.CommonBean;

public class TimeDto extends CommonBean {
	
	private Date	date;
	private long	dateInMillis;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public long getDateInMillis() {
		return dateInMillis;
	}
	
	public void setDateInMillis(long dateInMillis) {
		this.dateInMillis = dateInMillis;
	}
	
	@Override
	public String toString() {
		return "TimeDto [date=" + date + ", dateInMillis=" + dateInMillis + "]";
	}
	
}
