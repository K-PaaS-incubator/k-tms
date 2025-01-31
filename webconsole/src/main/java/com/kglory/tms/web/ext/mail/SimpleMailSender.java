/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.ext.mail;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.util.DateTimeUtil;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class SimpleMailSender {
    private static final Logger logger = LoggerFactory.getLogger(SimpleMailSender.class);
    private static final String TLS_WITH_VERSION = "TLSv1.2";
    
    private static String mailServer = "gwa.kglory.co.kr";
    private static String mailAuthId;
    private static String mailAuthPwd;
    private static String port = "25";
    private static String mailSecurity;
    
    public static boolean sendMail(String title, String content, String userId, List<AccountVO> receivers) {
        if (mailServer != null && !mailServer.isEmpty()) {
            
            String from = makeSender();
            String msg = makeContent(content, userId);
            Properties prop = System.getProperties();
            prop.put("mail.smtp.host", mailServer);
            prop.put("mail.smtp.user", "tas");
            prop.put("mail.smtp.port", port);
            Session session = Session.getDefaultInstance(prop, null);
            try {
                MimeMessage message = new MimeMessage(session);

                if (from != null) {
                    message.setFrom(new InternetAddress(from, "tas"));
                }

                if (receivers != null && receivers.size() > 0) {
                    for(AccountVO item : receivers) {
                        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(item.getEmail()));
                    }
                }

                message.setSubject(title, "UTF-8");
                message.setHeader("TESS TAS", "tas");
                message.setSentDate(new Date());
                message.setContent(ko(msg), "text/html");

                Transport.send(message);
                
                
                logger.debug("Mail Send Success title : " + title + ", receivers[" + receivers.toString() + "]");
            } catch (AddressException e) {
                logger.error("Mail Send Fail title : " + title + ", receivers[" + receivers.toString() + "]");
                return false;
            } catch (MessagingException e) {
                logger.error("Mail Send Fail title : " + title + ", receivers[" + receivers.toString() + "]");
                return false;
            } catch (UnsupportedEncodingException e) {
                logger.error("Mail Send Fail title : " + title + ", receivers[" + receivers.toString() + "]");
                return false;
            } return true;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Mail Server IS Null or Empty ~~");
            }
            return false;
        }
    }
    
    public static boolean sendAuthMail(String title, String content, String userId, List<AccountVO> receivers) throws BaseException{
        if (mailServer != null && !mailServer.isEmpty()) {
            String from = makeSender();
            String msg = makeContent(content, userId);
            Properties prop = System.getProperties();
            initSmtpInfo(prop, mailSecurity, port);

            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailAuthId, mailAuthPwd);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);

                if (from != null) {
                    message.setFrom(new InternetAddress(mailAuthId));
                }

                if (receivers != null && receivers.size() > 0) {
                    for(AccountVO item : receivers) {
                        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(item.getEmail()));
                    }
                }
                

                message.setSubject(title, "UTF-8");
                message.setHeader("TESS TAS", "tas");
                message.setSentDate(new Date());
                message.setContent(msg, "text/html;charset=UTF-8");

                Transport.send(message);

                logger.info("Mail Send Success Server : " + mailServer + ", port : " + port + ", title : " + title + ", receivers[" + receivers.toString() + "]");
            } catch (AddressException e) {
                logger.error("Mail Send Fail Server : " + mailServer + ", port : " + port + ", title : " + title + ", receivers[" + receivers.toString() + "]");
                return false;
            } catch (MessagingException e) {
                logger.error("Mail Send Fail Server : " + mailServer + ", port : " + port + ", title : " + title + ", receivers[" + receivers.toString() + "]");
                return false;
            } return true;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Mail Server IS Null or Empty ~~");
            }
            return false;
        }
    }
    
    private static String ko(String en) {
        String new_str = null;
        try {
            if (en != null) {
                new_str = new String(en.getBytes(StandardCharsets.UTF_8), "8859_1");
            }
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getLocalizedMessage(), ex);
            new_str = en;
        }
        return new_str;
    }
    
    private static String makeContent(String content, String userId) {
        String alertTime = "Alert Time : " + DateTimeUtil.getNowSimpleDateFormat("yyyy-MM-dd HH:mm:ss") + "</br>";
        String oper = " Operator : TESS TAS CLOUD" + "</br>";
        String operId = " Operator ID : " + userId + "</br>";
        String msg = "Alert Content : " + content;
        return alertTime + oper + operId + msg;
    }

    private static String makeSender() {
        String rtn = "tas@";
        String [] arr = mailServer.split("\\.");
        if (arr != null && arr.length > 0) {
            for(int i = 1 ; i < arr.length ; i++) {
                if (i == 1) {
                    rtn = rtn + arr[i];
                    
                } else {
                    rtn = rtn + "." + arr[i];
                }
            }
            return rtn;
        }
        return null;
    }

    private static void initSmtpInfo(Properties mailProps, String mailSecurity, String pot) {
        mailProps.put("mail.smtp.host", mailServer);
        mailProps.put("mail.smtp.port", port);
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.transport.protocol", "smtp");

        initSmtpBySecurity(mailProps, mailSecurity);
        initSmtpByPort(mailProps, port);
    }

    private static void initSmtpBySecurity(Properties mailProps, String mailSecurity) {
        if (mailSecurity.equals("-")) {
            mailProps.remove("mail.smtp.starttls.enable");
            mailProps.remove("mail.smtp.ssl.trust");
            mailProps.remove("mail.smtp.ssl.protocols");
        } else {
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.ssl.trust", mailServer);
            mailProps.put("mail.smtp.ssl.protocols", TLS_WITH_VERSION);
        }
    }

    private static void initSmtpByPort(Properties mailProps, String port) {
        if (port.equals("465")) {
            mailProps.put("mail.smtp.ssl.enable", "true");
        } else {
            mailProps.remove("mail.smtp.ssl.enable");
        }
    }
    
    public static String getMailServer() {
		return mailServer;
	}

	public static void setMailServer(String mailServer) {
		SimpleMailSender.mailServer = mailServer;
	}

	public static String getMailAuthId() {
		return mailAuthId;
	}

	public static void setMailAuthId(String mailAuthId) {
		SimpleMailSender.mailAuthId = mailAuthId;
	}

	public static String getMailAuthPwd() {
		return mailAuthPwd;
	}

	public static void setMailAuthPwd(String mailAuthPwd) {
		SimpleMailSender.mailAuthPwd = mailAuthPwd;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		SimpleMailSender.port = port;
	}

    public static String getMailSecurity() {
        return mailSecurity;
    }

    public static void setMailSecurity(String mailSecurity) {
        SimpleMailSender.mailSecurity = mailSecurity;
    }
}
