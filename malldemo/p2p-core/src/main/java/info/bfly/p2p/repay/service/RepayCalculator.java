package info.bfly.p2p.repay.service;

import info.bfly.p2p.repay.model.AdvanceRepay;

public interface RepayCalculator {
    /**
     * 计算提前还款
     */
    AdvanceRepay calculateAdvanceRepay(String loanId);

    /**
     * 计算预计利息
     *
     * @param investMoney 投资金额
     * @param loanId      借款编号
     * @return
     */
    Double calculateAnticipatedInterest(double money, String loanId);

    /**
     * 计算逾期还款的费用（逾期利息+罚息）
     *
     * @param loanId 借款交易id
     * @return 费用金额
     */
    double calculateOverdueRepayFee(String loanId);
}
