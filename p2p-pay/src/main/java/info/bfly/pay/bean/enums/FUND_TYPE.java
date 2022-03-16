package info.bfly.pay.bean.enums;

/**
 * Created by XXSun on 2/22/2017.
 */
public enum FUND_TYPE {
    PRINCIPAL("PRINCIPAL", "本金"),
    INTEREST("INTEREST", "利息"),
    BENEFIT("BENEFIT", "分润"),
    OTHER("OTHER", "其他");


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
    FUND_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
