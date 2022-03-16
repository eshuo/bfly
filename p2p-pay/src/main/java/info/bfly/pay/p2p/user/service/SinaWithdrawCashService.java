package info.bfly.pay.p2p.user.service;

import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.WithdrawCash;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;

/**
 * Created by XXSun on 5/11/2017.
 */
public interface SinaWithdrawCashService {

    /**
     * 根据ID获取实例
     *
     * @param withdrawId
     * @return
     */
    WithdrawCash getWithdrawById(String withdrawId);


    /**
     * 生成实例
     *
     * @param money
     * @param user
     * @return
     */
    WithdrawCash generateWithdraw(Double money, User user);


    void update(WithdrawCash withdrawCash);

    /**
     * 更新状态
     * @param withdrawCash
     * @param status
     */
    void updateStatus(WithdrawCash withdrawCash,String status);

    /**
     * 第一步，申请提现
     *
     * @param withdrawCash
     * @throws InsufficientBalance
     */
    void applyWithdrawCash(WithdrawCash withdrawCash) throws InsufficientBalance, TrusteeshipReturnException;

    /**
     * 计算提现费用
     *
     * @param amount 提现金额
     * @return double 提现费用
     */
    double calculateFee(double amount);

    /**
     * 第二步，通过提现申请
     *
     * @param withdrawCash 提现instance
     */
    void passWithdrawCashApply(WithdrawCash withdrawCash) throws TrusteeshipReturnException;

    /**
     * 第三步，通过提现复核
     *
     * @param withdrawCash 提现instance
     */
    void passWithdrawCashRecheck(WithdrawCash withdrawCash) throws TrusteeshipReturnException;

    /**
     * 拒绝提现申请
     *
     * @param withdrawCash 提现instance
     */
    void refuseWithdrawCashApply(WithdrawCash withdrawCash) throws TrusteeshipReturnException;

    /**
     * 拒绝复核申请
     *
     * @param withdrawCash 提现instance
     */
    void refuseWithdrawCashRecheck(WithdrawCash withdrawCash) throws TrusteeshipReturnException;

    /**
     * 管理员提现
     *
     * @throws InsufficientBalance
     */
    void withdrawByAdmin(UserBill ub) throws TrusteeshipReturnException;

    /**
     * 第四步，获取提现链接地址
     *
     * @param withdrawId
     * @return
     */
    String getWithdrawLink(String withdrawId) throws TrusteeshipReturnException;

    /**
     * 第五步，确认提现
     *
     * @param withdrawCash
     */
    void confirmWithdraw(WithdrawCash withdrawCash);

    /**
     * 提现失败
     *
     * @param withdrawCash
     */
    void failWithdraw(WithdrawCash withdrawCash);

    /**
     * 记录日志
     *
     * @param withdrawCash
     */
    void doLog(WithdrawCash withdrawCash);

}
