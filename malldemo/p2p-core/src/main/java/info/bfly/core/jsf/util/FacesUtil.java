package info.bfly.core.jsf.util;

import info.bfly.archer.theme.ThemeConstants;
import info.bfly.core.jsf.MultiPageMessagesSupport;
import info.bfly.core.util.SpringBeanUtil;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ManagedBean
public class FacesUtil {

    private static final Logger log = LoggerFactory.getLogger(FacesUtil.class);

    public static void addErrorMessage(String message) {
        FacesUtil.addErrorMessage(message, null);
    }

    public static void addErrorMessage(String summary, String message) {
        FacesUtil.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message));
    }

    public static void addErrorMessage(String clientId, String summary, String message) {
        FacesUtil.addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message));
    }

    public static void addFatalMessage(String message) {
        FacesUtil.addFatalMessage(message, null);
    }

    public static void addFatalMessage(String summary, String message) {
        FacesUtil.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, message));
    }

    public static void addInfoMessage(String message) {
        FacesUtil.addInfoMessage(message, null);
    }

    public static void addInfoMessage(String summary, String message) {
        FacesUtil.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message));
    }

    // ~ Message ..
    public static void addMessage(String clientId, FacesMessage message) {
        if (FacesUtil.getCurrentInstance() != null)
            FacesUtil.getCurrentInstance().addMessage(clientId, message);
    }

    /**
     * 如果消息产生不在jsf生命周期中，且需要展示，则调用如下方法。例如：通过pretty-faces：action中添加的message。
     *
     * @param context
     */
    public static void addMessagesOutOfJSFLifecycle(FacesContext context) {
        List<FacesMessage> messages = new ArrayList<FacesMessage>();
        for (Iterator<FacesMessage> iter = context.getMessages(null); iter.hasNext(); ) {
            messages.add(iter.next());
            iter.remove();
        }
        Map<String, Object> sessionMap = FacesUtil.getCurrentInstance().getExternalContext().getSessionMap();
        List<FacesMessage> existingMessages = (List<FacesMessage>) sessionMap.get(MultiPageMessagesSupport.sessionToken);
        if (existingMessages != null) {
            existingMessages.addAll(messages);
        } else {
            sessionMap.put(MultiPageMessagesSupport.sessionToken, messages);
        }
    }

    public static void addWarnMessage(String message) {
        FacesUtil.addWarnMessage(message, null);
    }

    public static void addWarnMessage(String summary, String message) {
        FacesUtil.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message));
    }

    public static void evictSecondLevelCache() {
        SessionFactory sf = (SessionFactory) SpringBeanUtil.getBeanByName("sessionFactory");
        Map<String, ClassMetadata> roleMap = sf.getAllCollectionMetadata();
        for (String roleName : roleMap.keySet()) {
            sf.getCache().evictCollectionRegion(roleName);
        }
        Map<String, ClassMetadata> entityMap = sf.getAllClassMetadata();
        for (String entityName : entityMap.keySet()) {
            sf.getCache().evictEntityRegion(entityName);
        }
        sf.getCache().evictQueryRegions();
    }

    public static Application getApplication() {
        return FacesUtil.getCurrentInstance().getApplication();
    }

    /**
     * 获取app硬盘绝对路径
     *
     * @return
     */
    public static String getAppRealPath() {
        return FacesUtil.getRealPath("/");
    }

    /**
     * 获取图片备份绝对路径
     *
     * @return
     */
    public static String getPicBackUpPath() {
        return FacesUtil.getRealPath("/") + "/backup";
    }

    public static String getContextPath() {
        return FacesUtil.getExternalContext().getRequestContextPath();
    }

    public static String getCurrentAppUrl() {
        ExternalContext ec = FacesUtil.getExternalContext();
        return ec.getRequestScheme() + "://" + ec.getRequestServerName() + (ec.getRequestServerPort() == 80 ? "" : (":" + ec.getRequestServerPort())) + ec.getRequestContextPath();
        // return getExternalContext().getRequestServerPort()+"";
    }

    // ~ Base ..
    public static FacesContext getCurrentInstance() {
        return FacesContext.getCurrentInstance();
    }

    // ~ JSF EL

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching object (or creating it).
     *
     * @param expression EL expression
     * @return Managed object
     */
    public static Object getExpressionValue(String expression) {
        Application app = FacesUtil.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = FacesUtil.getCurrentInstance().getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }

    public static ExternalContext getExternalContext() {
        return FacesUtil.getCurrentInstance().getExternalContext();
    }

    // ~ Servlet ..
    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) FacesUtil.getExternalContext().getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) FacesUtil.getExternalContext().getResponse();
    }

    public static HttpSession getHttpSession() {
        if (FacesUtil.getCurrentInstance() == null) {
            return null;
        } else {
            return (HttpSession) FacesUtil.getExternalContext().getSession(true);
        }
    }

    public static String getParameter(String name) {
        return FacesUtil.getHttpServletRequest().getParameter(name);
    }

    // ~~ Path
    public static String getRealPath(String path) {
        return FacesUtil.getExternalContext().getRealPath(path);
    }

    public static Object getRequestAttribute(String name) {
        return FacesUtil.getHttpServletRequest().getAttribute(name);
    }

    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串ip值,X-Forwarded-For中第一个非unknown的有效IP字符串是真实ip。
        return ip.split(",")[0];
    }

    public static Object getSessionAttribute(String name) {
        return FacesUtil.getHttpSession().getAttribute(name);
    }

    public static String getThemePath() {
        return ThemeConstants.THEME_PATH + "/" + FacesUtil.getUserTheme();
    }

    public static String getUserTheme() {
        String userTheme = (String) FacesUtil.getSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME);
        if (userTheme == null) {
            // 如果用户的主题不存在，取默认主题
            userTheme = ThemeConstants.DEFAULT_USER_THEME;
            FacesUtil.setSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME, userTheme);
        }
        return userTheme;
    }

    public static Map<String, Object> getViewMap() {
        return FacesUtil.getViewRoot().getViewMap();
    }

    public static Object getViewMapValueByKey(String key) {
        return FacesUtil.getViewMap().get(key);
    }

    public static UIViewRoot getViewRoot() {
        return FacesUtil.getCurrentInstance().getViewRoot();
    }

    /**
     * 判断是否为手机浏览器请求
     *
     * @return
     */
    public static boolean isMobileRequest() {
        return FacesUtil.isMobileRequest(FacesUtil.getHttpServletRequest());
    }

    /**
     * 判断是否为手机浏览器请求
     *
     * @return
     */
    public static boolean isMobileRequest(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return userAgent.contains("Mobile");
    }

    public static void putViewMap(String key, Object value) {
        FacesUtil.getViewMap().put(key, value);
    }

    public static String redirect(String url) {
        if (url == null) {
            return url;
        }
        String character = "?";

        if (url.contains("?")) {
            character = "&";
        } else {
            character = "?";
        }
        return (url + character + "faces-redirect=true");
    }

    public static void sendRedirect(String url) {
        try {
            FacesUtil.getExternalContext().redirect(url);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    public static void setRequestAttribute(String name, Object value) {
        FacesUtil.getHttpServletRequest().setAttribute(name, value);
    }

    public static void setSessionAttribute(String name, Object value) {
        FacesUtil.getHttpSession().setAttribute(name, value);
    }

    /**
     * 直接传递页面数据到浏览器
     *
     * @param form
     * @return
     * @throws IOException
     */
    public static String sendHtmlValue(String form) {
        try {
            Writer responseOutputWriter = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputWriter();
            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("text/html;charset=UTF-8");
            responseOutputWriter.write(form);
            responseOutputWriter.flush();
            FacesContext.getCurrentInstance().responseComplete();
            return "pretty:redirect";
        } catch (IOException e) {
            FacesUtil.addErrorMessage("跳转页面失败");
            return "pretty:";
        }
    }
}
