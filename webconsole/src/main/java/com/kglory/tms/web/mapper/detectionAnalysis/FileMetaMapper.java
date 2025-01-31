package com.kglory.tms.web.mapper.detectionAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.FileMetaSearchDto;
import com.kglory.tms.web.model.vo.FileMetaVO;

public interface FileMetaMapper {

	@Transactional(readOnly = true)
	List<FileMetaVO> selectFileMetaList (FileMetaSearchDto dto);
        
	@Transactional(readOnly = true)
	Long selectFileMetaListCount (FileMetaSearchDto dto);

	// 도움말 팝업 
	@Transactional(readOnly = true)
	FileMetaVO selectFileMetaHelpPopupList(FileMetaSearchDto dto);

}
