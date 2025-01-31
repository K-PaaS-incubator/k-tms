package com.kglory.tms.web.mapper.dashboard;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.AttackInfoPopupDto;
import com.kglory.tms.web.model.vo.AttackHelpVO;

public interface AttackInfoPopupMapper {

	@Transactional(readOnly = true)
	List<AttackHelpVO> selectAttackInfoPopupList(AttackInfoPopupDto dto);
        
	@Transactional(readOnly = true)
	AttackHelpVO selectAttackInfoPopupListSeverityCount(AttackInfoPopupDto dto);
	
	@Transactional(readOnly = true)
	List<AttackHelpVO> selectTypeOfVulnerabilityList();

}
