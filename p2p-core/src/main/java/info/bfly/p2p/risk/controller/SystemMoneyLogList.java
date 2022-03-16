package info.bfly.p2p.risk.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.risk.model.SystemMoneyLog;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class SystemMoneyLogList extends EntityQuery<SystemMoneyLog> implements Serializable {
    public SystemMoneyLogList() {
        final String[] RESTRICTIONS = {"id like #{loanerBillList.example.id}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
