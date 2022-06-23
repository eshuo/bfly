package info.bfly.p2p.trusteeship.service;

/**
 * Description: 资金托管service
 */
public interface TrusteeshipService {
    /**
     * 主动查询发往第三方资金托管的请求的状态，并根据结果做相应处理。
     */
    void handleSendedOperations();
}
