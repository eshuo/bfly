package info.bfly.archer.common.exception;

/**
 * 认证信息已经被激活
 * 
 * @author Administrator
 *
 */
public class AuthInfoAlreadyInColdException extends Exception {
    private static final long serialVersionUID = -3073510764645177631L;

    public AuthInfoAlreadyInColdException(String msg) {
        super(msg);
    }

    public AuthInfoAlreadyInColdException(String msg, Throwable e) {
        super(msg, e);
    }
}
