package info.bfly.pay.bean.enums;

/**
 * 批处理状态
 * Created by XXSun on 2017/2/17.
 */
public enum BATCH_STATUS {
    WAIT_PROCESS("WAIT_PROCESS", "待处理(系统不会异步通知)"),
    PROCESSING("PROCESSING", "处理中(系统不会异步通知)"),
    FINISHED("FINISHED", "处理结束(系统会异步通知)");

    private final String status_name;
    private final  String status_describe;

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
    BATCH_STATUS(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
