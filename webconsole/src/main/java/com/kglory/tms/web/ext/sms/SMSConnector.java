/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.ext.sms;

import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.StringUtil;
import com.kglory.tms.web.util.file.FileUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class SMSConnector {

    private static Logger log = LoggerFactory.getLogger(SMSConnector.class);
    // 일반적으로 사용하게될 변수입니다.
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private int timeOut = 2000;
    private String loginId;  // infosec
    private String loingPasswd; //no1infosec
    private String ip; // 211.172.232.124
    private int port; //7196

    public SMSConnector() {
    }

    public SMSConnector(String smsId, String smsPwd) {
        this.loginId = smsId;
        this.loingPasswd = smsPwd;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void connect(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;

        try {
            socket = new Socket();

            SocketAddress addr = new InetSocketAddress(ip, port);
            socket.connect(addr, timeOut); // 5 seconds timeout
            socket.setSoTimeout(timeOut);

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw e;
        } finally {
        	close();
        }
    }

    /**
     * 소켓 자원해제
     */
    public void close() {
        FileUtil.closeQuietly(in, out);
        FileUtil.closeQuietly(socket);
        socket = null;
    }

    public boolean reConnect(int timeOut) {
        try {
            this.close();
            this.setTimeOut(timeOut);
            this.connect(this.ip, this.port);
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public long sendMsg(String destMdn, String srcMdn, String srcUserName, String msg) throws IOException {
        return this.sendMsg(destMdn, srcMdn, srcUserName, null, msg);
    }

    // 실제 발송하는 과정입니다.
    /**
     *
     * @param destMdn 수신자 번호
     * @param srcMdn 발신자 번호
     * @param srcUserName 발신자 명
     * @param callDate 예약날짜 , 없을경우 null
     * @param msg 메세지
     * @return 메세지 번호
     * @throws IOException
     */
    public long sendMsg(String destMdn, String srcMdn, String srcUserName, String callDate, String msg) throws IOException {
        if (socket == null) {
            return -1L;
        }
        out.writeBytes("01");                       // Msg Type
        out.writeBytes("144 ");                     // Msg Len
        out.writeBytes(fillSpace(loginId, 10));       // 아이코드 ID
        out.writeBytes(fillSpace(loingPasswd, 10));      // 아이코드 PASSWORD
        out.writeBytes(fillSpace(destMdn.replaceAll("-", ""), 11));     // 수신번호
        out.writeBytes(fillSpace(srcMdn.replaceAll("-", ""), 11));      // 발신번호
        out.writeBytes(fillSpace(srcUserName, 10)); // 발신자명
        if (callDate == null) {
            out.writeBytes(fillSpace("            ", 12));    // 즉시전송시 날짜와 시간은 모두 space
        } else { // 예약 전송시 12자리를 맞춰주시면 됩니다. 형식은 YYYYMMDDHHMM            
            out.writeBytes(fillSpace(callDate, 12));
        }
        out.writeBytes(fillSpace(ko(msg), 80));		// 메시지
        out.flush();
        
        log.info("SMS Send srcMdn=" + srcMdn + ", msg=" + msg);
        return this.readResult();
    }

    private long readResult() throws IOException {
        long msgId = -1L;
        boolean inputExist = true;

        do {
            byte buffer[] = new byte[2];
            in.readFully(buffer);
            String msgType = new String(buffer);

            buffer = new byte[4];
            in.readFully(buffer);
            String msgLen = new String(buffer);
            msgLen = msgLen.trim();
            int nLen = Integer.parseInt(msgLen);

            //00 01023828279 73130170
            buffer = new byte[nLen];
            in.readFully(buffer);
            String result = new String(buffer);
            if (msgType.equals("02")) {
                inputExist = false;
                //result 의 구조  = 성공/실패(2 char), 전화번호(11 char) , msgid(10 char)

                String rtnCode = result.substring(0, 2);
                if (rtnCode.equals("00")) {
//                    String mdn = result.substring(2, 13);
                    msgId = Long.parseLong(result.substring(13).trim());
                } else {
                    msgId = -1L;
                }
            } else if (msgType.equals("03")) {
                out.writeBytes("04");
                out.writeBytes("12  ");
                out.writeBytes("00");
                out.writeBytes(result.substring(2));
                out.flush();
            }
            log.info("result : " + result);
        } while (inputExist);

        return msgId;

    }

    /**
     * 원하는 문자열의 길이를 원하는 길이만큼 공백을 넣어 맞추도록 합니다.
     *
     * @param	String	text	원하는 문자열입니다. int	size	원하는 길이입니다.
     * @return	String	변경된 문자열을 넘깁니다.
     */
    private String fillSpace(String text, int size) {
        int diff = size - text.length();
        if (diff > 0) {
            return StringUtil.rightPad(text, size, ' ');
        } else {
            return text.substring(0, size);
        }
    }

    /**
     * 메세지 한글 처리
     *
     * @param en
     * @return
     */
    private String ko(String en) {
        String new_str = null;
        try {
            if (en != null) {
                new_str = new String(en.getBytes("KSC5601"), "8859_1");
            }
        } catch (UnsupportedEncodingException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            new_str = en;
        }
        return new_str;
    }
}
