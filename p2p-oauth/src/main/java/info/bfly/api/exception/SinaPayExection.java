package info.bfly.api.exception;

import info.bfly.archer.key.ResponseMsg;

/**
 * @Author Wangs
 * Date：2017/6/8 0008
 * Description： 新浪异常
 */
public class SinaPayExection extends ApiExection {


    public SinaPayExection() {
    }

    public SinaPayExection(String message) {
        super(ResponseMsg.SINA_PAY_ERROR.getCode(), message);
    }

    public SinaPayExection(ResponseMsg message) {
        super(message);
    }

    public SinaPayExection(ResponseMsg message, Object... parameters) {
        super(message, parameters);
    }

    public SinaPayExection(Object... parametername) {
        super(ResponseMsg.SINA_PAY_ERROR.getCode(), String.format(ResponseMsg.SINA_PAY_ERROR.getDescription(), parametername));
    }
}
