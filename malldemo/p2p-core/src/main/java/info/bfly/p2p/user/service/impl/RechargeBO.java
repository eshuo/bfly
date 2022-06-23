package info.bfly.p2p.user.service.impl;

import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.p2p.coupon.event.UserCouponSyncEvent;
import info.bfly.p2p.loan.exception.InsufficientBalance;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.risk.service.SystemBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class RechargeBO {
    @Resource
    HibernateTemplate ht;
    @Resource
    private UserBillService           ibs;
    @Resource
    private SystemBillService         sbs;
    @Autowired
    private ApplicationEventPublisher applicationContext;
    /**
     * 充值支付成功
     *
     * @param recharge
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void rechargeSuccess(Recharge recharge) {
        //增加红包
        if (recharge.getCoupon() != null)
            recharge.setStatus(UserConstants.RechargeStatus.WAIT_COUPON);
        else
            recharge.setStatus(UserConstants.RechargeStatus.SUCCESS);
        recharge.setCallbackTime(new Date());
        ht.merge(recharge);
        // 往InvestorBill中插入值并计算余额
        ibs.transferIntoBalance(recharge.getUser().getId(), recharge.getRealMoney(), OperatorInfo.RECHARGE_SUCCESS, "充值编号：" + recharge.getId(), recharge.getAccountType());

        //处理优惠券
        if(recharge.getCoupon()!=null)
            applicationContext.publishEvent(new UserCouponSyncEvent(recharge.getCoupon().getId()));
        try {
            sbs.transferOut(recharge.getFee(), OperatorInfo.RECHARGE_SUCCESS, "充值手续费, 用户ID：" + recharge.getUser().getId() + "充值ID" + recharge.getId());
        } catch (InsufficientBalance insufficientBalance) {
            //insufficientBalance.printStackTrace();

            //TODO 平台金额不够支付
        }
    }

    /**
     * 充值支付成功
     *
     * @param recharge
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void rechargeFail(Recharge recharge) {
        recharge.setStatus(UserConstants.RechargeStatus.FAIL);
        recharge.setCallbackTime(new Date());
        ht.merge(recharge);
    }

}
