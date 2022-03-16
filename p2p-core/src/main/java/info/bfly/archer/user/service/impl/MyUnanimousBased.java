package info.bfly.archer.user.service.impl;

import info.bfly.archer.system.service.SpringSecurityService;
import info.bfly.archer.user.exception.MyAccessDeniedException;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.SpringBeanUtil;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MyUnanimousBased extends UnanimousBased {
    @Log
    static Logger log;
    @Resource
    SpringSecurityService springSecurityService;

    /**
     * @param decisionVoters
     */
    public MyUnanimousBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        FilterInvocation fi = (FilterInvocation) object;
        authentication = fresh(authentication, fi.getRequest());
        try {
            super.decide(authentication, object, configAttributes);
        } catch (AccessDeniedException ade) {
            throw new MyAccessDeniedException(ade.getMessage(), authentication, object, configAttributes);
        }
    }

    private Authentication fresh(Authentication authentication, ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionRegistry sessionRegistry = (SessionRegistry) SpringBeanUtil.getBeanByName("sessionRegistry");
            SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
            if (info != null) {
                // Non-expired - update last request date/time
                Object principal = info.getPrincipal();
                if (principal instanceof org.springframework.security.core.userdetails.User) {
                    org.springframework.security.core.userdetails.User userRefresh = (org.springframework.security.core.userdetails.User) principal;
                    ServletContext sc = session.getServletContext();
                    HashSet<String> unrgas = springSecurityService.getUsersNeedRefreshGrantedAuthorities();
                    if (unrgas.size() > 0) {
                        HashSet<String> loginedUsernames = new HashSet<String>();
                        List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
                        for (Object lUser : loggedUsers) {
                            if (lUser instanceof org.springframework.security.core.userdetails.User) {
                                org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) lUser;
                                loginedUsernames.add(u.getUsername());
                            }
                        }
                        // 清除已经下线的但需要刷新的username
                        for (Iterator iterator = unrgas.iterator(); iterator.hasNext(); ) {
                            String unrgs = (String) iterator.next();
                            if (!loginedUsernames.contains(unrgs)) {
                                iterator.remove();
                            }
                        }
                        if (unrgas.contains(userRefresh.getUsername())) {
                            // 如果需要刷新权限的列表中有当前的用户，刷新登录用户权限
                            // FIXME：与springSecurityServiceImpl中的功能，相重复，需重构此方法和springSecurityServiceImpl
                            MyJdbcUserDetailsManager mdudm = (MyJdbcUserDetailsManager) SpringBeanUtil.getBeanByType(MyJdbcUserDetailsManager.class);
                            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userRefresh, userRefresh.getPassword(), mdudm.getUserAuthorities(userRefresh
                                    .getUsername())));
                            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
                            unrgas.remove(userRefresh.getUsername());
                            return SecurityContextHolder.getContext().getAuthentication();
                        }
                    }
                }
            }
        }
        return authentication;
    }
}
