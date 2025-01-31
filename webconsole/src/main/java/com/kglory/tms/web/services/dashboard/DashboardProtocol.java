package com.kglory.tms.web.services.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.dashboard.DashboardMapper;
import com.kglory.tms.web.model.dto.ProtocolDto;
import com.kglory.tms.web.model.vo.ProtocolVO;
import com.kglory.tms.web.util.ChartUtil;
import com.kglory.tms.web.util.TableFinder;
import java.util.ArrayList;

@Service
public class DashboardProtocol {

    private static Logger logger = LoggerFactory.getLogger(DashboardProtocol.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    DashboardMapper dashboardMapper;
    @Autowired
    OracleMapper oracleMapper;

    public List<ProtocolVO> selectProtocolTraffic(@RequestBody ProtocolDto dto) throws BaseException {
        List<ProtocolVO> rtnList = new ArrayList<>();
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, ipType));
        dto.setTableNames(selectTables);

        rtnList = dashboardMapper.selectProtocolTraffic(dto);
        rtnList = ChartUtil.getDashboardProtocolChart(rtnList, startDate, endDate, "yy-MM-dd HH:mm");
        return rtnList;
    }

    public List<ProtocolVO> protocolsTraffic(@RequestBody ProtocolDto dto) throws BaseException{
        List<ProtocolVO> rtnList = new ArrayList<>();
        String startDate = dto.getTmstart();
        String endDate = dto.getTmend();
        Long ipType = dto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("PROTOCOL", startDate, endDate, ipType));
        dto.setTableNames(selectTables);

        List<ProtocolVO> protocolList = dashboardMapper.selectProtocolList(dto);
        if (protocolList != null && protocolList.size() > 0) {
            for (ProtocolVO item : protocolList) {
                dto.setnProtocol(item.getnProtocol());
                dto.setProtocolName(item.getProtocolName());
                List<ProtocolVO> trafficList = dashboardMapper.protocolsTraffic(dto);
                trafficList = ChartUtil.getDashboardProtocolChart(trafficList, startDate, endDate, "yyyy-MM-dd HH:mm");
                rtnList.addAll(trafficList);
                
                trafficList = new ArrayList<>();
            }
        }

        return rtnList;
    }
}
