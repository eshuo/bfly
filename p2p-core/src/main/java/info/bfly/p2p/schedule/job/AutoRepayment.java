package info.bfly.p2p.schedule.job;

import info.bfly.p2p.repay.service.RepayService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 还款到期，自动扣款，自动还款，顺便如果账户余额不足，那么项目变为逾期。
 * 
 * @author Administrator
 *
 */
@Component
public class AutoRepayment implements Job {
    @Resource
    private RepayService repayService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        repayService.autoRepay();
    }
}
