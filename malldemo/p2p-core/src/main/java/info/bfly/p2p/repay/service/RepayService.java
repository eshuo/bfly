package info.bfly.p2p.repay.service;

import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.repay.exception.AdvancedRepayException;
import info.bfly.p2p.repay.exception.NormalRepayException;
import info.bfly.p2p.repay.exception.OverdueRepayException;
import info.bfly.p2p.repay.model.LoanRepay;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Description: 还款管理接口
 */
public interface RepayService {
    /**
     * 获取一个还款对象
     */
    LoanRepay get(String id);

    /**
     * 提前还款
     *
     * @param loanId 借款id
     * @throws AdvancedRepayException 不符合提前还款条件
     * @throws InsufficientBalance    余额不足
     */
    void advanceRepay(String loanId) throws InsufficientBalance, AdvancedRepayException;

    /**
     * 还款到期，自动扣款，否则项目状态为逾期，还款和借款也变为逾期，锁定用户账号；如果还款已经逾期了，那么每天都计算该还款的逾期费用，
     * 加入到还款的本金和利息中。逾期超过一年，则变为坏账。
     */
    void autoRepay();

    /**
     * 通过借款交易的id生成还款信息
     *
     * @param loanId 借款交易的id
     */
    void generateRepay(String loanId);


    /**
     * 生成投资还款信息
     * @param investId
     */
    void generateOneRepay(String investId);

    /**
     * 判断是否在还款期
     *
     * @param repayDate 还款日
     */
    boolean isInRepayPeriod(Date repayDate);

    /**
     * 正常还款
     *
     * @param loanRepay 还款
     * @throws InsufficientBalance
     * @throws NormalRepayException
     */
    void normalRepay(LoanRepay loanRepay) throws InsufficientBalance, NormalRepayException;

    /**
     * 正常还款
     *
     * @param repayId 还款编号
     * @throws InsufficientBalance
     * @throws NormalRepayException
     */
    void normalRepay(String repayId) throws InsufficientBalance, NormalRepayException;

    /**
     * 逾期还款
     *
     * @param repayId 还款id
     * @throws InsufficientBalance   余额不足
     * @throws OverdueRepayException
     */
    void overdueRepay(String repayId) throws InsufficientBalance, OverdueRepayException;

    /**
     * 管理员进行逾期还款
     *
     * @param repayId     还款id
     * @param adminUserId 管理员用户id
     */
    void overdueRepayByAdmin(String repayId, String adminUserId);

    void finishInvestRepay(String investRepayId) throws NormalRepayException, InsufficientBalance;

    @Transactional
    void dealComplete(String loanRepayId);

    boolean isComplete(String loanRepayId);

    /**
     * 还款提醒，n天以内到还款日的还款，或者逾期的。给还款人发短信
     */
    void repayAlert();

    void update(LoanRepay lr);

    void finishRepay(String loanRepayId) throws InsufficientBalance;
}
