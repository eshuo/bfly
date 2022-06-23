package info.bfly.p2p.coupon.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.coupon.model.Coupon;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
@Component
@Scope(ScopeType.VIEW)
public class CouponList extends EntityQuery<Coupon> {


    CouponList(){
        final String[] RESTRICTIONS = { "coupon.type like #{couponList.example.type}","coupon.name like #{couponList.example.name}","coupon.status like #{couponList.example.status}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }






}
