package com.kglory.tms.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	
	public static Calendar parseDateTime(String dateString) {
		if (dateString == null)
			return null;
		DateFormat fmt = null;
		switch (dateString.trim().length()) {
		case 16:
			fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		case 19:
			fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		default:
			fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		}
		Calendar cal = Calendar.getInstance();
		
		if (dateString.contains("T")) {
			dateString = dateString.replace('T', ' ');
		}
		if (dateString.contains("Z")) {
			dateString = dateString.replace("Z", "+0000");
		}
		
		try {
			cal.setTime(fmt.parse(dateString));
			return cal;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Calendar getLatestTime(Calendar startDate, Calendar endDate) {
		Calendar latestCal = Calendar.getInstance();
		latestCal.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DATE),
				endDate.get(Calendar.HOUR), endDate.get(Calendar.MINUTE));
		
		if (startDate != null && endDate != null) {
			long differenceOfDate = TimeUtil.diffOfDate(startDate.getTime(), endDate.getTime());
			
			if (differenceOfDate >= 15) {
				latestCal.add(Calendar.DATE, -1);
			} else if (3 <= differenceOfDate && differenceOfDate < 15) {
				latestCal.add(Calendar.HOUR, -1);
			} else {
				latestCal.add(Calendar.MINUTE, -5);
			}
		}
		
		return latestCal;
	}
	
	public static String getLatestTime(String startDate, String endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formatter.format(getLatestTime(parseDateTime(startDate), parseDateTime(endDate)).getTime());
	}
	
	public static long diffOfDate(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		
		return (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60 * 60 * 24);
	}
	
	public static long diffOfDay(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		
		return (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / 1000;
	}
	
	public static long diffOfHour(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		startCal.set(Calendar.MINUTE, 0);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.MINUTE, 0);
		
		return (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / 1000;
	}
	
	public static long diffOfSecond(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		
		return (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / 1000;
	}
	
	public static long diffOfMinute(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		
		return (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000*60);
	}
}
