package info.bfly.pay.p2p.trusteeship.job;

import info.bfly.core.annotations.Log;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.p2p.trusteeship.service.impl.TrusteeshipOperationBO;
import info.bfly.pay.bean.enums.*;
import info.bfly.pay.p2p.trusteeship.publisher.SinaPayPublisher;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by XXSun on 3/5/2017.
 */
@Service
public class TrusteeshipOperationJob implements Job {


    @Log
    private Logger log;

    private final TrusteeshipOperationBO trusteeshipOperationBO;


    private final SinaPayPublisher sinaPayPublisher;

    @Autowired
    public TrusteeshipOperationJob(TrusteeshipOperationBO trusteeshipOperationBO, SinaPayPublisher sinaPayPublisher) {
        this.trusteeshipOperationBO = trusteeshipOperationBO;
        this.sinaPayPublisher = sinaPayPublisher;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<TrusteeshipOperation> waitCallbackOperation = trusteeshipOperationBO.getWaitCallbackOperation();
        for (TrusteeshipOperation callbackOperation : waitCallbackOperation) {
            String operator = callbackOperation.getOperator();
            if ((TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.PAY_FINISHED).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.TRADE_CLOSED).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.PRE_AUTH_APPLY_SUCCESS).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.TRADE_FINISHED).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.WAIT_PAY).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.PRE_AUTH_CANCELED).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.trade_status_sync + TRADE_STATUS.TRADE_FAILED).equals(operator)) {
                sinaPayPublisher.TradeStatusSyncEventPublisher(callbackOperation.getId());
                continue;
            }


            if ((TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.deposit_status_sync + DEPOSIT_STATUS.SUCCESS).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.deposit_status_sync + DEPOSIT_STATUS.FAILED).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.deposit_status_sync + DEPOSIT_STATUS.PROCESSING).equals(operator)) {
                sinaPayPublisher.DepositStatusSyncEventPublisher(callbackOperation.getId());
                continue;
            }

            if ((TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.withdraw_status_sync + WITHDRAW_STATUS.SUCCESS).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.withdraw_status_sync + WITHDRAW_STATUS.PROCESSING).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.withdraw_status_sync + WITHDRAW_STATUS.FAILED).equals(operator)) {
                sinaPayPublisher.WithdrawStatusSyncEventPublisher(callbackOperation.getId());
                continue;
            }
            if ((TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.refund_status_sync + REFUND_STATUS.SUCCESS).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.refund_status_sync + REFUND_STATUS.PAY_FINISHED).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.refund_status_sync + REFUND_STATUS.FAILED).equals(operator)) {
                sinaPayPublisher.RefundStatusSyncEventPublisher(callbackOperation.getId());
                continue;
            }
            if ((TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.VALID).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.INIT).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.INVALID).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.AUDITING).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.bid_status_sync + BID_STATUS.REJECT).equals(operator)) {
                sinaPayPublisher.BidStatusSyncEventPublisher(callbackOperation.getId());
                continue;
            }
            if ((TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.audit_status_sync + AUDIT_STATUS.SUCCESS).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.audit_status_sync + AUDIT_STATUS.PROCESSING).equals(operator) ||
                    (TrusteeshipConstants.Trusteeship.SINAPAY + NOTIFY_TYPE.audit_status_sync + AUDIT_STATUS.FAILED).equals(operator)) {
                sinaPayPublisher.AuditStatusSyncEventPublisher(callbackOperation.getId());
                continue;
            }
            //TODO MESSAGE
            log.info("callbackOperation {} not handle", callbackOperation.getId());
        }
    }
}
