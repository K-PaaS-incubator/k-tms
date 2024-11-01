package com.kglory.tms.web.services.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.dashboard.DashboardMapper;
import com.kglory.tms.web.model.dto.ServiceDto;
import com.kglory.tms.web.model.vo.ServiceVO;
import com.kglory.tms.web.util.TableFinder;
import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class DashboardService {

	private static Logger logger = LoggerFactory.getLogger(DashboardService.class);

	@Autowired
	MessageSource 	messageSource;
	@Autowired
	DashboardMapper dashboardMapper;
	@Autowired
	OracleMapper 	oracleMapper;

	public List<ServiceVO> selectServiceTopN(@RequestBody ServiceDto dto) throws BaseException {
		List<ServiceVO> rtnList = new ArrayList<>();
		String startDate = dto.getTmstart();
		String endDate = dto.getTmend();
		Long ipType = dto.getIpType();

		List<String> selectTables = oracleMapper
				.selectTables(TableFinder.getQueryTables("SERVICE", startDate, endDate, ipType));
		dto.setTableNames(selectTables);

		rtnList = dashboardMapper.selectServiceTopN(dto);
		if (rtnList != null && rtnList.size() > 0) {
			long total = dashboardMapper.selectServiceTopNTotal(dto);
			for (int i = 0; i < rtnList.size(); i++) {
				rtnList.get(i).setTotalbps(BigDecimal.valueOf(total));
			}
		}
		return rtnList;
	}
}
