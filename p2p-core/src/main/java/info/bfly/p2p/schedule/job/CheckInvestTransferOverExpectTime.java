package info.bfly.p2p.schedule.job;

import info.bfly.p2p.invest.service.TransferService;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 检查债权转让是否在转让申请有效期内
 * 
 * @author wangxiao
 *
 */
public class CheckInvestTransferOverExpectTime implements Job {
    public static final String INVEST_TRANSFER_ID = "investTransferId";
    @Resource
    private TransferService transferService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String investTransferId = context.getJobDetail().getJobDataMap().getString(CheckInvestTransferOverExpectTime.INVEST_TRANSFER_ID);
        transferService.dealOverExpectTime(investTransferId);
    }
}
