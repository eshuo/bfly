package info.bfly.pay.bean.enums;

/**
 * 交易状态
 * Created by XXSun on 2017/2/17.
 */
public enum TRADE_STATUS {
    WAIT_PAY("WAIT_PAY", "等待付款(系统不会异步通知)"),
    PAY_FINISHED("PAY_FINISHED", "已付款(系统会异步通知)"),
    TRADE_FAILED("TRADE_FAILED", "交易失败(系统会异步通知)"),
    TRADE_FINISHED("TRADE_FINISHED", "交易结束(系统会异步通知)"),
    TRADE_CLOSED("TRADE_CLOSED", "交易关闭（合作方通过调用交易取消接口来关闭）(系统会异步通知)"),
    PRE_AUTH_APPLY_SUCCESS("PRE_AUTH_APPLY_SUCCESS", "代收冻结成功（商户通知）"),
    PRE_AUTH_CANCELED("PRE_AUTH_CANCELED", "代收撤销成功（商户通知）");

    private final  String status_name;
    private final String status_describe;

    public String getStatus_name() {
        return status_name;
    }


    public String getStatus_describe() {
        return status_describe;
    }


    /**
     *
     * @param status_name
     * @param status_describe
     */
    TRADE_STATUS(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
