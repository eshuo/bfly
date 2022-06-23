package info.bfly.archer.theme.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("tplVars")
@Scope(ScopeType.SESSION)
public class TplVars implements java.io.Serializable {
    private static final long  serialVersionUID = -7089517277500127277L;
    @Log
    static Logger log;
    public final static String FRONT_PATTERN    = "(.*?)(/index.htm)(.*?)";
    public final static String LOGIN_PATTERN    = "(.*?)((/memberLogin.htm)|(/register.htm)|(/regsuccess.htm)|(/regActiveuser.htm)|(/findPwd.htm)|(/findPwdbyMail.htm)|(/findPwdbyMailCode.htm))(.*?)";
    // -----------
    public final static String LOGO_PATH        = "/logo.png";
    public final static String IMAGE_PATH       = "/images";
    public final static String JS_PATH          = "/js";
    public final static String CSS_PATH         = "/css";
    public final static String STATIC_PATH      = "/static";
    public final static String RESOURCES_PATH   = "/resources";
    public final static String COMPONENTS_PATH  = "/components";
    public final static String LAYOUT_PATH      = "/layout";

    public String getComponentsPath() {
        return getCurrentThemePath() + TplVars.COMPONENTS_PATH;
    }

    public String getContextPath() {
        return (String) FacesUtil.getExpressionValue("#{appInfo.contextPath}");
    }

    /**
     * 获取当前用户的主题 eg:default
     *
     * @return
     */
    public String getCurrentTheme() {
        return FacesUtil.getUserTheme();
    }

    /**
     * 获取当前用户使用主题的路径 例如：/themes/default
     *
     * @return
     */
    public String getCurrentThemePath() {
        return FacesUtil.getThemePath();
    }

    public String getLayoutPath() {
        return getCurrentThemePath() + TplVars.LAYOUT_PATH;
    }

    /**
     * 当前主题logo路径“#{appInfo.contextPath}"+{currentThemePath}+"/images/ logo.jpg”
    
     *
     * @return
     */
    public String getLogoPath() {
        return getContextPath() + getCurrentThemePath() + TplVars.RESOURCES_PATH + TplVars.IMAGE_PATH + TplVars.LOGO_PATH;
    }

    public String getStaticCssPath() {
        return getContextPath() + TplVars.STATIC_PATH + TplVars.CSS_PATH;
    }

    /**
     * 获取静态图片路径 eg:#{appInfo.contextPath}/static/images
     *
     * @return
     */
    public String getStaticImgPath() {
        return getContextPath() + TplVars.STATIC_PATH + TplVars.IMAGE_PATH;
    }

    public String getStaticJsPath() {
        return getContextPath() + TplVars.STATIC_PATH + TplVars.JS_PATH;
    }

    /**
     * 获取静态资源路径 eg:/static
     */
    public String getStaticPath() {
        return TplVars.STATIC_PATH;
    }

    public String getThemeCssPath() {
        return getContextPath() + getCurrentThemePath() + TplVars.RESOURCES_PATH + TplVars.CSS_PATH;
    }

    public String getThemeImgPath() {
        return getContextPath() + getCurrentThemePath() + TplVars.RESOURCES_PATH + TplVars.IMAGE_PATH;
    }

    public String getThemeJSPath() {
        return getContextPath() + getCurrentThemePath() + TplVars.RESOURCES_PATH + TplVars.JS_PATH;
    }

    /**
     * 判断当前页面路径是否是首页，判断方式，当前路径是否包含“/index.htm” 字符
     *
     * @return
     */
    public boolean isFront() {
        final String uri = FacesUtil.getHttpServletRequest().getRequestURI();
        return uri.matches(TplVars.FRONT_PATTERN);
    }

    public boolean isLogin() {
        final String uri = FacesUtil.getHttpServletRequest().getRequestURI();
        return uri.matches(TplVars.LOGIN_PATTERN);
    }
}
