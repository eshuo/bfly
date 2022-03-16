package info.bfly.app.protocol.service;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.coupon.model.UserCoupon;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/4/18 0018.
 */
@Scope(ScopeType.REQUEST)
@Service
public class ApiUserCouponListService extends EntityQuery<UserCoupon> {

    public ApiUserCouponListService() {
        final String[] RESTRICTIONS = {"userCoupon.user.id like #{apiUserCouponListService.example.user.id}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
