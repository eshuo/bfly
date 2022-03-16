package info.bfly.archer.common;

public class CommonConstants {
    /**
     * 认证信息状态
     *
     * @author Administrator
     */
    public static final class AuthInfoStatus {
        /**
         * 未激活
         */
        public static final String INACTIVE = "inactive";
        /**
         * 已激活
         */
        public static final String ACTIVATED = "activated";
    }

    /**
     * 认证信息类型
     *
     * @author Administrator
     */
    public static final class AuthInfoType {
        /**
         * 通过邮箱激活用户
         */
        public static final String ACTIVATE_USER_BY_EMAIL = "activate_user_by_email";
        /**
         * 通过邮箱找回密码
         */
        public static final String FIND_LOGIN_PASSWORD_BY_EMAIL = "find_login_password_by_email";
        /**
         * 通过手机找回密码
         */
        public static final String FIND_LOGIN_PASSWORD_BY_MOBILE = "find_login_password_by_mobile";

        /**
         * 手机验证码校验code
         */
        public static final String LOGIN_PASSWORD_BY_MOBILE_AUTHCODE = "login_password_by_mobile_authcode";

        /**
         * 修改绑定邮箱
         */
        public static final String CHANGE_BINDING_EMAIL = "change_binding_email";
        /**
         * 绑定邮箱
         */
        public static final String BINDING_EMAIL = "binding_email";
        /**
         * 绑定手机号
         */
        public static final String BINDING_MOBILE_NUMBER = "binding_mobile_number";
        /**
         * 充值RECHARG
         */
        public static final String  RECHARGE= "recharge";
        /**
         * 绑定sina银行卡
         */
        public static final String BINDING_BANK_CARD = "binding_bank_card";
        /**
         * 解绑sina银行卡
         */
        public static final String UNBINDING_BANK_CARD = "unbinding_bank_card";
        /**
         * 修改绑定手机号
         */
        public static final String CHANGE_BINDING_MOBILE_NUMBER = "change_binding_mobile_number";
        /**
         * 通过手机号注册
         */
        public static final String REGISTER_BY_MOBILE_NUMBER = "register_by_mobile_number";
        /**
         * 通过手机号修改密码
         */
        public static final String UPDATE_PASSWORD_BY_MOBILE_NUMBER = "update_password_by_mobile_number";
    }

    /**
     * 验证码标识
     *
     * @author Administrator
     */
    public static final class CaptchaFlag {
        /**
         * session中保存的验证码
         */
        public static final String CAPTCHA_SESSION = "captcha_session";
        /**
         * 输入的验证码
         */
        public static final String CAPTCHA_INPUT = "captcha_input";
    }

    public static final class AuthSinaPayType {

        /**
         * 主站名称
         */
        public static final String MAIN_NAME = "https://www.demo.com";

        /**
         * 操作者
         */
        public static final String OPERATOR_NAME = "sina_Pay";

        /**
         * 验证目标
         */
        public static final String TARGET = "api_verification";

    }

    /**
     * Package name. info.bfly.archer.common .
     */
    public static final String Package = "info.bfly.archer.common";
    /**
     * 一般是指一个对象或者一条数据的状态是可用的（1）
     */
    public static final String ENABLE = "1";
    /**
     * 一般是指一个对象或者一条数据的状态不可用 （0）
     */
    public static final String DISABLE = "0";
    /**
     * 用于验证保存数据时，是否有已经存在有相同的ID
     */
    public static final String VALIDATEID = "validateId";
    /**
     * 默认发送间隔 60s
     */
    public static final String DEFAULT_TIMEOUT = "60";
    /**
     * 默认验证次数
     */
    public static final String DEFAULT_VERIFY_TIMES = "3";
    /**
     * 默认验证有效时间30minComm
     */
    public static final String DEFAULT_DEADTIME = "30";
}
