package com.kglory.tms.web.controller.securityPolicy;

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
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.CommonBean.ReturnType;
import com.kglory.tms.web.model.dto.AuditLogPolicyDto;
import com.kglory.tms.web.services.securityPolicy.AuditLogPolicyService;
import com.kglory.tms.web.util.StringUtil;
import java.util.Locale;

@Controller
public class AuditLogPolicyController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    AuditLogPolicyService auditLogPolicyService;

    private static Logger logger = LoggerFactory.getLogger(AuditLogPolicyController.class);

    /**
     * 감사로그 행위 센서 리스트 조회
     *
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectAuditLogPolicyActionSensorList", method = RequestMethod.POST)
    @ResponseBody
    public List<AuditLogPolicyDto> selectAuditLogPolicyActionSensorList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;

        try {
            resultList = auditLogPolicyService.selectAuditLogPolicyActionSensorList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return resultList;
    }

    /**
     * 감사로그 행위 콘솔 리스트 조회
     *
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectAuditLogPolicyActionConsoleList", method = RequestMethod.POST)
    @ResponseBody
    public List<AuditLogPolicyDto> selectAuditLogPolicyActionConsoleList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;

        try {
            resultList = auditLogPolicyService.selectAuditLogPolicyActionConsoleList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return resultList;
    }

    /**
     * 감사로그 오류 -센서 리스트 조회
     *
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectAuditLogPolicyErrorSensorList", method = RequestMethod.POST)
    @ResponseBody
    public List<AuditLogPolicyDto> selectAuditLogPolicyErrorSensorList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;

        try {
            resultList = auditLogPolicyService.selectAuditLogPolicyErrorSensorList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return resultList;
    }

    /**
     * 감사로그 오류 -콘솔 리스트 조회
     *
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectAuditLogPolicyErrorConsoleList", method = RequestMethod.POST)
    @ResponseBody
    public List<AuditLogPolicyDto> selectAuditLogPolicyErrorConsoleList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;

        try {
            resultList = auditLogPolicyService.selectAuditLogPolicyErrorConsoleList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return resultList;
    }

    /**
     * 감사로그 경고 센서 리스트 조회
     *
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectAuditLogPolicyWarningSensorList", method = RequestMethod.POST)
    @ResponseBody
    public List<AuditLogPolicyDto> selectAuditLogPolicyWarningSensorList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;

        try {
            resultList = auditLogPolicyService.selectAuditLogPolicyWarningSensorList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return resultList;
    }

    @RequestMapping(value = "/api/securityPolicy/selectAuditLogPolicyWarningConsoleList", method = RequestMethod.POST)
    @ResponseBody
    public List<AuditLogPolicyDto> selectAuditLogPolicyWarningConsoleList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;

        try {
            resultList = auditLogPolicyService.selectAuditLogPolicyWarningConsoleList();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

        return resultList;
    }

    /*
     * 감사로그 행위 리스트 업데이트
     */
    @RequestMapping(value = "/api/securityPolicy/updateAuditLogPolicyActionList", method = RequestMethod.POST)
    @ResponseBody
    public void updateAuditLogPolicyActionList(@RequestBody List<AuditLogPolicyDto> dto, HttpSession session) throws BaseException {

        try {
            auditLogPolicyService.updateAuditLogPolicyList(dto, session);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    /*
     * 감사로그 오류 리스트 업데이트
     */
    @RequestMapping(value = "/api/securityPolicy/updateAuditLogPolicyErrorList", method = RequestMethod.POST)
    @ResponseBody
    public void updateAuditLogPolicyErrorList(@RequestBody List<AuditLogPolicyDto> dto, HttpSession session) throws BaseException {

        try {
            Integer updateCount = auditLogPolicyService.updateAuditLogPolicyList(dto, session);
//            if (dto != null && dto.size() > 0 && dto.size() == updateCount) {
//                // success
//            } else {
//                // fail
//            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    /*
     * 감사로그 경고 리스트 업데이트
     */
    @RequestMapping(value = "/api/securityPolicy/updateAuditLogPolicyWarningList", method = RequestMethod.POST)
    @ResponseBody
    public void updateAuditLogPolicyWarningList(@RequestBody List<AuditLogPolicyDto> dto, HttpSession session) throws BaseException {

        try {
            auditLogPolicyService.updateAuditLogPolicyList(dto, session);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

    }

}
