/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.batch;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.services.systemSettings.ManagerService;
import com.kglory.tms.web.util.SpringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author leecjong
 */
public class TimeSyncScheduler extends QuartzJobBean {
    private static Logger log = LoggerFactory.getLogger(TimeSyncScheduler.class);

    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        try {
			doBatch();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			log.error(e.getLocalizedMessage());
		}
    }
    
    public void doBatch() throws BaseException {
        long startTime = System.currentTimeMillis();
        log.info("Time Sync Start ~~~");
        
        ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
        managerSvc.execTimeSync();
        
        log.info("Time Sync End Running Time : " + (System.currentTimeMillis() - startTime));
    }
}
