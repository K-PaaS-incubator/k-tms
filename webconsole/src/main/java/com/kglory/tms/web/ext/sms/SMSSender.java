/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.ext.sms;

import com.kglory.tms.web.model.dto.WarningLogDto;
import com.kglory.tms.web.model.vo.AccountVO;
import com.kglory.tms.web.util.MessageUtil;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class SMSSender implements Runnable {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SMSSender.class);
    private Thread thread;
    private SMSConnector connector;
    private String sender = "0260030999"; //발신자 번호 등록(http://www.icodekorea.com)
    private String senderName = "tas";
    protected ArrayBlockingQueue<LogSms> sendBuffer = new ArrayBlockingQueue(2000);
    

    public SMSSender(String loginId, String loginPassword, String ip, int port) throws IOException {
        connector = new SMSConnector(loginId, loginPassword);
        connector.connect(ip, port);
    }

    public void start() {
        thread = new Thread(this, "SMSSender");
        thread.start();
    }

    public void stop() {
        if (thread == null) {
            return;
        }
        if (thread.isInterrupted() == false) {
            thread.interrupt();
        }
        sendBuffer.clear();
        thread = null;
    }

    /**
     * 전송 SMS 데이타 추가
     *
     * @param o
     * @return
     */
    public boolean addData(LogSms o) {
        boolean add = sendBuffer.offer(o);
        if (add == false) {
            log.error("sendBuffer full, size =" + sendBuffer.size());
        }
        return add;
    }
    
    public void addListData(List<AccountVO> recever, String message, WarningLogDto warningLog) {
        if (recever != null) {
            LogSms logSms = null;
            for (AccountVO item : recever) {
//                warningLog.setlUserIndex(Long.valueOf(item.getUserIndex()).intValue());
                logSms = new LogSms(sender, senderName, item.getMobile(), message, warningLog);
                addData(logSms);
            }

        }
    }

    private LogSms getData() throws InterruptedException {
        if (sendBuffer.size() == 0) {
            log.info("wait ......");
            stop();
        }
        LogSms sms = null;
        while (sms == null) {
            sms = sendBuffer.poll(60, TimeUnit.SECONDS);
            if (sms != null) {
                break;
            }
        }
        return sms;
    }
    
    @Override
    public void run() {
        log.info("SMSSender start");

        while (Thread.currentThread() == thread) {
            try {
                LogSms sms = this.getData();
                send(sms);

                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                log.error(ex.getLocalizedMessage());
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }
        log.info("SMSSender  end");
    }

    private void send(LogSms sms) {
        if (sms == null) {
            return;
        }
        try {
            long msgNum = connector.sendMsg(sms.getDestMdn(), sms.getSrcMdn(), sms.getSrcName(), null, sms.getMsg());
            if (msgNum == -1L) {
                sms.getWarningLog().setnStatus(0);
                sms.getWarningLog().setStrError(MessageUtil.getMessage("ext.sms.send.fail"));
            } else {
                sms.getWarningLog().setStrError("");
                sms.getWarningLog().setnStatus(1);
            }
//                this.preventionSvc.insertWarningResultLog(sms.getWarningLog());
            if (log.isInfoEnabled()) {
                log.info("msg_id =" + msgNum + ", tel = " + sms.getDestMdn());
            }
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            connector.reConnect(1000);
        }
    }
}
