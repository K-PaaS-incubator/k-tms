package com.kglory.tms.web.controller.preferences;

import com.kglory.tms.web.common.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kglory.tms.web.model.vo.*;
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
import com.kglory.tms.web.model.dto.FilterViewApplicationDto;
import com.kglory.tms.web.model.dto.FilterViewDetectionDto;
import com.kglory.tms.web.model.dto.FilterViewFileMetaDto;
import com.kglory.tms.web.model.dto.FilterViewSessionDto;
import com.kglory.tms.web.services.preferences.FilterViewService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;
import java.util.Locale;
import javax.servlet.http.HttpSession;

@Controller
public class FilterViewController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    FilterViewService filterViewService;
    @Autowired
    AuditLogService auditLogSvc;

    private static Logger logger = LoggerFactory.getLogger(FilterViewController.class);

    @RequestMapping(value = "/api/filterView/selectFilterViewSession", method = RequestMethod.POST)
    @ResponseBody
    public List<FilterViewSessionVO> selectFilterViewSession(@RequestBody FilterViewSessionDto dto) throws BaseException {
        List<FilterViewSessionVO> listData = null;
        try {
            listData = filterViewService.selectFilterViewSession(dto);
            //View XSS(audit, detection) 처리
            for(FilterViewSessionVO tmp : listData) {
                tmp.setStrFilterViewName(tmp.voCleanXSS(tmp.getStrFilterViewName()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<FilterViewSessionVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    @RequestMapping(value = "/api/filterView/insertFilterViewSession", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertFilterViewSession(@RequestBody FilterViewSessionDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = filterViewService.insertFilterViewSession(dto);
            String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
            String sessionMsg = MessageUtil.getFilterViewEnableColor(dto.getnSessionEnabled(), dto.getnSessionColor());
            String serverIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnServerIpEnabled(), dto.getnServerIpColor());
            String clientIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnClientIpEnabled(), dto.getnClientIpColor());
            Integer service = dto.getnSession();
            String strSession = "";
            if (service != null) {
            	strSession = MessageUtil.getServiceName(service);
            }
            String msg = MessageUtil.getbuilMessage("audit.filterview.session", dto.getPathName() + pathMsg, strSession + sessionMsg,
                    StringUtil.stringNullToEmpty(dto.getStrServerIp()) + serverIpMsg, StringUtil.stringNullToEmpty(dto.getStrClientIp()) + clientIpMsg,
                    StringUtil.stringNullToEmpty(dto.getStrDescription()));
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_ADD_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lIndex=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertLIndex);
        return returnValue;
    }

    @RequestMapping(value = "/api/filterView/updateFilterViewSession", method = RequestMethod.POST)
    @ResponseBody
    public void updateFilterViewSession(@RequestBody FilterViewSessionDto dto, HttpSession session)
            throws BaseException {
        try {
            int chk = filterViewService.updateFilterViewSession(dto);
            if (chk > 0) {
                String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
                String sessionMsg = MessageUtil.getFilterViewEnableColor(dto.getnSessionEnabled(), dto.getnSessionColor());
                String serverIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnServerIpEnabled(), dto.getnServerIpColor());
                String clientIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnClientIpEnabled(), dto.getnClientIpColor());
                Integer service = dto.getnSession();
                String strSession = "";
                if (service != null) {
                    strSession = MessageUtil.getServiceName(service);
                }
                String msg = MessageUtil.getbuilMessage("audit.filterview.session", dto.getPathName() + pathMsg, strSession + sessionMsg,
                        StringUtil.stringNullToEmpty(dto.getStrServerIp()) + serverIpMsg, StringUtil.stringNullToEmpty(dto.getStrClientIp()) + clientIpMsg,
                        StringUtil.stringNullToEmpty(dto.getStrDescription()));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_MOD_SUCCESS, (String) session.getAttribute("Username"),
                        messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/deleteFilterViewSession", method = RequestMethod.POST)
    @ResponseBody
    public void deleteFilterViewSession(@RequestBody FilterViewSessionDto dto, HttpSession session) throws BaseException {
        try {
            filterViewService.deleteFilterViewSession(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_DEL_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.session", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/selectFilterViewDetection", method = RequestMethod.POST)
    @ResponseBody
    public List<FilterViewDetectionVO> selectFilterViewDetection(@RequestBody FilterViewDetectionDto dto) throws BaseException {
        List<FilterViewDetectionVO> listData = null;
        try {
            listData = filterViewService.selectFilterViewDetection(dto);

            //View XSS(audit, detection) 처리
            for(FilterViewDetectionVO tmp : listData) {
                tmp.setStrFilterViewName(tmp.voCleanXSS(tmp.getStrFilterViewName()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<FilterViewDetectionVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    @RequestMapping(value = "/api/filterView/insertFilterViewDetection", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertFilterViewDetection(@RequestBody FilterViewDetectionDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = filterViewService.insertFilterViewDetection(dto);
            String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
            String attackNameMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackNameEnabled(), dto.getnAttackNameColor());
            String attackTypeMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackTypeEnabled(), dto.getnAttackTypeColor());
            String attackIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackIpEnabled(), dto.getnAttackIpColor());
            String victimIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnVictimIpEnabled(), dto.getnVictimIpColor());
            String attackPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackPortEnabled(), dto.getnAttackPortColor());
            String victimPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnVictimPortEnabled(), dto.getnVictimPortColor());
            String inbountMsg = MessageUtil.getFilterViewEnableColor(dto.getnWinboundEnabled(), dto.getnWinboundColor());
            String severityMsg = MessageUtil.getFilterViewEnable(dto.getnSeverityEnabled());
            String blockMsg = MessageUtil.getFilterViewEnableColor(dto.getnBlockEnabled(), dto.getnBlockColor());
            String attackPort = "";
            if (dto.getnAttackPort() != null) {
                attackPort = String.valueOf(dto.getnAttackPort());
            }
            String victimPort = "";
            if (dto.getnVictimPort() != null) {
                victimPort = String.valueOf(dto.getnVictimPort());
            }
            
            String strWin = "";
            if(dto.getnWinbound() == null) {
            	strWin = "";
            }
            else {
            	strWin = dto.getnWinbound()+"";
            }
            String strWBlock = "";
            if(dto.getnBlock() == null) {
            	strWBlock = "";
            }
            else {
            	strWBlock = dto.getnBlock()+"";
            }
            
            String msg = MessageUtil.getbuilMessage("audit.filterview.detect", dto.getPathName() + pathMsg, dto.getStrAttackName() + attackNameMsg, dto.getnAttackTypeName() + attackTypeMsg,
                    dto.getnAttackIp() + attackIpMsg, dto.getnVictimIp() + victimIpMsg, attackPort + attackPortMsg, victimPort + victimPortMsg, MessageUtil.getMessage("winbound.level" + strWin) + inbountMsg,
                    MessageUtil.getMessage("severity.level" + dto.getStrSeverity()) + severityMsg, MessageUtil.getMessage("block.level" + strWBlock) + blockMsg, dto.getStrDescription());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_ADD_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lIndex" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertLIndex);
        return returnValue;
    }

    @RequestMapping(value = "/api/filterView/updateFilterViewDetection", method = RequestMethod.POST)
    @ResponseBody
    public void updateFilterViewDetection(@RequestBody FilterViewDetectionDto dto, HttpSession session)
            throws BaseException {
        try {
            int chk = filterViewService.updateFilterViewDetection(dto);
            if (chk > 0) {
                String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
                String attackNameMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackNameEnabled(), dto.getnAttackNameColor());
                String attackTypeMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackTypeEnabled(), dto.getnAttackTypeColor());
                String attackIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackIpEnabled(), dto.getnAttackIpColor());
                String victimIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnVictimIpEnabled(), dto.getnVictimIpColor());
                String attackPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnAttackPortEnabled(), dto.getnAttackPortColor());
                String victimPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnVictimPortEnabled(), dto.getnVictimPortColor());
                String inbountMsg = MessageUtil.getFilterViewEnableColor(dto.getnWinboundEnabled(), dto.getnWinboundColor());
                String severityMsg = MessageUtil.getFilterViewEnable(dto.getnSeverityEnabled());
                String blockMsg = MessageUtil.getFilterViewEnableColor(dto.getnBlockEnabled(), dto.getnBlockColor());
                String attackPort = "";
                if (dto.getnAttackPort() != null) {
                    attackPort = String.valueOf(dto.getnAttackPort());
                }
                String victimPort = "";
                if (dto.getnVictimPort() != null) {
                    victimPort = String.valueOf(dto.getnVictimPort());
                }
                
                String strWin = "";
                if(dto.getnWinbound() == null) {
                	strWin = "";
                }
                else {
                	strWin = dto.getnWinbound()+"";
                }
                String strWBlock = "";
                if(dto.getnBlock() == null) {
                	strWBlock = "";
                }
                else {
                	strWBlock = dto.getnBlock()+"";
                }
                
                String msg = MessageUtil.getbuilMessage("audit.filterview.detect", dto.getPathName() + pathMsg, dto.getStrAttackName() + attackNameMsg, dto.getnAttackTypeName() + attackTypeMsg,
                        dto.getnAttackIp() + attackIpMsg, dto.getnVictimIp() + victimIpMsg, attackPort + attackPortMsg, victimPort + victimPortMsg, MessageUtil.getMessage("winbound.level" + strWin) + inbountMsg,
                        MessageUtil.getMessage("severity.level" + dto.getStrSeverity()) + severityMsg, MessageUtil.getMessage("block.level" + strWBlock) + blockMsg, dto.getStrDescription());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_MOD_SUCCESS, (String) session.getAttribute("Username"),
                        messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/deleteFilterViewDetection", method = RequestMethod.POST)
    @ResponseBody
    public void deleteFilterViewDetection(@RequestBody FilterViewDetectionDto dto, HttpSession session)
            throws BaseException {
        try {
            filterViewService.deleteFilterViewDetection(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_DEL_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.detection", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/selectFilterViewApplication", method = RequestMethod.POST)
    @ResponseBody
    public List<FilterViewApplicationVO> selectFilterViewApplication(@RequestBody FilterViewApplicationDto dto) throws BaseException {
        List<FilterViewApplicationVO> listData = null;
        try {
            listData = filterViewService.selectFilterViewApplication(dto);
            //View XSS(audit, detection) 처리
            for(FilterViewApplicationVO tmp : listData) {
                tmp.setStrFilterViewName(tmp.voCleanXSS(tmp.getStrFilterViewName()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<FilterViewApplicationVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    @RequestMapping(value = "/api/filterView/insertFilterViewApplication", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertFilterViewApplication(@RequestBody FilterViewApplicationDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = filterViewService.insertFilterViewApplication(dto);
            String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
            String appTypeMsg = MessageUtil.getFilterViewEnableColor(dto.getnAppTypeEnabled(), dto.getnAppTypeColor());
            String srcIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcIpEnabled(), dto.getnSrcIpColor());
            String srcPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcPortEnabled(), dto.getnSrcPortColor());
            String dstIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstIpEnabled(), dto.getnDstIpColor());
            String dstPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstPortEnabled(), dto.getnDstPortColor());
            
            String msg = MessageUtil.getbuilMessage("audit.filterview.application", dto.getPathName() + pathMsg, MessageUtil.getMessage("appType.level" + dto.getnAppType()) + appTypeMsg,
                    StringUtil.stringNullToEmpty(dto.getnSrcIp()) + srcIpMsg, StringUtil.stringNullToEmpty(dto.getnDstIp()) + dstIpMsg,
                    StringUtil.intNullToEmpty(dto.getnSrcPort()) + srcPortMsg, StringUtil.intNullToEmpty(dto.getnDstPort()) + dstPortMsg, StringUtil.stringNullToEmpty(dto.getStrDescription()));
            
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_ADD_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lIndex=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertLIndex);
        return returnValue;
    }

    @RequestMapping(value = "/api/filterView/updateFilterViewApplication", method = RequestMethod.POST)
    @ResponseBody
    public void updateFilterViewApplication(@RequestBody FilterViewApplicationDto dto, HttpSession session)
            throws BaseException {
        try {
            int chk = filterViewService.updateFilterViewApplication(dto);
            if (chk > 0) {
                String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
                String appTypeMsg = MessageUtil.getFilterViewEnableColor(dto.getnAppTypeEnabled(), dto.getnAppTypeColor());
                String srcIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcIpEnabled(), dto.getnSrcIpColor());
                String srcPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcPortEnabled(), dto.getnSrcPortColor());
                String dstIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstIpEnabled(), dto.getnDstIpColor());
                String dstPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstPortEnabled(), dto.getnDstPortColor());
                String msg = MessageUtil.getbuilMessage("audit.filterview.application", dto.getPathName() + pathMsg, MessageUtil.getMessage("appType.level" + dto.getnAppType()) + appTypeMsg,
                        StringUtil.stringNullToEmpty(dto.getnSrcIp()) + srcIpMsg, StringUtil.stringNullToEmpty(dto.getnDstIp()) + dstIpMsg,
                        StringUtil.intNullToEmpty(dto.getnSrcPort()) + srcPortMsg, StringUtil.intNullToEmpty(dto.getnDstPort()) + dstPortMsg, StringUtil.stringNullToEmpty(dto.getStrDescription()));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_MOD_SUCCESS, (String) session.getAttribute("Username"),
                        messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/deleteFilterViewApplication", method = RequestMethod.POST)
    @ResponseBody
    public void deleteFilterViewApplication(@RequestBody FilterViewApplicationDto dto, HttpSession session)
            throws BaseException {
        try {
            filterViewService.deleteFilterViewApplication(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_DEL_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.application", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/selectFilterViewFileMeta", method = RequestMethod.POST)
    @ResponseBody
    public List<FilterViewFileMetaVO> selectFilterViewFileMeta(@RequestBody FilterViewFileMetaDto dto) throws BaseException {
        List<FilterViewFileMetaVO> listData = null;
        try {
            listData = filterViewService.selectFilterViewFileMeta(dto);
            //View XSS(audit, detection) 처리
            for(FilterViewFileMetaVO tmp : listData) {
                tmp.setStrFilterViewName(tmp.voCleanXSS(tmp.getStrFilterViewName()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (listData == null) {
            return new ArrayList<FilterViewFileMetaVO>();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : " + StringUtil.listObjcetToString(listData));
        }

        return listData;
    }

    @RequestMapping(value = "/api/filterView/insertFilterViewFileMeta", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertFilterViewFileMeta(@RequestBody FilterViewFileMetaDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = filterViewService.insertFilterViewFileMeta(dto);
            String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
            String fileNameMsg = MessageUtil.getFilterViewEnableColor(dto.getnFileNameEnabled(), dto.getnFileNameColor());
            String srcIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcIpEnabled(), dto.getnSrcIpColor());
            String srcPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcPortEnabled(), dto.getnSrcPortColor());
            String dstIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstIpEnabled(), dto.getnDstIpColor());
            String dstPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstPortEnabled(), dto.getnDstPortColor());
            String msg = MessageUtil.getbuilMessage("audit.filterview.filemeta", dto.getPathName() + pathMsg, dto.getStrFileName() + fileNameMsg,
                    StringUtil.stringNullToEmpty(dto.getnSrcIp()) + srcIpMsg, StringUtil.stringNullToEmpty(dto.getnDstIp()) + dstIpMsg,
                    StringUtil.intNullToEmpty(dto.getnSrcPort()) + srcPortMsg, StringUtil.intNullToEmpty(dto.getnDstPort()) + dstPortMsg, StringUtil.stringNullToEmpty(dto.getStrDescription()));
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_ADD_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_ADD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lIndex=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertLIndex);
        return returnValue;
    }

    @RequestMapping(value = "/api/filterView/updateFilterViewFileMeta", method = RequestMethod.POST)
    @ResponseBody
    public void updateFilterViewFileMeta(@RequestBody FilterViewFileMetaDto dto, HttpSession session) throws BaseException {
        try {
            int chk = filterViewService.updateFilterViewFileMeta(dto);
            if (chk > 0) {
                String pathMsg = MessageUtil.getFilterViewEnableColor(dto.getnTargetEnabled(), dto.getnTargetColor());
                String fileNameMsg = MessageUtil.getFilterViewEnableColor(dto.getnFileNameEnabled(), dto.getnFileNameColor());
                String srcIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcIpEnabled(), dto.getnSrcIpColor());
                String srcPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnSrcPortEnabled(), dto.getnSrcPortColor());
                String dstIpMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstIpEnabled(), dto.getnDstIpColor());
                String dstPortMsg = MessageUtil.getFilterViewEnableColor(dto.getnDstPortEnabled(), dto.getnDstPortColor());
                String msg = MessageUtil.getbuilMessage("audit.filterview.filemeta", dto.getPathName() + pathMsg, dto.getStrFileName() + fileNameMsg,
                        StringUtil.stringNullToEmpty(dto.getnSrcIp()) + srcIpMsg, StringUtil.stringNullToEmpty(dto.getnDstIp()) + dstIpMsg,
                        StringUtil.intNullToEmpty(dto.getnSrcPort()) + srcPortMsg, StringUtil.intNullToEmpty(dto.getnDstPort()) + dstPortMsg, StringUtil.stringNullToEmpty(dto.getStrDescription()));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_MOD_SUCCESS, (String) session.getAttribute("Username"),
                        messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_MOD_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    @RequestMapping(value = "/api/filterView/deleteFilterViewFileMeta", method = RequestMethod.POST)
    @ResponseBody
    public void deleteFilterViewFileMeta(@RequestBody FilterViewFileMetaDto dto, HttpSession session) throws BaseException {
        try {
            filterViewService.deleteFilterViewFileMeta(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.FILTERVIEW_DEL_SUCCESS, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.FILTERVIEW_DEL_FAIL, (String) session.getAttribute("Username"),
                    messageSource.getMessage("filterview.fileMeta", null, Locale.getDefault()), dto.getStrFilterViewName());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    /**
     * 침입탐지 필터명 중복 검사
     */
    @RequestMapping(value = "/api/filterView/isDuplicateDetectionFilterName", method = RequestMethod.POST)
    @ResponseBody
    public FilterViewDetectionVO isDuplicateDetectionFilterName(@RequestBody FilterViewDetectionDto dto) throws BaseException {
        FilterViewDetectionVO result = new FilterViewDetectionVO();
        logger.debug("dto : " + dto.toMultiLineString());
        try {
            result = filterViewService.isDuplicateDetectionFilterName(dto);
            logger.debug("result : " + result.toMultiLineString());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 세션 필터명 중복 검사
     */
    @RequestMapping(value = "/api/filterView/isDuplicateSessionFilterName", method = RequestMethod.POST)
    @ResponseBody
    public FilterViewSessionVO isDuplicateSessionFilterName(@RequestBody FilterViewSessionDto dto) {
        FilterViewSessionVO result = new FilterViewSessionVO();
        try {
            result = filterViewService.isDuplicateSessionFilterName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * Application Layer 필터명 중복 검사
     */
    @RequestMapping(value = "/api/filterView/isDuplicateApplayerFilterName", method = RequestMethod.POST)
    @ResponseBody
    public FilterViewApplicationVO isDuplicateApplayerFilterName(@RequestBody FilterViewApplicationDto dto) {
        FilterViewApplicationVO result = new FilterViewApplicationVO();
        try {
            result = filterViewService.isDuplicateApplayerFilterName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * FileMeta 필터명 중복 검사
     */
    @RequestMapping(value = "/api/filterView/isDuplicateFileMetaFilterName", method = RequestMethod.POST)
    @ResponseBody
    public FilterViewFileMetaVO isDuplicateFileMetaFilterName(@RequestBody FilterViewFileMetaDto dto) {
        FilterViewFileMetaVO result = new FilterViewFileMetaVO();
        try {
            result = filterViewService.isDuplicateFileMetaFilterName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;

    }
}
