package com.kglory.tms.web.controller.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.net.whois.WhoisClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kglory.tms.web.model.dto.IpSearchDto;
import com.kglory.tms.web.util.IpUtil;
import com.kglory.tms.web.util.StringUtil;

public class WhoisHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(WhoisHandler.class);

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payloadMessage = (String) message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        IpSearchDto dto = objectMapper.readValue(payloadMessage, IpSearchDto.class);
        BufferedReader r = null;
        WhoisClient whois = null;
        try {
            int isIp = IpUtil.isIp4IpV6Check(dto.getIp());
            if (isIp == -1) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(String.format(
                        "IP %s isn't of IP type.", dto.getIp()))));
            } else {
                whois = new WhoisClient();
                if (dto.getTimeout() != null) {
                    whois.setConnectTimeout(dto.getTimeout() * 1000);
                }
                if (dto.getWhoisServer() == null) {
                    whois.connect(WhoisClient.DEFAULT_HOST);
                } else {
                    whois.connect(dto.getWhoisServer());
                }
                r = new BufferedReader(new InputStreamReader(whois.getInputStream(dto.getIp()), "UTF-8"));
                String line = null;
                while (session.isOpen() && (line = r.readLine()) != null) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(line)));
                }
                whois.disconnect();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("parameter : " + StringUtil.logDebugMessage(dto.toString()));
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), "[WhoisServer : "+dto.getWhoisServer()+", [ip : "+dto.getIp()+"]");
            String msg = "[WhoisServer("+dto.getWhoisServer()+") Fail] "+e.getLocalizedMessage();
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
            
        } catch (Exception e) {
        	logger.error(e.getLocalizedMessage(), "[WhoisServer : "+dto.getWhoisServer()+", [ip : "+dto.getIp()+"]");
        	String msg = "[WhoisServer("+dto.getWhoisServer()+") Fail] "+e.getLocalizedMessage();
        	session.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
        } finally {
        	if(whois != null && whois.isConnected()) {
        		whois.disconnect();
        	}
        	
        	if(r != null) {
        		r.close();
        	}
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        super.afterConnectionClosed(session, status);
    }
}
