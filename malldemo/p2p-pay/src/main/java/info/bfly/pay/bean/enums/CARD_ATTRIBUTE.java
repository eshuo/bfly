package info.bfly.pay.bean.enums;

/**
 * 卡属性
 * Created by XXSun on 2017/2/17.
 */
public enum CARD_ATTRIBUTE {
    C("C", "对私"),
    B("B", "对公");
    private final String attribute_name;
    private final String attribute_describe;

    public String getAttribute_name() {
        return attribute_name;
    }


    public String getAttribute_describe() {
        return attribute_describe;
    }


    /**
     * @param attribute_name
     * @param attribute_describe
     */
    CARD_ATTRIBUTE(String attribute_name, String attribute_describe) {
        this.attribute_name = attribute_name;
        this.attribute_describe = attribute_describe;
    }
}
