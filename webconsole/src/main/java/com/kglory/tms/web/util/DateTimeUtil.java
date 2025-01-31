/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.services.systemSettings.SystemConfService;
import com.kglory.tms.web.servlet.SessionListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;


/**
 * 날짜와 시간에 관련 Utility 클래스
 *
 * @author leecjong
 */
public class DateTimeUtil {

    private static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);

    /**
     * YYYYMMDDhhmmss' in the form of 14-digit date string SimpleDateFomat
     * converted to a format that supports them.
     *
     * @param fixedDate 'YYYYMMDDhhmmss' , <b> Must be 14 length. </br>
     * @param pattern format pattern
     * @return formated string
     */
    public static String getFixedSimpleDateFormat(String fixedDate, String pattern) {
        if (fixedDate == null || fixedDate.length() < 14) {
            return fixedDate;
        }
        int year = Integer.valueOf(fixedDate.substring(0, 4));
        int month = Integer.valueOf(fixedDate.substring(4, 6)) - 1;  // calendar  +1
        int day = Integer.valueOf(fixedDate.substring(6, 8));
        int hh = Integer.valueOf(fixedDate.substring(8, 10));
        int mm = Integer.valueOf(fixedDate.substring(10, 12));
        int ss = Integer.valueOf(fixedDate.substring(12, 14));

        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, day, hh, mm, ss);

        FastDateFormat ff = FastDateFormat.getInstance(pattern);
        return ff.format(c);
    }

    /**
     * 현재시간을 구한다
     *
     * @param pattern 패턴 문자열이 없으면 기본 패턴 "yyyyMMddHHmmss" 을 사용
     * @return yyyyMMddHHmmss 형태의 날짜시간 문자열
     */
    public static String getNowSimpleDateFormat(String pattern) {
        String p = "yyyyMMddHHmmss";
        if (pattern != null) {
            p = pattern;
        }

        Calendar c = Calendar.getInstance();
        FastDateFormat ff = FastDateFormat.getInstance(p);
        return ff.format(c);
    }

    public static String getSimpleDateFormat(Calendar c, String pattern) {
        FastDateFormat ff = FastDateFormat.getInstance(pattern);
        return ff.format(c);
    }

    /**
     * yyyyMMddHHmmss 형태의 현재 날짜을 얻는다
     *
     * @return
     */
    public static String getNow() {
        return getNowSimpleDateFormat(null);
    }

    public static Date getNowZeroTimeSec() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);

        Date d = new Date(c.getTimeInMillis());
        return d;
    }
    
    /**
     * 요청일 시간 분 초 zero 요청
     * 2018-01-01 00:00
     * @param pattern
     * @return 
     */
    public static String getNowZeroTime(String pattern) {
        String p = "yyyy-MM-dd HH:mm";
        if (pattern != null) {
            p = pattern;
        }
        Calendar c = Calendar.getInstance();

        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR, 0);

        Date d = new Date(c.getTimeInMillis());
        return DateTimeUtil.getDateToStr(d.getTime(), p);
    }
    
    /**
     * 요청일 초 zero 요청
     * 2018-01-01 00:00
     * @param pattern
     * @return 
     */
    public static String getNowZeroSec(String pattern) {
        String p = "yyyy-MM-dd HH:mm";
        if (pattern != null) {
            p = pattern;
        }
        Calendar c = Calendar.getInstance();

        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);

        Date d = new Date(c.getTimeInMillis());
        return DateTimeUtil.getDateToStr(d.getTime(), p);
    }
    
    /**
     * temp date time 조회
     * @param amount
     * @return 
     */
    public static String getNowTempDateHour(int amount) {
        String p = "yyyy-MM-dd HH:mm";
        Date d = getStrToDate(getNowDate(null));
        d = getChangeHour(d, amount);
        return DateTimeUtil.getDateToStr(d.getTime(), p);
    }
    
    /**
     * temp date time 조회
     * @param amount
     * @return 
     */
    public static String getNowTempDateHourMin(int hour, int min) {
        String p = "yyyy-MM-dd HH:mm";
        Date d = getStrToDate(getNowDate(null));
        d = getChangeHour(d, hour);
        d = getChangeMinute(d, min);
        return DateTimeUtil.getDateToStr(d.getTime(), p);
    }

    /**
     * Date 값을 String으로 변환
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateToStr(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return DateTimeUtil.getDateToStr(date.getTime(), pattern);
    }

    public static String getDateToStr(Date date) {
        if (date == null) {
            return null;
        }
        return DateTimeUtil.getDateToStr(date.getTime());
    }

    public static String getDateToStr(Long mill) {
        String p = "yyyyMMddHHmmss";
        return getDateToStr(mill, p);
    }

    public static String getDateToStr(Long mill, String pattern) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeInMillis(mill);
        return DateTimeUtil.getSimpleDateFormat(c, pattern);
    }

    /**
     * String 포맷을 Date형으로 변환 , 반드시 yyyy-MM-dd 형태의 문자열
     *
     * @param sDate
     * @return
     */
    public static Date getStrToDate(String sDate) {
        String pattern = "yyyy-MM-dd";
        return DateTimeUtil.getStrToDate(sDate, pattern);

    }

    public static Date getStrToDate(String sDate, String pattern) {
        SimpleDateFormat sm = new SimpleDateFormat(pattern);

        try {
            return sm.parse(sDate);
        } catch (ParseException pe) {
            log.error(pe.getLocalizedMessage());
        }
        return null;
    }

    /**
     * String 포맷을 Date형으로 변환 , 반드시 yyyy-MM-dd HH:mm:ss 형태의 문자열
     *
     * @param sDateTime
     * @return
     */
    public static Date getStrToDateTime(String sDateTime) {
        return DateTimeUtil.getStrToDateTime(sDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getStrToDateTime(String sDateTime, String pattern) {
        String defaultPattern = "yyyy-MM-dd HH:mm:ss";
        if (pattern != null) {
            defaultPattern = pattern;
        }

        SimpleDateFormat sm = new SimpleDateFormat(defaultPattern);

        try {
            return sm.parse(sDateTime);
        } catch (ParseException pe) {
            log.error(pe.getLocalizedMessage());
        }
        return null;
    }

    public static long getLongToDateTime(String sDateTime, String pattern) {
        Date d = DateTimeUtil.getStrToDateTime(sDateTime, pattern);
        return d.getTime();

    }

    /**
     * 주어진 날짜를 기준으로 날짜를 + , - 한다. 하루전 날짜를 원할경우 -1,
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date getChangeYear(Date date, int amount) {
        return DateTimeUtil.getChangeCalendar(date, Calendar.YEAR, amount);
    }

    /**
     * 주어진 날짜를 기준으로 날짜를 + , - 한다. 하루전 날짜를 원할경우 -1,
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date getChangeDay(Date date, int amount) {
        return DateTimeUtil.getChangeCalendar(date, Calendar.DAY_OF_MONTH, amount);
    }
    
    public static String getChangeDay(String date, int amount, String pattern) {
        if (pattern == null) {
            pattern = "yyyy-MM-dd";
        }
        return getDateToStr(getChangeCalendar(getStrToDate(date, pattern), Calendar.DAY_OF_MONTH, amount), pattern);
    }

    /**
     * 주어진 날짜를 기준으로 시간를 + , - 한다. 한시간전 날짜를 원할경우 -1,
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date getChangeHour(Date date, int amount) {
        return DateTimeUtil.getChangeCalendar(date, Calendar.HOUR_OF_DAY, amount);
    }

    /**
     * 분을 변경한다.
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date getChangeMinute(Date date, int amount) {
        return DateTimeUtil.getChangeCalendar(date, Calendar.MINUTE, amount);
    }

    /**
     * 초를 변경한다.
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date getChangeSecond(Date date, int amount) {
        return DateTimeUtil.getChangeCalendar(date, Calendar.SECOND, amount);
    }

    /**
     * 주어진 날짜를 일, 시간, 분을 +, - 한다.
     *
     * @param date
     * @param field , Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
     * Calendar.MINUTE
     * @param amount
     * @return
     */
    public static Date getChangeCalendar(Date date, int field, int amount) {
        return DateTimeUtil.getChangeCalendar(date.getTime(), field, amount);
    }

    public static Date getChangeCalendar(long longDate, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.clear();

        c.setTimeInMillis(longDate);
        c.add(field, amount);
        return c.getTime();
    }

    public static String getChangeCalendar(Long mill, int field, int amount, String pattern) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeInMillis(mill);
        c.add(field, amount);
        return DateTimeUtil.getSimpleDateFormat(c, pattern);
    }

    /**
     * 현재 시간의 시분초를 0으로 초기화한 Date 객체 생성
     *
     * @return
     */
    public static Date getZeroTimeDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        Date d = new Date(c.getTimeInMillis());
        return d;

    }

    public static Date getZeroTimeDate(Date date) {
        Calendar c = Calendar.getInstance();

        c.clear();
        c.setTimeInMillis(date.getTime());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);

        Date d = new Date(c.getTimeInMillis());
        return d;
    }

    public static Date getZeroTimeMinute(Date date) {
        Calendar c = Calendar.getInstance();

        c.clear();
        c.setTimeInMillis(date.getTime());
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);

        Date d = new Date(c.getTimeInMillis());
        return d;
    }

    public static Date getZeroTimeSec(Date date) {
        Calendar c = Calendar.getInstance();

        c.clear();
        c.setTimeInMillis(date.getTime());
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);

        Date d = new Date(c.getTimeInMillis());
        return d;
    }

    /**
     * 현재 날짜를 입력받은 포맷의 형태로 변환하여 결과값을 리턴하도로 한다. 예) format : yyyyMMddHHmmss -->
     * 20031130124130 로 결과값 반환
     */
    public static String getTime(String format) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    /**
     * 마지막 월, 일
     *
     * @param date
     * @param field
     * @return
     */
    public static int getLastDay(Date date, int field) {
        Calendar c = Calendar.getInstance();
        c.clear();

        c.setTimeInMillis(date.getTime());
        return c.getActualMaximum(field);
    }

    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.clear();

        c.setTimeInMillis(date.getTime());
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static long diffOfDate(String begin, String end) throws BaseException {
    	try {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	        Date beginDate = formatter.parse(begin);
			Date endDate = formatter.parse(end);
			long diff = endDate.getTime() - beginDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			return diffDays;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new BaseException(e);
		}
    }

    public static String getStrDateToCronExpression(String date) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(4, 6));
        int day = Integer.valueOf(date.substring(6, 8));
        int hh = Integer.valueOf(date.substring(8, 10));
        int mm = Integer.valueOf(date.substring(10, 12));
        int ss = Integer.valueOf(date.substring(12, 14));

        StringBuilder sb = new StringBuilder(20);
        sb.append(ss).append(" ").append(mm).append(" ");
        sb.append(hh).append(" ").append(day).append(" ");
        sb.append(month).append(" ? ").append(year);

        //ss + " " + mm + " " + hh + " " + day + " " + month + " ? " + year;
        return sb.toString();
    }

    /////////////////lee ////////////////////////////////////////////////
    public static String date2String(java.util.Date d) {
        return date2String(d, "yyyy/MM/dd");
    }

    /**
     * 날짜형태의 데이터를 사용자정의형태로 바꿔주는 메소드 Date -> String (2000/09/25)
     *
     * @param d 설정된 날짜표시형태로 변경할 date객체
     * @return yyyy/mm/dd형태로 변경되어진 문자열
     */
    public static String date2String(java.util.Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    /**
     * 문자열 데이터를 yyyy/mm/dd형태의 Date형태의 객체로 바꾸어준다 String -> Date (2000/09/25)
     *
     * @param s Date형태로 만들게 될 yyyy/mm/dd형태의 문자열
     * @return yyyy/mm/dd형태로 변경되어진 Date객체
     */
    public static java.util.Date string2Date(String s) {
        return string2Date(s, "yyyy/MM/dd");
    }

    /**
     * 문자열 데이터를 yyyy/mm/dd HH:mm:ss형태의 Date형태의 객체로 바꾸어준다 String -> Date
     * (2000/09/25 12:12:12)
     *
     * @param s Date형태로 만들게 될 yyyy/mm/dd HH:mm형태의 문자열
     * @return yyyy/mm/dd형태로 변경되어진 Date객체
     */
    public static java.util.Date string2DateTime(String s) {
        return string2Date(s, "yyyy/MM/dd HH:mm");
    }

    public static java.util.Date string2Time(String s) {
        return string2Date(s, "HH:mm");
    }

    /**
     * 문자열 데이터를 사용자형태의 Date형태의 객체로 바꾸어준다 String -> Date (2000/09/25)
     *
     * @param s Date형태로 만들게 될 yyyy/mm/dd형태의 문자열
     * @return yyyy/mm/dd형태로 변경되어진 Date객체
     */
    public static java.util.Date string2Date(String s, String format) {
        java.util.Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        d = sdf.parse(s, new ParsePosition(0));
        return d;
    }

    public static Date makeDateToCron(String rcycle, int outTime, String cron) {

        Calendar cal = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;
        int min;

        if (rcycle.equals("I") || rcycle.equals("O")) {   //00 00 00 05 08 ? 2013
            String[] split = cron.split(" ");
            min = Integer.parseInt(split[1]);
            hour = Integer.parseInt(split[2]);
            day = Integer.parseInt(split[3]);
            month = Integer.parseInt(split[4]) - 1;
            year = Integer.parseInt(split[6]);
            cal.set(year, month, day, hour, min);
        }

        if (rcycle.equals("D")) {  // 00 00 00 * * ?
            String[] split = cron.split(" ");
            if (outTime > 0) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[2]));
            cal.set(Calendar.MINUTE, Integer.parseInt(split[1]));
        }
        if (rcycle.equals("W")) {
            String[] split = cron.split(" ");

            cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(split[5]));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[2]));
            cal.set(Calendar.MINUTE, Integer.parseInt(split[1]));
            if (outTime > 0) {
                cal.add(Calendar.WEEK_OF_MONTH, - 1);
            }
        }

        if (rcycle.equals("M")) {
            String[] split = cron.split(" ");
            if (outTime > 0) {
                cal.add(Calendar.MONTH, -1);
            }
            if (split[3].equals("L")) {
                cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
            } else {
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(split[3]));
            }
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[2]));
            cal.set(Calendar.MINUTE, Integer.parseInt(split[1]));
        }
        return cal.getTime();
    }

    /**
     * 날짜 차이 계산
     *
     * @param sDate
     * @param eDate
     * @return
     */
    public static int getDateDiff(String sDate, String eDate) {

        return getDateDiff0(sDate, eDate, null);
    }
    
    public static int getDateDiff0(String sDate, String eDate, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyyMMdd";
        }
        Date startDate = getStrToDate(sDate, format);
        Date endDate = getStrToDate(eDate, format);

        return getDateDiff(startDate, endDate);
    }

    /**
     * 날짜 차이 계산 format "yyyyMMdd"
     *
     * @param sDate
     * @param eDate
     * @return
     */
    public static int getDateDiff(Date sDate, Date eDate) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(sDate);
        endCal.setTime(eDate);

        long diffMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        int diff = (int) (diffMillis / (24 * 60 * 60 * 1000));

        return diff;
    }

    /**
     * 두 날짜의 특정 필드의 차이를 비교한다.
     *
     * @param field Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND
     * @param sDate
     * @param eDate
     * @return
     */
    public static long getDiffDate(int field, Date sDate, Date eDate) {
        long diff = eDate.getTime() - sDate.getTime();

        if (field == Calendar.DATE) {
            return (diff / (60 * 60 * 1000 * 24));
        } else if (field == Calendar.HOUR_OF_DAY) {
            return (diff / (60 * 60 * 1000));
        } else if (field == Calendar.MINUTE) {
            return (diff / (60 * 1000));
        } else if (field == Calendar.SECOND) {
            return (diff / 1000);
        }
        return diff;
    }
    
    /**
     * 두 날짜의 milliseconds 차이 비교
     * @param sDate
     * @param eDate
     * @return 
     */
    public static long getDiffMilliSeconds(Date sDate, Date eDate) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(sDate);
        endCal.setTime(eDate);

        long diffMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        return diffMillis;
    }
    
    /**
     * 두 날짜의 milliseconds 차이 비교
     * format = "yyyy-MM-dd HH:mm"
     * @param sDate
     * @param eDate
     * @return 
     */
    public static long getDiffMilliSeconds(String startTime, String endTime) {
        String format = "yyyy-MM-dd HH:mm";
        Date sDate = getStrToDate(startTime, format);
        Date eDate = getStrToDate(endTime, format);;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(sDate);
        endCal.setTime(eDate);

        long diffMillis = endCal.getTimeInMillis() - startCal.getTimeInMillis();
        return diffMillis;
    }

    /**
     * 현재 시간 기준 특정 일에 대한 날짜 차이
     *
     * @param diffDate
     * @param pattern
     * @return
     */
    public static int getStrNowDiffDate(String diffDate, String pattern) {
        if (pattern == null) {
            pattern = "yyyyMMddHHmmss";
        }
        Date date = DateTimeUtil.getStrToDateTime(diffDate, pattern);
        Date nowDate = DateTimeUtil.getStrToDateTime(DateTimeUtil.getNowSimpleDateFormat(pattern), pattern);
        
        return DateTimeUtil.getDateDiff(date, nowDate);
    }
    
    public static long getStrNowDiffMilliseconds(String diffDate, String pattern) {
        if (pattern == null) {
            pattern = "yyyyMMddHHmmss";
        }
        Date date = DateTimeUtil.getStrToDateTime(diffDate, pattern);
        Date nowDate = DateTimeUtil.getStrToDateTime(DateTimeUtil.getNowSimpleDateFormat(pattern), pattern);
        
        return DateTimeUtil.getDiffMilliSeconds(date, nowDate);
    }
    
    public static String getNowTableDate() {
        return getNowSimpleDateFormat("yy_MM_dd");
    }
    
    public static String getNowDate(String pattern) {
        String p = "yyyy-MM-dd";
        if (pattern != null) {
            p = pattern;
        }
        return getNowSimpleDateFormat(p);
    }
    
    public static String getNextDayTableDate() {
        return getDateToStr(getChangeDay(getZeroTimeDate(), 1), "yy_MM_dd");
    }
    
    /**
     * 시간 동기화 List
     * @param list(ntpserver 목록)
     * @return 
     */
    public static String getNtpServerDateTime(List<String> list) {
        DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateParser.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(5000);
        for(String ntp : list) {
            try {
                InetAddress inetAddress = InetAddress.getByName(ntp);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                NtpV3Packet message = timeInfo.getMessage();
                TimeStamp tsp = message.getReceiveTimeStamp();
                return dateParser.format(new Date(tsp.getTime()));
            } catch(UnknownHostException e) {
                log.debug("Time Server error : " + ntp);
            } catch (IOException e) {
            	log.debug("Time Server error : " + ntp);
			}
        }
        timeClient.close();
        return null;
    }
    
    /**
     * 시간 동기화 배열
     * @param list(ntpserver 목록)
     * @return 
     */
    public static String getNtpServerDateTime(String[] list) {
        DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateParser.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(5000); // ntpserver 없을시 대기시간 설정
        for(String ntp : list) {
            try {
                InetAddress inetAddress = InetAddress.getByName(ntp);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                NtpV3Packet message = timeInfo.getMessage();
                TimeStamp tsp = message.getReceiveTimeStamp();
                return dateParser.format(new Date(tsp.getTime()));
            } catch(UnknownHostException e) {
                log.debug("Time Server error : " + ntp);
            } catch (IOException e) {
            	log.debug("Time Server error : " + ntp);
			}
        }
        timeClient.close();
        return null;
    }
    
    public static void aliveSessionTimeCheck(WebSocketSession session) {
        SystemConfService confSvc = (SystemConfService) SpringUtils.getBean("systemConfSvc");
        long sessionTime = 0;
		try {
			sessionTime = (long) (confSvc.getSessionTime() * 60 * 1000);
		} catch (BaseException e) {
			log.error(e.getLocalizedMessage());
		}
        try {
	        if ((System.currentTimeMillis() - Constants.getAliveStartTime()) > sessionTime) {
	            log.debug("session time out ~~~~~~~~~~~~~~~~~~~");
	            SessionListener.sessionInvalid();
                session.close();
	        }
        } catch (IOException e) {
        	 log.error(e.getLocalizedMessage());
        }
    }
    
    public static void processResponse(TimeInfo info)
    {
          NtpV3Packet message = info.getMessage();
          int stratum = message.getStratum();
          String refType;
          if (stratum <= 0) {
        	  refType = "(Unspecified or Unavailable)";
          }
          else if (stratum == 1) {
        	  refType = "(Primary Reference; e.g., GPS)"; // GPS, radio clock, etc.
          }
          else {
        	  refType = "(Secondary Reference; e.g. via NTP or SNTP)";
          }
          // stratum should be 0..15...
          int version = message.getVersion();
          int li = message.getLeapIndicator();
  
          int poll = message.getPoll();
          // poll value typically btwn MINPOLL (4) and MAXPOLL (14)
          double disp = message.getRootDispersionInMillisDouble();
  
          int refId = message.getReferenceId();
          String refAddr = NtpUtils.getHostAddress(refId);
          String refName = null;
  	          if (refId != 0) {
  	              if (refAddr.equals("127.127.1.0")) {
                  refName = "LOCAL"; // This is the ref address for the Local Clock
              } else if (stratum >= 2) {
                  // If reference id has 127.127 prefix then it uses its own reference clock
                  // defined in the form 127.127.clock-type.unit-num (e.g. 127.127.8.0 mode 5
                  // for GENERIC DCF77 AM; see refclock.htm from the NTP software distribution.
  	                  if (!refAddr.startsWith("127.127")) {
  	                      try {
                          InetAddress addr = InetAddress.getByName(refAddr);
                          String name = addr.getHostName();
                          if (name != null && !name.equals(refAddr)) {
                        	  refName = name;
                          }
                      } catch (UnknownHostException e) {
                          // some stratum-2 servers sync to ref clock device but fudge stratum level higher... (e.g. 2)
                          // ref not valid host maybe it's a reference clock name?
                          // otherwise just show the ref IP address.
                          refName = NtpUtils.getReferenceClock(message);
                      }
                 }
             } else if (version >= 3 && (stratum == 0 || stratum == 1)) {
                 refName = NtpUtils.getReferenceClock(message);
                 // refname usually have at least 3 characters (e.g. GPS, WWV, LCL, etc.)
             }
             // otherwise give up on naming the beast...
         }
         if (refName != null && refName.length() > 1) {
        	 refAddr += " (" + refName + ")";
         }
 
         TimeStamp refNtpTime = message.getReferenceTimeStamp();
 
         // Originate Time is time request sent by client (t1)
         TimeStamp origNtpTime = message.getOriginateTimeStamp();
 
         long destTime = info.getReturnTime();
         // Receive Time is time request received by server (t2)
         TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
 
         // Transmit time is time reply sent by server (t3)
         TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
 
         // Destination time is time reply received by client (t4)
         TimeStamp destNtpTime = TimeStamp.getNtpTime(destTime);
 
         info.computeDetails(); // compute offset/delay if not already done
         Long offsetValue = info.getOffset();
         Long delayValue = info.getDelay();
         String delay = (delayValue == null) ? "N/A" : delayValue.toString();
         String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();
 
     }
}
