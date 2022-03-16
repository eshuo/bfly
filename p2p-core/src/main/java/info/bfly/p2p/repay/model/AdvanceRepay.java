package info.bfly.p2p.repay.model;

import info.bfly.p2p.loan.model.Loan;

/**
 * 提前还款
 *
 * @author Administrator
 *
 */
public class AdvanceRepay {
    private Loan   loan;
    /**
     * 本金总额
     */
    private Double corpus;
    /**
     * 还款手续费
     */
    private Double repayFee;
    /**
     * 给系统罚金
     */
    private Double feeToSystem;
    /**
     * 给投资人罚金
     */
    private Double feeToInvestor;

    public Double getCorpus() {
        return corpus;
    }

    public Double getFeeToInvestor() {
        return feeToInvestor;
    }

    public Double getFeeToSystem() {
        return feeToSystem;
    }

    public Loan getLoan() {
        return loan;
    }

    public Double getRepayFee() {
        return repayFee;
    }

    public void setCorpus(Double corpus) {
        this.corpus = corpus;
    }

    public void setFeeToInvestor(Double feeToInvestor) {
        this.feeToInvestor = feeToInvestor;
    }

    public void setFeeToSystem(Double feeToSystem) {
        this.feeToSystem = feeToSystem;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setRepayFee(Double repayFee) {
        this.repayFee = repayFee;
    }
}
