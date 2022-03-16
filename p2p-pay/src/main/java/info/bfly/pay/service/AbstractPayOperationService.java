package info.bfly.pay.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.DateUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.p2p.trusteeship.service.impl.TrusteeshipOperationBO;
import info.bfly.pay.bean.BaseSinaEntity;
import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.enums.RESPONSE_CODE;
import info.bfly.pay.util.SinaUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Set;

/**
 * Created by XXSun on 2017/1/11.
 */
public abstract class AbstractPayOperationService<T extends SinaInEntity, E extends BaseSinaEntity> implements PayOperationService<T, E> {
    @Log
    private Logger log;
    @Autowired
    private HttpClient httpClient;

    @Autowired
    private SinaUtils sinaUtils;

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper m;

    @Autowired
    private transient AutowireCapableBeanFactory beanFactory;

    @Autowired
    private TrusteeshipOperationBO trusteeshipOperationBO;
    @Autowired
    private  IdGenerator idGenerator;


    @Override
    public E getRequestEntity(Class<E> eClass) {
        return beanFactory.createBean(eClass);
    }

    @Override
    public TrusteeshipOperation createOperation(E e) {
        return createOperation(e, IdGenerator.randomUUID());
    }

    @Override
    public TrusteeshipOperation createOperation(E e, String markId) {
        return createOperation(e, e.getService(), markId);
    }

    @Override
    public TrusteeshipOperation createOperation(E e, String type, String markId) {
        return createOperation(e, type, markId, StringUtils.EMPTY);

    }

    @Override
    public TrusteeshipOperation createOperation(E e, String type, String markId, String expiraTime) {
        TrusteeshipOperation operation = getOperation(type, markId);
        TrusteeshipOperation to = new TrusteeshipOperation();
        to.setId(IdGenerator.randomUUID());
        if (operation != null) {
            if (!operation.hasExpired()) {
                log.debug("operation not expired will use the old one");
                return operation;
            } else {
                operation.setMarkId(IdGenerator.randomUUID());
                operation.setType(TrusteeshipConstants.OperationType.RECREATE);
                trusteeshipOperationBO.save(operation);
            }
        }

        to.setType(type);
        to.setMarkId(markId);
        to.setOperator(SecurityContextHolder.getContext().getAuthentication().getName());
        to.setTrusteeship(TrusteeshipConstants.Trusteeship.SINAPAY);
        to.setCharset("utf-8");
        to.setRequestData(e.toString());
        to.setRequestTime(new Date());
        to.setStatus(TrusteeshipConstants.OperationStatus.PREPARE);
        to.setExpiraTimeStr(expiraTime);
        to.setExpiraTime(parseExpiraTime(expiraTime));
        if (StringUtils.isEmpty(e.getReturn_url())&&FacesUtil.getCurrentInstance() != null) {
            String referer = FacesUtil.getExternalContext().getRequestHeaderMap().get("referer");
            e.setReturn_url(referer);
        }
        trusteeshipOperationBO.save(to);

        if (operation != null) {
            operation.setOriginalOperation(to);
            trusteeshipOperationBO.save(operation);
        }
        return to;
    }


    private Date parseExpiraTime(String expiraTime) {

        if (StringUtils.isEmpty(expiraTime))
            return new Date();
        else {
            return DateUtil.add(new Date(), expiraTime);
        }

    }


    @Override
    public TrusteeshipOperation updateOperation(E e, TrusteeshipOperation to) {

        to.setRequestData(e.toString());
        trusteeshipOperationBO.save(to);
        return to;
    }

    @Override
    public TrusteeshipOperation updateOperation(TrusteeshipOperation to) {
        trusteeshipOperationBO.save(to);
        return to;
    }

    @Override
    public TrusteeshipOperation addExpiraTime(String expiraTime, TrusteeshipOperation to) {
        return addExpiraTime(parseExpiraTime(expiraTime), to);
    }

    @Override
    public TrusteeshipOperation addExpiraTime(Date expiraTime, TrusteeshipOperation to) {
        to.setExpiraTime(expiraTime);
        trusteeshipOperationBO.save(to);
        return to;
    }

    @Override
    public TrusteeshipOperation getOperation(String type, String markId, String operator, String trusteeship) {
        return trusteeshipOperationBO.get(type, markId, operator, trusteeship);
    }

    @Override
    public TrusteeshipOperation getOperation(String type, String markId) {
        return trusteeshipOperationBO.get(type, markId, SecurityContextHolder.getContext().getAuthentication().getName(), "sinaPay");
    }


    @Override
    public void success(String operation) {
        trusteeshipOperationBO.success(operation);
    }

    @Override
    public void refuse(String operation) {
        trusteeshipOperationBO.refuse(operation);
    }

    @Override
    public void waiting(String operation) {
        trusteeshipOperationBO.waiting(operation);
    }

    @Override
    public T sendHttpClientOperation(E sendEntity, TrusteeshipOperation to, Class<T> tClass) {
        if (to == null) {
            throw new ValidationException("请求参数错误 TrusteeshipOperation 不能为空");
        }
        //TODO 记录保存 TrusteeshipOperation
        Set<ConstraintViolation<E>> constraintViolations = validator.validate(sendEntity);
        for (ConstraintViolation violation : constraintViolations) {
            log.debug(violation.getRootBeanClass().getSimpleName() + "->" + violation.getPropertyPath().toString() + "->" + violation.getMessage());
        }
        if (!constraintViolations.isEmpty()) {
            to.setStatus(TrusteeshipConstants.OperationStatus.UN_SEND);
            trusteeshipOperationBO.save(to);
            //TODO 抛出参数验证错误异常
            throw new ValidationException("请求参数错误");

        }
        String url = sinaUtils.getRequestUrl(sendEntity);
        HttpPost post = new HttpPost(url);
        String postReturnStr = StringUtils.EMPTY;
        try {
            if (!to.hasExpired())
                return m.readValue(to.getResponseData(), tClass);
            else if (!to.getStatus().equals(TrusteeshipConstants.OperationStatus.PREPARE) && !to.getStatus().equals(TrusteeshipConstants.OperationStatus.UN_SEND)) {
                log.debug("operation is expired this time, will create a new one by the old operation");
                to = createOperation(sendEntity, to.getType(), to.getMarkId(), to.getExpiraTimeStr());
            }
            Set<BasicNameValuePair> basicNameValuePairs = sinaUtils.sign(sendEntity);
            post.setEntity(new StringEntity(StringUtils.join(basicNameValuePairs, SinaUtils.AND), ContentType.APPLICATION_FORM_URLENCODED.withCharset(sendEntity.get_input_charset())));
            log.debug("send=" + IOUtils.toString(post.getEntity().getContent()));
            to.setRequestData(StringUtils.join(basicNameValuePairs, SinaUtils.AND));
            to.setRequestUrl(url);
            to.setRequestTime(new Date());
            to.setStatus(TrusteeshipConstants.OperationStatus.SENDED);
            trusteeshipOperationBO.save(to);
            HttpResponse httpResponse = httpClient.execute(post);

            postReturnStr = URLDecoder.decode(IOUtils.toString(httpResponse.getEntity().getContent()), sendEntity.get_input_charset());
            log.debug("get=" + postReturnStr);
            T sinaIn = m.readValue(postReturnStr, tClass);
            //String toStr = sinaUtils.removeMapSizeToStr(postReturnStr, "sign", "sign_type", "sign_version");
            to.setResponseData(postReturnStr);
            to.setResponseTime(sinaIn.getResponse_time());
            to.setExpiraTime(DateUtil.add(sinaIn.getResponse_time(), to.getExpiraTimeStr()));
            //目前只对校验未通过的改变状态
            if (sinaUtils.checkSign(sinaIn)) {
                to.setStatus(TrusteeshipConstants.OperationStatus.RECEIVED);
            } else {
                to.setStatus(TrusteeshipConstants.OperationStatus.CHECK_SIGN_ERROR);
            }

            trusteeshipOperationBO.save(to);
            log.debug("get = " + sinaIn.getResponse_code() + sinaIn.getResponse_message() + sinaIn.toString());
            return sinaIn;
        } catch (JsonParseException e) {
            try {
                T sinaIn = ConstructorUtils.invokeConstructor(tClass);
                sinaIn.setResponse_code(RESPONSE_CODE.HTML_RESPONSE);
                sinaIn.setResponse_message(postReturnStr);
                return sinaIn;
            } catch (Exception e1) {
                log.error(e1.getMessage());

                throw new ValidationException("返回数据异常 "+e1.getMessage());
            }
        } catch (ConnectTimeoutException e) {
            try {
                T sinaIn = ConstructorUtils.invokeConstructor(tClass);
                sinaIn.setResponse_code(RESPONSE_CODE.CONNECT_TIME_OUT);
                sinaIn.setResponse_message(RESPONSE_CODE.CONNECT_TIME_OUT.getStatus_describe());
                return sinaIn;
            } catch (Exception e1) {
                log.error(e1.getMessage());
                throw new ValidationException("返回数据异常 "+e1.getMessage());
            }
        }
        catch (IOException e) {

            log.error("{}.{}", e.getClass().getName(), e.getMessage());
            throw new ValidationException("返回数据异常 "+e.getMessage());
        } finally {
            post.releaseConnection();
        }
    }


    public boolean checkOperation(TrusteeshipOperation to) throws TrusteeshipReturnException {
        if (to.getStatus().equals(TrusteeshipConstants.OperationStatus.PASSED) || to.getStatus().equals(TrusteeshipConstants.OperationStatus.REFUSED)) {
            //TODO 抛出请求已经处理异常
            throw new TrusteeshipReturnException("");
        }
        if (to.getStatus().equals(TrusteeshipConstants.OperationStatus.RECEIVED)) {
            if (to.getExpiraTime().after(new Date())) {
                return true;
            } else
                //TODO 请求已经过期异常
                throw new TrusteeshipReturnException("");
        }
        return false;
    }
}
