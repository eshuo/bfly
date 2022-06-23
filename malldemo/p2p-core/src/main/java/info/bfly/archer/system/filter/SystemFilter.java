package info.bfly.archer.system.filter;

import info.bfly.archer.system.service.AppLocalFilter;
import info.bfly.core.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SystemFilter implements Filter {

    private final static Logger log  = LoggerFactory.getLogger(SystemFilter.class);
    private final static String FILTER_SERVICE_NAME         = "filterServices";
    private final static String FILTER_SERVICE_IMPL_PACKAGE = "info.bfly.archer.system.service.impl";
    private static AppLocalFilter[] filters;

    @Override
    public void destroy() {
        if (SystemFilter.log.isInfoEnabled()) {
            SystemFilter.log.info("SystemFilter destroyed...");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        for (AppLocalFilter appFilter : SystemFilter.filters) {
            AppLocalFilter thisFilter = appFilter;
            thisFilter.doFilter(httpRequest, httpResponse, filter);
        }
        filter.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        if (SystemFilter.log.isInfoEnabled()) {
            SystemFilter.log.info("SystemFilter init start ...");
        }
        final String filterServices = config.getInitParameter(SystemFilter.FILTER_SERVICE_NAME);
        if (SystemFilter.log.isDebugEnabled()) SystemFilter.log.debug("Found services:" + filterServices);
        String[] services = filterServices.split(",");
        SystemFilter.filters = new AppLocalFilter[services.length];
        for (int i = 0; i < services.length; i++) {
            SystemFilter.filters[i] = (AppLocalFilter) SpringBeanUtil.getBeanByName(services[i]);
            if (SystemFilter.filters[i] == null) {
                SystemFilter.log.warn("Not fount Spring bean ,Bean name:" + services[i]);
            } else {
                SystemFilter.log.debug("init systefilter filter:" + SystemFilter.filters[i]);
            }
        }
        if (SystemFilter.log.isInfoEnabled()) {
            SystemFilter.log.info("SystemFilter init started ...");
        }
    }
}
