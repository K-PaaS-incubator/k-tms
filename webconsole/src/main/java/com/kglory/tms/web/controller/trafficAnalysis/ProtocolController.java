package com.kglory.tms.web.controller.trafficAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.dto.TrafficProtocolDto;
import com.kglory.tms.web.model.validation.SearchDtoValidator;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolChartVO;
import com.kglory.tms.web.model.vo.TrafficProtocolVO;
import com.kglory.tms.web.services.trafficAnalysis.ProtocolService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class ProtocolController {

    private static Logger logger = LoggerFactory.getLogger(ProtocolController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    ProtocolService protocolService;
    @Autowired
    SearchDtoValidator searchDtoValidator;

    @RequestMapping(value = "api/trafficAnalysis_protocolList", method = RequestMethod.POST)
    @ResponseBody
    public List<TrafficProtocolVO> selectProtocolList(@RequestBody TrafficProtocolDto trafficProtocolDto, BindingResult result) {
        CommonBean errorBean = new CommonBean();
        List<TrafficProtocolVO> listData = new ArrayList<TrafficProtocolVO>();

        try {
            listData = protocolService.selectProtocolList(trafficProtocolDto);
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.debug(e.getLocalizedMessage());
            errorBean = e.getErrorBean(errorBean);
            listData.add((TrafficProtocolVO) errorBean);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(trafficProtocolDto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }
        if (listData == null) {
            return new ArrayList<TrafficProtocolVO>();
        } else {
            return listData;
        }

    }

//	@RequestMapping(value = "api/trafficAnalysis_protocolVariationList",  method = RequestMethod.POST)
//	@ResponseBody
//	public List<TrafficProtocolVO> selectProtocolVariationList(@RequestBody TrafficProtocolDto trafficProtocolDto, BindingResult result){
//		CommonBean errorBean = new CommonBean();
//		List<TrafficProtocolVO> listData = new ArrayList<TrafficProtocolVO>();
//		
//		try{
//			listData = protocolService.selectProtocolVariationList(trafficProtocolDto);
//		} catch (BaseException e) {
//			// Service등에서 알 수 있는 메시지 발생
//			logger.debug(e.getLocalizedMessage());
//			errorBean = e.getErrorBean(errorBean);
//			listData.add((TrafficProtocolVO) errorBean);
//		}catch(Exception e){
//			logger.error(e.getLocalizedMessage(), e);
//		}
//		
//		if (listData == null){
//			return new ArrayList<TrafficProtocolVO>();
//		}else{
//			return listData;
//		}
//		
//	}
    @RequestMapping(value = "api/trafficAnalysis_protocolTotal", method = RequestMethod.POST)
    @ResponseBody
    public TrafficProtocolVO selectProtocolTotal(@RequestBody TrafficProtocolDto trafficProtocolDto, BindingResult result) {

        CommonBean errorBean = new CommonBean();
        TrafficProtocolVO resultData = new TrafficProtocolVO();

        try {
            resultData = protocolService.selectProtocolTotal(trafficProtocolDto);
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.debug(e.getLocalizedMessage());
            errorBean = e.getErrorBean(errorBean);
            //resultData.add((TrafficProtocolVO) errorBean);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (resultData == null) {
            return new TrafficProtocolVO();
        }
        return resultData;

    }

//	@RequestMapping(value = "api/trafficAnalysis_protocolTotalVariation",  method = RequestMethod.POST)
//	@ResponseBody
//	public  TrafficProtocolVO selectProtocolTotalVariation(@RequestBody TrafficProtocolDto trafficProtocolDto, BindingResult result){
//		
//		CommonBean errorBean = new CommonBean();
//		TrafficProtocolVO resultData = new TrafficProtocolVO();
//		
//		try {
//			resultData = protocolService.selectProtocolTotalVariation(trafficProtocolDto);
//		} catch (BaseException e) {
//			// Service등에서 알 수 있는 메시지 발생
//			logger.debug(e.getLocalizedMessage());
//			errorBean = e.getErrorBean(errorBean);
//			//resultData.add((TrafficProtocolVO) errorBean);
//		} catch (Exception e) {
//			logger.error(e.getLocalizedMessage(), e);
//		}
//		
//		if(resultData == null){
//			return new TrafficProtocolVO();
//		}
//		return resultData;
//		
//	}
    @RequestMapping(value = "api/trafficAnalysis/selectTrafficProtocolChart", method = RequestMethod.POST)
    @ResponseBody
    public List<TrafficProtocolChartVO> selectTrafficProtocolChart(@RequestBody TrafficProtocolDto trafficProtocolDto) throws BaseException {

        List<TrafficProtocolChartVO> chartDate = null;

        // 테이블 정보 가져오는 부분 추가
        try {
            chartDate = protocolService.selectTrafficProtocolChart(trafficProtocolDto);

        } catch (BaseException e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(trafficProtocolDto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(chartDate));
        }
        if (chartDate == null) {
            return new ArrayList<TrafficProtocolChartVO>();
        } else {
            return chartDate;
        }
    }

    @RequestMapping(value = "api/selectTrafficAnalysisBpsPopupGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectTrafficAnalysisBpsPopupGraphData(@RequestBody TrafficProtocolDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = protocolService.selectTrafficAnalysisBpsPopupGraphData(dto);
            }
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
            errorBean = e.getErrorBean(errorBean);
            results.add((ChartVO) errorBean);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
            
            try {
				throw new BaseException(messageSource, "errorCode", null, "", e);
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				errorBean = e1.getErrorBean(errorBean);
				results.add((ChartVO) errorBean);
			}
            
        }
        return results;
    }

    @RequestMapping(value = "api/selectTrafficAnalysisPpsPopupGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectTrafficAnalysisPpsPopupGraphData(@RequestBody TrafficProtocolDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = protocolService.selectTrafficAnalysisPpsPopupGraphData(dto);
            }
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
            errorBean = e.getErrorBean(errorBean);
            results.add((ChartVO) errorBean);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
            
            try {
				throw new BaseException(messageSource, "errorCode", null, "", e);
			} catch (BaseException e1) {
				errorBean = e1.getErrorBean(errorBean);
				results.add((ChartVO) errorBean);
			}
            
        }
        return results;
    }
}
