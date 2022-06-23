package info.bfly.p2p.user.service;

import info.bfly.archer.user.model.User;
import info.bfly.archer.user.model.UserBill;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.WithdrawCash;

/**
 * Description:提现接口
 */
public interface WithdrawCashService {


    /**
     * 根据ID获取实例
     * @param withdrawId
     * @return
     */
    WithdrawCash getWithdrawById(String withdrawId);


    /**
     * 生成实例
     * @param money
     * @param user
     * @return
     */
    @Deprecated
    WithdrawCash generateWithdraw(Double money,User user);
    /**
     * 生成实例
     * @param money
     * @param user
     * @return
     */
    WithdrawCash generateWithdraw(Double money,String accountType,User user);
    WithdrawCash merge(WithdrawCash withdrawCash);

    void update(WithdrawCash withdrawCash);
    /**
     * 第一步，申请提现
     *
     * @param withdraw
     * @throws InsufficientBalance
     */
    void applyWithdrawCash(WithdrawCash withdraw) throws InsufficientBalance;

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
    void passWithdrawCashApply(WithdrawCash withdrawCash);

    /**
     * 第三步，通过提现复核
     *
     * @param withdrawCash 提现instance
     */
    void passWithdrawCashRecheck(WithdrawCash withdrawCash);

    /**
     * 拒绝提现申请
     *
     * @param withdrawCash
     *            提现instance
     */
    void refuseWithdrawCashApply(WithdrawCash withdrawCash);

    /**
     * 拒绝复核申请
     *
     * @param withdrawCash
     *            提现instance
     */
    void refuseWithdrawCashRecheck(WithdrawCash withdrawCash);

    /**
     * 管理员提现
     *
     * @throws InsufficientBalance
     */
    void withdrawByAdmin(UserBill ub) throws InsufficientBalance;

    /**
     * 第四步，提现处理中
     * @param withdrawCash
     */
    void processWithdraw(WithdrawCash withdrawCash);

    /**
     * 第五步，确认提现
     * @param withdrawCash
     */
    void confirmWithdraw(WithdrawCash withdrawCash);

    /**
     * 提现失败
     * @param withdrawCash
     */
    void failWithdraw(WithdrawCash withdrawCash);
}
