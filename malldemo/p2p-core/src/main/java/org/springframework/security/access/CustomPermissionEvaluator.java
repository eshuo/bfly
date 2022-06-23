package org.springframework.security.access;

import info.bfly.archer.user.model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

/**
 * Created by XXSun on 5/23/2017.
 */

public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        logger.warn("Denying user " + authentication.getName() + " permission '"
                + permission + "' on object " + targetDomainObject);
        if ("OWNER".equals(permission.toString()) && targetDomainObject instanceof User) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            return StringUtils.equals(userName, ((User) targetDomainObject).getId());
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        logger.warn("Denying user " + authentication.getName() + " permission '"
                + permission + "' on object"+targetType+" with Id '" + targetId);
        return false;
    }
}
