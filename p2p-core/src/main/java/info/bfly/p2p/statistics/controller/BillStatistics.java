package info.bfly.p2p.statistics.controller;

import info.bfly.archer.user.service.UserBillService;
import info.bfly.core.annotations.ScopeType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 账户（单）统计
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class BillStatistics {
    @Resource
    private UserBillService ubs;

    /**
     * 获取用户账户余额
     *
     * @return
     */
    public double getBalanceByUserId(String userId) {
        return ubs.getBalance(userId);
    }

    /**
     * 获取用户账户冻结金额
     *
     * @param userId
     * @return
     */
    public double getFrozenMoneyByUserId(String userId) {
        return ubs.getFrozenMoney(userId);
    }
}
