package info.bfly.p2p.invest.service;

import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.invest.exception.ExceedMaxAcceptableRate;
import info.bfly.p2p.invest.exception.ExceedMoneyNeedRaised;
import info.bfly.p2p.invest.exception.IllegalLoanStatusException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import org.hibernate.LockMode;

/**
 * Description:投资service
 */
public interface InvestService {
    /**
     * 新建投资
     *
     * @param invest 新建投资对象
     * @throws InsufficientBalance          余额不足
     * @throws ExceedMoneyNeedRaised        投资金额大于尚未募集的金额
     * @throws ExceedMaxAcceptableRate      竞标借款利率大于借款者可接受的最高利率
     * @throws ExceedDeadlineException      优惠券过期
     * @throws UnreachedMoneyLimitException 优惠券未达到使用条件
     * @throws IllegalLoanStatusException   借款不是可投资状态
     */
    Invest create(Invest invest) throws InsufficientBalance, ExceedMoneyNeedRaised, ExceedMaxAcceptableRate, ExceedDeadlineException, UnreachedMoneyLimitException, IllegalLoanStatusException;

    /**
     * 获取投资记录
     *
     * @param investId
     */
    Invest get(String investId);

    /**
     * 获取投资记录
     *
     * @param investId
     */
    Invest get(String investId, LockMode lockMode);

    /**
     * 投资成功
     *
     * @param invest
     * @return
     */
    Invest success(Invest invest) throws InsufficientBalance;

    /**
     * 更新
     *
     * @param invest
     */
    void update(Invest invest);

    /**
     * 第三方资金冻结
     *
     * @param invest
     * @return
     */
    Invest frozen(Invest invest);

    /**
     * 投资失败
     *
     * @param invest
     * @return
     */
    Invest fail(Invest invest);

    /**
     * 删除
     *
     * @param invest
     */
    void delete(Invest invest);

    /**
     * 通过用户id，查询该用户的所有投资
     *
     * @param userId
     *            用户ID
     * @return 该用户的所有投资
     */
    // public List<Invest> getInvestsByUserId(String userId);

    /**
     * 生成id，当前日期+当前投资六位顺序序号+借款编号+投资所在借款中六位顺序序号
     *
     * @param loanId 投资的借款的id
     * @return 生成的借款id
     */
    String generateId(String loanId);


    /**
     * 撤单操作
     *
     * @param investId
     */
    void cancelOrder (String investId);


    /**
     * 退款操作
     *
     * @param investId
     */
    void investRefunds(String investId);


    /**
     * 将用户冻结金额划如资金池
     *
     * @param userId
     * @param money
     * @param operatorInfo
     * @param operatorDetail
     * @throws InsufficientBalance
     */
    void transferOutFromFrozen(String userId, double money, String operatorInfo, String operatorDetail) throws InsufficientBalance;
}
