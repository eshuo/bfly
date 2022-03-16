package info.bfly.p2p.repay.exception;

/**
 * Description: 正常还款异常
 *
 */
public class NormalRepayException extends Exception {
    private static final long serialVersionUID = -1055125317467137291L;

    public NormalRepayException(String msg) {
        super(msg);
    }

    public NormalRepayException(String msg, Throwable e) {
        super(msg, e);
    }
}
