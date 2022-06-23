package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.model.WithdrawCash;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 提现查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class WithdrawList extends EntityQuery<WithdrawCash> implements Serializable {
    private static final long serialVersionUID = 9057256750216810237L;
    @Log
    static Logger log;

    public WithdrawList() {
        final String[] RESTRICTIONS = {"id like #{withdrawList.example.id}", "user.username like #{withdrawList.example.user.username}", "status like #{withdrawList.example.status}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @Override
    protected void initExample() {
        super.initExample();
        getExample().setUser(new User());
    }
}
