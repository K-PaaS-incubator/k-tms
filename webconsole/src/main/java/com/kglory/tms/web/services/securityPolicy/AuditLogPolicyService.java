package com.kglory.tms.web.services.securityPolicy;

import com.kglory.tms.web.common.Constants;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.securityPolicy.AuditLogPolicyMapper;
import com.kglory.tms.web.model.dto.AuditLogPolicyDto;
import com.kglory.tms.web.model.vo.AuditLogPolicyVO;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Service
public class AuditLogPolicyService {

    private static Logger logger = LoggerFactory.getLogger(AuditLogPolicyService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    AuditLogPolicyMapper auditLogPolicyMapper;
    @Autowired
    AuditLogService auditLogSvc;

    /**
     * 감사로그 정책 행위 리스트 정보 업데이트 알람정보에 대한 index 값 조회후 해당 인덱스를 warnigindex에 set, 정책 행위 리스트 업데이트 policy_warningset
     * warningSetIndex를 조회, policy_auditSet warnigindex에 set 후 업데이트
     *
     * @param dto
     * @return
     */
    public Integer updateAuditLogPolicyList(List<AuditLogPolicyDto> dto, HttpSession session) throws BaseException{
        Integer updateCount = 0;
        try {
            for (int i = 0; i < dto.size(); i++) {
                if (dto.get(i).getnAlarmType() != 0) {
                    dto.get(i).setlWarningIndex(selectWarningsetIndex(dto.get(i)));
                } else {
                    dto.get(i).setlWarningIndex(BigInteger.valueOf(0));
                }
                AuditLogPolicyVO param = new AuditLogPolicyVO();
                param.setlType1(dto.get(i).getlType1());
                param.setlType2(dto.get(i).getlType2());
                AuditLogPolicyVO vo = auditLogPolicyMapper.selectAuditLogPolicy(param);
                if (dto.get(i).getlWarningIndex() != vo.getlWarningIndex() || dto.get(i).getnApply() != vo.getnApply()) {
                    updateCount += auditLogPolicyMapper.updateAuditLogPolicyList(dto.get(i));
                    String mail = "";
                    String sms = "";

                    if (dto.get(i).getlMailGroup() == 0L) {
                        mail = MessageUtil.getMessage("str.used0");
                    } else if (dto.get(i).getlMailGroup() == 1L) {
                        mail = messageSource.getMessage("Auth.type.admin", null, Locale.getDefault());
                    } else if (dto.get(i).getlMailGroup() == 2L) {
                        mail = messageSource.getMessage("Auth.type.user", null, Locale.getDefault());
                    }
                    if (dto.get(i).getlSmsGroup() == 0L) {
                        sms = MessageUtil.getMessage("str.used0");;
                    } else if (dto.get(i).getlSmsGroup() == 1L) {
                        sms = messageSource.getMessage("Auth.type.admin", null, Locale.getDefault());
                    } else if (dto.get(i).getlSmsGroup() == 2L) {
                        sms = messageSource.getMessage("Auth.type.user", null, Locale.getDefault());
                    }
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.AUDITLOG_MOD_SUCCESS,
                            (String) session.getAttribute("Username"), dto.get(i).getlType1() + "-" + dto.get(i).getlType2(), mail, sms);
                }
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage());
            updateCount = -1;
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.AUDITLOG_MOD_FAIL,
                        (String) session.getAttribute("Username"));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            updateCount = -1;
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.AUDITLOG_MOD_FAIL,
                        (String) session.getAttribute("Username"));
        }

        return updateCount;

    }

    /**
     * 감사로그 정책| 알람설정 인덱스 조회
     *
     * @param dto
     * @return
     */
    public BigInteger selectWarningsetIndex(AuditLogPolicyDto dto) throws BaseException {
        BigInteger warningSetIndex = null;
        warningSetIndex = auditLogPolicyMapper.selectWarningsetIndex(dto);
        return warningSetIndex;
    }

    /**
     * 감사로그 오류 - 센서리스트 조회
     *
     * @return
     */
    public List<AuditLogPolicyDto> selectAuditLogPolicyErrorSensorList() throws BaseException {

        List<AuditLogPolicyDto> resultList = null;
        AuditLogPolicyDto dto = new AuditLogPolicyDto();
        dto.setlType2(BigInteger.valueOf(1));

        resultList = auditLogPolicyMapper.selectAuditLogPolicyErrorList(dto);

        return resultList;
    }

    /**
     * 감사로그 오류 - 콘솔 리스트 조회
     *
     * @return
     */
    public List<AuditLogPolicyDto> selectAuditLogPolicyErrorConsoleList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;
        AuditLogPolicyDto dto = new AuditLogPolicyDto();
        dto.setlType2(BigInteger.valueOf(20000));

        resultList = auditLogPolicyMapper.selectAuditLogPolicyErrorList(dto);

        return resultList;
    }

    /**
     * 감사로그 행위 - 센서리스트 조회
     *
     * @return
     */
    public List<AuditLogPolicyDto> selectAuditLogPolicyActionSensorList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;
        AuditLogPolicyDto dto = new AuditLogPolicyDto();
        dto.setlType2(BigInteger.valueOf(1));

        resultList = auditLogPolicyMapper.selectAuditLogPolicyActionList(dto);

        return resultList;
    }

    /**
     * 감사로그 행위 - 콘솔리스트 조회
     *
     * @return
     */
    public List<AuditLogPolicyDto> selectAuditLogPolicyActionConsoleList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;
        AuditLogPolicyDto dto = new AuditLogPolicyDto();
        dto.setlType2(BigInteger.valueOf(20000));
        resultList = auditLogPolicyMapper.selectAuditLogPolicyActionList(dto);
        return resultList;
    }

    /**
     * 감사로그 경고 - 센서리스트 조회
     *
     * @return
     */
    public List<AuditLogPolicyDto> selectAuditLogPolicyWarningSensorList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;
        AuditLogPolicyDto dto = new AuditLogPolicyDto();
        dto.setlType2(BigInteger.valueOf(1));

        resultList = auditLogPolicyMapper.selectAuditLogPolicyWarningList(dto);
        return resultList;
    }

    /**
     * 감사로그 경고 - 콘솔리스트 조회
     *
     * @return
     */
    public List<AuditLogPolicyDto> selectAuditLogPolicyWarningConsoleList() throws BaseException {
        List<AuditLogPolicyDto> resultList = null;
        AuditLogPolicyDto dto = new AuditLogPolicyDto();
        dto.setlType2(BigInteger.valueOf(20000));

        resultList = auditLogPolicyMapper.selectAuditLogPolicyWarningList(dto);
        return resultList;
    }
}
