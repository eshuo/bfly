package info.bfly.pay.bean.enums;

/**
 * 异步通知类型
 * Created by XXSun on 2017/2/17.
 */
public enum NOTIFY_TYPE {
    trade_status_sync("trade_status_sync", "交易结果通知"),
    refund_status_sync("refund_status_sync", "交易退款结果通知"),
    deposit_status_sync("deposit_status_sync", "充值结果通知"),
    withdraw_status_sync("withdraw_status_sync", "出款结果通知"),
    batch_trade_status_sync("batch_trade_status_sync", "批量交易结果通知"),
    audit_status_sync("audit_status_sync", "审核结果通知"),
    bid_status_sync("bid_status_sync", "标的状态通知"),
    mig_set_pay_password("mig_set_pay_password","设置支付密码（会员信息综合通知）"),
    mig_binding_card("mig_binding_card","绑定银行卡（会员信息综合通知）"),
    mig_change_card("mig_change_card","换绑银行卡（会员信息综合通知）"),
    mig_unbind_card("mig_unbind_card","解绑银行卡（会员信息综合通知）"),
    mig_apply_withhold("mig_apply_withhold","申请委托扣款（会员信息综合通知）"),
    mig_modify_withhold("mig_modify_withhold","修改委托扣款（会员信息综合通知）"),
    mig_cancel_withhold("mig_cancel_withhold","取消委托扣款（会员信息综合通知）");
    private final String type_name;
    private final String type_describe;

    public String getType_name() {
        return type_name;
    }

    public String getType_describe() {
        return type_describe;
    }


    /**
     * @param type_name
     * @param type_describe
     */
    NOTIFY_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
