package com.kglory.tms.web.services.systemStatus;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.ext.mail.SimpleMailSender;
import com.kglory.tms.web.mapper.OracleMapper;
import com.kglory.tms.web.mapper.securityPolicy.AuditLogPolicyMapper;
import com.kglory.tms.web.mapper.systemStatus.AuditLogStatusMapper;
import com.kglory.tms.web.model.dto.AuditDto;
import com.kglory.tms.web.model.dto.AuditResultDto;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.model.vo.AuditLogPolicyVO;
import com.kglory.tms.web.model.vo.AuditVO;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.NumberUtil;
import com.kglory.tms.web.util.TableFinder;

import java.text.MessageFormat;

@Service("auditLogSvc")
public class AuditLogService {

    private static Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    AuditLogPolicyMapper auditLogPolicyMapper;
    @Autowired
    AuditLogStatusMapper auditLogStatusMapper;
    @Autowired
    OracleMapper oracleMapper;
    @Autowired
    AccountService accountSvc;

    /**
     * 감사로그를 입력한다.
     *
     * @param lType1 감사로그 타입1
     * @param lType2 감사로그 타입2
     * @param operator 감사로그 발생자
     * @param parameters 감사 로그 내용 파라미터
     */
    public synchronized void insertAuditLog(BigInteger lType1, BigInteger lType2, String operator, Object... parameters) throws BaseException{
        AuditLogPolicyVO auditLogPolicyVO = new AuditLogPolicyVO();
        auditLogPolicyVO.setlType1(lType1);
        auditLogPolicyVO.setlType2(lType2);
        auditLogPolicyVO = auditLogPolicyMapper.selectAuditLogDetail(auditLogPolicyVO);

        AuditVO auditVO = new AuditVO();

        List<String> tables = TableFinder.getQueryTables("AUDIT", Calendar.getInstance(), Calendar.getInstance());
//            List<String> warningTables = TableFinder.getQueryTables("WARNINGLOG", Calendar.getInstance(), Calendar.getInstance());
//            List<String> warningResultTables = TableFinder.getQueryTables("WARNINGRESULTLOG", Calendar.getInstance(), Calendar.getInstance());
        auditVO.setTableName(tables.get(0));

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        auditVO.setTmOccur(sf.format(Calendar.getInstance().getTime()));

        if (auditLogPolicyVO != null && auditLogPolicyVO.getnApply() != null && auditLogPolicyVO.getnApply().equals(1)) {
            auditVO.setStrContent(MessageFormat.format(auditLogPolicyVO.getStrContent(), parameters));

            auditVO.setStrOperator(operator);
            auditVO.setlAuditSetIndex(auditLogPolicyVO.getlAuditSetIndex().longValue());
            auditVO.setLtype1(auditLogPolicyVO.getlType1().longValue());
            auditVO.setLtype2(auditLogPolicyVO.getlType2().longValue());

            auditLogStatusMapper.insertAuditLog(auditVO);

            AuditResultDto resultDto = new AuditResultDto();
            resultDto.setLauditlogindex(auditVO.getlAuditLogIndex());

//                WarningLogDto warningLog = new WarningLogDto();
//                warningLog.setlRefIndex(auditVO.getlAuditLogIndex());
//                warningLog.setStrRefTable(tables.get(0));
//                warningLog.setnType(1);
//                warningLog.setStrDescription(auditVO.getStrContent());
//                warningLog.setlRefIndex(auditVO.getlAuditLogIndex());
//                warningLog.setStrRefTable(auditVO.getTableName());
//                warningLog.setWarningTable(warningTables.get(0));
//                warningLog.setWarningResultTable(warningResultTables.get(0));
//                warningLog.setStrError("");
            //전송 대상 (0: none, 1:mail, 2:sms, 3:sms mail)
            int logTarget = 0;
            try {
                String strAramType = "1000000000";
                // 감사로그 메일 전송
                if (auditLogPolicyVO.getlMailGroup() != null && auditLogPolicyVO.getlMailGroup() > 0) {
                    logTarget = 1;
                    List<AccountVO> mailList = new ArrayList<>();
                    long role = 1L;
                    int userGroup = 2;
                    if (auditLogPolicyVO.getlMailGroup() == 1) {
                        role = 7L;
                        userGroup = 1;
                    }
                    resultDto.setSendtype(0);
//                        warningLog.setlUserGroupIndex(userGroup);
                    mailList = accountSvc.selectAccountMailList(role);

                    if (mailList != null && mailList.size() > 0) {
                        boolean chk = SimpleMailSender.sendAuthMail(auditLogPolicyVO.getStrSmsContent(), auditVO.getStrContent(), operator, mailList);
                        String mailAuditMsg = "";
                        long type = 0L;
                        if (chk) {
                            resultDto.setResult(1);
                            mailAuditMsg = MessageUtil.getMessage("audit.send.mail.success");
                            type = 1L;
                        } else {
                            resultDto.setResult(0);
                            mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
                            type = 2L;
                        }
                        // mail 감사로그 결과 저장
                        for (AccountVO item : mailList) {
                            insertAuditLogMsg(type, mailAuditMsg, operator, item.getId(), auditVO.getStrContent());
                        }
                    }

                }
//                    strAramType = "0100000000";
//                    // 감사로그 SMS 전송
//                    if (auditLogPolicyVO.getlSmsGroup() != null && auditLogPolicyVO.getlSmsGroup() > 0) {
//                        if (logTarget == 1) {
//                            logTarget = 3;
//                        } else {
//                            logTarget = 2;
//                        }
//                        List<AccountVO> smsList = new ArrayList<>();
//                        long role = 1L;
//                        int userGroup = 2;
//                        if (auditLogPolicyVO.getlSmsGroup() == 1) {
//                            role = 7L;
//                            userGroup = 1;
//                        }
////                        warningLog.setlUserGroupIndex(userGroup);
////                        warningLog.setStrAlarmType(strAramType);
//                        smsList = accountSvc.selectAccountSmsList(role);
//                        if (smsList != null && smsList.size() > 0) {
//                            SMSService smsSvc = new SMSService();
//                            smsSvc.setPreventionSvc(preventionSvc);
//                            smsSvc.setMessage(auditLogPolicyVO.getStrSmsContent());
//                            smsSvc.setRecever(smsList);
////                            smsSvc.setWarningLog(warningLog);
//                            smsSvc.start();
//                        }
//                    }
            } catch (BaseException ex) {
                logger.error("Audit Mail Send Fail Type1=" + lType1 + ", Type2=" + lType2);
            } catch (Exception ex) {
                logger.error("Audit Mail Send Fail Type1=" + lType1 + ", Type2=" + lType2);
            }
        }
    }

    /**
     * mail, sms ... 감사로그 전송에 대한 감사로그 생성
     *
     * @param lType1
     * @param msg
     * @param operator
     * @param parameters
     */
    public void insertAuditLogMsg(long lType1, String msg, String operator, Object... parameters) throws BaseException{
        AuditVO auditVO = new AuditVO();

        List<String> tables = TableFinder.getQueryTables("AUDIT", Calendar.getInstance(), Calendar.getInstance());
        auditVO.setTableName(tables.get(0));

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        auditVO.setTmOccur(sf.format(Calendar.getInstance().getTime()));

        if (msg != null && !msg.isEmpty()) {
            auditVO.setStrContent(MessageFormat.format(msg, parameters));

            auditVO.setStrOperator(operator);
            auditVO.setlAuditSetIndex(0L);
            auditVO.setLtype1(lType1);
            auditVO.setLtype2(0L);

            auditLogStatusMapper.insertAuditLog(auditVO);
        }
    }

    public void insertAuditLog(long lType1, long lType2, String operator, Object... parameters) throws BaseException{
        insertAuditLog(BigInteger.valueOf(lType1), BigInteger.valueOf(lType2), operator, parameters);
    }

    public List<AuditVO> selectAuditLogList(AuditDto dto) throws BaseException {
        List<AuditVO> result = null;
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("AUDIT", startDate, endDate));
        dto.setTableNames(selectTables);

        if (selectTables.size() > 0) {
            result = auditLogStatusMapper.selectAuditLogList(dto);
            if (result != null && result.size() > 0) {
                result.get(0).setTotalRowSize(auditLogStatusMapper.selectAuditLogListTotalCount(dto));
            }
        } else {
            result = new ArrayList<AuditVO>();
        }

        return result;
    }

    public List<AuditVO> selectAuditSensorLogList(AuditDto dto) throws BaseException {
        List<AuditVO> result = null;
        String startDate = dto.getStartDateInput();
        String endDate = dto.getEndDateInput();

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("AUDIT", startDate, endDate));
        dto.setTableNames(selectTables);

        if (selectTables.size() > 0) {
            result = auditLogStatusMapper.selectAuditSensorLogList(dto);
        } else {
            result = new ArrayList<AuditVO>();
        }

        return result;
    }

    public void sendMailSensorAudit() throws BaseException {
        List<AuditVO> result = null;
        AuditDto dto = new AuditDto();
        String startDate = DateTimeUtil.getDateToStr(DateTimeUtil.getChangeMinute(DateTimeUtil.getStrToDate(DateTimeUtil.getNowZeroSec("yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm"), -1), "yyyy-MM-dd HH:mm");
        String endDate = DateTimeUtil.getNowZeroSec("yyyy-MM-dd HH:mm");
        dto.setStartDateInput(startDate);
        dto.setEndDateInput(endDate);

        List<String> selectTables = oracleMapper.selectTables(TableFinder.getQueryTables("AUDIT", startDate, endDate));
        dto.setTableNames(selectTables);

        if (selectTables.size() > 0) {
            result = auditLogStatusMapper.selectAuditSensorLogList(dto);
            if (result != null && result.size() > 0) {
                for (AuditVO item : result) {
                    AuditLogPolicyVO auditLogPolicy = new AuditLogPolicyVO();
                    auditLogPolicy.setlType1(BigInteger.valueOf(item.getLtype1()));
                    auditLogPolicy.setlType2(BigInteger.valueOf(item.getLtype2()));
                    AuditLogPolicyVO vo = auditLogPolicyMapper.selectAuditLogDetail(auditLogPolicy);
                    
                    // mail send sensor cli login lock 
                    if (NumberUtil.longEquals(item.getLtype1(), 2L) && NumberUtil.longEquals(item.getLtype2(), 24L)) {
                        vo = new AuditLogPolicyVO();
                        vo.setlMailGroup(1);
                        vo.setStrSmsContent(MessageUtil.getMessage("login.fail.cli.lock.title"));
                    }
                    if ((vo != null && vo.getlMailGroup() != null && vo.getlMailGroup() > 0)) {
                        List<AccountVO> mailList = new ArrayList<>();
                        long role = 1L;
                        if (vo.getlMailGroup() == 1) {
                            role = 7L;
                        }
                        mailList = accountSvc.selectAccountMailList(role);

                        if (mailList != null && mailList.size() > 0) {
                            boolean chk = SimpleMailSender.sendAuthMail(vo.getStrSmsContent(), item.getStrContent(), item.getStrOperator(), mailList);
                            String mailAuditMsg = "";
                            long type = 0L;
                            if (chk) {
                                mailAuditMsg = MessageUtil.getMessage("audit.send.mail.success");
                                type = 1L;
                            } else {
                                mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
                                type = 2L;
                            }
                            // mail 감사로그 결과 저장
                            for (AccountVO account : mailList) {
                                insertAuditLogMsg(type, mailAuditMsg, item.getStrOperator(), item.getStrOperator(), item.getStrContent());
                            }
                        }
                    }
                }
            }
        } else {
            result = new ArrayList<AuditVO>();
        }
    }

    /**
     * message format 적용 ({0} and {1} , "a", 1)
     *
     * @param msg
     * @param parameters
     * @return
     */
    public String messageBuiler(String msg, Object... parameters) {
        return MessageFormat.format(msg, parameters);
    }
}
