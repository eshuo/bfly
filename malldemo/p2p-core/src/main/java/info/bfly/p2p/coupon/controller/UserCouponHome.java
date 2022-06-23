package info.bfly.p2p.coupon.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.exception.ExceedDeadlineException;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.coupon.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
@Component
@Scope(ScopeType.VIEW)
public class UserCouponHome extends EntityHome<UserCoupon> {


    @Autowired
    UserCouponService userCouponService;


    public String save() {


        UserCoupon instance = getInstance();

//        userCouponService.giveUserCoupon(instance.getUser().getId(), instance.getCoupon().getId(), null, instance.getDescription());

        try {
            userCouponService.createUserCoupon(instance, instance.getCoupon().getPeriodOfValidity());
            FacesUtil.addInfoMessage("用户增加优惠券成功！");
        } catch (ExceedDeadlineException e) {
            FacesUtil.addInfoMessage("用户增加优惠券失败！" + e.getMessage());
        }
        return FacesUtil.redirect(CouponConstants.View.USER_COUPON_LIST);
    }


}
