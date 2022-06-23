package info.bfly.archer.user.exception;


/**
 * 等级积分下限的顺序，与等级序号的顺序，不相匹配
 */
public class MinPointLimitCannotMattchSeqNumException extends Exception {
    private static final long serialVersionUID = 2526769365584413908L;

    public MinPointLimitCannotMattchSeqNumException() {
    }

    public MinPointLimitCannotMattchSeqNumException(String msg) {
        super(msg);
    }

    public MinPointLimitCannotMattchSeqNumException(String msg, Throwable t) {
        super(msg, t);
    }
}
