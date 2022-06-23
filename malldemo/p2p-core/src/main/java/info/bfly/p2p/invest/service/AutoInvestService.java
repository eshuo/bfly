package info.bfly.p2p.invest.service;

import info.bfly.p2p.invest.model.AutoInvest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自动投标
 */
public interface AutoInvestService {
    /**
     * @param loanId
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    void autoInvest(String loanId);

    /**
     * 用户设置自动投标
     */
    @Transactional(rollbackFor = Exception.class)
    void settingAutoInvest(AutoInvest ai);
}
