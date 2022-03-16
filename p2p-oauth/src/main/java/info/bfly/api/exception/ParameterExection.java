package info.bfly.api.exception;

import info.bfly.archer.key.ResponseMsg;

/**
 * 参数错误异常
 * Created by XXSun on 2016/12/21.
 */
public class ParameterExection extends ApiExection {
    public ParameterExection() {
    }

    public ParameterExection(String message) {
        super(ResponseMsg.PARAMETER_NOT_NULL.getCode(), message);
    }

    public ParameterExection(ResponseMsg message) {
        super(message);
    }

    public ParameterExection(ResponseMsg message, Object... parameters) {
        super(message, parameters);
    }



    public ParameterExection(Object... parametername) {
        super(ResponseMsg.PARAMETER_NOT_NULL.getCode(), String.format(ResponseMsg.PARAMETER_NOT_NULL.getDescription(), parametername));
    }

}
