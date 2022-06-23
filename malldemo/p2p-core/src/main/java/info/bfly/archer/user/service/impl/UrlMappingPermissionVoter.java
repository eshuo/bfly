package info.bfly.archer.user.service.impl;

import com.ocpsoft.pretty.MyPrettyFilter;
import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.reload.PrettyConfigReloader;
import com.ocpsoft.pretty.faces.url.URL;
import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.urlmapping.service.UrlMappingService;
import info.bfly.archer.user.model.Permission;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service("urlMappingPermissionVoter")
public class UrlMappingPermissionVoter implements AccessDecisionVoter {
    @Resource
    private HibernateTemplate    ht;
    @Resource
    private UrlMappingService    urlMappingService;
    private PrettyConfigReloader reloader = new PrettyConfigReloader();

    public PrettyConfig getConfig(HttpServletRequest hreq) {
        // FIXME:目前放在application中，无法做到用户个性化主题
        PrettyConfig pc = (PrettyConfig) hreq.getSession(true).getServletContext().getAttribute(MyPrettyFilter.CONFIG_KEY);
        if (pc == null) {
            if (!PrettyContext.isInstantiated(hreq)) {
                reloader.onNewRequest(hreq.getSession().getServletContext());
            }
            pc = (PrettyConfig) hreq.getSession().getServletContext().getAttribute(PrettyContext.CONFIG_KEY);
            pc = handleUserConfig(pc, hreq);
            hreq.getSession(true).getServletContext().setAttribute(MyPrettyFilter.CONFIG_KEY, pc);
            // hreq.getSession(true).setAttribute(PrettyContext.CONFIG_KEY, pc);
        }
        return pc;
    }

    /**
     * 获取用户独有的prettyConfig
     *
     * @return
     */
    private PrettyConfig handleUserConfig(PrettyConfig pc, HttpServletRequest hreq) {
        String userTheme = (String) hreq.getSession(true).getAttribute(MyPrettyFilter.USER_THEME);
        if (userTheme == null) {
            // 如果用户的主题不存在，取默认主题
            userTheme = ThemeConstants.DEFAULT_USER_THEME;
            hreq.getSession().setAttribute(MyPrettyFilter.USER_THEME, userTheme);
        }
        String themepath = ThemeConstants.THEME_PATH + "/" + userTheme + "/";
        // 从数据库中查询所有的urlMapping，然后产生PerttyConfig对象。
        List<UrlMapping> mappings = new LinkedList<UrlMapping>();
        LinkedList<UrlMapping> ums = urlMappingService.getUrlMappings(pc, themepath, mappings);
        ums.addAll(pc.getMappings());
        ums.addAll(mappings);
        PrettyConfig newPc = new PrettyConfig();
        newPc.setGlobalRewriteRules(pc.getGlobalRewriteRules());
        newPc.setDynaviewId(pc.getDynaviewId());
        newPc.setMappings(ums);
        return newPc;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection attributes) {
        FilterInvocation fi = (FilterInvocation) object;
        UrlMapping mapping = getConfig(fi.getRequest()).getMappingForUrl(new URL(fi.getRequest().getServletPath()));
        if (mapping != null) {
            info.bfly.archer.urlmapping.model.UrlMapping um = ht.get(info.bfly.archer.urlmapping.model.UrlMapping.class, mapping.getId());
            if (um != null) {
                // List<Permission> permissions = um.getPermissions();
                List<Permission> permissions = (List<Permission>) ht.findByNamedQueryAndNamedParam("Permission.findPermissionsByUrlMappingId", "urlMappingId", um.getId());
                if (permissions.size() == 0) {
                    return AccessDecisionVoter.ACCESS_GRANTED;
                }
                Collection<GrantedAuthority> grantedAs = (Collection<GrantedAuthority>) authentication.getAuthorities();
                for (Permission pms : permissions) {
                    for (GrantedAuthority ga : grantedAs) {
                        if (ga.getAuthority().equals(pms.getId())) {
                            return AccessDecisionVoter.ACCESS_GRANTED;
                        }
                    }
                }
                return AccessDecisionVoter.ACCESS_DENIED;
            }
            else {
                return AccessDecisionVoter.ACCESS_GRANTED;
            }
        }
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
}
