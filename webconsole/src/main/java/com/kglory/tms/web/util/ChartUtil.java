/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.FrameSizeChartVO;
import com.kglory.tms.web.model.vo.IpChartVO;
import com.kglory.tms.web.model.vo.MaliciousTrafficChartVO;
import com.kglory.tms.web.model.vo.ProtocolVO;
import com.kglory.tms.web.model.vo.StatEPVO;
import com.kglory.tms.web.model.vo.TcpFlagChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceChartVO;
import static com.kglory.tms.web.util.DateTimeUtil.getChangeMinute;
import static com.kglory.tms.web.util.DateTimeUtil.getDateToStr;
import static com.kglory.tms.web.util.DateTimeUtil.getDiffDate;
import static com.kglory.tms.web.util.DateTimeUtil.getStrToDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * chart 관련 모음
 *
 * @author leecjong
 */
public class ChartUtil {

    /**
     * 트래픽 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<TrafficProtocolChartVO> getProtocolChartDateTimeList(List<TrafficProtocolChartVO> chartList, String startTime, String endTime) {
        List<TrafficProtocolChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (TrafficProtocolChartVO item : chartList) {
            map.put(item.getTime(), item);
            if (item != null && item.getStrName() != null) {
                strName = item.getStrName();
            }
        }
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            TrafficProtocolChartVO item = (TrafficProtocolChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                TrafficProtocolChartVO vo = new TrafficProtocolChartVO();
                vo.setTime(dateTime);
                vo.setData(0);
                vo.setDataIn(0);
                vo.setDataOut(0);
                vo.setStrName(strName);
                list.add(vo);
            }
        }
        return list;
    }
    
    /**
     * dash board protocol chart
     * @param chartList
     * @param startTime
     * @param endTime
     * @return 
     */
    public static List<ProtocolVO> getDashboardProtocolChart(List<ProtocolVO> chartList, String startTime, String endTime, String format) {
        List<ProtocolVO> list = new ArrayList<>();
        Date startDate = getStrToDate(startTime, "yyyy-MM-dd HH:mm");
        Date endDate = getStrToDate(endTime, "yyyy-MM-dd HH:mm");
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (ProtocolVO item : chartList) {
            map.put(item.getTmstart(), item);
            if (item != null && item.getProtocolName()!= null) {
                strName = item.getProtocolName();
            }
        }
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            ProtocolVO item = (ProtocolVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                ProtocolVO vo = new ProtocolVO();
                vo.setTmstart(dateTime);
                vo.setDblbps(0);
                vo.setProtocolName(strName);
                list.add(vo);
            }
        }
        return list;
    }
    
    /**
     * dash board statep chart
     * @param chartList
     * @param startTime
     * @param endTime
     * @return 
     */
    public static List<StatEPVO> getDashboardStatEpChart(List<StatEPVO> chartList, String startTime, String endTime) {
        List<StatEPVO> list = new ArrayList<>();
        String format = "yy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        Map<String, Object> map = new HashMap<>();
        for (StatEPVO item : chartList) {
            map.put(item.getTmstart(), item);
        }
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            StatEPVO item = (StatEPVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                StatEPVO vo = new StatEPVO();
                vo.setTmstart(dateTime);
                vo.setEventDblbps(0);
                vo.setEventCount(0);
                list.add(vo);
            }
        }
        return list;
    }

    /**
     * 서비스 트래픽 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<TrafficServiceChartVO> getServiceChartDateTimeList(List<TrafficServiceChartVO> chartList, String startTime, String endTime) {
        List<TrafficServiceChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (TrafficServiceChartVO item : chartList) {
            map.put(item.getTime(), item);
            if (item != null && item.getStrName() != null) {
                strName = item.getStrName();
            }
        }
        int k = 0;
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            TrafficServiceChartVO item = (TrafficServiceChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                TrafficServiceChartVO vo = new TrafficServiceChartVO();
                vo.setTime(dateTime);
                vo.setData(0);
                vo.setDataIn(0);
                vo.setDataOut(0);
                vo.setStrName(strName);
                list.add(vo);
            }
        }
        return list;
    }

    /**
     * TCP Flag 트래픽 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<TcpFlagChartVO> getTcpflagChartDateTimeList(List<TcpFlagChartVO> chartList, String startTime, String endTime) {
        List<TcpFlagChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (TcpFlagChartVO item : chartList) {
            map.put(item.getTime(), item);
            if (item != null && item.getStrName() != null) {
                strName = item.getStrName();
            }
        }
        
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            TcpFlagChartVO item = (TcpFlagChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                TcpFlagChartVO vo = new TcpFlagChartVO();
                vo.setTime(dateTime);
                vo.setData(0);
                vo.setDataIn(0);
                vo.setDataOut(0);
                vo.setStrName(strName);
                list.add(vo);
            }
        }
        return list;
    }
    
    /**
     * FrameSize 트래픽 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<FrameSizeChartVO> getFramesizeChartDateTimeList(List<FrameSizeChartVO> chartList, String startTime, String endTime) {
        List<FrameSizeChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (FrameSizeChartVO item : chartList) {
            map.put(item.getTime(), item);
            if (item != null && item.getStrName() != null) {
                strName = item.getStrName();
            }
        }
        
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            FrameSizeChartVO item = (FrameSizeChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                FrameSizeChartVO vo = new FrameSizeChartVO();
                vo.setTime(dateTime);
                vo.setData(0);
                vo.setDataIn(0);
                vo.setDataOut(0);
                vo.setStrName(strName);
                list.add(vo);
            }
        }
        return list;
    }
    
    /**
     * IP 트래픽 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<IpChartVO> getIpChartDateTimeList(List<IpChartVO> chartList, String startTime, String endTime) {
        List<IpChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (IpChartVO item : chartList) {
            map.put(item.getTime(), item);
            if (item != null && item.getStrName() != null) {
                strName = item.getStrName();
            }
        }
        
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            IpChartVO item = (IpChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                IpChartVO vo = new IpChartVO();
                vo.setTime(dateTime);
                vo.setData(0);
                vo.setStrName(strName);
                list.add(vo);
            }
        }
        return list;
    }
    
    /**
     * Malicious 트래픽 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<MaliciousTrafficChartVO> getMailciousChartDateTimeList(List<MaliciousTrafficChartVO> chartList, String startTime, String endTime) {
        List<MaliciousTrafficChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        String strName = "";
        Map<String, Object> map = new HashMap<>();
        for (MaliciousTrafficChartVO item : chartList) {
            map.put(item.getTime(), item);
            if (item != null && item.getStrName() != null) {
                strName = item.getStrName();
            }
        }
        
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            MaliciousTrafficChartVO item = (MaliciousTrafficChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                MaliciousTrafficChartVO vo = new MaliciousTrafficChartVO();
                vo.setTime(dateTime);
                vo.setData(0);
                vo.setStrName(strName);
                list.add(vo);
            }
        }
        return list;
    }

    /**
     * 차트 시간 별 데이터 비교(공백 시간 추가)
     *
     * @param chartList
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<ChartVO> getChartDateTimeList(List<ChartVO> chartList, String startTime, String endTime) {
        List<ChartVO> list = new ArrayList<>();
        String format = "yyyy-MM-dd HH:mm";
        Date startDate = getStrToDate(startTime, format);
        Date endDate = getStrToDate(endTime, format);
        Long diffMin = getDiffDate(Calendar.MINUTE, startDate, endDate);
        Map<String, Object> map = new HashMap<>();
        Double avg = 0D;
        Double max = 0D;
        Double min = 0D;
        for (ChartVO item : chartList) {
            map.put(item.getTime(), item);
            avg = item.getAvgData();
            max = item.getMaxDData();
            min = item.getMinDData();
        }
        
        for (int i = 0; i < diffMin; i++) {
            String dateTime = getDateToStr(getChangeMinute(startDate, i), format);
            ChartVO item = (ChartVO) map.get(dateTime);
            if (item != null) {
                list.add(item);
            } else {
                ChartVO vo = new ChartVO();
                vo.setTime(dateTime);
                vo.setDdata(0D);
                vo.setDdataIn(0D);
                vo.setDdataOut(0D);
                vo.setAvgData(avg);
                vo.setMaxDData(max);
                vo.setMinDData(min);
                list.add(vo);
            }
        }
        return list;
    }
}
