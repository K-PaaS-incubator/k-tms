package com.kglory.tms.web.controller.detectionAnalysis;


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
import com.kglory.tms.web.model.dto.AttackDto;
import com.kglory.tms.web.model.validation.SearchDtoValidator;
import com.kglory.tms.web.model.vo.AttackHelpVO;
import com.kglory.tms.web.services.detectionAnalysis.NationAttackService;

@Controller
public class NationAttackController {
	
	private static Logger	logger	= LoggerFactory.getLogger(NationAttackController.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	NationAttackService			nationAttackService;
	@Autowired
	SearchDtoValidator			searchDtoValidator;
	
	@RequestMapping(value = "api/selectAttackHelpPopupData", method = RequestMethod.POST)
	@ResponseBody
	public AttackHelpVO selectAttackHelpPopupData(@RequestBody AttackDto dto, BindingResult result) throws BaseException {
		AttackHelpVO results = new AttackHelpVO();
		CommonBean errorBean = new CommonBean();
		try {
			logger.debug(dto.toString());
			results = nationAttackService.selectAttackHelpPopupData(dto);
			
			results.setStrDescription(results.voCleanXSS(results.getStrDescription()));
			
		} catch (BaseException e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
			throw new BaseException(messageSource, "errorCode", null, "", e);
		} catch (Exception e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
			throw new BaseException(messageSource, "errorCode", null, "", e);
		}
		
		return results;
	}
}
