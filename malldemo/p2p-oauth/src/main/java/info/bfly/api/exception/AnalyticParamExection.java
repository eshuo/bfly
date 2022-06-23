package info.bfly.api.exception;

import info.bfly.archer.key.ResponseMsg;

/**
 *  解析参数异常
 * Created by Administrator on 2017/1/4 0004.
 */
public class AnalyticParamExection extends  ApiExection {

    AnalyticParamExection(){

    }

    public AnalyticParamExection(String message){
        super(ResponseMsg.PARSE_VALUE_ERROR.getCode(), message);
    }




}
