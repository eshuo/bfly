package info.bfly.archer.common.exception;

/**
 * Description:认证码不匹配
 *
 */
public class AuthCodeNotMatchException extends Exception {
    private static final long serialVersionUID = -5292629731347222109L;

    public AuthCodeNotMatchException(String msg) {
        super(msg);
    }

    public AuthCodeNotMatchException(String msg, Throwable e) {
        super(msg, e);
    }
}
