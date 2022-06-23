package info.bfly.archer.user;

import java.util.HashMap;
import java.util.Map;

public class UserConstants {
    public final static class AuthenticationManager {
        /**
         * 登录失败次数
         */
        public static final String LOGIN_FAIL_TIME = "login_fail_time";
        /**
         * 是否需要验证码
         */
        public static final String NEED_VALIDATE_CODE = "need_validate_code";
    }

    // public static final String SESSION_KEY_LOGIN_USER = "login_user";
    public final static class ClientIds {
        public final static String VALIDATECODECLIENTID = "user-register-form:inputvalidatecode";
        public final static String DYNAMICCODECLIENTID = "user-register-form2:dynamicCode";
        public final static String MOBILENUMCLIENTID = "user-register-form2:mobileNum";
        public final static String USERNAMECLIENTID = "user-register-form:username";
        public final static String EMAILCLIENTID = "user-register-form:email";
    }

    public final static class Errormsg {
        public final static String ERRORPHONE = "手机号码不正确！";
        public final static String USERVALIDATECODEERRORMSG = "验证码错误！请重新输入";
        public final static String USERMOBILERANDCODEERRORMGS = "验证码输入错误,注册失败！";
        public final static String PASSANDCASHPASSSAME = "提现密码和登录密码不能相同！";
        public final static String CANONTCHINESETESUFUHAO = "用户名不能含有中文或特殊符号！";
    }

    public final static class LoginIdType {
        public final static String USERNAME_TYPE = "input.username";
        public final static String EMAIL_TYPE = "input.email";
        public final static String MOBILE_TYPE = "input.mobile";
    }

    /**
     * 账户类型
     */
    public final static class AccountType{

        /**
         * 基本账户
         */
        public static final String  BASIC= "BASIC";
        /**
         * 奖励账户
         */
        public static final String  REWARD= "REWARD";
    }
    /**
     * 充值状态
     *
     * @author Administrator
     */
    public final static class RechargeStatus {

        /**
         * 等待付款
         */
        public static final String WAIT_PAY = "wait_pay";
        /**
         * 等待优惠券处理
         */
        public static final String WAIT_COUPON= "wait_coupon";
        /**
         * 充值成功
         */
        public static final String SUCCESS = "success";
        /**
         * 充值失败
         */
        public static final String FAIL = "fail";
    }
    /**
     * 转账状态
     *
     * @author Administrator
     */
    public final static class MoneyTransferStatus {
        /**
         * 初始化
         */
        public static final String INIT = "init";
        /**
         * 等待付款
         */
        public static final String WAIT_PAY = "wait_pay";
        /**
         * 第三方资金确认中
         */
        public static final String FROM_PROCESS ="from_process";
        /**
         * 入账成功等待确认是否同意转账
         */
        public static final String WAIT_RECHECK ="wait_recheck";

        /**
         * 第三方资金确认中
         */
        public static final String TO_PROCESS ="to_process";
        /**
         * 转账成功
         */
        public static final String SUCCESS = "success";

        /**
         * 转账拒绝
         */
        public static final String REFUSE = "refuse";

        /**
         * 转账失败
         */
        public static final String FAIL = "fail";
        /**
         * 转账异常
         */
        public static final String ERROR = "error";
    }
    /**
     * 转账类型
     *
     * @author XXSun
     */
    public final static class MoneyTransferType {
        /**
         * 优惠券
         */
        public static final String USERCOUPON = "usercoupon";
        /**
         * 系统
         */
        public static final String SYSTEM = "system";
        /**
         * 个人
         */
        public static final String PERSONAL = "personal";
    }

    public final static class UserLoginLog {
        public final static String SUCCESS = "1";
        public final static String FAIL = "0";
    }

    /**
     * 用户积分操作类型（增加还是扣除积分）
     *
     * @author
     */
    public final static class UserPointOperateType {
        /**
         * 增加积分
         */
        public static final String ADD = "add";
        /**
         * 扣除积分
         */
        public static final String MINUS = "minus";
    }

    /**
     * 用户积分类型
     */
    public final static class UserPointType {
        /**
         * 升级积分
         */
        public static final String LEVEL = "level";
        /**
         * 消费积分
         */
        public static final String COST = "cost";
        // hch start
        /**
         * 根据对应的类型找到对应的名称
         */
        public static Map<String, String> historyTypeMap; // 类型map集合,key:对应值；value:对应值

        static {
            if (UserPointType.historyTypeMap == null || UserPointType.historyTypeMap.size() == 0) {
                UserPointType.historyTypeMap = new HashMap<String, String>();
                UserPointType.historyTypeMap.put(UserPointType.COST, "消费积分");
                UserPointType.historyTypeMap.put(UserPointType.LEVEL, "升级积分");
            }
        }
        // hch end
    }

    public final static class UserStatus {
        public final static String ENABLE = "1";
        public final static String DISABLE = "0";
        public final static String NOACTIVE = "2";
    }

    public final static class UserType{
        public final static String PERSONAL ="1";
        public final static String BUSINESS ="2";
        public final static String AGENT ="agent";
    }
    public final static class UservalidateCodeType {
        public final static String EMAICODE_TYPE = "0";
        public final static String PHONECODE_TYPE = "1";
        // 普通手机验证码类型
        public final static String MOBILEPHONE = "M";
        // 绑定手机验证码类型
        public final static String MOBILEPHONEBINDING = "MB";
    }

    public static class View {
        public final static String POINT_VIEW_DIR = "/admin/user";
        /**
         * 积分列表页面
         */
        public static final String POINT_LIST = View.POINT_VIEW_DIR + "/userPointList";
        /**
         * 积分历史列表页
         */
        public static final String POINT_HISTORY_LIST = View.POINT_VIEW_DIR + "/userPointHistoryList";
    }

    /**
     * 提现状态
     *
     * @author Administrator
     */
    public final static class WithdrawStatus {

        /**
         * 初始化
         */
        public static final String INIT = "init";
        /**
         * 等待冻结余额
         */
        public static final String WAIT_FREEZE = "wait_freeze";

        /**
         * 冻结余额成功
         */
        public static final String FREEZE_SUCCESS = "freeze_success";
        /**
         * 冻结余额失败
         */
        public static final String FREEZE_FAIL = "freeze_fail";
        /**
         * 等待审核
         */
        public static final String WAIT_VERIFY = "wait_verify";
        /**
         * 等待复核
         */
        public final static String RECHECK = "recheck";
        /**
         * 等待确认
         */
        public final static String CONFIRM = "confirm";

        /**
         * 等待解冻余额
         */
        public static final String WAIT_UNFREEZE= "wait_unfreeze";
        /**
         * 新浪解冻成功，等待用户提现
         */
        public final static String UNFREEZE_SUCCESS= "unfreeze_success";
        /**
         * 新浪解冻失败，等待管理员处理
         */
        public final static String UNFREEZE_FAIL= "unfreeze_fail";

        /**
         * 提现处理中
         */
        public static final String PROCESS = "process";
        /**
         * 提现成功
         */
        public static final String SUCCESS = "success";

        /**
         * 提现失败
         */
        public static final String FAIL = "fail";
        /**
         * 审核未通过
         */
        public static final String VERIFY_FAIL = "verify_fail";
    }


    /**
     * 审核类型
     */
    public final static class VerifyType{

        public static String InvestorPersonalInfo = "InvestorPersonalInfo";
        public static String BorrowerBusinessInfo = "BorrowerBusinessInfo";
        public static String BorrowerBusinessAgentInfo = "BorrowerBusinessAgentInfo";
        public static String CallBack = "callback";
        public static String MoneyTransfer = "MoneyTransfer";
    }
    /**
     * 审核状态
     */
    public final static class VerifyStatus{
        /**
         * 通过
         */
        public static final String SUCCESS     = "success";
        /**
         * 未通过
         */
        public static final String FAIL = "fail";
    }
    /**
     * Package name. info.bfly.archer.user .
     */
    public static String Package = "info.bfly.archer.user";
    public static final String SESSION_REGISTER_MOBILE_VALIDATE_CODE = "mobile_validate";
}
