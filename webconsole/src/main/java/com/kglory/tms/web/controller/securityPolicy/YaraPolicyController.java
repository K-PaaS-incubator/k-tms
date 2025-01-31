package com.kglory.tms.web.controller.securityPolicy;

import com.kglory.tms.web.common.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.kglory.tms.web.model.dto.YaraRuleDto;
import com.kglory.tms.web.model.vo.YaraRuleVo;
import com.kglory.tms.web.services.securityPolicy.YaraPolicyService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;

import java.math.BigInteger;

@Controller
public class YaraPolicyController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    YaraPolicyService yaraPolicyService;
    @Autowired
    AuditLogService auditLogSvc;

    private static Logger logger = LoggerFactory.getLogger(YaraPolicyController.class);

    /**
     * 악성코드 신규 생성시 인덱스 셋팅
     *
     * @param YaraRuleDto dto
     * @return insertIndex
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/insertYaraRule", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertYaraRule(@RequestBody YaraRuleDto dto, HttpSession session) throws BaseException {
        long insertIndex = 0;
        try {
            insertIndex = dto.setlIndex(yaraPolicyService.selectYaraRuleLastIndex());
            String msg = MessageUtil.getbuilMessage("audit.policy.yara.user", dto.getGroupName(), MessageUtil.getMessage("sig.severity.level" + dto.getsSeverity()),
                    MessageUtil.getMessage("str.used" + dto.getlUsed()), dto.getMeta(), dto.getStrings(), dto.getCondition());
            yaraPolicyService.insertYaraRule(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_USER_ADD_SUCCESS,
                    (String) session.getAttribute("Username"), dto.getRuleName(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_YARA_USER_ADD_FAIL,
                    (String) session.getAttribute("Username"), dto.getRuleName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_YARA_USER_ADD_FAIL,
                    (String) session.getAttribute("Username"), dto.getRuleName());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertIndex);

        return returnValue;
    }

    /**
     * 악성코드 변경/저장
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/updateYaraRule", method = RequestMethod.POST)
    @ResponseBody
    public void updateYaraRule(@RequestBody YaraRuleDto dto, HttpSession session) throws BaseException {
        try {
            String msg = MessageUtil.getbuilMessage("audit.policy.yara.user", dto.getGroupName(), MessageUtil.getMessage("sig.severity.level" + dto.getsSeverity()),
                    MessageUtil.getMessage("str.used" + dto.getlUsed()), dto.getMeta(), dto.getStrings(), dto.getCondition());
            boolean chk = yaraPolicyService.updateYaraRule(dto);
            if (chk) {
                BigInteger auditCode = BigInteger.valueOf(0);
                if (dto.getGroupIndex() >= 99) {
                    auditCode = Constants.POLICY_YARA_USER_MOD_SUCCESS;
                } else {
                    auditCode = Constants.POLICY_YARA_MOD_SUCCESS;
                }
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), auditCode,
                        (String) session.getAttribute("Username"), dto.getRuleName(), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            BigInteger auditCode = BigInteger.valueOf(0);
            if (dto.getGroupIndex() >= 99) {
                auditCode = Constants.POLICY_YARA_USER_MOD_FAIL;
            } else {
                auditCode = Constants.POLICY_YARA_MOD_FAIL;
            }
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), auditCode,
                    (String) session.getAttribute("Username"), dto.getRuleName(), "");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            BigInteger auditCode = BigInteger.valueOf(0);
            if (dto.getGroupIndex() >= 99) {
                auditCode = Constants.POLICY_YARA_USER_MOD_FAIL;
            } else {
                auditCode = Constants.POLICY_YARA_MOD_FAIL;
            }
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), auditCode,
                    (String) session.getAttribute("Username"), dto.getRuleName(), "");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    /**
     * 악성코드 전체 목록 조회
     *
     * @param dto
     * @return List<YaraRuleVo>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectYaraRuleList", method = RequestMethod.POST)
    @ResponseBody
    public List<YaraRuleVo> selectYaraRuleList(@RequestBody YaraRuleDto dto) throws BaseException {

        List<YaraRuleVo> resultData = null;
        try {
            resultData = yaraPolicyService.selectYaraRuleList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (resultData == null) {
            return new ArrayList<YaraRuleVo>();
        }
        return resultData;
    }

    /**
     * 악성코드 그룹 유형 조회
     *
     * @param dto
     * @return List<YaraRuleVo>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/getYaraGroupList", method = RequestMethod.POST)
    @ResponseBody
    public List<YaraRuleVo> getYaraGroupList(@RequestBody YaraRuleDto dto) throws BaseException {
        List<YaraRuleVo> resultData = null;
        try {
            resultData = yaraPolicyService.getYaraGroupList(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (resultData == null) {
            return new ArrayList<YaraRuleVo>();
        }
        return resultData;
    }

    /**
     * 악성코드 목록의 상세 정보를 조회
     *
     * @param dto
     * @return YaraRuleVo
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectYaraRuleDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public YaraRuleVo selectYaraRuleDetailInfo(@RequestBody YaraRuleDto dto) throws BaseException {
        YaraRuleVo resultData = new YaraRuleVo();
        try {
            resultData = yaraPolicyService.selectYaraRuleDetailInfo(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }

    /**
     * 사용자 정의 악성코드 유형 삭제
     *
     * @param dto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/deleteYaraRuleGroupType", method = RequestMethod.POST)
    @ResponseBody
    public void deleteSignatureClassType(@RequestBody YaraRuleDto dto, HttpSession session) throws BaseException {
        try {
            List<YaraRuleVo> result = yaraPolicyService.selectYaraRuleList(dto);
            if (result != null && result.size() > 0) {
                for(YaraRuleVo item : result) {
                    YaraRuleDto pa = new YaraRuleDto();
                    pa.setlIndex(item.getlIndex());
                    pa.setRuleName(item.getRuleName());
                    deleteYaraUserRule(pa, session);
                }
            }
            yaraPolicyService.deleteYaraRuleGroupType(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_GROUP_DEL_SUCCESS,
                    (String) session.getAttribute("Username"), dto.getGroupName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_GROUP_DEL_FAIL,
                    (String) session.getAttribute("Username"), dto.getGroupName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_GROUP_DEL_FAIL,
                    (String) session.getAttribute("Username"), dto.getGroupName());
        }
    }

    /**
     * 악성코드 사용자 정의 그룹 생성
     *
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/insertYaraGroup", method = RequestMethod.POST)
    @ResponseBody
    public void insertYaraGroup(@RequestBody YaraRuleDto dto, HttpSession session) throws BaseException {
        try {
            yaraPolicyService.insertYaraGroup(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_GROUP_ADD_SUCCESS,
                    (String) session.getAttribute("Username"), dto.getGroupName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_GROUP_ADD_FAIL,
                    (String) session.getAttribute("Username"), dto.getGroupName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_GROUP_ADD_FAIL,
                    (String) session.getAttribute("Username"), dto.getGroupName());
        }
    }

    /**
     * 악성코드 사용자룰 삭제
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/deleteYaraUserRule", method = RequestMethod.POST)
    @ResponseBody
    public void deleteYaraUserRule(@RequestBody YaraRuleDto dto, HttpSession session) throws BaseException {
        try {
            yaraPolicyService.deleteYaraUserRule(dto);

            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_USER_DEL_SUCCESS,
                    (String) session.getAttribute("Username"), dto.getRuleName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_USER_DEL_FAIL,
                    (String) session.getAttribute("Username"), dto.getRuleName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_YARA_USER_DEL_FAIL,
                    (String) session.getAttribute("Username"), dto.getRuleName());
        }
    }

    /**
     * 악성코드 사용자 그룹 유형 목록을 조회
     *
     * @param dto
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectYaraRuleGroupType", method = RequestMethod.POST)
    @ResponseBody
    public List<YaraRuleVo> selectYaraRuleGroupType(@RequestBody YaraRuleDto dto) throws BaseException {
        List<YaraRuleVo> resultData = null;
        try {
            resultData = yaraPolicyService.selectYaraRuleGroupType(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return resultData;
    }

    /**
     * 악성코드 명 중복 검사
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/isDuplicateYaraRuleName", method = RequestMethod.POST)
    @ResponseBody
    public YaraRuleVo isDuplicateYaraRuleName(@RequestBody YaraRuleDto dto) throws BaseException {
        YaraRuleVo result = new YaraRuleVo();
        try {
            result = yaraPolicyService.isDuplicateYaraRuleName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;

    }

    /**
     * 악성코드 그룹 인덱스 조회
     *
     * @return YaraRuleVo
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectYaraGroupIndex", method = RequestMethod.POST)
    @ResponseBody
    public YaraRuleVo selectYaraGroupIndex() throws BaseException {
        YaraRuleVo result = new YaraRuleVo();
        try {
            result = yaraPolicyService.selectYaraGroupIndex();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return result;
    }
}
