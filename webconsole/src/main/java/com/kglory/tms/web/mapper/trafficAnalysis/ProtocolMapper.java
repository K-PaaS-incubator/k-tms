package com.kglory.tms.web.mapper.trafficAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.TrafficProtocolDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolVO;

public interface ProtocolMapper {

	@Transactional(readOnly = true)
	List<TrafficProtocolVO> selectProtocolList(TrafficProtocolDto trafficProtocolDto);

	@Transactional(readOnly = true)
	TrafficProtocolVO selectProtocolTotal(TrafficProtocolDto trafficProtocolDto);
	
	@Transactional(readOnly = true)
	List<TrafficProtocolVO> selectProtocolVariationList(TrafficProtocolDto trafficProtocolDto);

	@Transactional(readOnly = true)
	TrafficProtocolVO selectProtocolTotalVariation(TrafficProtocolDto trafficProtocolDto);

	@Transactional(readOnly = true)
	List<TrafficProtocolChartVO> selectTrafficProtocolChart(TrafficProtocolDto trafficProtocolDto);
	
	@Transactional(readOnly = true)
	List<TrafficProtocolChartVO> selectTrafficProtocolInOutChart(TrafficProtocolDto trafficProtocolDto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectTrafficAnalysisBpsPopupGraphData(TrafficProtocolDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectTrafficAnalysisBpsPopupInOutGraphData(TrafficProtocolDto dto);

	@Transactional(readOnly = true)
	List<ChartVO> selectTrafficAnalysisPpsPopupGraphData(TrafficProtocolDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectTrafficAnalysisPpsPopupInOutGraphData(TrafficProtocolDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisBpsPopupGraphDataMinMax(TrafficProtocolDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisBpsPopupGraphDataAvg(TrafficProtocolDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisBpsPopupInOutGraphDataMinMax(TrafficProtocolDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisBpsPopupInOutGraphDataAvg(TrafficProtocolDto dto);

	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisPpsPopupGraphDataMinMax(TrafficProtocolDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisPpsPopupGraphDataAvg(TrafficProtocolDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisPpsPopupInOutGraphDataMinMax(TrafficProtocolDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectTrafficAnalysisPpsPopupInOutGraphDataAvg(TrafficProtocolDto dto);
	
}
