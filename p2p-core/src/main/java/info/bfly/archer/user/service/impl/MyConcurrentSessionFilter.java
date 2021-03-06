package info.bfly.archer.user.service.impl;

import info.bfly.archer.system.service.SpringSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MyConcurrentSessionFilter extends GenericFilterBean {
    // ~ Instance fields
    // ================================================================================================
    private SessionRegistry       sessionRegistry;
    private String                expiredUrl;
    @Autowired
    private SpringSecurityService springSecurityService;
    private LogoutHandler[]  handlers         = new LogoutHandler[]{new SecurityContextLogoutHandler()};
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public MyConcurrentSessionFilter(SessionRegistry sessionRegistry, String expiredUrl) {
        this.sessionRegistry = sessionRegistry;
        this.expiredUrl = expiredUrl;
    }

    // ~ Methods
    // ========================================================================================================
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(sessionRegistry, "SessionRegistry required");
        Assert.isTrue(expiredUrl == null || UrlUtils.isValidRedirectUrl(expiredUrl), expiredUrl + " isn't a valid redirect URL");
    }

    protected String determineExpiredUrl(HttpServletRequest request, SessionInformation info) {
        return expiredUrl;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionInformation info = sessionRegistry.getSessionInformation(session.getId());
            if (info != null) {
                if (info.isExpired()) {
                    // Expired - abort processing
                    doLogout(request, response);
                    String targetUrl = determineExpiredUrl(request, info);
                    if (targetUrl != null) {
                        redirectStrategy.sendRedirect(request, response, targetUrl);
                        return;
                    } else {
                        response.getWriter().print("This session has been expired (possibly due to multiple concurrent " + "logins being attempted as the same user).");
                        response.flushBuffer();
                    }
                    return;
                } else {
                    // Non-expired - update last request date/time
                    Object principal = info.getPrincipal();
                    if (principal instanceof User) {
                        User userRefresh = (User) principal;
                        ServletContext sc = session.getServletContext();
                        HashSet<String> unrgas = springSecurityService.getUsersNeedRefreshGrantedAuthorities();
                        if (unrgas.size() > 0) {
                            HashSet<String> loginedUsernames = new HashSet<String>();
                            List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
                            for (Object lUser : loggedUsers) {
                                if (lUser instanceof User) {
                                    User u = (User) lUser;
                                    loginedUsernames.add(u.getUsername());
                                }
                            }
                            // ???????????????????????????????????????username
                            for (Iterator iterator = unrgas.iterator(); iterator.hasNext(); ) {
                                String unrgs = (String) iterator.next();
                                if (!loginedUsernames.contains(unrgs)) {
                                    iterator.remove();
                                }
                            }
                            if (unrgas.contains(userRefresh.getUsername())) {
                                // ?????????????????????????????????????????????????????????????????????????????????
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userRefresh, userRefresh.getPassword(), userRefresh.getAuthorities());
                                //???????????????details
                                Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
                                authentication.setDetails(details);
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                unrgas.remove(userRefresh.getUsername());
                            }

                        }

                    }
                    info.refreshLastRequest();
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].logout(request, response, auth);
        }
    }

    public void setExpiredUrl(String expiredUrl) {
        this.expiredUrl = expiredUrl;
    }

    public void setLogoutHandlers(LogoutHandler[] handlers) {
        Assert.notNull(handlers);
        this.handlers = handlers;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
