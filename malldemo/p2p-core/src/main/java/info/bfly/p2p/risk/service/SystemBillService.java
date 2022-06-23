package info.bfly.p2p.risk.service;

import info.bfly.p2p.loan.exception.InsufficientBalance;

/**
 * 系统收益账户service
 *
 * @author Administrator
 */
public interface SystemBillService {
    /**
     * 获取最新一条数据
     *
     * @return
     */
    // public SystemBill getLastestBill();

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
     */
    void transferInto(double money, String reason, String detail);

    /**
     * 转出
     *
     * @param money          金额
     * @throws InsufficientBalance 余额不足
     */
    void transferOut(double money, String reason, String detail) throws InsufficientBalance;
}
