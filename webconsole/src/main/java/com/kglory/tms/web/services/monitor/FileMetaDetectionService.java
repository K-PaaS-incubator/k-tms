package com.kglory.tms.web.services.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.monitor.FileMetaDetectionMapper;
import com.kglory.tms.web.model.dto.FileMetaMonitorSearchDto;
import com.kglory.tms.web.model.vo.FileMetaDetectionEventVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.TableFinder;

@Service
public class FileMetaDetectionService {

	@Autowired
	FileMetaDetectionMapper 	fileMetaDetectionMapper;
	@Autowired
	OracleMapper				oracleMapper;
	
	public List<FileMetaDetectionEventVO> selectFileMetaDetectionEvent(FileMetaMonitorSearchDto dto) throws BaseException {

		dto.setQueryTableName(TableFinder.getCurrentQueryTables("FILEMETA", null));

		// SourceIP
		if (dto.getStrSrcIp() == null || dto.getStrSrcIp().equals("")) {
			dto.setSourceIp(null);
		} else {
			if (dto.getIpType().equals(4L)) {
				try {
					dto.setSourceIp(IpUtil.getHostByteOrderIpToLong(dto.getStrSrcIp()));
				} catch (NumberFormatException e) {
					dto.setSourceIp(null);
				}
			} else {
				dto.setStrSourceIp(dto.getStrSrcIp());
			}
		}

		// DestnationIP
		if (dto.getStrDestIp() == null || dto.getStrDestIp().equals("")) {
			dto.setDestinationIp(null);
		} else {
			if (dto.getIpType().equals(4L)) {
				try {
					dto.setDestinationIp(IpUtil.getHostByteOrderIpToLong(dto.getStrDestIp()));
				} catch (NumberFormatException e) {
					dto.setDestinationIp(null);
				}
			} else {
				dto.setStrDestinationIp(dto.getStrDestIp());
			}
		}

		return fileMetaDetectionMapper.selectFileMetaDetectionEvent(dto);
	}
	
}
