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
import com.kglory.tms.web.mapper.detectionAnalysis.ApplicationMapper;
import com.kglory.tms.web.model.dto.ApplicationSearchDto;
import com.kglory.tms.web.model.vo.ApplicationVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.ProtocolEnum;
import com.kglory.tms.web.util.TableFinder;
import com.kglory.tms.web.util.TimeUtil;
import com.kglory.tms.web.util.packet.PacketAnalyzer;

@Service
public class ApplicationService {

    private static Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    PolicyMapper policyMapper;
    @Autowired
    OracleMapper oracleMapper;

    public List<ApplicationVO> selectApplicationLogList(ApplicationSearchDto dto) throws BaseException {
        List<ApplicationVO> result = null;
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("APPLAYER", startDate, endDate));
        dto.setTableNames(selectTables);

        if (dto.getToIpInput() == null || dto.getToIpInput().isEmpty()) {
            dto.setToIp(null);
            dto.setFromIp(null);
        } else {
            if (dto.getIpType() == null || dto.getIpType().equals(4L)) {
                dto.setToIp(IpUtil.getHostByteOrderIpToLong(dto.getToIpInput()));
                dto.setFromIp(IpUtil.getHostByteOrderIpToLong(dto.getFromIpInput()));
            } else {
                dto.setStrSourceIp(dto.getToIpInput());
                dto.setStrDestinationIp(dto.getFromIpInput());
            }
        }

        if (selectTables.size() > 0) {
            result = applicationMapper.selectApplicationLogList(dto);
            if (result != null && result.size() > 0) {
                result.get(0).setTotalRowSize(applicationMapper.selectApplicationLogListCount(dto));
                for(int i = 0 ; i < result.size() ; i++) {
                    if (result.get(i).getsData() != null && !result.get(i).getsData().isEmpty()) {
                        result.get(i).setsData(PacketAnalyzer.applicationHexToString(result.get(i).getsData(), Integer.valueOf(result.get(i).getwDataSize().intValue())));
                    }
                    result.get(i).setrNum(Long.valueOf(dto.getStartRowSize() + i + 1));
                }
            }
        }
        if (result == null) {
            result = new ArrayList<ApplicationVO>();
        }
        return result;
    }
    
    public ApplicationVO selectApplicationHelpPopupList(ApplicationSearchDto dto) throws BaseException {
        ApplicationVO resultList = new ApplicationVO();
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("APPLAYER", startDate, endDate));
        dto.setTableNames(selectTables);

        if (dto.getToIpInput() == null || dto.getToIpInput().isEmpty()) {
            dto.setToIp(null);
            dto.setFromIp(null);
        } else {
            dto.setToIp(IpUtil.getHostByteOrderIpToLong(dto.getToIpInput()));
            dto.setFromIp(IpUtil.getHostByteOrderIpToLong(dto.getFromIpInput()));
        }
        resultList = applicationMapper.selectApplicationHelpPopupList(dto);
        resultList.setsData(PacketAnalyzer.applicationHexToString(resultList.getsData(), Integer.valueOf(resultList.getwDataSize().intValue())));

        return resultList;
    }
}
