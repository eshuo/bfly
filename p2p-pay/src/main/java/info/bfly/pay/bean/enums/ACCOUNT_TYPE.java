package info.bfly.pay.bean.enums;

/**
 * 账户类型
 * Created by XXSun on 2017/2/17.
 */
public enum ACCOUNT_TYPE {
    BASIC("BASIC","基本户"),
    ENSURE("ENSURE","保证金户"),
    RESERVE("RESERVE","准备金"),
    SAVING_POT("SAVING_POT","存钱罐"),
    BANK("BANK","银行账户");

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
    ACCOUNT_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
