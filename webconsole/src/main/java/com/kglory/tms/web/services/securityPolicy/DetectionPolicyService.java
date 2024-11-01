package com.kglory.tms.web.services.securityPolicy;

import com.kglory.tms.web.common.Constants;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.securityPolicy.DetectionPolicyMapper;
import com.kglory.tms.web.model.dto.DetectionPolicyDto;
import com.kglory.tms.web.model.vo.DetectionPolicyVO;
import com.kglory.tms.web.services.systemSettings.NetworkService;
import com.kglory.tms.web.services.systemSettings.SensorService;
import com.kglory.tms.web.services.systemSettings.VsensorService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.TSCheckrules;
import com.kglory.tms.web.util.file.FileUtil;
import com.kglory.tms.web.util.security.AesUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

@Service("detectionPolicySvc")
public class DetectionPolicyService {

    private static Logger logger = LoggerFactory.getLogger(DetectionPolicyService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    DetectionPolicyMapper detectionPolicyMapper;
    @Autowired
    YaraPolicyService yaraPolicySvc;
    @Autowired
    VsensorService vsensorSvc;
    @Autowired
    SensorService sensorSvc;
    @Autowired
    NetworkService networkSvc;
    @Autowired
    AuditLogService auditLogSvc;

    /*
     * 그룹유형별 탐지정책 목록을 조회한다.  
     * @param 
     * @return 
     */
    public List<DetectionPolicyVO> selectSignaturePerGroup(DetectionPolicyDto dto) throws BaseException {
        List<DetectionPolicyVO> result = null;
        result = detectionPolicyMapper.selectSignaturePerGroup(dto);
        ruleDescript(result);
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setrNum(BigInteger.valueOf(i + 1));
                result.get(i).setTotalRowSize(BigInteger.valueOf(result.size()));
            }
        }
        return result;
    }
    
    private void ruleDescript(List<DetectionPolicyVO> list) throws BaseException{
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (StringUtil.isNotEmpty(list.get(i).getStrSigRule())) {
                    list.get(i).setStrSigRule(AesUtil.decryptSignature(list.get(i).getStrSigRule(), list.get(i).getlCode()));
                }
            }
        }
    }
    
    private void ruleDescript(DetectionPolicyVO vo) throws BaseException{
        if (vo != null && vo.getlCode() > 0) {
            vo.setStrSigRule(AesUtil.decryptSignature(vo.getStrSigRule(), vo.getlCode()));
        }
    }

    /**
     * 탐지정책 목록을 조회 시스템관리_가상센서_침입탐지_목록
     *
     * @param DetectionPolicyDto dto
     * @return List<DetectionPolicyVO>
     * @throws BaseException
     */
    public List<DetectionPolicyVO> selectDetectionPolicy(DetectionPolicyDto dto) throws BaseException {
        List<DetectionPolicyVO> result = null;
        result = detectionPolicyMapper.selectDetectionPolicy(dto);
        ruleDescript(result);
        return result;
    }

    /**
     * 시그니처 상세정보를 조회한다.
     *
     * @param DetectionPolicyDto dto
     * @return DetectionPolicyVO
     * @throws BaseException
     */
    public DetectionPolicyVO selectDetectionPolicyDetail(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectDetectionPolicyDetail(dto);
        ruleDescript(result);
        return result;
    }

    /**
     * 시그니처별 도움말 정보를 조회한다.
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public DetectionPolicyVO selectDetectionPolicyHelp(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectDetectionPolicyHelp(dto);
        return result;
    }

    /**
     * 벤더시그니처 상세정보를 수정한다.
     *
     * @param DetectionPolicyDto dto
     * @throws BaseException
     */
    public boolean updateDetectionPolicy(DetectionPolicyDto dto) throws BaseException {
        boolean rtn = false;
        setResponseValue(dto);
        DetectionPolicyVO vo = detectionPolicyMapper.selectDetectionPolicyDetail(dto);
        logger.debug("dto response : " + dto.getlResponse() + ", vo response : " + vo.getlResponse());
        logger.debug("dto : " + dto.toString());
        logger.debug("vo : " + vo.toString());
        if (!dto.equalsSig(DetectionPolicyDto.detectionResponseVoToDto(vo))) {
            detectionPolicyMapper.updateDetectionPolicy(dto);
            writeVendorSignature();
            rtn = true;
        }
        return rtn;
    }

    /**
     * 사용자정의 시그니처 상세정보를 수정한다.
     *
     * @param DetectionPolicyDto dto
     * @throws BaseException
     */
    public boolean updateUserSignatureDetail(DetectionPolicyDto dto) throws BaseException {
        boolean rtn = false;
        DetectionPolicyVO vo = detectionPolicyMapper.selectDetectionPolicyDetail(dto);
        // 침입탐지 기본정보 업데이트 
        setResponseValue(dto);
        DetectionPolicyDto dtoVo = DetectionPolicyDto.detectionResponseVoToDto(vo);
        if (!dto.equalsUserSig(dtoVo)) {
            dto.setStrTitle(AesUtil.encryptSignature(dto.getStrTitle(), dto.getlCode()));
            detectionPolicyMapper.updateUserSignatureDetail(dto);

            // 사용자정의 정책 update 할 때 전달받은 그룹 유형을 넘겨 조회한 뒤 업데이트한다.
            DetectionPolicyVO result = detectionPolicyMapper.selectSclassTypeName(dto.getsClassType());

            // POLICY_USERSIG 테이블에는 데이터가 있고 SYSTEM_SIGHELP에는 데이터가 없을 경우 LCODE를 검색해서 INSERT OR UPDATE 해야한다. 
            DetectionPolicyVO resultValue = detectionPolicyMapper.selectSignatureHelplCode(dto.getlCode());
            if (resultValue == null) {
                dto.setlCode(dto.getlCode());
                if (result.getnClassType() == dto.getsClassType()) {
                    dto.setStrName(result.getStrName());
                    detectionPolicyMapper.insertUserSignatureSummary(dto);
                }
            } else {
                if (result.getnClassType() == dto.getsClassType()) {
                    dto.setStrName(result.getStrName());

                    // 침입탐지 도움말 정보 업데이트  
                    detectionPolicyMapper.updateUserSignatureSummary(dto);
                }
            }
            writeUserSignature();
            rtn = true;
        }

        return rtn;
    }

    /**
     * 공격 그룹유형 목록을 조회한다.
     *
     * @return List<DetectionPolicyVO>
     * @throws BaseException
     */
    public List<DetectionPolicyVO> selectAttackTypeSelect() throws BaseException {

        List<DetectionPolicyVO> result = null;
        result = detectionPolicyMapper.selectAttackTypeSelect();
        return result;

    }

    /**
     * 사용자 정의 정책을 등록한다.
     *
     * @param DetectionPolicyDto dto
     * @return long insertlCode
     * @throws BaseException
     */
    public long insertUserSignature(DetectionPolicyDto dto) throws BaseException {
        long insertlCode = 0;
        DetectionPolicyVO vo = detectionPolicyMapper.selectUserSignatureIndex(dto);

        if (vo == null || vo.getlCode() <= 1000000L) {
            dto.setlCode(1000001L);
        } else {
            dto.setlCode(vo.getlCode());
        }
        setResponseValue(dto);
        dto.setStrTitle(AesUtil.encryptSignature(dto.getStrTitle(), dto.getlCode()));
        // 침입탐지 기본 정보 저장 
        detectionPolicyMapper.insertUserSignature(dto);

        DetectionPolicyVO result = detectionPolicyMapper.selectSclassTypeName(dto.getsClassType());
        if (result.getnClassType() == dto.getsClassType()) {
            dto.setStrName(result.getStrName());
            // 침입탐지 도움말 정보 저장 
            detectionPolicyMapper.insertUserSignatureHelp(dto);
            writeUserSignature();
        }

        insertlCode = dto.getlCode();

        return insertlCode;
    }

    /**
     * 사용자 정의 정책을 도움말을 수정/저장한다.
     *
     * @param dto
     * @throws BaseException
     */
    public void updateUserSignatureHelp(DetectionPolicyDto dto) throws BaseException {
        // 도움말 저장: 일부 컬럼 (도움말 관련된 컬럼만 update)
        DetectionPolicyVO result = detectionPolicyMapper.selectSignatureHelp(dto);
        if (result != null && dto != null) {
            dto.setStrName(result.getStrName());
            detectionPolicyMapper.updateUserSignatureHelpDetail(dto);
        }
    }

    public boolean updateUserSignature(DetectionPolicyDto dto) throws BaseException {
        boolean rtn = false;
        DetectionPolicyVO result = detectionPolicyMapper.selectDetectionPolicyHelp(dto);
        DetectionPolicyDto voDto = DetectionPolicyDto.detectionResponseVoToDto(result);
        if (!dto.equalsSigHelp(voDto)) {
            detectionPolicyMapper.updateUserSignature(dto);
            rtn = true;
        }
        return rtn;
    }

    /**
     * 사용자 정의 정책 목록을 삭제한다.
     *
     * @param dto
     * @throws BaseException
     */
    public void deleteUserSignature(DetectionPolicyDto dto) throws BaseException {
        detectionPolicyMapper.deleteUserSignature(dto);
        detectionPolicyMapper.deleteUserSignatureHelp(dto);
        writeUserSignature();
    }

    /**
     * 패턴 검사
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public DetectionPolicyVO signatureRuleCheck(DetectionPolicyDto dto) throws BaseException {
        int result = -1;
        DetectionPolicyVO rtn = new DetectionPolicyVO();
        String strSigRule = dto.getStrSigRule();
        strSigRule = decode(strSigRule, "UTF-8");
        TSCheckrules checker = new TSCheckrules();
        checker.errorMessage = "";
        result = checker.CheckRuleV2(strSigRule, "4.5.5");
        logger.debug("result : " + result + ", errorMessage : " + checker.errorMessage);

        rtn.setSigRuleYn(result);
        rtn.setErrorMessage(checker.errorMessage);
        return rtn;
    }
    
    /**
     * 전달받은 패턴을 decoding
     *
     * @param String str, String characterSet
     * @return String characterSet
     * @throws BaseException
     */
    public String decode(String str, String characterSet) throws BaseException {
        try {
            return java.net.URLDecoder.decode(str, characterSet);
        } catch (UnsupportedEncodingException e) {
        	throw new BaseException(messageSource, "errorCode", null, "", e);
        } catch (Exception e) {
        	throw new BaseException(messageSource, "errorCode", null, "", e);
        }
    }

    /**
     * 공격명 중복 검사
     *
     * @param DetectionPolicyDto dto
     * @return DetectionPolicyVO
     * @throws BaseException
     */
    public DetectionPolicyVO isDuplicateSignatureName(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.isDuplicateSignatureName(dto);
        if (result == null) {
            return new DetectionPolicyVO();
        } else {
            return result;
        }
    }

    /**
     * 사용자 정의 공격유형 추가
     *
     * @param dto
     * @throws BaseException
     */
    public void insertSignatureClassType(DetectionPolicyDto dto) throws BaseException {
        detectionPolicyMapper.insertSignatureClassType(dto);
    }

    /**
     * 사용자 정의 공격유형 조회
     *
     * @param dto
     * @return List<DetectionPolicyVO>
     * @throws BaseException
     */
    public List<DetectionPolicyVO> selectSignatureClassType(DetectionPolicyDto dto) throws BaseException {
        List<DetectionPolicyVO> result = detectionPolicyMapper.selectSignatureClassType(dto);
        logger.info("result >> "+result);
        return result;
    }

    /**
     * 시그니처 이름 중복검사
     *
     * @param dto
     * @return boolean
     * @throws BaseException
     */
    public boolean isDuplicateSignatureClassTypeName(DetectionPolicyDto dto) throws BaseException {
        List<DetectionPolicyVO> result = null;
        result = detectionPolicyMapper.isDuplicateSignatureClassTypeName(dto);

        if (result != null && result.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 사용자 정의 공격유형 삭제
     *
     * @param DetectionPolicyDto dto
     * @throws BaseException
     */
    public void deleteSignatureClassType(DetectionPolicyDto dto) throws BaseException {
        detectionPolicyMapper.deleteSignatureClassType(dto);
    }

    /**
     * 침입탐지 탐지/대응 설정 조회
     *
     * @param DetectionPolicyDto dto
     * @return DetectionPolicyVO
     * @throws BaseException
     */
    public DetectionPolicyVO selectIntrusionDetectionResponse(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectIntrusionDetectionResponse(dto);
        return result;
    }

    /**
     * 침입탐지 탐지/대응 설정 수정/저장
     *
     * @param dto
     * @throws BaseException
     */
    public void updateIntrusionDetectionResponse(DetectionPolicyDto dto, HttpSession session) throws BaseException {
        try {
            String response = "0";
            String block = "0";
            if(dto.getlResponseBool()) {
                response = "1";
            }
            if(dto.getlBlockBool()) {
                block = "1";
            }
            if (dto.getlCode() > 1000000) {
                if (dto.getlResponseBool() == true && dto.getlBlockBool() == true) {
                    dto.setlResponse(268566530L);
                } else if (dto.getlResponseBool() == true && dto.getlBlockBool() == false) {
                    dto.setlResponse(131074L);
                } else if (dto.getlResponseBool() == false && dto.getlBlockBool() == true) {
                    dto.setlResponse(268566529L);
                } else if (dto.getlResponseBool() == false && dto.getlBlockBool() == false) {
                    dto.setlResponse(131073L);
                }
                detectionPolicyMapper.updateIntrusionDetectionUserResponse(dto);
            } else {
                if (dto.getlResponseBool() == true && dto.getlBlockBool() == true) {
                    dto.setlResponse(268566530L);
                } else if (dto.getlResponseBool() == true && dto.getlBlockBool() == false) {
                    dto.setlResponse(131074L);
                } else if (dto.getlResponseBool() == false && dto.getlBlockBool() == true) {
                    dto.setlResponse(268566529L);
                } else if (dto.getlResponseBool() == false && dto.getlBlockBool() == false) {
                    dto.setlResponse(131073L);
                }
                detectionPolicyMapper.updateIntrusionDetectionResponse(dto);
            }
            String msg = MessageUtil.getbuilMessage("audit.policy.detection.vender", MessageUtil.getMessage("str.used" + dto.getlUsedValue()), MessageUtil.getMessage("str.used" + block), 
                    MessageUtil.getMessage("str.used" + response), dto.getlThresholdNumValue(), dto.getlThresholdTimeValue());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.POLICY_DETECTION_MOD_SUCCESS, (String) session.getAttribute("Username"), dto.getStrDescription(), msg);
            if (dto.getlCode() > 1000000) {
                writeUserSignature();
            } else {
                writeVendorSignature();
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
            throw new BaseException(messageSource, "errorCode", null, "", e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.POLICY_DETECTION_MOD_FAIL, (String) session.getAttribute("Username"), dto.getStrDescription());
            throw new BaseException(messageSource, "errorCode", null, "", e);
        }
    }

    /**
     * 침입탐지 탐지/대응 설정 저장시 필요한 유형그룹 정보
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public DetectionPolicyVO selectIntrusionDetectionNclassType(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectIntrusionDetectionNclassType(dto);
        return result;
    }

    /**
     * 공격 코드 중복 검사
     *
     * @param dto
     * @return DetectionPolicyVO
     */
    public DetectionPolicyVO isDuplicatelCode(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.isDuplicatelCode(dto);
        if (result == null) {
            return new DetectionPolicyVO();
        } else {
            return result;
        }
    }

    /**
     * 사용자정의 정책 인덱스 생성
     *
     * @param DetectionPolicyDto dto
     * @return DetectionPolicyVO
     */
    public DetectionPolicyVO selectUserSignatureIndex(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectUserSignatureIndex(dto);
        if (result == null) {
            return new DetectionPolicyVO();
        } else {
            return result;
        }
    }

    /**
     * 침입탐지 탐지/대응 설정 저장시 필요한 사용자 유형그룹 정보
     *
     * @param dto
     * @return DetectionPolicyVO
     * @throws BaseException
     */
    public DetectionPolicyVO selectIntrusionDetectionUserNclassType(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectIntrusionDetectionUserNclassType(dto);
        return result;
    }

    /**
     * 침입탐지 사용자룰 탐지/대응 설정 조회
     *
     * @param DetectionPolicyDto dto
     * @return DetectionPolicyVO
     * @throws BaseException
     */
    public DetectionPolicyVO selectIntrusionDetectionUserResponse(DetectionPolicyDto dto) throws BaseException {
        DetectionPolicyVO result = new DetectionPolicyVO();
        result = detectionPolicyMapper.selectIntrusionDetectionUserResponse(dto);
        return result;
    }

    public void mergeVendorSignature(List<DetectionPolicyDto> policyList) throws BaseException {
        long totTime = System.currentTimeMillis();
        for (DetectionPolicyDto item : policyList) {
            Long code = detectionPolicyMapper.isSignature(item);
            item.setStrSigRule(AesUtil.encryptSignature(item.getStrSigRule(), item.getlCode()));
            if(code != null) {
                detectionPolicyMapper.updateImportVendorSignature(item);
            } else {
                detectionPolicyMapper.insertImportVendorSignature(item);
            }
        }
        logger.debug("vendor signature list.size : " + policyList.size() + ",totTime : " + (System.currentTimeMillis() - totTime));
    }

    public void mergeUserSignature(List<DetectionPolicyDto> policyList) throws BaseException {
        long totTime = System.currentTimeMillis();
        for (DetectionPolicyDto item : policyList) {
            Long code = detectionPolicyMapper.isSignature(item);
            item.setStrSigRule(AesUtil.encryptSignature(item.getStrSigRule(), item.getlCode()));
            if(code != null) {
                detectionPolicyMapper.updateImportUserSignature(item);
            } else {
                detectionPolicyMapper.insertImportUserSignature(item);
            }
        }
        logger.debug("user signature list.size : " + policyList.size() + ",totTime : " + (System.currentTimeMillis() - totTime));
    }

    public void setResponseValue(DetectionPolicyDto dto) {
        if (dto.getlResponseBool() == true && dto.getlBlockBool() == true) {
            dto.setlResponse(268566530L);
        } else if (dto.getlResponseBool() == true && dto.getlBlockBool() == false) {
            dto.setlResponse(131074L);
        } else if (dto.getlResponseBool() == false && dto.getlBlockBool() == true) {
            dto.setlResponse(268566529L);
        } else if (dto.getlResponseBool() == false && dto.getlBlockBool() == false) {
            dto.setlResponse(131073L);
        }
    }
    
    public void writeVendorSignature() throws BaseException{
        List<DetectionPolicyVO> list = detectionPolicyMapper.selectVendorWritePolicy();
        ruleDescript(list);
        FileUtil.writePolicy(list, FileUtil.SIG_POL, FileUtil.SIG_RESP_POL, FileUtil.SIG_POL_SEC, FileUtil.SIG_RESP_POL_SEC);
    }
    
    public void writeUserSignature() throws BaseException{
        List<DetectionPolicyVO> list = detectionPolicyMapper.selectUserWritePolicy();
        ruleDescript(list);
        FileUtil.writePolicy(list, FileUtil.SIG_USER_POL, FileUtil.SIG_USER_RESP_POL, FileUtil.SIG_USER_POL_SEC, FileUtil.SIG_USER_RESP_POL_SEC);
    }
    
    public void initWriteSensorFile() throws BaseException{
        writeVendorSignature();
        writeUserSignature();
        yaraPolicySvc.writeYaraPolicy();
        vsensorSvc.writePolicyException();
        sensorSvc.writeSensorInbound();
        networkSvc.writeNetwork();
        sensorSvc.writeSensorDetailInfo();
    }
}
