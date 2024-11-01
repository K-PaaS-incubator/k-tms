package com.kglory.tms.web.services.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.mapper.monitor.SensorMonitorMapper;
import com.kglory.tms.web.model.dto.SensorMonitoringDto;
import com.kglory.tms.web.model.vo.SensorMonitorVO;
import com.kglory.tms.web.util.TableFinder;

@Service
public class SensorSystemlogMonitorService {
	
	@Autowired
	SensorMonitorMapper	sensorMonitorMapper;
	
	public List<SensorMonitorVO> selectLastSensorMonitoring(SensorMonitoringDto dto) {
		dto.setSensorAliveTableName(TableFinder.getCurrentQueryTables("SENSOR_ALIVE", null));
		dto.setSensorSessionTableName(TableFinder.getCurrentQueryTables("SENSOR_SESSION", null));
		
		return sensorMonitorMapper.selectLastSensorMonitoring(dto);
	}
	
}
