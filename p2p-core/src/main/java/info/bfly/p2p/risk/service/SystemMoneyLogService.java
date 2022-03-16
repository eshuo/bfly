package info.bfly.p2p.risk.service;

import info.bfly.p2p.loan.exception.InsufficientBalance;

/**
 * 系统金额账户service
 *
 * @author Administrator
 */
public interface SystemMoneyLogService {
    /**
     * 获取最新一条数据
     *
     * @return
     */
    // public SystemMoneyLog getLastestBill();

    /**
     * 获取账户余额
     *
     * @return
     */
    double getBalance();

    /**
     * 转入.
     *
     * @param money          金额
     * @param operatorType   操作类型
     * @param operatorDetail 操作详情
     */
    void transferInto(double money, String reason, String detail, String fromAccount, String toAccount);

    /**
     * 转出
     *
     * @param money          金额
     * @param operatorType   操作类型
     * @param operatorDetail 操作详情
     * @throws InsufficientBalance 余额不足
     */
    void transferOut(double money, String reason, String detail, String fromAccount, String toAccount) throws InsufficientBalance;
}
