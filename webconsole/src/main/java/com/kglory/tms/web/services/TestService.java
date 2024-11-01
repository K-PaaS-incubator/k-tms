/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.services;

import com.kglory.tms.web.mapper.TestMapper;
import com.kglory.tms.web.util.DateTimeUtil;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author leecjong
 */
@Service("TestSvc")
public class TestService {
	private static Logger logger = LoggerFactory.getLogger(TestService.class);
    @Autowired
    TestMapper testMapper;
    
    public void startScheduler() {
        try {
            insertDetectionLog();
            Thread.sleep(1000L);
            insertFileMetaLog();
        } catch (InterruptedException ex) {
        	logger.error(ex.getLocalizedMessage());
        }
    }
    
    public void insertDetectionLog() {
        String logTable = "LOG_" + DateTimeUtil.getNowTableDate();
        Map<String, String> map = new HashMap<>();
        map.put("tableName", logTable);
        testMapper.insertDetectionLog(map);
    }
    
    public void insertFileMetaLog() {
        String logTable = "FILEMETA_" + DateTimeUtil.getNowTableDate();
        Map<String, String> map = new HashMap<>();
        map.put("tableName", logTable);
        testMapper.insertFileMetaLog(map);
    }
}
