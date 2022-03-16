package info.bfly.p2p.loan.service;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.exception.BorrowedMoneyTooLittle;
import info.bfly.p2p.loan.exception.ExistWaitAffirmInvests;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.exception.InvalidExpectTimeException;
import info.bfly.p2p.loan.model.ApplyEnterpriseLoan;
import info.bfly.p2p.loan.model.Loan;

import java.util.Date;
import java.util.List;

/**
 */
public interface LoanService {

    /**
     * 获取一个对象
     * @param id
     * @return
     */
    Loan get(String id);


    /**
     * 新浪调用完成审核接口
     * @param loanId
     */
    void sinaPassApply(String loanId,String Sina_bid_no,String Inner_bid_no);

    /**
     * 申请企业借款
     *
     * @param ael 企业借款对象
     */
    void applyEnterpriseLoan(ApplyEnterpriseLoan ael);

    /**
     * 申请借款
     *
     * @param loan
     * @throws InsufficientBalance 余额不足以支付保证金
     */
    void applyLoan(Loan loan) throws InsufficientBalance;

    /**
     * 管理员添加借款
     *
     * @param loan
     * @throws InsufficientBalance
     * @throws InvalidExpectTimeException
     */
    void createLoanByAdmin(Loan loan) throws InsufficientBalance, InvalidExpectTimeException;

    /**
     * 处理借款完成工作，即改借款状态、与之相关的投资状态等。
     *
     * @param loanId 借款id
     * @return
     */
    void dealComplete(String loanId);

    /**
     * 借款到预计执行时间，处理借款
     *
     * @param loanId
     *            借款id
     */
    void dealOverExpectTime(String loanId);

    /**
     * 处理借款募集完成。
     *
     * @param loanId
     *            借款编号
     * @throws NoMatchingObjectsException
     *             找不到loan
     */
    void dealRaiseComplete(String loanId) throws NoMatchingObjectsException;

    /**
     * 延长借款募集时间，即延长预计执行时间
     *
     * @param loanId
     *            借款id
     * @param newExpectTime
     *            新的预计执行时间
     * @throws InvalidExpectTimeException
     *             预计执行时间设置错误
     */
    void delayExpectTime(String loanId, Date newExpectTime) throws InvalidExpectTimeException;


    /**
     * 继续融资（满标后）
     * @param loanId
     */
    void  continueInvest(String loanId,Double money) throws ExistWaitAffirmInvests;

    /**
     * 流标
     *
     * @param loanId
     *            借款id
     * @param operatorId
     *            操作用户id
     * @throws ExistWaitAffirmInvests
     */
    void fail(String loanId, String operatorId) throws ExistWaitAffirmInvests;

    /**
     * 获取某笔借款里成功的投资
     *
     * @param loanId
     */
    List<Invest> getSuccessfulInvests(String loanId);

    /**
     * 借款放款，即借款执行，转钱给借款者。
     *
     * @param loanId
     * @throws ExistWaitAffirmInvests
     *             存在等待第三方资金托管确认的投资，不能放款。
     * @throws BorrowedMoneyTooLittle
     *             募集到的资金太少，为0、或者不足以支付借款保证金
     */
    void giveMoneyToBorrower(String loanId) throws ExistWaitAffirmInvests, BorrowedMoneyTooLittle;


    /**
     * 单个借款放款
     * @param investId 投资ID
     * @throws ExistWaitAffirmInvests
     *             存在等待第三方资金托管确认的投资，不能放款。
     * @throws BorrowedMoneyTooLittle
     *             募集到的资金太少，为0、或者不足以支付借款保证金
     */
    void giveMoneyToOneBorrower(String investId) throws ExistWaitAffirmInvests, BorrowedMoneyTooLittle;

    /**
     * 判断借款是否已完成，即是否所有的还款都还了
     *
     * @param loanId
     *            借款id
     * @return 是否已完成
     */
    boolean isCompleted(String loanId);

    /**
     * 检查借款是否到预计执行时间
     *
     * @param loanId
     *            借款id
     */
    boolean isOverExpectTime(String loanId);

    /**
     * 检查借款是否募集完成
     *
     * @param loanId
     *            借款编号
     * @return
     * @throws NoMatchingObjectsException
     *             找不到loan
     */
    boolean isRaiseCompleted(String loanId) throws NoMatchingObjectsException;

    /**
     * 借款申请，通过审核
     *
     * @param loanId
     *            借款id
     * @param auditInfo
     *            审核信息
     * @param verifyUserId
     *            审核人编号
     * @throws InvalidExpectTimeException
     *             预计执行时间设置错误
     */
    void passApply(Loan loan) throws InvalidExpectTimeException;

    /**
     * 借款申请，未通过审核，即拒绝借款申请
     *
     * @param loanId
     *            借款id
     * @param refuseInfo
     *            审核信息
     * @param verifyUserId
     *            审核人编号
     */
    void refuseApply(String loanId, String refuseInfo, String verifyUserId);

    /**
     * 更新loan
     *
     * @param loan
     */
    void update(Loan loan);

    /**
     * 审核企业借款
     *
     * @param ael
     *            企业借款对象
     */
    void verifyEnterpriseLoan(ApplyEnterpriseLoan ael);


    /**
     * 判断项目代收是否全部完成(不考虑超标投资)
     *
     * @param loanId
     *            借款id
     * @return 是否已完成
     */
    boolean isCompletedLoan(String loanId) throws NoMatchingObjectsException;


    /**
     * 判断项目是否满标
     * @param loanId
     * @return
     * @throws NoMatchingObjectsException
     */
    boolean isLoanFullInvest(String loanId) throws NoMatchingObjectsException;


    Double getLoanMoneyToBidSuccess(String loanId);





}
