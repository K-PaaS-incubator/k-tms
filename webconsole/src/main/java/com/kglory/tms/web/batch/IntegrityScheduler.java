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
public class IntegrityScheduler extends QuartzJobBean {
    private static Logger log = LoggerFactory.getLogger(IntegrityScheduler.class);

    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        doBatch();
    }
    
    public void doBatch() {
        try {
            long startTime = System.currentTimeMillis();
            log.info("Integrity Start ~~~");
            
            ManagerService managerSvc = (ManagerService) SpringUtils.getBean("managerSvc");
            managerSvc.execConsoleIntegrity();
            
            log.info("Integrity End Running Time : " + (System.currentTimeMillis() - startTime));
        } catch(BaseException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
