package com.kglory.tms.web.services.trafficAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.controller.trafficAnalysis.MaliciousTrafficController;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.trafficAnalysis.MaliciousTrafficMapper;
import com.kglory.tms.web.model.dto.MaliciousTrafficDto;
import com.kglory.tms.web.model.vo.MaliciousTrafficChartVO;
import com.kglory.tms.web.model.vo.MaliciousTrafficVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;

@Service
public class MaliciousTrafficService {

    private static Logger logger = LoggerFactory.getLogger(MaliciousTrafficController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    MaliciousTrafficMapper maliciousTrafficMapper;
    @Autowired
    OracleMapper oracleMapper;

    /**
     * 5분 통계 데이터 조회
     *
     * @param dto
     * @return MaliciousTrafficVO resultData
     */
    public MaliciousTrafficVO selectMaliciousTrafficList(MaliciousTrafficDto dto) throws BaseException {

        MaliciousTrafficVO resultData = new MaliciousTrafficVO();
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();
        Long ipType = dto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, ipType));
        dto.setTableNames(selectTables);
        dto.setAvgTime(TableFinder.getStatisticsTime(dto.getStartDateInput(), dto.getEndDateInput()));

        resultData = maliciousTrafficMapper.selectMaliciousTrafficList(dto);

        return resultData;

    }

    /**
     * 유해트래픽 그래프 조회
     *
     * @param dto
     * @return List<MaliciousTrafficVO> chartList
     * @throws BaseException
     */
    public List<MaliciousTrafficChartVO> selectMaliciousTrafficChart(MaliciousTrafficDto dto) throws BaseException {

        List<MaliciousTrafficChartVO> chartList = null;

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType()));
        dto.setTableNames(selectTables);

        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (selectTables.size() > 0) {
            chartList = maliciousTrafficMapper.selectMaliciousTrafficChart(dto);
        } else {
            chartList = new ArrayList<MaliciousTrafficChartVO>();
        }
        if (chartList.size() > 0) {
            chartList = ChartUtil.getMailciousChartDateTimeList(chartList, dto.getStartDateInput(), dto.getEndDateInput());
        }

        return chartList;
    }

}
