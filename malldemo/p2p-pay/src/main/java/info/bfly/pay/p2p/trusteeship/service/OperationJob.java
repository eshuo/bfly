package info.bfly.pay.p2p.trusteeship.service;

import info.bfly.core.annotations.Log;
import info.bfly.p2p.schedule.ScheduleConstants;
import info.bfly.pay.p2p.trusteeship.job.TrusteeshipOperationJob;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 新浪回调处理
 */
@Component
public class OperationJob implements ApplicationListener<ContextRefreshedEvent> {

    @Log
    static Logger log;
    @Resource
    StdScheduler scheduler;

    private void initTrusteeshipOperationJob() throws SchedulerException {
        // 新浪回调
        JobDetail jobDetail = JobBuilder.newJob(TrusteeshipOperationJob.class)
                .withIdentity(ScheduleConstants.JobName.TRUSTEESHIP_OPE_RATION_JOB_NAME, ScheduleConstants.JobGroup.TRUSTEESHIP_OPE_RATION_GROUP).build();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(ScheduleConstants.TriggerName.TRUSTEESHIP_OPE_RATION_JOB_TRIGGERNAME, ScheduleConstants.TriggerGroup.TRUSTEESHIP_OPE_RATION_JOB_TRIGGERGROUP)
                .forJob(jobDetail).withSchedule(
                        // 每十分钟执行一次
                        CronScheduleBuilder.cronSchedule("0 0/5 * * * ? *")).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //启动程序启动

        if (event.getApplicationContext().getParent() == null) {
            // root application context 没有parent，他就是老大
            // 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。 初始化所有的调度。
            // 第三方资金托管，主动查询，默认不开启
            log.info("Sina Callback {}", "Start");

            try {
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey
                        .triggerKey(ScheduleConstants.TriggerName.TRUSTEESHIP_OPE_RATION_JOB_TRIGGERNAME, ScheduleConstants.TriggerGroup.TRUSTEESHIP_OPE_RATION_JOB_TRIGGERGROUP));

                if (trigger == null) {
                    log.info("Sina Callback {}", "initTrusteeshipOperationJob");
                    initTrusteeshipOperationJob();
                } else {
                    log.info("Sina Callback {}", "rescheduleJob");
                    scheduler.rescheduleJob(trigger.getKey(), trigger);
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }


    }
}
