/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;

import com.kglory.tms.web.util.typehandler.NetworkByteOrderIpTypeHandler;

/**
 *
 * @author leecjong
 */
public class ServletUtils {
	private static Logger	logger	= LoggerFactory.getLogger(ServletUtils.class);
    public static String getHeaderInfo(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(200);
        sb.append("<< Header Infomation >>");
        sb.append(SystemUtil.LF);
        for (Enumeration<?> headers = req.getHeaderNames(); headers.hasMoreElements();) {
            String headerName = (String) headers.nextElement();
            for (Enumeration<?> values = req.getHeaders(headerName); values.hasMoreElements();) {
                sb.append(headerName);
                sb.append(": ");
                sb.append((String) values.nextElement());
                sb.append(SystemUtil.LF);
            }
        }
        return sb.toString();
    }

    public static String getParameters(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<< Http Parameters >>").append(SystemUtil.LF);

        Enumeration<?> values = req.getParameterNames();
        while (values.hasMoreElements()) {
            String name = (String) values.nextElement();
            String value = req.getParameterValues(name)[0];
            sb.append(name).append(" = ").append(value);

            if (values.hasMoreElements()) {
                sb.append(", ");
            }
        }
        //sb.delete(sb.length() - 3, sb.length());
        sb.append(SystemUtil.LF);
        return sb.toString();
    }
    
    public static String getParameterMap(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<< Http ParameterMap >>").append(SystemUtil.LF);
        
        Map<String, String[]> parameterMap = req.getParameterMap();
        
        for (String key : parameterMap.keySet()) {
            String[] value = parameterMap.get(key);
            
            sb.append(key).append(" = ").append(value.toString());
        }
        
        sb.append(SystemUtil.LF);
        return sb.toString();
    }

    public static String getRequestAttributs(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<< Reqeust Attributes Infomation >>").append(SystemUtil.LF);

        Enumeration<?> names = req.getAttributeNames();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = req.getAttribute(name);
            sb.append(name).append(": ").append(value);
            sb.append(SystemUtil.LF);
        }
        return sb.toString();
    }

    public static String getCookies(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<<  Cookies Infomation >>").append(SystemUtil.LF);

        Cookie[] c = req.getCookies();
        if (c != null) {
            for (int i = 0; i < c.length; i++) {
                Cookie cookie = c[i];
                sb.append(cookie.getName()).append(": ").append(cookie.getValue());
                sb.append(SystemUtil.LF);
            }
        }
        return sb.toString();
    }

    public static void close(InputStream in, OutputStream out) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
        	logger.error(e.getLocalizedMessage());
        }
    }

    public static void sendXmlContents(HttpServletResponse response, String xml) {
        try {
            response.setContentType(MediaType.APPLICATION_XHTML_XML_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(xml);
            response.flushBuffer();
        } catch (IOException e) {
        	logger.error(e.getLocalizedMessage());
        }
    }
    
    public static String getHandlerParameter(HandlerMethod handlerMethod) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("<<  Handler Parameter  >>").append(SystemUtil.LF);
        MethodParameter[] mParamter = handlerMethod.getMethodParameters();
        Method method = handlerMethod.getMethod();
//        Object[] allArge = method.getp
        
        return sb.toString();
    }
    
    public static Integer getIntegerRequest(HttpServletRequest request, String name) {
        String rtn = "0";
        rtn = request.getParameter(name);
        if (rtn == null || rtn.isEmpty()) {
            return null;
        }
        return Integer.parseInt(rtn);
    }
}
