package com.kglory.tms.web.mapper.detectionAnalysis;


import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.AttackDto;
import com.kglory.tms.web.model.vo.AttackHelpVO;

public interface NationAttackMapper {
	
	@Transactional(readOnly = true)
	public AttackHelpVO selectAttackHelpPopupData(AttackDto dto);
	
}
