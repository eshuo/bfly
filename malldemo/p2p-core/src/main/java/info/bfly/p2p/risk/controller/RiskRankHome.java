package info.bfly.p2p.risk.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.risk.model.RiskRank;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.REQUEST)
public class RiskRankHome extends EntityHome<RiskRank> implements Serializable {
    private final static String UPDATE_VIEW = FacesUtil.redirect("/admin/risk/riskRankList");

    public RiskRankHome() {
        // FIXME：保存角色的时候会执行一条更新User的语句
        setUpdateView(RiskRankHome.UPDATE_VIEW);
    }
}
