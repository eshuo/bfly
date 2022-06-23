package info.bfly.core.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 * Description: Filter 的代理类。系统所有的 Filter 共用此一个
 */
public class DelegatingFilterProxy implements Filter {
    private String targetFilterBeanName;
    private String targetFilterBeanClassName;
    private Filter proxy;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        proxy.doFilter(servletRequest, servletResponse, filterChain);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        targetFilterBeanName = config.getInitParameter("targetFilterBeanName");
        targetFilterBeanClassName = config.getInitParameter("targetFilterBeanClassName");
        if (StringUtils.isEmpty(targetFilterBeanClassName) && StringUtils.isEmpty(targetFilterBeanName)) {
            // 两个参数不能都为空
            throw new IllegalArgumentException("targetFilterBeanName and targetFilterBeanClassName can not both be empty!");
        }
        ServletContext servletContext = null;
        servletContext = config.getServletContext();
        WebApplicationContext wac = null;
        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        if (StringUtils.isNotEmpty(targetFilterBeanName)) {
            proxy = (Filter) wac.getBean(targetFilterBeanName);
            if (StringUtils.isNotEmpty(targetFilterBeanClassName) && !StringUtils.equals(proxy.getClass().getName(), targetFilterBeanClassName)) {
                throw new IllegalArgumentException("beanName:" + targetFilterBeanName + " and beanClassName:" + targetFilterBeanClassName + " not point to the same class");
            }
        } else {
            Class clazz;
            try {
                clazz = Class.forName(targetFilterBeanClassName);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("class name of " + targetFilterBeanClassName + " not found");
            }
            proxy = (Filter) wac.getBean(clazz);
        }
        proxy.init(config);
    }
}
