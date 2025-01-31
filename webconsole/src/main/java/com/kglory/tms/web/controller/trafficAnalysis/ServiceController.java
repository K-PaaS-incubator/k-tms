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
import com.kglory.tms.web.model.dto.TrafficServiceDto;
import com.kglory.tms.web.model.validation.SearchDtoValidator;
import com.kglory.tms.web.model.vo.ChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceChartVO;
import com.kglory.tms.web.model.vo.TrafficServiceVO;
import com.kglory.tms.web.services.trafficAnalysis.ServiceService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class ServiceController {

    private static Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    ServiceService serviceService;
    @Autowired
    SearchDtoValidator searchDtoValidator;

    @RequestMapping(value = "/api/trafficAnalysis_serviceList", method = RequestMethod.POST)
    @ResponseBody
    public List<TrafficServiceVO> selectServiceList(@RequestBody TrafficServiceDto trafficServiceDto, BindingResult result) throws BaseException {

        List<TrafficServiceVO> listData = new ArrayList<TrafficServiceVO>();

        try {
            listData = serviceService.selectServiceList(trafficServiceDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(trafficServiceDto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        if (listData == null) {
            return new ArrayList<TrafficServiceVO>();
        } else {
            return listData;
        }

    }

//	@RequestMapping(value = "/api/trafficAnalysis_serviceVariationList",  method = RequestMethod.POST)
//	@ResponseBody
//	public List<TrafficServiceVO> selectServiceVariationList(@RequestBody TrafficServiceDto trafficServiceDto, BindingResult result) throws BaseException{
//		
//		List<TrafficServiceVO> listData = new ArrayList<TrafficServiceVO>();
//		
//		try{
//			listData = serviceService.selectServiceVariationList(trafficServiceDto);
//		}catch(Exception e){
//			logger.error(e.getLocalizedMessage(), e);
//		}
//		
//		if (listData == null){
//			return new ArrayList<TrafficServiceVO>();
//		}else{
//			return listData;
//		}
//	}
    @RequestMapping(value = "/api/trafficAnalysis_serviceTotal", method = RequestMethod.POST)
    @ResponseBody
    public TrafficServiceVO selectProtocolTotal(@RequestBody TrafficServiceDto trafficServiceDto, BindingResult result) throws BaseException {

        TrafficServiceVO data = new TrafficServiceVO();

        try {
            data = serviceService.selectServiceTotal(trafficServiceDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (data == null) {
            return new TrafficServiceVO();
        }
        return data;

    }

//	@RequestMapping(value = "/api/trafficAnalysis_serviceTotalVariation",  method = RequestMethod.POST)
//	@ResponseBody
//	public  TrafficServiceVO selectProtocolTotalVariation(@RequestBody TrafficServiceDto trafficServiceDto, BindingResult result)throws BaseException{
//		
//		TrafficServiceVO data = new TrafficServiceVO();
//		
//		try {
//			data = serviceService.selectServiceTotalVariation(trafficServiceDto);
//		} catch (Exception e) {
//			logger.error(e.getLocalizedMessage(), e);
//		}
//		
//		if(data == null){
//			return new TrafficServiceVO();
//		}
//		return data;
//		
//	}
    @RequestMapping(value = "/api/trafficAnalysis/selectServiceChart", method = RequestMethod.POST)
    @ResponseBody
    public List<TrafficServiceChartVO> selectServiceChart(@RequestBody TrafficServiceDto trafficServiceDto, BindingResult result) throws BaseException {

        List<TrafficServiceChartVO> chartDate = null;

        try {
            chartDate = serviceService.selectServiceChart(trafficServiceDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(trafficServiceDto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(chartDate));
        }
        
        if (chartDate == null) {
            return new ArrayList<TrafficServiceChartVO>();
        } else {
            return chartDate;
        }
    }

    @RequestMapping(value = "api/selectServiceTrendBpsGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectServiceTrendBpsGraphData(@RequestBody TrafficServiceDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = serviceService.selectServiceTrendBpsGraphData(dto);
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

    @RequestMapping(value = "api/selectServiceTrendPpsGraphData", method = RequestMethod.POST)
    @ResponseBody
    public List<ChartVO> selectServiceTrendPpsGraphData(@RequestBody TrafficServiceDto dto, BindingResult result) {

        List<ChartVO> results = new ArrayList<ChartVO>();
        CommonBean errorBean = new CommonBean();

        try {
            searchDtoValidator.validate(dto, result);
            if (result.hasErrors()) {
                logger.debug(messageSource.getMessage(result.getAllErrors().get(0).getCode(), null, Locale.getDefault()));
            } else {
                results = serviceService.selectServiceTrendPpsGraphData(dto);
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

    /**
     * protocol 포트 에 해당하는 ip, traffic List 조회 (팝업)
     *
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = "/api/trafficAnalysis/selectProtocolIpTrafficList", method = RequestMethod.POST)
    @ResponseBody
    public List<TrafficServiceVO> selectProtocolIpTrafficList(@RequestBody TrafficServiceDto dto, BindingResult result) {

        List<TrafficServiceVO> listData = new ArrayList<TrafficServiceVO>();

        try {
            listData = serviceService.selectProtocolIpTrafficList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (listData == null) {
            return new ArrayList<TrafficServiceVO>();
        } else {
            return listData;
        }
    }

}
