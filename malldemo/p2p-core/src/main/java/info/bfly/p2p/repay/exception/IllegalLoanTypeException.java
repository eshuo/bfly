package info.bfly.p2p.repay.exception;

/**
 * Description: 借款类型异常（计息方式、计息节点之间冲突之类的。。。）
 *
 */
public class IllegalLoanTypeException extends RuntimeException {
    private static final long serialVersionUID = 1320161025586143987L;

    public IllegalLoanTypeException(String msg) {
        super(msg);
    }

    public IllegalLoanTypeException(String msg, Throwable e) {
        super(msg, e);
    }
}
