package info.bfly.p2p.risk.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.risk.model.FeeConfig;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class FeeConfigHome extends EntityHome<FeeConfig> implements Serializable {
    private final static String UPDATE_VIEW = FacesUtil.redirect("/admin/risk/feeConfigList");

    public FeeConfigHome() {
        setUpdateView(FeeConfigHome.UPDATE_VIEW);
    }
}
