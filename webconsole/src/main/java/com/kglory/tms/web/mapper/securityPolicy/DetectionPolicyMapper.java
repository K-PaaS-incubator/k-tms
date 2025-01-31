package com.kglory.tms.web.mapper.securityPolicy;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.DetectionPolicyDto;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;

public interface DetectionPolicyMapper {

    // 그룹유형별 탐지정책 목록 조회 
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> selectSignaturePerGroup(DetectionPolicyDto dto);

    // 탐지정책 목록 조회 
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> selectDetectionPolicy(DetectionPolicyDto dto);

    // 시그니처 상세 정보 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectDetectionPolicyDetail(DetectionPolicyDto dto);

    // 시그니처 도움말 상세 정보 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectDetectionPolicyHelp(DetectionPolicyDto dto);

    // 벤더시그니처 상세 정보 수정
    @Transactional
    public void updateDetectionPolicy(DetectionPolicyDto dto);

    // 벤더시그니처 요약정보 수정
    @Transactional
    public void updateDetectionPolicySummary(DetectionPolicyDto dto);

    // 유저시그니처 등록정보 수정
    @Transactional
    public void updateUserSignatureDetail(DetectionPolicyDto dto);

    // 유저시그니처 도움말 수정 
    @Transactional
    public void updateUserSignatureSummary(DetectionPolicyDto dto);

    // 유저시그니처 도움말 정보 lCode를 가지고 insert
    @Transactional
    public void updateUserSignatureHelp(DetectionPolicyDto dto);

    // 유저 시그니처 도움말 정보 수정
    @Transactional
    public void updateUserSignature(DetectionPolicyDto dto);

    // 공격유형 목록 정보 (select Box)
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> selectAttackTypeSelect();

    // 사용자 정의정책 신규생성
    @Transactional
    public long insertUserSignature(DetectionPolicyDto dto);

    // 요약정보가 Null이면 사용여부 insert 
    @Transactional
    public void insertPolicySignatureSummary(DetectionPolicyDto dto);	// 삭제

    // 탐지여부 정보 저장 
    @Transactional
    public void insertUserSignaturelUsed(DetectionPolicyDto dto);

    // 사용자 정의정책 기본 정보와 요약정보 insert
    @Transactional
    public void insertUserSignatureHelp(DetectionPolicyDto dto);

    // 사용자 정의정책 상세 도움말 신규생성
    @Transactional
    public void insertUserSignatureHelpDetail(DetectionPolicyDto dto);

    // 요약설명이 Null이면 도움말 정보 insert
    @Transactional
    public void insertUserSignatureDetail(DetectionPolicyDto dto);

    @Transactional
    public void insertUserSignatureSummary(DetectionPolicyDto dto);

    // 사용자 정의 정책 목록 삭제 
    @Transactional
    public void deleteUserSignature(DetectionPolicyDto dto);

    // 사용자 정의 정책 도움말 정보 삭제
    @Transactional
    public void deleteUserSignatureHelp(DetectionPolicyDto dto);

    // 룰 검사 
    @Transactional
    public void signatureRuleCheck(DetectionPolicyDto dto);

    // 공격명 중복검사
    @Transactional(readOnly = true)
    public DetectionPolicyVO isDuplicateSignatureName(DetectionPolicyDto dto);

    // 사용자정의 공격유형 추가
    @Transactional
    public void insertSignatureClassType(DetectionPolicyDto dto);

    // 사용자정의 공격유형 조회 
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> selectSignatureClassType(DetectionPolicyDto dto);

    // 시그니처 그룹 명 중복 검사
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> isDuplicateSignatureClassTypeName(DetectionPolicyDto dto);

    // 사용자정의 공격유형 삭제 
    @Transactional
    public void deleteSignatureClassType(DetectionPolicyDto dto);

    // 침입탐지 벤더룰 탐지/대응 설정 조회 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectIntrusionDetectionResponse(DetectionPolicyDto dto);

    // 침입탐지 탐지/대응 설정 저장
    @Transactional
    public void updateIntrusionDetectionResponse(DetectionPolicyDto dto);

    // 침입탐지 탐지/사용자 정의 대응 설정 정보 저장
    @Transactional
    public void updateIntrusionDetectionUserResponse(DetectionPolicyDto dto);

    // 침입탐지 탐지/유형 그룹 조회 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectIntrusionDetectionNclassType(DetectionPolicyDto dto);

    // 사용자정의 정책 등록시 코드 중복 검사 
    @Transactional(readOnly = true)
    public DetectionPolicyVO isDuplicatelCode(DetectionPolicyDto dto);

    // 사용자정의 정책 등록시 코드 자동 생성 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectUserSignatureIndex(DetectionPolicyDto dto);

    // 침입탐지 사용자 탐지/유형 그룹 조회 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectIntrusionDetectionUserNclassType(DetectionPolicyDto dto);

    // 침입탐지 사용자룰 탐지/대응 설정 조회 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectIntrusionDetectionUserResponse(DetectionPolicyDto dto);

    // Rule merge
    @Transactional
    public void mergeUserSignature(DetectionPolicyVO pa);

    @Transactional
    public void mergeVendorSignature(DetectionPolicyVO pa);
    
    @Transactional
    public Long isSignature(DetectionPolicyDto pa);
    
    @Transactional
    public void updateImportVendorSignature(DetectionPolicyDto pa);
    
    @Transactional
    public void updateImportUserSignature(DetectionPolicyDto pa);
    
    @Transactional
    public void insertImportVendorSignature(DetectionPolicyDto pa);
    
    @Transactional
    public void insertImportUserSignature(DetectionPolicyDto pa);

    // 도움말 정보 저장을 위해 공격유형 그룹 조회 
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectSclassTypeName(long getsClassType);

    // 도움말 정보 저장을 위해 그룹코드 조회
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectSignatureHelp(DetectionPolicyDto dto);

    // 인덱스 없을 경우 도움말 업데이트 
    @Transactional
    public void updateUserSignatureHelpDetail(DetectionPolicyDto dto);

    // 도움말 정보가 데이터가 없을 경우에 insert하기 위해 조회   
    @Transactional(readOnly = true)
    public DetectionPolicyVO selectSignatureHelplCode(long getlCode);
    
    // pol 파일 생성 조회(밴더)
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> selectVendorWritePolicy();
    
    // pol 파일 생성 조회(사용자)
    @Transactional(readOnly = true)
    public List<DetectionPolicyVO> selectUserWritePolicy();
}
