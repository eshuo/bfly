package info.bfly.converter;

import info.bfly.api.exception.ParameterExection;
import info.bfly.api.exception.SinaPayExection;
import info.bfly.api.exception.UserOperationExection;
import info.bfly.api.model.Out;
import info.bfly.api.service.ApiService;
import info.bfly.archer.key.ResponseMsg;
import info.bfly.core.annotations.Log;
import info.bfly.core.exception.DecryptException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

@Service
public class MappingJacksonConverter extends MappingJackson2HttpMessageConverter {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ApiService apiService;

    @Log
    static Logger log;

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        //所有状态都返回200
        if (outputMessage instanceof ServerHttpResponse) {
            ((ServerHttpResponse) outputMessage).setStatusCode(HttpStatus.OK);
        }
        super.writeInternal(handleObject(object), type, outputMessage);
    }


    //TODO 做成可以注入的方式
    protected Object handleObject(Object object) {
        log.debug("handleObject with  -->" + object.getClass().getSimpleName());
        if (Exception.class.isAssignableFrom(object.getClass())) {
            log.error(((Exception) object).getMessage());
        }
        if (Out.class.isAssignableFrom(object.getClass()))
            return object;

        Out out;
        try {
            out = apiService.parseOut(request);
        } catch (ParameterExection e) {

            out = apiService.parseOutSimple(request);
            out.setResultCode(e.getErrorCode());
            out.setResultMsg(e.getErrorMsg());
            out.setResult();
            return out;
        } catch (DecryptException e) {
            out = apiService.parseOutwhitMessage(ResponseMsg.ILLEGAL_DECRYPT, request);
            out.setResult();
            return out;
        } catch (Exception e) {
            out = new Out(ResponseMsg.ERROR);
            out.setResult(e.getMessage());
            return out;
        }
        //重新定义OAuth2Exception返回的结果
        if (OAuth2AccessToken.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(ResponseMsg.SUCCESS);
            out.setResult(object);
            return out;
        }
        if (InvalidTokenException.class.isAssignableFrom(object.getClass())) {

            out.setResultCode(ResponseMsg.TOKEN_INVALID);
            out.setResult(object);
            return out;
        }
        if (InvalidGrantException.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(ResponseMsg.LOGIN_NOT_FIND);
            out.setResult(object);
            return out;
        }
        if (InvalidRequestException.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(ResponseMsg.PARAMETER_INVALID);
            out.setResult(object);
            return out;
        }
        if (OAuth2Exception.class.isAssignableFrom(object.getClass())) {

            int httpErrorCode = ((OAuth2Exception) object).getHttpErrorCode();
            switch (httpErrorCode) {
                case 400:
                case 401:
                    out.setResultCode(ResponseMsg.LOGIN_NOT_FIND);
                    break;
                default:
                    out.setResultCode(ResponseMsg.ERROR);
            }
            out.setResult();
            return out;
        }

        if (ParameterExection.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(((ParameterExection) object).getErrorCode());
            out.setResultMsg(((ParameterExection) object).getErrorMsg());
            out.setResult();
            return out;
        }

        if (SinaPayExection.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(((SinaPayExection) object).getErrorCode());
            out.setResultMsg(((SinaPayExection) object).getErrorMsg());
            out.setResult();
            return out;
        }

        if (UserOperationExection.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(((UserOperationExection) object).getErrorCode());
            out.setResultMsg(((UserOperationExection) object).getErrorMsg());
            out.setResult();
            return out;
        }


        //TODO 增加对应异常返回


        if (Exception.class.isAssignableFrom(object.getClass())) {
            out.setResultCode(ResponseMsg.ERROR);
            out.setResult(((Exception) object).getMessage());
            return out;
        }
        return out;
    }


}