package info.bfly.pay.service;

import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import info.bfly.pay.bean.BaseSinaEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

/**
 * Description: 资金托管service，负责发起操作请求（充值、投标等等），与接收回调
 */
public interface PayOperationService<T, E extends BaseSinaEntity> {

    /**
     * 获取基本请求数据
     * @param eClass
     * @return
     */
    E getRequestEntity(Class<E> eClass);

    /**
     * 创建操作请求，例如开户、充值等等,不涉及前台页面
     *
     * @param e 请求数据的封装对象
     * @return TrusteeshipOperation
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TrusteeshipOperation createOperation(E e) throws IOException;

    /**
     * 创建操作请求，例如开户、充值等等,不涉及前台页面
     *
     * @param e      请求数据的封装对象
     * @param markId 标记需要处理的数据 一般用于回调
     * @return TrusteeshipOperation
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TrusteeshipOperation createOperation(E e, String markId) throws IOException;

    /**
     * 创建操作请求，例如开户、充值等等,不涉及前台页面
     *
     * @param e      请求数据的封装对象
     * @param Type   请求类型 默认为e.getService()
     * @param markId 标记需要处理的数据 一般用于回调
     * @return TrusteeshipOperation
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TrusteeshipOperation createOperation(E e, String Type, String markId);

    /**
     * 创建操作请求，例如开户、充值等等,不涉及前台页面
     *
     * @param e
     * @param type
     * @param markId
     * @param expiraTime 返回结果有效截至时间 减少请求次数
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TrusteeshipOperation createOperation(E e, String type, String markId, String expiraTime);


    /**
     * 更新操作请求
     *
     * @param e
     * @param to
     * @return
     */
    TrusteeshipOperation updateOperation(E e, TrusteeshipOperation to);

    TrusteeshipOperation updateOperation(TrusteeshipOperation to);

    /**
     * 添加请求返回结果有效截至时间
     *
     * @param expiraTime
     * @return
     */
    TrusteeshipOperation addExpiraTime(String expiraTime, TrusteeshipOperation to);

    /**
     * 添加请求返回结果有效截至时间
     *
     * @param expiraTime
     * @return
     */
    TrusteeshipOperation addExpiraTime(Date expiraTime, TrusteeshipOperation to);

    /**
     * 获取操作记录
     *
     * @param type
     * @param markId
     * @param operator
     * @param trusteeship
     * @return
     */
    TrusteeshipOperation getOperation(String type, String markId, String operator, String trusteeship);

    /**
     * 获取操作记录
     * operator = 当前登录用户
     * trusteeship = sinaPay
     *
     * @param type
     * @param markId
     * @return
     */
    TrusteeshipOperation getOperation(String type, String markId);


    // T sendHttpClientOperation( E sendEntity,TrusteeshipOperation to);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void success(String operationId);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void refuse(String operationId);

    void waiting(String operation);

    /**
     * 发送http请求
     *
     * @return T 封装的对象
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    T sendHttpClientOperation(E sendEntity, TrusteeshipOperation to, Class<T> tClass);
}
