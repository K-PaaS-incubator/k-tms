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
import com.kglory.tms.web.model.dto.VictimIpSearchDto;
import com.kglory.tms.web.model.validation.SearchDtoValidator;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.VictimIpChartVO;
import com.kglory.tms.web.model.vo.VictimIpVO;
import com.kglory.tms.web.services.detectionAnalysis.VictimIpService;
import com.kglory.tms.web.util.StringUtil;
import java.util.Locale;

/**
 * Handles requests for the application home page.
 */
@Controller
public class VictimIpController {
	private static Logger	logger	= LoggerFactory.getLogger(VictimIpController.class);
	
	@Autowired
	MessageSource				messageSource;
	@Autowired
	VictimIpService				victimIpService;
	@Autowired
	SearchDtoValidator			searchDtoValidator;
	
	@RequestMapping(value = "api/selectVictimIpAttackCountGraphData", method = RequestMethod.POST)
	@ResponseBody
	public List<ChartVO> selectVictimIpAttackCountGraphData(@RequestBody VictimIpSearchDto dto, BindingResult result){
		
		List<ChartVO> results = new ArrayList<ChartVO>();
		CommonBean errorBean = new CommonBean();
		
		try {
			searchDtoValidator.validate(dto, result);
			if (result.hasErrors()) {
				logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null,
						Locale.getDefault()));
			} else {
				
				results = victimIpService.selectVictimIpAttackCountGraphData(dto);
			}
		} catch (BaseException e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
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
	
	@RequestMapping(value = "api/selectVictimIpBpsGraphData", method = RequestMethod.POST)
	@ResponseBody
	public List<ChartVO> selectVictimIpBpsGraphData(@RequestBody VictimIpSearchDto dto, BindingResult result){
		
		List<ChartVO> results = new ArrayList<ChartVO>();
		CommonBean errorBean = new CommonBean();
		
		try {
			searchDtoValidator.validate(dto, result);
			if (result.hasErrors()) {
				logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null,
						Locale.getDefault()));
			} else {
				
				results = victimIpService.selectVictimIpBpsGraphData(dto);
			}
//		} catch (BaseException e) {
//			// Service등에서 알 수 있는 메시지 발생
//			logger.error(e.getLocalizedMessage(), e);
//			errorBean = e.getErrorBean(errorBean);
//			results.add((ChartVO) errorBean);
		} catch (BaseException e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
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
	
	@RequestMapping(value = "api/selectVictimIpPpsGraphData", method = RequestMethod.POST)
	@ResponseBody
	public List<ChartVO> selectVictimIpPpsGraphData(@RequestBody VictimIpSearchDto dto, BindingResult result){
		
		List<ChartVO> results = new ArrayList<ChartVO>();
		CommonBean errorBean = new CommonBean();
		
		try {
			searchDtoValidator.validate(dto, result);
			if (result.hasErrors()) {
				logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null,
						Locale.getDefault()));
			} else {
				
				results = victimIpService.selectVictimIpPpsGraphData(dto);
			}
//		} catch (BaseException e) {
//			// Service등에서 알 수 있는 메시지 발생
//			logger.error(e.getLocalizedMessage(), e);
//			errorBean = e.getErrorBean(errorBean);
//			results.add((ChartVO) errorBean);
		} catch (BaseException e) {
			// 알수 없는 에러 발생
			logger.error(e.getLocalizedMessage(), e);
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