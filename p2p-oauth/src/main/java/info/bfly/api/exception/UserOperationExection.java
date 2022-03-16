package info.bfly.api.exception;

import info.bfly.archer.key.ResponseMsg;

/**
 * @Author Wangs
 * Date：2017/6/8 0008
 * Description：用户操作异常
 */
public class UserOperationExection extends ApiExection {


    public UserOperationExection() {
    }

    public UserOperationExection(String message) {
        super(ResponseMsg.USER_ERROR.getCode(), message);
    }

    public UserOperationExection(ResponseMsg message) {
        super(message);
    }

    public UserOperationExection(ResponseMsg message, Object... parameters) {
        super(message, parameters);
    }

    public UserOperationExection(Object... parametername) {
        super(ResponseMsg.USER_ERROR.getCode(), String.format(ResponseMsg.USER_ERROR.getDescription(), parametername));
    }
}
