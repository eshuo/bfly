package info.bfly.p2p.coupon.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.model.Coupon;
import info.bfly.p2p.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
@Component
@Scope(ScopeType.VIEW)
public class CouponHome extends EntityHome<Coupon> {

    @Autowired
    CouponService couponService;


    /**
     * 增加优惠券
     *
     * @return
     */
    public String save() {

        couponService.createInstance(getInstance());

        FacesUtil.addInfoMessage("新增优惠券成功！");

        return FacesUtil.redirect(CouponConstants.View.COUPON_LIST);
    }


}
