package info.bfly.archer.theme.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.Theme;
import info.bfly.archer.theme.service.ThemeService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@Scope(ScopeType.REQUEST)
public class ThemeHome extends EntityHome<Theme> implements Serializable {
    @Log
    static Logger log;
    private static StringManager sm = StringManager.getManager(ThemeConstants.Package);
    @Resource
    private ThemeService themeService;

    public ThemeHome() {
        setUpdateView(FacesUtil.redirect(ThemeConstants.View.THEME_LIST));
    }

    public String setDefaultTheme(final String themeId) {
        boolean success = themeService.setDefaultTheme(themeId);
        if (success) {
            final String message = ThemeHome.sm.getString("log.setDefaultTheme", themeId);
            if (ThemeHome.log.isInfoEnabled()) {
                ThemeHome.log.info(message);
            }
            FacesUtil.addInfoMessage(message);
            // FIXME:
            FacesUtil.setSessionAttribute(ThemeConstants.SESSION_KEY_PRETTY_CONFIG, null);
            FacesUtil.setSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME, themeId);
        }
        return FacesUtil.redirect(ThemeConstants.View.THEME_LIST);
    }
}
