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
import com.kglory.tms.web.mapper.detectionAnalysis.FileMetaMapper;
import com.kglory.tms.web.model.dto.FileMetaSearchDto;
import com.kglory.tms.web.model.vo.FileMetaVO;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.TableFinder;

@Service
public class FileMetaService {

    private static Logger logger = LoggerFactory.getLogger(FileMetaService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    FileMetaMapper fileMetaMapper;
    @Autowired
    OracleMapper oracleMapper;

	public List<FileMetaVO> selectFileMetaList(FileMetaSearchDto dto) throws BaseException {
		List<FileMetaVO> result = null;
		String startDate = dto.getStartDateInput();
		String endDate = dto.getEndDateInput();

		List<String> selectTables = oracleMapper
				.selectTables(TableFinder.getQueryTables("FILEMETA", startDate, endDate));
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
			result = fileMetaMapper.selectFileMetaList(dto);
			if (result != null && result.size() > 0) {
				result.get(0).setTotalRowSize(fileMetaMapper.selectFileMetaListCount(dto));
				for (int i = 0; i < result.size(); i++) {
					result.get(i).setrNum(Long.valueOf(dto.getStartRowSize() + i + 1));
				}
			}
		} else {
			result = new ArrayList<FileMetaVO>();
		}
		return result;
    }

	public FileMetaVO selectFileMetaHelpPopupList(FileMetaSearchDto dto) throws BaseException {
		FileMetaVO resultList = new FileMetaVO();
		String startDate = dto.getStartDateInput();
		String endDate = dto.getEndDateInput();

		List<String> selectTables = oracleMapper
				.selectTables(TableFinder.getQueryTables("FILEMETA", startDate, endDate));
		dto.setTableNames(selectTables);

		if (dto.getToIpInput() == null || dto.getToIpInput().isEmpty()) {
			dto.setToIp(null);
			dto.setFromIp(null);
		} else {
			dto.setToIp(IpUtil.getHostByteOrderIpToLong(dto.getToIpInput()));
			dto.setFromIp(IpUtil.getHostByteOrderIpToLong(dto.getFromIpInput()));
		}
		resultList = fileMetaMapper.selectFileMetaHelpPopupList(dto);

		return resultList;
	}
}
