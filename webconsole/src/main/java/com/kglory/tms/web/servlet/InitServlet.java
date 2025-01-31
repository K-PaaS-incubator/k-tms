/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.servlet;

import com.kglory.tms.web.batch.ScheduleUtil;
import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.conf.ConfFile;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.ext.mail.SimpleMailSender;
import com.kglory.tms.web.model.dto.IntegrityFile;
import com.kglory.tms.web.model.vo.ManagerBackupVO;
import com.kglory.tms.web.model.vo.ManagerSyslogVO;
import com.kglory.tms.web.model.vo.SystemVO;
import com.kglory.tms.web.services.common.CreateTableService;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.securityPolicy.DetectionPolicyService;
import com.kglory.tms.web.services.systemSettings.ManagerService;
import com.kglory.tms.web.services.systemSettings.SystemConfService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.SpringUtils;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.file.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author leecjong
 */
public class InitServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(InitServlet.class);
    
    /**
     * 시스템 설정정보 세팅
     *
     * @param sc
     */
    @Override
    public void init(ServletConfig sc) {
        try {
            SpringUtils.initByServletContext(sc.getServletContext());
            ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
            SystemConfService systemConfSvc = (SystemConfService) SpringUtils.getBean("systemConfSvc");
            AccountService accountSvc = (AccountService) context.getBean("accountSvc");
            AuditLogService auditLogSvc = (AuditLogService) context.getBean("auditLogSvc");
            CreateTableService createTableSvc = (CreateTableService) context.getBean("createTableSvc");
            DetectionPolicyService detectionPolicySvc = (DetectionPolicyService) context.getBean("detectionPolicySvc");
            log.info("[InitServlet] Call config load start");
            ConfFile confFile = (ConfFile) SpringUtils.getBean("confFile");
            //설정 정보 로드
            confFile.load();
            log.info("[InitServlet] Call config load end");
            
            log.info("[InitServlet] Call system mode setting start");
            // 시스템 모드 세팅
            Constants.setSystemMode(confFile.getIntValue(ConfFile.SYSTEM_MODE));
            // 로그인 모드 세팅
            Constants.setLoginMode(confFile.getIntValue(ConfFile.LOGIN_MODE));
            // 사용자별 로그인 허용 IP 수
            Constants.setLoginIpCount(confFile.getIntValue(ConfFile.LOGIN_IP_COUNT)); 
            log.info("[InitServlet] Call system mode setting end");

            // 로그인 허용 IP setting
//            if (Constants.getSystemMode() == Constants.SYSTEM.SECURITY.getValue()) {
            log.info("[InitServlet] Auth Login IP Setting start ~~");
            accountSvc.authLoginIpInti();
            log.info("[InitServlet] Auth Login IP Setting end ~~");

//            }

            log.info("[InitServlet] Call table setting start");
            //log 날짜 테이블 생성
            createTableSvc.initCreateLogTable();
            log.info("[InitServlet] Call table setting end");

            log.info("[InitServlet] Call sessing setting start");
            //세션 시간 millisecond 변경(초기화)
            SessionListener.init(auditLogSvc);
            log.info("[InitServlet] Call sessing setting end");

            //스케쥴 초기화
            log.info("[InitServlet] Call schedule setting start");
            ScheduleUtil.initScheduler();
            log.info("[InitServlet] Call schedule setting end");
            
            log.info("[InitServlet] Call sensor file setting start");
            //초기 센서파일 생성(정책, 악성코드, 예외, 인바운드, 세션모니터링, 네트워크, 블랙리스트)
            detectionPolicySvc.initWriteSensorFile();
            log.info("[InitServlet] Call sensor file setting end");

            log.info("[InitServlet] Call login setting start");
            // 로그인 관련 설정 정보 setting
            systemConfSvc.init();
            int sessionTime = systemConfSvc.getSessionTime();
            log.info("[InitServlet] Call login setting end");
            
            log.info("[InitServlet] Call Integrity start");
            //무결성 검사 실행
            initIntegrity();
            log.info("[InitServlet] Call Integrity end");
            //스케쥴 실행
            log.info("[InitServlet] Call schedule start");
            initSchedule();
            log.info("[InitServlet] Call schedule end ");
            
            
            //TEST insert data scheduler
//            initTestScheduler();
            
         // mail server setting
            log.info("[InitServlet] Call mail server start");
            try {
            	initMailServer();
            }
            catch (BaseException e) {
            	log.error("sftp fail");
			}
            catch (Exception e) {
            	log.error("sftp fail");
            }
            log.info("[InitServlet] Call mail server end");
         // 초기 SFTP 설정
            log.info("[InitServlet] Call sftp setting start");
            try {
            	systemConfSvc.writeSftpInfo();
            }
            catch (BaseException e) {
            	log.error("sftp fail");
			}
            catch (Exception e) {
            	log.error("sftp fail");
            }

            // 언어 초기화
            try {
                changeLocale();
                log.info("[InitServlet] default locale changed");
            } catch (Exception e) {
                log.error("changing default locale failed");
            }

            // 복호화된 db_web.properties 삭제
            try {
                systemConfSvc.deleteDecryptedFile();
                log.info("[InitServlet] decrypted file deleted");
            } catch (Exception e) {
                log.error("decrypted file delete process failed");
            }

            log.info("[InitServlet] Call sftp setting end");

            log.info("[InitServlet] Start Web Console~~~~~");
            // 프로세스 실행시 감사로그 남김 
//            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_SERVICE_PROCESS_SUCCESS, "콘솔");
            // configFile 점검 용
            log.info(confFile.toStringInfo());
            
        } catch (BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        } catch (IOException e) {
        	log.error(e.getLocalizedMessage(), e);
		}
    }

    @Override
    public void destroy() {
    	AuditLogService auditLogSvc = (AuditLogService) SpringUtils.getBean("auditLogSvc");
        // 종료
        ScheduleUtil.stopSchedule();
        log.info("End Web Console ~~~~~~~~~~~~~");
//        auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_SERVICE_PROCESSEND_SUCCESS, "콘솔");
    }

    public void initIntegrity() {
        String integrityFile = "";
        if (SystemUtil.isOsWindows()) {
            integrityFile = FileUtil.getCurrentPath(FileUtil.WEB_ROOT_PATH) + "/" + FileUtil.INTEGRITY_FILENAME;
        } else {
            integrityFile = FileUtil.LINUX_ROOT + "/" + FileUtil.INTEGRITY_FILENAME;
        }
        log.debug("initIntegrity integrityFile: " + integrityFile);
//        String filePath = "D:\\TESS_TMS_WEB\\target\\ROOT" + "\\" + FileUtil.integrityFileName;
        try {
            ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
            
            if (FileUtil.isFile(integrityFile)) {
                //설치 후 최초 실행(무결성 파일 등록)
                List<IntegrityFile> list = FileUtil.readIntegrityFile(integrityFile);
                log.debug("integrityFile size : " + list.size());
                managerSvc.insertConsoleIntegrityFileBatch(list);
            }
//            if(!managerSvc.directConsoleIntegrity()) {
//            	Runtime runtime = Runtime.getRuntime();
//                runtime.exec("cmd /c start sc stop tms_webconsole");
//                log.debug("ConsoleIntegrity process : fail ~~~");
//            }
        } catch (BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
    
    public void initSchedule() {
        long minTime = 150L;
        long nAutoValue = 1L;
        //무결성 검사
        try {
            int min = Long.valueOf(minTime).intValue();
            int nAuto = Long.valueOf(nAutoValue).intValue();
            
            if (nAuto == 1) {
                ScheduleUtil.integrityStart(min);
            }
        } catch (BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        
        //날짜 별 로그 테이블 생성
        try {
            ScheduleUtil.createLogTableStart();
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        
        // 시간 동기화
        try {
            ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
            ManagerSyslogVO timeSync = managerSvc.selectManagerTimeSync();
//            log.info("manager timesync used >>> "+timeSync.getnUseTimeSync());
            if (timeSync != null && timeSync.getnUseTimeSync() == 1) {
                ScheduleUtil.timeSyncStart(timeSync.getnTimeSyncPeriod());
            }
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        
        // 데이터베이스 삭제 스케쥴
        try {
            ScheduleUtil.dbConfigStart();
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        
        // 데이터베이스 management 스케쥴
        try {
            ScheduleUtil.dbManagementStart();
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        
        // DB Backup
        try {
            ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
            ManagerBackupVO backup = managerSvc.getDayBookTiem();
            if (backup.getnDayConfigFlag() == 1) {
                ScheduleUtil.dbBackupStart(backup.getDayHour(), backup.getDayMin(), backup.getDaySec());
            }
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        
        // 센서 감사로그 메일 발송 스케쥴
        try {
            ScheduleUtil.sensorAuditMailStart();
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
    
    /**
     * mailServer setting
     */
    public void initMailServer() throws BaseException{
        try {
            ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
            SystemVO systemVo = managerSvc.selectSystemSettingInfo();
            if (systemVo != null) {
                SimpleMailSender.setMailServer(systemVo.getEmailServer()); 
                SimpleMailSender.setMailAuthId(systemVo.getEmailUserId()); 
                SimpleMailSender.setMailAuthPwd(systemVo.getEmailUserPwd());
                SimpleMailSender.setPort(systemVo.getEmailPort());
                SimpleMailSender.setMailSecurity(systemVo.getEmailSecurity());
            }
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
            SimpleMailSender.setMailServer(null); 
        }
    }
    
    private void initTestScheduler() {
        try {
            ScheduleUtil.testInsertStart();
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void changeLocale() {
        Locale fixedLocale = Locale.KOREA;
        Locale.setDefault(fixedLocale);
    }
}
