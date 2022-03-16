package info.bfly.p2p.invest.exception;

/**
 * 借款状态非法
 *
 * @author Administrator
 *
 */
public class IllegalLoanStatusException extends Exception {
    private static final long serialVersionUID = 3090545102629192369L;

    public IllegalLoanStatusException() {
    }

    public IllegalLoanStatusException(String msg) {
        super(msg);
    }

    public IllegalLoanStatusException(String msg, Throwable t) {
        super(msg, t);
    }
}
