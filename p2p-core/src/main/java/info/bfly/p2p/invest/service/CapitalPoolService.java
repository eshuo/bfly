package info.bfly.p2p.invest.service;

import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.exception.InsufficientBalance;

/**
 * 资金池投资接口
 *
 * @author gaoxinzhong
 */
public interface CapitalPoolService {
    /**
     * 新建投资 user_wealth
     *
     * @param invest      新建投资对象
     * @param investId    新建投资对象id
     * @param investMoney 投资金额
     * @throws InsufficientBalance          余额不足
     * @throws ExceedMoneyNeedRaised        投资金额大于尚未募集的金额
     * @throws ExceedMaxAcceptableRate      竞标借款利率大于借款者可接受的最高利率
     * @throws ExceedDeadlineException      优惠券过期
     * @throws UnreachedMoneyLimitException 优惠券未达到使用条件
     * @throws IllegalLoanStatusException   借款不是可投资状态
     */
    void createGood(Invest invest, String investId, Double investMoney) throws InsufficientBalance, ExceedMoneyNeedRaised, ExceedMaxAcceptableRate, ExceedDeadlineException,
            UnreachedMoneyLimitException, IllegalLoanStatusException;

    /**
     * 生成id，当前日期+当前投资六位顺序序号+借款编号+投资所在借款中六位顺序序号
     *
     * @param loanId 投资的借款的id
     * @return 生成的借款id
     */
    String generateId(String loanId);
    /**
     * 通过用户id，查询该用户的所有投资
     *
     * @param userId
     *            用户ID
     * @return 该用户的所有投资
     */
    // public List<Invest> getInvestsByUserId(String userId);
}
