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
import com.kglory.tms.web.model.dto.IpDto;
import com.kglory.tms.web.model.vo.IpChartVO;
import com.kglory.tms.web.model.vo.IpVO;
import com.kglory.tms.web.services.trafficAnalysis.IpService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class IpController {

    private static Logger logger = LoggerFactory.getLogger(IpController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    IpService ipService;

    @RequestMapping(value = "/api/trafficAnalysis_ipList", method = RequestMethod.POST)
    @ResponseBody
    public List<IpVO> selectIpList(@RequestBody IpDto ipDto, BindingResult result) throws BaseException {

        List<IpVO> listData = new ArrayList<IpVO>();

        try {
            listData = ipService.selectIpList(ipDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(ipDto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        if (listData == null) {
            return new ArrayList<IpVO>();
        } else {
            return listData;
        }

    }

    @RequestMapping(value = "/api/trafficAnalysis_ipTotal", method = RequestMethod.POST)
    @ResponseBody
    public IpVO selectProtocolTotal(@RequestBody IpDto ipDto, BindingResult result) throws BaseException {

        IpVO data = new IpVO();

        try {
            data = ipService.selectIpTotal(ipDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (data == null) {
            return new IpVO();
        }
        return data;

    }

    @RequestMapping(value = "/api/trafficAnalysis/selectIpChart", method = RequestMethod.POST)
    @ResponseBody
    public List<IpChartVO> selectIpChart(@RequestBody IpDto ipDto, BindingResult result) throws BaseException {

        List<IpChartVO> chartDate = null;

        try {
            chartDate = ipService.selectIpChart(ipDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(ipDto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(chartDate));
        }
        
        if (chartDate == null) {
            return new ArrayList<IpChartVO>();
        } else {
            return chartDate;
        }
    }

    /**
     * service 팝업(top5 service)
     *
     * @param ipDto
     * @param result
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/trafficAnalysis/selectTop5ServicePopup", method = RequestMethod.POST)
    @ResponseBody
    public List<IpVO> selectTop5ServicePopup(@RequestBody IpDto ipDto, BindingResult result) throws BaseException {

        List<IpVO> resultList = null;

        try {
            resultList = ipService.selectTop5ServicePopup(ipDto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (resultList == null) {
            return new ArrayList<IpVO>();
        } else {
            return resultList;
        }
    }
}
