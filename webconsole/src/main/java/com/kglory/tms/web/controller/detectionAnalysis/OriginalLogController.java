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
import com.kglory.tms.web.model.dto.DetectionAttackHelpDto;
import com.kglory.tms.web.model.dto.OriginalLogSearchDto;
import com.kglory.tms.web.model.vo.DetectionEventVO;
import com.kglory.tms.web.model.vo.OriginalLogVO;
import com.kglory.tms.web.services.detectionAnalysis.OriginalLogService;
import com.kglory.tms.web.services.monitor.IntrusionDetectionService;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.packet.Packet;
import com.kglory.tms.web.util.packet.PacketAnalyzer;

@Controller
public class OriginalLogController {

    private static Logger logger = LoggerFactory.getLogger(OriginalLogController.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    OriginalLogService originalLogService;
    @Autowired
    IntrusionDetectionService intrusionDetectionService;

    @RequestMapping(value = "/api/detectionAnalysis/selectOriginalLogBylindex", method = RequestMethod.POST)
    @ResponseBody
    public List<OriginalLogVO> selectOriginalLogBylindex(@RequestBody OriginalLogSearchDto dto, BindingResult result) throws BaseException {

        List<OriginalLogVO> listData = null;

        try {
            listData = originalLogService.selectOriginalLogBylindex(dto);
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        if (listData == null) {
            return new ArrayList<OriginalLogVO>();
        } else {
            return listData;
        }
    }

    @RequestMapping(value = "api/selectOriginalLogPopupList", method = RequestMethod.POST)
    @ResponseBody
    public List<OriginalLogVO> selectOriginalLogPopupList(@RequestBody OriginalLogSearchDto dto, BindingResult result) throws BaseException {

        return originalLogService.selectOriginalLogPopupList(dto);

    }

    @RequestMapping(value = "/api/selectRawPacketPopup", method = RequestMethod.POST)
    @ResponseBody
    public Packet selectRawPacketPopup(@RequestBody OriginalLogSearchDto dto, BindingResult result) throws BaseException {
        OriginalLogVO resultData = new OriginalLogVO();
        Packet packet = null;

        if ("".equals(dto.getEncodingType()) || dto.getEncodingType() == null) {
            dto.setEncodingType("US-ASCII");
        }

        try {
            resultData = originalLogService.selectRawPacketPopup(dto);
            
            logger.info("resultData.getsData() >>> "+resultData.getsData());
            logger.info("resultData.getDwMaliciousSrvFrame() >>> "+resultData.getDwMaliciousSrvFrame().intValue());
            logger.info("resultData.getDwMaliciousSrvByte() >>> "+resultData.getDwMaliciousSrvByte().intValue());
            logger.info("resultData.getEncodingType() >>> "+dto.getEncodingType());
            
            
            PacketAnalyzer packetAnalyzer = new PacketAnalyzer();
            packet = packetAnalyzer.analyzeHexStringPacket(resultData.getsData(),
                    resultData.getDwMaliciousSrvFrame().intValue(),
                    resultData.getDwMaliciousSrvByte().intValue(),
                    dto.getEncodingType()
            );
            logger.debug(packet.toString());

        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return packet;
    }

    @RequestMapping(value = "/api/selectDetectionAttackHelpPopupData", method = RequestMethod.POST)
    @ResponseBody
    public DetectionEventVO selectDetectionAttackHelpPopupData(@RequestBody DetectionAttackHelpDto dto, BindingResult result) {
        DetectionEventVO eventVOs = new DetectionEventVO();
        try {
            eventVOs = originalLogService.selectDetectionAttackHelpPopupData(dto);
        } catch (BaseException e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            // 알수 없는 에러 발생
            logger.error(e.getLocalizedMessage(), e);
        }
        if (eventVOs == null) {
            return new DetectionEventVO();
        } else {
            return eventVOs;
        }
    }

}
