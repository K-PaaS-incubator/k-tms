/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

/**
 *
 * @author leecjong
 */
public class MessageUtil {

    private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    public static String getMessage(String code) {
        String rtn = "";
        try {
            MessageSource messageSource = (MessageSource) SpringUtils.getBean("messageSource");
            rtn = messageSource.getMessage(code, null, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            logger.error("Message is Empty Code : " + code);
        }
        return rtn;
    }

    public static String getbuilMessage(String code, Object... parameters) {
        String rtn = "";
        try {
            rtn = MessageFormat.format(getMessage(code), parameters);
        } catch (NoSuchMessageException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    public static String getFilterViewEnableColor(Integer enable, Integer color) {
        StringBuffer msg = new StringBuffer();
        msg.append("[");
        msg.append(MessageUtil.getMessage("str.used1"));
        msg.append(":");
        msg.append(MessageUtil.getMessage("str.used" + enable));
        msg.append(", Color:");
        msg.append(MessageUtil.getMessage("filterView.color" + color));
        msg.append("]");
        return msg.toString();
    }
    
    public static String getFilterViewEnable(Integer enable) {
        StringBuffer msg = new StringBuffer();
        msg.append("[");
        msg.append(MessageUtil.getMessage("str.used1"));
        msg.append(":");
        msg.append(MessageUtil.getMessage("str.used" + enable));
        msg.append("]");
        return msg.toString();
    }
    
    public static String getUserIsLock(Integer lockout) {
    	StringBuffer result = new StringBuffer();
    	if(lockout == null) {
    		return "-";
    	}
    	switch (lockout.intValue()) {
		case 1:
			result.append("계정 잠김");
			break;

		default:
			result.append("로그인 허용");
			break;
		}
    	return result.toString();
    }
    
    public static String getServiceName(Integer port) {
    	StringBuffer result = new StringBuffer();
    	if(port == null) {
    		return "-";
    	}
    	switch(port.intValue()) {
    		case 20 :
    			result.append("FTP-DATA");
    			break;
    		case 21 :
    			result.append("FTP");
    			break;
    		case 22 :
    			result.append("SSH");
    			break;
    		case 23 :
    			result.append("TELNET");
    			break;
    		case 25 :
    			result.append("SMTP");
    			break;
    		case 43 :
    			result.append("NICNAME");
    			break;
    		case 80 :
    			result.append("HTTP");
    			break;
    		case 110 :
    			result.append("POP");
    			break;
    		case 137 :
    			result.append("NETBIOS-NS");
    			break;
    		case 139 :
    			result.append("NETBIOS-SSN");
    			break;
    		case 443 :
    			result.append("HTTPS");
    			break;
    		case 1863 :
    			result.append("MSN");
    			break;
    		default : 
    			result.append("-");
    			
    	}
    	if(port.intValue() > 0) {
    		result.append(" - ");
    		result.append(port);
    	}
    	
    	return result.toString();
    }
}
