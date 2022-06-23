package info.bfly.p2p.risk.exception;

/**
 * Description: 区间不连续异常
 */
public class NotContinuityIntervalException extends Exception {
    private static final long serialVersionUID = -5420644008376512038L;

    public NotContinuityIntervalException() {
    }

    public NotContinuityIntervalException(String msg) {
        super(msg);
    }

    public NotContinuityIntervalException(String msg, Throwable e) {
        super(msg, e);
    }
}
