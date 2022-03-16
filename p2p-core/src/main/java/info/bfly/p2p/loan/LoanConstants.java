package info.bfly.p2p.loan;

public class LoanConstants {
    /**
     * 申请企业借款 状态
     *
     * @author Administrator
     */
    public final static class ApplyEnterpriseLoanStatus {
        /**
         * 等待审核
         */
        public final static String WAITING_VERIFY = "waiting_verify";
        /**
         * 已审核
         */
        public final static String VERIFIED       = "verified";
    }
    
    /**
     * 
    * @ClassName: BusinessType 
    * @Description: TODO(借款类型) 
    * @author zeminshao
    * @date 2017年5月3日 上午10:24:54 
    *
     */
    public final static class BusinessType {
        /**
         * 众筹
         */
        public final static String CROWD_FUNDING_TYPE = "众筹";
        /**
         * 个人借款
         */
        public final static String PERSIONAL_LOAN_TYPE = "个人借款";
        /**
         * 企业借款
         */
        public final static String BUSINESS_LOAN_TYPE = "企业借款";

    }
    
    /**
     * 借款状态
     *
     * @author Administrator
     */
    public final static class LoanStatus {
        /**
         * 等待审核
         */
        public final static String WAITING_VERIFY             = "waiting_verify";
        /**
         * 审核后等待第三方确认
         */
        public final static String WAITING_VERIFY_AFFIRM      = "waiting_verify_affirm";
        /**
         * 审核后等待用户确认
         */
        public final static String WAITING_VERIFY_AFFIRM_USER = "waiting_verify_affirm_user";
        /**
         * 贷前公示
         */
        public final static String DQGS                       = "dqgs";
        /**
         * 审核未通过
         */
        public final static String VERIFY_FAIL                = "verify_fail";
        /**
         * 筹款中
         */
        public final static String RAISING                    = "raising";
        /**
         * 代收，等待资金确认
         */
        public final static String WAITING_RECHECK_VERIFY     = "waiting_recheck_verify";

//        /**
//         * 强制代收完成
//         */
//        public final static String FORCE_RECHECK_VERIFY = "force_recheck_verify";
        /**
         * 等待复核
         */
        public final static String RECHECK                    = "recheck";


        /**
         * 新浪放款确认
         */
        public final static String SINA_RECHECK_COMPLETE = "sina_recheck_complete";

        /**
         * 流标
         */
        public final static String CANCEL                     = "cancel";
        /**
         * 流标后等待第三方确认
         */
        public final static String WAITING_CANCEL_AFFIRM      = "waiting_cancel_affirm";
        /**
         * 还款中
         */
        public final static String REPAYING                   = "repaying";
        /**
         * 逾期
         */
        public final static String OVERDUE                    = "overdue";
        /**
         * 完成
         */
        public final static String COMPLETE                   = "complete";
        /**
         * 坏账
         */
        public final static String BAD_DEBT                   = "bad_debt";
        /**
         * 管理员删除
         */
        public final static String DEL                        = "del";

        /**
         * 新浪审核中
         */
        public final static String SINA_VERIFY = "sina_verify";


        /**
         * 满标后继续融资
         */

    }

    /**
     * 借款审核状态
     *
     * @author Administrator
     */
    public final static class LoanVerifyStatus {
        /**
         * 通过
         */
        public final static String PASSED = "通过";
        /**
         * 未通过
         */
        public final static String FAILED = "未通过";
    }

    /**
     * 还款状态
     *
     * @author Administrator
     */
    public final static class RepayStatus {
        /**
         * 还款中
         */
        public final static String REPAYING          = "repaying";
        /**
         * 等待第三方还款确认
         */
        public final static String WAIT_REPAY_VERIFY = "wait_repay_verify";
        /**
         * 第三方资金冻结成功
         */
        public final static String REPAY_FROZEN = "repay_frozen";
        /**
         * 等待第三方划入资金
         */

        public final static String WAIT_FINISH_FUNDS      = "wait_finish_funds";
        /**
         * 资金已入账
         */
        public final static String FINISH_FUNDS      = "finish_funds";

        /**
         * 等待还款到借款用户账户
         */
        public final static String WAIT_REPAY_COMPLETE = "wait_repay_complete";
        /**
         * 逾期
         */
        public final static String OVERDUE           = "overdue";
        /**
         * 完成
         */
        public final static String COMPLETE          = "complete";
        /**
         * 坏账
         */
        public final static String BAD_DEBT          = "bad_debt";


    }

    public final static class View {
        public final static String LOAN_LIST = "/admin/loan/loanList";
    }

    /**
     * Package name.
     */
    public final static String Package = "info.bfly.archer.loan";
}
