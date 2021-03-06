package info.bfly.p2p.schedule;

/**
 * Schedule constants.
 *
 */
public class ScheduleConstants {
    /**
     * job group
     */
    public static class JobGroup {
        /**
         * 检查项目是否到预计执行时间
         */
        public static final String CHECK_LOAN_OVER_EXPECT_TIME            = "check_loan_over_expect_time";
        /**
         * 自动投标
         */
        public static final String AUTO_INVEST_AFTER_LOAN_PASSED          = "auto_invest_after_loan_passed";
        /**
         * 检查债权转让是否到期
         */
        public static final String CHECK_INVEST_TRANSFER_OVER_EXPECT_TIME = "check_invest_transfer_over_expect_time";
        /**
         * 项目到期自动还款，自动改项目、投资 状态，等等。
         */
        public static final String AUTO_REPAYMENT                         = "auto_repayment";
        /**
         * 第三方资金托管，主动查询
         */
        public static final String REFRESH_TRUSTEESHIP_OPERATION          = "refresh_trusteeship_operation";
        /**
         * 百度ping
         */
        public static final String BAIDU_PING                             = "baidu_ping";
        /**
         * 新浪回调
         */
        public static final String  TRUSTEESHIP_OPE_RATION_GROUP                 = "trusteeship_ope_ration_group";
    }

    /**
     * job name
     */
    public static class JobName {
        /**
         * 项目到期自动还款，自动改项目、投资 状态，等等。
         */
        public static final String AUTO_REPAYMENT                = "auto_repayment";
        /**
         * 第三方资金托管，主动查询
         */
        public static final String REFRESH_TRUSTEESHIP_OPERATION = "refresh_trusteeship_operation";
        /**
         * 百度ping
         */
        public static final String BAIDU_PING                    = "baidu_ping";

        /**
         * 新浪回调
         */
        public static final String TRUSTEESHIP_OPE_RATION_JOB_NAME                 = "trusteeship_ope_ration_job_name";

    }

    /**
     * trigger group
     */
    public static class TriggerGroup {
        /**
         * 检查项目是否到预计执行时间
         */
        public static final String CHECK_LOAN_OVER_EXPECT_TIME            = "check_loan_over_expect_time";
        /**
         * 自动投标
         */
        public static final String AUTO_INVEST_AFTER_LOAN_PASSED          = "auto_invest_after_loan_passed";
        /**
         * 检查债权转让是否到期
         */
        public static final String CHECK_INVEST_TRANSFER_OVER_EXPECT_TIME = "check_invest_transfer_over_expect_time";
        /**
         * 项目到期自动还款，自动改项目、投资 状态，等等。
         */
        public static final String AUTO_REPAYMENT                         = "auto_repayment";
        /**
         * 第三方资金托管，主动查询
         */
        public static final String REFRESH_TRUSTEESHIP_OPERATION          = "refresh_trusteeship_operation";
        /**
         * 还款提醒
         */
        public static final String REPAY_ALERT                            = "repay_alert";
        /**
         * 百度ping
         */
        public static final String BAIDU_PING                             = "baidu_ping";

        /**
         * 新浪回调
         */
        public static final String TRUSTEESHIP_OPE_RATION_JOB_TRIGGERGROUP                 = "trusteeship_ope_ration_job_triggergroup";
    }

    /**
     * trigger name
     */
    public static class TriggerName {
        /**
         * 项目到期自动还款，自动改项目、投资 状态，等等。
         */
        public static final String AUTO_REPAYMENT                = "auto_repayment";
        /**
         * 第三方资金托管，主动查询
         */
        public static final String REFRESH_TRUSTEESHIP_OPERATION = "refresh_trusteeship_operation";
        /**
         * 还款提醒
         */
        public static final String REPAY_ALERT                   = "repay_alert";
        /**
         * 百度ping
         */
        public static final String BAIDU_PING                    = "baidu_ping";
        /**
         * 新浪回调
         */
        public static final String TRUSTEESHIP_OPE_RATION_JOB_TRIGGERNAME                 = "trusteeship_ope_ration_job_triggername";
    }

    public static final String Package = "info.bfly.p2p.schedule";
}
