package info.bfly.p2p.coupon;

import java.util.HashMap;
import java.util.Map;

public class CouponConstants {
    /**
     * 代金券状态
     *
     * @author Administrator
     */
    public static class CouponStatus {
        /**
         * 可用
         */
        public static final String ENABLE  = "enable";
        /**
         * 不可用
         */
        public static final String DISABLE = "disable";
    }

    /**
     * 代金券类型
     *
     * @author Administrator
     */
    public static class Type {
        /**
         * 充值优惠券
         */
        public static final String RECHARGE   = "recharge";
        /**
         * 投资优惠券
         */
        public static final String INVEST     = "invest";
        /**
         * 还款优惠券
         */
        public static final String REPAY_LOAN = "repay_loan";

    }

    /**
     * 用户代金券状态
     *
     * @author Administrator
     */
    public static class UserCouponStatus {
        /**
         * 未使用
         */
        public static final String UNUSED  = "unused";
        /**
         * 已使用
         */
        public static final String USED    = "used";
        /**
         * 系统处理中
         */
        public static final String PROCESS = "process";
        /**
         * 处理成功
         */
        public static final String SUCCESS = "success";
        /**
         * 处理失败
         */
        public static final String FAIL    = "fail";
        /**
         * 不可用
         */
        public static final String DISABLE = "disable";
        // hch start
        /**
         * 根据类型，查找对应的名称
         */
        @Deprecated
        public static Map<String, String> couponTypeMap;       // 类型map集合,key:对应值；value:对应值

        static {
            if (UserCouponStatus.couponTypeMap == null || UserCouponStatus.couponTypeMap.size() == 0) {
                UserCouponStatus.couponTypeMap = new HashMap<String, String>();
                UserCouponStatus.couponTypeMap.put(UserCouponStatus.UNUSED, "未使用");
                UserCouponStatus.couponTypeMap.put(UserCouponStatus.USED, "已使用");
                UserCouponStatus.couponTypeMap.put(UserCouponStatus.DISABLE, "不可用");
            }
        }
        // hch end
    }


    public static class View {

        public final static String COUPON_VIEW_DIR = "/admin/user";


        public static final String COUPON_LIST = COUPON_VIEW_DIR + "/couponList";

        public static final String USER_COUPON_LIST = COUPON_VIEW_DIR + "/userCouponList";


    }
}
