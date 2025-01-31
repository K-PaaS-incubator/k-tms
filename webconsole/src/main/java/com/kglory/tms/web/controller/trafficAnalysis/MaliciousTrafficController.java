package com.kglory.tms.web.controller.trafficAnalysis;

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
import com.kglory.tms.web.model.dto.MaliciousTrafficDto;
import com.kglory.tms.web.model.vo.MaliciousTrafficChartVO;
import com.kglory.tms.web.model.vo.MaliciousTrafficVO;
import com.kglory.tms.web.services.trafficAnalysis.MaliciousTrafficService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class MaliciousTrafficController {

    private static Logger logger = LoggerFactory.getLogger(MaliciousTrafficController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    MaliciousTrafficService maliciousTrafficService;

    @RequestMapping(value = "api/trafficAnalysis/selectMaliciousTrafficList", method = RequestMethod.POST)
    @ResponseBody
    public MaliciousTrafficVO selectMaliciousTrafficList(@RequestBody MaliciousTrafficDto dto) throws BaseException {

        MaliciousTrafficVO listData = null;
        try {
            listData = maliciousTrafficService.selectMaliciousTrafficList(dto);
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            if (listData != null) {
                logger.debug("result : " + StringUtil.logDebugMessage(listData.toString()));
            } else {
                logger.debug("result : listData null ~~");
            }
        }
        
        if (listData == null) {
            listData = new MaliciousTrafficVO();
        }
        return listData;

    }

    @RequestMapping(value = "api/trafficAnalysis/selectMaliciousTrafficChart", method = RequestMethod.POST)
    @ResponseBody
    public List<MaliciousTrafficChartVO> selectMaliciousTrafficChart(@RequestBody MaliciousTrafficDto dto) throws BaseException {
//        logger.debug("selectMaliciousTrafficChart Graph dto : " + dto);

        List<MaliciousTrafficChartVO> chartDate = null;

        // 테이블 정보 가져오는 부분 추가
        try {
            chartDate = maliciousTrafficService.selectMaliciousTrafficChart(dto);

        } catch (BaseException e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(chartDate));
        }
        
        if (chartDate == null) {
            return new ArrayList<MaliciousTrafficChartVO>();
        } else {
            return chartDate;
        }
    }

//	@RequestMapping(value = "api/trafficAnalysis/selectMaliciousTrafficVariation",  method = RequestMethod.POST)
//	@ResponseBody
//	public MaliciousTrafficVO selectMaliciousTrafficVariation(@RequestBody MaliciousTrafficDto dto, BindingResult result) throws BaseException {
//		
//		MaliciousTrafficVO resultData = new MaliciousTrafficVO();
//		
//		try {
//			resultData = maliciousTrafficService.selectMaliciousTrafficVariation(dto);
//		} catch (Exception e) {
//			logger.error(e.getLocalizedMessage(), e);
//		}
//		
//		if(resultData == null){
//			return new MaliciousTrafficVO();
//		}
//		return resultData;
//		
//	}
}
