package info.bfly.p2p.invest.service;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.p2p.invest.exception.ExceedInvestTransferMoney;
import info.bfly.p2p.invest.exception.InvestTransferException;
import info.bfly.p2p.invest.model.TransferApply;
import info.bfly.p2p.loan.exception.InsufficientBalance;

import org.hibernate.ObjectNotFoundException;

/**
 * Description:债权转让service
 */
public interface TransferService {
    /**
     * 申请债权转让
     *
     * @param investId      被转让的投资
     * @param money         转让的本金
     * @param transferMoney 债权转让价格
     */
    void applyInvestTransfer(TransferApply ta) throws ExceedInvestTransferMoney;

    /**
     * 计算债权转让手续费
     *
     * @param ta 债权转让对象
     * @return
     * @throws NoMatchingObjectsException
     */
    double calculateFee(TransferApply ta);

    /**
     * 计算债权转让完成百分比
     *
     * @param loanId
     * @return
     * @throws NoMatchingObjectsException
     * @author wangxiao 3-27
     */
    double calculateInvestTransferCompletedRate(String transferApplyId) throws NoMatchingObjectsException;

    /**
     * 计算未转出的本金
     *
     * @param investTransferId
     * @return
     * @author wx 4-9
     */
    double calculateRemainCorpus(String transferApplyId);

    /**
     * 计算债权转让剩余时间
     *
     * @param transferApplyId
     * @return
     * @throws NoMatchingObjectsException
     */
    String calculateRemainTime(String transferApplyId) throws NoMatchingObjectsException;

    /**
     * 计算投资中某本金的当前价格
     *
     * @param investId
     * @param corpus
     * @return
     */
    double calculateWorth(String investId, double corpus);

    /**
     * 取消债权转让
     *
     * @param investTransferId 债权转让id
     */
    void cancel(String investTransferId);

    /**
     * 判断当前投资是否可转让
     *
     * @param investId
     * @return
     * @throws ObjectNotFoundException
     * @author wangxiao
     */
    boolean canTransfer(String investId) throws InvestTransferException;

    /**
     * 债权转让到期
     *
     * @param investTransferId 债权转让id
     */
    void dealOverExpectTime(String investTransferId);

    /**
     * 债权转让成功
     *
     * @param investTransferId 被转让的债权
     * @param userId           债权购买者
     * @param transferMoney    转让价格
     * @throws InsufficientBalance 余额不足
     */
    void transfer(String transferApplyId, String userId, double transferMoney) throws InsufficientBalance, ExceedInvestTransferMoney;
}
