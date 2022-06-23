package info.bfly.p2p.invest;

public class InvestConstants {
    /**
     * 自动投标
     *
     * @author Administrator
     */
    public final static class AutoInvest {
        /**
         * 状态
         *
         * @author Administrator
         */
        public final static class Status {
            /**
             * 开启
             */
            public final static String ON = "on";
            /**
             * 关闭
             */
            public final static String OFF = "off";
        }
    }

    /**
     * 投资状态
     *
     * @author Administrator
     */
    public final static class InvestStatus {
        /**
         * 竞标中
         */
        // public final static String BIDDING = "竞标中";
        /**
         * 第三方资金冻结确认中
         */
        public final static String WAIT_AFFIRM = "wait_affirm";
        /**
         * 第三方资金冻结成功
         */
        public final static String BID_FROZEN = "bid_frozen";
        /**
         * 第三方资金托管退款确认中
         */
        public final static String WAIT_CANCEL_AFFIRM = "wait_cancel_affirm";
        /**
         * 完成代收等待第三方确认
         */
        public final static String WAIT_LOANING_VERIFY = "wait_loaning_verify";
        /**
         * 投标成功
         */
        public final static String BID_SUCCESS = "bid_success";

        /**
         * 单笔放款等待新浪回复
         */
        public final static String ONE_SINAPAY_WAIT = "one_sinapay_wait";


        /**
         * 流标
         */
        public final static String CANCEL = "cancel";
        /**
         * 还款中
         */
        public final static String REPAYING = "repaying";
        /**
         * 债权转让
         */
        // public final static String TRANSFER = "债权转让";
        /**
         * 逾期
         */
        public final static String OVERDUE = "overdue";
        /**
         * 完成
         */
        public final static String COMPLETE = "complete";
        /**
         * 坏账
         */
        public final static String BAD_DEBT = "bad_debt";
        /**
         * 申请退款
         */
        public final static String APP_REFUND = "app_refund";
        /**
         * 退款成功
         */
        public final static String REFUND = "refund";
    }

    /**
     * 投资类型
     *
     * @author Administrator
     */
    public final static class InvestType {
        /* 公司代投 */
        public static final String COMPANYINVEST = "companyInvest";
        /* 个人投资 */
        public static final String PERSONINVEST = "personInvest";
        // 好有钱帐号
        public static final String GOODACCOUNT = "goodAccount";
        // 环迅帐号
        public static final String IPSACCOUNT = "ipsAccount";
        /**
         * 无
         */
        // public final static String NONE = "无";
        /**
         * 本息保障
         */
        // public final static String PRINCIPAL_PROTECTION = "本息保障";
        // /**
        // * 本息保障
        // */
        // public final static String PRINCIPAL_INTEREST_PROTECTION = "本息保障";
    }

    /**
     * 债权转让状态
     *
     * @author Administrator
     */
    public final static class TransferStatus {
        /**
         * 转让中
         */
        public final static String TRANSFERING = "转让中";
        /**
         * 待确认(资金托管，发送购买请求至确认之间的状态)
         */
        public final static String WAITCONFIRM = "待确认";
        /**
         * 转让成功
         */
        public final static String TRANSFED = "转让成功";
        /**
         * 流标
         */
        public final static String CANCEL = "流标";
    }


    /**
     * 退款状态
     */
    public final static class InvestRefundsStatus {

        /**
         * 退款申请
         */
        public final static String REFUND_APPLY = "refund_apply";

        /**
         * 退款处理中
         */
        public final static String REFUND_HANDLE = "refund_handle";

        /**
         * 退款处理成功
         */
        public final static String REFUND_SUCCESS = "refund_success";

        /**
         * 退款处理失败
         */
        public final static String REFUND_FAIL = "refund_fail";
    }

    public final static class View {
        // public final static String INVESTMENT_LIST = "/admin/link/linkList";
    }

    /**
     * Package name.
     */
    public final static String Package = "info.bfly.archer.investment";
}
