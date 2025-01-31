/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.batch;

import com.kglory.tms.web.services.common.CreateTableService;
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
public class CreateLogTableScheduler  extends QuartzJobBean {
    private static Logger log = LoggerFactory.getLogger(CreateLogTableScheduler.class);

    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        doBatch();
    }
    
    public void doBatch() {
        long startTime = System.currentTimeMillis();
        log.info("Create Log Table Scheduler Start ~~~");
        
        //파일 확인
        CreateTableService createTableSvc = (CreateTableService) SpringUtils.getBean("createTableSvc");
        createTableSvc.nextDayCreateLogTable();
        
        log.info("Create Log Table Scheduler End Running Time : " + (System.currentTimeMillis() - startTime));
    }
}
