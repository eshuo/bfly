package info.bfly.archer.user.exception;

/**
 * 该异常出现意味着在当前系统中找不到该配置项， 或者当前配置项是非法的
 * 
 * @author wanghm
 *
 */
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3789676841910527261L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
