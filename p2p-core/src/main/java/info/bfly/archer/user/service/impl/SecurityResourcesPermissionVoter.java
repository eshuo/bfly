package info.bfly.archer.user.service.impl;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Description: 菜单权限投票器，查看当前用户是否有访问该菜单的权限
 *
 */
@Service("securityResourcesPermissionVoter")
public class SecurityResourcesPermissionVoter implements AccessDecisionVoter {

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
        Collection<GrantedAuthority> grantedAs = (Collection<GrantedAuthority>) authentication.getAuthorities();
        //资料审核员
        for (GrantedAuthority ga : grantedAs) {
            if (ga.getAuthority().equals("USER_BORROW_VERIFY")) {
                return AccessDecisionVoter.ACCESS_GRANTED;
            }
        }
        //当前用户
        //获取当前url
        String requestUrl = fi.getRequestUrl();
        if(requestUrl.startsWith("/bAuth/"+authentication.getName()))
            return AccessDecisionVoter.ACCESS_GRANTED;
        if(requestUrl.startsWith("/bAuth/"))
            return AccessDecisionVoter.ACCESS_DENIED;
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
}
