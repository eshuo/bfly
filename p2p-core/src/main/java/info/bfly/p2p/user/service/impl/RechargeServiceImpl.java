package info.bfly.p2p.user.service.impl;

import info.bfly.archer.user.UserBillConstants.OperatorInfo;
import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.UserConstants.RechargeStatus;
import info.bfly.archer.user.model.RechargeBankCard;
import info.bfly.archer.user.model.UserBill;
import info.bfly.archer.user.service.UserBillService;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.exception.UnreachedMoneyLimitException;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.coupon.service.UserCouponService;
import info.bfly.p2p.loan.model.Recharge;
import info.bfly.p2p.risk.FeeConfigConstants.FeePoint;
import info.bfly.p2p.risk.FeeConfigConstants.FeeType;
import info.bfly.p2p.risk.service.impl.FeeConfigBO;
import info.bfly.p2p.user.service.RechargeService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 */
@Service("rechargeService")
public class RechargeServiceImpl implements RechargeService {
    @Log
    private static Logger log;

    @Autowired
    private
    IdGenerator idGenerator;

    @Autowired
    private
    UserCouponService userCouponService;


    @Resource
    private
    HibernateTemplate ht;
    @Resource
    private
    RechargeBO        rechargeBO;
    @Resource
    private UserBillService userBillService;
    @Resource
    private FeeConfigBO     feeConfigBO;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Recharge initRecharge(Recharge recharge) {
        // 往recharge中插入值。
        recharge.setId(generateId());
        recharge.setFee(0D);
        recharge.setRechargeWay("");
        recharge.setIsRechargedByAdmin(false);
        recharge.setTime(new Date());
        recharge.setStatus(UserConstants.RechargeStatus.WAIT_PAY);
        ht.save(recharge);
        return recharge;
    }

    @Override
    public double calculateFee(double amount) {
        return feeConfigBO.getFee(FeePoint.RECHARGE, FeeType.FACTORAGE, null, null, amount);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String createOfflineRechargeOrder(Recharge recharge) {

        return "success";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Recharge createRechargeOrder(Recharge recharge) throws ExceedDeadlineException, UnreachedMoneyLimitException {
        // 往recharge中插入值。
        recharge.setId(generateId());
        recharge.setRealMoney(recharge.getActualMoney());
        if (recharge.getCoupon() != null) {
            // 优惠券
            if (StringUtils.isEmpty(recharge.getCoupon().getId())) {
                recharge.setCoupon(null);
            } else {
                UserCoupon coupon = userCouponService.get(recharge.getCoupon().getId(), LockMode.PESSIMISTIC_WRITE);
                // 判断是否有代金券；判断能否用代金券
                if (!coupon.getStatus().equals(CouponConstants.UserCouponStatus.UNUSED)) {
                    throw new ExceedDeadlineException();
                }
                // 判断代金券是否达到使用条件
                if (recharge.getActualMoney() < coupon.getCoupon().getLowerLimitMoney()) {
                    throw new UnreachedMoneyLimitException();
                }
                userCouponService.userUserCoupon(coupon.getId(), recharge.getId());
                double realMoney = ArithUtil.sub(recharge.getActualMoney(), coupon.getCoupon().getMoney());

                if (realMoney < 0) {
                    realMoney = 0;
                }
                recharge.setRealMoney(realMoney);
            }

        }
        recharge.setFee(calculateFee(recharge.getRealMoney()));
        recharge.setIsRechargedByAdmin(false);
        recharge.setTime(new Date());
        recharge.setStatus(UserConstants.RechargeStatus.WAIT_PAY);
        ht.save(recharge);
        return recharge;
    }


    @Override
    public Recharge get(String rechargeId) {
        return ht.get(Recharge.class, rechargeId);
    }

    @Override
    @Transactional
    public void update(Recharge recharge) {
        ht.update(recharge);
    }

    /**
     * 生成充值id
     *
     * @return
     */
    private String generateId() {
        return idGenerator.nextId(Recharge.class);
    }

    @Override
    public List<RechargeBankCard> getBankCardsList() {
        List<RechargeBankCard> bcs = new ArrayList<RechargeBankCard>();
       /* Iterator<Entry<Object, Object>> it = RechargeServiceImpl.props.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            bcs.add(new RechargeBankCardImpl(key, value));
        }*/

        return bcs;
    }

    @Override
    public String getBankNameByNo(final String bankNo) {
        List<RechargeBankCard> banks = getBankCardsList();
        for (RechargeBankCard bank : banks) {
            if (StringUtils.equals(bank.getNo(), bankNo)) {
                return bank.getBankName();
            }
        }
        return "Not found Bank";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeByAdmin(String rechargeId) {
        rechargePaySuccess(rechargeId, "adminNoOrder");
        Recharge r = ht.get(Recharge.class, rechargeId);
        r.setIsRechargedByAdmin(true);
        ht.update(r);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeByAdmin(UserBill userBill) {
        Recharge r = new Recharge();
        r.setActualMoney(userBill.getMoney());
        r.setCoupon(null);
        r.setFee(0D);
        r.setId(generateId());
        r.setIsRechargedByAdmin(true);
        r.setRechargeWay("admin");
        r.setStatus(RechargeStatus.SUCCESS);
        r.setCallbackTime(new Date());
        r.setTime(new Date());
        r.setUser(userBill.getUser());
        ht.save(r);
        userBillService.transferIntoBalance(userBill.getUser().getId(), userBill.getMoney(), OperatorInfo.ADMIN_OPERATION, userBill.getDetail(),userBill.getAccountType());
    }


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void rechargePaySuccess(String rechargeId, String orderNo) {
        Recharge recharge = ht.get(Recharge.class, rechargeId);
        ht.evict(recharge);
        recharge = ht.get(Recharge.class, rechargeId, LockMode.PESSIMISTIC_WRITE);
        if (recharge != null && recharge.getStatus().equals(UserConstants.RechargeStatus.WAIT_PAY)) {
            recharge.setOperationOrderNo(orderNo);
            rechargeBO.rechargeSuccess(recharge);
            log.info("{}充值成功！！！", recharge.getUser().getUsername());
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void rechargePayFail(String rechargeId, String orderNo) {
        Recharge recharge = ht.get(Recharge.class, rechargeId);
        ht.evict(recharge);
        recharge = ht.get(Recharge.class, rechargeId, LockMode.PESSIMISTIC_WRITE);
        if (recharge != null && recharge.getStatus().equals(UserConstants.RechargeStatus.WAIT_PAY)) {
            recharge.setOperationOrderNo(orderNo);
            rechargeBO.rechargeFail(recharge);
            log.info("{}充值失败！！！", recharge.getUser().getUsername());
        }
    }
}
