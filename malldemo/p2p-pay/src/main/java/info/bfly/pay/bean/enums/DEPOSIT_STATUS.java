package info.bfly.pay.bean.enums;

/**
 * 充值状态
 * Created by XXSun on 2017/2/17.
 */
public enum DEPOSIT_STATUS {
    SUCCESS("SUCCESS", "成功(系统会异步通知)"),
    FAILED("FAILED", "失败(系统会异步通知)"),
    PROCESSING("PROCESSING", "处理中(系统不会异步通知)");
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
    DEPOSIT_STATUS(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
