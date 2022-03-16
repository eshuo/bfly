package info.bfly.p2p.loan.exception;

/**
 *
 *
 * Description: 余额不足
 *
 */
public class InsufficientBalance extends Exception {
    private static final long serialVersionUID = -2246755054622029989L;

    public InsufficientBalance() {
    }

    public InsufficientBalance(String msg) {
        super(msg);
    }
}
