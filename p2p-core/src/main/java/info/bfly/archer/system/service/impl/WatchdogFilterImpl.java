package info.bfly.archer.system.service.impl;

import info.bfly.archer.config.ConfigConstants;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.system.model.Watchdog;
import info.bfly.archer.system.service.AppLocalFilter;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.SpringBeanUtil;
import info.bfly.core.util.StringManager;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WatchdogFilterImpl implements AppLocalFilter {
    @Log
    static Logger log;
    private final static StringManager sm                = StringManager.getManager(ConfigConstants.Package);
    private static       int           BATCH_INSERT_SIZE = 20;
    LoginUserInfo     loginUserInfo;
    // 这里注入会有问题，在初始化ht之前，就会加载此filter，所以注不进来。
    // @Resource
    HibernateTemplate ht;
    private List<Watchdog> list = new ArrayList<Watchdog>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
        loginUserInfo = (LoginUserInfo) SpringBeanUtil.getBeanByName("loginUserInfo");
        Config config = null;
        try {
            config = ht.get(Config.class, ConfigConstants.Watchdog.IF_OPEN_WATCHDOG);
        } catch (Exception e) {
            WatchdogFilterImpl.log.error(WatchdogFilterImpl.sm.getString("log.error.configWatchdogNotFound", ConfigConstants.Watchdog.IF_OPEN_WATCHDOG, e.toString()));
            log.debug(e.getMessage());
            return;
        }
        String isOpen = ConfigConstants.Watchdog.UN_OPEN_WATCHDOG;
        if (config == null) {
            WatchdogFilterImpl.log.warn(WatchdogFilterImpl.sm.getString("log.notFoundConfig", ConfigConstants.Watchdog.IF_OPEN_WATCHDOG));
        } else {
            isOpen = config.getValue();
        }
        if (isOpen.equals(ConfigConstants.Watchdog.UN_OPEN_WATCHDOG)) {
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURL = httpRequest.getRequestURL().toString();
        if (requestURL.indexOf("/themes/") > 0 || requestURL.indexOf("/static/") > 0 || requestURL.indexOf("javax.faces.model") > 0 || requestURL.indexOf("/upload/") > 0) {
            return;
        }
        Watchdog watchdog = new Watchdog();
        User user = null;
        if (loginUserInfo != null) {
            user = ht.get(User.class, loginUserInfo.getLoginUserId());
        }
        // = (User) httpRequest.getSession().getAttribute(
        // UserConstants.SESSION_KEY_LOGIN_USER);
        if (user != null && user.getId() != null) {
            watchdog.setUserId(user.getId());
        } else {
            watchdog.setUserId("");
        }
        watchdog.setUrl(requestURL);
        watchdog.setReallyUrl(requestURL);
        watchdog.setIp(request.getRemoteAddr());
        watchdog.setTime(new Date());
        list.add(watchdog);
        if (list.size() >= WatchdogFilterImpl.BATCH_INSERT_SIZE) {
            ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
            ht.execute(new HibernateCallback<Boolean>() {
                @Override
                public Boolean doInHibernate(Session session) {
                    for (Watchdog t : list) {
                        session.save(t);
                    }
                    session.flush();
                    session.clear();
                    return null;
                }
            });
            list.clear();
        }
    }
}
