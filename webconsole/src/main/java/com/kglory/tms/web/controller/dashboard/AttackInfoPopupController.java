package com.kglory.tms.web.controller.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.dto.AttackInfoPopupDto;
import com.kglory.tms.web.model.vo.AttackHelpVO;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.services.dashboard.AttackInfoPopupService;

@Controller
public class AttackInfoPopupController {
	
	private static Logger logger= LoggerFactory.getLogger(AttackInfoPopupController.class);
	
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	AttackInfoPopupService		attackInfoPopupService;
	
	@RequestMapping(value = "/api/selectAttackInfoPopup", method = RequestMethod.POST)
	@ResponseBody 
	public List<AttackHelpVO> selectAttackInfoPopupList(@RequestBody AttackInfoPopupDto dto) throws BaseException{
		
		List<AttackHelpVO> listData = null;
		try{
			listData = attackInfoPopupService.selectAttackInfoPopupList(dto);
			
			//View XSS(audit, detection) 처리
			for(AttackHelpVO tmp : listData) {
				tmp.setStrTitle(tmp.voCleanXSS(tmp.getStrTitle()));
				tmp.setStrDescription(tmp.voCleanXSS(tmp.getStrDescription()));
				tmp.setStrCveId(tmp.voCleanXSS(tmp.getStrCveId()));
			}
			
		}catch(BaseException e){
			logger.error(e.getLocalizedMessage(), e);
		}
		if (listData == null) {
			return new ArrayList<AttackHelpVO>();
		} else {
			return listData;
		}
	}
	@RequestMapping(value = "/api/selectTypeOfVulnerabilityList", method = RequestMethod.POST)
	@ResponseBody 
	public List<AttackHelpVO> selectTypeOfVulnerabilityList() throws BaseException{
		
		List<AttackHelpVO> listData = null;
		try{
			listData = attackInfoPopupService.selectTypeOfVulnerabilityList();
		}catch(BaseException e){
			logger.error(e.getLocalizedMessage(), e);
		}
		if (listData == null) {
			return new ArrayList<AttackHelpVO>();
		} else {
			return listData;
		}
	}
	
}
