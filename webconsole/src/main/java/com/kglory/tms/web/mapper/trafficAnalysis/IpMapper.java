package com.kglory.tms.web.mapper.trafficAnalysis;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.IpDto;
import com.kglory.tms.web.model.vo.IpChartVO;
import com.kglory.tms.web.model.vo.IpVO;

public interface IpMapper {

	// 리스트 
	@Transactional(readOnly = true)
	List<IpVO> selectIpList(IpDto ipDto);
	// 리스트 전체 건수
	@Transactional(readOnly = true)
	IpVO selectIpListTotalRow(IpDto ipDto);
	// 전체값 
	@Transactional(readOnly = true)
	IpVO selectIpTotal(IpDto ipDto);
	// 차트
	@Transactional(readOnly = true)
	List<IpChartVO> selectIpChart(IpDto ipDto);
	
	// 서비스 popup (top5 service)
	@Transactional(readOnly = true)
	List<IpVO> selectTop5ServicePopup(IpDto ipDto);
}
