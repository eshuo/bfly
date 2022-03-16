package info.bfly.p2p.schedule.job;

import info.bfly.core.annotations.Log;
import info.bfly.p2p.invest.service.AutoInvestService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: 借款审核通过以后，开启当前借款的自动投标
 */
@Component
public class AutoInvestAfterLoanPassed implements Job {
    public static final String LOAN_ID = "aialp_loan_id";
    @Resource
    private AutoInvestService autoInvestService;
    @Log
    Logger log;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            String loanId = context.getJobDetail().getJobDataMap().getString(AutoInvestAfterLoanPassed.LOAN_ID);
            if (log.isDebugEnabled()) {
                log.debug("autoInvestJob, loanId: " + loanId);
            }
            autoInvestService.autoInvest(loanId);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("{}",e);
            }
        }
    }
}
