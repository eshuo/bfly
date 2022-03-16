package info.bfly.p2p.repay.exception;

/**
 * Description: 提前还款异常
 */
public class AdvancedRepayException extends RuntimeException {
    private static final long serialVersionUID = -8786336283878520437L;

    public AdvancedRepayException(String msg) {
        super(msg);
    }

    public AdvancedRepayException(String msg, Throwable e) {
        super(msg, e);
    }
}
