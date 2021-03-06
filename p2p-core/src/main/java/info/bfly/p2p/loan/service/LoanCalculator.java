package info.bfly.p2p.loan.service;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.repay.model.Repay;
import info.bfly.p2p.risk.model.RiskRank;

import java.util.List;

public interface LoanCalculator {
    /**
     * 计算借款的预期利息
     *
     * @return
     */
    Double calculateAnticipatedInterest(Loan loan);

    /**
     * 计算借款的预期还款
     *
     * @return
     */
    List<Repay> calculateAnticipatedRepays(Loan loan);

    /**
     * 计算借款保证金
     *
     * @param loanMoney 借款金额
     * @return 对应的借款保证金
     */
    Double calculateCashDeposit(double loanMoney);

    /**
     * 计算项目需要还的利息总额
     */
    Double calculateLoanInterest(String loanId);

    /*
     * 查询某一借款正在还款+等待还款的期数
     */
    // public Long getRemainRepayCount(String loanId);

    /**
     * 计算当前标的的最大投资金额（取最大投资额和当前可投金额的最小额）
     *
     * @param loanId
     * @return 当前标的最大投资金额
     * @throws NoMatchingObjectsException 找不到Loan
     */
    Double calculateMoneyMaxInvested(String loanId) throws NoMatchingObjectsException;

    /**
     * 计算当前标的的最小投资金额（取最大投资额和当前可投金额的最小额）
     *
     * @param loanId
     * @return 当前标的最大投资金额
     * @throws NoMatchingObjectsException 找不到Loan
     */
    Double calculateMoneyMinInvested(String loanId) throws NoMatchingObjectsException;

    /**
     * 计算借款尚未募集的金额
     *
     * @param loanId 借款id
     * @return 尚未募集的金额
     * @throws NoMatchingObjectsException 找不到Loan
     */
    Double calculateMoneyNeedRaised(String loanId) throws NoMatchingObjectsException;

    /**
     * 计算借款投标完成百分比
     *
     * @param loanId 借款id
     * @return
     * @throws NoMatchingObjectsException 找不到loan
     */
    Double calculateRaiseCompletedRate(String loanId) throws NoMatchingObjectsException;

    /**
     * 计算借款剩余募集时间，如果未开始，则返回“未开始”，如果到期，则返回“已到期”，如果正在募集，则返回：X天X时X分
     *
     * @param loanId 借款id
     * @return
     * @throws NoMatchingObjectsException 找不到loan
     */
    String calculateRemainTime(String loanId) throws NoMatchingObjectsException;

    /**
     * 计算借款风险等级
     *
     * @param loanId 借款id
     * @return 对应的风险等级对象
     */
    RiskRank calculateRiskRank(String loanId);

    /**
     * 计算项目成功的投资笔数
     *
     * @param loanId
     * @return
     */
    Long countSuccessInvest(String loanId);


    /**
     * 计算借款尚未第三方确认放款的金额
     *
     * @param loanId 借款id
     * @return 尚未放款的金额
     * @throws NoMatchingObjectsException 找不到Loan
     */
    Double calculateMoneyNeedMake(String loanId) throws NoMatchingObjectsException;


    /**
     * 计算借款尚未投资成功的金额
     *
     * @param loanId
     * @return
     * @throws NoMatchingObjectsException
     */
    Double calculateMoneyToBidSuccess(String loanId) throws NoMatchingObjectsException;

}
