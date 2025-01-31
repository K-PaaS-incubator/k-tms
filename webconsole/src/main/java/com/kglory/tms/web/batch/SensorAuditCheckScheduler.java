/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.batch;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.services.systemStatus.AuditLogService;
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
public class SensorAuditCheckScheduler extends QuartzJobBean {
    private static Logger log = LoggerFactory.getLogger(SensorAuditCheckScheduler.class);
    
    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        doBatch();
    }
    
    public void doBatch() {
        AuditLogService auditLogSvc = (AuditLogService) SpringUtils.getBean("auditLogSvc");
        try {
			auditLogSvc.sendMailSensorAudit();
		} catch (BaseException e) {
			log.error(e.getLocalizedMessage());
		}
    }
}
