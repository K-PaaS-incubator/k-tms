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

import com.kglory.tms.web.model.dto.IpSearchDto;
import com.kglory.tms.web.util.IpUtil;
import static com.kglory.tms.web.util.SystemUtil.logger;
import java.util.ArrayList;
import java.util.List;

public class PingHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(PingHandler.class);

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
                // IP 혹은 도메인명을 매개변수로 ping을 비동기로 실행한다.
                String option = "ping";
                if (isIp == 6) {
                    option = "ping6";
                }
                List<String> command = new ArrayList<>();
                command.add(option);
                command.add("-c");
                command.add("6");
                if (isIp == 6) {
                    command.add("-I");
                    command.add("eth0");
                }
                command.add(dto.getIp());
                ProcessBuilder pidBilder = new ProcessBuilder();
                pidBilder.command(command);
                process = pidBilder.start();
                br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                
                String line = br.readLine();
                if (line == null) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString("connect: Network is unreachable")));
                }
                while (session.isOpen() && line != null) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(line)));
                    line = br.readLine();
                }
                
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
        	if (br != null) {
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
