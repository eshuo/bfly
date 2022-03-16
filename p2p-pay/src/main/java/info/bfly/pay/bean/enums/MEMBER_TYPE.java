package info.bfly.pay.bean.enums;

/**
 * Created by XXSun on 2017/2/21.
 */
public enum MEMBER_TYPE {
    A("1","个人会员"),B("2","企业会员");
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
    MEMBER_TYPE(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
