package info.bfly.archer;

import info.bfly.archer.common.CommonConstants;
import info.bfly.core.annotations.Log; import org.slf4j.Logger;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 整个项目中所用到的一些App常量的获取。<br/>
 * <li>在xhtml中可以使用 #{appInfo.contextPath}获取当前项目的上下文路径</li> <li>
 * 在xhtml中可以使用#{appInfo.appUrl}获取当前站点地址，包含端口号，格式如下：
 * http://|https://+serverName+port+contextPath</li>
 *
 * @author wanghm
 *
 */
@Component
@Scope("application")
public class AppInfo {
    private static String  contextPath;
    private static String  appUrl;
    private static AppInfo instance;
    private static StringManager sm = StringManager.getManager(CommonConstants.Package);
    @Log
    private static Logger log;

    public static AppInfo getInstance() {
        if (AppInfo.instance == null) {
            AppInfo.instance = new AppInfo();
        }
        return AppInfo.instance;
    }



    private AppInfo() {
        //
    }

    public String getAppRealPath() {
        return FacesUtil.getAppRealPath();
    }

    public String getAppUrl() {
        if (AppInfo.appUrl == null) {
            AppInfo.appUrl = FacesUtil.getCurrentAppUrl();
            if (AppInfo.appUrl.endsWith("/")) {
                AppInfo.appUrl = AppInfo.appUrl.substring(0, AppInfo.appUrl.length() - 1);
            }
        }
        return AppInfo.appUrl;
    }

    public String getContextPath() {
        if (AppInfo.contextPath == null) {
            AppInfo.contextPath = FacesUtil.getContextPath();
            if (AppInfo.contextPath.equals("/")) {
                AppInfo.contextPath = "";
            }
        }
        return AppInfo.contextPath;
    }

    /**
     * 刷新系统Bean
     */
    public void refresh() {
        long startTime = System.currentTimeMillis();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(FacesUtil.getHttpSession().getServletContext());
        AbstractRefreshableApplicationContext absContext = ((AbstractRefreshableApplicationContext) context);
        absContext.refresh();
        long endTime = System.currentTimeMillis();
        final String message = AppInfo.sm.getString("refreshSuccess", (endTime - startTime));
        FacesUtil.addInfoMessage(message);
        AppInfo.log.debug(message);
    }
}
