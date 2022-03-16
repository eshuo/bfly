package info.bfly.archer.common.exception;

/**
 * Description:认证码过期异常
 *
 */
public class AuthInfoOutOfDateException extends Exception {
    private static final long serialVersionUID = -5292629731347222109L;

    public AuthInfoOutOfDateException(String msg) {
        super(msg);
    }

    public AuthInfoOutOfDateException(String msg, Throwable e) {
        super(msg, e);
    }
}
