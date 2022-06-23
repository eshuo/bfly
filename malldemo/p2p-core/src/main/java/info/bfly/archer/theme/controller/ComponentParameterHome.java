package info.bfly.archer.theme.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.ComponentParameter;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(ScopeType.REQUEST)
public class ComponentParameterHome extends EntityHome<ComponentParameter> implements Serializable {
    @Log
    static Logger log;

    public ComponentParameterHome() {
        // setUpdateView(
        // FacesUtil.redirect(ThemeConstants.View.COMPONENT_EDIT+"?id="+getId())
        // );
    }

    @Override
    protected ComponentParameter createInstance() {
        ComponentParameter instance = new ComponentParameter();
        instance.setComponent(new info.bfly.archer.theme.model.Component());
        return instance;
    }

    @Override
    public String getUpdateView() {
        return FacesUtil.redirect(ThemeConstants.View.COMPONENT_EDIT) + "&id=" + getInstance().getComponent().getId();
    }
}
