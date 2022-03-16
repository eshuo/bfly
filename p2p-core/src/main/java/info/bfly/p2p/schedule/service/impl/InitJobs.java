package info.bfly.p2p.schedule.service.impl;

import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.service.ConfigService;
import info.bfly.core.annotations.Log;
import info.bfly.p2p.schedule.ScheduleConstants;
import info.bfly.p2p.schedule.job.AutoRepayment;
import info.bfly.p2p.schedule.job.PushInfo;
import info.bfly.p2p.schedule.job.RefreshTrusteeshipOperation;
import info.bfly.p2p.schedule.job.RepayAlert;
import org.hibernate.ObjectNotFoundException;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: 项目启动以后，初始化调度
 */
@Component
public class InitJobs implements ApplicationListener<ContextRefreshedEvent> {
    @Log
    static Logger log;
    @Resource
    StdScheduler  scheduler;
    @Resource
    ConfigService configService;

    private void initAutoRepaymengJob() throws SchedulerException {
        // 到期自动还款
        JobDetail jobDetail = JobBuilder.newJob(AutoRepayment.class).withIdentity(ScheduleConstants.JobName.AUTO_REPAYMENT, ScheduleConstants.JobGroup.AUTO_REPAYMENT).build();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(ScheduleConstants.TriggerName.AUTO_REPAYMENT, ScheduleConstants.TriggerGroup.AUTO_REPAYMENT).forJob(jobDetail).withSchedule(
                // 每天0点
                CronScheduleBuilder.cronSchedule("0 0 0 * * ? *")).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private void initInfoPush() throws SchedulerException {
        // baidu ping
        JobDetail jobDetail2 = JobBuilder.newJob(PushInfo.class).withIdentity(ScheduleConstants.JobName.BAIDU_PING, ScheduleConstants.JobGroup.BAIDU_PING).build();
        CronTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity(ScheduleConstants.TriggerName.BAIDU_PING, ScheduleConstants.TriggerGroup.BAIDU_PING).forJob(jobDetail2)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 50 23 * * ?")).build();
        scheduler.scheduleJob(jobDetail2, trigger2);
    }

    private void initRefreshTrusteeshipJob() throws SchedulerException {
        // 资金托管主动查询
        JobDetail jobDetail2 = JobBuilder.newJob(RefreshTrusteeshipOperation.class)
                .withIdentity(ScheduleConstants.JobName.REFRESH_TRUSTEESHIP_OPERATION, ScheduleConstants.JobGroup.REFRESH_TRUSTEESHIP_OPERATION).build();
        CronTrigger trigger2 = TriggerBuilder.newTrigger().withIdentity(ScheduleConstants.TriggerName.REFRESH_TRUSTEESHIP_OPERATION, ScheduleConstants.TriggerGroup.REFRESH_TRUSTEESHIP_OPERATION)
                .forJob(jobDetail2).withSchedule(
                        // 每十分钟执行一次
                        CronScheduleBuilder.cronSchedule("0 0/2 * * * ? *")).build();
        scheduler.scheduleJob(jobDetail2, trigger2);
    }


    // 开启哪些调度，能手动控制
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            // root application context 没有parent，他就是老大
            // 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。 初始化所有的调度。
            // 第三方资金托管，主动查询，默认不开启
            String enableRefreshTrusteeship = "0";
            try {
                enableRefreshTrusteeship = configService.getConfigValue(ConfigConstants.Schedule.ENABLE_REFRESH_TRUSTEESHIP);
            } catch (ObjectNotFoundException e) {
                log.debug(e.getMessage());
            }
            // 自动还款+检查项目逾期，默认不开启
            String enableAutoRepayment = "0";
            try {
                enableAutoRepayment = configService.getConfigValue(ConfigConstants.Schedule.ENABLE_AUTO_REPAYMENT);
            } catch (ObjectNotFoundException e) {
                log.debug(e.getMessage());
            }
            String enableRepayAlert = "0";
            try {
                enableRepayAlert = configService.getConfigValue(ConfigConstants.Schedule.ENABLE_REPAY_ALERT);
            } catch (ObjectNotFoundException e) {
                log.debug(e.getMessage());
            }
            try {
                if (enableRefreshTrusteeship.equals("1")) {
                    if (InitJobs.log.isDebugEnabled()) {
                        InitJobs.log.debug("enable refresh trusteeship schdule job");
                    }
                    // 第三方资金托管，主动查询
                    CronTrigger trigger2 = (CronTrigger) scheduler.getTrigger(TriggerKey
                            .triggerKey(ScheduleConstants.TriggerName.REFRESH_TRUSTEESHIP_OPERATION, ScheduleConstants.TriggerGroup.REFRESH_TRUSTEESHIP_OPERATION));
                    if (trigger2 == null) {
                        initRefreshTrusteeshipJob();
                    } else {
                        scheduler.rescheduleJob(trigger2.getKey(), trigger2);
                    }
                }
                if (enableAutoRepayment.equals("1")) {
                    if (InitJobs.log.isDebugEnabled()) {
                        InitJobs.log.debug("enable auto repayment schdule job");
                    }
                    // 到期自动还款，改状态（还款完成、逾期之类）
                    CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(ScheduleConstants.TriggerName.AUTO_REPAYMENT, ScheduleConstants.TriggerGroup.AUTO_REPAYMENT));
                    if (trigger == null) {
                        initAutoRepaymengJob();
                    } else {
                        scheduler.rescheduleJob(trigger.getKey(), trigger);
                    }
                }
                if (InitJobs.log.isDebugEnabled()) {
                    InitJobs.log.debug("auto baidu ping");
                }/*
                // baidu ping
                CronTrigger triggerPush = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(ScheduleConstants.TriggerName.BAIDU_PING, ScheduleConstants.TriggerGroup.BAIDU_PING));
                if (triggerPush == null) {
                    initInfoPush();
                }
                else {
                    scheduler.rescheduleJob(triggerPush.getKey(), triggerPush);
                }*/
                if (enableRepayAlert.equals("1")) {
                    // 还款提醒
                    CronTrigger trigger3 = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(ScheduleConstants.TriggerName.REPAY_ALERT, ScheduleConstants.TriggerGroup.REPAY_ALERT));
                    if (trigger3 == null) {
                        JobDetail jobDetail3 = JobBuilder.newJob(RepayAlert.class).withIdentity(ScheduleConstants.TriggerName.REPAY_ALERT, ScheduleConstants.TriggerGroup.REPAY_ALERT).build();
                        trigger3 = TriggerBuilder.newTrigger().withIdentity(ScheduleConstants.TriggerName.REPAY_ALERT, ScheduleConstants.TriggerGroup.REPAY_ALERT).forJob(jobDetail3).withSchedule(
                                // 每天上午九点
                                CronScheduleBuilder.cronSchedule("0 0 9 * * ? *")).build();
                        scheduler.scheduleJob(jobDetail3, trigger3);
                    } else {
                        scheduler.rescheduleJob(trigger3.getKey(), trigger3);
                    }
                }
            } catch (SchedulerException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
