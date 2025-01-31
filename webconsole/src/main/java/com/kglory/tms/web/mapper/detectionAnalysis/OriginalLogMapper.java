package com.kglory.tms.web.mapper.detectionAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.DetectionAttackHelpDto;
import com.kglory.tms.web.model.dto.OriginalLogSearchDto;
import com.kglory.tms.web.model.vo.DetectionEventVO;
import com.kglory.tms.web.model.vo.OriginalLogVO;

public interface OriginalLogMapper {
		
	// 리스트 
	@Transactional(readOnly = true)
	List<OriginalLogVO> selectOriginalLogBylindex(OriginalLogSearchDto dto);
        
	// 리스트 
	@Transactional(readOnly = true)
	OriginalLogVO selectOriginalLogBylindexTotalCount(OriginalLogSearchDto dto);

	// 원본 로그 팝업 
	@Transactional(readOnly = true)
	List<OriginalLogVO> selectOriginalLogPopupList(OriginalLogSearchDto dto);
        
	// 원본 로그 팝업 
	@Transactional(readOnly = true)
	OriginalLogVO selectOriginalLogPopupListTotal(OriginalLogSearchDto dto);

	// 원본 패킷 팝업 
	@Transactional(readOnly = true)
	OriginalLogVO selectRawPacketPopup(OriginalLogSearchDto dto);

	// 원본 로그 공격정보도움말(탐지정보) 팝업
	@Transactional(readOnly = true)
	DetectionEventVO selectDetectionAttackHelpPopupData(DetectionAttackHelpDto dto);

}
