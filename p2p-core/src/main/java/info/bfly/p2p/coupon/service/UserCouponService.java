package info.bfly.p2p.coupon.service;

import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.model.UserCoupon;
import org.hibernate.LockMode;

/**
 * Description: 优惠券servcie
 */
public interface UserCouponService {
    /**
     * 获取优惠券
     * @param userCouponId 优惠券ID
     * @return 用户优惠券
     */
    UserCoupon get(String userCouponId);
    /**
     * 获取优惠券
     * @param userCouponId 优惠券ID
     * @return 用户优惠券
     */
    UserCoupon get(String userCouponId, LockMode lockMode);

    /**
     * 优惠券失效
     *
     * @param userCouponId 用户优惠券id
     */
    void disable(String userCouponId);

    /**
     * 优惠券过期
     *
     * @param userCouponId 用户优惠券id
     */
    void exceedTimeLimit(String userCouponId);

    /**
     * 给用户发放优惠券
     *
     * @param userId           用户id
     * @param couponId         优惠券id
     * @param periodOfValidity 有效期（秒），如果为空，则为优惠券本身有效期
     */
    void giveUserCoupon(String userId, String couponId, Integer periodOfValidity, String description);


    /**
     * 创建用户优惠券
     * @param userCoupon
     * @param periodOfValidity
     */
    void createUserCoupon(UserCoupon userCoupon,Integer periodOfValidity) throws ExceedDeadlineException;

    /**
     * 使用优惠券
     *
     * @param userCouponId
     *            用户优惠券id
     */
    void userUserCoupon(String userCouponId,String target);


    /**
     * 恢复优惠券
     * @param userCouponId
     */
    void restoreUserCoupon(String userCouponId);

    /**
     * 更新
     * @param userCoupon
     */
    void update(UserCoupon userCoupon);
}
