package info.bfly.archer.theme.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.Template;
import info.bfly.archer.theme.model.Theme;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(ScopeType.REQUEST)
public class TemplateHome extends EntityHome<Template> implements Serializable {
    @Log
    static Logger log;

    public TemplateHome() {
        setUpdateView(FacesUtil.redirect(ThemeConstants.View.TEMPLATE_LIST));
    }

    @Override
    protected Template createInstance() {
        Template template = new Template();
        template.setTheme(new Theme());
        return template;
    }
}
