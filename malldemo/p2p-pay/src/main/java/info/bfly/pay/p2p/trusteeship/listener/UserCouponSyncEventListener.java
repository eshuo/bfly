package info.bfly.pay.p2p.trusteeship.listener;

import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.user.UserConstants;
import info.bfly.core.annotations.Log;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.event.UserCouponSyncEvent;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.coupon.service.UserCouponService;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.invest.service.InvestService;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.trusteeship.exception.TrusteeshipFormException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.exception.TrusteeshipTicketException;
import info.bfly.p2p.user.service.RechargeService;
import info.bfly.pay.p2p.user.model.MoneyTransfer;
import info.bfly.pay.p2p.user.service.SinaMoneyTransferService;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Created by XXSun on 6/6/2017.
 */
@Async
@Component
public class UserCouponSyncEventListener extends AbstractSystemUserSyncEventListener<UserCouponSyncEvent> {

    @Log
    Logger log;

    @Autowired
    private UserCouponService userCouponService;


    @Autowired
    private SinaMoneyTransferService sinaMoneyTransferService;

    @Autowired
    private BaseService<MoneyTransfer> moneyTransferService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private InvestService investService;

    @Override
    @Transactional
    void handleEvent(UserCouponSyncEvent event) {
        Assert.notNull(event.getSource(), "优惠券Id不能为空");
        String userCouponId = (String) event.getSource();
        UserCoupon userCoupon = userCouponService.get(userCouponId, LockMode.PESSIMISTIC_WRITE);
        Assert.notNull(userCoupon, "用户优惠券信息不存在");
        switch (userCoupon.getCoupon().getType()) {
            case CouponConstants.Type.INVEST:
                handleInvest(userCoupon.getTarget());
                break;
                //投资优惠券
            case CouponConstants.Type.RECHARGE:
                handleRecharge(userCoupon.getTarget());
                break;
                //充值
            case CouponConstants.Type.REPAY_LOAN:
                //还款
                break;
        }
    }

    private void handleInvest(String investId){
        Assert.hasText(investId, "订单号不能为空！");
        Invest invest = investService.get(investId);
        Assert.notNull(invest, "投资订单" + investId + "不存在！");

        UserCoupon coupon = invest.getUserCoupon();
        if(CouponConstants.UserCouponStatus.USED.equals(coupon.getStatus())){

        }
    }

    private void handleRecharge(String rechargeId) {
        Assert.hasText(rechargeId, "订单号不能为空！");
        Recharge recharge = rechargeService.get(rechargeId);
        Assert.notNull(recharge, "充值订单" + rechargeId + "不存在！");
        try {
            //判断是否已经处理
            UserCoupon coupon = recharge.getCoupon();
            if (CouponConstants.UserCouponStatus.USED.equals(coupon.getStatus())) {
                MoneyTransfer moneyTransfer = sinaMoneyTransferService.initSystemTransfer(coupon.getCoupon().getMoney(), recharge.getUser().getId(), recharge.getAccountType());
                sinaMoneyTransferService.bindTarget(moneyTransfer, UserConstants.MoneyTransferType.USERCOUPON, coupon.getId());
                coupon.setOperationOrderNo(moneyTransfer.getId());
                sinaMoneyTransferService.applyMoneyTransfer(moneyTransfer);
                coupon.setStatus(CouponConstants.UserCouponStatus.PROCESS);
                userCouponService.update(coupon);
            } else if (coupon.getStatus().equals(CouponConstants.UserCouponStatus.PROCESS)) {
                MoneyTransfer moneyTransfer = moneyTransferService.get(MoneyTransfer.class,coupon.getOperationOrderNo());
                Assert.notNull(moneyTransfer, "找不到转账订单");
                //TODO
            }
        } catch (InsufficientBalance insufficientBalance) {
            log.error("系统余额不足，稍后重试");
            //TODO 发送消息
        } catch (TrusteeshipReturnException e) {
            log.error(e.getMessage());
            //TODO 发送消息
        } catch (TrusteeshipFormException e) {
            log.error("返回form表单");
        } catch (TrusteeshipTicketException e) {
            log.error("返回推进");
        }

    }
}
