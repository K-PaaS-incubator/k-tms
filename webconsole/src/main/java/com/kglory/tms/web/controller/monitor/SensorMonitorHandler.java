package com.kglory.tms.web.controller.monitor;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kglory.tms.web.model.dto.SensorMonitoringDto;
import com.kglory.tms.web.model.vo.SensorMonitorVO;
import com.kglory.tms.web.services.monitor.SensorSystemlogMonitorService;
import com.kglory.tms.web.util.DateTimeUtil;

public class SensorMonitorHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(SensorMonitorHandler.class);

    @Autowired
    SensorSystemlogMonitorService sensorSystemlogMonitorService;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws BaseException {
        String payloadMessage = (String) message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        	SensorMonitoringDto dto = objectMapper.readValue(payloadMessage, SensorMonitoringDto.class);
            Constants.setAliveStartTime(System.currentTimeMillis()); 
            while (session.isOpen()) {
                List<SensorMonitorVO> sensorMonitorVOs = sensorSystemlogMonitorService.selectLastSensorMonitoring(dto);
//				logger.debug(objectMapper.writeValueAsString(sensorMonitorVOs));
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(sensorMonitorVOs)));
                Thread.sleep(10 * 1000);
                DateTimeUtil.aliveSessionTimeCheck(session);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            if (session != null) {
                try {
					session.close();
				} catch (IOException e) {
					logger.error(e.getLocalizedMessage());
				}
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
