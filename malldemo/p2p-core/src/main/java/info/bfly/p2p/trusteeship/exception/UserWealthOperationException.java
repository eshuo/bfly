package info.bfly.p2p.trusteeship.exception;

public class UserWealthOperationException extends RuntimeException {
    private static final long serialVersionUID = -3308801858457843800L;

    public UserWealthOperationException(String msg) {
        super(msg);
    }

    public UserWealthOperationException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserWealthOperationException(Throwable e) {
        super(e);
    }
}
