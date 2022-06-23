package info.bfly.archer.user.exception;

/**
 * 该异常出现意味着在当前系统中找不到该角色
 * 
 * @author wanghm
 *
 */
public class RoleNotFoundException extends Exception {
    private static final long serialVersionUID = 5157093449098542746L;

    public RoleNotFoundException(String message) {
        super(message);
    }
}
