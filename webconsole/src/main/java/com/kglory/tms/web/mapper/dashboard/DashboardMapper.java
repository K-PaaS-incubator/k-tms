package com.kglory.tms.web.mapper.dashboard;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kglory.tms.web.model.dto.ApplicationSearchDto;
import com.kglory.tms.web.model.dto.AuditDto;
import com.kglory.tms.web.model.dto.ProtocolDto;
import com.kglory.tms.web.model.dto.ServiceDto;
import com.kglory.tms.web.model.dto.StatEPDto;
import com.kglory.tms.web.model.vo.ApplicationVO;
import com.kglory.tms.web.model.vo.AuditVO;
import com.kglory.tms.web.model.vo.ProtocolVO;
import com.kglory.tms.web.model.vo.ServiceVO;
import com.kglory.tms.web.model.vo.StatEPVO;
import com.kglory.tms.web.model.vo.VictimIpVO;

public interface DashboardMapper {
	
	@Transactional(readOnly = true)
	List<ServiceVO> selectServiceTopN(ServiceDto dto);
        
	@Transactional(readOnly = true)
	long selectServiceTopNTotal(ServiceDto dto);
	
	@Transactional(readOnly = true)
	List<StatEPVO> selectEventTopN(StatEPDto dto);
        
	@Transactional(readOnly = true)
	long selectEventTopNTotal(StatEPDto dto);
	
	@Transactional(readOnly = true)
	List<VictimIpVO> selectVictimIpTopN(StatEPDto dto);
        
	@Transactional(readOnly = true)
	VictimIpVO selectVictimIpTopNTotal(StatEPDto dto);
	
	@Transactional(readOnly = true)
	List<AuditVO> selectAuditTopN(AuditDto dto);
        
	@Transactional(readOnly = true)
	AuditVO selectAuditCount(AuditDto dto);
	
	@Transactional(readOnly = true)
	List<ProtocolVO> selectProtocolTraffic(ProtocolDto dto);
	
	@Transactional(readOnly = true)
	List<StatEPVO> selectStatEPTraffic(StatEPDto dto);
	
	@Transactional(readOnly = true)
	List<StatEPVO> selectOrg(StatEPDto dto);

	@Transactional(readOnly = true)
	List<ProtocolVO> protocolsTraffic(ProtocolDto dto);
        
	@Transactional(readOnly = true)
	List<ProtocolVO> selectProtocolList(ProtocolDto dto);

	@Transactional(readOnly = true)
	List<ApplicationVO> selectApplicationTopN(ApplicationSearchDto dto);
        
	@Transactional(readOnly = true)
	Long selectApplicationTopNTotal(ApplicationSearchDto dto);

	@Transactional(readOnly = true)
	List<StatEPVO> selectStatTraffic(StatEPDto dto);

	@Transactional(readOnly = true)
	List<StatEPVO> selectStatEvent(StatEPDto dto);

}
