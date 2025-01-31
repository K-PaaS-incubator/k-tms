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
import com.kglory.tms.web.model.dto.ApplicationSearchDto;
import com.kglory.tms.web.model.vo.ApplicationVO;
import com.kglory.tms.web.util.TableFinder;
import java.util.ArrayList;

@Service
public class DashboardApplication {

    private static Logger logger = LoggerFactory.getLogger(DashboardApplication.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    DashboardMapper dashboardMapper;
    @Autowired
    OracleMapper oracleMapper;

    public List<ApplicationVO> selectApplicationTopN(@RequestBody ApplicationSearchDto dto) throws BaseException {
        List<ApplicationVO> rtnList = new ArrayList<>();
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();
        Long ipType = dto.getIpType();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("APPLAYER", startDate, endDate, ipType));
        dto.setTableNames(selectTables);
        rtnList = dashboardMapper.selectApplicationTopN(dto);
        if(rtnList != null && rtnList.size() > 0) {
            Long total = dashboardMapper.selectApplicationTopNTotal(dto);
            for(int i = 0 ; i < rtnList.size() ; i++) {
                rtnList.get(i).setSumCount(total);
            }
        }
        return rtnList;
    }
}
