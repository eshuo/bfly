package info.bfly.pay.bean;

/**
 * Created by XXSun on 2017/1/12.
 */
public enum SinaAPI {
    CREATE_ACTIVATE_MEMBER("create_activate_member", "创建用户接口"),
    SET_REAL_NAME("set_real_name", "设置用户实名信息"),
    SET_PAY_PASSWORD("set_pay_password", "设置支付密码"),
    MODIFY_PAY_PASSWORD("modify_pay_password", "修改支付密码"),
    FIND_PAY_PASSWORD("find_pay_password", "找回支付密码"),
    QUERY_IS_SET_PAY_PASSWORD("query_is_set_pay_password", "查询是否绑定银行卡"),
    BINDING_BANK_CARD("binding_bank_card", "绑定银行卡"),
    BINDING_BANK_CARD_ADVANCE("binding_bank_card_advance", "绑定银行卡推进"),
    UNBINDING_BANK_CARD("unbinding_bank_card", "解绑银行卡"),
    UNBINDING_BANK_CARD_ADVANCE("unbinding_bank_card_advance", "解绑银行卡推进"),
    QUERY_BANK_CARD("query_bank_card", "查询银行卡"),
    CREATE_HOSTING_COLLECT_TRADE("create_hosting_collect_trade", "创建托管代收交易"),
    CREATE_SINGLE_HOSTING_PAY_TRADE("create_single_hosting_pay_trade", "创建托管代付交易"),
    CREATE_BATCH_HOSTING_PAY_TRADE("create_batch_hosting_pay_trade", "创建批量托管代付交易"),
    PAY_HOSTING_TRADE("pay_hosting_trade", "托管交易支付"),
    QUERY_PAY_RESULT("query_pay_result", "支付结果查询"),
    QUERY_HOSTING_TRADE("query_hosting_trade", "托管交易查询"),
    QUERY_HOSTING_BATCH_TRADE("query_hosting_batch_trade", "托管交易批次查询"),
    CREATE_HOSTING_REFUND("create_hosting_refund", "托管退款"),
    QUERY_HOSTING_REFUND("query_hosting_refund", "托管退款查询"),
    CREATE_HOSTING_DEPOSIT("create_hosting_deposit", "托管充值"),
    QUERY_HOSTING_DEPOSIT("query_hosting_deposit", "托管充值查询"),
    CREATE_HOSTING_WITHDRAW("create_hosting_withdraw", "托管提现"),
    QUERY_HOSTING_WITHDRAW("query_hosting_withdraw", "托管提现查询"),
    CREATE_HOSTING_TRANSFER("create_hosting_transfer", "转账接口"),
    ADVANCE_HOSTING_PAY("advance_hosting_pay", "支付推进"),
    CREATE_BID_INFO("create_bid_info", "标的录入"),
    QUERY_BID_INFO("query_bid_info", "标的信息查询"),
    CREATE_SINGLE_HOSTING_PAY_TO_CARD_TRADE("create_single_hosting_pay_to_card_trade", "创建单笔代付到提现卡交易"),
    CREATE_BATCH_HOSTING_PAY_TO_CARD_TRADE("create_batch_hosting_pay_to_card_trade", "创建批量代付到提现卡交易"),
    FINISH_PRE_AUTH_TRADE("finish_pre_auth_trade", "代收完成"),
    CANCEL_PRE_AUTH_TRADE("cancel_pre_auth_trade", "代收撤销"),
    QUERY_FUND_YIELD("query_fund_yield", "存钱罐基金收益率查询"),
    QUERY_ACCOUNT_DETAILS("query_account_details", "查询收支明细"),
    BALANCE_FREEZE("balance_freeze", "冻结余额"),
    BALANCE_UNFREEZE("balance_unfreeze", "解冻余额"),
    QUERY_CTRL_RESULT("query_ctrl_result", "查询冻结解冻结果"),
    QUERY_MEMBER_INFOS("query_member_infos", "查询企业会员信息"),
    QUERY_AUDIT_RESULT("query_audit_result", "查询企业会员审核结果"),
    AUDIT_MEMBER_INFOS("audit_member_infos", "请求审核企业会员资质"),
    SMT_FUND_AGENT_BUY("smt_fund_agent_buy", "经办人信息"),
    QUERY_FUND_AGENT_BUY("query_fund_agent_buy", "查询经办人信息"),
    SHOW_MEMBER_INFOS_SINA("show_member_infos_sina", "sina页面展示用户信息"),
    MODIFY_VERIFY_MOBILE("modify_verify_mobile", "修改认证手机"),
    FIND_VERIFY_MOBILE("find_verify_mobile", "找回 认证手机"),
    QUERY_MIDDLE_ACCOUNT("query_middle_account", "查询中间用户"),
    QUERY_BALANCE("query_balance", "查询余额/基金份额"),

    //需要新浪那边开通委托扣款
    BINDING_VERIFY("binding_verify", "绑定认证信息"),
    UNBINDING_VERIFY("unbinding_verify", "解绑认证信息"),
    QUERY_VERIFY("query_verify", "查询认证信息"),
    WEB_BINDING_BANK_CARD("web_binding_bank_card", "我的银行卡"),
    OPEN_ACCOUNT("open_account", "会员开户接口"),
    HANDLE_WITHHOLD_AUTHORITY("handle_withhold_authority", "委托扣款重定向"),
    MODIFY_WITHHOLD_AUTHORITY("modify_withhold_authority", "修改委托扣款重定向"),
    RELIEVE_WITHHOLD_AUTHORITY("relieve_withhold_authority", "解除委托扣款重定向"),
    QUERY_WITHHOLD_AUTHORITY("query_withhold_authority", "查看用户是否委托扣款"),
    SET_MERCHANT_CONFIG("set_merchant_config", "修改商户配置"),
    WEB_REAL_NAME_PIC_AUTH("web_real_name_pic_auth", "实名认证重定向"),


    INIT_MEMBER_BY_PROCESS("init_member_by_process", "综合初始化会员接口"),
    advance_hosting_pay("advance_hosting_pay", "支付推进");


    private String service_name;
    private String service_describe;

    public String getService_name() {
        return service_name;
    }


    public String getService_describe() {
        return service_describe;
    }


    /**
     * @param service_name
     * @param service_describe
     */
    SinaAPI(String service_name, String service_describe) {
        this.service_name = service_name;
        this.service_describe = service_describe;
    }
}
