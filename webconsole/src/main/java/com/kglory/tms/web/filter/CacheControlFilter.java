package com.kglory.tms.web.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public  class CacheControlFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(CacheControlFilter.class);
	private String MODE = "DENY";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
		
        resp.addHeader("X-FRAME-OPTIONS", MODE );
		resp.addHeader("X-Content-Type-OPTIONS", "nosniff" );
        resp.addHeader("X-XSS-Protection", "1; mode=block" );
        resp.addHeader("Vary", "*" );
        resp.addHeader("Expires", "-1" );
        resp.addHeader("Pragma", "no-cache" );
        resp.addHeader("Cache-control", "no-cache, no-store,max-age=0, must-revalidate" );
//        resp.setHeader("Set-Cookie", "; HttpOnly; SameSite=None");
        addCookie(req, resp);
        chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("CacheControlFilter init");
	}

	@Override
	public void destroy() {
		this.destroy();
	}
	
	public static void addCookie(HttpServletRequest request,HttpServletResponse response) {
/*        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            Cookie session = Arrays.stream(allCookies).filter(x -> x.getName().equals("JSESSIONID")).findFirst().orElse(null);

            if (session != null) {
                session.setHttpOnly(true);
                session.setSecure(true);
                response.addCookie(session);
            }
        }*/
        
        Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for(String tm : headers) {
        	if(firstHeader) {
        		response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; Secure; HttpOnly%s", tm, "; SameSite=strict"));
        		firstHeader = false;
        		continue;
        	}
        }
    }
    
}	