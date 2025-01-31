/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.batch;

import com.kglory.tms.web.exception.BaseException;
import com.kglory.tms.web.util.SpringUtils;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leecjong
 */
public class ScheduleUtil {

    private static Logger logger = LoggerFactory.getLogger(ScheduleUtil.class);

    private static Scheduler webConsoleScheduler;
    public static final String TMS_WEB_NAME = "WEB_GROUP";

    public static final String INTEGRITY_JOB = "integrityJob";
    public static final String INTEGRITY_TRIGGER = "integrityTrigger";
    public static final String DUAL_SYSTEM_JOB = "dualSystemJob";
    public static final String DUAL_SYSTEM_TRIGGER = "dualSystemTrigger";
    public static final String CREATE_LOG_TABLE_JOB = "CreateLogTableJob";
    public static final String CREATE_LOG_TABLE_TRIGGER = "CreateLogTableTrigger";
    public static final String TIME_SYNC_JOB = "timeSyncJob";
    public static final String TIME_SYNC_TRIGGER = "timeSyncTrigger";
    public static final String DB_CONFIG_JOB = "dbConfigJob";
    public static final String DB_CONFIG_TRIGGER = "dbConfigTrigger";
    public static final String DB_BACKUP_JOB = "dbBackupJob";
    public static final String DB_BACKUP_TRIGGER = "dbBackupTrigger";
    public static final String DB_MANAGEMENT_JOB = "dbManagementJob";
    public static final String DB_MANAGEMENT_TRIGGER = "dbManagementTrigger";
    public static final String SENSOR_AUDIT_JOB = "sensorAuditJob";
    public static final String SENSOR_AUDIT_TRIGGER = "sensorAuditTrigger";
    
    public static final String TEST_JOB = "testJob";
    public static final String TEST_TRIGGER = "testTrigger";
    
    private static Timer timesyncTimer;

    public static void initScheduler() {
        if (webConsoleScheduler == null) {
            webConsoleScheduler = (StdScheduler) SpringUtils.getBean("baseScheduler");
        }
    }

    public static void stopSchedule() {
        try {
            if (webConsoleScheduler != null) {
                webConsoleScheduler.shutdown();
            }
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public static boolean isJobRunning(String jobName) {
        boolean rtn = false;
        String name = "";
        try {
            List<JobExecutionContext> currentJobs = webConsoleScheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext jobCtx: currentJobs){
        		name = jobCtx.getJobDetail().getKey().getName();
        		if(name.equals(jobName)) {
                    rtn = true;
                    break;
                }		
            }
        } catch(SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return rtn;
    }

    public static void integrityStart(int minValue) throws BaseException{
        try {
            JobDetail job = getJobDetail(IntegrityScheduler.class, INTEGRITY_JOB);
            Trigger trigger = getSimpleTriggerMin(INTEGRITY_TRIGGER, minValue);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.info("Integrity Schedule  Start ~~~~~");
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void integrityStop() throws BaseException{
        try {
            JobDetail job = getJobDetail(IntegrityScheduler.class, INTEGRITY_JOB);
            webConsoleScheduler.deleteJob(job.getKey());
            logger.info("Integerity Shceduler Stop ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void changeIntegrityTrigger(int minValue) throws BaseException{
        try {
            TriggerKey triggerKey = new TriggerKey(INTEGRITY_TRIGGER, TMS_WEB_NAME);
            Trigger trigger = getSimpleTriggerMin(INTEGRITY_TRIGGER, minValue);
            webConsoleScheduler.rescheduleJob(triggerKey, trigger);

            logger.info("Integerity Shceduler time Change ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void createLogTableStart() throws BaseException{
        // 매일 1시 시작
        String cron = "0 0 1 * * ?";
        try {
            JobDetail job = getJobDetail(CreateLogTableScheduler.class, CREATE_LOG_TABLE_JOB);
            Trigger trigger = getCronTrigger(CREATE_LOG_TABLE_TRIGGER, cron);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.debug("Create Log Table Shceduler Start ~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void createLogTableStop() throws BaseException{
        try {
            JobDetail job = getJobDetail(CreateLogTableScheduler.class, CREATE_LOG_TABLE_JOB);
            webConsoleScheduler.deleteJob(job.getKey());
            logger.info("Create Log Table Shceduler Stop ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void timeSyncStart(int minValue) throws BaseException {
		// JobDetail job = getJobDetail(TimeSyncScheduler.class, TIME_SYNC_JOB);
		// Trigger trigger = getSimpleTriggerMin(TIME_SYNC_TRIGGER, minValue);
		//
		// webConsoleScheduler.scheduleJob(job, trigger);
		// webConsoleScheduler.start();
		if (timesyncTimer == null) {
			timesyncTimer = new Timer(TIME_SYNC_JOB);
		}
		
		TimeSyncTask task = new TimeSyncTask();
//		 timesyncTimer.scheduleAtFixedRate(task, 0, minValue * 1000 * 60);
		timesyncTimer.scheduleAtFixedRate(task, 0, minValue * 1000 * 60);

		logger.info("Time Sync Schedule  Start ~~~~~ minValue :: "+minValue);
    }

	public static void timeSyncReStart(int minValue) throws BaseException{
		timeSyncStop();
		timeSyncStart(minValue);
	}

    public static void timeSyncStop() throws BaseException{
		if (timesyncTimer != null) {
			timesyncTimer.cancel();
			timesyncTimer = null;
		}
		// JobDetail job = getJobDetail(TimeSyncScheduler.class, TIME_SYNC_JOB);
		// webConsoleScheduler.deleteJob(job.getKey());
		logger.info("Time Sync Shceduler Stop ~~~~~~~~~");
    }

    public static void changeTimeSyncTrigger(int minValue) throws BaseException{
        try {
            TriggerKey triggerKey = new TriggerKey(TIME_SYNC_TRIGGER, TMS_WEB_NAME);
            Trigger trigger = getSimpleTriggerMin(TIME_SYNC_TRIGGER, minValue);
            webConsoleScheduler.rescheduleJob(triggerKey, trigger);

            logger.info("Time Sync Shceduler time Change ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void dbConfigStart() throws BaseException{
        // 매일 2시 시작
        String cron = "0 0 2 * * ?";
        try {
            JobDetail job = getJobDetail(DropTableScheduler.class, DB_CONFIG_JOB);
            Trigger trigger = getCronTrigger(DB_CONFIG_TRIGGER, cron);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.debug("db Config(drop table) Shceduler Start ~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void dbConfigStop() throws BaseException{
        try {
            JobDetail job = getJobDetail(DropTableScheduler.class, DB_CONFIG_JOB);
            webConsoleScheduler.deleteJob(job.getKey());
            logger.info("db Config(drop table) Shceduler Stop ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void dbBackupStart(int hour, int min, int sec) throws BaseException{
        String cron = getHourMinSecCronExpress(hour, min, sec);
        try {
            JobDetail job = getJobDetail(DbBackupScheduler.class, DB_BACKUP_JOB);
            Trigger trigger = getCronTrigger(DB_BACKUP_TRIGGER, cron);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.debug("DB Backup Shceduler Start ~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void dbBackupStop() throws BaseException{
        try {
            JobDetail job = getJobDetail(DbBackupScheduler.class, DB_BACKUP_JOB);
            webConsoleScheduler.deleteJob(job.getKey());
            logger.info("DB Backup Shceduler Stop ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void changeDbBackupTrigger(int hour, int min, int sec) throws BaseException{
        String cron = getHourMinSecCronExpress(hour, min, sec);
        try {
            TriggerKey triggerKey = new TriggerKey(DB_BACKUP_TRIGGER, TMS_WEB_NAME);
            Trigger trigger = getCronTrigger(DB_BACKUP_TRIGGER, cron);
            webConsoleScheduler.rescheduleJob(triggerKey, trigger);

            logger.info("DB Backup Shceduler time Change ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }
    
    public static void dbManagementStart() throws BaseException{
        String cron = getMinCronExpress(30);
        try {
            JobDetail job = getJobDetail(DbManagementScheduler.class, DB_MANAGEMENT_JOB);
            Trigger trigger = getCronTrigger(DB_MANAGEMENT_TRIGGER, cron);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.debug("DB Management Shceduler Start ~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void dbManagementStop() throws BaseException{
        try {
            JobDetail job = getJobDetail(DbManagementScheduler.class, DB_MANAGEMENT_JOB);
            webConsoleScheduler.deleteJob(job.getKey());
            logger.info("DB Management Shceduler Stop ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }
    
    public static void sensorAuditMailStart() throws BaseException{
        String cron = getMinCronExpress(1);
        try {
            JobDetail job = getJobDetail(SensorAuditCheckScheduler.class, SENSOR_AUDIT_JOB);
            Trigger trigger = getCronTrigger(SENSOR_AUDIT_TRIGGER, cron);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.debug("SensorAudit Mail Shceduler Start ~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static void sensorAuditMailStop() throws BaseException{
        try {
            JobDetail job = getJobDetail(SensorAuditCheckScheduler.class, SENSOR_AUDIT_JOB);
            webConsoleScheduler.deleteJob(job.getKey());
            logger.info("SensorAudit Mail Shceduler Stop ~~~~~~~~~");
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }

    public static JobDetail getJobDetail(Class c, String jobName) {
        return (JobDetail) JobBuilder.newJob(c)
                .withIdentity(jobName, TMS_WEB_NAME)
                .build();
    }

    public static CronTrigger getCronTrigger(String triggerName, String cronExpress) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerName, TMS_WEB_NAME)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpress))
                .build();
    }

    public static Trigger getSimpleTriggerMin(String triggerName, int min) {
        return TriggerBuilder.newTrigger().withIdentity(triggerName, TMS_WEB_NAME)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(min).repeatForever())
                .build();
    }

    public static Trigger getSimpleTriggerSeconds(String triggerName, int seconds) {
        return TriggerBuilder.newTrigger().withIdentity(triggerName, TMS_WEB_NAME)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(seconds).repeatForever())
                .build();
    }

    public static String getMinCronExpress(int min) {
        return "0 0/" + min + " * * * ?";
    }

    public static String getHourMinSecCronExpress(int hour, int min, int sec) {
        return sec + " " + min + " " + hour + " * * ?";
    }
    
    public static void testInsertStart() throws BaseException {
        try {
            String cron = "0/20 * * * * ?";
            
            JobDetail job = getJobDetail(TestScheduler.class, TEST_JOB);
            Trigger trigger = getCronTrigger(TEST_TRIGGER, cron);

            webConsoleScheduler.scheduleJob(job, trigger);
            webConsoleScheduler.start();
            logger.info("TEST Schedule  Start ~~~~~");
        } catch (SchedulerException e) {
            logger.debug(e.getLocalizedMessage(), e);
            throw new BaseException(e);
        }
    }
    
}
