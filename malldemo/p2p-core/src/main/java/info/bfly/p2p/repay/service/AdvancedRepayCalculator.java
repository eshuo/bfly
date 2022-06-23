package info.bfly.p2p.repay.service;


/**
 * Description: 提前还款数据 计算接口
 */
public interface AdvancedRepayCalculator {
    /**
     * 计算提前还款的手续费
     *
     * @param loanId 提前还款的借款
     * @return
     */
    Double calculateAdvanceRepayFee(String loanId);

    // /////////////////////////提前还款--结束/////////////////////////////////////////////
    // //////////////////////提前还款--开始////////////////////////////////////////////////

    /**
     * 计算提前还款所需还的本金
     *
     * @param loanId 提前还款的借款
     * @return
     */
    Double getAdvanceRepayCorpus(String loanId);
}
