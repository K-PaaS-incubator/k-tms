package com.kglory.tms.web.services;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.conf.ConfFile;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.ext.mail.SimpleMailSender;
import com.kglory.tms.web.filter.SessionObject;
import com.kglory.tms.web.filter.TokenObject;
import com.kglory.tms.web.mapper.preferences.AccountMapper;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.CommonBean.ReturnType;
import com.kglory.tms.web.model.dto.AccountDto;
import com.kglory.tms.web.model.dto.LoginFormDto;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.systemSettings.SystemConfService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.servlet.SessionListener;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.MessageUtil;
import com.kglory.tms.web.util.NumberUtil;
import com.kglory.tms.web.util.SpringUtils;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.security.SeedCrypto64;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

@Service
public class AuthenticationService {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    MessageSource messageSource;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    SystemConfService systemConfSvc;
    @Autowired
    AccountService accountSvc;
    @Autowired
    AuditLogService auditLogSvc;

    public static final String SESSION_OBJ = "userLoginOject";
    public static final String SESSION_DUBLE = "userLoginDuble";
    private static Integer loginCountLimit = 3;
    private static Integer loginInitLimit = 5;
    private static Integer loginTimeLimit = 10;
    private static Integer sessionTime = 30;

    public void login(LoginFormDto dto, HttpSession session) throws BaseException {
        try {
            loginCountLimit = systemConfSvc.getUserLockFailCount();
            loginTimeLimit = systemConfSvc.getUserLockTime();
            sessionTime = systemConfSvc.getSessionTime();
//          logger.debug("LOGIN_COUNT_LIMIT : " + LOGIN_COUNT_LIMIT + ", LOGIN_TIME_LIMIT : " + LOGIN_TIME_LIMIT + ", SESSION_TIME : " + SESSION_TIME);

            int rtn = login0(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("login auth : id=" + dto.getUsername() + ", remoteIp=" + dto.getLoginIp());
                logger.debug("login result : value=" + rtn);
            }

            if (LoginFormDto.LoginStatus.SUCCESS.getValue() == rtn) {
                // 관리자 최초 id 체크
                if (isFistLoginCheck(dto.getUsername())) {
                    dto.setSuccessLogin("C");
                    dto.setErrorCode("login.first.admin");
                } else {
                    // 로그인 성공
                    loginSuccess(dto, session);
                }

            } else if (LoginFormDto.LoginStatus.ID_FAIL.getValue() == rtn || LoginFormDto.LoginStatus.PWD_FAIL.getValue() == rtn || LoginFormDto.LoginStatus.LOGIN_IP_FAIL.getValue() == rtn) {
                loginFail(dto);
                if (logger.isDebugEnabled()) {
                    logger.debug("login fail : return value=" + rtn + ", message=" + messageSource.getMessage("login.fail.info", null, Locale.getDefault()));
                }
                
//                if (LoginFormDto.LoginStatus.PWD_FAIL.getValue() == rtn) {
//                    
//                }
                // 아이디 또는 패스워드 오류, 허용 IP 오류
                throw new BaseException(messageSource, "login.fail.info", null, "", new Exception());

            } else if (LoginFormDto.LoginStatus.LOCK_FAIL.getValue() == rtn) {
                logger.debug("rtn: " + rtn);
                logger.debug("LoginFormDto.LoginStatus.LOCK_FAIL.getValue(): " + LoginFormDto.LoginStatus.LOCK_FAIL.getValue());
                loginFail(dto);
                // 계정 잠김 알림
                String[] param = new String[1];
                param[0] = String.valueOf(loginTimeLimit);
                if (logger.isDebugEnabled()) {
                    //logger.debug("login fail : " + messageSource.getMessage("login.fail.lock", null, Locale.getDefault()));
                    logger.debug("login fail cancel : " + MessageUtil.getbuilMessage("login.fail.lock", systemConfSvc.getUserLockTime()));
                }
                //throw new BaseException(messageSource, "login.fail.lock", param, "", new Exception());
                Object[] messageParameters = new Object[5];
                messageParameters[0] = systemConfSvc.getUserLockTime();
                throw new BaseException(messageSource, "login.fail.lock", messageParameters, "", new Exception());
            } else if (LoginFormDto.LoginStatus.LOCK_FAIL_SUCCESS.getValue() == rtn) {
                // 로그인 성공이나 계정 잠김시간 남은 상태
                if (logger.isDebugEnabled()) {
                    logger.debug("login fail : " + messageSource.getMessage("login.fail.timeout", null, Locale.getDefault()));		// TODO login.fail.lockout
                }
                throw new BaseException(messageSource, "login.fail.timeout", null, "", new Exception());							// TODO login.fail.lockout
            } else if (LoginFormDto.LoginStatus.USER_LOCK.getValue() == rtn) {
                // 계정 사용 안함 사용자
                if (logger.isDebugEnabled()) {
                    logger.debug("login user lock : " + messageSource.getMessage("login.fail.pause", null, Locale.getDefault()));
                }
                throw new BaseException(messageSource, "login.fail.pause", null, "", new Exception());
            } 
//                else if (LoginFormDto.LoginStatus.LOCK_FAIL_PWDINIT.getValue() == rtn) {
//                // 계정 비밀번호 초기화 (추후 사용)
//            }
        } catch (IllegalStateException ie) {
            logger.error("", ie, ie);
            if (ie.getCause().getClass().getName().equals("javax.crypto.BadPaddingException") || ie.getCause().getClass().getName().equals("java.lang.NullPointerException")) {
                throw new BaseException(messageSource, "session.timeout", null, "", new Exception());
            }
            throw new BaseException(messageSource, "errorCode", null, "", ie);
        } catch (BaseException be) {
            if (be.getMessageParameters() != null) {
                logger.debug("Message Params : " + be.getMessageParameters().toString());
            } else {
                logger.debug("Message Params Null~~~~~~~~~");
            }
            throw new BaseException(messageSource, be.getMessageKey(), be.getMessageParameters(), "", be);
        } catch (Exception e) {
            logger.error("", e, e);
            throw new BaseException(messageSource, "errorCode", null, "", e);
        }
    }

    public int login0(LoginFormDto dto) throws BaseException {
        int rtn = -99;
        AccountVO accountVO = new AccountVO();
        int loginPauseTime = loginTimeLimit;
        int loginInitCount = loginInitLimit;

        String isEpki = dto.getIsEpki();
        accountVO = accountMapper.selectAccountByUsername(dto.getUsername());

        if (accountVO == null) {
            return LoginFormDto.LoginStatus.ID_FAIL.getValue();
        }

        boolean isPass = SeedCrypto64.isPassword(dto.getUsername(), dto.getPassword(), accountVO.getPassword());

        if (logger.isDebugEnabled()) {
            logger.debug("LoginFormDto dto: " + StringUtil.logDebugMessage(dto.toString()));
        }

        if (!isPass) {
            if (accountVO.getLockout() == 1 || accountVO.getLockout() == 2) {
                int userFailCount = 0;
                if (accountVO.getFailCount() != null) {
                    userFailCount = accountVO.getFailCount();
                }
                // 계정이 잠김 상태에서 초기화 설정값 초과시 처리
//                    if (userFailCount >= (loginInitCount - 1)) {
//                        // 계정 비밀번호 초기 작업 부분(미정)
//                        return LoginFormDto.LoginStatus.LOCK_FAIL_PWDINIT.getValue();
//                    }
                if (accountVO.getLockout() == 2) {
                    return LoginFormDto.LoginStatus.USER_LOCK.getValue();
                } else {
                    // 계정 잠김 시간 지났을 경우 로그인 실패 초기화
                    boolean isOver = lockTimeOver(accountVO.getLoginDate(), loginTimeLimit);
                    if (accountVO.getLockout() == 1 && isOver == true) {
                        return LoginFormDto.LoginStatus.PWD_FAIL.getValue();
                    } else {
                        return LoginFormDto.LoginStatus.LOCK_FAIL.getValue();
                    }
                }

            } else {
                //비밀번호 실패로 인해 실패 횟수 증가
                logger.debug("LoginFormDto.LoginStatus.PWD_FAIL.getValue(): " + LoginFormDto.LoginStatus.PWD_FAIL.getValue());
                return LoginFormDto.LoginStatus.PWD_FAIL.getValue();
            }
        }
        // 계정 활성화 여부 확인
        if (accountVO.getAccountStatus() != null && accountVO.getAccountStatus() == 1) {
            // 계정 비활성 사용자 Alert
            return LoginFormDto.LoginStatus.USER_LOCK.getValue();
        }

        // 로그인 허용 IP 확인
//        if (Constants.getSystemMode() == Constants.SYSTEM.SECURITY.getValue()) {
            if (!AccountService.isLoginIPCheck(dto.getLoginIp()) && !isFistLoginCheck(dto.getUsername())) {
                return LoginFormDto.LoginStatus.LOGIN_IP_FAIL.getValue();
            }
//        } else {
//            //사용자 별 허용 IP 확인
//            if (!accountSvc.isUserLoginIp(dto.getUsername(), dto.getLoginIp())) {
//                return LoginFormDto.LoginStatus.LOGIN_IP_FAIL.getValue();
//            }
//        }
        // 기존 로그인으로 실패시 계정 잠금
        if (accountVO.getLockout() != null && (accountVO.getLockout() == 1 || accountVO.getLockout() == 2)) {
            if (accountVO.getLoginDate() != null) {
                boolean isOver = lockTimeOver(accountVO.getLoginDate(), loginPauseTime);
                if (isOver) { 	//계정 잠김 상태에서 활성 상태로 변경 (NLOCKOUT : 0 -> 1 변경)
                    // 로그인 성공, 계정 활성 상태로 변경
                    //return LoginFormDto.LoginStatus.SUCCESS.getValue(); 주석처리
                    // 계정 잠금 설정 여부에 따라 로그인 상태 남김 
                    if (accountVO.getLockout() == 2) {
                        return LoginFormDto.LoginStatus.USER_LOCK.getValue();
                    } else {
                        return LoginFormDto.LoginStatus.SUCCESS.getValue();
                    }
                } else {
                    // 계정 잠김 시간이 남은 상태
                    //return LoginFormDto.LoginStatus.LOCK_FAIL_SUCCESS.getValue();
                    if (accountVO.getLockout() == 2) {
                        return LoginFormDto.LoginStatus.USER_LOCK.getValue();
                    } else {
                        return LoginFormDto.LoginStatus.LOCK_FAIL_SUCCESS.getValue();
                    }
                }
            } else {
                // 로그인 성공
                return LoginFormDto.LoginStatus.SUCCESS.getValue();
            }
        } else {
            // 로그인 성공
            return LoginFormDto.LoginStatus.SUCCESS.getValue();
        }
    }

    public void loginFail(LoginFormDto loginDto) throws BaseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Login Fail {" + loginDto.toString() + "}");
        }

        //로그인 실패 값 세팅
        loginDto.setSuccessLogin("N");
        loginDto.setReturnType(ReturnType.warning);
        loginDto.setErrorCode("username.password.invalid");

        AccountVO user = accountMapper.selectAccountByUsername(loginDto.getUsername());
        AccountDto pa = new AccountDto();
        int failLimitCount = loginCountLimit;		// 시스템에 설정된 로그인 허용 제한 횟수
        if (user != null) {
            if (user.getLockout() != 2) {
                int userFailCount = 0;
                if (user.getFailCount() != null) {
                    userFailCount = user.getFailCount();	// 계정별 nFailedLogin 값  
                }
                if (userFailCount >= (failLimitCount - 1)) {
                    boolean isOver = lockTimeOver(user.getLoginDate(), loginTimeLimit);
                    // 계정 잠김 시간 지났을 경우 로그인 실패 초기화
                    if (user.getLockout() == 1 && isOver == true) {
                        pa.setLockout(0);
                        userFailCount = 0;
                    } else {
                        if (!NumberUtil.longEquals(user.getRole(), 7L)) {
                            pa.setLockout(2);
                            loginDto.setErrorCode("login.fail.pause");
                        } else {
                            loginDto.setErrorCode("login.fail.lock");
                            pa.setLockout(1);
                        }
                    }
                } else {
                    pa.setLockout(0);
                }
                pa.setFailCount(userFailCount + 1);
                pa.setUserIndex(user.getUserIndex());
                if (logger.isDebugEnabled()) {
                    logger.debug("Login Fail {userId=" + loginDto.getUsername() + ", failCount=" + pa.getFailCount() + "}");
                }
                accountMapper.updateLoginFail(pa);
                
                // 계정 잠김시 관리자 메일 발송
                if(pa.getLockout() == 1 || pa.getLockout() == 2) {
                    List<AccountVO> mailList = accountSvc.selectAccountMailList(7L);
                    if (mailList != null && mailList.size() > 0) {
                        String content = MessageUtil.getbuilMessage("login.fail.lock.content", loginDto.getUsername(), loginTimeLimit);
                        if (pa.getLockout() == 2) {
                            content = MessageUtil.getbuilMessage("login.fail.user.lock.content", loginDto.getUsername());
                        }
                        String mailAuditMsg = "";
                        long type = 0L;
                        try {
                        	boolean chk = SimpleMailSender.sendAuthMail(MessageUtil.getMessage("login.fail.lock.title"), content, "administrator", mailList);
                        	if (chk) {
                        		mailAuditMsg = MessageUtil.getMessage("audit.send.mail.success");
                        		type = 1L;
                        	} else {
                        		mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
                        		type = 2L;
                        	}
                        }
                        catch (BaseException e) {
							// TODO: handle exception
                        	mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
                    		type = 2L;
						}
                        catch (Exception e) {
                        	// TODO: handle exception
                        	mailAuditMsg = MessageUtil.getMessage("audit.send.mail.fail");
                    		type = 2L;
                        }
                        for (AccountVO item : mailList) {
                            auditLogSvc.insertAuditLogMsg(type, mailAuditMsg, "administrator", item.getId(), content);
                        }
                    }
                }
            }
        }
    }

    public void loginSuccess(LoginFormDto loginDto, HttpSession session) throws BaseException {
        AccountVO user = new AccountVO();

        if (logger.isDebugEnabled()) {
            logger.debug("Login Success {" + loginDto.toString() + "}");
        }

        AccountDto accountDto = new AccountDto();
        accountDto.setId(loginDto.getUsername());
        user = accountMapper.selectAccountByUsernameForPath(accountDto);
        AccountDto pa = new AccountDto();
        if (user != null) {
            //로그인 성공 값 세팅
            loginDto.setSuccessLogin("Y");
            loginDto.setToken(TokenObject.makeToken(loginDto.getUsername()));
            loginDto.setReturnType(ReturnType.success);
            loginDto.setErrorCode("");
            loginDto.setRole(user.getRole());
            loginDto.setSessionId(session.getId());
            if (logger.isDebugEnabled()) {
                logger.debug("Login Success {" + loginDto.toString() + "}");
                logger.debug("Login Success : " + loginDto.getSuccessLogin());
                logger.debug("Login Success returnType : " + loginDto.getReturnType());
            }
            // 로그인 완료 후 세션 관리
            session.setAttribute("Username", loginDto.getUsername());
            session.setAttribute("Token", loginDto.getToken());
            session.setAttribute("RoleNo", user.getRole());
            session.setAttribute("LoginDate", Calendar.getInstance().getTime());
            session.setAttribute("Category", user.getCategory());				// 범위 분류 (0: 전체, 1: 네트워크 그룹, 2: 네트워크)
            session.setAttribute("RefIndex", user.getRefIndex());				// 네트워크 그룹 또는 네트워크 인덱스
            session.setAttribute("PathName", user.getPathName());
            session.setAttribute("UserIndex", user.getUserIndex());
            session.setAttribute(SESSION_OBJ, loginDto);
            session.setMaxInactiveInterval(sessionTime * 60);

            // 제어권을 가지고 있으면 재로그인할 경우 제어권을 회수 한다.
            if (loginDto.getUsername().equals(SessionObject.getController())) {
                SessionObject.setController("");
            }

            SessionObject.addSession(loginDto.getUsername(), session);
            TokenObject.setToken(loginDto.getUsername(), loginDto.getToken());

            // 로그인 성공시 데이타 업데이트
            pa.setUserIndex(user.getUserIndex());
            accountMapper.updateLoginSuccess(pa);

            // 세션 관리에 추가
            SessionListener.addLoginSession(session, loginDto);

            // 관리 모드 확인 (일반, 보안) : 중복 로그인
            if (Constants.getSystemMode() == Constants.SYSTEM.SECURITY.getValue()) {
                SessionListener.checkDuplicationLogin(session.getId(), loginDto);
            }

            //성공 감사로그
        }
    }

    public void logout(HttpSession session) throws BaseException {
        String username = (String) session.getAttribute("Username");
        if (username != null && !username.isEmpty() && username.equals(SessionObject.getController())) {
            SessionObject.setController("");
        }
        SessionListener.loginCount--;
        session.removeAttribute("Username");
        session.invalidate();

    }

    /**
     * 현재 시간과 LOCK_DATE 시간 비교 처리 메소드 현재 시간과 LOCK_DATE 시간을 비교해서 Lock이 해제 가능하면 True or 해제 불가능하면 false를 반환 하는 메소드이다.
     *
     * @param lockDate - Locking된 시간
     * @return 결과값 반환, Lock이 해제 가능하면 True or 해제 불가능하면 false
     */
    private boolean lockTimeOver(String lockDate, int lockTimeOut) {
        Date today = new Date();
        Date lDate = DateTimeUtil.getStrToDateTime(lockDate, "yyyy-MM-dd HH:mm:ss");
        // TSYS_CONF 테이블에 저장된 user.lock.timeout 값(Default는 10분)을 가져와서 lockDate날짜에 더한다.
        Date tmp = DateTimeUtil.getChangeMinute(lDate, lockTimeOut);

        // lockDate에 timeout값을 더한 long값과 현재날짜의 long값을 비교
        return today.getTime() >= tmp.getTime();
    }

    public CommonBean updateAdmin(AccountDto dto, String remoteIp) throws BaseException {
        CommonBean rtn = new CommonBean();
        if (dto.getPassword() != null && !dto.getPassword().equals("")) {
            dto.setPassword(SeedCrypto64.encrypt(dto.getPassword(), dto.getId()));
        }
        ConfFile confFile = (ConfFile) SpringUtils.getBean("confFile");
        if (confFile.isLimitAdmin(dto.getId())) {
            rtn.setErrorMessage(MessageUtil.getbuilMessage("account.add.userid.limit", dto.getId()));
        } else {
            accountMapper.updateAdmin(dto);
            accountMapper.insertAuthIp(remoteIp);
            accountSvc.authLoginIpInti();
        }
        return rtn;
    }

    private boolean isFistLoginCheck(String id) {
        ConfFile confFile = (ConfFile) SpringUtils.getBean("confFile");
        if (confFile.isAdminFirstLogin(id)) {
            return true;
        } else {
            return false;
        }
    }
}
