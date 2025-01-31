package com.kglory.tms.web.mapper.systemStatus;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.AuditDto;
import com.kglory.tms.web.model.vo.AuditVO;

public interface AuditLogStatusMapper {
	
	@Transactional
	public void insertAuditLog(AuditVO auditVO);
	
	@Transactional(readOnly = true)
	public List<AuditVO> selectAuditLogList(AuditDto dto);
        
	@Transactional(readOnly = true)
	public int selectAuditLogListTotalCount(AuditDto dto);
        
	@Transactional(readOnly = true)
	public List<AuditVO> selectAuditSensorLogList(AuditDto dto);
}
