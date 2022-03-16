package info.bfly.core.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DelegatingServletProxy extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String  targetServletBeanName;
    private String  targetServletBeanClassName;
    private Servlet proxy;

    private void getServletBean() {
        ServletContext servletContext = getServletContext();
        WebApplicationContext wac = null;
        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        if (StringUtils.isNotEmpty(targetServletBeanName)) {
            proxy = (Servlet) wac.getBean(targetServletBeanName);
            if (StringUtils.isNotEmpty(targetServletBeanClassName) && !StringUtils.equals(proxy.getClass().getName(), targetServletBeanClassName)) {
                throw new IllegalArgumentException("beanName:" + targetServletBeanName + " and beanClassName:" + targetServletBeanClassName + " not point to the same class");
            }
        } else {
            Class clazz;
            try {
                clazz = Class.forName(targetServletBeanClassName);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("class name of " + targetServletBeanClassName + " not found");
            }
            proxy = (Servlet) wac.getBean(clazz);
        }
    }

    @Override
    public void init() throws ServletException {
        targetServletBeanName = getInitParameter("targetServletBeanName");
        targetServletBeanClassName = getInitParameter("targetServletBeanClassName");
        if (StringUtils.isEmpty(targetServletBeanClassName) && StringUtils.isEmpty(targetServletBeanName)) {
            // 两个参数不能都为空
            throw new IllegalArgumentException("targetServletBean and targetServletBeanClassName can not both be empty!");
        }
        getServletBean();
        proxy.init(getServletConfig());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        proxy.service(request, response);
    }
}
