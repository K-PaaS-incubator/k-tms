/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.services.systemSettings;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.mapper.preferences.AccountMapper;
import com.kglory.tms.web.mapper.systemSettings.SystemConfMapper;
import com.kglory.tms.web.model.dto.ManagerDto;
import com.kglory.tms.web.model.dto.SystemConfDto;
import com.kglory.tms.web.model.dto.SystemDto;
import com.kglory.tms.web.model.vo.ManagerVO;
import com.kglory.tms.web.model.vo.SystemConfVO;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.file.FileUtil;
import com.kglory.tms.web.util.security.AesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author leecjong
 */
@Service("systemConfSvc")
public class SystemConfService {

    private static Logger logger = LoggerFactory.getLogger(SystemConfService.class);
    @Autowired
    SystemConfMapper systemConfMapper;
    @Autowired
    AuditLogService auditLogSvc;
    @Autowired
    AccountService accountSvc;
    @Autowired
    AccountMapper accountMapper;

    private static HashMap<String, SystemConfVO> sysMap = new HashMap<>(20);

    public void init() throws BaseException {
        SystemConfService.sysMap.clear();
        List<SystemConfVO> sysConfList = this.getSystemConfList();
        Iterator<SystemConfVO> it = sysConfList.iterator();
        while (it.hasNext()) {
            SystemConfVO item = it.next();
            sysMap.put(item.getKey(), item);
        }
    }

    /**
     * session Time
     *
     * @return
     * @throws BaseException
     */
    public int getSessionTime() throws BaseException {
        if (sysMap.isEmpty()) {
            this.init();
        }
        return Integer.parseInt(getValue(SystemConfVO.Key.SESSION_TIMEOUT));
    }

    /**
     * 사용자 계정 잠김 시간
     *
     * @return
     * @throws BaseException
     */
    public int getUserLockTime() throws BaseException {
        if (sysMap.isEmpty()) {
            this.init();
        }
        return Integer.parseInt(getValue(SystemConfVO.Key.USER_LOCK_TIME));
    }

    /**
     * 사용자 로그인 실패 제한 횟수
     *
     * @return
     * @throws BaseException
     */
    public int getUserLockFailCount() throws BaseException {
        if (sysMap.isEmpty()) {
            this.init();
        }
        return Integer.parseInt(getValue(SystemConfVO.Key.USER_LOCK_FAIL_COUNT));
    }
    
    /**
     * sftp id 조회
     * @return
     * @throws BaseException 
     */
    public String getSftpId() throws BaseException {
        if (sysMap.isEmpty()) {
            this.init();
        }
        return getValue(SystemConfVO.Key.SFTP_ID);
    }
    
    /**
     * sftp 비밀번호 조회
     * @return
     * @throws BaseException 
     */
    public String getSftpPwd() throws BaseException {
        if (sysMap.isEmpty()) {
            this.init();
        }
        return getValue(SystemConfVO.Key.SFTP_PWD);
    }

    public String getValue(SystemConfVO.Key key) {
    	String value = sysMap.get(key.getValue()).getValue();
        return value;
    }

    /**
     * 로그인 설정정보 조회(전체)
     *
     * @return
     * @throws BaseException
     */
    public List<SystemConfVO> getSystemConfList() throws BaseException {
        List<SystemConfVO> rtnList = new ArrayList<>();
        rtnList = systemConfMapper.getSystemConfList();
        return rtnList;
    }

    /**
     * 로그인 설정정보 업데이트
     *
     * @param dto
     * @param list
     * @throws BaseException
     */
    public void updateSystemConf(SystemDto dto, List<SystemConfDto> list, HttpSession session) throws BaseException {
        try {
            //변경사항이 있을때만 변경
            boolean chk = this.updateSystemConfChange(dto, list, session);
            this.init();
            boolean loginIpChk = false;
            //로그인 허용 IP 저장 (security mode 에서만 저장 사용)
            if (Constants.getSystemMode().equals(Constants.SYSTEM.SECURITY.getValue())) {
            	
            	// 추후 변경 가능(로그인 혀용 IP)
                logger.debug("dto getLoginAuthIp: "+ dto.getLoginAuthIpList());
                logger.debug("=======================================================");
                List<String> loginIPList = accountMapper.selectAuthIpList();
                logger.debug("loginIPList: "+ loginIPList);
                if(!dto.getLoginAuthIpList().equals(loginIPList)) {
                    accountSvc.insertLoginAuthIp(dto.getLoginAuthIpList());
                    accountSvc.authLoginIpInti();
                    loginIpChk = true;
                }
            	
            }
            //감사로그
            if (chk == true || loginIpChk == true) {
                StringBuffer ipList = new StringBuffer();
                for(int i = 0 ; i < dto.getLoginAuthIpList().size() ; i++) {
                    ipList.append(dto.getLoginAuthIpList().get(i));
                    if (i != (dto.getLoginAuthIpList().size() -1)) {
                        ipList.append(", ");
                    }
                }
                String msg = MessageUtil.getbuilMessage("audit.system.login", getUserLockFailCount(), getUserLockTime(), 
                        getSessionTime(), ipList);
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_LOGIN_MOD_SUCCESS, (String) session.getAttribute("Username"), msg);                	
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(list.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_LOGIN_MOD_FAIL, (String) session.getAttribute("Username"));
            throw e;
        }
    }
    
    /**
     * sftp 정보 업데이트
     * @param dto
     * @param list
     * @param session
     * @throws BaseException 
     */
    public void updateSftpConf(SystemDto dto, List<SystemConfDto> list, HttpSession session) throws BaseException {
        boolean isAdd = false;
        String ftpId = "";
        if (getSftpId() == null || getSftpId().isEmpty()) {
            isAdd = true;
        }
        for (SystemConfDto conf : list) {
            if (conf != null && SystemConfVO.Key.SFTP_ID.getValue().equals(conf.getKey())) {
                ftpId = conf.getValue();
            }
        }
        try {
            //변경사항이 있을때만 변경
            boolean chk = this.updateSftpChange(dto, list);
            this.init();
            //감사로그
            if (chk == true) {
                if (isAdd) {
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_SFTP_ADD_SUCCESS, (String) session.getAttribute("Username"), ftpId);                	
                } else {
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.SYSTEM_SFTP_MOD_SUCCESS, (String) session.getAttribute("Username"), ftpId);                	
                }
                // sensor 전달 파일 생성 및 커멘드
                logger.info("list >>>>> "+list.toString());
                writeSftpInfo();
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(list.toString()));
            }
        } catch (BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
            if (isAdd) {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_SFTP_ADD_FAIL, (String) session.getAttribute("Username"), ftpId);
            } else {
                auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.SYSTEM_SFTP_MOD_FAIL, (String) session.getAttribute("Username"), ftpId);
            }
            throw e;
        }
    }

    /**
     * 변경사항 확인 true:변경, false:미변경
     *
     * @param managerInfo
     * @param managerDto
     * @param list
     * @return
     * @throws BaseException
     */
    public boolean updateSystemConfChange(SystemDto managerDto, List<SystemConfDto> list, HttpSession session) throws BaseException {
        boolean rtn = false;
        for (SystemConfDto dto : list) {

            if (dto != null && SystemConfVO.Key.SESSION_TIMEOUT.getValue().equals(dto.getKey())) {
                if (!dto.getValue().equals(getValue(SystemConfVO.Key.SESSION_TIMEOUT))) {
                    session.setMaxInactiveInterval(Integer.parseInt(dto.getValue()) * 60);
                    systemConfMapper.updateSystemConf(dto);
                    // 변경사항 확인 후 저장된 값과 전달받은 값이 불일치할 경우에 감사로그 남김 
                    rtn = true;
                }
            }
            if (dto != null && SystemConfVO.Key.USER_LOCK_FAIL_COUNT.getValue().equals(dto.getKey())) {		// 로그인 실패 
                if (!dto.getValue().equals(getValue(SystemConfVO.Key.USER_LOCK_FAIL_COUNT))) {
                    systemConfMapper.updateSystemConf(dto);
                    rtn = true;
                }
            }
            if (dto != null && SystemConfVO.Key.USER_LOCK_TIME.getValue().equals(dto.getKey())) {			// 계정 잠김 시간
                if (!dto.getValue().equals(getValue(SystemConfVO.Key.USER_LOCK_TIME))) {
                    systemConfMapper.updateSystemConf(dto);
                    rtn = true;
                }
            }
        }
        return rtn;
    }
    
    public boolean updateSftpChange(SystemDto managerDto, List<SystemConfDto> list) throws BaseException {
        boolean rtn = false;
        String sftpId = "";
        String sftpIdKey = "";
        String sftpPwd = "";
        String sftpPwdKey = "";
        int sftpIdIdx = 0;
        int sftpPwdIdx = 0;
        for (SystemConfDto dto : list) {
            if (dto != null && SystemConfVO.Key.SFTP_ID.getValue().equals(dto.getKey())) {
                sftpId = dto.getValue();
                sftpIdKey = dto.getKey();
                sftpIdIdx = dto.getIdx();
            }
            if (dto != null && SystemConfVO.Key.SFTP_PWD.getValue().equals(dto.getKey())) {
                sftpPwd = dto.getValue();
                sftpPwdKey = dto.getKey();
                sftpPwdIdx = dto.getIdx();
            }
        }
        SystemConfDto dto = new SystemConfDto();
        if (!sftpId.equals(getValue(SystemConfVO.Key.SFTP_ID))) {
            dto.setKey(sftpIdKey);
            dto.setValue(sftpId);
            dto.setIdx(sftpIdIdx);
            systemConfMapper.updateSystemConf(dto);
            rtn = true;
        }
        String chkPwd = "";
        if (getSftpId() != null && !getSftpId().isEmpty() && getSftpPwd() != null && !getSftpPwd().isEmpty()) {
        	logger.debug("getSftpId() >>>> "+getSftpId());
        	logger.debug("getSftpPwd() >>>> "+getSftpPwd());
        	chkPwd = AesUtil.decryptString(getSftpPwd(), getSftpId());
        }
        if (!chkPwd.equals(sftpPwd) || rtn == true) {
            String encPwd = "";
            if (sftpId != null && !sftpId.isEmpty() && sftpPwd != null && !sftpPwd.isEmpty()) {
                encPwd = AesUtil.encryptString(sftpPwd, sftpId);
            }
            dto = new SystemConfDto();
            dto.setKey(sftpPwdKey);
            dto.setValue(encPwd);
            dto.setIdx(sftpPwdIdx);
            systemConfMapper.updateSystemConf(dto);
            rtn = true;
        }
        return rtn;
    }
    
    public void writeSftpInfo() throws BaseException  {
    	try {
    		String id = getSftpId();
            String pwd = getSftpPwd();
            logger.info("writeSftpInfo sysMap >>>>"+sysMap);
            logger.info("writeSftpInfo() id >>>>>>>>>>"+id);
            logger.info("writeSftpInfo() pwd >>>>>>>>>>"+pwd);
            if (id != null && !id.isEmpty() && pwd != null && !pwd.isEmpty()) {
                pwd = AesUtil.decryptString(pwd, id);
            } else {
                pwd = "";
            }
            FileUtil.writeSftp(id, pwd);
    	}
    	catch (BaseException e) {
    		logger.error(e.getLocalizedMessage());
			// TODO: handle exception
    		FileUtil.writeSftp("", "");
		}
        
    }
}
