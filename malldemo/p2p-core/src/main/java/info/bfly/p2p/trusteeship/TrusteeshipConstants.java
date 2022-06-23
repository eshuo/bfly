package info.bfly.p2p.trusteeship;

/**
 * 资金托管 constants.
 */
public class TrusteeshipConstants {
    public static final class OperationType {
        /**
         * 收到错误签名信息
         */
        public static  final  String CHECK_SIGN_ERROR ="check_sign_error";
        /**
         * 收到回调
         */
        public static final String CALLBACK    = "callback";
        /**
         * 开户
         */
        public static final String CREATE_ACCOUNT              = "create_account";
        /**
         * 充值
         */
        public static final String RECHARGE                    = "recharge";
        /**
         * 投标
         */
        public static final String INVEST                      = "invest";
        /**
         * 转账
         */
        public static final String MONEY_TRANSFER_FROM    = "money_transfer_from";
        public static final String MONEY_TRANSFER_TO    = "money_transfer_to";
        /**
         * 流标
         */
        public static final String CANCEL_LOAN                 = "cancel_loan";

        /**
         * 退款
         */
        public static final String INVEST_REFUND = "invest_refund";
        /**
         * 单笔撤销
         */
        public static final String CANCEL_INVEST            = "cancel_invest";
        /**
         * 放款
         */
        public static final String GIVE_MOENY_TO_BORROWER = "give_moeny_to_borrower";

        /**
         * 单个放款
         */
        public static final String ONE_GIVE_MOENY_TO_BORROWER = "one_give_moeny_to_borrower";

        /**
         * 代收完成
         */
        public static final String FINISH_PRE_AUTH_TRADE       = "finish_pre_auth_trade";
        /**
         * 批代收完成
         */
        public static final String FINISH_PRE_AUTH_TRADE_BATCH = "finish_pre_auth_trade_batch";
        /**
         * 借款者还款
         */
        public static final String REPAY                       = "repay";
        /**
         * 还款到投资者账户
         */
        public static final String REPAY_BATCH                 = "repay_batch";
        /**
         * 提前还款
         */
        public static final String ADVANCE_REPAY               = "advance_repay";
        /**
         * 逾期还款
         */
        public static final String OVERDUE_REPAY               = "overdue_repay";
        /**
         * 提现
         */
        public static final String WITHDRAW_CASH               = "withdraw_cash";

        /**
         * 冻结金额
         */
        public static final String BALANCE_FREEZE = "balance_freeze";

        /**
         * 解冻金额
         */
        public static final String BALANCE_UNFREEZE = "balance_unfreeze";

        /**
         * 录入标
         */
        public static final String CREATE_LOAN = "create_loan";

        /**
         * 审核企业信息
         */
        public static final String AUDIT_MEMBER_INFOS = "audit_member_infos";
        /**
         * 重新发送
         */
        public static final String RECREATE    = "recreate";


    }

    /**
     * 操作狀態
     */
    public static final class OperationStatus {
        /**
         * 准备数据
         */
        public static final String PREPARE     = "prepare";
        /**
         * 等待发送
         */
        public static final String UN_SEND     = "un_send";
        /**
         * 已发送
         */
        public static final String SENDED      = "sended";
        /**
         * 收到响应
         */
        public static final String RECEIVED    = "received";
        /**
         * 回调等待其他回调处理完成
         */
        public static final String WAITING     = "waiting";
        /**
         * 收到回调
         */
        public static final String CALLBACK    = "callback";

        /**
         * 已操作
         */
        public static final String ALREADY = "already";
        /**
         * 通过
         */
        public static final String PASSED      = "passed";
        /**
         * 未通过
         */
        public static final String REFUSED     = "refused";
        /**
         * 无响应
         */
        public static final String NO_RESPONSE = "no_response";
        /**
         * 处理异常  需要人工干预
         */
        public static final String ERROR       = "error";

        /**
         * 验签错误
         */
        public static final String CHECK_SIGN_ERROR = "check_sign_error";
    }

    /**
     * 托管平台
     */
    public static final class Trusteeship {

        public static final String SINAPAY = "sinaPay";
    }

    /**
     * 托管状态
     */
    public static final class TrusteeshipStatus {

        public static final String ACTIVE   = "active";
        public static final String INACTIVE = "inactive";
    }

    public static final String Package = "info.bfly.p2p.trusteeship";
}
