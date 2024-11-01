package com.kglory.tms.web.services.systemSettings;

import com.kglory.tms.web.batch.ScheduleUtil;
import com.kglory.tms.web.common.Constants;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.ext.mail.SimpleMailSender;
import com.kglory.tms.web.mapper.preferences.AccountMapper;
import com.kglory.tms.web.mapper.systemSettings.ManagerMapper;
import com.kglory.tms.web.model.dto.ImDbBackupDto;
import com.kglory.tms.web.model.dto.IntegrityFile;
import com.kglory.tms.web.model.dto.ManagerBackupDto;
import com.kglory.tms.web.model.dto.ManagerDto;
import com.kglory.tms.web.model.dto.ManagerIntegrityFileDto;
import com.kglory.tms.web.model.dto.ManagerSyslogDto;
import com.kglory.tms.web.model.dto.SystemConfDto;
import com.kglory.tms.web.model.dto.SystemDto;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.model.vo.ImDbBackupVO;
import com.kglory.tms.web.model.vo.ManagerBackupVO;
import com.kglory.tms.web.model.vo.ManagerSyslogVO;
import com.kglory.tms.web.model.vo.ManagerVO;
import com.kglory.tms.web.model.vo.SystemVO;
import com.kglory.tms.web.services.OracleService;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.services.systemStatus.DbUsageService;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.NumberUtil;
import com.kglory.tms.web.util.SpringUtils;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.file.FileUtil;
import com.kglory.tms.web.util.security.AesUtil;
import com.kglory.tms.web.util.security.DigestUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("managerSvc")
public class ManagerService {

    private static Logger logger = LoggerFactory.getLogger(ManagerService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    ManagerMapper managerMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    SystemConfService systemConfSvc;
    @Autowired
    AccountService accountSvc;
    @Autowired
    AuditLogService auditLogSvc;
    @Autowired
    OracleService oracleSvc;
    @Autowired
    DbUsageService dbUsageSvc;

    @Value("${username}")
    private String jdbcUser;
    @Value("${password}")
    private String jdbcPwd;

    private static List<IntegrityFile> consoleIntegrityFile = new ArrayList<>();

    private static final String[] DETECTION_LOG = {"LOG_%", "RAWDATA_%", "APPLAYER_%", "FILEMETA_%"};
    private static final String[] DETECTION_LOG_FILE = {"log_", "rawdata_", "applayer_", "filemeta_"};
    private static final String[] TRAFFIC_LOG = {"IP_TRAFFIC_%", "PROTOCOL_%", "SERVICE_%", "TRAFFIC_IP_%"};
    private static final String[] TRAFFIC_LOG_FILE = {"ifTraffic_", "protocol_", "service_", "trafficIp_"};
    private static final String SESSION_LOG = "SESSION_%";
    private static final String SESSION_LOG_FILE = "session_";
    private static final String[] POLICY_LOG = {"POLICY_AUDITSET", "POLICY_EXCEPTION", "POLICY_SIGNATURE", "POLICY_USERSIG", "POLICY_YARA_RULE", "POLICY_YARA_USERRULE"};
    private static final String[] POLICY_LOG_FILE = {"policyAuditset", "policyException", "policySignature", "policyUsersig", "policyYaraRule", "policyYaraUserrule"};
    private static final String AUDIT_LOG = "AUDIT_%";
    private static final String AUDIT_LOG_FILE = "audit_";

    private static final String BACKUP_FOLDER = "/logs/data/backup/";
    private static final String BACKUP_FILENAME = "dbBackup_";

    private static final String V6 = "V6_";
    private static final String LOG = "LOG_";
    private static final String ROWDATA = "RAWDATA_";
    private static final String SESSION = "SESSION_";
    private static final String APPLAYER = "APPLAYER_";
    private static final String FILEMETA = "FILEMETA_";
    private static final String AUDIT = "AUDIT_";
    private static final String AUDIT_RESULT = "AUDIT_RESULT_";
    private static final String SENSOR_ALIVE = "SENSOR_ALIVE_";
    private static final String SENSOR_SESSION = "SENSOR_SESSION_";
    private static final String SYSTEMLOG = "SYSTEMLOG_SENSOR_";
    private static final String IPTRAFFIC = "IP_TRAFFIC_";
    private static final String PROTOCOL = "PROTOCOL_";
    private static final String SERVICE = "SERVICE_";
    private static final String TRAFFICIP = "TRAFFIC_IP_";
    private static final String TRAFFICLOG = "TRAFFIC_DETECTION_";

    /**
     * 매니저 등록정보를 조회한다
     *
     * @return
     * @throws BaseException
     */
    public SystemVO selectSystemSettingInfo() throws BaseException {
        SystemVO result = null;
        result = managerMapper.selectSystemSettingInfo();
        if (!result.getEmailUserPwd().isEmpty()) {
        	try {
        		result.setEmailUserPwd(AesUtil.decryptString(result.getEmailUserPwd(), result.getEmailUserId()));
        	}
        	catch (IllegalStateException e) {
        		logger.error("setEmailUserPwd err"+e);
        		result.setEmailUserPwd("");
        	}
        	catch (Exception e) {
        		logger.error("setEmailUserPwd err"+e);
        		result.setEmailUserPwd("");
        	}
        }
        return result;
    }

    /**
     * 매니저 등록정보를 수정/저장한다.
     *
     * @param dto
     * @param session
     * @return
     * @throws BaseException
     */
    public int updateManagerSettingInfo(SystemDto dto, HttpSession session) throws BaseException {
        String[] lockValues = {"3", "4", "5"};
        String[] timeValues = {"5", "10", "15", "20", "30"};
        String[] sessionValues = {"5", "10"};
        if (!Arrays.asList(lockValues).contains(dto.getLockValue()) || !Arrays.asList(lockValues).contains(dto.getSystemConfList().get(1).getValue())) {
            return -99;
        } else if (!Arrays.asList(timeValues).contains(dto.getTimeValue())|| !Arrays.asList(timeValues).contains(dto.getSystemConfList().get(2).getValue())) {
            return -99;
        } else if (!Arrays.asList(sessionValues).contains(dto.getSessionValue())|| !Arrays.asList(sessionValues).contains(dto.getSystemConfList().get(0).getValue())) {
            return -99;
        }

        int rtn = 0;
        //updateEmailServer
        SystemVO systemInfo = managerMapper.selectSystemSettingInfo();

        //f->b decrypt
        if (StringUtils.hasText(dto.getEmailUserPwd())) {
        	String emailPwd = AesUtil.decrypt(dto.getEmailUserId(), dto.getEmailUserPwd());
        	dto.setEmailUserPwd(emailPwd);
        }

        String sftpId = "";
        String sftpPwd = "";
        for(SystemConfDto tmp : dto.getSftpConfList()) {
        	if("sftp.pwd".equals(tmp.getKey())) {
        		sftpPwd = tmp.getValue();
        	}
        	if("sftp.id".equals(tmp.getKey())) {
        		sftpId = tmp.getValue();
        	}
        }
        
        if(!"".equals(sftpId) && !"".equals(sftpPwd)) {
        	for(SystemConfDto tmp : dto.getSftpConfList()) {
        		if("sftp.pwd".equals(tmp.getKey())) {
        			sftpPwd = AesUtil.decrypt(sftpId, tmp.getValue());
            		tmp.setValue(sftpPwd);
            	}
        	}
        }
        
        logger.debug("managerInfo:::::::::::::: " + systemInfo.toString());
        logger.debug("getSystemConfList ::::::::::::::: " + dto.getSystemConfList());
        logger.debug("getSftpConfList ::::::::::::::: " + dto.getSftpConfList());
        
        
        // 로그인 설정정보 저장
        systemConfSvc.updateSystemConf(dto, dto.getSystemConfList(), session);
        // sftp 설정정보 저장
        systemConfSvc.updateSftpConf(dto, dto.getSftpConfList(), session);
        
        if (systemInfo.getEmailUserPwd() != null && !systemInfo.getEmailUserPwd().isEmpty()) {
        	try {
        		String emailPwd = AesUtil.decryptString(systemInfo.getEmailUserPwd(), systemInfo.getEmailUserId());
        		systemInfo.setEmailUserPwd(emailPwd);
        	}
        	catch (IllegalStateException e) {
        		systemInfo.setEmailUserPwd("");
			}
        	catch (Exception e) {
        		systemInfo.setEmailUserPwd("");
        	}
        }

        //system_manager 기본 정보 수정(변경 사항이 있으면)
        if (!systemInfo.equalsEmailSetting(dto)) {
            try {
                logger.debug("dto :" + dto.toString());
                String pwd = dto.getEmailUserPwd();
                if (dto.getEmailUserPwd() != null && !dto.getEmailUserPwd().isEmpty()) {
                    dto.setEmailUserPwd(AesUtil.encryptString(dto.getEmailUserPwd(), dto.getEmailUserId()));
                }

                managerMapper.updateEmailServer(dto);

                //audit.system.email=서버:{0}, 포트{1}, 사용자:{2}
                String msg = MessageUtil.getbuilMessage("audit.system.email", dto.getEmailServer(), dto.getEmailPort(), dto.getEmailSecurity(), dto.getEmailUserId(), dto.getEmailUserPwd());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_EMAIL_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
                SimpleMailSender.setMailServer(dto.getEmailServer()); 
                SimpleMailSender.setMailAuthId(dto.getEmailUserId()); 
                SimpleMailSender.setMailAuthPwd(pwd);
                SimpleMailSender.setPort(dto.getEmailPort());
                SimpleMailSender.setMailSecurity(dto.getEmailSecurity());
                rtn = 1;
            } catch (BaseException ex) {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_EMAIL_MOD_FAIL, (String) session.getAttribute("Username"));
                throw ex;
            } catch (Exception ex) {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_EMAIL_MOD_FAIL, (String) session.getAttribute("Username"));
                throw new BaseException(ex);
            }
        }
        return rtn;
    }

    /*
     * 매니저 무결성 정보를 조회한다.
     * @param 
     * @return SensorVO 센서 상세 목록 정보 result
     */
    public ManagerVO selectManagerIntegrityInfo() throws BaseException {

        ManagerVO result = null;
        result = managerMapper.selectManagerIntegrityInfo();

        return result;
    }

    /*
     * 매니저 무결성검사 정보를 수정한다.
     * @param SystemConfigDto
     * @return 
     */
    public int updateManagerIntegrityInfo(ManagerIntegrityFileDto dto, HttpSession session) throws BaseException {
        int rtn = 0;
        try {
            ManagerVO info = managerMapper.selectManagerIntegrityInfo();
            //매니저 무결성 기본 정보를 수정 한다.
            if (!info.equalsIntegrityInfo(dto)) {
                managerMapper.updateManagerIntegrityInfo(dto);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_INTEGRITY_MOD_SUCCESS, (String) session.getAttribute("Username"));
                rtn = 1;
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_INTEGRITY_MOD_FAIL, (String) session.getAttribute("Username"));
            throw e;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_INTEGRITY_MOD_FAIL, (String) session.getAttribute("Username"));
            throw new BaseException(e);
        }
        return rtn;
    }

    public ManagerVO selectDbManagement() throws BaseException {
        ManagerVO result = null;
        result = managerMapper.selectDbManagement();
        return result;
    }

    public int updateDbManagement(ManagerDto dto, HttpSession session) throws BaseException {
        int rtn = 0;
        if (dto.getnDiskWarn() < 60 || dto.getnDiskWarn() > 99 ||
            dto.getnDiskUsage() < 70 || dto.getnDiskUsage() > 99) {
            return -99;
        }
        try {
            ManagerVO dbInfo = managerMapper.selectDbManagement();
            //system_manager 기본 정보 수정 (기본 정보가 변경이 있다면)
            if (dbInfo == null || !dbInfo.equalsDbInfo(dto)) {
                managerMapper.updateDbManagement(dto);
                rtn = 1;
                String msg = MessageUtil.getbuilMessage("audit.system.dbManagment", dto.getnRawPeriodicInput(), dto.getnAuditPeriodicInput(), dto.getnDiskWarn(), dto.getnDiskUsage());
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_DB_MANAGE_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_DB_MANAGE_MOD_FAIL, (String) session.getAttribute("Username"));
            throw e;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_DB_MANAGE_MOD_FAIL, (String) session.getAttribute("Username"));
            throw new BaseException(e);
        }
        return rtn;
    }
    /*
     * 매니저 시간동기화 정보를 조회한다
     */

    public ManagerSyslogVO selectManagerTimeSync() throws BaseException {
        ManagerSyslogVO result = null;
        result = managerMapper.selectManagerTimeSync();
        logger.debug(result.toString());

        return result;
    }
    /*
     * 매니저 시간동기화 정보를 업데이트한다.
     */

    public int updateManagerTimeSync(ManagerSyslogDto dto, HttpSession session) throws BaseException {
        int rtn = 0;
        try {
            ManagerSyslogVO timeSync = managerMapper.selectManagerTimeSync();
            if (dto.getStrTimeServerName() == null) {
                dto.setStrTimeServerName("");
            }
            if (!timeSync.equalsTimeSync(dto)) {
                managerMapper.updateManagerTimeSync(dto);
                if (dto.getnUseTimeSync() == 1) {
                    ScheduleUtil.timeSyncReStart(dto.getnTimeSyncPeriod());
                } else {
                    ScheduleUtil.timeSyncStop();
                }
                rtn = 1;
                if (!dto.getnTimeSyncPeriod().equals(timeSync.getnTimeSyncPeriod()) || !dto.getStrTimeServerName().equals(timeSync.getStrTimeServerName()) || dto.getnUseTimeSync() != timeSync.getnUseTimeSync()) {
                    String msg = MessageUtil.getbuilMessage("audit.system.timeSync", MessageUtil.getMessage("str.used" + dto.getnUseTimeSync()), dto.getnTimeSyncPeriod(), dto.getStrTimeServerName());
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_TIMESYC_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
                }
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_TIMESYC_MOD_FAIL, (String) session.getAttribute("Username"));
            throw e;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_TIMESYC_MOD_FAIL, (String) session.getAttribute("Username"));
            throw new BaseException(e);
        }
        return rtn;
    }

    public void execTimeSync() throws BaseException {
    	logger.info("forlks >>>>>>>> execTimeSync()");
        ManagerSyslogVO timeSync = managerMapper.selectManagerTimeSync();
        logger.info("forlks >>>>>>>> timeSync.getnUseTimeSync() : "+timeSync.getnUseTimeSync());
        if (timeSync.getnUseTimeSync() == 1) {
            String[] ntp = timeSync.getStrTimeServerName().split("\\|");
            String time = DateTimeUtil.getNtpServerDateTime(ntp);
            logger.info("forlks >>>>>>>> getnTimeSyncPeriod : "+timeSync.getnTimeSyncPeriod());
            if (time != null && !time.isEmpty()) {
                List<String> list = new ArrayList<>();
                list.add("date");
                list.add("-s");
                list.add(time);
                try {
                    SystemUtil.execCommand(list);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.TIME_SYNC_SUCCESS, "administrator");
                } catch (BaseException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.TIME_SYNC_FAIL, "administrator");
                }
            }
        }
    }

    /**
     * 매니저 백업 정보를 조회한다.
     *
     * @return
     */
    public ManagerBackupVO selectDbBackup() throws BaseException {
        ManagerBackupVO result = null;
        result = managerMapper.selectDbBackup();
        return result;
    }
    /*
     * 매니저 백업 정보를 업데이트한다.
     */

    public int updateDbBackup(ManagerBackupDto dto, HttpSession session) throws BaseException {
        int rtn = 0;
        try {
            ManagerBackupVO backup = managerMapper.selectDbBackup();
            if (!backup.equalseDbBackup(dto)) {
                managerMapper.updateDbBackup(dto);
                ManagerBackupVO scheduler = getDayBookTiem();
                if (scheduler.getnDayConfigFlag() == 1) {
                    ScheduleUtil.dbBackupStop();
                    ScheduleUtil.dbBackupStart(scheduler.getDayHour(), scheduler.getDayMin(), scheduler.getDaySec());
                } else {
                    ScheduleUtil.dbBackupStop();
                }
                rtn = 1;
                String[] arr = new String[4];
                String tableVal = Integer.toBinaryString(Long.valueOf(dto.getnDayTableCheckValue()).intValue());
                if (tableVal.length() != 5) {
                    String aa = "";
                    for (int i = 0; i < (5 - tableVal.length()); i++) {
                        aa += "0";
                    }
                    tableVal = aa + tableVal;
                }
                String msg = MessageUtil.getbuilMessage("audit.system.dbBackup", MessageUtil.getMessage("str.used" + dto.getnDayConfigFlag()), MessageUtil.getMessage("str.used" + tableVal.charAt(4)),
                        MessageUtil.getMessage("str.used" + tableVal.charAt(3)), MessageUtil.getMessage("str.used" + tableVal.charAt(2)),
                        MessageUtil.getMessage("str.used" + tableVal.charAt(1)), MessageUtil.getMessage("str.used" + tableVal.charAt(0)),
                        dto.getStrDayBookTime(), dto.getnDayBookDayBefore(), dto.getStrBackupPathName(), MessageUtil.getMessage("str.backupFlag" + dto.getnDayFileFlag()),
                        dto.getStrDayFileName(), MessageUtil.getMessage("str.used" + dto.getnDayTableDeleteFlag()));
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_DB_BACKUP_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_DB_BACKUP_MOD_FAIL, (String) session.getAttribute("Username"));
            throw e;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_DB_BACKUP_MOD_FAIL, (String) session.getAttribute("Username"));
            throw new BaseException(e);
        }
        return rtn;
    }

    private boolean longEquals(Long check, Long value) {
        if (check == null && value == null) {
            return true;
        } else if (check != null && value != null) {
            if (Long.valueOf(check).intValue() == Long.valueOf(value).intValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 수동패턴 정보 업데이트
     *
     * 패턴 파일을 업로드 하는 위치를 조회한다. 해당경로는 manager가 설치된 위치 + /update 이다.
     *
     * @return
     */
    public String selectUpdatePath() throws BaseException {
        String updatePath = null;
        updatePath = managerMapper.selectUpdatePath();
        return updatePath;
    }

    /**
     * 즉시 무결성 검사
     *
     * @throws BaseException
     */
    public void directWebConsoleIngeterity() throws BaseException {
        ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
        managerSvc.directConsoleIntegrity();
    }

    /**
     * DB백업 즉시백업 기능
     *
     * @param dto
     * @return
     * @throws BaseException
     */
    public long insertImDbBackup(ImDbBackupDto dto, HttpSession session) throws BaseException {
        String nowDate = DateTimeUtil.getNowDate("yy_MM_dd");
//            managerMapper.insertImDbBackup(dto);
//
//            insertLIndex = dto.getlIndex();
        execNowDbBackup(dto, session);
        logger.info("dbBackup param >>> "+dto.toMultiLineString());
        //dto.setlIndex(insertLIndex);
        return managerMapper.insertImDbBackupFile(dto);
    }

    /**
     * 웹콘솔 무결성 파일 등록 최초 실행시 삭제 후 등록
     *
     * @param list
     * @throws BaseException
     */
    public void insertConsoleIntegrityFile(List<IntegrityFile> list) throws BaseException {

        ManagerIntegrityFileDto dto = new ManagerIntegrityFileDto();
        dto.setFileList(list);
        managerMapper.deleteConsoleIntegrityFile();
        managerMapper.insertConsoleIntegrityFile(dto);
    }

    /**
     * 웹콘솔 무결성 파일 등록 최초 실행시 삭제 후 등록
     *
     * @param list
     * @throws BaseException
     */
    @Transactional
    public void insertConsoleIntegrityFileBatch(List<IntegrityFile> list) throws BaseException {
        managerMapper.deleteConsoleIntegrityFile();
        for (IntegrityFile dto : list) {
            managerMapper.insertConsoleIntegrityFileBatch(dto);
        }
    }

    /**
     * 웹 콘솔 무결성 검사 파일 목록 조회
     *
     * @return
     * @throws BaseException
     */
    public List<IntegrityFile> selectConsoleIntegrityFileList() throws BaseException {
        List<IntegrityFile> rtnList = new ArrayList<>();
        rtnList = managerMapper.selectConsoleIntegrityFileList();
        return rtnList;
    }

    /**
     * 웹 콘솔 무결성 검사 파일 세팅
     *
     * @throws BaseException
     */
    public void initConsoleIntegrityFile() throws BaseException {
        consoleIntegrityFile = selectConsoleIntegrityFileList();
    }

    /**
     * 웹 콘솔 무결성 검사 실행(자동)
     *
     * @throws BaseException
     */
    public void execConsoleIntegrity() throws BaseException {
        String baseDir = FileUtil.getCurrentPath(FileUtil.WEB_ROOT_PATH);
        if (!SystemUtil.isOsWindows()) {
            baseDir = FileUtil.LINUX_ROOT;
        }
        boolean integrity = true;
        List<IntegrityFile> list = managerMapper.selectConsoleIntegrityFileList();
        for (IntegrityFile item : list) {
            
        	if (!item.getFileHashcode().equals(DigestUtils.extractFileHashSHA256(baseDir + item.getStrPath() + item.getStrFileName()))) {
                integrity = false;
                logger.debug("integrity false: " + integrity);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.WARNNING.getValue(), Constants.INTEGRITY_FAIL, "administrator", item.getStrPath() + item.getStrFileName());
            }
        }
        if (integrity) {
            logger.debug("integrity true: " + integrity);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.INTEGRITY_SUCCESS, "administrator");
        }
    }

    /**
     * 웹 콘솔 무결성 검사 실행(수동)
     *
     * @return
     * @throws BaseException
     */
    public boolean directConsoleIntegrity() throws BaseException {
        boolean integrity = true;
        String baseDir = FileUtil.getCurrentPath(FileUtil.WEB_ROOT_PATH);
        if (!SystemUtil.isOsWindows()) {
            baseDir = FileUtil.LINUX_ROOT;
        }
        List<IntegrityFile> list = managerMapper.selectConsoleIntegrityFileList();
        for (IntegrityFile item : list) {
            if (!item.getFileHashcode().equals(DigestUtils.extractFileHashSHA256(baseDir + item.getStrPath() + item.getStrFileName()))) {
                integrity = false;
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.WARNNING.getValue(), Constants.INTEGRITY_FAIL, "administrator", item.getStrPath() + item.getStrFileName());
            }
        }
        if (integrity) {
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.INTEGRITY_SUCCESS, "administrator");
        }
        return integrity;
    }

    public void execIntegrity() throws BaseException{
        FileUtil.writeIntegirtyFile();
    }

    public void execNowDbBackup(ImDbBackupDto dto, HttpSession session) throws BaseException{
        String fileExtension = ".sql";
        String nowDate = DateTimeUtil.getNowDate("yy_MM_dd_HHmmss");
        String folder = BACKUP_FOLDER + nowDate;
        
        ManagerBackupVO backup = new ManagerBackupVO();
        backup.setnDayTableCheckValue(Long.valueOf(dto.getnTableCheckValue()));
        backup.setnDayTableDeleteFlag(Long.valueOf(dto.getnTableDel()));
        backup.setStrDayFileName(dto.getStrFileName());
        String dayCheckVal = NumberUtil.intToBinaryString(dto.getnTableCheckValue());
        if (dayCheckVal.length() != 5) {
            for (int i = dayCheckVal.length(); i < 5; i++) {
                dayCheckVal = "0" + dayCheckVal;
            }
        }
        if (!dayCheckVal.equals("00000")) {
            String eventStartDay = DateTimeUtil.getDateToStr(DateTimeUtil.getStrToDate(dto.getTmTo(), "yyyy-MM-dd"), "yy_MM_dd");
            String startTableDate = DateTimeUtil.getDateToStr(DateTimeUtil.getStrToDate(dto.getTmFrom(), "yyyy-MM-dd"), "yy_MM_dd");
            FileUtil.createFolder(folder);
            //보안정책
            if (String.valueOf(dayCheckVal.charAt(0)).equals("1")) {
                for (int i = 0; i < POLICY_LOG.length; i++) {
                    execDbBackupCommnad(POLICY_LOG[i], folder + "/" + POLICY_LOG_FILE[i] + fileExtension);
                }
            }
            //세션
            if (String.valueOf(dayCheckVal.charAt(1)).equals("1")) {
                sessionBackup(folder, eventStartDay, startTableDate, backup);
            }
            //감사로그
            if (String.valueOf(dayCheckVal.charAt(2)).equals("1")) {
                auditBackup(folder, eventStartDay, startTableDate, backup);
            }
            //트래픽
            if (String.valueOf(dayCheckVal.charAt(3)).equals("1")) {
            	trafficBackup(folder, eventStartDay, startTableDate, backup);
            }
            //침입탐지
            if (String.valueOf(dayCheckVal.charAt(4)).equals("1")) {
                detectionBackup(folder, eventStartDay, startTableDate, backup);
            }

            //백업파일 생성시간 대기
            try {
				Thread.sleep(1000 * 20);
			} catch (InterruptedException e) {
				throw new BaseException(e);
			}
            FileUtil.zipDirectory(new File(folder), BACKUP_FOLDER + dto.getStrFileName() + "_" + nowDate + ".zip");
            FileUtil.deleteDirectory(folder);
            dto.setStrBackupFilePath(BACKUP_FOLDER + dto.getStrFileName() + "_" + nowDate + ".zip");
            //침입탐지 사용:{0}, 트래픽 사용:{1}, 보안감사 사용:{2}, 세션 사용:{3}, 보안정책:{4}, 백업기간:{5}, 폴더위치:{6}, 생성방식:{7}, 파일명:{8}, 테이블 삭제:{9}
            String msg = MessageUtil.getbuilMessage("audit.system.nowDbBackup", MessageUtil.getMessage("str.used" + dayCheckVal.charAt(4)),
                    MessageUtil.getMessage("str.used" + dayCheckVal.charAt(3)), MessageUtil.getMessage("str.used" + dayCheckVal.charAt(2)), MessageUtil.getMessage("str.used" + dayCheckVal.charAt(1)),
                    MessageUtil.getMessage("str.used" + dayCheckVal.charAt(0)), dto.getTmFrom() + "~" + dto.getTmTo(), BACKUP_FOLDER, MessageUtil.getMessage("str.backupFlag" + dto.getnFileFlag()),
                    dto.getStrFileName(), MessageUtil.getMessage("str.used" + dto.getnTableDel()));
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_DB_BACKUP_EXEC_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);
        
        }
        
        
    }

    public void execDbBackup_test() {
        ImDbBackupDto dto = new ImDbBackupDto();
        String fileExtension = ".sql";
        String nowDate = DateTimeUtil.getNowDate("yy_MM_dd");
        String folder = BACKUP_FOLDER + nowDate;
        ManagerBackupVO backup = managerMapper.selectDbBackup();
        String folderSep = "";
        if ((backup.getStrBackupPathName().lastIndexOf("/") + 1) != backup.getStrBackupPathName().length()) {
            folderSep = "/";
        }
        String backupFileName = backup.getStrBackupPathName() + folderSep + backup.getStrDayFileName() + "_" + nowDate + ".zip";
        dto.setnTableCheckValue((int) backup.getnDayTableCheckValue());
        dto.setnTableDel((int) backup.getnDayTableDeleteFlag());
        dto.setTmFrom(DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yyyy-MM-dd"), ((int) backup.getnDayBookDayBefore() * -1), "yyyy-MM-dd"));
        dto.setTmTo(DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yyyy-MM-dd"), -1, "yyyy-MM-dd"));
        dto.setStrBackupFilePath(backupFileName);
        managerMapper.insertImDbBackupFile(dto);
    }

    public void execDbBackup() throws BaseException{
        ImDbBackupDto dto = new ImDbBackupDto();
        try {
            String fileExtension = ".sql";
            String nowDate = DateTimeUtil.getNowDate("yy_MM_dd_HHmmss");
            String folder = BACKUP_FOLDER + nowDate;
            ManagerBackupVO backup = managerMapper.selectDbBackup();
            logger.info("select >>> "+backup.toMultiLineString());
            String folderSep = "";
            if ((backup.getStrBackupPathName().lastIndexOf("/") + 1) != backup.getStrBackupPathName().length()) {
                folderSep = "/";
            }
            String backupFileName = backup.getStrBackupPathName() + folderSep + backup.getStrDayFileName() + "_" + nowDate + ".zip";
            dto.setnTableCheckValue((int) backup.getnDayTableCheckValue());
            dto.setnTableDel((int) backup.getnDayTableDeleteFlag());
            dto.setTmFrom(DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yyyy-MM-dd"), ((int) backup.getnDayBookDayBefore() * -1), "yyyy-MM-dd"));
            dto.setTmTo(DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yyyy-MM-dd"), -1, "yyyy-MM-dd"));
            dto.setStrBackupFilePath(backupFileName);
            logger.info("backupFileName >> "+backupFileName);
            logger.info("dto >> "+dto.toMultiLineString());
            String dayCheckVal = NumberUtil.intToBinaryString((int) backup.getnDayTableCheckValue());
            if (dayCheckVal.length() != 5) {
                for (int i = dayCheckVal.length(); i < 5; i++) {
                    dayCheckVal = "0" + dayCheckVal;
                }
            }
            if (!dayCheckVal.equals("00000") && backup.getnDayConfigFlag() == 1) { //000001
                String eventStartDay = DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yy_MM_dd"), -1, "yy_MM_dd");
                String eventEndDay = DateTimeUtil.getChangeDay(DateTimeUtil.getNowDate("yy_MM_dd"), ((int) backup.getnDayBookDayBefore() * -1), "yy_MM_dd");
                FileUtil.createFolder(folder);
                //보안정책 (0)
                if (String.valueOf(dayCheckVal.charAt(0)).equals("1")) {
                    for (int i = 0; i < POLICY_LOG.length; i++) {
                        execDbBackupCommnad(POLICY_LOG[i], folder + "/" + POLICY_LOG_FILE[i] + fileExtension);
                    }
                }
                //세션 (1)
                if (String.valueOf(dayCheckVal.charAt(1)).equals("1")) {
                    sessionBackup(folder, eventStartDay, eventEndDay, backup);
                }
                //감사로그 (2)
                if (String.valueOf(dayCheckVal.charAt(2)).equals("1")) {
                    auditBackup(folder, eventStartDay, eventEndDay, backup);
                }
                //트래픽 (3)
                if (String.valueOf(dayCheckVal.charAt(3)).equals("1")) {
                    trafficBackup(folder, eventStartDay, eventEndDay, backup);
                }
                //침입탐지 (4)
                if (String.valueOf(dayCheckVal.charAt(4)).equals("1")) {
                    detectionBackup(folder, eventStartDay, eventEndDay, backup);
                }

                //백업파일 생성시간 대기
                Thread.sleep(1000 * 60);
                if (backup.getStrBackupPathName() != null && !backup.getStrBackupPathName().isEmpty()) {
                    FileUtil.createFolder(backup.getStrBackupPathName());
                }
                FileUtil.zipDirectory(new File(folder), backupFileName);
                FileUtil.deleteDirectory(folder);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.DB_BACKUP_SUCCESS, "administrator");
                managerMapper.insertImDbBackupFile(dto);
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.DB_BACKUP_FAIL, "administrator");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.DB_BACKUP_FAIL, "administrator");
        }
    }

    private void detectionBackup(String folder, String eventStartDay, String startTableDate, ManagerBackupVO backup) throws BaseException{
        String fileExtension = ".sql";
        //{"LOG_%", "RAWDATA_%", "APPLAYER_%", "FILEMETA_%"}
        List<String> deleteTables = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();
        //------ log -----------------------------
        map.put("tableName", LOG);
        map.put("tableDate", eventStartDay);
        map.put("startTableDate", startTableDate);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }

        //------ rowData -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", ROWDATA);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        //------ applayer -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", APPLAYER);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        //------ fileMeta -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", FILEMETA);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        //------ trafficLog -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", TRAFFICLOG);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        // 백업 후 삭제
        // 20초 대기 후 데이터 삭제 (백업 시간 대기)
//            Thread.sleep(1000 * 10);
        if (backup.getnDayTableDeleteFlag() == 1 && deleteTables.size() > 0) {
            oracleSvc.dropTables(deleteTables);
        }
    }

    private void trafficBackup(String folder, String eventStartDay, String startTableDate, ManagerBackupVO backup)throws BaseException {
        String fileExtension = ".sql";
            //{"IP_TRAFFIC_%", "PROTOCOL_%", "SERVICE_%", "TRAFFIC_IP_%"}
        List<String> deleteTables = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();
        //------ ipTraffic -----------------------------
        map.put("tableName", IPTRAFFIC);
        map.put("tableDate", eventStartDay);
        map.put("startTableDate", startTableDate);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        //------ protocol -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", PROTOCOL);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        //------ service -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", SERVICE);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        //------ trafficIp -----------------------------
        tables = new ArrayList<>();
        map.put("tableName", TRAFFICIP);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        if (backup.getnDayTableDeleteFlag() == 1 && tables.size() > 0) {
            tables = new ArrayList<>();
            tables = oracleSvc.selectTableDeleteNames(map);
            deleteTables.addAll(tables);
        }
        //========================================
        // 백업 후 삭제
        // 20초 대기 후 데이터 삭제 (백업 시간 대기)
//            Thread.sleep(1000 * 20);
        if (backup.getnDayTableDeleteFlag() == 1 && deleteTables.size() > 0) {
            oracleSvc.dropTables(deleteTables);
        }
    }

    private void auditBackup(String folder, String eventStartDay, String startTableDate, ManagerBackupVO backup) throws BaseException {
        String fileExtension = ".sql";

        List<String> deleteTables = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();
        //------ audit -----------------------------
        map.put("tableName", AUDIT);
        map.put("tableDate", eventStartDay);
        map.put("startTableDate", startTableDate);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        //========================================

        // 20초 대기 후 데이터 삭제 (백업 시간 대기)
//            Thread.sleep(1000 * 20);
        deleteTables = oracleSvc.selectTableDeleteNames(map);
        if (backup.getnDayTableDeleteFlag() == 1 && deleteTables.size() > 0) {
            oracleSvc.dropTables(deleteTables);
        }
    }

    private void sessionBackup(String folder, String eventStartDay, String startTableDate, ManagerBackupVO backup) throws BaseException{
        String fileExtension = ".sql";
        List<String> deleteTables = new ArrayList<>();
        List<String> tables = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();

        //------ session -----------------------------
        map.put("tableName", SESSION);
        map.put("tableDate", eventStartDay);
        map.put("startTableDate", startTableDate);
        tables = oracleSvc.selectTableNames(map);
        for (int i = 0; i < tables.size(); i++) {
            execDbBackupCommnad(tables.get(i), folder + "/" + tables.get(i) + fileExtension);
        }
        //========================================

        // 20초 대기 후 데이터 삭제 (백업 시간 대기)
//            Thread.sleep(1000 * 20);
        deleteTables = oracleSvc.selectTableDeleteNames(map);
        if (backup.getnDayTableDeleteFlag() == 1 && deleteTables.size() > 0) {
            oracleSvc.dropTables(deleteTables);
        }
    }

    public ManagerBackupVO getDayBookTiem() throws BaseException{
        ManagerBackupVO backup = new ManagerBackupVO();
        backup = managerMapper.selectDbBackup();
        if (backup != null && backup.getStrDayBookTime() != null) {
            String[] arr = backup.getStrDayBookTime().split(":");
            backup.setDayHour(Integer.parseInt(arr[0]));
            backup.setDayMin(Integer.parseInt(arr[1]));
            backup.setDaySec(Integer.parseInt(arr[2]));
        }
        return backup;
    }

    /**
     * 즉시 백업 실행
     *
     * @param table
     * @param file
     */
    private void execDbBackupCommnad(String table, String file) throws BaseException{
        byte[] arr;
		try {
			arr = jdbcPwd.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(e);
		}
        byte[] saltArr = new byte[8];
        System.arraycopy(arr, 0, saltArr, 0, 8);
        String salt = new String(saltArr);
        String root = salt + "root" + getKeyMac();
        List<String> list = new ArrayList<>();
        list.add("/bin/sh");
        list.add("-c");
        list.add("/usr/local/mysql/bin/mysqldump -u root -p" + root + " TESS " + table + " > " + file);
        SystemUtil.execCommand(list);
    }

    private static String getKeyMac() throws BaseException{
        String rtn = "";
        List<String> list = new ArrayList<>();
        list.add("/bin/sh");
        list.add("-c");
        list.add("cat /sys/class/net/$(ip route show default | awk '/default/ {print $5}')/address | cut -c 10- | tr -d [:]");
        List<String> command = SystemUtil.execCommand(list);
        if (command != null && command.size() > 0) {
            rtn = command.get(0);
        }
        return rtn;
    }

    /**
     * 데이터베이스 설정 관련 스케쥴 실행 메일전송, 데이터베이스 날짜별 삭제
     */
    public void dbCheckDelete()  throws BaseException{
        ManagerVO manager = selectDbManagement();
        int warn = manager.getnDiskWarn().intValue();
        int delete = manager.getnDiskUsage().intValue();
        int dbUsed = Integer.parseInt(dbUsageSvc.getDbUsed());
        List<AccountVO> mailList = accountSvc.selectAccountMailList(7L);
        if (dbUsed >= warn && dbUsed < delete) {
        	auditLogSvc.insertAuditLogMsg(3L, MessageUtil.getbuilMessage("dbmanagerment.warn.content", dbUsed), "administrator");
        	
            // 경고 메일 발송
            if (mailList != null && mailList.size() > 0) {
                boolean chk = SimpleMailSender.sendAuthMail(MessageUtil.getMessage("dbmanagerment.warn.title"), MessageUtil.getbuilMessage("dbmanagerment.warn.content", dbUsed), "administrator", mailList);
                String mailAuditMsg = "";
                long type = 0L;
                if (chk) {
                    mailAuditMsg = MessageUtil.getMessage("audit.send.mail.success");
                    type = 1L;
                } else {
                    mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
                    type = 2L;
                }
                for (AccountVO item : mailList) {
                    auditLogSvc.insertAuditLogMsg(type, mailAuditMsg, "administrator", item.getId(), MessageUtil.getbuilMessage("dbmanagerment.warn.content", dbUsed));
                }
            } else {
            	logger.info("db used warning mail receiver count : " + mailList.size() );
            }
        } else if (dbUsed >= delete) {
        	auditLogSvc.insertAuditLogMsg(3L, MessageUtil.getbuilMessage("dbmanagerment.warn.content", dbUsed), "administrator");
        	
            // 데이터 삭제
            int maxSize = 0;
            List<String> dateList = oracleSvc.selectTableDateList();
            String deleteTables = "";
            maxSize = dateList.size();
            for (int i = 0; i < maxSize; i++) {
                String tableDate1 = "";
                if (dateList.size() > i) {
                    tableDate1 = dateList.get(i);
                }
                if (tableDate1 != null && !tableDate1.isEmpty()) {
                    List<String> list = oracleSvc.selectTableNameList(tableDate1);
                    if (list != null && list.size() > 0) {
                        if (deleteTables.isEmpty()) {
                            deleteTables = list.toString();
                        } else {
                            deleteTables = deleteTables + ", " + list.toString();
                        }
                        oracleSvc.dropTables(list);
                    }
                }
                int dbCheck = Integer.parseInt(dbUsageSvc.getDbUsed());
                if (dbCheck < delete) {
                    break;
                }
            }
            
            // 삭제 메일 발송
            if (!deleteTables.isEmpty()) {
            	if (mailList != null && mailList.size() > 0) {
	                boolean chk = SimpleMailSender.sendAuthMail(MessageUtil.getMessage("dbmanagerment.tabledelete.title"), MessageUtil.getbuilMessage("audit.db.tabledelete.success", deleteTables), "administrator", mailList);
	                String mailAuditMsg = "";
	                long type = 0L;
	                if (chk) {
	                    mailAuditMsg = MessageUtil.getMessage("audit.send.mail.success");
	                    type = 1L;
	                } else {
	                    mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
	                    type = 2L;
	                }
	                for (AccountVO item : mailList) {
	                    auditLogSvc.insertAuditLogMsg(type, mailAuditMsg, "administrator", item.getId(), MessageUtil.getbuilMessage("audit.db.tabledelete.success", deleteTables));
	                }
	            } else {
	            	logger.info("db table delete mail receiver count : " + mailList.size() );
	            }
            }
        }
    }

    public List<ImDbBackupVO> selectDbBackupFileList(ImDbBackupDto dto) throws BaseException{
        List<ImDbBackupVO> rtnList = new ArrayList<>();
        rtnList = managerMapper.selectDbBackupFileList(dto);
        if (rtnList != null && rtnList.size() > 0) {
            Long totalRow = managerMapper.selectDbBackupFileListTotal(dto);
            rtnList.get(0).setTotalRow(totalRow);
            for (int i = 0; i < rtnList.size(); i++) {
                rtnList.get(i).setrNum((dto.getStartRowSize() + i + 1));
            }
        }
        return rtnList;
    }

    public ImDbBackupVO selectDbBackupFileDetail(ImDbBackupDto dto) throws BaseException {
        ImDbBackupVO rtn = new ImDbBackupVO();
        rtn = managerMapper.selectDbBackupFileDetail(dto);
        return rtn;
    }
}
