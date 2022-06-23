package info.bfly.api.exception;

import info.bfly.archer.key.ResponseMsg;

/**
 * Created by XXSun on 2016/12/21.
 */
public class ApiExection extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public ApiExection(){

    }

    public ApiExection(ResponseMsg message) {
        super(message.toString());
        this.errorCode = message.getCode();
        this.errorMsg = message.getDescription();
    }
    public ApiExection(ResponseMsg message,Object... parameters) {
        super(String.format(message.toString(),parameters));
        this.errorCode = message.getCode();
        this.errorMsg = String.format(message.getDescription(),parameters);
    }
    public ApiExection(int errorCode,String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public int getErrorCode(){
        return errorCode;
    }

    public String getErrorMsg(){
        return errorMsg;
    }

}
