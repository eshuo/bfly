package info.bfly.pay.p2p.trusteeship.publisher;

import info.bfly.core.annotations.Log;
import info.bfly.p2p.coupon.event.UserCouponSyncEvent;
import info.bfly.pay.p2p.trusteeship.event.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 添加发布信息
 * Created by Administrator on 2017/5/25 0025.
 */
@Component
public class SinaPayPublisher {


    @Autowired
    private ApplicationEventPublisher applicationContext;

    @Log
    private Logger logger;


    /**
     * 发布 交易结果通知 event
     *
     * @param trusteeshipOperationId
     */
    public void TradeStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=TradeStatusSyncEventPublisher");
        applicationContext.publishEvent(new TradeStatusSyncEvent(this, trusteeshipOperationId));
    }

    /**
     * 发布  企业会员审核通知 event
     *
     * @param trusteeshipOperationId
     */
    public void AuditStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=AuditStatusSyncEventPublisher");
        applicationContext.publishEvent(new AuditStatusSyncEvent( trusteeshipOperationId));
    }

    /**
     * 发布 使用优惠券通知
     */
    public void UserCouponSyncEventPublisher(String userCouponId){
        logger.info("发布任务：SinaPayPublisher=UserCouponSyncEventPublisher");
        applicationContext.publishEvent(new UserCouponSyncEvent(userCouponId));
    }
    /**
     * 发布 批处理交易结果通知 event
     *
     * @param trusteeshipOperationId
     */
    public void BatchTradeStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=BatchTradeStatusSyncEventPublisher");
        applicationContext.publishEvent(new BatchTradeStatusSyncEvent(this, trusteeshipOperationId));
    }

    /**
     * 发布 标审核通知 event
     *
     * @param trusteeshipOperationId
     */
    public void BidStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=BidStatusSyncEventPublisher");
        applicationContext.publishEvent(new BidStatusSyncEvent(this, trusteeshipOperationId));
    }

    /**
     * 发布 充值结果通知 event
     *
     * @param trusteeshipOperationId
     */
    public void DepositStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=DepositStatusSyncEventPublisher");
        applicationContext.publishEvent(new DepositStatusSyncEvent(this, trusteeshipOperationId));
    }

    /**
     * 发布 退款结果通知 event
     *
     * @param trusteeshipOperationId
     */
    public void RefundStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=RefundStatusSyncEventPublisher");
        applicationContext.publishEvent(new RefundStatusSyncEvent(this, trusteeshipOperationId));
    }

    /**
     * 发布 提现结果通知 event
     *
     * @param trusteeshipOperationId
     */
    public void WithdrawStatusSyncEventPublisher(String trusteeshipOperationId) {
        logger.info("发布任务：SinaPayPublisher=WithdrawStatusSyncEventPublisher");
        applicationContext.publishEvent(new WithdrawStatusSyncEvent(this, trusteeshipOperationId));
    }


}
