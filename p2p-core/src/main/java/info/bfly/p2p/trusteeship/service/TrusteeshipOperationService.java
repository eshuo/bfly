package info.bfly.p2p.trusteeship.service;

import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Description: 资金托管service，负责发起操作请求（充值、投标等等），与接收回调
 */
public interface TrusteeshipOperationService<T> {
    /**
     * 创建操作请求，例如开户、充值等等
     *
     * @return TrusteeshipOperation的编号
     */
    TrusteeshipOperation createOperation(T t, FacesContext facesContext) throws IOException;

    /**
     * 接收操作回调(POST方式)，例如开户回调、充值回调
     *
     * @return 需要返回的相应页面
     */
    void receiveOperationPostCallback(ServletRequest request) throws TrusteeshipReturnException;

    /**
     * 接收操作回调(server to server方式)，例如开户回调、充值回调
     */
    void receiveOperationS2SCallback(ServletRequest request, ServletResponse response);


}
