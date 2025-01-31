package com.kglory.tms.web.controller.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.model.dto.IpSearchDto;
import com.kglory.tms.web.util.IpUtil;
import java.util.ArrayList;
import java.util.List;

public class TraceRouteHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(TraceRouteHandler.class);

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payloadMessage = (String) message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        IpSearchDto dto = objectMapper.readValue(payloadMessage, IpSearchDto.class);
        Process process = null;
        BufferedReader br = null;
        try {
            int isIp = IpUtil.isIp4IpV6Check(dto.getIp());
            if (isIp == -1) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(String.format(
                        "IP %s isn't of IP type.", dto.getIp()))));
            } else {
                // IP 혹은 도메인명, 각 응답 대기 시간, 최대 홉 수를 매개변수로 TraceRoute를 비동기로 실행한다.
                List<String> command = new ArrayList<>();
                if (isIp == 6) {
                    command.add("traceroute6");
                    command.add("-i");
                    command.add("eth0");
                } else {
                    command.add("traceroute");
                }
                if (dto.getMaximumHops() != null) {
                    command.add("-m");
                    command.add(dto.getMaximumHops().toString());
                }
                if (dto.getTimeout() != null) {
                    command.add("-w");
                    command.add(dto.getTimeout().toString());
                }
                command.add(dto.getIp());
                String line;
                ProcessBuilder pidBilder = new ProcessBuilder();
                pidBilder.command(command);
                process = pidBilder.start();
                br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                
                while (session.isOpen() && (line = br.readLine()) != null) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(line)));
                }
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString("Traceroute end...")));
                
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
        	if(br != null) {
        		br.close();
        	}
            if (session != null) {
                session.close();
            }
            if (process != null) {
                process.destroy();
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
