package com.kglory.tms.web.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TableFinder {

    public static final String KEY_5M = "5M";
    public static final String KEY_1H = "1H";
    public static final String KEY_1D = "1D";

    /**
     * 통계 테이블 구하는 API
     *
     * @param prefix 테이블 특성 키워드. 예) 프로토콜 트래픽 검색할 경우: PROTOCOL
     * @param startDate 검색 시작 시간
     * @param endDate 검색 종료 시간
     * @return 질의할 테이블 리스트
     */
    public static List<String> getStatisticsTables(String prefix, Calendar startDate, Calendar endDate, Long ipType) {
        ArrayList<String> statsTables = new ArrayList<String>();

        if (startDate != null && endDate != null) {
            long differenceOfDate = TimeUtil.diffOfDate(startDate.getTime(), endDate.getTime());

            String ip = "";
            if (ipType == null || ipType == 4) {
                ip = "";
            } else {
                ip = "_V6";
            }
            if (differenceOfDate >= 15) {
                // 1D_yy_mm
                SimpleDateFormat formatter = new SimpleDateFormat("_yy_MM");
                for (int i = 0; i <= differenceOfDate; i++) {
                    String tableName = prefix + KEY_1D + ip + formatter.format(startDate.getTime());
                    if (!statsTables.contains(tableName)) {
                        statsTables.add(tableName);
                    }
                    startDate.add(Calendar.DATE, 1);
                }
            } else if (3 <= differenceOfDate && differenceOfDate < 15) {
                // 1H_yy_mm_dd
                SimpleDateFormat formatter = new SimpleDateFormat("_yy_MM_dd");
                for (int i = 0; i <= differenceOfDate; i++) {
                    statsTables.add(prefix + KEY_1H + ip + formatter.format(startDate.getTime()));
                    startDate.add(Calendar.DATE, 1);
                }
            } else { // Less than 3 days
                // 5M_yy_mm_dd
                SimpleDateFormat formatter = new SimpleDateFormat("_yy_MM_dd");
                for (int i = 0; i <= differenceOfDate; i++) {
                    statsTables.add(prefix + KEY_5M + ip + formatter.format(startDate.getTime()));
                    startDate.add(Calendar.DATE, 1);
                }

            }
        }

        return statsTables;
    }

    public static List<String> getStatisticsTables(String prefix, String strStartDate, String strEndDate, Long ipType) {
        return getStatisticsTables(prefix, TimeUtil.parseDateTime(strStartDate), TimeUtil.parseDateTime(strEndDate), ipType);
    }

    /**
     * 날짜별 테이블을 구하는 API
     *
     * @param prefix 테이블 특성 키워드. 예) 프로토콜 트래픽 검색할 경우: PROTOCOL
     * @param startDate 검색 시작 시간
     * @param endDate 검색 종료 시간
     * @return 질의할 테이블 리스트
     */
    public static List<String> getQueryTables(String prefix, Calendar startDate, Calendar endDate) {
        ArrayList<String> queryTables = new ArrayList<String>();

        long differenceOfDate = TimeUtil.diffOfDate(startDate.getTime(), endDate.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("_yy_MM_dd");
        for (int i = 0; i <= differenceOfDate; i++) {
            queryTables.add(prefix + formatter.format(startDate.getTime()));
            startDate.add(Calendar.DATE, 1);
        }

        return queryTables;
    }

    public static List<String> getQueryTables(String prefix, String strStartDate, String strEndDate) {
        return getQueryTables(prefix, TimeUtil.parseDateTime(strStartDate), TimeUtil.parseDateTime(strEndDate));
    }

    public static List<String> getQueryTables(String prefix, String strStartDate, String strEndDate, Long ip) {
        String ipTable;
        if (ip == null || ip.equals(4L)) {
            ipTable = "";
        } else {
            ipTable = "_V6";
        }
        return getQueryTables(prefix + ipTable, TimeUtil.parseDateTime(strStartDate), TimeUtil.parseDateTime(strEndDate));
    }

    /**
     * 현재 날짜 테이블을 구하는 API
     *
     * @param prefix 테이블 특성 키워드.
     * @return 질의할 테이블 리스트
     */
    public static String getCurrentQueryTables(String prefix, Long ipType) {
        String ip = "";
        if (ipType == null || ipType == 4) {
            ip = "";
        } else {
            ip = "_V6";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("_yy_MM_dd");
        return prefix + ip + formatter.format(Calendar.getInstance().getTime());
    }

    /**
     * 현재 날짜 통계 테이블을 구하는 API
     *
     * @param prefix 통계 테이블 특성 키워드.
     * @return 질의할 통계 테이블 리스트 사용하지 않음
     */
    public static String getCurrentStatisticsTables(String prefix, String type) {
        SimpleDateFormat formatter = new SimpleDateFormat("_yy_MM_dd");
        String tableName = null;
        if (type == null) {
            type = "";
        }
        switch (type) {
            case KEY_5M:
                tableName = prefix + KEY_5M + formatter.format(Calendar.getInstance().getTime());
                break;
            case KEY_1H:
                tableName = prefix + KEY_5M + formatter.format(Calendar.getInstance().getTime());
                break;
            case KEY_1D:
                tableName = prefix + KEY_5M + formatter.format(Calendar.getInstance().getTime());
                break;
            default:
                tableName = prefix + formatter.format(Calendar.getInstance().getTime());
                break;
        }
        return tableName;
    }

    //평균 트래픽 구하기 위해서는 시간값을 나누어야 하는데, 그 시간값을 통계 테이블 종류에 따라 달리 함.
    public static Long getStatisticsTime(Calendar startDate, Calendar endDate) {
        long statsTime = 1; //return 값
        long mTime = 1; //5M, 1H, 1D 에 따라 나누는 값이 달라짐

        if (startDate != null && endDate != null) {
            long differenceOfDate = TimeUtil.diffOfDate(startDate.getTime(), endDate.getTime());
            long differenceOfTime = 1;

            if (differenceOfDate >= 15) {
                mTime = 60 * 60 * 24;
                differenceOfTime = TimeUtil.diffOfDay(startDate.getTime(), endDate.getTime());
            } else if (3 <= differenceOfDate && differenceOfDate < 15) {
                mTime = 60 * 60;
                differenceOfTime = TimeUtil.diffOfHour(startDate.getTime(), endDate.getTime());
            } else { // Less than 3 days
                mTime = 60;
                differenceOfTime = TimeUtil.diffOfSecond(startDate.getTime(), endDate.getTime());
            }

            statsTime = differenceOfTime / mTime;
        }
        return statsTime;
    }

    public static Long getStatisticsTime(String strStartDate, String strEndDate) {
        return getStatisticsTime(TimeUtil.parseDateTime(strStartDate), TimeUtil.parseDateTime(strEndDate));
    }
    
    public static String getChartTableUnit(Calendar startDate, Calendar endDate) {
        String tableUnit = "";

        if (startDate != null && endDate != null) {
            Long differenceOfDate = TimeUtil.diffOfDate(startDate.getTime(), endDate.getTime());
            if (differenceOfDate >= 15) {
                tableUnit = "1D";
            } else if (3 <= differenceOfDate && differenceOfDate < 15) {
                tableUnit = "1H";
            } else { // Less than 3 days
                tableUnit = "5M";
            }
        }
        return tableUnit;
    }

    public static String getChartTableUnit(String strStartDate, String strEndDate) {
        return getChartTableUnit(TimeUtil.parseDateTime(strStartDate), TimeUtil.parseDateTime(strEndDate));
    }
}
