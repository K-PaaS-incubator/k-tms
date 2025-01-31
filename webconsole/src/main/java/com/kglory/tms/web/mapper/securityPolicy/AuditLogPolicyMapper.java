package com.kglory.tms.web.mapper.securityPolicy;

import java.math.BigInteger;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.AuditLogPolicyDto;
import com.kglory.tms.web.model.vo.AuditLogPolicyVO;

public interface AuditLogPolicyMapper {
	
	@Transactional(readOnly = true)
	public AuditLogPolicyVO selectAuditLogPolicy(AuditLogPolicyVO auditLogPolicyVO);
        
	@Transactional(readOnly = true)
	public AuditLogPolicyVO selectAuditLogDetail(AuditLogPolicyVO auditLogPolicyVO);
	
	// 보안정책> 감사로그정책 > 감사로그정책 행위 리스트 조회 
	@Transactional(readOnly = true)
	public List<AuditLogPolicyDto> selectAuditLogPolicyActionList(AuditLogPolicyDto dto);

	// 보안정책> 감사로그정책 > 감사로그정책 오류 (센서, 매니저, 콘솔) 리스트 조회
	@Transactional(readOnly = true)
	public List<AuditLogPolicyDto> selectAuditLogPolicyErrorList(AuditLogPolicyDto dto);

	// 보안정책> 감사로그정책 > 감사로그정책 경고 리스트 조회 
	@Transactional(readOnly = true)
	public List<AuditLogPolicyDto> selectAuditLogPolicyWarningList(AuditLogPolicyDto dto);
	
	// 보안정책> 감사로그정책 > 감사로그정책 행위 리스트 업데이트 
	@Transactional
	public Integer updateAuditLogPolicyList(AuditLogPolicyDto auditLogPolicyDto);

	// 보안정책> 감사로그정책 > 감사로그정책 WarningSetIndex 조회  
	@Transactional(readOnly = true)
	public BigInteger selectWarningsetIndex(AuditLogPolicyDto dto);

	
}
