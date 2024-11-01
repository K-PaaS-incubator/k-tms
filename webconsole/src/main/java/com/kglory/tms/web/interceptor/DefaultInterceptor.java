/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.interceptor;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.model.dto.LoginFormDto;
import com.kglory.tms.web.services.AuthenticationService;
import com.kglory.tms.web.util.ServletUtils;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author leecjong
 */
public class DefaultInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(DefaultInterceptor.class);

    private List<String> permsAllowUrl = new ArrayList<String>();
    private String uri = "/index.html";

    public void setPermsAllowUrl(List<String> permsAllowUrl) {
        this.permsAllowUrl = permsAllowUrl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getServletPath();
        
        boolean result = false;
        HttpSession session = request.getSession();
        String paramToken = request.getHeader("_csrf");
        String cookieToken = null;
//        for (Cookie cookie : request.getCookies()) {
//            if ("CSRF_TOKEN".equals(cookie.getName())) {
//                cookieToken = URLDecoder.decode(cookie.getValue(), "UTF-8"); // 재사용이 불가능하도록 쿠키 만료 
//                cookie.setPath("/");
//                cookie.setValue("");
//                cookie.setMaxAge(0);
//                cookie.setSecure(true);
//                response.addCookie(cookie);
//                break;
//            }
//        }
//        if(cookieToken != null && !cookieToken.equals(paramToken)) {
//            return false;
//        }

        Object obj = (LoginFormDto) session.getAttribute(AuthenticationService.SESSION_OBJ);

        if (obj == null) {
            if (isAjaxRequest(request)) {
                if (isPassUrl(url)) {
                    return true;
                } else {
                    response.sendRedirect(uri);
                    return false;
                }
            } else {
                if (isPassUrl(url)) {
                    return true;
                }
            }
        } else {
            LoginFormDto login = (LoginFormDto) obj;
            if (url.startsWith("/api/")) {
                String[] arrUrl = url.split("/");
                String checkUrl = "";
                if (arrUrl != null && arrUrl.length > 2) {
                    checkUrl = "/" + arrUrl[1] + "/" + arrUrl[2] + "/";
                }
                if (permsAllowUrl.contains(checkUrl)) {
                    if ((login.getRole() & Constants.AUTH_CONTROLCONTROLLER) != Constants.AUTH_CONTROLCONTROLLER || (login.getRole() & Constants.AUTH_SUPERADMIN) == Constants.AUTH_SUPERADMIN) {
                        // 관리자 제어권 확인 (추후 기능 추가)
//                        if(!session.getId().equals(SessionListener.ControllerSessionId)) {
//                            // 수정 권한이 없음
//                            return false;
//                        }
                    	// 권한 오류 페이지 이동
                    	response.sendRedirect(uri);
                    	return false;
                    }
                }

            }
            return true;
        }

        response.sendRedirect(uri);
        return result;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    	super.postHandle(request, response, handler, modelAndView);
    }

    // 현재 요청이 AJAX 요청인지 체크

    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");

        return accept.contains("application/json");
    }

    private boolean isPassUrl(String url) {
        boolean rtn = false;
        if (url.startsWith("/api/login") || url.contains(".js") || url.contains(".css") || url.contains(".gif")
                || url.contains(".png") || url.contains(".jpg") || url.contains(".html") || url.contains(".csv")
                || url.contains(".mfd") || url.startsWith("/api/updateAdmin")) {
            rtn = true;
        }
        return rtn;
    }
}
