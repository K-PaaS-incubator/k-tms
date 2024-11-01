package com.kglory.tms.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kglory.tms.web.filter.SessionObject;
import com.kglory.tms.web.model.dto.LoginFormDto;
import com.kglory.tms.web.model.dto.LoginStatusDto;
import com.kglory.tms.web.servlet.SessionListener;
import java.util.List;

public class PresenceHandler extends TextWebSocketHandler {
	private static Logger	logger	= LoggerFactory.getLogger(PresenceHandler.class);
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		int hashCode = 0;
		try {
			while (session.isOpen()) {
                ArrayList<LoginStatusDto> loginStatusDtos = SessionListener.getLoginStatusList();
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(loginStatusDtos)));
				Thread.sleep(10*1000);
			}
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e, e);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e, e);
		} finally {
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
