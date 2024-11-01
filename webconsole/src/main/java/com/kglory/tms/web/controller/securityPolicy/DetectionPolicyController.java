package com.kglory.tms.web.controller.securityPolicy;

import com.kglory.tms.web.common.Constants;
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
import com.kglory.tms.web.model.dto.DetectionPolicyDto;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.model.vo.VsensorVO;
import com.kglory.tms.web.services.securityPolicy.DetectionPolicyService;
import com.kglory.tms.web.services.systemSettings.VsensorService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class DetectionPolicyController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    DetectionPolicyService detectionPolicyService;
    @Autowired
    VsensorService vSensorSvc;
    @Autowired
    AuditLogService auditLogSvc;

    @Resource(name = "downloadView")
    private View downloadView;
    @Resource(name = "detectionPolicyDownload")
    private View detectionPolicyDownload;

    private static Logger logger = LoggerFactory.getLogger(DetectionPolicyController.class);

    /** 
     * 그룹별 룰 select 
     * 그룹유형에 해당하는 탐지정책 목록을 조회한다. 
     * @param DetectionPolicyDto dto
     * @return List<DetectionPolicyVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectSignaturePerGroup", method = RequestMethod.POST)
    @ResponseBody
    public List<DetectionPolicyVO> selectSignaturePerGroup(@RequestBody DetectionPolicyDto dto) throws BaseException {

        List<DetectionPolicyVO> resultData = null;
        try {
            resultData = detectionPolicyService.selectSignaturePerGroup(dto);
            
            //View XSS(audit, detection) 처리
			for(DetectionPolicyVO tmp : resultData) {
				tmp.setStrDescription(tmp.voCleanXSS(tmp.getStrDescription()));
				tmp.setStrSigRule(tmp.voCleanXSS(tmp.getStrSigRule()));
			}
			
			
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (resultData == null) {
            return new ArrayList<DetectionPolicyVO>();
        }
        return resultData;
    }
    
    /**
     * 룰 select 
     * 탐지정책 목록을 조회한다.
     * @param DetectionPolicyDto dto
     * @return List<DetectionPolicyVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectDetectionPolicy", method = RequestMethod.POST)
    @ResponseBody
    public List<DetectionPolicyVO> selectDetectionPolicy(@RequestBody DetectionPolicyDto dto) throws BaseException {

        List<DetectionPolicyVO> resultData = null;
        try {
            resultData = detectionPolicyService.selectDetectionPolicy(dto);
          //View XSS(audit, detection) 처리
			for(DetectionPolicyVO tmp : resultData) {
				tmp.setStrDescription(tmp.voCleanXSS(tmp.getStrDescription()));
				tmp.setStrSigRule(tmp.voCleanXSS(tmp.getStrSigRule()));
			}
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        if (resultData == null) {
            return new ArrayList<DetectionPolicyVO>();
        } else {
            return resultData;
        }
    }

    /**
     * 상세정보 select 
     * 시그니처별 상세 정보를 조회한다.
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectDetectionPolicyDetail", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectDetectionPolicyDetail(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO resultData = new DetectionPolicyVO();
        try {
            resultData = detectionPolicyService.selectDetectionPolicyDetail(dto);
            
            //View XSS(audit, detection) 처리
            resultData.setStrDescription(resultData.voCleanXSS(resultData.getStrDescription()));
            resultData.setStrSigRule(resultData.voCleanXSS(resultData.getStrSigRule()));
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }
    
    /**
     * 도움말 정보 select  
     * 시그니처별 도움말 정보를 조회한다.
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectDetectionPolicyHelp", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectDetectionPolicyHelp(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO resultData = new DetectionPolicyVO();
        try {
            resultData = detectionPolicyService.selectDetectionPolicyHelp(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }

    
    /** 
     * 벤더 정책 update 
     * 유관기관정책 상세 정보를 수정한다. 
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/updateDetectionPolicy", method = RequestMethod.POST)
    @ResponseBody
    public void updateDetectionPolicy(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        try {
            boolean chk = detectionPolicyService.updateDetectionPolicy(dto);
            if (chk) {
                String response = "0";
                String block = "0";
                if(dto.getlResponseBool()) {
                    response = "1";
                }
                if(dto.getlBlockBool()) {
                    block = "1";
                }
                String msg = MessageUtil.getbuilMessage("audit.policy.detection.vender", MessageUtil.getMessage("str.used" + dto.getlUsed()), MessageUtil.getMessage("str.used" + block), 
                        MessageUtil.getMessage("str.used" + response), dto.getlThresholdNumValue(), dto.getlThresholdTimeValue());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getStrDescription(), msg);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
                logger.debug("result : " + chk);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
        }
    }

    /**
     * 유형그룹 select
     * 공격유형 정보를 가져온다.
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectAttackTypeSelect", method = RequestMethod.POST)
    @ResponseBody
    public List<DetectionPolicyVO> selectAttackTypeSelect() throws BaseException {

        List<DetectionPolicyVO> resultData = null;
        try {
            resultData = detectionPolicyService.selectAttackTypeSelect();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } 
        return resultData;

    }

    /**
     * 사용자룰 insert
     * 사용자 정의정책 신규생성
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/insertUserSignature", method = RequestMethod.POST)
    @ResponseBody
    public long insertUserSignature(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        long lcode = 0;
        try {
            String rule = dto.getStrTitle();
            lcode = detectionPolicyService.insertUserSignature(dto);
            String response = "0";
            String block = "0";
            if(dto.getlResponseBool()) {
                response = "1";
            }
            if(dto.getlBlockBool()) {
                block = "1";
            }
            String msg = MessageUtil.getbuilMessage("audit.policy.detection.user", dto.getsClassType(), MessageUtil.getMessage("sig.severity.level" + dto.getsSeverity()), 
                    MessageUtil.getMessage("str.used" + dto.getlUsed()), MessageUtil.getMessage("str.used" + block), 
                    MessageUtil.getMessage("str.used" + response), dto.getlThresholdNumValue(), dto.getlThresholdTimeValue(), rule);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_USER_ADD_SUCCESS, 
                    (String) session.getAttribute("Username"), dto.getStrDescription(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_USER_ADD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_USER_ADD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lcode=" + lcode);
        }
        
        return lcode;
    }

    /*
     * 사용자 정의정책 도움말 신규생성
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/updateUserSignatureHelp", method = RequestMethod.POST)
    @ResponseBody
    public void updateUserSignatureHelp(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        try {
            detectionPolicyService.updateUserSignatureHelp(dto);
            String msg = MessageUtil.getbuilMessage("audit.policy.detection.user.hel", dto.getStrSummary(), dto.getStrDescription(), dto.getStrSolution(),
                        dto.getStrReference(), dto.getStrbId(), dto.getStrCveId(), dto.getStrVul(), dto.getStrFalsePositive(), dto.getStrNotVul(), dto.getStrAddrsPoof());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_USER_HELP_ADD_SUCCESS, 
                    (String) session.getAttribute("Username"), dto.getStrTitle(), msg);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 사용자 정의 정책(유저룰) 수정
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/updateUserSignature", method = RequestMethod.POST)
    @ResponseBody
    public void updateUserSignature(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        try {
            boolean chk = detectionPolicyService.updateUserSignature(dto);
            if (chk) {
                String msg = MessageUtil.getbuilMessage("audit.policy.detection.user.hel", dto.getStrSummary(), dto.getStrDescription(), dto.getStrSolution(),
                        dto.getStrReference(), dto.getStrbId(), dto.getStrCveId(), dto.getStrVul(), dto.getStrFalsePositive(), dto.getStrNotVul(), dto.getStrAddrsPoof());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_USER_HELP_MOD_SUCCESS, 
                        (String) session.getAttribute("Username"), dto.getStrTitle(), msg);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
                logger.debug("result : " + chk);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    /*
     * 사용자 정의 정책 LCODE 범위에 따라 update
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/updateUserSignatureDetail", method = RequestMethod.POST)
    @ResponseBody
    public void updateUserSignatureDetail(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        try {
            String response = "0";
            String block = "0";
            if(dto.getlResponseBool()) {
                response = "1";
            }
            if(dto.getlBlockBool()) {
                block = "1";
            }
            String msg = MessageUtil.getbuilMessage("audit.policy.detection.user", dto.getsClassName(), MessageUtil.getMessage("sig.severity.level" + dto.getsSeverity()), 
                    MessageUtil.getMessage("str.used" + dto.getlUsed()), MessageUtil.getMessage("str.used" + block), 
                    MessageUtil.getMessage("str.used" + response), dto.getlThresholdNumValue(), dto.getlThresholdTimeValue(), dto.getStrTitle());
            boolean chk = detectionPolicyService.updateUserSignatureDetail(dto);
            if (chk) {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_USER_MOD_SUCCESS, 
                        (String) session.getAttribute("Username"), dto.getStrDescription(), msg);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
                logger.debug("result : " + chk);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_USER_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_USER_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
        }
    }

    /*
     * 사용자 정의 정책 목록 삭제
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/deleteUserSignature", method = RequestMethod.POST)
    @ResponseBody
    public void deleteUserSignature(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        String strName = "";
        try {
        	DetectionPolicyVO vo = detectionPolicyService.selectDetectionPolicyDetail(dto);
        	if (vo != null) {
        		strName = vo.getStrDescription();
        	}
            detectionPolicyService.deleteUserSignature(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_USER_DEL_SUCCESS, (String) session.getAttribute("Username"), strName);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_USER_DEL_FAIL, (String) session.getAttribute("Username"), strName);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_USER_DEL_FAIL, (String) session.getAttribute("Username"), strName);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }
    }

    
    public String deployTargetVseonsorString(DetectionPolicyDto dto) {
        StringBuffer sb = new StringBuffer("");
            sb.append("[" + dto.getStrName() + " : ");
            sb.append("value=" + dto.getlThresholdNumValue() + ", time=" + dto.getlThresholdTimeValue());
            
            if (dto.getlUsedValue() == 0L) {
                sb.append(", USED=N");
            } else {
                sb.append(", USED=Y");
            }
            if (dto.getlResponseBool()) {
                sb.append(", PACKET=Y");
            } else {
                sb.append(", PACKET=N");
            }
            if (Constants.getSystemMode() == 2) {
                if (dto.getlBlockBool()) {
                    sb.append(", BLOCK=Y)");
                } else {
                    sb.append(", BLOCK=N)");
                }
            } else {
                sb.append("]");
            }
//            sb.append(StringUtil.detectionBlockPacketUsedString(dto.getlResponseValue()));
            sb.append(")");
        return sb.toString();
    }

    /*
     * 룰 검사 
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/signatureRuleCheck", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO signatureRuleCheck(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        try {
            result = detectionPolicyService.signatureRuleCheck(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /*
     * 공격명 중복검사
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/isDuplicateSignatureName", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO isDuplicateSignatureName(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        try {
            result = detectionPolicyService.isDuplicateSignatureName(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return result;
    }

    /*
     * 공격 유형 등록 
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/insertSignatureClassType", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO insertSignatureClassType(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        DetectionPolicyVO rtn = new DetectionPolicyVO();
        try {
            detectionPolicyService.insertSignatureClassType(dto);
            rtn.setnClassType(dto.getnClassType());
            rtn.setStrName(dto.getStrName());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_GROUP_ADD_SUCCESS, 
                        (String) session.getAttribute("Username"), dto.getStrName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_GROUP_ADD_FAIL, 
                        (String) session.getAttribute("Username"), dto.getStrName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_GROUP_ADD_FAIL, 
                        (String) session.getAttribute("Username"), dto.getStrName());
        }
        return rtn;
    }

    /*
     * 공격 유형(그룹) 목록 조회 
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectSignatureClassType", method = RequestMethod.POST)
    @ResponseBody
    public List<DetectionPolicyVO> selectSignatureClassType(@RequestBody DetectionPolicyDto dto) throws BaseException {
    	List<DetectionPolicyVO> listData = null;
        try {
            listData = detectionPolicyService.selectSignatureClassType(dto);
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

    /**
     * 시그니처 그룹 이름 중복검사
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/isDuplicateSignatureClassTypeName", method = RequestMethod.POST)
    @ResponseBody
    public boolean isDuplicateSignatureClassTypeName(@RequestBody DetectionPolicyDto dto) throws BaseException {
        boolean rtn = false;
        try {
            rtn = detectionPolicyService.isDuplicateSignatureClassTypeName(dto);

        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    /*
     * 사용자 정의 공격유형(그룹) 삭제
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/deleteSignatureClassType", method = RequestMethod.POST)
    @ResponseBody
    public void deleteSignatureClassType(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        try {
            List<DetectionPolicyVO> resultData = detectionPolicyService.selectSignaturePerGroup(dto);
            if (resultData != null && resultData.size() > 0) {
                for (DetectionPolicyVO item : resultData) {
                    DetectionPolicyDto pa = new DetectionPolicyDto();
                    pa.setlCode(item.getlCode());
                    deleteUserSignature(pa, session);
                }
            }
            detectionPolicyService.deleteSignatureClassType(dto);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_GROUP_DEL_SUCCESS, 
                        (String) session.getAttribute("Username"), dto.getStrName());
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_GROUP_DEL_FAIL, 
                        (String) session.getAttribute("Username"), dto.getStrName());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_GROUP_DEL_FAIL, 
                        (String) session.getAttribute("Username"), dto.getStrName());
        }
    }

    /*
     * 침입탐지 벤더룰 탐지/대응 설정 조회
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectIntrusionDetectionResponse", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectIntrusionDetectionResponse(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO resultData = new DetectionPolicyVO();
        try {
            resultData = detectionPolicyService.selectIntrusionDetectionResponse(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }

    /*
     * 침입탐지 사용자룰 탐지/대응 설정 조회
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectIntrusionDetectionUserResponse", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectIntrusionDetectionUserResponse(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO resultData = new DetectionPolicyVO();
        try {
            resultData = detectionPolicyService.selectIntrusionDetectionUserResponse(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }

    /*
     * 침입탐지 탐지/대응 설정 저장
     * @param dto
     * @param session
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/updateIntrusionDetectionResponse", method = RequestMethod.POST)
    @ResponseBody
    public void updateIntrusionDetectionResponse(@RequestBody DetectionPolicyDto dto, HttpSession session) throws BaseException {
        DetectionPolicyVO vo = new DetectionPolicyVO();
        VsensorVO vSensor = new VsensorVO();
        try {
        	vo = detectionPolicyService.selectDetectionPolicyDetail(dto);
            detectionPolicyService.updateIntrusionDetectionResponse(dto, session);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    /*
     * 침입탐지 탐지/대응 설정 저장시 필요한 유형그룹 정보 조회 
     * @param dto
     * @return
     * @throws BaseException
     */
    @RequestMapping(value = "/api/securityPolicy/selectIntrusionDetectionNclassType", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectIntrusionDetectionNclassType(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO resultData = new DetectionPolicyVO();
        try {
            resultData = detectionPolicyService.selectIntrusionDetectionNclassType(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }

    @RequestMapping(value = "/api/securityPolicy/selectIntrusionDetectionUserNclassType", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectIntrusionDetectionUserNclassType(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO resultData = new DetectionPolicyVO();
        try {
            resultData = detectionPolicyService.selectIntrusionDetectionUserNclassType(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return resultData;
    }

    /*
     * 사용자정의정책 등록시 코드 중복검사 
     * @param username
     * @param errorCode
     * @return
     */
    @RequestMapping(value = "/api/securityPolicy/isDuplicatelCode", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO isDuplicatelCode(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        try {
            result = detectionPolicyService.isDuplicatelCode(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return result;

    }
    /*
     * 사용자정의 정책 등록시 코드 인덱스 자동생성
     * @param dto
     * @return
     * @throws BaseException
     */

    @RequestMapping(value = "/api/securityPolicy/selectUserSignatureIndex", method = RequestMethod.POST)
    @ResponseBody
    public DetectionPolicyVO selectUserSignatureIndex(@RequestBody DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        try {
            result = detectionPolicyService.selectUserSignatureIndex(dto);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return result;
    }
}
