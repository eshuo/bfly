package info.bfly.p2p.message;

public class MessageConstants {
    public final static class InBoxConstants {
        public final static String NOREAD = "0";
        public final static String ISREAD = "1";
    }

    /**
     * UserMessageNode Id 信息发送节点ID
     *
     * @author Administrator
     */
    public final static class UserMessageNodeId {
        /**
         * 注册成功激活
         */
        public final static String REGISTER_ACTIVE               = "register_active";
        /**
         * 通过邮箱找回登录密码
         */
        public final static String FIND_LOGIN_PASSWORD_BY_EMAIL  = "find_login_password_by_email";
        /**
         * 通过手机找回登录密码
         */
        public final static String FIND_LOGIN_PASSWORD_BY_MOBILE = "find_login_password_by_mobile";
        /**
         * 修改绑定邮箱
         */
        public static final String CHANGE_BINDING_EMAIL          = "change_binding_email";
        /**
         * 绑定邮箱
         */
        public static final String BINDING_EMAIL                 = "binding_email";
        /**
         * 绑定手机号
         */
        public static final String BINDING_MOBILE_NUMBER         = "binding_mobile_number";
        /**
         * 修改绑定手机号
         */
        public static final String CHANGE_BINDING_MOBILE_NUMBER  = "change_binding_mobile_number";
        /**
         * 通过手机号注册
         */
        public static final String REGISTER_BY_MOBILE_NUMBER     = "register_by_mobile_number";
        /**
         * 投资
         */
        public static final String INVEST                        = "invest";
        /**
         * 投资确认
         */
        public static final String INVEST_CONFIRM                = "invest_CONFIRM";
        /**
         * 投资成功
         */
        public static final String INVEST_SUCCESS                = "invest_success";
        /**
         * 充值成功
         */
        public static final String RECHARGE_SUCCESS              = "recharge_success";
        /**
         * 好友币充值成功
         */
        public static final String HAOYOUBI_RECHARGE_SUCCESS     = "haoyoubi_recharge_success";
        /**
         * 审核通过
         */
        public static final String LOAN_VERIFY_SUCCESS           = "loan_verify_success";
        /**
         * 审核不通过
         */
        public static final String LOAN_VERIFY_FAIL              = "loan_verify_fail";
        /**
         * 还款提醒
         */
        public static final String REPAY_ALERT                   = "repay_alert";
        /**
         * 提现确认
         */
        public static final String WITHDRAW_CONFIRM              = "withdraw_confirm";
        /**
         * 提现成功
         */
        public static final String WITHDRAW_SUCCESS              = "withdraw_success";
        /**
         * 投资人满标提醒
         */
        public static final String INVEST_FULL_REMIND            = "invest_full_remind";
        /**
         * 投资人回款提醒
         */
        public static final String INVEST_REPAY_REMIND           = "invest_repay_remind";
        /**
         * 投资人标的延期提醒
         */
        public static final String INVEST_DELAY_REMIND           = "invest_delay_remind";
        /**
         * 投资人流标提醒
         */
        public static final String INVEST_FLOW_REMIND            = "invest_flow_remind";
        /**
         * 借款人还款提醒
         */
        public static final String BORROWER_REPAY_REMIND         = "borrower_repay_remind";
        /**
         * 借款人收款提醒
         */
        public static final String BORROWER_RECEIVE_REMIND       = "borrower_receive_remind";

        public static final String UPDATE_PASSWORD_BY_MOBILE_NUMBER = "update_password_by_mobile_number";
    }

    /**
     * UserMessageNodeStatus status
     *
     * @author Administrator
     */
    public final static class UserMessageNodeStatus {
        public final static String OPEN   = "开启";
        public final static String CLOSED = "关闭";
    }

    /**
     * UserMessageTemplate status
     *
     * @author Administrator
     */
    public final static class UserMessageTemplateStatus {
        public final static String OPEN     = "可选";
        public final static String REQURIED = "必须";
        public final static String CLOSED   = "关闭";
    }

    /**
     * UserMessageWay Id
     *
     * @author Administrator
     */
    public final static class UserMessageWayId {
        /**
         * 邮件
         */
        public final static String EMAIL  = "email";
        /**
         * 站内信
         */
        public final static String LETTER = "letter";
        /**
         * 短信
         */
        public final static String SMS    = "sms";
    }

    /**
     * UserMessageWayStatus status
     *
     * @author Administrator
     */
    public final static class UserMessageWayStatus {
        public final static String OPEN   = "开启";
        public final static String CLOSED = "关闭";
    }
}
