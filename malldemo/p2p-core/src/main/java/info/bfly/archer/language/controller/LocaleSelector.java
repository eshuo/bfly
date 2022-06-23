package info.bfly.archer.language.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.Cookie;
import java.util.Locale;

@Component
@Scope(ScopeType.SESSION)
public class LocaleSelector implements java.io.Serializable {
    private static final long serialVersionUID = -2397960330536445772L;
    @Log
    static Logger log;
    public final static String CURRENT_LANGUAGE_SESSION_KEY = "info.bfly.archer.localeSelected";
    private Locale             locale;

    public void changeLocale(String locale) {
        int separator = locale.indexOf("_");
        if (separator > 0) {
            final String[] info = locale.split("_");
            setLocale(new Locale(info[0], info[1]));
        }
    }

    public void changeLocale(ValueChangeEvent event) {
        changeLocale((String) event.getNewValue());
    }

    private String getCookieValue() {
        Cookie[] cookies = FacesUtil.getHttpServletRequest().getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), LocaleSelector.CURRENT_LANGUAGE_SESSION_KEY)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public Locale getLocale() {
        if (locale == null) {
            // get locale from cookie
            if (getCookieValue() != null) {
                changeLocale(getCookieValue());
            } else {
                // locale =
                // FacesUtil.getCurrentInstance().getViewRoot().getLocale();
                locale = FacesUtil.getExternalContext().getRequestLocale();
            }
        }
        return locale;
    }

    private void setCookieValue(String value) {
        Cookie cookie = new Cookie(LocaleSelector.CURRENT_LANGUAGE_SESSION_KEY, value);
        cookie.setMaxAge(365 * 24 * 60 * 60);
        FacesUtil.getHttpServletResponse().addCookie(cookie);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        setSessionValue(locale.getLanguage() + "_" + locale.getCountry());
        setCookieValue(locale.getLanguage() + "_" + locale.getCountry());
    }

    private void setSessionValue(String value) {
        FacesUtil.setSessionAttribute(LocaleSelector.CURRENT_LANGUAGE_SESSION_KEY, value);
    }
}
