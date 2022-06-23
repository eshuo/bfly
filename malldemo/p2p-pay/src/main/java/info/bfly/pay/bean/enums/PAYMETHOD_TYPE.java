package info.bfly.pay.bean.enums;

/**
 * Created by XXSun on 3/1/2017.
 */
public enum PAYMETHOD_TYPE {
    ONLINE_BANK("online_bank","在线支付"),
    BALANCE("balance","余额支付"),
    BINDING_PAY("binding_pay","绑定银行卡支付");
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
    PAYMETHOD_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
