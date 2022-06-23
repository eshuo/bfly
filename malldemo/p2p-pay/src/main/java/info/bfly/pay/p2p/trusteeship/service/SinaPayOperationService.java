package info.bfly.pay.p2p.trusteeship.service;


/**
 * 新浪操作处理Service
 * Created by Administrator on 2017/5/25 0025.
 */
public interface SinaPayOperationService {


    /**
     * 交易结果操作
     *
     * @param callbackOperationId
     */
    void TradeStatusSyncOperation(String callbackOperationId);

    /**
     * 企业会员审核通知 操作
     *
     * @param callbackOperationId
     */
    void AuditStatusSyncOperation(String callbackOperationId);

    /**
     * 批处理交易结果通知 操作
     *
     * @param callbackOperationId
     */
    void BatchTradeStatusSyncOperation(String callbackOperationId);

    /**
     * 标审核通知 操作
     *
     * @param callbackOperationId
     */
    void BidStatusSyncOperation(String callbackOperationId);

    /**
     * 充值结果通知 操作
     *
     * @param callbackOperationId
     */
    void DepositStatusSyncOperation(String callbackOperationId);

    /**
     * 退款结果通知 操作
     *
     * @param callbackOperationId
     */
    void RefundStatusSyncOperation(String callbackOperationId);

    /**
     * 提现结果通知 操作
     *
     * @param callbackOperationId
     */
    void WithdrawStatusSyncOperation(String callbackOperationId);


}
