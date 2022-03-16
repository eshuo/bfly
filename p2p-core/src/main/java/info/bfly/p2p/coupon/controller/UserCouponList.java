package info.bfly.p2p.coupon.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.coupon.CouponConstants;
import info.bfly.p2p.coupon.model.Coupon;
import info.bfly.p2p.coupon.model.UserCoupon;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UserCouponList extends EntityQuery<UserCoupon> implements Serializable {
    private List<UserCoupon> userInvestCoupons;
    @Resource
    private LoginUserInfo    loginUserInfo;

    public UserCouponList() {
        final String[] RESTRICTIONS = {"userCoupon.user.id = #{userCouponList.example.user.id}", "userCoupon.coupon.type = #{userCouponList.example.coupon.type}", "userCoupon.status = #{userCouponList.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    /**
     * @param type
     * @return 根据优惠劵类型找到对应的优惠劵类型名称
     * @author hch
     */
    public String getCouponTypeName(String type) {
        return CouponConstants.UserCouponStatus.couponTypeMap.get(type);
    }

    /**
     * @param value
     * @return 为了获取字符串的长度，为了方便页面调用
     * @author hch
     */
    public int getStrLength(String value) {
        if (value != null) {
            return value.length();
        } else {
            return 0;
        }
    }

    /**
     * 通过用户id和优惠券类型获取该用户持有的优惠券
     *
     * @param userId
     * @param couponType
     * @return
     */
    private List<UserCoupon> getUserCouponsByUserIdAndCouponTypeAndCouponStatus(String userId, String couponType, String couponStatus) {
        String hql = "from UserCoupon uc where uc.user.id=? and uc.coupon.type=? and uc.status=?";
        return (List<UserCoupon>) getHt().find(hql, new String[]{userId, couponType, couponStatus});
    }

    public List<UserCoupon> getUserInvestCoupons() {
        if (userInvestCoupons == null) {
            userInvestCoupons = getUserCouponsByUserIdAndCouponTypeAndCouponStatus(loginUserInfo.getLoginUserId(), CouponConstants.Type.INVEST, CouponConstants.UserCouponStatus.UNUSED);
        }
        return userInvestCoupons;
    }

    /**
     * hch
     *
     * @param user_id
     * @return 获取该用户的优惠劵总金额
     */
    public double getUserSumMoney(String user_id) {
        String hql = "select sum(money) from Coupon where id in(select coupon.id from UserCoupon where user.id=?)";
        Double value = (Double) getHt().find(hql, new Object[]{user_id}).get(0);
        if (value == null) {
            value = (double) 0;
        }
        return value;
    }

    @Override
    protected void initExample() {
        UserCoupon uc = new UserCoupon();
        uc.setUser(new User());
        uc.setCoupon(new Coupon());
        setExample(uc);
    }
}
