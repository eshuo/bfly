package info.bfly.archer.system.service.impl;

import info.bfly.archer.system.service.SpringSecurityService;
import info.bfly.archer.user.service.impl.CustomUserDetailsService;
import info.bfly.core.annotations.ScopeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service(value = "springSecurityService")
@Scope(ScopeType.APPLICATION)
public class SpringSecurityServiceImpl implements SpringSecurityService {
    /**
     * 需要刷新权限的用户名HashSet
     */
    private static HashSet<String> unrgas = new HashSet<String>();
    @Autowired
    SessionRegistry sessionRegistry;
    @Resource
    CustomUserDetailsService customUserDetailsService;

    private void addRefreshAuthoritiesByLoginedUsername(String username, Collection<? extends GrantedAuthority> authorities) {
        List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
        for (Object principal : loggedUsers) {
            if (principal instanceof User) {
                User u = (User) principal;
                if (username.equals(u.getUsername())) {
                    User userN = new User(u.getUsername(), "", u.isEnabled(), u.isAccountNonExpired(), u.isCredentialsNonExpired(), u.isAccountNonLocked(), authorities);
                    userN.eraseCredentials();
                    if (!SpringSecurityServiceImpl.unrgas.contains(username)) {
                        SpringSecurityServiceImpl.unrgas.add(username);
                    }
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionRegistry.registerNewSession(sessionInformation.getSessionId(), userN);
                        }
                    }
                }
            }
        }
    }

    @Override
    public HashSet<String> getUsersNeedRefreshGrantedAuthorities() {
        return SpringSecurityServiceImpl.unrgas;
    }

    @Override
    public void refreshLoginUserAuthorities(String userId) {
        List<GrantedAuthority> authorities = customUserDetailsService.getUserAuthorities(userId);
        // 刷新登录用户权限
        addRefreshAuthoritiesByLoginedUsername(userId, authorities);
    }

    @Override
    public void cleanSpringSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
