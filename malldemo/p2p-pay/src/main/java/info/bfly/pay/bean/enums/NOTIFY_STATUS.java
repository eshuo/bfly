package info.bfly.pay.bean.enums;

/**
 * 标的状态
 * Created by XXSun on 2017/2/17.
 */
public enum NOTIFY_STATUS {
	single_notify("single_notify", "逐笔通知"),
	batch_notify("batch_notify", "逐笔通知");
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
    NOTIFY_STATUS(String status_name, String status_describe) {
        this.status_name = status_name;
        this.status_describe = status_describe;
    }
}
