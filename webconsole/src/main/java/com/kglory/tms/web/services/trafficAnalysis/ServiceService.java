package com.kglory.tms.web.services.trafficAnalysis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.PolicyMapper;
import com.kglory.tms.web.mapper.trafficAnalysis.ServiceMapper;
import com.kglory.tms.web.model.dto.TrafficServiceDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.math.BigInteger;

@Service
public class ServiceService {

    private static Logger logger = LoggerFactory.getLogger(ServiceService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    OracleMapper oracleMapper;
    @Autowired
    ServiceMapper serviceMapper;

    /**
     * 서비스 5분 통계 조회
     *
     * @param trafficServiceDto
     * @return List<TrafficServiceVO> resultList
     */
    public List<TrafficServiceVO> selectServiceList(TrafficServiceDto trafficServiceDto) throws BaseException {

        List<TrafficServiceVO> resultList = null;

        String startDate = trafficServiceDto.getStartDateInput();
        String endDate = trafficServiceDto.getEndDateInput();
        Long ipType = trafficServiceDto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("SERVICE", startDate, endDate, ipType));
        trafficServiceDto.setTableNames(selectTables);
        trafficServiceDto.setAvgTime(TableFinder.getStatisticsTime(trafficServiceDto.getStartDateInput(), trafficServiceDto.getEndDateInput()));

        // 임계치 값이 없을 경우
        if (trafficServiceDto.getThresholdSelect() == null || trafficServiceDto.getThresholdSelect().isEmpty()) {
            trafficServiceDto.setThresholdNumInput(0L);
            trafficServiceDto.setThresholdSelect(null);
        }
        resultList = serviceMapper.selectServiceList(trafficServiceDto);
        
        if (resultList != null && resultList.size() > 0) {
            Long totalCount = serviceMapper.selectServiceListTotal(trafficServiceDto);
            for(int i = 0 ; i < resultList.size() ; i++) {
                resultList.get(i).setrNum(BigInteger.valueOf(trafficServiceDto.getStartRowSize() + i + 1));
                resultList.get(i).setTotalRowSize(totalCount);
            }
        }
        return resultList;

    }

    /**
     * 서비스 TOTAL 조회
     *
     * @param trafficServiceDto
     * @return TrafficServiceVO resultData
     */
    public TrafficServiceVO selectServiceTotal(TrafficServiceDto trafficServiceDto) throws BaseException {

        TrafficServiceVO resultData = new TrafficServiceVO();

        String startDate = trafficServiceDto.getStartDateInput();
        String endDate = trafficServiceDto.getEndDateInput();
        Long ipType = trafficServiceDto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("SERVICE", startDate, endDate, ipType));
        trafficServiceDto.setTableNames(selectTables);
        trafficServiceDto.setAvgTime(TableFinder.getStatisticsTime(trafficServiceDto.getStartDateInput(), trafficServiceDto.getEndDateInput()));

        resultData = serviceMapper.selectServiceTotal(trafficServiceDto);


        return resultData;
    }

    /**
     * 서비스 그래프 조회
     *
     * @param trafficServiceDto
     * @return List<TrafficServiceChartVO> chartList
     */
    public List<TrafficServiceChartVO> selectServiceChart(TrafficServiceDto trafficServiceDto) throws BaseException {

        List<TrafficServiceChartVO> chartList = null;
        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("SERVICE", trafficServiceDto.getStartDateInput(), trafficServiceDto.getEndDateInput(), trafficServiceDto.getIpType()));
        trafficServiceDto.setTableNames(selectTables);

        Integer winbound = trafficServiceDto.getWinBoundSelect();

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(trafficServiceDto.getStartDateInput(), trafficServiceDto.getEndDateInput());
        trafficServiceDto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(trafficServiceDto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(trafficServiceDto.getEndDateInput()).getTime());
        trafficServiceDto.setTimeDiffSecond(chartTimeDiffSecond);

        if (selectTables.size() > 0) {
            if (winbound == 1) { //양방향
                chartList = serviceMapper.selectServiceInOutChart(trafficServiceDto);
            } else {
                chartList = serviceMapper.selectServiceChart(trafficServiceDto);
            }
        } else {
            chartList = new ArrayList<TrafficServiceChartVO>();
        }
        if (chartList.size() > 0) {
            chartList = ChartUtil.getServiceChartDateTimeList(chartList, trafficServiceDto.getStartDateInput(), trafficServiceDto.getEndDateInput());
        }


        return chartList;
    }

    /**
     * 서비스 트래픽 bps 그래프 조회
     *
     * @param dto
     * @return List<ChartVO> selectedList
     * @throws BaseException
     */
    public List<ChartVO> selectServiceTrendBpsGraphData(TrafficServiceDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("SERVICE", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

        Integer winbound = dto.getWinBoundSelect();

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (winbound == 1) { //양방향
            selectedList = serviceMapper.selectServiceTrendBpsInOutGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            }
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = serviceMapper.selectServiceTrendBpsInOutGraphDataMinMax(dto);
                ChartVO chartAvg = serviceMapper.selectServiceTrendBpsInOutGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        } else {
            selectedList = serviceMapper.selectServiceTrendBpsGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            }
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = serviceMapper.selectServiceTrendBpsGraphDataMinMax(dto);
                ChartVO chartAvg = serviceMapper.selectServiceTrendBpsGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        }
        return selectedList;
    }

    /**
     * 서비스 트래픽 pps 그래프 조회
     *
     * @param dto
     * @return List<ChartVO> selectedList
     * @throws BaseException
     */
    public List<ChartVO> selectServiceTrendPpsGraphData(TrafficServiceDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("SERVICE", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

        Integer winbound = dto.getWinBoundSelect();

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (winbound == 1) { //양방향
            selectedList = serviceMapper.selectServiceTrendPpsInOutGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            }
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = serviceMapper.selectServiceTrendPpsInOutGraphDataMinMax(dto);
                ChartVO chartAvg = serviceMapper.selectServiceTrendPpsInOutGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        } else {
            selectedList = serviceMapper.selectServiceTrendPpsGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            }
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = serviceMapper.selectServiceTrendPpsGraphDataMinMax(dto);
                ChartVO chartAvg = serviceMapper.selectServiceTrendPpsGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        }

        return selectedList;
    }

    /**
     *
     * @param dto
     * @return List<TrafficServiceVO> resultList
     */
    public List<TrafficServiceVO> selectProtocolIpTrafficList(TrafficServiceDto dto) throws BaseException {
        List<TrafficServiceVO> resultList = null;

        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();

        String ip = "";
        if (dto.getIpType().equals(4L)) {
            ip = "";
        } else {
            ip = "_V6";
        }
        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("IP_TRAFFIC" + ip, startDate, endDate));
        dto.setTableNames(selectTables);

        long timeDifference = timeDifference(startDate, endDate);
        dto.setTimeDifference(timeDifference);

        resultList = serviceMapper.selectProtocolIpTrafficList(dto);
        Long totCount = serviceMapper.selectProtocolIpTrafficListTotalCount(dto);
        if (resultList != null && resultList.size() > 0) {
            for(int i = 0 ; i < resultList.size() ; i ++) {
                resultList.get(i).setrNum(BigInteger.valueOf(dto.getStartRowSize() + i + 1));
                resultList.get(i).setTotalRowSize(totCount);
            }
        }

        return resultList;

    }

    private long timeDifference(String startDate, String endDate) throws BaseException {
        long min = 0;
        try {
            //시간 설정
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date startday = simpleDateFormat.parse(startDate);

            long startTime = startday.getTime();

            Date endDay = simpleDateFormat.parse(endDate);

            long endTime = endDay.getTime();
            long mills = endTime - startTime;

            //분으로 변환
            min = mills / 60000;

        } catch (ParseException e) {
            logger.error("(errorCode)" + e.getLocalizedMessage());
            throw new BaseException(messageSource, "errorCode", null, "", e);
        } catch (Exception e) {
            logger.error("(errorCode)" + e.getLocalizedMessage());
            throw new BaseException(messageSource, "errorCode", null, "", e);
        }
        return min;

    }
    
    public void setMinMaxAvgData(List<ChartVO> list, ChartVO chart, ChartVO chartAvg) {
        if (list != null && list.size() > 0) {
            for (int i = 0 ; i < list.size() ; i++) {
                list.get(i).setAvgData(chartAvg.getAvgData());
                list.get(i).setMaxDData(chart.getMaxDData());
                list.get(i).setMinDData(chart.getMinDData());
            }
        }
    }
}
