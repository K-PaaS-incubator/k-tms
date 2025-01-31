package com.kglory.tms.web.mapper.detectionAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.VictimIpSearchDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.VictimIpChartVO;
import com.kglory.tms.web.model.vo.VictimIpVO;

public interface VictimIpMapper {
	
	// 팝업 공격횟수 그래프
	@Transactional(readOnly = true)
	List<ChartVO> selectVictimIpAttackCountGraphData(VictimIpSearchDto dto);
        
	// 팝업 공격횟수 그래프
	@Transactional(readOnly = true)
	ChartVO selectVictimIpAttackCountGraphDataMinMax(VictimIpSearchDto dto);
        
	// 팝업 공격횟수 그래프
	@Transactional(readOnly = true)
	ChartVO selectVictimIpAttackCountGraphDataAvg(VictimIpSearchDto dto);
	
	// 팝업 bps 그래프
	@Transactional(readOnly = true)
	List<ChartVO> selectVictimIpBpsGraphData(VictimIpSearchDto dto);
        
	// 팝업 bps 그래프
	@Transactional(readOnly = true)
	ChartVO selectVictimIpBpsGraphDataMinMax(VictimIpSearchDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectVictimIpBpsGraphDataAvg(VictimIpSearchDto dto);
	
	// 팝업 pps 그래프
	@Transactional(readOnly = true)
	List<ChartVO> selectVictimIpPpsGraphData(VictimIpSearchDto dto);
        
	// 팝업 pps 그래프
	@Transactional(readOnly = true)
	ChartVO selectVictimIpPpsGraphDataMinMax(VictimIpSearchDto dto);
        
	// 팝업 pps 그래프
	@Transactional(readOnly = true)
	ChartVO selectVictimIpPpsGraphDataAvg(VictimIpSearchDto dto);
}
