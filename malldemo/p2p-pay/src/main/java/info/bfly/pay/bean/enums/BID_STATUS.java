package info.bfly.pay.bean.enums;

/**
 * 标的状态
 * Created by XXSun on 2017/2/17.
 */
public enum BID_STATUS {
    INIT("INIT", "初始"),
    AUDITING("AUDITING", "审核中"),
    REJECT("REJECT", "审核拒绝(商户通知)"),
    VALID("VALID", "有效(商户通知)"),
    INVALID("INVALID", "无效(商户通知)");
    private final String status_name;
    private final String status_describe;

    public String getStatus_name() {
        return status_name;
    }


    public String getStatus_describe() {
        return status_describe;
    }


    /**
     * @param status_name
     * @param status_describe
     */
    BID_STATUS(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
