package info.bfly.p2p.repay.exception;

/**
 * Description: 逾期还款异常
 */
public class OverdueRepayException extends Exception {
    private static final long serialVersionUID = -1505041890038305641L;

    public OverdueRepayException(String msg) {
        super(msg);
    }

    public OverdueRepayException(String msg, Throwable e) {
        super(msg, e);
    }
}
