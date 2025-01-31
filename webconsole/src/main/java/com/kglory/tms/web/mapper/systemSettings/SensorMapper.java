package com.kglory.tms.web.mapper.systemSettings;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.IpMonitorDto;
import com.kglory.tms.web.model.dto.SensorInboundDto;
import com.kglory.tms.web.model.dto.SensorIntegrityFileDto;
import com.kglory.tms.web.model.dto.SystemConfigDto;
import com.kglory.tms.web.model.vo.SensorInboundVO;
import com.kglory.tms.web.model.vo.SensorIntegrityFileVO;
import com.kglory.tms.web.model.vo.SensorVO;

public interface SensorMapper {

	// 센서 목록
	@Transactional(readOnly = true)
	List<SensorVO> selectSensorSettingInfo();
        
	// 센서 등록 상세 정보
	@Transactional(readOnly = true)
	SensorVO selectSensorDetailInfo();

	// 센서 등록 정보 수정
	@Transactional
	void updateSensorDetailInfo(SystemConfigDto dto);

	// // 센서 신규 등록
	// @Transactional
	// void insertSensorDetailInfo(SystemConfigDto dto);

	// 센서 신규 등록
	@Transactional
	long insertSensorDetailInfo(SystemConfigDto dto);

	// 센서 정보 삭제
	@Transactional
	void deleteSensorSettingInfo(SystemConfigDto dto);

	// 센서 무결성 정보
	@Transactional(readOnly = true)
	SensorVO selectSensorIntegrityInfo(SystemConfigDto dto);

	// 센서 무결성 정보 수정
	@Transactional
	void updateSensorIntegrityInfo(SensorIntegrityFileDto dto);

	// 센서 무결성 정보 생성
	void insertSensorIntegrityInfo(SensorIntegrityFileDto dto);

	// 센서 인바운드 정보 조회
	@Transactional(readOnly = true)
	SensorInboundVO selectSensorInboundInfo();

	// 센서 인바운드 정보 삭제
	@Transactional
	void deleteSensorInboundInfo(SensorInboundDto dto);

	// 센서 인바운드 정보 등록
	@Transactional
	void insertSensorInboundInfo(SensorInboundDto dto);
        
	// 센서 인바운드 정보 수정
	@Transactional
	void updateSensorInboundInfo(SensorInboundDto dto);
	
	// 사설IP, 공인IP 중복확인 조회 
	@Transactional(readOnly = true)
	SensorVO duplicatePrivateIpAndPublicIp(SystemConfigDto dto);

	// 센서 이름 중복검사 
	@Transactional(readOnly = true)
	SensorVO isDuplicateSensorName(SystemConfigDto dto);

	// 센서 IP 모니터링 설정 정보 목록 조회  
        @Transactional(readOnly = true)
	ArrayList<SensorVO> selectSensorIpMonitoringList();
        
	// 센서 IP 모니터링 설정 정보 조회  
        @Transactional(readOnly = true)
	SensorVO selectSensorIpMonitoring(IpMonitorDto ipMonitorDto);

	@Transactional
	void deleteSensorIpMonitor(IpMonitorDto dto);
        
	// 센서 IP 모니터링 설정 신규생성 
	@Transactional
	void insertSensorIpMonitoring(IpMonitorDto ipMonitorDto);

	@Transactional
	void updateSensorIpMonitoring(IpMonitorDto ipMonitorDto);
        
        // 센서 전처리기 정보 수정
	@Transactional
	void updateSensorHyperscanHitCount(SystemConfigDto dto);
        
        // 센서 블랙리스트 사용여부 수정
	@Transactional
	void updateSensorUseBlackList(SystemConfigDto dto);
}
