package info.bfly.archer.theme.controller;

import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.SESSION)
public class AdminTheme implements java.io.Serializable {
    private static final long   serialVersionUID                = -2180480674044634558L;
    private final static String ADMIN_CURRENT_THEME_SESSION_KEY = "info.bfly.archer.adminThemeSelected";
    private String              theme;
    private Map<String, String> themes;

    private String getCookieValue() {
        Cookie[] cookies = FacesUtil.getHttpServletRequest().getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), AdminTheme.ADMIN_CURRENT_THEME_SESSION_KEY)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String getTheme() {
        if (theme == null) {
            theme = getCookieValue();
        }
        if (theme == null) {
            theme = "bootstrap";
        }
        return theme;
    }

    public Map<String, String> getThemes() {
        return themes;
    }

    @PostConstruct
    private void init() {
        themes = new HashMap<String, String>(3);
        themes.put("bluesky", "bluesky");
        themes.put("redmond", "redmond");
        themes.put("aristo", "aristo");
        themes.put("bootstrap", "bootstrap");
    }

    public void saveTheme() {
        setCookieValue(theme);
    }

    private void setCookieValue(String value) {
        Cookie cookie = new Cookie(AdminTheme.ADMIN_CURRENT_THEME_SESSION_KEY, value);
        cookie.setMaxAge(365 * 24 * 60 * 60);
        FacesUtil.getHttpServletResponse().addCookie(cookie);
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setThemes(Map<String, String> themes) {
        this.themes = themes;
    }
}
