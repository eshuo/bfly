package info.bfly.archer.theme.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.theme.ThemeConstants;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(ScopeType.REQUEST)
public class ComponentHome extends EntityHome<info.bfly.archer.theme.model.Component> implements Serializable {
    @Log
    static Logger log;

    public ComponentHome() {
        setUpdateView(FacesUtil.redirect(ThemeConstants.View.COMPONENT_LIST));
    }
}
