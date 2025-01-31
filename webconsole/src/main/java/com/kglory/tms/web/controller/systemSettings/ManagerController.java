package com.kglory.tms.web.controller.systemSettings;

import java.util.HashMap;
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
import com.kglory.tms.web.model.dto.ImDbBackupDto;
import com.kglory.tms.web.model.dto.ManagerBackupDto;
import com.kglory.tms.web.model.dto.ManagerDto;
import com.kglory.tms.web.model.dto.ManagerIntegrityFileDto;
import com.kglory.tms.web.model.dto.ManagerSyslogDto;
import com.kglory.tms.web.model.dto.SystemDto;
import com.kglory.tms.web.model.vo.ImDbBackupVO;
import com.kglory.tms.web.model.vo.ManagerBackupVO;
import com.kglory.tms.web.model.vo.ManagerSyslogVO;
import com.kglory.tms.web.model.vo.ManagerVO;
import com.kglory.tms.web.model.vo.SystemConfVO;
import com.kglory.tms.web.model.vo.SystemVO;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.systemSettings.ManagerService;
import com.kglory.tms.web.services.systemSettings.SystemConfService;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.SystemUtil;
import com.kglory.tms.web.util.security.AesUtil;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ManagerController {

    @Autowired
    MessageSource messageSource;
    @Autowired
    ManagerService managerService;
    @Autowired
    SystemConfService systemConfSvc;
    @Autowired
    AccountService accountSvc;

    private static Logger logger = LoggerFactory.getLogger(ManagerController.class);

    /**
     * 매니저 등록 정보를 조회 한다
     *
     * @return ManagerVO
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/selectManagerSettingInfo", method = RequestMethod.POST)
    @ResponseBody
    public SystemVO selectManagerSettingInfo() throws BaseException {

        SystemVO result = null;
        try {
            result = managerService.selectSystemSettingInfo();
            List<SystemConfVO> rtnList = systemConfSvc.getSystemConfList();
            try {
            	for (int i = 0 ; i < rtnList.size() ; i++) {
            		if (rtnList.get(i).getKey().equals(SystemConfVO.Key.SFTP_PWD.getValue()) && systemConfSvc.getSftpId() != null && !systemConfSvc.getSftpId().isEmpty()) {
            			String p = AesUtil.decryptString(rtnList.get(i).getValue(), systemConfSvc.getSftpId());
            			rtnList.get(i).setValue(AesUtil.encrypt(systemConfSvc.getSftpId(), p));
            		}
            	}
            }
            catch (BaseException e) {
            	logger.info("sftp decryption >>>"+e.getLocalizedMessage(), e);
            }
            catch (Exception e) {
            	logger.info("sftp decryption >>>"+e.getLocalizedMessage(), e);
            }
            
            result.setSystemConfList(rtnList);
            logger.debug("setSystemConfList: "+ result.getSystemConfList().size());
            // Security 버전에서 만 조회
            result.setLoginAuthIpList(accountSvc.selectLoginIpList());
            if(!"".equals(result.getEmailUserPwd()) && result.getEmailUserPwd() != null) {
            	result.setEmailUserPwd(AesUtil.encrypt(result.getEmailUserId(), result.getEmailUserPwd()));
            }
            
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (result == null) {
            return new SystemVO();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
            }
            return result;
        }
    }

    /**
     * 매니저 등록 정보를 업데이트 한다
     *
     * @return Map<String, Integer>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/updateManagerSettingInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateManagerSettingInfo(@RequestBody SystemDto dto, HttpSession session) throws BaseException {
        int rtnValue = 0;
        try {
            rtnValue = managerService.updateManagerSettingInfo(dto, session);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
        }

        HashMap<String, Integer> rtn = new HashMap<>();
        rtn.put("managerSettingRtn", rtnValue);
        return rtn;
    }

    /**
     * 매니저 무결성 검사 주기 설정을 조회한다
     *
     * @return ManagerVO
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/selectManagerIntegrityInfo", method = RequestMethod.POST)
    @ResponseBody
    public ManagerVO selectManagerIntegrityInfo() throws BaseException {

        ManagerVO result = new ManagerVO();
        try {
            result = managerService.selectManagerIntegrityInfo();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (result == null) {
            return new ManagerVO();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
            }
            return result;
        }
    }

    /**
     * 매니저 무결성 검사 파일 목록을 업데이트 한다
     *
     * @return List<ManagerIntegrityFileVO>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/updateManagerIntegrityInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateManagerIntegrityInfo(@RequestBody ManagerIntegrityFileDto dto, HttpSession session) throws BaseException {
        int rtnValue = 0;
        HashMap<String, Integer> rtn = new HashMap<>();
        try {
            rtnValue = managerService.updateManagerIntegrityInfo(dto, session);

			// 센서 무결성 수정 후, 감사로그 Todo
            // auditLogService.insertAuditLog(BigInteger.valueOf(2),
            // BigInteger.valueOf(10003), dto.getUsername(),
            // dto.getUsername(), (String) session.getAttribute("LocalAddr"),
            // (String) session.getAttribute("RemoteAddr"));
            rtn.put("managerIntegrityRtn", rtnValue);
            //logger.debug("managerIntegrityRtn : " + rtnValue);

            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    @RequestMapping(value = "/api/systemSetting/selectDbManagement", method = RequestMethod.POST)
    @ResponseBody
    public ManagerVO selectDbManagement() throws BaseException {
        ManagerVO result = null;
        try {
            result = managerService.selectDbManagement();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("result : " + (result.toString()));
        }

        if (result == null) {
            return new ManagerVO();
        } else {
            return result;
        }
    }

    @RequestMapping(value = "/api/systemSetting/updateDbManagement", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateDbManagement(@RequestBody ManagerDto dto, HttpSession session) throws BaseException {
        HashMap<String, Integer> rtn = new HashMap<>();
        try {
            int resultValue = managerService.updateDbManagement(dto, session);
            rtn.put("managerDbSettingRtn", resultValue);

            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    @RequestMapping(value = "/api/systemSetting/getDbPassword", method = RequestMethod.POST)
    @ResponseBody
    public ManagerVO getDbPassword() throws BaseException {
        ManagerVO result = null;
        try {
            result = managerService.getDbPassword();
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("result : " + (result.toString()));
        }

        if (result == null) {
            return new ManagerVO();
        } else {
            return result;
        }
    }

    @RequestMapping(value = "/api/systemSetting/modifyDbPassword", method = RequestMethod.POST)
    @ResponseBody
    public void modifyDbPassword(@RequestBody ManagerDto dto, HttpSession session) throws BaseException {
        try {
            if (dto.getStrDbpwdInput() != null && !dto.getStrDbpwdInput().equals(""))
            managerService.modifyDbPassword(dto, session);
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @RequestMapping(value = "/api/systemSetting/selectManagerTimeSync", method = RequestMethod.POST)
    @ResponseBody
    public ManagerSyslogVO selectManagerTimeSync() throws BaseException {
        ManagerSyslogVO result = null;
        try {
            result = managerService.selectManagerTimeSync();
            if (logger.isDebugEnabled()) {
                logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/api/systemSetting/updateManagerTimeSync", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateManagerTimeSync(@RequestBody ManagerSyslogDto dto, HttpSession session) throws BaseException {
        HashMap<String, Integer> rtn = new HashMap<>();
        try {
            int resultValue = managerService.updateManagerTimeSync(dto, session);
            rtn.put("managerTimeSyncRtn", resultValue);

            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    @RequestMapping(value = "/api/systemSetting/selectDbBackup", method = RequestMethod.POST)
    @ResponseBody
    public ManagerBackupVO selectDbBackup() throws BaseException {
        ManagerBackupVO result = null;
        try {
            result = managerService.selectDbBackup();

            if (logger.isDebugEnabled()) {
                if(result != null) {
                    logger.debug("result : " + StringUtil.logDebugMessage(result.toString()));
                } else {
                    logger.debug("result : NULL ~~~");
                }
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        if (result == null) {
            return new ManagerBackupVO();
        } else {
            return result;
        }
    }

    @RequestMapping(value = "/api/systemSetting/updateDbBackup", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Integer> updateDbBackup(@RequestBody ManagerBackupDto dto, HttpSession session) throws BaseException {
        HashMap<String, Integer> rtn = new HashMap<>();
        try {
            int resultValue = managerService.updateDbBackup(dto, session);
            rtn.put("managerBackupRtn", resultValue);

            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    /**
     * 즉시백업 실행
     *
     * @param dto, session
     * @return Map<String, Long>
     * @throws BaseException
     */
    @RequestMapping(value = "/api/systemSetting/insertImDbBackup", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Long> insertImDbBackup(@RequestBody ImDbBackupDto dto, HttpSession session) throws BaseException {
        long insertLIndex = 0;
        try {
            insertLIndex = managerService.insertImDbBackup(dto, session);

        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            logger.debug("result : lIndex=" + insertLIndex);
        }

        HashMap<String, Long> returnValue = new HashMap<String, Long>();
        returnValue.put("lIndex", insertLIndex);
        return returnValue;
    }
    
    /**
     * 무결성 즉시 실행
     * @throws BaseException 
     */
    @RequestMapping(value = "/api/systemSetting/directWebConsoleIngeterity", method = RequestMethod.POST)
    @ResponseBody
    public void directWebConsoleIngeterity() throws BaseException {
    	try {
    		managerService.directWebConsoleIngeterity();
            managerService.execIntegrity();
            SystemUtil.execIntegrityCommand();	//sensor 무결성 추가
    	} catch (BaseException e) {
    		logger.error(e.getLocalizedMessage(), e);
    	} catch (Exception e) {
    		logger.error(e.getLocalizedMessage(), e);
    	}
    }
    
    /**
     * 백업파일 목록 조회
     * @param dto
     * @return 
     */
    @RequestMapping(value = "/api/systemSetting/selectBackupFileList", method = RequestMethod.POST)
    @ResponseBody
    public List<ImDbBackupVO> selectDbBackupFileList(@RequestBody ImDbBackupDto dto){
        List<ImDbBackupVO> rtnList = new ArrayList<>();
        try {
            rtnList = managerService.selectDbBackupFileList(dto);
        } catch(BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch(Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtnList;
    }
}
