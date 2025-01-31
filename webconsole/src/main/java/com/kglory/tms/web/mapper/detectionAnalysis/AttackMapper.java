package com.kglory.tms.web.mapper.detectionAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.AttackDto;
import com.kglory.tms.web.model.vo.AttackVO;
import com.kglory.tms.web.model.vo.ChartVO;

public interface AttackMapper {

	//탐지분석>공격 전체 값 구하기 (변화량)
	@Transactional(readOnly = true)
	AttackVO selectAttackTotalVariationList(AttackDto attackDto);
	
	//탐지분석>공격 리스트 구하기 (변화량)
	@Transactional(readOnly = true)
	List<AttackVO> selectAttackVariationList(AttackDto attackDto);
	
	//탐지분석>공격횟수 팝업 차트 구하기
	@Transactional(readOnly = true)
	List<ChartVO> selectAttackAttackCountGraphData(AttackDto dto);
        
	//탐지분석>공격횟수 팝업 차트 구하기
	@Transactional(readOnly = true)
	ChartVO selectAttackAttackCountGraphDataMinMax(AttackDto dto);
        
	//탐지분석>공격횟수 팝업 차트 구하기
	@Transactional(readOnly = true)
	ChartVO selectAttackAttackCountGraphDataAvg(AttackDto dto);

	//탐지분석>bps 팝업 차트 구하기
	@Transactional(readOnly = true)
	List<ChartVO> selectAttackBpsGraphData(AttackDto dto);
        
	//탐지분석>bps 팝업 차트 구하기
	@Transactional(readOnly = true)
	ChartVO selectAttackBpsGraphDataMinMax(AttackDto dto);
        
	//탐지분석>bps 팝업 차트 구하기
	@Transactional(readOnly = true)
	ChartVO selectAttackBpsGraphDataAvg(AttackDto dto);

	//탐지분석>pps 팝업 차트 구하기
	@Transactional(readOnly = true)
	List<ChartVO> selectAttackPpsGraphData(AttackDto dto);
        
	//탐지분석>pps 팝업 차트 구하기
	@Transactional(readOnly = true)
	ChartVO selectAttackPpsGraphDataMinMax(AttackDto dto);
        
	//탐지분석>pps 팝업 차트 구하기
	@Transactional(readOnly = true)
	ChartVO selectAttackPpsGraphDataAvg(AttackDto dto);
}
