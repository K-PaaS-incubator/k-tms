package com.kglory.tms.web.controller.monitor;

import com.kglory.tms.web.common.Constants;
import com.kglory.tms.web.exception.BaseException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.kglory.tms.web.model.dto.FileMetaMonitorSearchDto;
import com.kglory.tms.web.model.vo.FileMetaDetectionEventVO;
import com.kglory.tms.web.services.OracleService;
import com.kglory.tms.web.services.monitor.FileMetaDetectionService;
import com.kglory.tms.web.util.DateTimeUtil;
import com.kglory.tms.web.util.TimeUtil;

public class FileMetaDetectionHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(FileMetaDetectionHandler.class);

    @Autowired
    FileMetaDetectionService fileMetaDetectionService;
    @Autowired
    OracleService oracleSvc;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws BaseException {
        try {
            String payloadMessage = (String) message.getPayload();
            ObjectMapper objectMapper = new ObjectMapper();
            FileMetaMonitorSearchDto dto = objectMapper.readValue(payloadMessage, FileMetaMonitorSearchDto.class);
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dbDateTime = oracleSvc.selectNowDbDateTime();
            dto.setStartDateInput(DateTimeUtil.getDateToStr(DateTimeUtil.getChangeSecond(DateTimeUtil.getStrToDate(dbDateTime, "yyyy-MM-dd HH:mm:ss"), -10), "yyyy-MM-dd HH:mm:ss"));
            dto.setEndDateInput(DateTimeUtil.getDateToStr(DateTimeUtil.getChangeSecond(DateTimeUtil.getStrToDate(dbDateTime, "yyyy-MM-dd HH:mm:ss"), -9), "yyyy-MM-dd HH:mm:ss"));
            int count = 0;
            Constants.setAliveStartTime(System.currentTimeMillis());;
            while (session.isOpen()) {
                count++;
                long startTime = System.currentTimeMillis();
                List<FileMetaDetectionEventVO> eventVOs = fileMetaDetectionService.selectFileMetaDetectionEvent(dto);
                if (eventVOs.size() > 0) {
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(eventVOs)));
                }
                if (count == 100) {
                    count = 0;
                    dbDateTime = oracleSvc.selectNowDbDateTime();
                    dto.setStartDateInput(DateTimeUtil.getDateToStr(DateTimeUtil.getChangeSecond(DateTimeUtil.getStrToDate(dbDateTime, "yyyy-MM-dd HH:mm:ss"), -10), "yyyy-MM-dd HH:mm:ss"));
                    dto.setEndDateInput(DateTimeUtil.getDateToStr(DateTimeUtil.getChangeSecond(DateTimeUtil.getStrToDate(dbDateTime, "yyyy-MM-dd HH:mm:ss"), -9), "yyyy-MM-dd HH:mm:ss"));
                }
                Calendar startCalendar = TimeUtil.parseDateTime(dto.getStartDateInput());
                Calendar endCalendar = TimeUtil.parseDateTime(dto.getEndDateInput());
                startCalendar.add(Calendar.SECOND, 1);
                endCalendar.add(Calendar.SECOND, 1);
                dto.setStartDateInput(fmt.format(startCalendar.getTime()));
                dto.setEndDateInput(fmt.format(endCalendar.getTime()));
                
                int sleepTime = 1000 - Long.valueOf(System.currentTimeMillis() - startTime).intValue();
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
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
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
