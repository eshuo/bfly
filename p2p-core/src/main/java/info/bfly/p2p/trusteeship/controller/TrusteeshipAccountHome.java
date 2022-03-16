package info.bfly.p2p.trusteeship.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.trusteeship.model.TrusteeshipAccount;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by XXSun on 6/8/2017.
 */
@Component
@Scope(ScopeType.VIEW)
public class TrusteeshipAccountHome extends EntityHome<TrusteeshipAccount> implements Serializable {

}
