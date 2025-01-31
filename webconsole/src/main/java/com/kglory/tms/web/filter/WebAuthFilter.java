package com.kglory.tms.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kglory.tms.web.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import com.kglory.tms.web.services.AuthenticationService;
import com.kglory.tms.web.services.preferences.AccountService;
import com.kglory.tms.web.util.SpringUtils;

public class WebAuthFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(WebAuthFilter.class);

    private String loginUri = "/api/login";
    private String firstLoginUri = "/api/updateAdmin";
    private String localeUri = "/api/common/getLocale";
    private String keyUri = "/api/common/getAesKey";

    private List<String> exceptionUrl = new ArrayList<String>();
    private List<String> roleCheckUrl = new ArrayList<String>();

    @Override
    public void destroy() {
        this.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    	try {
    		HttpServletRequest httpRequest = (HttpServletRequest) request;
    		HttpServletResponse httpResponse = (HttpServletResponse) response;
    		HttpSession session = httpRequest.getSession();
    		String uri = httpRequest.getRequestURI();
    		httpRequest.setCharacterEncoding("UTF-8");
    		
    		Object obj = session.getAttribute(AuthenticationService.SESSION_OBJ);
    		logger.debug("uri : " + uri);
//            logger.debug("param strDescription >>>"+request.getParameter("strDescription"));
    		
    		//ip체크
    		String username = (String) session.getAttribute("Username");
    		if(username != null) {
    			checkAuthIp(request, session);
    		}

            // 브라우저 변경 체크
            String originBrowser =  (String) session.getAttribute("browser");
            if (originBrowser != null && originBrowser != SystemUtil.getBrowserName(httpRequest)) {
                session.invalidate();
            }


    		//관리자 권한 체크
//            logger.info("forlks roleCheckUrl.size() >>>> "+roleCheckUrl.size());
//            logger.info("forlks roleCheckUrl.indexOf(uri) >>>> "+roleCheckUrl.indexOf(uri));
    		if (roleCheckUrl.size() > 0 && roleCheckUrl.indexOf(uri) > -1) {
    			boolean authCheck = roleCheckURL(session);
    			
//            	logger.info("forlks authCheck >>>> "+authCheck);
    			if(!authCheck) {
    				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
    			}
    		}
    		
    		// Script files, style files, image files is not filter.
    		if (obj != null || uri.startsWith(loginUri) || uri.startsWith(keyUri) || uri.startsWith(localeUri) || uri.startsWith(firstLoginUri) || uri.contains(".js") 
    				|| uri.contains(".css") || uri.contains(".gif") || uri.contains(".png") || uri.contains(".jpg") || uri.contains(".html") 
    				|| uri.contains(".csv") || uri.contains(".mfd")) {
    			if (exceptionUrl.size() > 0 && exceptionUrl.contains(uri)) {
    				filterChain.doFilter(httpRequest, httpResponse);
    			} else {
    				filterChain.doFilter(new RequestWrapper(httpRequest), httpResponse);
    			}
    			return;
    			
    		} else {
    			passRequest(response, httpResponse);
    		}

    	}
    	catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			throw e;
		}
    	catch (ServletException e) {
    		logger.error(e.getLocalizedMessage());
    		// TODO: handle exception
    		throw e;
    	}
    	catch (Exception e) {
    		logger.error(e.getLocalizedMessage());
    		throw e;
    	}
    }
    
    /**
     * 세션 체크(관리자 가능하도록)
     */
    public boolean roleCheckURL(HttpSession session) {
    	boolean result = false;
    	Long roleNo = (Long) session.getAttribute("RoleNo");
    	
//    	logger.info("forlks roleNo >>>>> "+ roleNo);
    	
    	if(roleNo != null && roleNo.intValue() == 7) {
    		result = true;
    	}
//    	logger.info("forlks result >>>>> "+ result);
    	
    	return result;
    }

    /**
     * 접속 IP 체크(허용된 아이피 아닐경우 logout)
     * @param request
     * @param session
     */
    private void checkAuthIp(ServletRequest request, HttpSession session) {
		// TODO Auto-generated method stub
    	String remoteIp = request.getRemoteAddr();
    	if (remoteIp.equals("0:0:0:0:0:0:0:1")) {
            remoteIp = "127.0.0.1";
        }
    	
    	AccountService accountSvc = (AccountService) SpringUtils.getBean("accountSvc");
    	boolean result = accountSvc.isLoginIPCheck(remoteIp);
    	
    	if(!result) {
    		AuthenticationService authenticationService = (AuthenticationService) SpringUtils.getBean("authenticationService");
				
//				String username = (String) session.getAttribute("Username");
//				
//				AuditLogService auditLogService = (AuditLogService) SpringUtils.getBean("auditLogSvc");
//				auditLogService.insertAuditLog(Constants.AUDIT_LTYPE1.ACTION.getValue(), Constants.LOGOUT_SUCCESS, username, username, request.getRemoteAddr());

			session.invalidate();
				//authenticationService.logout(session);
    	}
	}

	private void releaseCotroller() {
        String controller = SessionObject.getController();
        if (controller != null && !controller.isEmpty()) {
            HttpSession session = SessionObject.getSession(controller);
            try {
                session.getCreationTime();
            } catch (IllegalStateException e) {
                SessionObject.setController("");
            }
        }
    }

    private void passRequest(ServletResponse response, HttpServletResponse httpResponse)
            throws IOException {
    	
//        String str = "location.href='/';";
//        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        httpResponse.setContentType("text/html; charset=UTF-8");
//        httpResponse.setCharacterEncoding("utf-8");
//        PrintWriter pw = response.getWriter();
//        pw.print(str);
//        pw.flush();
//        pw.close();
    	Collection<String> headers = httpResponse.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for(String tm : headers) {
        	if(firstHeader) {
        		httpResponse.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; HttpOnly%s", tm, "; SameSite=strict"));
        		firstHeader = false;
        		continue;
        	}
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        exceptionUrl.add("/api/securityPolicy/signatureRuleCheck");
        exceptionUrl.add("/api/securityPolicy/insertUserSignature");
        exceptionUrl.add("/api/securityPolicy/updateUserSignature");
        exceptionUrl.add("/api/securityPolicy/updateUserSignatureDetail");
        exceptionUrl.add("/api/common/fileDownload.do");
        
        //roleCheck url(관리자 인지 여부 체크) 
        //등록 수정 위주
        roleCheckUrl.add("/api/common/fileDownload.do");
        //1. 시스템 설정
        roleCheckUrl.add("/api/systemSetting/updateManagerSettingInfo");
        roleCheckUrl.add("/api/systemSetting/updateManagerIntegrityInfo");
        roleCheckUrl.add("/api/systemSetting/updateDbManagement");
        roleCheckUrl.add("/api/systemSetting/updateManagerTimeSync");
        roleCheckUrl.add("/api/systemSetting/updateDbBackup");
        roleCheckUrl.add("/api/systemSetting/insertImDbBackup");
        roleCheckUrl.add("/api/systemSetting/directWebConsoleIngeterity");
        roleCheckUrl.add("/api/systemSetting/deleteNetworkIpBlockLid");
        roleCheckUrl.add("/api/systemSetting/insertNetworkIpBlockList");
        roleCheckUrl.add("/api/systemSetting/updateNetworkDetailInfo");
        roleCheckUrl.add("/api/systemSetting/insertNetworkDetailInfo");
        roleCheckUrl.add("/api/systemSetting/deleteNetworkSettingInfo");
        //2.사용자 계정
        roleCheckUrl.add("/api/preferences/insertAdminAccountDetails");
        roleCheckUrl.add("/api/preferences/updateAccountDetails");
        roleCheckUrl.add("/api/preferences/updateUserAccountDetails");
        roleCheckUrl.add("/api/preferences/insertUserAccountDetails");
        roleCheckUrl.add("/api/preferences/deleteAccountList");
        roleCheckUrl.add("/api/preferences/deleteAccountGroupList");
        roleCheckUrl.add("/api/preferences/insertGroupAccountDetails");
        roleCheckUrl.add("/api/preferences/updateGroupAccountDetails");
        roleCheckUrl.add("/api/preferences/updateAccountDefault");
//        roleCheckUrl.add("/api/updateUserAccountDefault");
        //3.침입탐지 설정
        roleCheckUrl.add("/api/securityPolicy/updateDetectionPolicy");
        roleCheckUrl.add("/api/securityPolicy/insertUserSignature");
        roleCheckUrl.add("/api/securityPolicy/updateUserSignatureHelp");
        roleCheckUrl.add("/api/securityPolicy/updateUserSignature");
        roleCheckUrl.add("/api/securityPolicy/updateUserSignatureDetail");
        roleCheckUrl.add("/api/securityPolicy/deleteUserSignature");
        roleCheckUrl.add("/api/securityPolicy/insertSignatureClassType");
        roleCheckUrl.add("/api/securityPolicy/deleteSignatureClassType");
        roleCheckUrl.add("/api/securityPolicy/updateIntrusionDetectionResponse");
        
        //4.악성코드 성정
        roleCheckUrl.add("/api/securityPolicy/insertYaraRule");
        roleCheckUrl.add("/api/securityPolicy/updateYaraRule");
        roleCheckUrl.add("/api/securityPolicy/deleteYaraRuleGroupType");
        roleCheckUrl.add("/api/securityPolicy/insertYaraGroup");
        roleCheckUrl.add("/api/securityPolicy/deleteYaraUserRule");
        
        roleCheckUrl.add("/api/common/test");
        
    }
}
