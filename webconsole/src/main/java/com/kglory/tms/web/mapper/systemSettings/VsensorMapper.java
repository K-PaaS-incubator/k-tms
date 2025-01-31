package com.kglory.tms.web.mapper.systemSettings;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.DetectionExceptionDto;
import com.kglory.tms.web.model.dto.NetworkDto;
import com.kglory.tms.web.model.dto.SessionMonitoringFile;
import com.kglory.tms.web.model.dto.VsensorDto;
import com.kglory.tms.web.model.vo.DetectionExceptionVO;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.model.vo.NetworkVO;
import com.kglory.tms.web.model.vo.SessionServiceDataVO;

public interface VsensorMapper {

    // 세션모니터링 서비스 목록 
    @Transactional(readOnly = true)
    List<SessionServiceDataVO> selectSessionMonitoringService(VsensorDto dto);

    // 세션모니터링 서비스 목록 삭제 
    @Transactional
    void deleteSessionMonitoringService(VsensorDto dto);

    // 세션모니터링 서비스 목록 등록 (생성) 
    @Transactional
    void insertSessionMonitoringService(VsensorDto dto);

    // 세션모니터링 서비스 목록 (세션감시 신규 생성시 조회)
    @Transactional(readOnly = true)
    List<SessionServiceDataVO> selectSessionMonitoringServiceData();

    // 탐지예외 정책 그룹 조회 
    @Transactional(readOnly = true)
    List<DetectionPolicyVO> selectDetectionPolicyGroup();

    // 탐지예외 조회
    @Transactional(readOnly = true)
    List<DetectionExceptionVO> selectDetectionException(DetectionExceptionDto dto);
    
    @Transactional(readOnly = true)
    DetectionExceptionVO selectDetectionExceptionDetail(DetectionExceptionDto dto);

    // 시스템 설정 탐지예외 수정/저장
    @Transactional
    void updateDetectionException(DetectionExceptionDto detectionPolicyDto);

    @Transactional
    // 침입탐지예외 수정/저장
    void updatePolicyException(DetectionExceptionDto detectionPolicyDto);

    @Transactional
    // 침입탐지예외 수정/저장
    void updatePolicyExceptionByIndex(DetectionExceptionDto detectionPolicyDto);

    @Transactional
    void insertDetectionException(DetectionExceptionDto detectionExceptionDto);

    // 가상센서에 매핑되어 있는 네트워크 목록 
    @Transactional(readOnly = true)
    List<NetworkVO> selectVsensorTargetNetworkList(NetworkDto dto);

    // 탐지 예외 목록 삭제 
    @Transactional
    void deleteDetectionExceptionList(DetectionExceptionDto detectionExceptionDto);

    // 탐지 예외 목록 신규생성
    @Transactional
    void insertDetectionExceptionList(DetectionExceptionDto detectionExceptionDto);

    // 세션감시 변경된 내용이 있을 경우에만 업데이트를 하기 위해 조회
    @Transactional
    List<SessionMonitoringFile> selectVsensorPerSessionMonitoring(VsensorDto dto);

    // 실시간 탐지예외 설정 
    @Transactional(readOnly = true)
    void insertMonitorIntrusionDetectionException(DetectionExceptionDto dto);

    // 탐지예외 처리시 중복검사 
    @Transactional(readOnly = true)
    DetectionExceptionVO isDuplicateDetectionException(DetectionExceptionDto dto);

    @Transactional(readOnly = true)
    DetectionExceptionVO isTotalDuplicateDetectionException(DetectionExceptionDto detectionExceptionDto);

    @Transactional
    void updatePolicyTotalException(DetectionExceptionDto detectionExceptionDto);
}
