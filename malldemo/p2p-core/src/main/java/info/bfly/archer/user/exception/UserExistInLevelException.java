package info.bfly.archer.user.exception;

/**
 * 有用户处于该等级，不能删除
 */
public class UserExistInLevelException extends Exception {
    private static final long serialVersionUID = 6973403128239156117L;

    public UserExistInLevelException() {
    }

    public UserExistInLevelException(String msg) {
        super(msg);
    }

    public UserExistInLevelException(String msg, Throwable t) {
        super(msg, t);
    }
}
