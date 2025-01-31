package com.kglory.tms.web.mapper.detectionAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.ApplicationSearchDto;
import com.kglory.tms.web.model.vo.ApplicationVO;

public interface ApplicationMapper {
	
	// 어플리케이션 원본로그 검색/조회 
	@Transactional(readOnly = true)
	List<ApplicationVO> selectApplicationLogList(ApplicationSearchDto dto);
        
	// 어플리케이션 원본로그 검색/조회(total row size)
	@Transactional(readOnly = true)
	Long selectApplicationLogListCount(ApplicationSearchDto dto);
        
	// 어플리케이션 원본로그 도움말 팝업 검색/조회
	@Transactional(readOnly = true)
	ApplicationVO selectApplicationHelpPopupList(ApplicationSearchDto dto);
}
