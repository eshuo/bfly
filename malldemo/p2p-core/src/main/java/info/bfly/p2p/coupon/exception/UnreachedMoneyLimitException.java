package info.bfly.p2p.coupon.exception;

/**
 * Description: 金额未达到优惠券使用条件
 */
public class UnreachedMoneyLimitException extends Exception {
    private static final long serialVersionUID = 7641009428307294917L;

    public UnreachedMoneyLimitException() {
    }

    public UnreachedMoneyLimitException(String msg) {
        super(msg);
    }

    public UnreachedMoneyLimitException(String msg, Throwable e) {
        super(msg, e);
    }
}
