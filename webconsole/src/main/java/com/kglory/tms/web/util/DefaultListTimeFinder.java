package com.kglory.tms.web.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Deprecated
public class DefaultListTimeFinder {
	
	public static final int	KEY_5M	= 5*60*1000;
	public static final int	KEY_1H	= 60*60*1000;
	public static final int	KEY_1D	= 24*60*60*1000;
	
	/**
	 * 시간 간격에 따른 최근 시간 구하는 API<br/>
	 * TimeUtil의 getLatestTime() 메서드를 사용하기를 권장함.
	 * 
	 * @param startDate
	 *            검색 시작 시간
	 * @param endDate
	 *            검색 종료 시간
	 * @return 검색 시작 시간, 검색 종료 시간
	 */
	@Deprecated
	public static List<String> getDefaultTime(Calendar startDate, Calendar endDate) {
		ArrayList<String> statsTimes = new ArrayList<String>();
		
		if (startDate != null && endDate != null) {
			long differenceOfDate = TimeUtil.diffOfDate(startDate.getTime(), endDate.getTime());
			
			if (differenceOfDate >= 15) {
				// 1D_yy_mm
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				statsTimes.add(formatter.format(endDate.getTimeInMillis()-KEY_1D));
				statsTimes.add(formatter.format(endDate.getTimeInMillis()));
			} else if (3 <= differenceOfDate && differenceOfDate < 15) {
				// 1H_yy_mm_dd
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				statsTimes.add(formatter.format(endDate.getTimeInMillis()-KEY_1H));
				statsTimes.add(formatter.format(endDate.getTimeInMillis()));
			} else { // Less than 3 days
				// 5M_yy_mm_dd
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
				statsTimes.add(formatter.format(endDate.getTimeInMillis()-KEY_5M));
				statsTimes.add(formatter.format(endDate.getTimeInMillis()));
				
			}
		}
		
		return statsTimes;
	}
	
	@Deprecated
	public static List<String> getDefaultTime(String strStartDate, String strEndDate) {
		return getDefaultTime(TimeUtil.parseDateTime(strStartDate), TimeUtil.parseDateTime(strEndDate));
	}
}
