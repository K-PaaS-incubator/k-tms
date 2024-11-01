package com.kglory.tms.web.services.detectionAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.common.PolicyMapper;
import com.kglory.tms.web.mapper.detectionAnalysis.VictimIpMapper;
import com.kglory.tms.web.model.CommonBean.ReturnType;
import com.kglory.tms.web.model.dto.PolicyDto;
import com.kglory.tms.web.model.dto.VictimIpSearchDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.VictimIpChartVO;
import com.kglory.tms.web.model.vo.VictimIpVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.ProtocolEnum;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;

@Service
public class VictimIpService {

    private static Logger logger = LoggerFactory.getLogger(VictimIpService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    VictimIpMapper victimIpMapper;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    OracleMapper oracleMapper;

    public List<ChartVO> selectVictimIpAttackCountGraphData(VictimIpSearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("LOG", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));
        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (dto.getDestIp() != null && !dto.getDestIp().isEmpty()) {
            if (dto.getIpType().equals(4L)) {
                dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getDestIp()));
            } else {
                dto.setStrDestinationIp(dto.getDestIp());
            }
        }

        selectedList = victimIpMapper.selectVictimIpAttackCountGraphData(dto);
        ChartVO chart = victimIpMapper.selectVictimIpAttackCountGraphDataMinMax(dto);
        ChartVO chartSum = victimIpMapper.selectVictimIpAttackCountGraphDataAvg(dto);
        if (selectedList != null && selectedList.size() > 0) {
            selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            for(int i = 0 ; i < selectedList.size() ; i++) {
                selectedList.get(i).setMinDData(chart.getMinDData());
                selectedList.get(i).setMaxDData(chart.getMaxDData());
                selectedList.get(i).setSumData(chartSum.getSumData());
            }
        }

        return selectedList;
    }

    public List<ChartVO> selectVictimIpBpsGraphData(VictimIpSearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("LOG", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));
        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (dto.getDestIp() != null && !dto.getDestIp().isEmpty()) {
            if (dto.getIpType().equals(4L)) {
                dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getDestIp()));
            } else {
                dto.setStrDestinationIp(dto.getDestIp());
            }
        }

        selectedList = victimIpMapper.selectVictimIpBpsGraphData(dto);
        ChartVO chart = victimIpMapper.selectVictimIpBpsGraphDataMinMax(dto);
        ChartVO chartaVG = victimIpMapper.selectVictimIpBpsGraphDataAvg(dto);
        if (selectedList != null && selectedList.size() > 0) {
            selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            for(int i = 0 ; i < selectedList.size() ; i++) {
                selectedList.get(i).setMinDData(chart.getMinDData());
                selectedList.get(i).setMaxDData(chart.getMaxDData());
                selectedList.get(i).setAvgData(chartaVG.getAvgData());
            }
        }
        return selectedList;
    }

    public List<ChartVO> selectVictimIpPpsGraphData(VictimIpSearchDto dto) throws BaseException {
        List<ChartVO> selectedList = null;
        dto.setTableNames(oracleMapper.selectTables(TableFinder.getQueryTables("LOG", dto.getStartDateInput(), dto.getEndDateInput(), dto.getIpType())));
        //데이터 0처리 추가
        String chartTableUnit = TableFinder.getChartTableUnit(dto.getStartDateInput(), dto.getEndDateInput());
        dto.setTableUnit(chartTableUnit);

        Long chartTimeDiffSecond = TimeUtil.diffOfMinute(TimeUtil.parseDateTime(dto.getStartDateInput()).getTime(), TimeUtil.parseDateTime(dto.getEndDateInput()).getTime());
        dto.setTimeDiffSecond(chartTimeDiffSecond);

        if (dto.getDestIp() != null && !dto.getDestIp().isEmpty()) {
            if (dto.getIpType().equals(4L)) {
                dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getDestIp()));
            } else {
                dto.setStrDestinationIp(dto.getDestIp());
            }
        }

        selectedList = victimIpMapper.selectVictimIpPpsGraphData(dto);
        ChartVO chart = victimIpMapper.selectVictimIpPpsGraphDataMinMax(dto);
        ChartVO chartaVG = victimIpMapper.selectVictimIpPpsGraphDataAvg(dto);
        if (selectedList != null && selectedList.size() > 0) {
            selectedList = ChartUtil.getChartDateTimeList(selectedList, dto.getStartDateInput(), dto.getEndDateInput());
            for(int i = 0 ; i < selectedList.size() ; i++) {
                selectedList.get(i).setMinDData(chart.getMinDData());
                selectedList.get(i).setMaxDData(chart.getMaxDData());
                selectedList.get(i).setAvgData(chartaVG.getAvgData());
            }
        }

        return selectedList;
    }
}
