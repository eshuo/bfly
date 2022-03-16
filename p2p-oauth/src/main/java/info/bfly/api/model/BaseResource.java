package info.bfly.api.model;

import info.bfly.archer.user.model.User;
import info.bfly.archer.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class BaseResource {


    @Autowired
    private UserService userService;


    protected User ensureUserIsAuthorized(String userId) {
        User user = loadUserFromSecurityContext();
        if (user != null && (user.getId().equals(userId) || user.getEmail().equals(userId.toLowerCase()))) {
            return user;
        }
        //throw new ("User not permitted to access this model");
        return null;
    }


    protected User loadUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication requestingUser = (OAuth2Authentication) authentication;
            Object principal = requestingUser.getUserAuthentication().getPrincipal();
            if (principal instanceof User) {
                user = (User) principal;
            } else if (principal instanceof org.springframework.security.core.userdetails.User) {
                //TODO Email Phone And Id
                user = userService.getUserById(((org.springframework.security.core.userdetails.User) principal).getUsername());
            }
        } else
            user = userService.getUserById(authentication.getName());
        return user;
    }

    protected String loadUserIpSecurityContext() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (details instanceof WebAuthenticationDetails) {
            return ((WebAuthenticationDetails) details).getRemoteAddress();
        }
        return "unknown ip";
    }
}
