/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.ext.sms;

import com.kglory.tms.web.model.dto.AuditResultDto;
import com.kglory.tms.web.model.dto.WarningLogDto;
import com.kglory.tms.web.model.vo.AccountVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class SMSService {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SMSService.class);
    private SMSSender smsSender;
    private static SmsConfig smsConfig;
    private List<AccountVO> recever;
    private String message;
//    private WarningLogDto warningLog;
    private AuditResultDto resultDto;
    
    public SMSService() {
    }

    public List<AccountVO> getRecever() {
        return recever;
    }

    public void setRecever(List<AccountVO> recever) {
        this.recever = recever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public WarningLogDto getWarningLog() {
//        return warningLog;
//    }
//
//    public void setWarningLog(WarningLogDto warningLog) {
//        this.warningLog = warningLog;
//    }

    public AuditResultDto getResultDto() {
        return resultDto;
    }

    public void setResultDto(AuditResultDto resultDto) {
        this.resultDto = resultDto;
    }


    public void start() throws IOException {

        if (smsConfig == null || smsConfig.getLoginId() == null || smsConfig.getLoginId().isEmpty() || 
                smsConfig.getLoginPasswd() == null || smsConfig.getLoginPasswd().isEmpty() ||
                smsConfig.getIp() == null || smsConfig.getIp().isEmpty() || smsConfig.getPort() == null) {
            return;
        }
        smsSender = new SMSSender(smsConfig.getLoginId(), smsConfig.getLoginPasswd(), smsConfig.getIp(), smsConfig.getPort());
//        smsSender.addListData(recever, message, warningLog, preventionSvc);
        smsSender.addListData(recever, message, null);
        smsSender.start();
    }

    public static SmsConfig getSmsConfig() {
        return smsConfig;
    }

    public static void setSmsConfig(SmsConfig smsConfig) {
        SMSService.smsConfig = smsConfig;
    }

    public void stop() {
        if (smsSender != null) {
            smsSender.stop();
        }
        smsSender = null;
    }

    /**
     * 테스트 메세지 발송
     *
     * @param cnf
     * @param mdn
     * @param msg
     * @return
     */
    public boolean sendTestMsg(SmsConfig cnf, String mdn, String msg) {
        boolean rtn = false;
        SMSConnector c = null;
        try {
            c = new SMSConnector(cnf.getLoginId(), cnf.getLoginPasswd());
            c.connect(cnf.getIp(), cnf.getPort());
            long msgId = c.sendMsg(mdn, cnf.getSrcMdn(), cnf.getSrcName(), null, msg);
            if (msgId > 0) {
                rtn = true;
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return rtn;
    }

    public boolean isRunnig() {
        if (smsSender != null) {
            return true;
        }
        return false;
    }

    public String toInfoString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMSService {").append(SystemUtils.LINE_SEPARATOR);
        sb.append(smsConfig.toMultiLineString());
        sb.append('}');
        return sb.toString();
    }
}
