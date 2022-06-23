package info.bfly.archer.system;

/**
 * System constants.
 * 
 * @author wanghm
 *
 */
public class SystemConstants {
    /**
     * 动作追踪
     *
     * @author wangzhi
     */
    public static final class MotionTrackingConstants {
        public final static class ActionType {
            /**
             * 注册
             */
            public final static String REGISTER = "register";
        }

        /**
         * 来自类型
         */
        public final static class FromType {
            /**
             * 推荐人
             */
            public final static String REFERRER = "referee";
        }

        /**
         * 产生动作者类型
         *
         * @author wangzhi
         */
        public final static class WhoType {
            /**
             * 用户
             */
            public final static String USER = "user";
        }
    }

    /**
     * Package name.
     */
    public final static String Package     = "info.bfly.archer.system";
    public final static String UPLOAD_PATH = "upload";
}
