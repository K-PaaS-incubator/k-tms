package com.kglory.tms.web.services.trafficAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.controller.trafficAnalysis.ProtocolController;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.PolicyMapper;
import com.kglory.tms.web.mapper.trafficAnalysis.ProtocolMapper;
import com.kglory.tms.web.model.dto.TrafficProtocolDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;
import java.math.BigInteger;

@Service
public class ProtocolService {

    private static Logger logger = LoggerFactory.getLogger(ProtocolController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    ProtocolMapper protocolMapper;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    OracleMapper oracleMapper;

    /**
     * 프로토콜 5분 통계 조회
     *
     * @param trafficProtocolDto
     * @return List<TrafficProtocolVO> resultList
     * @throws BaseException
     */
    public List<TrafficProtocolVO> selectProtocolList(TrafficProtocolDto trafficProtocolDto) throws BaseException {

        String startDate = trafficProtocolDto.getStartDateInput();
        String endDate = trafficProtocolDto.getEndDateInput();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, trafficProtocolDto.getIpType()));
        trafficProtocolDto.setTableNames(selectTables);
        trafficProtocolDto.setAvgTime(TableFinder.getStatisticsTime(trafficProtocolDto.getStartDateInput(), trafficProtocolDto.getEndDateInput()));

        List<TrafficProtocolVO> resultList = protocolMapper.selectProtocolList(trafficProtocolDto);
        if (resultList != null && resultList.size() > 0 && resultList.get(0) != null && (resultList.get(0).getnProtocol() == null || resultList.get(0).getnProtocol().isEmpty())) {
            resultList = new ArrayList<>();
        } else {
            for (int i  = 0 ; i < resultList.size() ; i++) {
                resultList.get(i).setrNum(BigInteger.valueOf(i + 1));
                resultList.get(i).setTotalRowSize(resultList.size());
            }
        }

        return resultList;
    }

//	public List<TrafficProtocolVO> selectProtocolVariationList(TrafficProtocolDto trafficProtocolDto)  throws BaseException{
//		
//		String startDate = trafficProtocolDto.getStartDateInput();
//		String endDate = trafficProtocolDto.getEndDateInput();
//		
//		List<String> selectTables = oracleMapper.selectTables(TableFinder.getStatisticsTables("PROTOCOL", startDate, endDate));
//		trafficProtocolDto.setTableNames(selectTables);
//		
//		List<String> selectBeforeTables = oracleMapper.selectTables(TableFinder.getStatisticsTables("PROTOCOL", trafficProtocolDto.getStartDateBeforeInput(),
//				trafficProtocolDto.getEndDateBeforeInput()));
//		trafficProtocolDto.setTableBeforeNames(selectBeforeTables);
//		
//		List<TrafficProtocolVO> resultList = protocolMapper.selectProtocolVariationList(trafficProtocolDto);
//		
//		return resultList;
//	}
    /**
     * 프로토콜 5분 통계 TOTAL 조회
     *
     * @param trafficProtocolDto
     * @return TrafficProtocolVO resultData
     * @throws BaseException
     */
    public TrafficProtocolVO selectProtocolTotal(TrafficProtocolDto trafficProtocolDto) throws BaseException {

        TrafficProtocolVO resultData = new TrafficProtocolVO();

        String startDate = trafficProtocolDto.getStartDateInput();
        String endDate = trafficProtocolDto.getEndDateInput();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, trafficProtocolDto.getIpType()));
        trafficProtocolDto.setTableNames(selectTables);
        trafficProtocolDto.setAvgTime(TableFinder.getStatisticsTime(trafficProtocolDto.getStartDateInput(), trafficProtocolDto.getEndDateInput()));

        resultData = protocolMapper.selectProtocolTotal(trafficProtocolDto);

        return resultData;
    }

//	public TrafficProtocolVO selectProtocolTotalVariation(TrafficProtocolDto trafficProtocolDto) throws BaseException {
//
//		TrafficProtocolVO resultData = new TrafficProtocolVO();
//		
//		String startDate = trafficProtocolDto.getStartDateInput();
//		String endDate = trafficProtocolDto.getEndDateInput();
//		
//		List<String> selectTables = oracleMapper.selectTables(TableFinder.getStatisticsTables("PROTOCOL", startDate, endDate));
//		trafficProtocolDto.setTableNames(selectTables);
//		
//		List<String> selectBeforeTables = oracleMapper.selectTables(TableFinder.getStatisticsTables("PROTOCOL", trafficProtocolDto.getStartDateBeforeInput(),
//				trafficProtocolDto.getEndDateBeforeInput()));
//		trafficProtocolDto.setTableBeforeNames(selectBeforeTables);
//		
//		resultData = protocolMapper.selectProtocolTotalVariation(trafficProtocolDto);
//		
//		return resultData;
//	}
    /**
     * 프로토콜 그래프 조회
     *
     * @param trafficProtocolDto
     * @return List<TrafficProtocolChartVO> chartList
     */
    public List<TrafficProtocolChartVO> selectTrafficProtocolChart(TrafficProtocolDto trafficProtocolDto)throws BaseException {

        List<TrafficProtocolChartVO> chartList = null;

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", trafficProtocolDto.getStartDateInput(), trafficProtocolDto.getEndDateInput(), trafficProtocolDto.getIpType()));
        trafficProtocolDto.setTableNames(selectTables);

        Integer winbound = trafficProtocolDto.getWinBoundSelect();
        if (trafficProtocolDto.getGraphItem() != null) {
            trafficProtocolDto.setProtocolName(TrafficProtocolDto.getTrafficProtocolName(Long.valueOf(trafficProtocolDto.getGraphItem()).intValue()));
        }

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(trafficProtocolDto.getStartDateInput(), trafficProtocolDto.getEndDateInput());
        trafficProtocolDto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(trafficProtocolDto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(trafficProtocolDto.getEndDateInput()).getTime());
        trafficProtocolDto.setTimeDiffSecond(chartTimeDiffSecond);

        if (selectTables.size() > 0) {
            if (winbound == 1) { //양방향
                chartList = protocolMapper.selectTrafficProtocolInOutChart(trafficProtocolDto);
            } else { //힙계, In, Out
                chartList = protocolMapper.selectTrafficProtocolChart(trafficProtocolDto);
            }
            chartList = ChartUtil.getProtocolChartDateTimeList(chartList, trafficProtocolDto.getStartDateInput(), trafficProtocolDto.getEndDateInput());
        } else {
            chartList = new ArrayList<TrafficProtocolChartVO>();
        }

        return chartList;
    }

    /**
     * 프로토콜 트래픽 bps 그래프 조회
     *
     * @param dto
     * @return List<ChartVO> selectedList
     * @throws BaseException
     */
    public List<ChartVO> selectTrafficAnalysisBpsPopupGraphData(TrafficProtocolDto dto) throws BaseException {

        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

        Integer winbound = dto.getWinBoundSelect();

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);
        

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (winbound == 1) { //양방향
            selectedList = protocolMapper.selectTrafficAnalysisBpsPopupInOutGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = protocolMapper.selectTrafficAnalysisBpsPopupInOutGraphDataMinMax(dto);
                ChartVO chartAvg = protocolMapper.selectTrafficAnalysisBpsPopupInOutGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        } else {
            selectedList = protocolMapper.selectTrafficAnalysisBpsPopupGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = protocolMapper.selectTrafficAnalysisBpsPopupGraphDataMinMax(dto);
                ChartVO chartAvg = protocolMapper.selectTrafficAnalysisBpsPopupGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        }
        if (selectedList != null && selectedList.size() > 0) {
            selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
        }
        return selectedList;
    }

    /**
     * 프로토콜 트래픽 pps 그래프 조회
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public List<ChartVO> selectTrafficAnalysisPpsPopupGraphData(TrafficProtocolDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));

        Integer winbound = dto.getWinBoundSelect();

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);
        
        dto.setAvgTime(TableFinder.getStatisticsTime(dto.getStartDateInput(), dto.getEndDateInput()));

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (winbound == 1) { //양방향
            selectedList = protocolMapper.selectTrafficAnalysisPpsPopupInOutGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = protocolMapper.selectTrafficAnalysisPpsPopupInOutGraphDataMinMax(dto);
                ChartVO chartAvg = protocolMapper.selectTrafficAnalysisPpsPopupInOutGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        } else {
            selectedList = protocolMapper.selectTrafficAnalysisPpsPopupGraphData(dto);
            if (selectedList != null && selectedList.size() > 0) {
                ChartVO chart = protocolMapper.selectTrafficAnalysisPpsPopupGraphDataMinMax(dto);
                ChartVO chartAvg = protocolMapper.selectTrafficAnalysisPpsPopupGraphDataAvg(dto);
                setMinMaxAvgData(selectedList, chart, chartAvg);
            }
        }
        if (selectedList != null && selectedList.size() > 0) {
            selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
        }
        return selectedList;
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
