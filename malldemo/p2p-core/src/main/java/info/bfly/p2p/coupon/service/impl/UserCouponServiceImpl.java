package info.bfly.p2p.coupon.service.impl;

import info.bfly.archer.user.model.User;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.model.Coupon;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.coupon.service.UserCouponService;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 */
@Service
public class UserCouponServiceImpl implements UserCouponService {
    @Resource
    private HibernateTemplate ht;


    @Override
    public UserCoupon get(String userCouponId) {
        return ht.get(UserCoupon.class, userCouponId);
    }

    @Override
    public UserCoupon get(String userCouponId, LockMode lockMode) {
        return ht.get(UserCoupon.class, userCouponId, lockMode);
    }

    @Override
    public void disable(String userCouponId) {
        // TODO:implement
        throw new RuntimeException("you must override this method!");
    }

    @Override
    public void exceedTimeLimit(String userCouponId) {
        // TODO:implement
        throw new RuntimeException("you must override this method!");
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void giveUserCoupon(String userId, String couponId, Integer periodOfValidity, String description) {
        Coupon c = ht.get(Coupon.class, couponId);
        User u = ht.get(User.class, userId);
        UserCoupon uc = new UserCoupon();
        uc.setCoupon(c);
        uc.setDeadline(DateUtil.addDay(new Date(), periodOfValidity == null ? c.getPeriodOfValidity() : periodOfValidity));
        uc.setDescription(description);
        uc.setGenerateTime(new Date());
        uc.setId(IdGenerator.randomUUID());
        uc.setStatus(CouponConstants.UserCouponStatus.UNUSED);
        uc.setUser(u);
        ht.save(uc);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createUserCoupon(UserCoupon userCoupon, Integer periodOfValidity) throws ExceedDeadlineException {
        Coupon c = ht.get(Coupon.class, userCoupon.getCoupon().getId());
        if (c.getDeadline().before(new Date()))
            throw new ExceedDeadlineException("[" + c.getName() + "]优惠券超出有效期！");
        User u = ht.get(User.class, userCoupon.getUser().getId());
        UserCoupon uc = new UserCoupon();
        uc.setCoupon(c);
        if (userCoupon.getDeadline() == null)
            uc.setDeadline(DateUtil.addDay(new Date(), periodOfValidity == null ? c.getPeriodOfValidity() : periodOfValidity));
        else
            uc.setDeadline(userCoupon.getDeadline());
        uc.setDescription(userCoupon.getDescription());
        uc.setGenerateTime(new Date());
        uc.setId(IdGenerator.randomUUID());
        uc.setStatus(CouponConstants.UserCouponStatus.UNUSED);
        uc.setUser(u);
        ht.save(uc);

    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void userUserCoupon(String userCouponId, String target) {
        UserCoupon uc = ht.get(UserCoupon.class, userCouponId);
        uc.setStatus(CouponConstants.UserCouponStatus.USED);
        uc.setTarget(target);
        uc.setUsedTime(new Date());
        ht.update(uc);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void restoreUserCoupon(String userCouponId) {
        UserCoupon uc = ht.get(UserCoupon.class, userCouponId);
        uc.setStatus(CouponConstants.UserCouponStatus.UNUSED);
        uc.setUsedTime(null);
        ht.update(uc);
    }

    @Override
    @Transactional
    public void update(UserCoupon userCoupon) {
        ht.update(userCoupon);
    }
}
