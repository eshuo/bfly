package info.bfly.p2p.coupon.service;

import info.bfly.p2p.coupon.model.Coupon;

/**
 * Description: 优惠券service
 */
public interface CouponService {


    /**
     * 根据优惠券属性生成ID并保存返回
     *
     * @param coupon
     * @return
     */
    Coupon createInstance(Coupon coupon);






}
