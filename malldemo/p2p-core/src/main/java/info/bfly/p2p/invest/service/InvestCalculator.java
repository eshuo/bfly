package info.bfly.p2p.invest.service;

public interface InvestCalculator {
    /**
     * 计算投资的预计收益
     *
     * @param investMoney 投资金额
     * @param loanId      借款编号
     * @return
     */
    Double calculateAnticipatedInterest(double investMoney, String loanId);
}
