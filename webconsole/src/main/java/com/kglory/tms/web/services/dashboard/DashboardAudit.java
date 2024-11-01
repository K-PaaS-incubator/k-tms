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
import com.kglory.tms.web.model.dto.AuditDto;
import com.kglory.tms.web.model.vo.AuditVO;
import com.kglory.tms.web.util.TableFinder;
import java.util.ArrayList;

@Service
public class DashboardAudit {

	private static Logger logger = LoggerFactory.getLogger(DashboardAudit.class);

	@Autowired
	MessageSource 	messageSource;
	@Autowired
	DashboardMapper dashboardMapper;
	@Autowired
	OracleMapper 	oracleMapper;
	
	public List<AuditVO> selectAuditTopN(@RequestBody AuditDto dto) throws BaseException {
        List<AuditVO> rtnList = new ArrayList<>();
		String startDate = dto.getTmstart();
		String endDate = dto.getTmend();
		
		List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("AUDIT", startDate, endDate));
		dto.setTableNames(selectTables);
		
		rtnList = dashboardMapper.selectAuditTopN(dto);
        AuditVO auditVo = dashboardMapper.selectAuditCount(dto);
        if (rtnList != null && rtnList.size() > 0) {
            for(int i = 0; i < rtnList.size() ; i++) {
                rtnList.get(i).setCntAction(auditVo.getCntAction());
                rtnList.get(i).setCntError(auditVo.getCntError());
                rtnList.get(i).setCntWarning(auditVo.getCntWarning());
            }
        }
	    return rtnList;
	}
}
