package info.bfly.pay.bean.order;

/**
 * Created by Administrator on 2017/2/28 0028.
 */
public class OrderSinaAPI {


    /**
     * 标的类型
     */
    public static class TargetType {

        /**
         * 信用
         */
        public static final String CREDIT = "CREDIT";

        /**
         * 抵押
         */
        public static final String MORTGAGE = "MORTGAGE";

        /**
         * 债权转让
         */
        public static final String ASSIGNMENT_DEBT = "ASSIGNMENT_DEBT";

        /**
         * 其他
         */
        public static final String OTHER = "OTHER";


    }


    /**
     * 还款方式
     */
    public static class RepaymentType {

        /**
         * 一次还本付息
         */
        public static final String REPAY_CAPITAL_WITH_INTEREST = "REPAY_CAPITAL_WITH_INTEREST";
        /**
         * 等额本金
         */
        public static final String AVERAGE_CAPITAL = "AVERAGE_CAPITAL";
        /**
         * 等额本息
         */
        public static final String AVERAGE_CAPITAL_PLUS_INTERES = "AVERAGE_CAPITAL_PLUS_INTERES";
        /**
         * 按期付息到期还本
         */
        public static final String SCHEDULED_INTEREST_PAYMENTS_DUE = "SCHEDULED_INTEREST_PAYMENTS_DUE";
        /**
         * 其他
         */
        public static final String OTHER = "OTHER";

    }


    /**
     * 协议类型
     */
    public static class ProtocolType {

        /**
         * 债权转让
         */
        public static final String ASSIGNMENT_DEBT = "ASSIGNMENT_DEBT";
        /**
         * 借款协议
         */
        public static final String LOAN_AGREEMENT = "LOAN_AGREEMENT";
        /**
         * 其他
         */
        public static final String OTHER = "OTHER";


    }

    /**
     * 标的产品类型
     */

    public static class BidProductType {

        /**
         * 房贷类
         */
        public static final String HOUSING_LOAN = "HOUSING_LOAN";
        /**
         * 车贷类
         */
        public static final String CAR_LOAN = "CAR_LOAN";
        /**
         * 收益权转让类
         */
        public static final String ASSIGNMENT_DEBT = "ASSIGNMENT_DEBT";
        /**
         * 信用贷款类
         */
        public static final String CREDIT_LOAN = "CREDIT_LOAN";
        /**
         * 股票配资类
         */
        public static final String STOCK_ALLOCATION = "STOCK_ALLOCATION";
        /**
         * 银行承兑汇票
         */
        public static final String BANK_ACCEPTANCE = "BANK_ACCEPTANCE";
        /**
         * 商业承兑汇票
         */
        public static final String COMMERCIAL_ACCEPTANCE = "COMMERCIAL_ACCEPTANCE";
        /**
         * 消费贷款类
         */
        public static final String CONSUMER_LOANS = "CONSUMER_LOANS";
        /**
         * 供应链类
         */
        public static final String SUPPLY_CHAIN_LOAN = "SUPPLY_CHAIN_LOAN";

        /**
         * 过桥贷类
         */
        public static final String BRIDGE_LOAN = "BRIDGE_LOAN";

        /**
         * 融资租赁类
         */
        public static final String FINANCE_LEASE = "FINANCE_LEASE";

        /**
         * 其他
         */
        public static final String OTHER = "OTHER";


    }

    /**
     * 标的状态
     */
    public static class BidType{

        /**
         * 初始
         */
        public static final String INIT ="INIT";

        /**
         * 审核中
         */
        public static final String AUDITING ="AUDITING";

        /**
         * 审核拒绝(商户通知)
         */
        public static final String REJECT ="REJECT";

        /**
         * 有效(商户通知)
         */
        public static final String VALID ="VALID";

        /**
         * 无效(商户通知)
         */
        public static final String INVALID ="INVALID";






    }



}
