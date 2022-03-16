package info.bfly.pay.bean;

import info.bfly.pay.bean.enums.NOTIFY_TYPE;

import javax.validation.constraints.NotNull;

/**
 * 回调请求基本参数
 * Created by XXSun on 2017/2/17.
 */
public class BaseCallBackEntity extends SinaInEntity {
    private static final long serialVersionUID = -5852001000398115908L;
    /**
     * 通知类型
     */
    @NotNull
    private NOTIFY_TYPE notify_type;
    /**
     * 通知Id
     */
    @NotNull
    private String      notify_id;
    /**
     * 通知时间
     */
    @NotNull
    private String      notify_time;
    /**
     * 返回错误码
     */
    private String      error_code;
    /**
     * 返回错误信息
     */
    private String      error_message;

    public NOTIFY_TYPE getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(NOTIFY_TYPE notify_type) {
        this.notify_type = notify_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    @Override
    public String toString() {
        return "BaseCallBackEntity{" +
                "notify_type=" + notify_type +
                ", notify_id='" + notify_id + '\'' +
                ", notify_time='" + notify_time + '\'' +
                ", error_code='" + error_code + '\'' +
                ", error_message='" + error_message + '\'' +
                "} " + super.toString();
    }
}
