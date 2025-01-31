package com.kglory.tms.web.controller.systemSettings;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.kglory.tms.web.model.dto.DetectionExceptionDto;
import com.kglory.tms.web.model.dto.DetectionPolicyDto;
import com.kglory.tms.web.model.dto.NetworkDto;
import com.kglory.tms.web.model.dto.VsensorDto;
import com.kglory.tms.web.model.vo.DetectionExceptionVO;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.model.vo.NetworkVO;
import com.kglory.tms.web.model.vo.SessionServiceDataVO;
import com.kglory.tms.web.services.securityPolicy.DetectionPolicyService;
import com.kglory.tms.web.services.systemSettings.VsensorService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.StringUtil;

@Controller
public class VirtualSensorController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    VsensorService vsensorService;
    @Autowired
    AuditLogService auditLogSvc;
    @Autowired
    DetectionPolicyService detectionSvc;

    private static Logger logger = LoggerFactory.getLogger(VirtualSensorController.class);

    /*
     *  가상센서 등록 상세 정보
     */

    @RequestMapping(value = "/api/systemSetting/selectSessionMonitoringService", method = RequestMethod.POST)
    @ResponseBody
    public List<SessionServiceDataVO> selectSessionMonitoringService(@RequestBody VsensorDto dto) throws BaseException {
        List<SessionServiceDataVO> result = null;
        try {
            result = vsensorService.selectSessionMonitoringService(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (result == null) {
            return new ArrayList<SessionServiceDataVO>();
        } else {
            return result;
        }
    }
    /*
     * 세션감시 서비스 목록을 수정(업데이트)한다.  
     * @param VsensorDto
     * @return 
     */

    @RequestMapping(value = "/api/systemSetting/updateSessionMonitoringService", method = RequestMethod.POST)
    @ResponseBody
    public void updateSessionMonitoringService(@RequestBody VsensorDto dto, HttpSession session) throws BaseException {
        try {
            vsensorService.updateSessionMonitoringService(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 세션감시 서비스 목록을 등록(생성)한다.  
     * @param VsensorDto
     * @return 
     */
    @RequestMapping(value = "/api/systemSetting/insertSessionMonitoringService", method = RequestMethod.POST)
    @ResponseBody
    public void insertSessionMonitoringService(@RequestBody VsensorDto dto, HttpSession session) throws BaseException {
        try {
            vsensorService.insertSessionMonitoringService(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 세션 감시 서비스 목록 조회
     * @param 
     * @return 
     */

    @RequestMapping(value = "/api/systemSetting/selectSessionMonitoringServiceData", method = RequestMethod.POST)
    @ResponseBody
    public List<SessionServiceDataVO> selectSessionMonitoringServiceData() throws BaseException {
        List<SessionServiceDataVO> result = null;
        try {
            result = vsensorService.selectSessionMonitoringServiceData();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (result == null) {
            return new ArrayList<SessionServiceDataVO>();
        } else {
            return result;
        }
    }


    /*
     * 
     */
    @RequestMapping(value = "/api/systemSetting/selectDetectionPolicyGroup", method = RequestMethod.POST)
    @ResponseBody
    public List<DetectionPolicyVO> selectDetectionPolicyGroup() throws BaseException {
        List<DetectionPolicyVO> listData = null;
        try {
            listData = vsensorService.selectDetectionPolicyGroup();

            //View XSS(audit, detection) 처리
            for(DetectionPolicyVO tmp : listData) {
                tmp.setStrDescription(tmp.voCleanXSS(tmp.getStrDescription()));
                tmp.setStrSigRule(tmp.voCleanXSS(tmp.getStrSigRule()));
            }

        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<DetectionPolicyVO>();
        } else {
            return listData;
        }
    }

    @RequestMapping(value = "/api/systemSetting/selectDetectionException", method = RequestMethod.POST)
    @ResponseBody
    public List<DetectionExceptionVO> selectDetectionException(@RequestBody DetectionExceptionDto dto) throws BaseException {
        List<DetectionExceptionVO> resultList = null;
        try {
            resultList = vsensorService.selectDetectionException(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (resultList == null) {
            return new ArrayList<DetectionExceptionVO>();
        } else {
            return resultList;
        }
    }
    /*
     * 탐지예외 목록 수정/저장
     */

    @RequestMapping(value = "/api/systemSetting/updateDetectionException", method = RequestMethod.POST)
    @ResponseBody
    public void updateDetectionException(@RequestBody List<DetectionExceptionDto> dto, HttpSession session) throws BaseException {
        try {
            if (dto != null && dto.size() > 0) {
                vsensorService.updateDetectionException(dto, session);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 가상센서에 매핑되어 있는 네트워크 목록 조회
     */
    @RequestMapping(value = "/api/systemSetting/selectVsensorTargetNetworkList", method = RequestMethod.POST)
    @ResponseBody
    public List<NetworkVO> selectVsensorTargetNetworkList(@RequestBody NetworkDto dto) throws BaseException {
        List<NetworkVO> resultList = null;
        try {
            resultList = vsensorService.selectVsensorTargetNetworkList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return resultList;
    }

    /*
     * 예외 목록 삭제
     */
    @RequestMapping(value = "/api/systemSetting/deleteDetectionExceptionList", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDetectionExceptionList(@RequestBody List<DetectionExceptionDto> dto, HttpSession session) throws BaseException {
        try {
            if (dto != null && dto.size() > 0) {
                vsensorService.deleteDetectionExceptionList(dto, session);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 탐지 예외 신규 생성
     */
    @RequestMapping(value = "/api/systemSetting/insertDetectionExceptionList", method = RequestMethod.POST)
    @ResponseBody
    public void insertDetectionExceptionList(@RequestBody List<DetectionExceptionDto> dto, HttpSession session) throws BaseException {
        try {
            if (dto != null && dto.size() > 0) {
                vsensorService.insertDetectionExceptionList(dto);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 침입탐지 탐지예외 추가
     * 
     */
    @RequestMapping(value = "/api/systemSetting/updateMonitorIntrusionDetectionException", method = RequestMethod.POST)
    @ResponseBody
    public void updateMonitorIntrusionDetectionException(@RequestBody DetectionExceptionDto dto, HttpSession session) throws BaseException {
        //List<DetectionExceptionVO> detectionExceptionList = null;
        DetectionPolicyDto detection = new DetectionPolicyDto();
        detection.setlCode(dto.getlVioCode());
        try {
            vsensorService.updateMonitorIntrusionDetectionException(dto, (String) session.getAttribute("Username"));
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    
    /*
     * 탐지예외 소스IP, 목적IP 중복 검사 
     */
    @RequestMapping(value = "/api/systemSetting/isDuplicateDetectionException", method = RequestMethod.POST)
    @ResponseBody
    public DetectionExceptionVO isDuplicateDetectionException(@RequestBody DetectionExceptionDto dto) throws BaseException {
    	DetectionExceptionVO result = new DetectionExceptionVO();
        try {
        	result = vsensorService.isDuplicateDetectionException(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (result == null) {
            return new DetectionExceptionVO();
        } else {
            return result;
        }
    }    
}
