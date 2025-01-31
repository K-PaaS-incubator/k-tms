package com.kglory.tms.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.kglory.tms.web.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.filter.SessionObject;
import com.kglory.tms.web.filter.TokenObject;
import com.kglory.tms.web.model.CommonBean;
import com.kglory.tms.web.model.CommonBean.ReturnType;
import com.kglory.tms.web.model.dto.AccountDto;
import com.kglory.tms.web.model.dto.LoginFormDto;
import com.kglory.tms.web.model.dto.LoginStatusDto;
import com.kglory.tms.web.model.validation.LoginFormDtoValidator;
import com.kglory.tms.web.services.AuthenticationService;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.services.systemSettings.SystemConfService;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
import com.kglory.tms.web.util.MessageUtil;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class AuthenticationController {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    LoginFormDtoValidator loginFormDtoValidator;
    @Autowired
    MessageSource messageSource;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AuditLogService auditLogService;
    @Autowired
    SystemConfService systemConfSvc;
    @Autowired
    AccountService accountService;

    /**
     *
     * @param dto
     * @param result
     * @param session
     * @return
     * @throws BaseException 
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonBean login(@RequestBody LoginFormDto dto, BindingResult result, HttpSession session, HttpServletRequest request) throws BaseException {

        try {
            //로그인 시도시 중복 로그인 메시지 삭제
            session.removeAttribute(AuthenticationService.SESSION_DUBLE);

            loginFormDtoValidator.validate(dto, result);
            String remoteIp = request.getRemoteAddr();
            // 테스트용
            if (remoteIp.equals("0:0:0:0:0:0:0:1")) {
                remoteIp = "127.0.0.1";
                logger.debug("remoteIp :: " + remoteIp);
            }
            dto.setLoginIp(remoteIp);
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : id=" + dto.getUsername() + ", remoteIp=" + remoteIp);
            }
            if (dto.getIsEpki().equals("1")) {
                authenticationService.login(dto, session);
            } else {
                if (result.hasErrors()) {
                    dto.setSuccessLogin("N");
                    String code = result.getAllErrors().get(0).getCode();
                    dto.setReturnType(ReturnType.warning);
                    dto.setErrorCode(code);
                    dto.setErrorMessage(messageSource.getMessage(code, null, Locale.getDefault()));
                    // 감사로그
                    auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.LOGIN_FAIL, dto.getUsername(), dto.getUsername(), dto.getLoginIp());
                } else {
                    // 로그인 정보 조회
                    authenticationService.login(dto, session);
                    if (dto.getReturnType() == ReturnType.success) {
                        // protect password and token.
                        dto.setPassword("");
                        dto.setToken("");
                        dto.setErrorMessage("");
                        if (logger.isDebugEnabled()) {
                            logger.debug("login Success ~~");
                        }

                        // 로그인 시 사용된 브라우저 정보 입력
                        session.setAttribute("browser", SystemUtil.getBrowserName(request));

                        // 감사로그
                        auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.LOGIN_SUCCESS, dto.getUsername(), dto.getUsername(), dto.getLoginIp());
                    } else {
                        if (dto.getSuccessLogin().equals("C")) {
                            dto.setErrorMessage(messageSource.getMessage(dto.getErrorCode(), null, Locale.getDefault()));
                            if (logger.isDebugEnabled()) {
                                logger.debug("admin first login ~~");
                            }
                        } else {
//                            dto.setErrorMessage(messageSource.getMessage(dto.getErrorCode(), null, Locale.getDefault()));
                            dto.setErrorMessage(MessageUtil.getbuilMessage(dto.getErrorCode(), systemConfSvc.getUserLockTime()));
                            if (logger.isDebugEnabled()) {
                                logger.debug("login Fail ~~");
                            }
                            auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.LOGIN_FAIL, dto.getUsername(), dto.getUsername(), dto.getLoginIp());
                        }
                    }
                }

            }
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            String errorCode = dto.getErrorCode();
            if (errorCode == null) {
                errorCode = "";
                logger.debug("error code null ~~~~~~~~~~~~~~~~");
            }
            dto = (LoginFormDto) e.getErrorBean(dto);
            if (logger.isDebugEnabled()) {
                logger.debug("Login Fail Message : " + e.getLocalizedMessage());
                logger.debug("login fail error code : " + errorCode);
            }
            if (errorCode.equals("login.fail.info")) {
                // 패스워드가 틀렸을 경우  
                auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.LOGIN_FAIL, dto.getUsername(), dto.getUsername(), dto.getLoginIp());
            } else if (errorCode.equals("login.fail.timeout")) {
                // 로그인 시도 횟수 초과로 락 타임아웃 상태일 경우
                auditLogService.insertAuditLogMsg(1L, MessageUtil.getbuilMessage("login.success.lock", dto.getUsername(), dto.getLoginIp()), dto.getUsername());
            } else if (errorCode.equals("login.fail.lockout")) {
                // 계정잠금 상태인 사용자로 로그인 시도 실패일 경우 
                auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.LOGIN_FAIL, dto.getUsername(), dto.getUsername(), dto.getLoginIp());
            } else if (errorCode.equals("login.fail.lock")) {
                // 관리자 로그인 실패로 일시 잠김
                try {
                    auditLogService.insertAuditLogMsg(2L, MessageUtil.getMessage("audit.admin.login.fail.lock"), dto.getUsername(), systemConfSvc.getUserLockTime(), dto.getUsername(), dto.getLoginIp());
                } catch(BaseException ee) {
                    logger.error(ee.getLocalizedMessage());
                }
            } else if (errorCode.equals("login.fail.pause")) {
                // 일반사용자 로그인 실패로 계정 정지
                try {
                    auditLogService.insertAuditLogMsg(2L, MessageUtil.getMessage("audit.user.login.fail.lock"), dto.getUsername(), dto.getUsername(), dto.getLoginIp());
                } catch(BaseException ee) {
                    logger.error(ee.getLocalizedMessage(), ee);
                }
            } else {
                auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.LOGIN_FAIL, dto.getUsername(), dto.getUsername(), dto.getLoginIp());
            }
        } 
        return dto;
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonBean logout(HttpSession session, HttpServletRequest request) {
        CommonBean commonBean = new CommonBean();
        try {
            // 감사로그 - 로그아웃 서비스를 실행한 후 감사로그를 넣으면 행위자에 대한 정보가 DB에 기록되지 않는다.
            auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.LOGOUT_SUCCESS, (String) session.getAttribute("Username"), session.getAttribute("Username"), request.getRemoteAddr());
            authenticationService.logout(session);
            commonBean.setReturnType(ReturnType.success);
            commonBean.setErrorCode("");
            commonBean.setErrorMessage("");
        } catch (BaseException e) {
            // Service등에서 알 수 있는 메시지 발생
            logger.error(e.getLocalizedMessage(), e);
            commonBean = e.getErrorBean(commonBean);
        }
        return commonBean;
    }

    @RequestMapping(value = "/requireLogin", method = RequestMethod.POST)
    @ResponseBody
    public LoginStatusDto requireLogin(HttpSession session) {
        LoginStatusDto loginStatusDto = new LoginStatusDto();
        Object obj = session.getAttribute(AuthenticationService.SESSION_OBJ);

        // 세션 체크
        if (obj == null) {
            loginStatusDto.setLoginYN(false);
        } else {
            loginStatusDto.setUserName((String) session.getAttribute("Username"));
            Object loginDate = session.getAttribute("LoginDate");
            // 로그인 날짜
            if (loginDate == null) {
                loginStatusDto.setLoginDate(new Date(session.getCreationTime()));
            } else {
                loginStatusDto.setLoginDate((Date) loginDate);
            }
            // 제어권
            if (SessionObject.getController().equals(loginStatusDto.getUserName())) {
                loginStatusDto.setController(true);
            }
            loginStatusDto.setCategory(Long.parseLong(session.getAttribute("Category").toString()));		// 범위 분류 (0: 전체, 1: 네트워크 그룹, 2: 네트워크)
            loginStatusDto.setRefIndex(Long.parseLong(session.getAttribute("RefIndex").toString()));		// 네트워크 그룹 또는 네트워크 인덱스
            loginStatusDto.setRole(Long.parseLong(session.getAttribute("RoleNo").toString()));
            loginStatusDto.setLoginYN(true);
            loginStatusDto.setPathName((String) session.getAttribute("PathName"));
            loginStatusDto.setUserIndex((Long) session.getAttribute("UserIndex"));
            loginStatusDto.setSystemType(Constants.getSystemMode());
            loginStatusDto.setLoginType(Constants.getLoginMode());
            loginStatusDto.setLoginIpCount(Constants.getLoginIpCount());
            loginStatusDto.setDualSystem(Constants.getDualSystem());    // 시스템 이중화
        }
        return loginStatusDto;
    }

    @RequestMapping(value = "/requireLogin0", method = RequestMethod.POST)
    @ResponseBody
    public LoginStatusDto requireLogin0(HttpSession session) {
        LoginStatusDto loginStatusDto = new LoginStatusDto();
        Object sessionToken = session.getAttribute("Token");

        // 세션 token 체크
        if (sessionToken == null) {
            loginStatusDto.setLoginYN(false);
        } else {
            String tokenObjectToken = TokenObject.getToken((String) session.getAttribute("Username"));
            if (tokenObjectToken != null && tokenObjectToken.equals(sessionToken)) {
                loginStatusDto.setUserName((String) session.getAttribute("Username"));
                Object loginDate = session.getAttribute("LoginDate");
                // 로그인 날짜
                if (loginDate == null) {
                    loginStatusDto.setLoginDate(new Date(session.getCreationTime()));
                } else {
                    loginStatusDto.setLoginDate((Date) loginDate);
                }
                // 제어권
                if (SessionObject.getController().equals(loginStatusDto.getUserName())) {
                    loginStatusDto.setController(true);
                }
                loginStatusDto.setCategory(Long.parseLong(session.getAttribute("Category").toString()));		// 범위 분류 (0: 전체, 1: 네트워크 그룹, 2: 네트워크)
                loginStatusDto.setRefIndex(Long.parseLong(session.getAttribute("RefIndex").toString()));		// 네트워크 그룹 또는 네트워크 인덱스
                loginStatusDto.setRole(Long.parseLong(session.getAttribute("RoleNo").toString()));
                loginStatusDto.setLoginYN(true);
                loginStatusDto.setPathName((String) session.getAttribute("PathName"));
            } else {
                loginStatusDto.setLoginYN(false);
            }
        }
        return loginStatusDto;
    }

    @RequestMapping(value = "/api/requirePresence", method = RequestMethod.POST)
    @ResponseBody
    public List<LoginStatusDto> requirePresence() {
        ArrayList<LoginStatusDto> loginStatusDtos = new ArrayList<LoginStatusDto>();
        Set<String> users = SessionObject.getUsers();
        for (String user : users) {
            LoginStatusDto loginStatusDto = new LoginStatusDto();
            loginStatusDto.setUserName(user);
            HttpSession session = SessionObject.getSession(user);
            Object loginDate = session.getAttribute("LoginDate");
            if (loginDate == null) {
                loginStatusDto.setLoginDate(new Date(session.getCreationTime()));
            } else {
                loginStatusDto.setLoginDate((Date) loginDate);
            }
            if (SessionObject.getController().equals(user)) {
                loginStatusDto.setController(true);
            }
            loginStatusDtos.add(loginStatusDto);
        }
        return loginStatusDtos;
    }

    @RequestMapping(value = "/api/acquireController", method = RequestMethod.POST)
    @ResponseBody
    public CommonBean acquireController(HttpSession session) {
        return null;
    }

    @RequestMapping(value = "/api/releaseController", method = RequestMethod.POST)
    @ResponseBody
    public CommonBean releaseController(HttpSession session) {
        SessionObject.setController("");
        CommonBean result = new CommonBean();
        result.setReturnType(ReturnType.success);
        return result;
    }

    @RequestMapping(value = "/api/requireController", method = RequestMethod.POST)
    @ResponseBody
    public CommonBean requireController(HttpSession session, HttpServletRequest request) {
        CommonBean result = new CommonBean();
        String userName = (String) session.getAttribute("Username");
        String currentController = SessionObject.getController();
        if (currentController == null || currentController.isEmpty()) {
            SessionObject.setController(userName);
            result.setReturnType(ReturnType.success);
        } else {
            if (userName.equals(currentController)) {
                return releaseController(session);
            } else {
                result.setReturnType(ReturnType.warning);
                result.setErrorCode("controller.exist");
                result.setErrorMessage(messageSource.getMessage(result.getErrorCode(), new String[]{
                    currentController, request.getLocalAddr()},
                        Locale.getDefault()));
            }
        }
        return result;
    }

    @RequestMapping(value = "/api/loginDuplication", method = RequestMethod.POST, produces = "application/text;charset=utf-8")
    @ResponseBody
    public String isDuplecationLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        try {
            String sessionId = session.getId();
            String dupl = (String) session.getAttribute(AuthenticationService.SESSION_DUBLE);
            logger.debug("sessionId : " + sessionId);
            logger.debug("dupl : " + dupl);
            if (dupl != null && !dupl.isEmpty()) {
                result = messageSource.getMessage("login.fail.double.logout", null, Locale.getDefault());
            }
        } catch (NoSuchMessageException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/api/loginInitData", method = RequestMethod.POST, produces = "application/text;charset=utf-8")
    @ResponseBody
    public String initLoginData(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws org.codehaus.jackson.JsonGenerationException, JsonMappingException {
        String rtn = "";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        try {
        	List<String> allowIps = accountService.selectLoginIpList();
            if (allowIps.size() != 0 && !allowIps.contains(request.getRemoteHost())) {
                map.put("status", "405");
                auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ERROR.getValue(), Constants.UNAUTHORIZED_IP, request.getRemoteHost(), request.getRemoteAddr());
            } else {
            	String epkiServerCert = "";
//              if (Constants.getLoginMode() == Constants.LOGIN.EPKI.getValue()) {
//                  epkiServerCert = EpkiEncryption.getStrServerCert();
//              }
              map.put("strServerCert", epkiServerCert);
              map.put("sessionID", session.getId());
              map.put("loginMode", String.valueOf(Constants.getLoginMode()));
            }
            rtn = objectMapper.writeValueAsString(map);
        } catch (IOException | BaseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    @RequestMapping(value = "/api/updateAdmin", method = RequestMethod.POST, produces = "application/text;charset=utf-8")
    @ResponseBody
    public String modifyAdmin(@RequestBody AccountDto dto, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String rtn = "";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        try {
            String remoteIp = request.getRemoteAddr();
            // 테스트용
            if (remoteIp.equals("0:0:0:0:0:0:0:1")) {
                remoteIp = "127.0.0.1";
            }
            CommonBean bean = authenticationService.updateAdmin(dto, remoteIp);
            // 관리자 아이디 비밀번호 변경시 감사로그 생성
            if (bean.getErrorMessage() == null || bean.getErrorMessage().isEmpty()) {
                auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.ACCOUNT_MOD_SUCCESS, "admin", "admin", "ID:" + dto.getId());
            }
            map.put("errorMessage", bean.getErrorMessage());
            rtn = objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (BaseException e) {
        	logger.error(e.getLocalizedMessage(), e);
		}
        return rtn;
    }
}
