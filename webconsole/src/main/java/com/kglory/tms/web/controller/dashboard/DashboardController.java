package com.kglory.tms.web.controller.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.ApplicationSearchDto;
//대쉬보드 감사로그
import com.kglory.tms.web.model.dto.AuditDto;
import com.kglory.tms.web.model.dto.ProtocolDto;
//대쉬보드 Top5서비스
import com.kglory.tms.web.model.dto.ServiceDto;
//대쉬보드 Top5이벤트
import com.kglory.tms.web.model.dto.StatEPDto;
import com.kglory.tms.web.model.vo.ApplicationVO;
import com.kglory.tms.web.model.vo.AuditVO;
import com.kglory.tms.web.model.vo.ProtocolVO;
import com.kglory.tms.web.model.vo.ServiceVO;
import com.kglory.tms.web.model.vo.StatEPVO;
import com.kglory.tms.web.model.vo.VictimIpVO;
import com.kglory.tms.web.services.dashboard.DashboardApplication;
import com.kglory.tms.web.services.dashboard.DashboardAudit;
import com.kglory.tms.web.services.dashboard.DashboardProtocol;
import com.kglory.tms.web.services.dashboard.DashboardService;
import com.kglory.tms.web.services.dashboard.DashboardStatEP;


@Controller
public class DashboardController {
	private static Logger	logger	= LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	DashboardService			dashboardService;
	@Autowired
	DashboardStatEP				dashboardStatEP;
	@Autowired
	DashboardAudit				dashboardAudit;
	@Autowired
	DashboardProtocol			dashboardProtocol;
	@Autowired
	DashboardApplication		dashboardApplication;
	
	
	@RequestMapping(value = "/api/dashboard/selectServiceTopN", method = RequestMethod.POST)
	@ResponseBody
	public List<ServiceVO> selectServiceTopN(@RequestBody ServiceDto dto, BindingResult result) {
		
		List<ServiceVO> commonBeans = new ArrayList<ServiceVO>();
		CommonBean errorBean = new CommonBean();
		try {

			commonBeans = dashboardService.selectServiceTopN(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			errorBean = e.getErrorBean(errorBean);
			commonBeans.add((ServiceVO) errorBean);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
			try {
				throw new BaseException(messageSource, "errorCode", null, "", e);
			} catch (BaseException e1) {
				logger.error(e1.getLocalizedMessage(), e1);
				errorBean = e1.getErrorBean(errorBean);
				commonBeans.add((ServiceVO) errorBean);
			}
		}
		return commonBeans;
	}
	
	@RequestMapping(value = "/api/dashboard/selectEventTopN", method = RequestMethod.POST)
	@ResponseBody
	public List<StatEPVO> selectEventTopN(@RequestBody StatEPDto dto, BindingResult result) {
		
		List<StatEPVO> commonBeans = new ArrayList<StatEPVO>();
		CommonBean errorBean = new CommonBean();
		try {
			commonBeans = dashboardStatEP.selectEventTopN(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			errorBean = e.getErrorBean(errorBean);
			commonBeans.add((StatEPVO) errorBean);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			try {
				throw new BaseException(messageSource, "errorCode", null, "", e);
			} catch (BaseException e1) {
				logger.error(e1.getLocalizedMessage(), e1);
				errorBean = e1.getErrorBean(errorBean);
				commonBeans.add((StatEPVO) errorBean);
			}
		}
		return commonBeans;
	}
	
	@RequestMapping(value = "/api/dashboard/selectVictimIpTopN", method = RequestMethod.POST)
	@ResponseBody
	public List<VictimIpVO> selectVictimIpTopN(@RequestBody StatEPDto dto, BindingResult result) {
		
		List<VictimIpVO> commonBeans = new ArrayList<VictimIpVO>();
		CommonBean errorBean = new CommonBean();
		try {
			commonBeans = dashboardStatEP.selectVictimIpTopN(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			errorBean = e.getErrorBean(errorBean);
			commonBeans.add((VictimIpVO) errorBean);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
//			errorBean = new BaseException(messageSource, "errorCode", null, "", e)
//					.getErrorBean(errorBean);
//			commonBeans.add((VictimIpVO) errorBean);
			try {
				throw new BaseException(messageSource, "errorCode", null, "", e);
			} catch (BaseException e1) {
				errorBean = e1.getErrorBean(errorBean);
				commonBeans.add((VictimIpVO) errorBean); 
			}
		}
		return commonBeans;
	}
	
	@RequestMapping(value = "/api/dashboard/selectAuditTopN", method = RequestMethod.POST)
	@ResponseBody
	public List<AuditVO> selectAuditTopN(@RequestBody AuditDto dto, BindingResult result) {
		
		List<AuditVO> commonBeans = new ArrayList<AuditVO>();
		CommonBean errorBean = new CommonBean();
		try {
			commonBeans = dashboardAudit.selectAuditTopN(dto);
			
			//View XSS(audit, detection) 처리
			for(AuditVO tmp : commonBeans) {
				tmp.setStrComment(tmp.voCleanXSS(tmp.getStrComment()));
				tmp.setStrContent(tmp.voCleanXSS(tmp.getStrContent()));
			}
			
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			errorBean = e.getErrorBean(errorBean);
			commonBeans.add((AuditVO) errorBean);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
//			errorBean = new BaseException(messageSource, "errorCode", null, "", e)
//					.getErrorBean(errorBean);
//			commonBeans.add((AuditVO) errorBean);
		}
		return commonBeans;
	}
	
	@RequestMapping(value = "/api/dashboard/selectProtocolTraffic", method = RequestMethod.POST)
	@ResponseBody
	public List<ProtocolVO> selectProtocolTraffic(@RequestBody ProtocolDto dto, BindingResult result) {
		
		List<ProtocolVO> protocolChart = new ArrayList<ProtocolVO>();
		//CommonBean errorBean = new CommonBean();
		try {
			protocolChart = dashboardProtocol.selectProtocolTraffic(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
		}
		return protocolChart;
	}
	
//	@RequestMapping(value = "/api/dashboard/selectStatEPTraffic", method = RequestMethod.POST)
//	@ResponseBody
//	public List<StatEPVO> selectStatEPTraffic(@RequestBody StatEPDto dto, BindingResult result) {
//		
//		List<StatEPVO> statEPChart = new ArrayList<StatEPVO>();
//		//CommonBean errorBean = new CommonBean();
//		try {
//			statEPChart = dashboardStatEP.selectStatEPTraffic(dto);
//		} catch (BaseException e) {
//			// Service등에서 알 수 있는 메시지 발생
//			logger.error(e.getLocalizedMessage(), e);
//		} catch (Exception e) {
//			// 알수 없는 에러 발생
//			logger.error(e.getLocalizedMessage(), e);
//		}
//		return statEPChart;
//	}
	@RequestMapping(value = "/api/dashboard/selectStatTraffic", method = RequestMethod.POST)
	@ResponseBody
	public List<StatEPVO> selectStatTraffic(@RequestBody StatEPDto dto, BindingResult result) {
		
		List<StatEPVO> statEPChart = new ArrayList<StatEPVO>();
		try {
			statEPChart = dashboardStatEP.selectStatTraffic(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
		}
		return statEPChart;
	}
	
	@RequestMapping(value = "/api/dashboard/selectStatEvent", method = RequestMethod.POST)
	@ResponseBody
	public List<StatEPVO> selectStatEvent(@RequestBody StatEPDto dto, BindingResult result) {
		
		List<StatEPVO> statEPChart = new ArrayList<StatEPVO>();
		try {
			statEPChart = dashboardStatEP.selectStatEvent(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
		}
		return statEPChart;
	}
	
	@RequestMapping(value = "/api/dashboard/selectOrg", method = RequestMethod.POST)
	@ResponseBody
	public List<StatEPVO> selectOrg(@RequestBody StatEPDto dto, BindingResult result) {
		
		List<StatEPVO> statOrgChart = new ArrayList<StatEPVO>();
		//CommonBean errorBean = new CommonBean();
		try {
			statOrgChart = dashboardStatEP.selectOrg(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
		} 
		return statOrgChart;
	}
	
	@RequestMapping(value = "/api/dashboard/protocolsTraffic", method = RequestMethod.POST)
	@ResponseBody
	public List<ProtocolVO> protocolsTraffic(@RequestBody ProtocolDto dto, BindingResult result) {
		
		List<ProtocolVO> protocolChart = new ArrayList<ProtocolVO>();
		try {
			protocolChart = dashboardProtocol.protocolsTraffic(dto);
//			logger.debug("protocolChart >>>>>>>>> "+ protocolChart);
		} catch (BaseException e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
		}
		return protocolChart;
	}
	
	@RequestMapping(value = "/api/dashboard/selectApplicationTopN", method = RequestMethod.POST)
	@ResponseBody
	public List<ApplicationVO> selectApplicationTopN(@RequestBody ApplicationSearchDto dto) {
		
		List<ApplicationVO> commonBeans = new ArrayList<ApplicationVO>();
		CommonBean errorBean = new CommonBean();
		try {
			commonBeans = dashboardApplication.selectApplicationTopN(dto);
		} catch (BaseException e) {
			// Service등에서 알 수 있는 메시지 발생
			logger.error(e.getLocalizedMessage(), e);
			errorBean = e.getErrorBean(errorBean);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
		}
		return commonBeans;
		
	}
}
