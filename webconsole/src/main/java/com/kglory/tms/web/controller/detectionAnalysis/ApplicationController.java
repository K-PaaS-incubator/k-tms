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
import com.kglory.tms.web.model.dto.ApplicationSearchDto;
import com.kglory.tms.web.model.dto.VictimIpSearchDto;
import com.kglory.tms.web.model.vo.ApplicationVO;
import com.kglory.tms.web.model.vo.VictimIpVO;
import com.kglory.tms.web.services.detectionAnalysis.ApplicationService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class ApplicationController {

    private static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    ApplicationService applicationService;

    /**
     * 어플리케이션분석 원본로그 검색 조회
     *
     * @param dto
     * @param result
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/detectionAnalysis/selectApplicationLogList", method = RequestMethod.POST)
    @ResponseBody
    public List<ApplicationVO> selectApplicationLogList(@RequestBody ApplicationSearchDto dto) throws BaseException {
        List<ApplicationVO> listData = null;
        try {
            listData = applicationService.selectApplicationLogList(dto);
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
        } 

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        if (listData == null) {
            return new ArrayList<ApplicationVO>();
        } else {
            return listData;
        }
    }
    
    /**
     * 어플리케이션 분석 로그 도움말 팝업
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/api/detectionAnalysis/selectApplicationHelpPopupList", method = RequestMethod.POST)
    @ResponseBody
    public ApplicationVO selectApplicationHelpPopupList(@RequestBody ApplicationSearchDto dto) {
        ApplicationVO applicationVO = new ApplicationVO();
        try {
            applicationVO = applicationService.selectApplicationHelpPopupList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return applicationVO;

    }
}
