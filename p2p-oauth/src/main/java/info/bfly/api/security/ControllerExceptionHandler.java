package info.bfly.api.security;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by XXSun on 2016/12/21.
 */
@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Exception handException(Exception e) {
        return e;
    }
}
