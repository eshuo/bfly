package info.bfly.archer.common.exception;

/**
 * Description:认证码过期异常
 *
 */
public class AuthInfoOutOfTimesException extends Exception {
    private static final long serialVersionUID = -5292629731347222109L;

    public AuthInfoOutOfTimesException(String msg) {
        super(msg);
    }

    public AuthInfoOutOfTimesException(String msg, Throwable e) {
        super(msg, e);
    }
}
