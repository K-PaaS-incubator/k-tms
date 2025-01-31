package com.kglory.tms.web.mapper.systemStatus;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.SearchDto;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.DbUsageVO;
import com.kglory.tms.web.model.vo.ManagerStateVO;

public interface DbUsageMapper {
	
	@Transactional(readOnly = true)
	List<DbUsageVO> selectDbUsageList(List<String> tableNames);
	
	@Transactional(readOnly = true)
	List<ManagerStateVO> selectManagerStateList(List<String> tableNames);

	
	@Transactional(readOnly = true)
	List<ChartVO> selectManagerStateCpuUsedGraphData(SearchDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectManagerStateMemUsedGraphData(SearchDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectManagerStateHddUsedGraphData(SearchDto dto);
	
	@Transactional(readOnly = true)
	List<ChartVO> selectManagerStateProcessNumGraphData(SearchDto dto);
        
	@Transactional(readOnly = true)
	ChartVO selectManagerStateCpuUsedGraphDataMinMaxAvg(SearchDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectManagerStateMemUsedGraphDataMinMaxAvg(SearchDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectManagerStateHddUsedGraphDataMinMaxAvg(SearchDto dto);
	
	@Transactional(readOnly = true)
	ChartVO selectManagerStateProcessNumGraphDataMinMaxAvg(SearchDto dto);
}
