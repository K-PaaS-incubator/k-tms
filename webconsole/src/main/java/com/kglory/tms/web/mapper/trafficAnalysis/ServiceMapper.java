package com.kglory.tms.web.mapper.trafficAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.TrafficServiceDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceVO;

public interface ServiceMapper {

	@Transactional(readOnly = true)
	List<TrafficServiceVO> selectServiceList(TrafficServiceDto trafficServiceDto);
        
	@Transactional(readOnly = true)
	Long selectServiceListTotal(TrafficServiceDto trafficServiceDto);

	@Transactional(readOnly = true)
	TrafficServiceVO selectServiceTotal(TrafficServiceDto trafficServiceDto);
	
	@Transactional(readOnly = true)
	List<TrafficServiceVO> selectServiceVariationList(TrafficServiceDto trafficServiceDto);

	@Transactional(readOnly = true)
	TrafficServiceVO selectServiceTotalVariation(TrafficServiceDto trafficServiceDto);

	@Transactional(readOnly = true)
	List<TrafficServiceChartVO> selectServiceChart(TrafficServiceDto trafficServiceDto);
	
	@Transactional(readOnly = true)
	List<TrafficServiceChartVO> selectServiceInOutChart(TrafficServiceDto trafficServiceDto);

	@Transactional(readOnly = true)
	List<ChartVO> selectServiceTrendBpsGraphData(TrafficServiceDto dto);

	@Transactional(readOnly = true)
	List<ChartVO> selectServiceTrendBpsInOutGraphData(TrafficServiceDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectServiceTrendPpsGraphData(TrafficServiceDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectServiceTrendPpsInOutGraphData(TrafficServiceDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendBpsGraphDataMinMax(TrafficServiceDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendBpsGraphDataAvg(TrafficServiceDto dto);

	@Transactional(readOnly = true)
	ChartVO selectServiceTrendBpsInOutGraphDataMinMax(TrafficServiceDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendBpsInOutGraphDataAvg(TrafficServiceDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendPpsGraphDataMinMax(TrafficServiceDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendPpsGraphDataAvg(TrafficServiceDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendPpsInOutGraphDataMinMax(TrafficServiceDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectServiceTrendPpsInOutGraphDataAvg(TrafficServiceDto dto);

	@Transactional(readOnly = true)
	List<TrafficServiceVO> selectProtocolIpTrafficList(TrafficServiceDto dto);
        
	@Transactional(readOnly = true)
	Long selectProtocolIpTrafficListTotalCount(TrafficServiceDto dto);
}
