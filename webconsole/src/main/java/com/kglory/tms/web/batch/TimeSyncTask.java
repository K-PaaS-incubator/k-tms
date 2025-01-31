/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.batch;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.services.systemSettings.ManagerService;
import com.kglory.tms.web.util.SpringUtils;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class TimeSyncTask extends TimerTask {
    private static Logger log = LoggerFactory.getLogger(TimeSyncTask.class);

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            log.info("Time Sync Start ~~~");
            
            ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
            managerSvc.execTimeSync();
            
            log.info("Time Sync End Running Time : " + (System.currentTimeMillis() - startTime));
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
    
}
