package com.kglory.tms.web.services.monitor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.monitor.IntrusionDetectionMapper;
import com.kglory.tms.web.model.dto.DetectionMonitorSearchDto;
import com.kglory.tms.web.model.vo.DetectionEventVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.TableFinder;

@Service
public class IntrusionDetectionService {
	
	private static Logger logger= LoggerFactory.getLogger(IntrusionDetectionService.class);
	
	@Autowired
	IntrusionDetectionMapper	intrusionDetectionMapper;
	@Autowired
	OracleMapper				oracleMapper;
	
	public List<DetectionEventVO> selectDetectionEvent(DetectionMonitorSearchDto dto) throws BaseException {
		dto.setQueryTableName(TableFinder.getCurrentQueryTables("LOG", dto.getIpType()));
		dto.setQuerySubTableName(TableFinder.getCurrentQueryTables("RAWDATA", dto.getIpType()));

		// 위험도
		ArrayList<Integer> severities = new ArrayList<Integer>();
		severities = getSeverity(dto);
		dto.setSeverities(severities);

		// SourceIP
		if (dto.getStrSrcIp() == null || dto.getStrSrcIp().equals("")) {
			dto.setSourceIp(null);
			dto.setStrSourceIp(null);
		} else {
			if (dto.getIpType().equals(4L)) {
				try {
					dto.setSourceIp(IpUtil.getHostByteOrderIpToLong(dto.getStrSrcIp()));
				} catch (NumberFormatException ex) {
					dto.setSourceIp(-1L);
					dto.setStrSourceIp(null);
				}
			} else {
				dto.setStrSourceIp(dto.getStrSrcIp());
			}
		}

		// DestnationIP
		if (dto.getStrDestIp() == null || dto.getStrDestIp().equals("")) {
			dto.setDestinationIp(null);
			dto.setStrDestinationIp(null);
		} else {
			if (dto.getIpType().equals(4L)) {
				try {
					dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getStrDestIp()));
				} catch (NumberFormatException ex) {
					dto.setDestinationIp(-1L);
					dto.setStrDestinationIp(null);
				}
			} else {
				dto.setStrDestinationIp(dto.getStrDestIp());
			}
		}

		return intrusionDetectionMapper.selectDetectionEvent(dto);
	}

	// 공통 함수
	public ArrayList<Integer> getSeverity(DetectionMonitorSearchDto dto) throws BaseException {
		// 위험도가 true 이면, 위험도 값 세팅
		ArrayList<Integer> severities = new ArrayList<Integer>();

		if (dto.getSeverityHCheck() != null && dto.getSeverityHCheck()) {
			severities.add(new Integer(Constants.SEVERITY_HIGH));
		}
		if (dto.getSeverityMCheck() != null && dto.getSeverityMCheck()) {
			severities.add(new Integer(Constants.SEVERITY_MEDIUM));
		}
		if (dto.getSeverityLCheck() != null && dto.getSeverityLCheck()) {
			severities.add(new Integer(Constants.SEVERITY_LOW));
		}
		if (dto.getSeverityICheck() != null && dto.getSeverityICheck()) {
			severities.add(new Integer(Constants.SEVERITY_INFO));
		}

		return severities;
	}
}
