package info.bfly.p2p.trusteeship.service;

import info.bfly.p2p.trusteeship.model.TrusteeshipAccount;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XXSun on 3/9/2017.
 */
public interface TrusteeshipAccountService {
    /**
     * 获取用户绑定第三方账户
     *
     * @param userId
     * @param trusteeship
     * @return
     */
    TrusteeshipAccount getTrusteeshipAccount(String userId, String trusteeship);

    /**
     * 获取系统在第三方的账户
     */
    TrusteeshipAccount getSystemTrusteeshipAccount(String trusteeship);

    /**
     * 绑定第三方账户
     *
     * @param userId
     * @param trusteeship
     * @param accountId
     */
    void bindTrusteeshipAccount(String userId, String trusteeship, String accountId);

    /**
     * 绑定第三方账户
     *
     * @param userId
     * @param trusteeship
     * @param accountId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void bindTrusteeshipAccount(String userId, String trusteeship, String accountId, String type);

    /**
     * 绑定第三方账户
     *
     * @param trusteeshipAccount
     */
    void bindTrusteeshipAccount(TrusteeshipAccount trusteeshipAccount);

}
