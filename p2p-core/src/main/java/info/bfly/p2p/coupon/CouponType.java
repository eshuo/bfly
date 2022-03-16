package info.bfly.p2p.coupon;


/**
 * 代金券类型
 *
 * @author Administrator
 */
public  enum CouponType {
    CREDIT("invest", "投资代金券"),
    MORTGAGE("demo", "测试代金券"),
    ASSIGNMENT_DEBT("addUser", "开户代金券"),
    OTHER("other", "其他");
    private final String type_name;
    private final String type_describe;

    public String getType_name() {
        return type_name;
    }


    public String getType_describe() {
        return type_describe;
    }


    CouponType(String type_name, String type_describe) {
        this.type_name = type_name;
        this.type_describe = type_describe;
    }
}
