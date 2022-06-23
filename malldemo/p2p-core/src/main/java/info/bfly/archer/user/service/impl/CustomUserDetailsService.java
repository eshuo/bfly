package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.exception.UserNotFoundException;
import info.bfly.archer.user.model.Permission;
import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XXSun on 2016/11/23.
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Transactional
    public UserDetails loadUserByUsername(String ssoId)
            throws UsernameNotFoundException {
        User user = null;
        try {
            user = userService.getUserByIdWithOutCache(ssoId);
        } catch (UserNotFoundException e) {
            log.debug(e.getMessage());
            throw new UsernameNotFoundException("Username not found");
        }
        log.info("User : {}", user);
        if (user == null) {
            log.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                "1".equals(user.getStatus()), true, true, true, getGrantedAuthorities(user));
    }


    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : user.getRoles()) {
            log.info("UserRole : {}", role);
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getId()));
            }
        }
        log.info("authorities : {}", authorities);
        return authorities;
    }

    public List<GrantedAuthority> getUserAuthorities(String userId) {
        try {
            User user = userService.getUserById(userId);
            return getGrantedAuthorities(user);
        } catch (UserNotFoundException e) {
            log.info("User not found");
        }
        return new ArrayList<GrantedAuthority>();
    }
}
