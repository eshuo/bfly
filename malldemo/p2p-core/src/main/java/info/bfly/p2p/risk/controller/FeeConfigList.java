package info.bfly.p2p.risk.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.risk.model.FeeConfig;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class FeeConfigList extends EntityQuery<FeeConfig> implements Serializable {
}
