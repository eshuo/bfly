package info.bfly.p2p.repay.service;


/**
 * Description: 逾期还款数据 计算接口
 */
public interface OverdueRepayCalculator {
    /**
     * 获取逾期还款，需支付的罚息
     *
     * @param money 逾期金额
     * @param rate  利率
     * @param day   逾期时间
     * @return
     */
    double calculateOverduePenalty(double money, double rate, int day);

    // ///////////////////////逾期--结束//////////////////////////////////////////////////

    /**
     * 获取逾期还款，需支付的因投资产生的逾期时期的利息（除掉网站罚息的那部分）
     *
     * @param money 逾期金额
     * @param rate  利率
     * @param day   逾期时间
     * @return
     */
    double calculateOverdueRatePay(double money, double rate, int day);

    // ///////////////////////逾期--开始//////////////////////////////////////////////////

    /**
     * 计算逾期还款的费用（逾期利息+罚息）
     *
     * @param loanId 借款交易id
     * @return 费用金额
     */
    double calculateOverdueRepayFee(String loanId);

    /**
     * 计算某期逾期借款需要支付的逾期利息+逾期罚金
     *
     * @param repayment
     * @return
     */
    double calculateOverdueSumByPeriod(String repayId);
}
