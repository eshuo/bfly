package info.bfly.archer.user.service.impl;

import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.user.model.Permission;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * Description: 菜单权限投票器，查看当前用户是否有访问该菜单的权限
 *
 */
@Service("menuPermissionVoter")
public class MenuPermissionVoter implements AccessDecisionVoter {
    @Resource
    private HibernateTemplate ht;

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
        List<Menu> menus = (List<Menu>) ht.findByNamedQueryAndNamedParam("Menu.getMenuByUrl", "url", fi.getRequestUrl());
        if (menus.size() == 0) {
            menus = (List<Menu>) ht.findByNamedQueryAndNamedParam("Menu.getMenuByUrl", "url", fi.getRequest().getServletPath());
        }
        // 查询当前用户是否拥有访问该菜单的权限。
        if (menus.size() > 0) {
            List<Permission> permissions = menus.get(0).getPermissions();
            if (permissions.size() == 0) {
                return AccessDecisionVoter.ACCESS_ABSTAIN;
            }
            for (Permission pms : permissions) {
                for (GrantedAuthority ga : grantedAs) {
                    if (ga.getAuthority().equals(pms.getId())) {
                        return AccessDecisionVoter.ACCESS_GRANTED;
                    }
                }
            }
            return AccessDecisionVoter.ACCESS_DENIED;
        }
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
}
