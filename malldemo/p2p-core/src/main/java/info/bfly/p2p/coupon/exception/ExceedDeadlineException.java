package info.bfly.p2p.coupon.exception;

/**
 * Description: 优惠券超过有效期
 */
public class ExceedDeadlineException extends Exception {
    private static final long serialVersionUID = 7421407974293134145L;

    public ExceedDeadlineException() {
    }

    public ExceedDeadlineException(String msg) {
        super(msg);
    }

    public ExceedDeadlineException(String msg, Throwable e) {
        super(msg, e);
    }
}
