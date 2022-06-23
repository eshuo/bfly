package info.bfly.archer.user.exception;

import java.util.Collection;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

/**
 *
 * @version: 1.0
 */
public class MyAccessDeniedException extends AccessDeniedException {
    private static final long serialVersionUID = -4107773575555215933L;
    private Authentication              authentication;
    private Object                      object;
    private Collection<ConfigAttribute> configAttributes;

    public MyAccessDeniedException(String msg, Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
        super(msg);
        this.authentication = authentication;
        this.object = object;
        this.configAttributes = configAttributes;
    }

    public MyAccessDeniedException(String msg, Throwable t, Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
        super(msg, t);
        this.authentication = authentication;
        this.object = object;
        this.configAttributes = configAttributes;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public Collection<ConfigAttribute> getConfigAttributes() {
        return configAttributes;
    }

    public Object getObject() {
        return object;
    }
}
