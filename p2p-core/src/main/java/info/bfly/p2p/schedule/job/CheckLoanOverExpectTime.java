package info.bfly.p2p.schedule.job;

import info.bfly.core.util.SpringBeanUtil;
import info.bfly.p2p.loan.service.LoanService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 检查项目是否到预计执行时间
 * 
 * @author Administrator=
 *
 */
public class CheckLoanOverExpectTime implements Job {
    public static final String LOAN_ID = "loanId";
    private LoanService loanService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        loanService = (LoanService) SpringBeanUtil.getBeanByName("loanService");
        String loanId = context.getJobDetail().getJobDataMap().getString(CheckLoanOverExpectTime.LOAN_ID);
        loanService.dealOverExpectTime(loanId);
    }
}
