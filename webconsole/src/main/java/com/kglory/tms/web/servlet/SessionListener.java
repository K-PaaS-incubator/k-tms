/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.servlet;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.filter.SessionObject;
import com.kglory.tms.web.model.dto.LoginFormDto;
import com.kglory.tms.web.model.dto.LoginStatusDto;
import com.kglory.tms.web.services.AuthenticationService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.security.AesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class SessionListener implements HttpSessionListener {

    private static Logger log = LoggerFactory.getLogger(SessionListener.class);
    
    private static AuditLogService auditLogSvc;
    
    public static SessionListener sessionManager = null;
    public static ConcurrentHashMap<String, Object> sessionMonitor;
    public static ConcurrentHashMap<String, Object> loginSessionMonitor;
    public static ConcurrentHashMap<String, Object> loginObject;
    public static String controllerSessionId = "";

    private static int count;
    public static int loginCount;

    public SessionListener() {
        if (sessionMonitor == null) {
            sessionMonitor = new ConcurrentHashMap<>();
        }
        if (loginSessionMonitor == null) {
            loginSessionMonitor = new ConcurrentHashMap<>();
        }
        if (loginObject == null) {
            loginObject = new ConcurrentHashMap<>();
        }
        sessionManager = this;

    }

    public static void init(AuditLogService auditLogSvc) {
        if (loginSessionMonitor != null) {
            loginSessionMonitor.clear();
        }
        if (loginObject != null) {
            loginObject.clear();
        }
        loginCount = 0;
        if (SessionListener.auditLogSvc == null) {
            SessionListener.auditLogSvc = auditLogSvc;
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        count++;

        if (log.isDebugEnabled()) {
            log.debug(se.getSession().getId() + " created");
            log.debug("session count : " + getSessionCount());
        }
        HttpSession session = se.getSession();
        sessionMonitor.put(session.getId(), session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        count--;
        if (count < 0) {
            count = 0;
        }

        HttpSession session = se.getSession();
        LoginFormDto loginDto = (LoginFormDto) session.getAttribute(AuthenticationService.SESSION_OBJ);
        String loginIp = "";
        if (loginDto != null) {
            loginIp = loginDto.getLoginIp();
        }
        if (session.getAttribute("Username") != null) {
            loginCount--;
            if (log.isDebugEnabled()) {
                log.debug("session[" + se.getSession().getId() + "] destroyed UserId=" + session.getAttribute("Username") + ", loginIp=" + loginIp);
            }
            if (!loginIp.isEmpty()) {
                try {
					auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.LOGIN_SESSION_OUT,
					                (String) session.getAttribute("Username"), (String) session.getAttribute("Username"), loginIp);
				} catch (BaseException e) {
					// TODO Auto-generated catch block
					log.error(e.getLocalizedMessage());
				}
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("session[" + se.getSession().getId() + "] destroyed");
            }
        }
        AesUtil.destoryedPassPhrase(session);
        sessionMonitor.remove(session.getId());
        loginSessionMonitor.remove(session.getId());
        loginObject.remove(session.getId());
    }

    public static void addLoginSession(HttpSession session, LoginFormDto login) {
        loginCount++;
        log.debug("login session ID : " + session.getId() + ", loginCount : " + loginCount);
        loginSessionMonitor.put(session.getId(), session);
        loginObject.put(session.getId(), login);
    }

    public static LoginFormDto getLoginUser(HttpSession session) {
        return (LoginFormDto) loginObject.get(session.getId());
    }

    public static int getSessionCount() {
        return count;
    }

    public static boolean checkDuplicationLogin(String sessionId, LoginFormDto loginDto) throws BaseException {
        boolean ret = false;
        Enumeration<?> eNum = loginSessionMonitor.elements();
        String admId = loginDto.getUsername();
        while (eNum.hasMoreElements()) {
            HttpSession sh_session = null;
            sh_session = (HttpSession) eNum.nextElement();
            LoginFormDto seAdmin = (LoginFormDto) sh_session.getAttribute(AuthenticationService.SESSION_OBJ);
            if (seAdmin != null) {
                // 아이디 또는 관리자 1계정만 로그인 허용
                if ((admId.equals(seAdmin.getUsername()) && !sessionId.equals(sh_session.getId())) || 
                        (loginDto.isAdmin() && seAdmin.isAdmin() && !sessionId.equals(sh_session.getId()))) {
                    loginCount--;
                    // 전달 받은 아이디와(admId) 기존 세션값 중 아이디가 중복 되면
                    // 기존 세션을 소멸 시킨다.
                    if (log.isDebugEnabled()) {
                        log.debug("essionOut(Duble): " + seAdmin.getUsername());
                    }
                    auditLogSvc.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.DUPLE_LOGIN,
                                seAdmin.getUsername(), seAdmin.getUsername(), seAdmin.getLoginIp(), loginDto.getLoginIp());

                    // 해당 세션 무효화
//                    sh_session.invalidate();
                    sh_session.removeAttribute(AuthenticationService.SESSION_OBJ);
                    sh_session.setAttribute(AuthenticationService.SESSION_DUBLE, sessionId);
                    AesUtil.destoryedPassPhrase(sh_session);
                    
                    ret = true;
                    if (log.isDebugEnabled()) {
                        log.debug("sessionOut: " + ret);
                    }
                    break;
                }
            }
        }
        return ret;
    }

    public static List<LoginFormDto> getLoginUserSingleList() {
        List<LoginFormDto> rtnList = getLoginList();

        return userDubleCheck(rtnList);
    }

    public static List<LoginFormDto> getLoginList() {
        List<LoginFormDto> rtnList = new ArrayList<>();

        Enumeration<?> eNum = loginObject.elements();
        while (eNum.hasMoreElements()) {
            LoginFormDto loginUser = null;
            loginUser = (LoginFormDto) eNum.nextElement();
            rtnList.add(loginUser);
        }

        return rtnList;
    }

    public static ArrayList<LoginStatusDto> getLoginStatusList() {
        ArrayList<LoginStatusDto> rtnList = new ArrayList<>();

        List<LoginFormDto> loginList = getLoginList();
        for (LoginFormDto user : loginList) {
            HttpSession httpSession = (HttpSession) SessionListener.sessionMonitor.get(user.getSessionId());
            LoginStatusDto loginStatusDto = new LoginStatusDto();
            loginStatusDto.setUserName(user.getUsername());
            Object loginDate = httpSession.getAttribute("LoginDate");
            if (loginDate == null) {
                loginStatusDto.setLoginDate(new Date(httpSession.getCreationTime()));
            } else {
                loginStatusDto.setLoginDate((Date) loginDate);
            }
            if (SessionObject.getController().equals(user)) {
                loginStatusDto.setController(true);
            }
            rtnList.add(loginStatusDto);
        }
        return rtnList;
    }

    public static List<LoginFormDto> userDubleCheck(List<LoginFormDto> userList) {
        List<LoginFormDto> rtnList = new ArrayList<>();
        for (LoginFormDto item : userList) {
            boolean check = true;
            for (LoginFormDto uniqueItem : rtnList) {
                if (item.getUsername().equals(uniqueItem.getUsername())) {
                    check = false;
                    break;
                }
            }
            if (check) {
                rtnList.add(item);
            }
        }
        return rtnList;
    }

    /**
     * 세션 시간 초기화 Session time millisecond 로 변경
     *
     */
    public static void sessionReset(int time) {
        Enumeration<?> eNum = loginSessionMonitor.elements();
        while (eNum.hasMoreElements()) {
            HttpSession sh_session = null;
            sh_session = (HttpSession) eNum.nextElement();
            if (sh_session != null) {
                sh_session.setMaxInactiveInterval(time);
            }
        }
    }

    /**
     * 세션 초기화
     */
    public static void sessionInvalid() {
        Enumeration<?> eNum = loginSessionMonitor.elements();
        while (eNum.hasMoreElements()) {
            HttpSession sh_session = null;
            sh_session = (HttpSession) eNum.nextElement();
            if (sh_session != null) {
                log.debug("invalid ssid : " + sh_session.getId());
                AesUtil.destoryedPassPhrase(sh_session);
                sh_session.invalidate();
            }
        }
    }
}
