package com.kglory.tms.web.mapper.trafficAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.MaliciousTrafficDto;
import com.kglory.tms.web.model.vo.MaliciousTrafficChartVO;
import com.kglory.tms.web.model.vo.MaliciousTrafficVO;

public interface MaliciousTrafficMapper {
	
	// 전체 트래픽, 유해트래픽 5분 통계 데이터 조회 
	@Transactional(readOnly = true)
	MaliciousTrafficVO selectMaliciousTrafficList(MaliciousTrafficDto dto);

	// 전체 트래픽, 유해트래픽 그래프 조회 
	@Transactional(readOnly = true)
	List<MaliciousTrafficChartVO> selectMaliciousTrafficChart(MaliciousTrafficDto dto);

	// 유해 트래픽 변화량 조회 
	@Transactional(readOnly = true)
	MaliciousTrafficVO selectMaliciousTrafficVariation(MaliciousTrafficDto dto);
}
