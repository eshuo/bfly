package info.bfly.p2p.invest.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.invest.model.AutoInvest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class AutoInvestList extends EntityQuery<AutoInvest> implements java.io.Serializable {
    private static final long serialVersionUID = 1532747758585263333L;

    public AutoInvestList() {
        addRestriction(" status='on' ");
        addOrder("lastAutoInvestTime", DIR_ASC);
        addOrder("seqNum", DIR_ASC);
    }
}
