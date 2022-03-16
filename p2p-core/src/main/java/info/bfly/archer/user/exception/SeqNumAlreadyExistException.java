package info.bfly.archer.user.exception;

/**
 * 等级序号已存在
 */
public class SeqNumAlreadyExistException extends Exception {
    private static final long serialVersionUID = -1695378501540854738L;

    public SeqNumAlreadyExistException() {
    }

    public SeqNumAlreadyExistException(String msg) {
        super(msg);
    }

    public SeqNumAlreadyExistException(String msg, Throwable t) {
        super(msg, t);
    }
}
