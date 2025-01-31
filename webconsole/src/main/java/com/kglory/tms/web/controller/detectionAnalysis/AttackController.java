package com.kglory.tms.web.controller.detectionAnalysis;

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
import com.kglory.tms.web.model.dto.AttackDto;
import com.kglory.tms.web.model.validation.SearchDtoValidator;
import com.kglory.tms.web.model.vo.AttackVO;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.services.detectionAnalysis.AttackService;
import java.util.Locale;

@Controller
public class AttackController {
	
	private static Logger	logger	= LoggerFactory.getLogger(AttackController.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	AttackService				attackService;
	@Autowired
	SearchDtoValidator			searchDtoValidator;
	
	@RequestMapping(value = "api/selectAttackAttackCountGraphData", method = RequestMethod.POST)
	@ResponseBody
	public List<ChartVO> selectAttackAttackCountGraphData(@RequestBody AttackDto dto, BindingResult result){
		
		List<ChartVO> results = new ArrayList<ChartVO>();
		CommonBean errorBean = new CommonBean();
		
		try {
			searchDtoValidator.validate(dto, result);
			if (result.hasErrors()) {
				logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null,
						Locale.getDefault()));
			} else {
				
				results = attackService.selectAttackAttackCountGraphData(dto);
			}
		} catch (BaseException e1) {
			errorBean = e1.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
		}

		if(results.size() == 0){
			return new ArrayList<ChartVO>();
		}else{
			return results;
		}
	}
	
	@RequestMapping(value = "api/selectAttackBpsGraphData", method = RequestMethod.POST)
	@ResponseBody
	public List<ChartVO> selectAttackBpsGraphData(@RequestBody AttackDto dto, BindingResult result) {
		
		List<ChartVO> results = new ArrayList<ChartVO>();
		CommonBean errorBean = new CommonBean();
		
		try {
			searchDtoValidator.validate(dto, result);
			if (result.hasErrors()) {
				logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null,
						Locale.getDefault()));
			} else {
				
				results = attackService.selectAttackBpsGraphData(dto);
			}
		} catch (BaseException e) {
			errorBean = e.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
		}
		
		if(results.size() == 0){
			return new ArrayList<ChartVO>();
		}else{
			return results;
		}
	}
	
	@RequestMapping(value = "api/selectAttackPpsGraphData", method = RequestMethod.POST)
	@ResponseBody
	public List<ChartVO> selectAttackPpsGraphData(@RequestBody AttackDto dto, BindingResult result) {
		
		List<ChartVO> results = new ArrayList<ChartVO>();
		CommonBean errorBean = new CommonBean();
		
		try {
			searchDtoValidator.validate(dto, result);
			if (result.hasErrors()) {
				logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null,
						Locale.getDefault()));
			} else {
				
				results = attackService.selectAttackPpsGraphData(dto);
			}
		} catch (BaseException e) {
			errorBean = e.getErrorBean(errorBean);
			results.add((ChartVO) errorBean);
			return results;
		}
		
		if(results.size() == 0){
			return new ArrayList<ChartVO>();
		}else{
			return results;
		}
	}
}
