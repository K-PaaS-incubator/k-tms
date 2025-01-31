/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.batch;

import com.kglory.tms.web.services.TestService;
import com.kglory.tms.web.services.common.CreateTableService;
import com.kglory.tms.web.util.SpringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author leecjong
 */
public class TestScheduler extends QuartzJobBean {
    
    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        TestService testSvc = (TestService) SpringUtils.getBean("TestSvc");
        testSvc.startScheduler();
    }
}
