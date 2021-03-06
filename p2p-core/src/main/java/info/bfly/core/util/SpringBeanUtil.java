package info.bfly.core.util;

import org.springframework.web.context.WebApplicationContext;

import static org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext;

public class SpringBeanUtil {
    /**
     * 违背了 Spring 依赖注入思想，慎用。。。
     *
     * @param beanId
     * @return
     */
    public static Object getBeanByName(String beanId) {
        if (beanId == null) {
            return null;
        }
        WebApplicationContext wac = getCurrentWebApplicationContext();
        return wac.getBean(beanId);
    }

    /**
     * 慎用此方法，违背spring的ioc解耦思想。
     */
    public static Object getBeanByType(Class clazz) {
        if (clazz == null) {
            return null;
        }
        WebApplicationContext wac = getCurrentWebApplicationContext();
        return wac.getBean(clazz);
    }
}
