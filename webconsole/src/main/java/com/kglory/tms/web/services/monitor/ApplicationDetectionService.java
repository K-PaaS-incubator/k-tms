package com.kglory.tms.web.services.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.monitor.ApplicationDetectionMapper;
import com.kglory.tms.web.model.dto.ApplicationMonitorSearchDto;
import com.kglory.tms.web.model.vo.ApplicationDetectionEventVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.TableFinder;

@Service
public class ApplicationDetectionService {

	@Autowired
	ApplicationDetectionMapper 	applicationDetectionMapper;
	@Autowired
	OracleMapper				oracleMapper;
	
	public List<ApplicationDetectionEventVO> selectApplicationDetectionEvent(ApplicationMonitorSearchDto dto) throws BaseException {
		
		dto.setQueryTableName(TableFinder.getCurrentQueryTables("APPLAYER", null));
		
		//SourceIP
		if (dto.getStrSrcIp() == null || dto.getStrSrcIp().equals("")) {
			dto.setSourceIp(null);
            dto.setStrSourceIp(null);
		} else {
            if (dto.getIpType().equals(4L)) {
                try {
                    dto.setSourceIp(IpUtil.getHostByteOrderIpToLong(dto.getStrSrcIp()));
                } catch(NumberFormatException e) {
                    dto.setSourceIp(null);
                }
            } else {
                dto.setStrSourceIp(dto.getStrSrcIp());
            }
		}
		
		//DestnationIP
		if (dto.getStrDestIp() == null || dto.getStrDestIp().equals("")) {
			dto.setDeDestinationIp(null);
            dto.setStrDestinationIp(null);
		} else {
            if (dto.getIpType().equals(4L)) {
                try {
                    dto.setDeDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getStrDestIp()));
                } catch(NumberFormatException e) {
                    dto.setDeDestinationIp(null);
                }
            } else {
                dto.setStrDestinationIp(dto.getStrDestIp());
            }
		} 
		
		return applicationDetectionMapper.selectApplicationDetectionEvent(dto);
	}
}
